package io.live4.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3HwUrls {
    public static final String API_3_HW = "/api/3/hardware";
    public static final String FIND_BY_PORT = "/findByPort";
    public static final String RELEASE = "/release";

    public static final String OBJECT = "/object";
    public static final String LIST = "/list";

    public static String createUrl() {
        return API_3_HW + OBJECT;
    }

    public static String getUrl(String id) {
        return API_3_HW + OBJECT + "/" + id;
    }

    public static String listUrl(String orgId) {
        return API_3_HW + LIST + "/" + orgId;
    }

    public static String findByPortUrl(int port) {
        return API_3_HW + FIND_BY_PORT + "/" + port;
    }

    public static String releaseUrl(String id) {
        return API_3_HW + RELEASE + "/" + id;
    }
}
