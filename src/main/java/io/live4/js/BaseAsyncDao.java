package io.live4.js;

import static org.stjs.javascript.JSGlobal.JSON;

import org.stjs.javascript.Array;
import org.stjs.javascript.functions.Callback1;

import com.vg.js.bridge.Rx;
import com.vg.js.bridge.Rx.Observable;

import io.live4.js.internal.Requests;
import io.live4.js.internal.Typefy;

public abstract class BaseAsyncDao<T> {

    protected final Class<T> cls;
    protected Observable<T> _wsrx;
    protected Requests requests;

    protected BaseAsyncDao(Class<T> cls, Observable<T> updates, Requests requests) {
        this.cls = cls;
        this.requests = requests;
        this._wsrx = updates.share();
    }

    public Rx.Observable<T> get(String id) {
        return _get(getItemUrl(id));
    }

    @Deprecated
    public Rx.Observable<T> clarifyGet(String id, String clarifyId) {
        return _get(getItemForMission(id, clarifyId));
    }

    protected abstract String getItemUrl(String id);

    @Deprecated
    protected String getItemForMission(String id, String clarifyId) {
        return null;
    }

    protected abstract String createItemUrl();

    protected abstract String listUrl(String orgId);

    public Rx.Observable<T> create(T item) {
        return _post(createItemUrl(), item);
    }

    @Deprecated
    public Rx.Observable<T> update(T item) {
        return _put(createItemUrl(), item);
    }

    /** get latest revision of object, apply transformer and then update; */
    public Rx.Observable<T> getAndUpdate(String id, Callback1<T> transformer) {
        return get(id).concatMap(item -> {
            transformer.$invoke(item);
            return _put(createItemUrl(), item);
        }).retryWhen(observable -> observable.delay(1000));
    }
    

    public Rx.Observable<T> remove(String id) {
        return _delete(getItemUrl(id));
    }

    public Rx.Observable<Array<T>> list(String orgId) {
        return _list(listUrl(orgId));
    }

    protected Rx.Observable<Array<T>> _list(String url) {
        return requests.get(url).map(this::parseArray);
    }

    protected Rx.Observable<T> _delete(String url) {
        return requests.deleteRequest(url).map(this::parse);
    }

    protected Rx.Observable<T> _put(String url, T item) {
        return requests.putAsJson(url, item).map(this::parse);
    }

    protected Rx.Observable<T> _post(String url, T item) {
        return requests.postAsJson(url, item).map(this::parse);
    }

    protected Rx.Observable<T> _get(String url) {
        return requests.get(url).map(this::parse);
    }

    protected T parse(String response) {
        T parse = (T) JSON.parse(response);
        return Typefy.typefy(parse, cls);
    }

    protected Array<T> parseArray(String response) {
        Array<T> parse = (Array<T>) JSON.parse(response);
        return Typefy.typefyArray(parse, cls);
    }

    public Rx.Observable<T> updates() {
        return _wsrx;
    }
}
