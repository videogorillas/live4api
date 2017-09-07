package io.live4.js;

import static org.stjs.javascript.JSCollections.$map;

import com.vg.js.bridge.Rx;

import io.live4.api3.Api3Urls;
import io.live4.js.internal.Requests;

public class OverlayApi {
    private Requests requests;

    OverlayApi(Requests requests) {
        this.requests = requests;
    }

    public Rx.Observable<String> getOverlay(String id) {
        return (requests.get(Api3Urls.API_3_OVERLAY + Api3Urls.OVERLAY_OBJECT + "/" + id));
    }

    public Rx.Observable<String> createOverlay(String orgId, String url) {
        return requests.request(Api3Urls.API_3_OVERLAY + Api3Urls.OVERLAY_OBJECT + "/", "id=" + orgId + "&url=" + url,
                "POST", $map("Content-Type", "application/x-www-form-urlencoded"));
    }

    public Rx.Observable<String> deleteOverlay(String id) {
        return requests.deleteRequest(Api3Urls.API_3_OVERLAY + Api3Urls.OVERLAY_OBJECT + "/" + id);
    }
}
