package io.live4.apiclient;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import io.live4.api3.Api3Urls;
import io.live4.apiclient.internal.RxWebSocket;
import io.live4.apiclient.internal.UnhandledResponseException;
import io.live4.model.*;
import rx.Observable;
import rx.Subscription;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.squareup.okhttp.ws.WebSocket.TEXT;
import static io.live4.apiclient.internal.HttpUtils.GET;
import static io.live4.apiclient.internal.RxRequests.okResponseRx;
import static io.live4.apiclient.internal.RxRequests.requestString;

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
    private Subscription subscription;
    public final ApiRequest request;

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

    public Observable<User> resetPassword(String login, String password, String token) {
        return requestObject(request.resetPassword(login, password, token), User.class);
    }

    public Observable<User> getUser(String userId) {
        return requestObject(request.getUser(userId), User.class);
    }

    public Observable<User> createLoggedExternalUserByMissionToken(String token, String email, String name) {
        return requestObject(request.createLoggedExternalUserByMissionToken(token, email, name), User.class);
    }

    public Observable<String> getOrgIdByMissionToken(String token) {
        return requestObject(request.getOrgIdByMissionToken(token), String.class);
    }

    public Observable<User> addExternalProfileByMissionToken(User user, String token) {
        return requestObject(request.addExternalProfileByMissionToken(user, token), User.class);
    }

    public Observable<User> joinMissionByToken(User user, String token) {
        return requestObject(request.joinMissionByToken(user, token), User.class);
    }

    public Observable<User> inviteToMission(User user, String missionId) {
        return requestObject(request.inviteToMission(user, missionId), User.class);
    }

    public Observable<Organization> getOrganization(String orgId) {
        return requestObject(request.getOrganization(orgId), Organization.class);
    }

    public Observable<Boolean> isTokenValid(String token) {
        return requestObject(request.isTokenValid(token), Boolean.class);
    }

    public Observable<Boolean> isUserTemp(String email) {
        return requestObject(request.isUserTemp(email), Boolean.class);
    }

    public Observable<User> login(String email, String password) {
        return requestObject(request.login(email, password), User.class);
    }

    public Observable<StreamResponse> createStream(Stream sr) {
        return requestObject(request.createStream(sr), StreamResponse.class);
    }

    public Observable<Mission> listMissions(String orgId) {
        return requestObject(request.listMissions(orgId), Mission[].class).concatMap(arr -> Observable.from(arr));
    }

    public Observable<Mission> getMission(String id) {
        return requestObject(request.getMission(id), Mission.class);
    }

    public Observable<StreamResponse> getStream(StreamId id) {
        return requestObject(request.getStream(id), StreamResponse.class);
    }

    public Observable<StreamLocation> getLocations(StreamId id) {
        return requestObject(request.getLocations(id), StreamLocation[].class).concatMap(arr -> Observable.from(arr));
    }

    public Observable<MissionShareToken> getShareToken(String missionId) {
        return requestObject(GET(serverUrl.shareToken(missionId)), MissionShareToken.class);
    }

    public Observable<TwilioToken> requestChatToken() {
        return requestObject(request.accessToken(), TwilioToken.class);
    }

    public Observable<TwilioToken> requestAudioChatToken() {
        return requestObject(request.accessAudioToken(), TwilioToken.class);
    }

    public <T> Observable<T> fromJsonRx(String json, Class<T> cls) {
        return Observable.fromCallable(() -> gson.fromJson(json, cls));
    }

    String gsonToString(Object o) {
        return gson.toJson(o);
    }

    public Observable<Hardware> createHw(Hardware hw) {
        return requestObject(request.createHw(hw), Hardware.class);
    }

    public Observable<Hardware> getHw(String hwId) {
        return requestObject(request.getHw(hwId), Hardware.class);
    }

    public Observable<Hardware> getHwByExternalId(String externalId, String orgId) {
        return requestObject(request.listHw(orgId), Hardware[].class)
                .concatMap(arr -> Observable.from(arr))
                .filter(_hw -> externalId.equals(_hw.externalId));
    }

    public Observable<Mission> createMission(Mission mission) {
        return requestObject(request.createMission(mission), Mission.class);
    }

    public Observable<Mission> updateMission(Mission mission) {
        return requestObject(request.updateMission(mission), Mission.class);
    }

    public Observable<StreamResponse> updateStreamTitle(String streamId, String title) {
        return requestObject(request.updateStreamTitle(streamId, title), StreamResponse.class);
    }

    private <T> Observable<T> requestObject(Request req, Class<T> cls) {
        return requestString(getApiClient(), req).concatMap(json -> fromJsonRx(json, cls));
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

    public Observable<Stream> getAndUpdateStream(StreamId sid, Consumer<Stream> transformer) {
        return getStream(sid).concatMap(stream -> {
            transformer.accept(stream);
            return requestObject(request.updateStream(stream), Stream.class);
        });
    }

    public Observable<String> getServerApiVersion() {
        return requestObject(request.getServerApiVersion(), String.class)
                .onErrorResumeNext(err -> {
                    if (err instanceof UnhandledResponseException && ((UnhandledResponseException) err).getResponse().code() == 404) {
                        return Observable.just("3.4.1");
                    } else {
                        return Observable.error(err);
                    }
                });
    }

    private void error(Object msg) {
        System.err.println(msg);
    }

    private void debug(Object string) {
//        System.out.println(string);
    }
}