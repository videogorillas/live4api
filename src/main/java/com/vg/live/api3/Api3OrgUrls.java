package com.vg.live.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3OrgUrls {
    public static final String API_3_ORG = "/api/3/org";
    public static final String CREATEWITHADMIN = "/createWithAdmin";

    public static final String OBJECT = "/object";
    public static final String LIST = "/list";

    public static String baseUrl() {
        return API_3_ORG + OBJECT;
    }

    public static String createUrl() {
        return API_3_ORG + OBJECT;
    }

    public static String getUrl(String orgId) {
        return API_3_ORG + OBJECT + "/" + orgId;
    }

    public static String listUrl(String orgId) {
        return API_3_ORG + LIST + "/" + orgId;
    }

    public static String createWithAdminUrl() {
        return API_3_ORG + CREATEWITHADMIN;
    }
}
