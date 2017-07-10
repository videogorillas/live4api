package io.live4.js.internal;

import static io.live4.model.Internal.eq;
import static io.live4.model.Internal.isBlank;
import static io.live4.model.Internal.keys;
import static org.stjs.javascript.Global.console;
import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.javascript.JSGlobal.JSON;

import com.vg.js.bridge.Rx;

import org.stjs.javascript.Map;
import org.stjs.javascript.XMLHttpRequest;
import org.stjs.javascript.dom.FormData;
import org.stjs.javascript.typed.ArrayBuffer;

import com.vg.js.bridge.Rx.Observable;

public class Requests {

    public static Observable<String> postAsJson(String url, Object o) {
        return request(url, JSON.stringify(o), "POST", $map("Content-type", "application/json"));
    }

    public static Observable<String> putAsJson(String url, Object o) {
        return request(url, JSON.stringify(o), "PUT", $map("Content-type", "application/json"));
    }

    public static Observable<String> get(String url) {
        return request(url, null, "GET", null);
    }

    public static Observable<String> delete_(String url) {
        return request(url, null, "DELETE", null);
    }

    public static Observable<Object> getJson(String url) {
        return request(url, null, "GET", null).filter(response->!eq(response, "")).map(jsonResponse -> JSON.parse(jsonResponse));
    }

    public static Observable<String> request(String url, String data, String method, Map<String, String> headers) {
        Observable<String> o = Observable.$create(observer -> {
            RequestObserver requestObserver = new RequestObserver(url, data, headers, null, method, observer).invoke();
            MutableBoolean loaded = requestObserver.getLoaded();
            XMLHttpRequest http = requestObserver.getHttp();

            return () -> {
                if (!loaded.value) {
                    if (http.readyState != 4) {
                        dbg("abort " + method + " " + url);
                        http.abort();
                    }
                }
            };
        });
        return o;
    }

    public static Observable<String> formPost(String url, FormData data) {
        Observable<String> o = Observable.$create(observer -> {
            RequestObserver requestObserver = new RequestObserver(url, null, null, data, "POST", observer).invoke();
            MutableBoolean loaded = requestObserver.getLoaded();
            XMLHttpRequest http = requestObserver.getHttp();

            return () -> {
                if (!loaded.value) {
                    if (http.readyState != 4) {
                        dbg("abort FORMPOST " + url);
                        http.abort();
                    }
                }
            };
        });
        return o;
    }

    public static boolean debug = true;

    private static void dbg(String msg) {
        if (debug) {
            console.log(msg);
        }
    }

    public static Observable<ArrayBuffer> getArrayBuffer(String url) {
        Observable<ArrayBuffer> o = Observable.create(observer -> {
            XMLHttpRequest http = new XMLHttpRequest();
            http.open("GET", url, true);
            http.responseType = "arraybuffer";
            http.onreadystatechange = () -> {
                if (http.readyState == 4) {
                    if (http.status == 200) {
                        observer.onNext(http.response);
                        observer.onCompleted();
                    } else {
                        observer.onError(http);
                    }
                }
            };
            http.send();
        });
        return o;
    }

    public static Observable<String> _delete(String url) {
        return request(url, null, "DELETE", null);
    }

    private static class RequestObserver {
        private String url;
        private String data;
        private FormData form;
        private String method;
        private Rx.Observer<String> observer;
        private XMLHttpRequest http;
        private MutableBoolean loaded;
        private Map<String, String> headers;

        public RequestObserver(String url, String data, Map<String, String> headers, FormData form, String method, Rx.Observer<String> observer) {
            this.url = url;
            this.data = data;
            this.headers = headers == null ? $map() : headers;
            this.form = form;
            this.method = method;
            this.observer = observer;
        }

        public XMLHttpRequest getHttp() {
            return http;
        }

        public MutableBoolean getLoaded() {
            return loaded;
        }

        public RequestObserver invoke() {
            http = new XMLHttpRequest();
            http.open(method, url);
            keys(headers).$forEach(h -> http.setRequestHeader(h, headers.$get(h)));
            console.log(method, url);
            loaded = new MutableBoolean(false);
            http.onreadystatechange = () -> {
                if (http.readyState == 1) {
                    keys(headers).$forEach(h -> http.setRequestHeader(h, headers.$get(h)));
                }
            };
            http.onload = () -> {
                loaded.value = true;

                // 1xx Informational
                // 2xx Success
                // 3xx Redirection
                if(http.status == 204){
                    observer.onNext(null);
                    observer.onCompleted();
                }
                if (http.status >= 200 && http.status < 400) {
                    if (isBlank(http.responseText)) {
                        observer.onError(http);
                    } else {
                        observer.onNext(http.responseText);
                        observer.onCompleted();
                    }
                }

                //4xx Client Error
                //5xx Server Error
                if (http.status >= 400) {
                    dbg("error " + http.status + " " + method + " " + url);
                    observer.onError(http);
                }

                //CORS errors, connection errors etc
                if (http.status == 0) {
                    dbg("error " + http.status + " " + method + " " + url);
                    observer.onError(http);
                }
            };
            http.onerror = e -> {
                dbg("error " + http.status + " " + method + " " + url);
                observer.onError(e);
            };
            if (data == null) {
                http.send(form);
            } else {
                http.send(data);
            }
            return this;
        }
    }
}
