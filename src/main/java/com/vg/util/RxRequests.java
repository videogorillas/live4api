package com.vg.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.ws.WebSocket;
import com.squareup.okhttp.ws.WebSocketCall;
import com.squareup.okhttp.ws.WebSocketListener;

import okio.Buffer;
import rx.Observable;
import rx.subscriptions.Subscriptions;

public class RxRequests {
    public static Observable<Response> responseRx(OkHttpClient c, Request request) {
        return Observable.create(o -> {
            Call newCall = c.newCall(request);
            AtomicBoolean needCancel = new AtomicBoolean(true);
            o.add(Subscriptions.create(() -> {
                if (needCancel.get() && !newCall.isCanceled()) {
                    newCall.cancel();
                }
            }));
            newCall.enqueue(new Callback() {

                @Override
                public void onResponse(Response response) throws IOException {
                    needCancel.set(false);
                    o.onNext(response);
                    o.onCompleted();
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    needCancel.set(false);
                    o.onError(new RequestException(request, e));
                    o.onCompleted();
                }
            });
        });
    }

    public static Observable<Response> okResponseRx(OkHttpClient c, Request request) {
        return responseRx(c, request).concatMap(r -> {
            boolean ok = r != null && (r.code() == 200 || r.code() == 206);
            if (ok) {
                return Observable.just(r);
            }
            discardBody(r);
            return Observable.<Response> error(new UnhandledResponseException(r));
        });
    }

    public static Observable<String> requestString(OkHttpClient client, Request request) {
        return okResponseRx(client, request).concatMap(r -> {
            try {
                return Observable.just(r.body().string());
            } catch (IOException e1) {
                return Observable.error(e1);
            }
        });
    }

    public static Observable<InputStream> requestByteStream(OkHttpClient client, Request request) {
        return okResponseRx(client, request).concatMap(r -> {
            try {
                return Observable.just(r.body().byteStream());
            } catch (IOException e1) {
                return Observable.error(e1);
            }
        });
    }

    public static void discardBody(Response r) {
        try {
            discardInputStream(r.body().byteStream());
        } catch (IOException e1) {
            throw new UncheckedIOException(e1);
        }
    }
    
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static long discardInputStream(InputStream input)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            count += n;
        }
        return count;
    }

    public static Observable<String> webSocket(OkHttpClient c, Request request, BiConsumer<WebSocket, Response> onOpen) {
        Observable<String> wsMsgs = Observable.create(o -> {
            WebSocketCall call = WebSocketCall.create(c, request);
            Subscriptions.create(() -> call.cancel());

            call.enqueue(new WebSocketListener() {
                @Override
                public void onPong(Buffer payload) {
                    System.out.println("onPong " + payload);
                }

                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    System.out.println("onOpen " + webSocket + " " + response);
                    if (onOpen != null) {
                        onOpen.accept(webSocket, response);
                    }
                }

                @Override
                public void onMessage(ResponseBody message) throws IOException {
                    o.onNext(message.string());
                }

                @Override
                public void onFailure(IOException e, Response response) {
                    System.err.println("response: " + response + " err " + e);
                    o.onError(e);
                }

                @Override
                public void onClose(int code, String reason) {
                    System.out.println("onClose " + code + " " + reason);
                    o.onCompleted();
                }
            });
        });
        return wsMsgs;
    }
}
