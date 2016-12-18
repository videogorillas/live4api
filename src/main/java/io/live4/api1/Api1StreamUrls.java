package io.live4.api1;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api1StreamUrls {
    public static final String API_STREAM = "/api/stream";

    public static final String createUrl() {
        return API_STREAM;
    }

    public static final String listUrl() {
        return API_STREAM;
    }

    public static final String getUrl(String id) {
        return API_STREAM + "/" + id;
    }
}
