package io.live4.js.internal;

import com.vg.js.bridge.Rx;
import org.stjs.javascript.Error;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.dom.DOMEvent;
import org.stjs.javascript.functions.Callback2;
import org.stjs.javascript.websocket.MessageEvent;
import org.stjs.javascript.websocket.WebSocket;

@Namespace("live4api")
public class WRX {
    private final Callback2<WRX, DOMEvent> onOpen;
    private final String url;
    private WebSocket ws;

    public WRX(String url, Callback2<WRX, DOMEvent> onOpen) {
        this.onOpen = onOpen;
        this.url = url;
    }

    public static Rx.Observable<String> webSocketMessages(String url) {
        return new WRX(url, null).connect().map(m -> (String) m.data);
    }

    public void send(String data) {
        ws.send(data);
    }

    public Rx.Observable<MessageEvent> connect() {
        return Rx.Observable.$create(eventObserver -> {
            ws = new WebSocket(url);

            ws.onopen = domEvent -> {
                if (onOpen != null) {
                    onOpen.$invoke(this, domEvent);
                }
            };
            ws.onmessage = eventObserver::onNext;
            ws.onerror = eventObserver::onError;

            ws.onclose = (e) -> {
                eventObserver.onCompleted();
            };
            return () -> {
                close();
            };
        });
    }

    public void close() {
        if (ws.readyState != WebSocket.CLOSED && ws.readyState != WebSocket.CLOSING) {
            ws.close(1000, "ok");
        }
    }

    @Native
    public static Rx.Observable<String> webSocket(String uri) {
        return webSocket(uri, null);
    }
    
    public static Rx.Observable<String> webSocket(String uri, Callback2<WebSocket, DOMEvent> onOpen) {
        return Rx.Observable.$create(eventObserver -> {
            WebSocket _ws = new WebSocket(uri);

            _ws.onopen = domEvent -> {
                if (onOpen != null) {
                    onOpen.$invoke(_ws, domEvent);
                }
            };
            _ws.onmessage = msg -> {
                eventObserver.onNext((String) msg.data);
            };
            _ws.onerror = eventObserver::onError;

            _ws.onclose = (e) -> {
                if(e.code == 1008){
                    Object err = new Error(e.reason);
                    eventObserver.onError(err);
                }
                eventObserver.onCompleted();
            };
            return () -> {
                if (_ws.readyState != WebSocket.CLOSED && _ws.readyState != WebSocket.CLOSING) {
                    _ws.close(1000, "ok");
                }
            };
        });

    }
}
