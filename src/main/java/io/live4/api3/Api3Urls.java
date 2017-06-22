package io.live4.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3Urls {

    public static final String WSVIDEO = "/api/3/wsvideo";
    public static final String API_3_WSPUSHVIDEO = "/api/3/wspushvideo";
    public static final String API_3_ORG_STORAGE_UPLOAD = "/api/3/orgstorage/upload";
    public static final String API_3_ORG_STORAGE_GET = "/api/3/orgstorage/get";
    public static final String API_3_HWLOG = "/api/3/hwlog";
    public static final String API_3_LOGIN = "/api/3/login";
    public static final String API_3_RESETPASSWORD = "/api/3/resetpassword";
    public static final String API_3_WSUPDATES = "/api/3/wsupdates";
    public static final String API_3_LOCATIONS = "/api/3/locations";
    public static final String API_3_CHECKTOKEN = "/api/3/checktoken";
    public static String wsVideo(String streamId) {
        return WSVIDEO + "/" + streamId;
    }
    public static String wsPushVideo(String streamId) {
        return API_3_WSPUSHVIDEO + "/" + streamId;
    }
    public static String locationsUrl(String streamId) {
        return API_3_LOCATIONS + "/" + streamId;
    }
    public static String checkTokenUrl(String token) {
        return API_3_CHECKTOKEN + "/" + token;
    }
}
