package io.live4.apiclient;

import static com.squareup.okhttp.ws.WebSocket.TEXT;
import static io.live4.api2.Api2Urls.API_2_UPLOAD_AV;
import static io.live4.apiclient.HttpUtils.GET;
import static io.live4.apiclient.HttpUtils.JSON_MIMETYPE;
import static io.live4.apiclient.HttpUtils.LAST_MODIFIED;
import static io.live4.apiclient.HttpUtils.OCTET_STREAM;
import static io.live4.apiclient.HttpUtils.httpDateFormat;
import static io.live4.apiclient.RxRequests.requestString;
import static io.live4.apiclient.RxRequests.webSocket;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.schedulers.Schedulers.newThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ws.WebSocket;

import io.live4.api1.Api1StreamUrls;
import io.live4.api2.Api2Urls;
import io.live4.api3.Api3HwUrls;
import io.live4.api3.Api3MissionUrls;
import io.live4.api3.Api3Urls;
import io.live4.model.Hardware;
import io.live4.model.LiveMessage;
import io.live4.model.LoginRequestData;
import io.live4.model.Mission;
import io.live4.model.StreamId;
import io.live4.model.StreamResponse;
import io.live4.model.User;
import rx.Observable;
import rx.subjects.ReplaySubject;

public class RxApiClient {
    
    private String serverUrl;
    private ReplaySubject<WebSocket> _ws = ReplaySubject.create(1);
    private Observable<LiveMessage> _liveMessages;

    private OkHttpClient httpClient;

    private Gson gson;

    public RxApiClient(String serverUrl) {
        this(serverUrl, ApiGson.gson(), newHttpClient());
    }
    
    private static OkHttpClient newHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        CookieManager cookieHandler = new CookieManager();
        cookieHandler.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient.setCookieHandler(cookieHandler);
        httpClient.setReadTimeout(30, TimeUnit.SECONDS);
        return httpClient;
    }

    public RxApiClient(String serverUrl, Gson gson, OkHttpClient httpClient) {
        this.serverUrl = serverUrl;
        this.gson = gson;
        this.httpClient = httpClient;

        this._liveMessages = webSocket(getApiClient(), GET(serverUrl + Api3Urls.API_3_WSUPDATES + "/"),
                (ws, x) -> _ws.onNext(ws))
                        .concatMap(json -> fromJsonRx(json, LiveMessage.class))
                        .retryWhen(e -> e.doOnNext(err -> System.err.println("retry " + err)).delay(1, SECONDS))
                        .repeatWhen(e -> e.delay(1, SECONDS))
                        .share();
    }

    public Request uploadJsonRequest(StreamId sid, String filename, Object o) {
        return uploadJsonRequest(sid, filename, System.currentTimeMillis(), gsonToString(o));
    }

    public Request uploadJsonRequest(StreamId sid, String filename, long mtime, String json) {
        String buildUrl = String.format(serverUrl + API_2_UPLOAD_AV + "/%s/%s/%s", sid.userId, sid.streamId, filename);
        Request.Builder builder = new Request.Builder().header(LAST_MODIFIED, httpDateFormat(mtime));
        try {
            builder.url(buildUrl + ".gz").post(RequestBody.create(OCTET_STREAM, gzip(json)));
        } catch (IOException e) {
            builder.url(buildUrl).post(RequestBody.create(JSON_MIMETYPE, json));
        }

        return builder.build();
    }

    public Request uploadFileRequest(StreamId sid, File file) {
        String buildUrl = String.format(serverUrl + API_2_UPLOAD_AV + "/%s/%s/%s", sid.userId, sid.streamId, file.getName());
        Request.Builder builder = new Request.Builder().header(LAST_MODIFIED, httpDateFormat(file.lastModified()));
        builder.url(buildUrl).post(RequestBody.create(OCTET_STREAM, file));
        return builder.build();
    }

    private static byte[] gzip(String json) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GZIPOutputStream gz = new GZIPOutputStream(os);
        gz.write(json.getBytes());
        gz.finish();
        gz.close();
        return os.toByteArray();
    }

    public OkHttpClient newUploaderClient() {
        return new OkHttpClient();
    }

    public OkHttpClient getApiClient() {
        return httpClient;
    }

    public Request loginRequest(String email, String password) {
        LoginRequestData lrd = new LoginRequestData(email, password);
        return HttpUtils.postAsJsonRequest(serverUrl + Api3Urls.API_3_LOGIN, gsonToString(lrd));
    }

    public Request createStreamRequest(StreamResponse sr) {
        return HttpUtils.postAsJsonRequest(serverUrl + Api2Urls.API_2_STREAM, gsonToString(sr));
    }

    public Observable<User> login(String email, String password) {
        return requestString(getApiClient(), loginRequest(email, password))
                .concatMap(json -> fromJsonRx(json, User.class));
    }

    public Observable<StreamResponse> createStream(StreamResponse sr) {
        return requestString(getApiClient(), createStreamRequest(sr))
                .concatMap(json -> fromJsonRx(json, StreamResponse.class));
    }

    public Request listMissionsRequest(String orgId) {
        return GET(serverUrl + Api3MissionUrls.listUrl(orgId));
    }

    public Request getMissionRequest(String id) {
        return GET(serverUrl + Api3MissionUrls.getUrl(id));
    }

    public Request listHwRequest(String orgId) {
        return GET(serverUrl + Api3HwUrls.listUrl(orgId));
    }

    public Request createHwRequest(Hardware hw) {
        return HttpUtils.postAsJsonRequest(serverUrl + Api3HwUrls.createUrl(), gsonToString(hw));
    }

    public Request createMissionRequest(Mission m) {
        return HttpUtils.postAsJsonRequest(serverUrl + Api3MissionUrls.createUrl(), gsonToString(m));
    }

    public Request updateMissionRequest(Mission m) {
        return HttpUtils.putAsJsonRequest(serverUrl + Api3MissionUrls.updateUrl(), gsonToString(m));
    }

    public Observable<Mission> listMissions(String orgId) {
        return requestString(getApiClient(), listMissionsRequest(orgId))
                .concatMap(json -> fromJsonRx(json, Mission[].class))
                .concatMap(arr -> Observable.from(arr));
    }

    public Observable<Mission> getMission(String id) {
        return requestString(getApiClient(), getMissionRequest(id)).concatMap(json -> fromJsonRx(json, Mission.class));
    }

    public Observable<StreamResponse> getStream(StreamId id) {
        return requestString(getApiClient(), getStreamRequest(id))
                .concatMap(json -> fromJsonRx(json, StreamResponse.class));
    }

    public Request getStreamRequest(StreamId id) {
        return GET(serverUrl + Api1StreamUrls.getUrl(id.toString()));
    }

    public Observable<Mission.ShareToken> getShareToken(String missionId) {
        return requestString(getApiClient(), GET(serverUrl + Api3MissionUrls.tokenUrl(missionId)))
                .concatMap(json -> fromJsonRx(json, Mission.ShareToken.class));
    }

    public <T> Observable<T> fromJsonRx(String json, Class<T> cls) {
        try {
            return Observable.just(fromJson(json, cls));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    private <T> T fromJson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    private String gsonToString(Object o) {
        return gson.toJson(o);
    }

    public Observable<Hardware> createHw(Hardware hw) {
        return requestString(getApiClient(), createHwRequest(hw)).concatMap(json -> fromJsonRx(json, Hardware.class));
    }

    public Observable<Hardware> getHwByExternalId(String externalId, String orgId) {
        Observable<Hardware> concatMap = requestString(getApiClient(), listHwRequest(orgId))
                .concatMap(json -> fromJsonRx(json, Hardware[].class))
                .concatMap(arr -> Observable.from(arr))
                .filter(_hw -> externalId.equals(_hw.externalId));
        return concatMap;
    }

    public Observable<Mission> createMission(Mission mission) {
        return requestString(getApiClient(), createMissionRequest(mission))
                .concatMap(json -> fromJsonRx(json, Mission.class));
    }

    public Observable<Mission> updateMission(Mission mission) {
        return requestString(getApiClient(), updateMissionRequest(mission))
                .concatMap(json -> fromJsonRx(json, Mission.class));
    }

    public Observable<LiveMessage> streamUpdates(StreamId sid) {
        wsSendText(gsonToString(LiveMessage.subscribeStream(sid.toString())));
        return _liveMessages.filter(lm -> sid.toString().equals(lm.streamId));
    }

    public Observable<Mission> missionUpdates() {
        wsSendText(gsonToString(LiveMessage.subscribe("mission")));
        return _liveMessages.filter(lm -> lm.mission != null).map(lm -> lm.mission);
    }

    private void wsSendText(String msg) {
        Observable<WebSocket> websocketReady = _ws.take(1).subscribeOn(newThread());
        websocketReady.zipWith(Observable.just(msg), (ws, _msg) -> {
            try {
                ws.sendMessage(RequestBody.create(TEXT, _msg));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }).subscribe(x -> {
        }, err -> err.printStackTrace());
    }

}
