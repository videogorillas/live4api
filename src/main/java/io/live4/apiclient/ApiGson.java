package io.live4.apiclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.stjs.server.json.gson.JSArrayAdapter;
import org.stjs.server.json.gson.JSMapAdapter;

public class ApiGson {
    private final static Gson instance = createGson();

    public static Gson gson() {
        return instance;
    }
    
    public static Gson createGson() {
        return builder().create();
    }
    
    public static GsonBuilder builder() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeSpecialFloatingPointValues();
        builder.registerTypeHierarchyAdapter(Enum.class, new STJSEnumDeserializer());
        builder.registerTypeAdapter(org.stjs.javascript.Map.class, new JSMapAdapter());
        builder.registerTypeAdapter(org.stjs.javascript.Array.class, new JSArrayAdapter());
        builder.registerTypeAdapter(org.stjs.javascript.Date.class, new JSDateAdapter());
        return builder;
    }
}
