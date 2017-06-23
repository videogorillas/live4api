package io.live4.apiclient;

import static com.squareup.okhttp.ws.WebSocket.TEXT;
import static io.live4.apiclient.internal.HttpUtils.GET;
import static io.live4.apiclient.internal.HttpUtils.LAST_MODIFIED;
import static io.live4.apiclient.internal.HttpUtils.httpDateFormat;
import static io.live4.apiclient.internal.RxRequests.okResponseRx;
import static io.live4.apiclient.internal.RxRequests.requestString;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import io.live4.api3.Api3MissionUrls;
import io.live4.api3.Api3Urls;
import io.live4.api3.Api3UserUrls;
import io.live4.apiclient.internal.RxWebSocket;
import io.live4.model.Hardware;
import io.live4.model.LiveMessage;
import io.live4.model.Mission;
import io.live4.model.Organization;
import io.live4.model.Stream;
import io.live4.model.StreamId;
import io.live4.model.StreamLocation;
import io.live4.model.StreamResponse;
import io.live4.model.TwilioToken;
import io.live4.model.User;
import rx.Observable;
import rx.Subscription;

public class RxApiClient {
    
    private static final long WS_SEND_TEXT_TIMEOUT = 2000L;
    private Observable<LiveMessage> _liveMessages;
    private ConcurrentSkipListSet<String> subscribeMessages = new ConcurrentSkipListSet<>();

    private OkHttpClient httpClient;
    final ServerUrl serverUrl;

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
    public ApiRequest request;

    public RxApiClient(String serverUrl, Gson gson, OkHttpClient httpClient) {
        this.serverUrl = new ServerUrl(serverUrl);
        this.gson = gson;
        this.httpClient = httpClient;
        request = new ApiRequest(this.serverUrl, gson);
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
    
    public ServerUrl getServerUrl() {
        return serverUrl;
    }

    public Observable<Boolean> uploadLogs(String logs) {
        return okResponseRx(httpClient, request.uploadLogs(logs)).map(r -> r.isSuccessful());
    }

    public OkHttpClient newUploaderClient() {
        return new OkHttpClient();
    }

    public OkHttpClient getApiClient() {
        return httpClient;
    }

    public Observable<User> resetPassword(String email) {
        return requestString(getApiClient(), request.resetPassword(email))
                .concatMap(json -> fromJsonRx(json, User.class));
    }

    public Observable<User> getUser(String userId) {
        return requestString(getApiClient(), request.getUser(userId))
                .concatMap(json -> fromJsonRx(json, User.class));
    }

    public Observable<User> getUserByMissionToken(String token) {
        Request get = GET(serverUrl + Api3MissionUrls.getUserByMissionToken(token));
        return requestString(getApiClient(), get)
                .concatMap(json -> fromJsonRx(json, User.class));
    }

    public Observable<Organization> getOrganization(String orgId) {
        return requestString(getApiClient(), request.getOrganization(orgId))
                .concatMap(json -> fromJsonRx(json, Organization.class));
    }

    public Observable<Boolean> isTokenValid(String token) {
        Request.Builder builder = new Request.Builder().header(LAST_MODIFIED, httpDateFormat(new Date().getTime()));
        builder.url(serverUrl + Api3MissionUrls.checkTokenUrl(token)).get();
        Request r = builder.build();
        return requestString(getApiClient(), r).concatMap(json -> fromJsonRx(json, Boolean.class));
    }

    public Observable<Boolean> isUserTemp(String email) {
        Request.Builder builder = new Request.Builder().header(LAST_MODIFIED, httpDateFormat(new Date().getTime()));
        builder.url(serverUrl + Api3UserUrls.isUserTemp(email)).get();
        Request r = builder.build();
        return requestString(getApiClient(), r).concatMap(json -> fromJsonRx(json, Boolean.class));
    }

    public Observable<User> login(String email, String password) {
        return requestString(getApiClient(), request.login(email, password))
                .concatMap(json -> fromJsonRx(json, User.class));
    }

    public Observable<StreamResponse> createStream(Stream sr) {
        return requestString(getApiClient(), request.createStream(sr))
                .concatMap(json -> fromJsonRx(json, StreamResponse.class));
    }

    public Observable<Mission> listMissions(String orgId) {
        return requestString(getApiClient(), request.listMissions(orgId))
                .concatMap(json -> fromJsonRx(json, Mission[].class))
                .concatMap(arr -> Observable.from(arr));
    }

    public Observable<Mission> getMission(String id) {
        return requestString(getApiClient(), request.getMission(id)).concatMap(json -> fromJsonRx(json, Mission.class));
    }

    public Observable<StreamResponse> getStream(StreamId id) {
        return requestString(getApiClient(), request.getStream(id))
                .concatMap(json -> fromJsonRx(json, StreamResponse.class));
    }

    public Observable<StreamLocation> getLocations(StreamId id) {
        return requestString(getApiClient(), request.getLocations(id))
                .concatMap(json -> fromJsonRx(json, StreamLocation[].class))
                .concatMap(arr -> Observable.from(arr));
    }

    public Observable<Mission.ShareToken> getShareToken(String missionId) {
        return requestString(getApiClient(), GET(serverUrl.shareToken(missionId)))
                .concatMap(json -> fromJsonRx(json, Mission.ShareToken.class));
    }

    public Observable<TwilioToken> requestChatToken() {
        return requestString(getApiClient(), request.accessToken())
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

    String gsonToString(Object o) {
        return gson.toJson(o);
    }

    public Observable<Hardware> createHw(Hardware hw) {
        return requestString(getApiClient(), request.createHw(hw)).concatMap(json -> fromJsonRx(json, Hardware.class));
    }

    public Observable<Hardware> getHwByExternalId(String externalId, String orgId) {
        Observable<Hardware> concatMap = requestString(getApiClient(), request.listHw(orgId))
                .concatMap(json -> fromJsonRx(json, Hardware[].class))
                .concatMap(arr -> Observable.from(arr))
                .filter(_hw -> externalId.equals(_hw.externalId));
        return concatMap;
    }

    public Observable<Mission> createMission(Mission mission) {
        return requestString(getApiClient(), request.createMission(mission))
                .concatMap(json -> fromJsonRx(json, Mission.class));
    }

    public Observable<Mission> updateMission(Mission mission) {
        return requestString(getApiClient(), request.updateMission(mission))
                .concatMap(json -> fromJsonRx(json, Mission.class));
    }

    public Observable<StreamResponse> updateStreamTitle(String streamId, String title) {
        return requestString(getApiClient(), request.updateStreamTitle(streamId, title))
                .concatMap(json -> fromJsonRx(json, StreamResponse.class));
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

    public Observable<Stream> updateStream(Stream stream, Consumer<Stream> resolveConflict){
        return null;
    }

    private void error(Object msg) {
        System.err.println(msg);
    }

    private void debug(Object string) {
//        System.out.println(string);
    }
}