package io.live4.apiclient;

import static com.squareup.okhttp.ws.WebSocket.TEXT;
import static io.live4.api2.Api2Urls.API_2_UPLOAD_AV;
import static io.live4.api2.Api2Urls.API_2_UPLOAD_LOG;
import static io.live4.apiclient.internal.HttpUtils.GET;
import static io.live4.apiclient.internal.HttpUtils.JSON_MIMETYPE;
import static io.live4.apiclient.internal.HttpUtils.LAST_MODIFIED;
import static io.live4.apiclient.internal.HttpUtils.OCTET_STREAM;
import static io.live4.apiclient.internal.HttpUtils.httpDateFormat;
import static io.live4.apiclient.internal.RxRequests.okResponseRx;
import static io.live4.apiclient.internal.RxRequests.requestString;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import io.live4.api1.Api1StreamUrls;
import io.live4.api2.Api2Urls;
import io.live4.api3.Api3HwUrls;
import io.live4.api3.Api3MissionUrls;
import io.live4.api3.Api3Urls;
import io.live4.api3.Api3UserUrls;
import io.live4.apiclient.internal.HttpUtils;
import io.live4.apiclient.internal.RxWebSocket;
import io.live4.model.Hardware;
import io.live4.model.LiveMessage;
import io.live4.model.LoginRequestData;
import io.live4.model.Mission;
import io.live4.model.StreamId;
import io.live4.model.StreamLocation;
import io.live4.model.StreamResponse;
import io.live4.model.TwilioToken;
import io.live4.model.User;
import rx.Observable;
import rx.Subscription;

public class RxApiClient {
    
    private static final long WS_SEND_TEXT_TIMEOUT = 2000L;
    private String serverUrl;
    private Observable<LiveMessage> _liveMessages;
    private ConcurrentSkipListSet<String> subscribeMessages = new ConcurrentSkipListSet<>();

    private OkHttpClient httpClient;

    private Gson gson;
    private RxWebSocket rxws;

    public RxApiClient(String serverUrl) {
        this(serverUrl.replaceAll("/$", ""), ApiGson.gson(), newHttpClient());
    }
    
    private static OkHttpClient newHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        CookieManager cookieHandler = new CookieManager();
        cookieHandler.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient.setCookieHandler(cookieHandler);
        httpClient.setReadTimeout(30, TimeUnit.SECONDS);
        
        List<Protocol> protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        httpClient.setProtocols(protocolList);
       
        return httpClient;
    }
    Subscription subscription;
    
    public RxApiClient(String serverUrl, Gson gson, OkHttpClient httpClient) {
        this.serverUrl = serverUrl;
        this.gson = gson;
        this.httpClient = httpClient;
        
        rxws = RxWebSocket.createRxWebSocket(httpClient, serverUrl + Api3Urls.API_3_WSUPDATES + "/", 1000);
       
        this._liveMessages = rxws
                .getMessages()
                .concatMap(json -> fromJsonRx(json, LiveMessage.class))
                .doOnSubscribe(() -> {
                    subscription = rxws.getOpenWebSockets().subscribe(ws -> {
                        debug("open: " + ws);
                        for(String msg : subscribeMessages) {
                            debug(msg);
                            try {
                                ws.sendMessage(RequestBody.create(TEXT, msg));
                            } catch (IOException e1) {
                                error(e1);
                            }
                        }
                    });
                })
                .doOnUnsubscribe(() -> {
                    if (subscription != null && !subscription.isUnsubscribed()) {
                        debug("unsubscribe api client");
                        subscription.unsubscribe();
                    }
                }).share();
    }
    
    public String getServerUrl() {
        return serverUrl;
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
    
    public Request uploadLogsRequest(String logs) {
        Request.Builder builder = new Request.Builder().header(LAST_MODIFIED, httpDateFormat(new Date().getTime()));
        builder.url(serverUrl + API_2_UPLOAD_LOG + "/" + System.currentTimeMillis()).post(RequestBody.create(HttpUtils.OCTET_STREAM, logs));
        return builder.build();
    }
    
    public Observable<Boolean> uploadLogs(String logs) {
        return okResponseRx(httpClient, uploadLogsRequest(logs)).map(r -> r.isSuccessful());
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
    
    public String getWsPushVideoUrl(StreamId id) {
        return serverUrl + Api3Urls.wsPushVideo(id.toString());
    }

    public Request loginRequest(String email, String password) {
        LoginRequestData lrd = new LoginRequestData(email, password);
        return HttpUtils.postAsJsonRequest(serverUrl + Api3Urls.API_3_LOGIN, gsonToString(lrd));
    }
    
    public Request getUserRequest(String userId) {
        return GET(serverUrl + Api3UserUrls.getUrl(userId));
    }
    
    public Observable<User> getUser(String userId) {
        return requestString(getApiClient(), getUserRequest(userId))
                .concatMap(json -> fromJsonRx(json, User.class));
    }

    public Request createStreamRequest(StreamResponse sr) {
        return HttpUtils.postAsJsonRequest(serverUrl + Api2Urls.API_2_STREAM, gsonToString(sr));
    }
    
    public Request accessTokenRequest() {
        return HttpUtils.GET(serverUrl + Api3MissionUrls.CHAT_TOKEN);
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

    public String getShareMissionUrl(Mission.ShareToken shareToken) {
        return serverUrl + Api3MissionUrls.shareMissionUrl(shareToken.missionId, shareToken.token);
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
        return GET(getStreamUrl(id));
    }

    public String getStreamUrl(StreamId id) {
        return serverUrl + Api1StreamUrls.getUrl(id.toString());
    }

    public Observable<StreamLocation> getLocations(StreamId id) {
        return requestString(getApiClient(), getLocationsRequest(id))
                .concatMap(json -> fromJsonRx(json, StreamLocation[].class))
                .concatMap(arr -> Observable.from(arr));
    }

    public Request getLocationsRequest(StreamId id) {
        return GET(getLocationsUrl(id));
    }
    
    public String getLocationsUrl(StreamId id) {
        return serverUrl + Api3Urls.locationsUrl(id.toString());
    }

    public Observable<Mission.ShareToken> getShareToken(String missionId) {
        return requestString(getApiClient(), GET(serverUrl + Api3MissionUrls.tokenUrl(missionId)))
                .concatMap(json -> fromJsonRx(json, Mission.ShareToken.class));
    }
    
    public Observable<TwilioToken> requestChatToken() {
        return requestString(getApiClient(), accessTokenRequest())
                .concatMap(json -> fromJsonRx(json, TwilioToken.class));
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
        String sub = gsonToString(LiveMessage.subscribeStream(sid.toString()));
        Observable<String> sent = rxws.sendText(sub, WS_SEND_TEXT_TIMEOUT).take(1).doOnNext(x -> subscribeMessages.add(sub));
        Observable<LiveMessage> updates = sent.concatMap(x -> _liveMessages.filter(lm -> sid.toString().equals(lm.streamId)));
        return updates.doOnUnsubscribe(() -> subscribeMessages.remove(sub));
    }

    public Observable<Mission> missionUpdates() {
        String sub = gsonToString(LiveMessage.subscribe("mission"));
        Observable<String> sent = rxws.sendText(sub, WS_SEND_TEXT_TIMEOUT).take(1).doOnNext(x -> subscribeMessages.add(sub));

        Observable<Mission> updates = sent
                .concatMap(x -> _liveMessages.filter(lm -> lm.mission != null).map(lm -> lm.mission));
        return updates.doOnUnsubscribe(() -> subscribeMessages.remove(sub));
    }

    private void error(Object msg) {
        System.err.println(msg);
    }

    private void debug(Object string) {
//        System.out.println(string);
    }
}