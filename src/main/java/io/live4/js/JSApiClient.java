package io.live4.js;

import static io.live4.api3.Api3Urls.API_3_LOGIN;
import static io.live4.api3.Api3Urls.API_3_LOGOUT;
import static io.live4.api3.Api3Urls.API_3_RESETPASSWORD;
import static io.live4.api3.Api3Urls.API_3_WSUPDATES;
import static io.live4.model.Internal.isBlank;
import static org.stjs.javascript.Global.console;
import static org.stjs.javascript.Global.window;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.JSON;
import static org.stjs.javascript.JSGlobal.typeof;

import org.stjs.javascript.Date;
import org.stjs.javascript.Error;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.websocket.WebSocket;

import com.vg.js.bridge.Rx;
import com.vg.js.bridge.Rx.Observable;

import io.live4.api3.Api3OrgUrls;
import io.live4.api3.Api3Urls;
import io.live4.js.internal.Requests;
import io.live4.js.internal.Typefy;
import io.live4.js.internal.WSLive;
import io.live4.js.internal.WSLiveSession;
import io.live4.model.Calendar;
import io.live4.model.Hardware;
import io.live4.model.Internal;
import io.live4.model.LoginRequest;
import io.live4.model.NewOrgAdminProfile;
import io.live4.model.Organization;
import io.live4.model.TimeInterval;
import io.live4.model.User;
import io.live4.model.UserProfile;

@Namespace("live4api")
public class JSApiClient {
    
    public StreamApi streams;
    public MissionApi missions;
    public UserApi users;
    public OrgApi orgs;
    public CalendarApi calendars;
    
    public HardwareApi hw;
    public HWStatusApi hwStatus;
    
    public OverlayApi overlays;
    private Requests requests;
    private WSLive wsLive;

    protected JSApiClient(Requests requests) {
        this.requests = requests;
    }

    public static JSApiClient createApiClient(String serverUrl) {
        Requests requests = new Requests(serverUrl);
        String wsurl = wsUrl(serverUrl);
        JSApiClient b = new JSApiClient(requests);
        b.wsLive = new WSLive(wsurl);
        b.setupApi();
        return b;
    }

    public void setupApi() {
        missions = new MissionApi(requests, wsLive.missionUpdates());
        orgs = new OrgApi(requests, wsLive.orgUpdates());
        users = new UserApi(requests, wsLive.userUpdates());
        hw = new HardwareApi(requests, wsLive.hwUpdates());
        calendars = new CalendarApi(requests, wsLive.calendarUpdates());
        streams = new StreamApi(requests, wsLive);
        hwStatus = new HWStatusApi(requests, wsLive.hwStatusUpdates());
        overlays = new OverlayApi(requests);
    }
    
    public static JSApiClient createApiClientBare(String serverUrl) {
        Requests requests = new Requests(serverUrl);
        JSApiClient b = new JSApiClient(requests);
        return b;
    }
    
    public static String wsUrl(String serverUrl) {
        if (Internal.isBlank(serverUrl)) {
            serverUrl = window.location.protocol + "//" + window.location.host;
        }
        return serverUrl.replaceFirst("http", "ws").replaceAll("/$", "") + API_3_WSUPDATES + "/";
    }

    public Observable<Error> liveErrors() {
        return wsLive.onError();
    }
    
    @Deprecated
    public Observable<Hardware> hardwareRx(String orgId) {
        Observable<Hardware> hwrx = rxFor(hw, orgId).merge(calendars.updates().concatMap(c->hw.get(c.id)).filter(h->h!=null));
        return hwrx.concatMap(h -> {
            Observable<Calendar> calRx = calendars.get(h.id);
            
            Observable<Hardware> flatMapObserver = calRx.flatMapObserver((cal, i) -> {
                h._calendar = cal;
                if(cal == null){
                    h._calendar = new Calendar();
                }
                return Observable.just(h);
            }, err -> {
//                console.log("err", typeof(err), err);
                h._calendar = new Calendar();
                return Observable.just(h);
            }, () -> Observable.empty());

            Observable<Hardware> hardwareObservable = flatMapObserver.concatMap(hw -> {
                TimeInterval timeInterval = new TimeInterval(new Date(), new Date());
                h._availability = h.getAvailabilityFor(timeInterval);
                return Observable.just(h);
            });

            return hardwareObservable.flatMap(hardware -> {
                if (!isBlank(hardware.orgId)) {
                    return orgs.get(hardware.orgId)
                            .flatMap(organization -> {
                                hardware._orgName = organization.name;
                                return Observable.just(hardware);
                            });
                }
                return Observable.just(hardware);
            });
        });
    }

    public Observable<Organization> createOrgFull(Organization org, User admin, UserProfile userProfile) {
        return requests.postAsJson(Api3OrgUrls.createWithAdminUrl(), new NewOrgAdminProfile(org, admin, userProfile)).map(json -> Internal.typefyJson(json, Organization.class));
    }

    public Observable<User> resetPassword(LoginRequest loginData) {
        return requests.postAsJson(API_3_RESETPASSWORD, loginData).map(json -> {
            return Typefy.typefy(JSON.parse(json), User.class);
        });
    }

    public Observable<String> logout() {
        return requests.get(API_3_LOGOUT);
    }

    public Observable<User> login(LoginRequest loginData) {
        return requests.postAsJson(API_3_LOGIN, loginData).map(json -> {
            return Typefy.typefy(JSON.parse(json), User.class);
        });
    }
    
    public void setWebSocket(WebSocket _ws){
        wsLive = new WSLiveSession(_ws);
    }
    
    public static Rx.Observable<Hardware> mapHardwareWithCalendar (JSApiClient be, Hardware hardware) {
        return Rx.Observable.of(hardware).concatMap(h -> {
            return be.calendars.get(h.id).flatMapObserver((cal, i) -> {
                h._calendar = cal;
                if(cal == null){
                    h._calendar = new Calendar();
                }
                return Observable.just(h);
            }, err -> {
//                console.log("err", typeof(err), err);
                h._calendar = new Calendar();
                return Observable.just(h);
            }, () -> Observable.empty());
        });
    }

    @Deprecated
    public static <T> Observable<T> rxFor(BaseAsyncDao<T> dao, String orgId) {
        Observable<T> existing = dao.list(orgId).concatMap(Observable::from);
        Observable<T> updates = dao.updates();
        return Observable.concat($array(existing, updates));
    }
    
}
