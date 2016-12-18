package com.vg.live.model;

import static com.vg.live.model.InternalJSAdapter.hasOwnProperty;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.javascript.JSGlobal.typeof;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

class Internal {
    public static <T> Array<T> mapValues(Map<String, T> map) {
        Array<T> result = $array();
        if (map == null)
            return result;
        for (String k : map) {
            T item = map.$get(k);
            result.push(item);
        }
        return result;
    }

    public static boolean isBlank(String string) {
        return string == null || "".equals(string) || string.matches("\\s+");
    }

    public static <T> Map<String, T> defaultMap(Map<String, T> map) {
        return map == null ? $map() : map;
    }

    public final static <T> boolean containsKey(Map<String, T> map, String key) {
        return map != null && hasOwnProperty(map, key);
    }

    public static <T> Array<T> defaultArray(Array<T> arr) {
        return arr == null ? $array() : arr;
    }

    public static String defaultString(String string, String defaultString) {
        if (string != null && isString(string) && !"".equals(string)) {
            return string;
        }
        return defaultString;
    }
    
    public static boolean isNotBlank(String str) {
        return str != null && !"".equals(str) && !str.matches("\\s+");
    }

    public static boolean isString(Object anything) {
        return "string".equals(typeof(anything));
    }

}
