package io.live4.apiclient;

import com.squareup.okhttp.HttpUrl;

import static io.live4.api2.Api2Urls.API_2_UPLOAD_AV;
import static io.live4.api2.Api2Urls.API_2_UPLOAD_LOG;
import static io.live4.api3.Api3Urls.API_3_ORG_STORAGE_GET;

import io.live4.api1.Api1StreamUrls;
import io.live4.api2.Api2Urls;
import io.live4.api3.Api3HwUrls;
import io.live4.api3.Api3MissionUrls;
import io.live4.api3.Api3OrgUrls;
import io.live4.api3.Api3StreamUrls;
import io.live4.api3.Api3Urls;
import io.live4.api3.Api3UserUrls;
import io.live4.model.Mission.ShareToken;
import io.live4.model.StreamId;
import io.live4.model.User;

public class ServerUrl {
    private final String serverUrl;
    private final String hostname;

    public ServerUrl(String url) {
        this.serverUrl = url;
        HttpUrl parse = HttpUrl.parse(url);
        this.hostname = parse.host();

    }
    public String getHostname() {
        return hostname;
    }

    public String getBaseUrl() {
        return serverUrl;
    }

    public String updateStreamTitleUrl(String streamId) {
        return serverUrl + Api2Urls.updateStreamTitleUrl(streamId);
    }

    public String uploadUrl(StreamId sid, String filename) {
        String buildUrl = String.format(serverUrl + API_2_UPLOAD_AV + "/%s/%s/%s", sid.userId, sid.streamId, filename);
        return buildUrl;
    }

    public String uploadLog(long now) {
        return serverUrl + API_2_UPLOAD_LOG + "/" + now;
    }

    public String wsPushVideo(StreamId id) {
        return serverUrl + Api3Urls.wsPushVideo(id.toString());
    }

    public String login() {
        return serverUrl + Api3Urls.API_3_LOGIN;
    }

    public String resetPassword() {
        return serverUrl + Api3Urls.API_3_RESETPASSWORD;
    }

    public String getUser(String userId) {
        return serverUrl + Api3UserUrls.getUrl(userId);
    }

    public String getOrg(String orgId) {
        return serverUrl + Api3OrgUrls.getUrl(orgId);
    }

    public String createStreamUrl() {
        return serverUrl + Api2Urls.API_2_STREAM;
    }
    
    public String updateStreamUrl() {
        return serverUrl + Api3StreamUrls.updateUrl();
    }

    public String listMissions(String orgId) {
        return serverUrl + Api3MissionUrls.listUrl(orgId);
    }

    public String getMission(String id) {
        return serverUrl + Api3MissionUrls.getUrl(id);
    }

    public String shareMission(ShareToken shareToken) {
        return serverUrl + Api3MissionUrls.shareMissionUrl(shareToken.missionId, shareToken.token);
    }

    public String getStream(StreamId id) {
        return serverUrl + Api1StreamUrls.getUrl(id.toString());
    }

    public String getLocations(StreamId id) {
        return serverUrl + Api3Urls.locationsUrl(id.toString());
    }

    public String createHw() {
        return serverUrl + Api3HwUrls.createUrl();
    }

    public String createMission() {
        return serverUrl + Api3MissionUrls.createUrl();
    }

    public String updateMission() {
        return serverUrl + Api3MissionUrls.updateUrl();
    }

    public String listHw(String orgId) {
        return serverUrl + Api3HwUrls.listUrl(orgId);
    }

    public String shareToken(String missionId) {
        return serverUrl + Api3MissionUrls.tokenUrl(missionId);
    }
    
    public String chatToken() {
        return serverUrl + Api3MissionUrls.CHAT_TOKEN;
    }

    @Override
    public String toString() {
        return serverUrl;
    }

    public String userAvatarUrl(User user) {
        return serverUrl + API_3_ORG_STORAGE_GET + user.getAvatarUrl();
    }
}
