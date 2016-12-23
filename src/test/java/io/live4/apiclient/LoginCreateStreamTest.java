package io.live4.apiclient;

import static java.lang.Integer.MAX_VALUE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSCollections.$map;
import static rx.schedulers.Schedulers.io;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.stjs.javascript.Array;

import com.github.davidmoten.rx.RetryWhen;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import io.live4.apiclient.internal.HttpUtils;
import io.live4.apiclient.internal.RxRequests;
import io.live4.apiclient.internal.UnhandledResponseException;
import io.live4.model.CameraFile;
import io.live4.model.EndOfStream;
import io.live4.model.Hardware;
import io.live4.model.Mission;
import io.live4.model.MissionRole;
import io.live4.model.StreamId;
import io.live4.model.StreamLocation;
import io.live4.model.StreamResponse;
import io.live4.model.User;
import rx.Observable;
import rx.subscriptions.Subscriptions;

public class LoginCreateStreamTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
    String url = "http://10.0.1.105:8042";
    String email = "alex@videogorillas.com";
    String password = "pewpew";
    String missionName = "Demo Mission";
    Hardware hw = Hardware.drone("MyDrone");
    DebugJs noStripAacDebugJs = createNostripAacDebugJs();
    
    @Test
    public void testLoginAndCreateStream() {
        RxApiClient rxApiClient = new RxApiClient(url);
        
        // log in with your email and password:
        Observable<User> userRx = rxApiClient.login(email, password);     

        userRx.toBlocking().subscribe(user -> {
            // select the first organization available for streaming:
            String orgId = user.getFirstOrgId();
            
            // create a new mission in this org:
            Mission mission = createMission(missionName, user, orgId);
            Observable<Mission> missionRx = rxApiClient.createMission(mission);
            
            missionRx.subscribe(m -> {
                // create new stream:
                StreamResponse sr = new StreamResponse();
                sr.title = "Stream from " + user.getName();
                sr.hardwareId = hw.getId();
                Observable<StreamResponse> newStreamRx = rxApiClient.createStream(sr).replay().autoConnect();
                
                // set up the upload:
                Observable<Response> uploadsRx = newStreamRx.concatMap(s -> {
                    StreamId sid = s.sid();
                    OkHttpClient client = rxApiClient.newUploaderClient();

                    // json initial metadata:
                    Observable<Request> debugjs = Observable.just(rxApiClient.uploadJsonRequest(sid, DebugJs.DEBUG_JS, noStripAacDebugJs));
                                        
                    // you should actually set up video uploads here instead of using Observable.empty():
                    Observable<Request> videoUploadsRx = Observable.empty();
                    
                    // locations metadata:
                    Observable<Request> locationUploadsRx = locationUploads(rxApiClient, sid);
                    
                    Observable<Request> uploadRequests = Observable.merge(videoUploadsRx, locationUploadsRx);
                    
//                    uploadRequests = uploadRequests.takeUntil(broadcast.stopStreaming).replay().autoConnect();
//                    
                    // send eos info:
                    Observable<Request> eosUploadsRx = uploadRequests.map(req -> cameraFileFromRequest(req))
                            .toList()
                            .map(files -> eosFromCameraFiles(files))
                            .map(eos -> rxApiClient.uploadJsonRequest(sid, EndOfStream.ENDOFSTREAM_JS, eos));

                    // concat metadata, video data and eos data:
                    Observable<Response> uploads = Observable.concat(debugjs, uploadRequests, eosUploadsRx)
                            .onBackpressureBuffer()
                            .concatMap(request -> {
                                return uploadWithRetry(client, request);
                            });

                    return uploads;
                }); 
            });            
        });
    }
    
    private Observable<Response> uploadWithRetry(OkHttpClient client, Request request) {
        return requestOnlyHeaders(client, request).retryWhen(RetryWhen.exponentialBackoff(200, MILLISECONDS).maxRetries(5).action(_e -> {
            System.out.println("retry " + request.urlString() + " " + _e.durationMs() + " " + _e.throwable().getMessage());
        }).build()).retry();
    }
    
    public Observable<Response> requestOnlyHeaders(final OkHttpClient client, final Request request) {
        return okResponseRx(client, request).doOnNext(r -> {
            if (r == null) return;
            RxRequests.discardBody(r);
        });
    }
    
    public Observable<Response> okResponseRx(OkHttpClient c, Request request) {
        return responseRx(c, request).concatMap(r -> {
            boolean ok = r != null && (r.code() == 200 || r.code() == 206);
            if (ok) {
                return Observable.just(r);
            } else {
                RxRequests.discardBody(r);
                return Observable.<Response>error(new UnhandledResponseException(r));
            }
        });
    }
    
    public Observable<Response> responseRx(OkHttpClient c, Request request) {
        Observable<Response> observable = Observable.create(o -> {
            Call newCall = c.newCall(request);
            AtomicBoolean needCancel = new AtomicBoolean(true);
            o.add(Subscriptions.create(() -> {
                if (needCancel.get() && !newCall.isCanceled()) {
                    newCall.cancel();
                }
            }));
            newCall.enqueue(new Callback() {

                @Override
                public void onResponse(Response response) throws IOException {
                    needCancel.set(false);
                    o.onNext(response);
                    o.onCompleted();
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    needCancel.set(false);
                    o.onError(new RequestException(request, e));
                    o.onCompleted();
                }
            });
        });
        
        return observable.unsubscribeOn(io());
    }
    
    public class RequestException extends IOException {
        private static final long serialVersionUID = 1L;
        private Request request;

        public RequestException(Request request, IOException cause) {
            super(cause);
            this.request = request;
        }

        public Request getRequest() {
            return request;
        }
    }
    
    private Observable<Request> locationUploads(RxApiClient rxApiClient, StreamId sid) {
        Observable<StreamLocation> locationsRx = getLocationsRx();

        Observable<List<StreamLocation>> chunks = locationsRx.buffer(2, SECONDS).filter(list -> !list.isEmpty());

        Observable<Pair<Integer, List<StreamLocation>>> numberedChunks = chunks.zipWith(
                Observable.range(0, MAX_VALUE), (locations, mseq) -> new Pair(mseq, locations));
        numberedChunks = numberedChunks.onBackpressureBuffer();
        
        return numberedChunks.map(p -> {
            int mseq = p.left;
            List<StreamLocation> locations = p.right;
            String filename = locationsFilename(mseq);
            return rxApiClient.uploadJsonRequest(sid, filename, Collections.singletonMap("locations", locations));
        });
    }
    
    private Observable<StreamLocation> getLocationsRx() {
        // we are using fake location here:
        StreamLocation loc = StreamLocation.latLng(sdf.format(new Date()), 42, 42);
        loc.altitude = new Double(42);
        loc.course = new Double(42);
        loc.horizontalAccuracy = new Double(42);
        loc.verticalAccuracy = new Double(42);
        loc.speed = new Double(42);
        
        return Observable.just(loc).repeat();
    }
    
    private String locationsFilename(int mseq) {
        return "locations" + mseq + ".js";
    }
    
    private CameraFile cameraFileFromRequest(Request req) {
        String name = new File(req.url().getFile()).getName();
        CameraFile cf = new CameraFile(name, name);
        cf.lastModified = req.header(HttpUtils.LAST_MODIFIED);
        try {
            cf.size = String.valueOf(req.body().contentLength());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return cf;
    }
    
    private EndOfStream eosFromCameraFiles(List<CameraFile> files) {
        EndOfStream eos = new EndOfStream();
        eos.files = new Array<>(files);
        return eos;
    }
    
    private Mission createMission(String missionName, User me, String orgId) {
        Mission mission = new Mission();
        mission.createdByUserId = me.id;
        mission.startTime = new org.stjs.javascript.Date();
        long oneDay = 24 * 60 * 60 * 1000;
        mission.endTime = new org.stjs.javascript.Date(mission.startTime.getTime() + oneDay);
        mission.hardware = $array();
        mission.joined = $map();
        mission.name = missionName;
        mission.orgId = orgId;
        mission.state = Mission.State.PENDING;
        mission.streamIds = $array();
        mission.addUser(me, MissionRole.OWNER);
        return mission;
    }
    
    private DebugJs createNostripAacDebugJs() {
        DebugJs d = new DebugJs();
        d.stripaac = false;
        return d;
    }
    
    public class DebugJs {
        public static final String DEBUG_JS = "debug.js";
        public static final String DEBUG_JS_GZ = "debug.js.gz";

        public String url;
        public String dcim;
        public byte[] statusArray;
        
        //strip 3 aac frames from every mp4 file (aac decoder reset issue)
        public boolean stripaac;
    }
    
    public class Pair<K, V> {
        public final K left;
        public final V right;

        public Pair(K left, V right) {
            this.left = left;
            this.right = right;
        }
    }
}
