package com.vg.live;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LiveGson4api3 {
    public static final GsonHelper4api3 GSON = new GsonHelper4api3(pretty(false), compact(false));

    public static Gson pretty(boolean serializeNulls) {
        GsonBuilder builder = GsonFactory4api3.builder(serializeNulls);
        builder.setPrettyPrinting();
        return builder.create();
    }

    public static Gson compact(boolean serializeNulls) {
        GsonBuilder builder = GsonFactory4api3.builder(serializeNulls);
        return builder.create();
    }

}
