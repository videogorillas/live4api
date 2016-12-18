package io.live4.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3CalendarUrls {
    public static final String API_3_CALENDAR = "/api/3/calendar";

    public static final String OBJECT = "/object";
    public static final String LIST = "/list";

    public static String createUrl() {
        return API_3_CALENDAR + OBJECT;
    }

    public static String getUrl(String id) {
        return API_3_CALENDAR + OBJECT + "/" + id;
    }

    public static String listUrl(String orgId) {
        return API_3_CALENDAR + LIST + "/" + orgId;
    }
}
