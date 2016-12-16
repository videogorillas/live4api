package com.vg.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class HttpUtils {

    public final static MediaType JSON_MIMETYPE = MediaType.parse("application/json");
    public final static MediaType OCTET_STREAM = MediaType.parse("application/octet-stream");
    public final static String LAST_MODIFIED = "Last-Modified";

    public static Request GET(String url) {
        return new Request.Builder().url(url).build();
    }

    public static Request GET(HttpUrl url) {
        return GET(url.toString());
    }

    public static Request postAsJsonRequest(String url, String json) {
        String buildUrl = String.format(url);
        Request.Builder builder = new Request.Builder();
        builder.url(buildUrl).post(RequestBody.create(JSON_MIMETYPE, json));
        return builder.build();
    }

    public static Request putAsJsonRequest(String url, String json) {
        String buildUrl = String.format(url);
        Request.Builder builder = new Request.Builder();
        builder.url(buildUrl).put(RequestBody.create(JSON_MIMETYPE, json));
        return builder.build();
    }

    public static SimpleDateFormat httpDateFormat() {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    }

    public static String httpDateFormat(long mtime) {
        return httpDateFormat().format(new java.util.Date(mtime));
    }
}