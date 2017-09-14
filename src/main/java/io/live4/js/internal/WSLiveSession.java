package io.live4.js.internal;

import static org.stjs.javascript.JSCollections.$array;

import org.stjs.javascript.Error;
import org.stjs.javascript.websocket.WebSocket;

import com.vg.js.bridge.Rx;
import com.vg.js.bridge.Rx.ReplaySubject;

import io.live4.model.Internal;
import io.live4.model.LiveMessage;

public class WSLiveSession extends WSLive {

    public WSLiveSession(String url, WebSocket _ws) {
        super(url);
        this._liveMessages = WRX
                .fromWebSocket(_ws, (ws, e) -> {
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
    
}
