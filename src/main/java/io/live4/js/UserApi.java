package io.live4.js;

import static io.live4.api3.Api3MissionUrls.cancelNotificationUrl;
import static io.live4.api3.Api3MissionUrls.joinByTokenUrl;
import static io.live4.js.internal.Typefy.typefy;
import static org.stjs.javascript.JSCollections.$array;

import com.vg.js.bridge.Rx;
import com.vg.js.bridge.Rx.Observable;

import io.live4.api3.Api3MissionUrls;
import io.live4.api3.Api3UserUrls;
import io.live4.js.internal.Requests;
import io.live4.model.User;

public class UserApi extends BaseAsyncDao<User> {

    UserApi(Requests requests, Observable<User> updates) {
        super(User.class, updates, requests);
    }

    public Rx.Observable<User> createOrUpdate(User user) {
        return user.id == null ? create(user) : forceUpdate(user);
    }

    @Override
    protected String getItemUrl(String id) {
        return Api3UserUrls.getUrl(id);
    }

    @Override
    protected String createItemUrl() {
        return Api3UserUrls.createUrl();
    }

    @Override
    protected String listUrl(String orgId) {
        return Api3UserUrls.listUrl(orgId);
    }

    public Rx.Observable<User> forceUpdate(User user) {
        return getAndUpdate(user.id, u -> {
            u.profiles = user.profiles;
            u.name = user.name;
            u.lastname = user.lastname;
            u.setAvatarUrl(user.getAvatarUrl());
        });
    }

    public Rx.Observable<User> inviteToMission(User user, String missionId) {
        return _post(Api3MissionUrls.inviteUrl(missionId), user);
    }
    
    public Rx.Observable<User> allUsersUpdates(String orgId) {
        Observable<User> existingUsers = this.list(orgId).concatMap(arr -> Observable.from(arr));
        Observable<User> userUpdates = this.updates();
        return Observable.concat($array(existingUsers, userUpdates));
    }

    public Rx.Observable<User> sendCancelNotification(User user, String missionId) {
        return _post(cancelNotificationUrl(missionId), user);
    }

    public Observable<User> join(User user, String missionId) {
        return _post(joinByTokenUrl(missionId), user);
    }

    public Rx.Observable<User> getUserByEmail(String email) {
        return requests.getJson(Api3UserUrls.byEmailUrl(email)).map(obj -> {
            return typefy(obj, User.class);
        });
    }

    public Rx.Observable<Boolean> isUserExists(String email) {
        return requests.getJson(Api3UserUrls.checkUserByEmail(email)).map(obj -> (Boolean) obj);
    }

    public Rx.Observable<Boolean> isTempUser(String email) {
        return requests.getJson(Api3UserUrls.isUserTemp(email)).map(obj -> (Boolean) obj);
    }

}
