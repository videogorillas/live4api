package io.live4.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3StreamUrls {
    public static final String API_3_STREAM = "/api/3/stream";

    public static final String OBJECT = "/object";
    public static final String LIST = "/list";

    public static String createUrl() {
        return API_3_STREAM + OBJECT;
    }

    public static String getUrl(String id) {
        return API_3_STREAM + OBJECT + "/" + id;
    }
}
