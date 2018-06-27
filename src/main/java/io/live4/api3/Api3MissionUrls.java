package io.live4.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
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

    public static final String _MISSIONS = "/missions";
    public static final String MISSION_SHARE_PARAM = "t";
    public static final String CHAT_TOKEN = "/chatToken";
    public static final String AUDIO_CHAT_TOKEN = "/audioChatToken";
    public static final String BY_MISSION_TOKEN = "/byMissionToken";
    public static final String IS_TOKEN_VALID = "/isTokenValid";


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

    public static String shareMissionUrl(String missionId, String shareToken) {
        return _MISSIONS + "/" + missionId + "?" + MISSION_SHARE_PARAM + "=" + shareToken;
    }

    public static String getUserByMissionToken(String token) {
        return API_3_MISSION + BY_MISSION_TOKEN + "/" + token;
    }

    public static String checkTokenUrl(String token) {
        return API_3_MISSION + IS_TOKEN_VALID + "/" + token;
    }

    public static final String SHARE_TO_EMAIL_PARAM = "toEmail";

    public static String audioChatTokenUrl() {
        return AUDIO_CHAT_TOKEN + "?device=ios";
    }
}