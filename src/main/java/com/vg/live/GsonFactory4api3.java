package com.vg.live;

import java.awt.Dimension;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.stjs.server.json.gson.JSArrayAdapter;
import org.stjs.server.json.gson.JSMapAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory4api3 {

    private static final Gson GSON_TOSTRING = builder(false).create();
    private static final Gson GSON = builder(false).create();

    public static <T> T gsonClone(T t) {
        String json = GSON_TOSTRING.toJson(t);
        T fromJson = (T) GSON_TOSTRING.fromJson(json, t.getClass());
        return fromJson;
    }

    public static GsonBuilder builder(boolean serializeNulls) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(Enum.class, new STJSEnumDeserializer());
        builder.registerTypeAdapter(org.stjs.javascript.Map.class, new JSMapAdapter());
        builder.registerTypeAdapter(org.stjs.javascript.Array.class, new JSArrayAdapter());
        builder.registerTypeAdapter(org.stjs.javascript.Date.class, new JSDateAdapter());
        if (serializeNulls)
            builder.serializeNulls();
        return builder;
    }

    public static String toGson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T fromInputStream(InputStream in, Class<T> classOf) {
        return GSON.fromJson(new InputStreamReader(in), classOf);
    }

    public static String gsonToString(Object o) {
        return GSON_TOSTRING.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }
}