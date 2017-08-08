package io.live4.js.internal;

import static io.live4.model.LiveMessage.subscribe;
import static io.live4.model.LiveMessage.subscribeStream;
import static org.stjs.javascript.Global.console;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.JSON;

import org.stjs.javascript.Array;
import org.stjs.javascript.Error;
import org.stjs.javascript.websocket.WebSocket;

import com.vg.js.bridge.Rx;
import com.vg.js.bridge.Rx.Observable;
import com.vg.js.bridge.Rx.ReplaySubject;

import io.live4.model.Calendar;
import io.live4.model.HWStatus;
import io.live4.model.Hardware;
import io.live4.model.Internal;
import io.live4.model.LiveMessage;
import io.live4.model.Mission;
import io.live4.model.Organization;
import io.live4.model.StreamLocation;
import io.live4.model.User;

public class WSLive {
    private ReplaySubject<WebSocket> _ws;
    private Observable<LiveMessage> _liveMessages;
    private Rx.Subject<Error> errorSubject;
    private Array<String> subs;

    public WSLive(String url) {
        subs = $array();
        _ws = new ReplaySubject<>(1);
        errorSubject = new Rx.Subject<>();

        this._liveMessages = WRX
                .webSocket(url, (ws, e) -> {
                    subs.$forEach(sub -> {
                        ws.send(sub);
                    });
                    this._ws.onNext(ws);
                })
                .doOnError(e -> {
                    errorSubject.onNext((Error) e);
                })
                .repeatWhen(e -> e.delay(1000))
                .retryWhen(e -> e.delay(1000))
                .map(json -> {
                    return Internal.typefyJson(json, LiveMessage.class);
                })
                .share();

    }

    public Observable<Error> onError(){
        return errorSubject;
    }

    public Observable<LiveMessage> streamUpdates(String sid) {
        _subscribe((LiveMessage) subscribeStream(sid));
        return _liveMessages.filter(lm -> Internal.eq(sid, lm.streamId));
    }
    
    public Observable<StreamLocation> locationUpdates(String sid) {
        return streamUpdates(sid)
                .filter(lm -> lm.hasMap())
                .concatMap(lm -> Observable.from(lm.map).map(d -> {
                    d.location.streamId = sid;
                    return d.location;
                }));
    }

    private void _subscribe(LiveMessage msg) {
        String json = JSON.stringify(msg);
        if (subs.indexOf(json) < 0) {
//            console.log("subscribe", json, subs);
            _ws.take(1).subscribe(ws -> ws.send(json));
            subs.push(json);
        }
    }

    public Observable<Calendar> calendarUpdates() {
        _subscribe((LiveMessage) subscribe("calendar"));
        return _liveMessages.filter(lm -> lm.calendar != null).map(lm -> lm.calendar);
    }

    public Observable<User> userUpdates() {
        _subscribe((LiveMessage) subscribe("user"));
        return _liveMessages.filter(lm -> lm.user != null).map(lm -> lm.user);
    }

    public Observable<Mission> missionUpdates() {
        _subscribe((LiveMessage) subscribe("mission"));
        return _liveMessages.filter(lm -> lm.mission != null).map(lm -> lm.mission);
    }

    public Observable<Hardware> hwUpdates() {
        _subscribe((LiveMessage) subscribe("hardware"));
        return _liveMessages.filter(lm -> lm.hardware != null).map(lm -> lm.hardware);
    }

    public Observable<Organization> orgUpdates() {
        _subscribe((LiveMessage) subscribe("org"));
        return _liveMessages.filter(lm -> lm.org != null).map(lm -> lm.org);
    }

    public Observable<HWStatus> hwStatusUpdates() {
        _subscribe((LiveMessage) subscribe("hwstatus"));
        return _liveMessages.filter(lm -> lm.hwStatus != null).map(lm -> lm.hwStatus);
    }
}
