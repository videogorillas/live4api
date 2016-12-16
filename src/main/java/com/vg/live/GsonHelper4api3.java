package com.vg.live;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class GsonHelper4api3 {

    private Gson GSON_PRETTY;
    private Gson GSON;

    public GsonHelper4api3(Gson pretty, Gson compact) {
        this.GSON_PRETTY = pretty;
        this.GSON = compact;
    }

    public String toGson(Object src) {
        return GSON.toJson(src);
    }

    public String toGsonPretty(Object src) {
        return GSON_PRETTY.toJson(src);
    }

    public <T> T fromInputStream(InputStream in, Class<T> classOf) {
        return GSON.fromJson(new InputStreamReader(in), classOf);
    }

    public String gsonToString(Object o) {
        return GSON.toJson(o);
    }

    public <T> T fromJson(String json, Class<T> typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

}