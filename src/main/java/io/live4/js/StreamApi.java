package io.live4.js;

import static io.live4.api2.Api2Urls.API_2_STREAM_UPDATE_TITLE;

import org.stjs.javascript.Array;

import com.vg.js.bridge.Rx;
import com.vg.js.bridge.Rx.Observable;

import io.live4.api1.Api1StreamUrls;
import io.live4.api3.Api3StreamUrls;
import io.live4.js.internal.Requests;
import io.live4.js.internal.WSLive;
import io.live4.model.Internal;
import io.live4.model.LiveMessage;
import io.live4.model.Stream;
import io.live4.model.StreamLocation;
import io.live4.model.StreamResponse;

public class StreamApi extends BaseAsyncDao<StreamResponse> {

    private WSLive wsLive;

    StreamApi(Requests requests, WSLive wsLive) {
        super(StreamResponse.class, Observable.empty(), requests);
        this.wsLive = wsLive;
    }

    @Override
    protected String getItemUrl(String id) {
        return Api1StreamUrls.getUrl(id);
    }

    @Override
    protected String createItemUrl() {
        return Api3StreamUrls.createUrl();
    }

    @Override
    protected String listUrl(String userId) {
        return Api1StreamUrls.listUrl() + "/" + userId;
    }
    
    @Override
    public Rx.Observable<Array<StreamResponse>> list(String userId) {
        return _list(listUrl(userId));
    }

    public Observable<StreamLocation> locationUpdates(String sid) {
        return wsLive.locationUpdates(sid);
    }

    public Observable<LiveMessage> liveMessages(String sid) {
        return wsLive.streamUpdates(sid);
    }

    public Observable<StreamResponse> updateTitle(String sid, String newTitle) {
        Stream sr = new Stream();
        sr.setId(sid);
        sr.title = newTitle;
        return requests.putAsJson(API_2_STREAM_UPDATE_TITLE + "/" + sr.sid().toString(), sr).map(
                json -> Internal.typefyJson(json, StreamResponse.class));
    }
}
