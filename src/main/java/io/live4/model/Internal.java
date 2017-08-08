package io.live4.model;

import static io.live4.js.internal.InternalJSAdapter.hasOwnProperty;
import static org.stjs.javascript.Global.window;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.javascript.JSGlobal.JSON;
import static org.stjs.javascript.JSGlobal.typeof;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.JSStringAdapterBase;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.Native;

import io.live4.js.internal.InternalJSAdapter;
import io.live4.js.internal.Typefy;

public class Internal {
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
        if (string != null && !"".equals(string)) {
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

    public static long currentTimeMillis() {
        return (long) Date.now();
    }

    public static boolean isJava = "9007199254740993".equals("" + (9007199254740991L + 2));

    public static String fromCharCode(int charcode) {
        if (!isJava) {
            return JSStringAdapterBase.fromCharCode(String.class, charcode);
        } else {
            return String.valueOf((char) charcode);
        }
    }

    @Native
    static long tryParse(String format, String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    @Native
    public static long tryParseDate(String timestamp) {
        try {
            return Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
        }
    
        long tryParse = tryParse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", timestamp);
        if (-1 == tryParse) {
            tryParse = tryParse("yyyy-MM-dd'T'HH:mm:ssZZZZ", timestamp);
        }
        if (-1 == tryParse) {
            timestamp = timestamp.replace(" a.m.", "AM");
            timestamp = timestamp.replace(" p.m.", "PM");
            tryParse = tryParse("yyyy-MM-dd'T'HH:mm:ss,SSSaZ", timestamp);
        }
        if (-1 == tryParse) {
            System.err.println("CANT PARSE TIMESTAMP " + timestamp);
            return 0;
        }
    
        return tryParse;
    }
    
    public static boolean eq(String str, String str2) {
        if (str == null) {
            if (str2 == null) {
                return true;
            }
            return false;
        }
        return str.equals(str2);
    }
    
    public static <T> Array<String> keys(Map<String, T> map) {
        return map == null ? $array() : InternalJSAdapter.keys(Object.class, map);
    }
    
    public static <T> T typefyJson(String json, Class<T> cls) {
        if (json != null) {
            return Typefy.typefy(JSON.parse(json), cls);
        }
        return null;
    }

    public static boolean isBrowser() {
        try {
            return window != null;
        } catch (Exception e) {
        }
        return false;
    }
}
