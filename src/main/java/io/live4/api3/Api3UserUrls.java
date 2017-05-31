package io.live4.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3UserUrls {
    public static final String API_3_USER = "/api/3/users";
    public static final String BYEMAIL = "/byemail";

    public static final String OBJECT = "/object";
    public static final String LIST = "/list";
    public static final String CHECK = "/check";
    public static final String ISTEMP = "/istemp";

    public static final String createUrl() {
        return API_3_USER + OBJECT;
    }

    public static final String updateUrl() {
        return API_3_USER + OBJECT;
    }

    public static final String getUrl(String id) {
        return API_3_USER + OBJECT + "/" + id;
    }

    public static final String listUrl(String orgId) {
        return API_3_USER + LIST + "/" + orgId;
    }

    public static String byEmailUrl(String email) {
        return API_3_USER + BYEMAIL + "/" + email;
    }

    public static String checkUserByEmail(String email) {
        return API_3_USER + CHECK + "/" + email;
    }

    public static String isUserTemp(String email) {
        return API_3_USER + ISTEMP + "/" + email;
    }
}
