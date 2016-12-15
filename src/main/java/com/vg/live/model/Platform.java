package com.vg.live.model;

import org.stjs.javascript.Date;
import org.stjs.javascript.JSStringAdapterBase;

public class Platform {
    public static boolean isJava = "9007199254740993".equals("" + (9007199254740991L + 2));

    public static String fromCharCode(int charcode) {
        if (!isJava) {
            return JSStringAdapterBase.fromCharCode(String.class, charcode);
        } else {
            return String.valueOf((char) charcode);
        }
    }

    public static long currentTimeMillis() {
        return (long) Date.now();
    }

}
