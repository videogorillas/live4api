package com.vg.live.model.api3;

public class Api3MissionUrls {
    public static final String API_3_MISSION = "/api/3/mission";
    public static final String TOKEN = "/token";
    public static final String SHARE = "/share";
    public static final String JOIN = "Join";
    public static final String UNSHARE = "/unshare";
    public static final String CANCEL_NOTIFICATION = "/cancelNotification";
    public static final String INVITE = "/invite";

    public static final String OBJECT = "/object";
    public static final String LIST = "/list";

    public static String baseUrl() {
        return API_3_MISSION + OBJECT;
    }
    
    public static String createUrl() {
        return API_3_MISSION + OBJECT;
    }

    public static String updateUrl() {
        return API_3_MISSION + OBJECT;
    }

    public static String getUrl(String id) {
        return API_3_MISSION + OBJECT + "/" + id;
    }

    public static String listUrl(String orgId) {
        return API_3_MISSION + LIST + "/" + orgId;
    }

    public static String tokenUrl(String missionId) {
        return API_3_MISSION + TOKEN + "/" + missionId;
    }

    public static String shareUrl(String missionId) {
        return API_3_MISSION + SHARE + "/" + missionId;
    }

    public static String unshareUrl(String missionId) {
        return API_3_MISSION + UNSHARE + "/" + missionId;
    }

    public static String joinUrl(String missionId) {
        return API_3_MISSION + JOIN + "/" + missionId;
    }

    public static String baseJoinUrl() {
        return API_3_MISSION + JOIN;
    }

    public static String cancelNotificationUrl(String missionId) {
        return API_3_MISSION + CANCEL_NOTIFICATION + "/" + missionId;
    }

    public static String inviteUrl(String missionId) {
        return API_3_MISSION + INVITE + "/" + missionId;
    }

}
