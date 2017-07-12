package io.live4.js;

import static org.stjs.javascript.Global.console;
import static org.stjs.javascript.JSGlobal.typeof;

import org.stjs.javascript.XMLHttpRequest;

import com.vg.js.bridge.Rx.Observable;

import io.live4.api3.Api3CalendarUrls;
import io.live4.js.internal.Requests;
import io.live4.model.Calendar;

public class CalendarApi extends BaseAsyncDao<Calendar> {

    CalendarApi(Requests requests, Observable<Calendar> updates) {
        super(Calendar.class, updates, requests);
    }

    @Override
    public Observable<Calendar> get(String id) {
        return super.get(id).flatMapObserver((c, _i) -> {
            return Observable.just(c);
        }, err -> {
            console.log("err", typeof(err), err);
            if (404 == ((XMLHttpRequest) err).status) {
                // no calendar for this hw - it's ok, means nobody scheduled anything for this hw
                return Observable.just(new Calendar());
            }
            return Observable.$throw(err);
        }, () -> Observable.empty());
    }

    @Override
    protected String getItemUrl(String id) {
        return Api3CalendarUrls.getUrl(id);
    }

    @Override
    protected String createItemUrl() {
        return Api3CalendarUrls.createUrl();
    }

    @Override
    protected String listUrl(String orgId) {
        return Api3CalendarUrls.listUrl(orgId);
    }
}
