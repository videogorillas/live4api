package io.live4.js;

import com.vg.js.bridge.Rx.Observable;

import io.live4.api3.Api3OrgUrls;
import io.live4.js.internal.Requests;
import io.live4.model.Organization;

public class OrgApi extends BaseAsyncDao<Organization> {
    OrgApi(Requests requests, Observable<Organization> updates) {
        super(Organization.class, updates, requests);
    }

    @Override
    protected String getItemUrl(String orgId) {
        return Api3OrgUrls.getUrl(orgId);
    }

    @Override
    protected String createItemUrl() {
        return Api3OrgUrls.createUrl();
    }

    @Override
    protected String listUrl(String orgId) {
        return Api3OrgUrls.listUrl("su"); // super admin
    }
}
