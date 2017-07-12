package io.live4.js;

import static io.live4.api3.Api3HwUrls.API_3_HW;
import static io.live4.api3.Api3Urls.API_3_HWLOG;
import static org.stjs.javascript.JSGlobal.JSON;

import org.stjs.javascript.Array;

import com.vg.js.bridge.Rx;
import com.vg.js.bridge.Rx.Observable;

import io.live4.api3.Api3HwUrls;
import io.live4.js.internal.Requests;
import io.live4.js.internal.Typefy;
import io.live4.model.HWLogEntry;
import io.live4.model.Hardware;

public class HardwareApi extends BaseAsyncDao<Hardware> {
    HardwareApi(Requests requests, Observable<Hardware> updates) {
        super(Hardware.class, updates, requests);
    }

    public Rx.Observable<Hardware> findByPort(int port) {
        return requests.get(Api3HwUrls.findByPortUrl(port)).map(this::parse);
    }

    public Observable<String> releaseHardwares(Array<String> removedHws, String mId) {
        return requests.postAsJson(Api3HwUrls.releaseUrl(mId), removedHws);
    }

    public Observable<String> reassignHardware(String orgId, String hwId) {
        return requests.get(API_3_HW + Api3HwUrls.REASSIGN +  "/" + hwId + "/" + orgId);
    }

    @Override
    protected String getItemForMission(String id, String mid) {
        return Api3HwUrls.getUrl(id) + "/" + mid;
    }

    @Override
    protected  String getItemUrl(String id) {
        return Api3HwUrls.getUrl(id);
    }

    @Override
    protected String createItemUrl() {
        return Api3HwUrls.createUrl();
    }

    @Override
    protected String listUrl(String orgId) {
        return Api3HwUrls.listUrl(orgId);
    }


    public Rx.Observable<Array<HWLogEntry>> logList(String hwId) {
        return requests.get(API_3_HWLOG + "/" + hwId).map(response -> {
            return Typefy.typefyArray(JSON.parse(response), HWLogEntry.class);
        });
    }
}
