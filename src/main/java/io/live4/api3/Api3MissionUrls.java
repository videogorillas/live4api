package io.live4.api3;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Api3MissionUrls {
    public static final String API_3_MISSION = "/api/3/mission";
    public static final String TOKEN = "/token";
    public static final String SHARE = "/share";
    public static final String JOIN_BY_TOKEN = "/joinByToken";
    public static final String UNSHARE = "/unshare";
    public static final String CANCEL_NOTIFICATION = "/cancelNotification";
    public static final String INVITE = "/invite";

    public static final String OBJECT = "/object";
    public static final String LIST = "/list";

    public static final String _MISSIONS = "/missions";
    public static final String MISSION_SHARE_PARAM = "t";
    public static final String CHAT_TOKEN = "/chatToken";
    public static final String AUDIO_CHAT_TOKEN = "/audioChatToken";
    public static final String USER_BY_MISSION_TOKEN = "/userByMissionToken";
    public static final String ORG_ID_BY_MISSION_TOKEN = "/orgIdByMissionToken";
    public static final String ADD_EXTERNAL_PROFILE_BY_MISSION_TOKEN = "/addExternalProfileByMissionToken";
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

    public static String joinByTokenUrl(String token) {
        return API_3_MISSION + JOIN_BY_TOKEN + "/" + token;
    }

    public static String baseJoinByTokenUrl() {
        return API_3_MISSION + JOIN_BY_TOKEN;
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

    public static String createLoggedExternalUserByMissionToken(String token) {
        return API_3_MISSION + USER_BY_MISSION_TOKEN + "/" + token;
    }

    public static String getOrgIdByMissionToken(String token) {
        return API_3_MISSION + ORG_ID_BY_MISSION_TOKEN + "/" + token;
    }

    public static String addExternalProfileByMissionToken(String token) {
        return API_3_MISSION + ADD_EXTERNAL_PROFILE_BY_MISSION_TOKEN + "/" + token;
    }

    public static String checkTokenUrl(String token) {
        return API_3_MISSION + IS_TOKEN_VALID + "/" + token;
    }

    public static final String SHARE_TO_EMAIL_PARAM = "toEmail";

    public static String audioChatTokenUrl() {
        return AUDIO_CHAT_TOKEN + "?device=ios";
    }
}