package io.live4.js;

import static io.live4.api3.Api3MissionUrls.shareUrl;
import static io.live4.api3.Api3MissionUrls.tokenUrl;

import com.vg.js.bridge.Rx.Observable;

import io.live4.api3.Api3MissionUrls;
import io.live4.js.internal.Requests;
import io.live4.model.Internal;
import io.live4.model.Mission;
import io.live4.model.MissionShareToken;

public class MissionApi extends BaseAsyncDao<Mission> {

    MissionApi(Requests requests, Observable<Mission> updates) {
        super(Mission.class, updates, requests);
    }

    public Observable<String> getShareToken(String missionId) {
        return requests.get(tokenUrl(missionId)).map(
                json -> Internal.typefyJson(json, MissionShareToken.class).token);
    }

    public Observable<Boolean> shareMission(Mission mission, String toEmail) {
        String url = shareUrl(mission.id)+ "?" + Api3MissionUrls.SHARE_TO_EMAIL_PARAM + "=" + toEmail;
        return requests.get(url).map(str -> true);
    }

    public Observable<Boolean> unshareMission(Mission mission, String toEmail) {
        String url = Api3MissionUrls.unshareUrl(mission.id) + "?" + Api3MissionUrls.SHARE_TO_EMAIL_PARAM + "=" + toEmail;
        return requests.get(url).map(str -> true);
    }

    @Override
    protected String getItemUrl(String id) {
        return Api3MissionUrls.getUrl(id);
    }

    @Override
    protected String createItemUrl() {
        return Api3MissionUrls.createUrl();
    }

    @Override
    protected String listUrl(String orgId) {
        return Api3MissionUrls.listUrl(orgId);
    }

    public Observable<String> splitStreamsOnMissionEnd(String mid) {
        return requests.get(Api3MissionUrls.API_3_MISSION + "/split/" + mid);
    }

    public Observable<String> getAudioChatToken() {
        return requests.get(Api3MissionUrls.audioChatTokenUrl()).map(
                json -> Internal.typefyJson(json, MissionShareToken.class).token);
    }
}
