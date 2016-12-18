package com.vg.live.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3Urls {

    public static final String WSVIDEO = "/api/3/wsvideo";
    public static final String API_3_ORG_STORAGE_UPLOAD = "/api/3/orgstorage/upload";
    public static final String API_3_ORG_STORAGE_GET = "/api/3/orgstorage/get";
    public static final String API_3_HWLOG = "/api/3/hwlog";
    public static final String API_3_LOGIN = "/api/3/login";
    public static final String API_3_RESETPASSWORD = "/api/3/resetpassword";
    public static final String API_3_WSUPDATES = "/api/3/wsupdates";
    public static final String API_3_LOCATIONS = "/api/3/locations";
    public static String wsVideo(String streamId) {
        return WSVIDEO + "/" + streamId;
    }

}
