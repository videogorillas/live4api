package io.live4.js;

import static io.live4.model.HWStatus.LIST;
import static io.live4.model.HWStatus.OBJECT;

import com.vg.js.bridge.Rx.Observable;

import io.live4.js.internal.Requests;
import io.live4.model.HWStatus;

public class HWStatusApi extends BaseAsyncDao<HWStatus> {
    HWStatusApi(Requests requests, Observable<HWStatus> updates) {
        super(HWStatus.class, updates, requests);
    }

    @Override
    protected String getItemUrl(String id) {
        return HWStatus.API_HWSTATUS + OBJECT + "/" + id;
    }

    @Override
    protected String createItemUrl() {
        return null;
    }

    @Override
    protected String listUrl(String orgId) {
        return HWStatus.API_HWSTATUS + LIST + "/" + orgId;
    }
}
