package io.live4.apiclient;

import static io.live4.apiclient.internal.HttpUtils.GET;
import static io.live4.apiclient.internal.HttpUtils.JSON_MIMETYPE;
import static io.live4.apiclient.internal.HttpUtils.LAST_MODIFIED;
import static io.live4.apiclient.internal.HttpUtils.OCTET_STREAM;
import static io.live4.apiclient.internal.HttpUtils.httpDateFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;

import io.live4.apiclient.internal.HttpUtils;
import io.live4.model.Hardware;
import io.live4.model.LoginRequestData;
import io.live4.model.Mission;
import io.live4.model.Stream;
import io.live4.model.StreamId;

public class ApiRequest {

    private ServerUrl serverUrl;
    private Gson gson;

    public ApiRequest(ServerUrl serverUrl, Gson gson) {
        this.gson = gson;
        this.serverUrl = serverUrl;
    }
    
    String gsonToString(Object o) {
        return gson.toJson(o);
    }

    static byte[] gzip(String json) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GZIPOutputStream gz = new GZIPOutputStream(os);
        gz.write(json.getBytes());
        gz.finish();
        gz.close();
        return os.toByteArray();
    }

    public Request uploadJson(StreamId sid, String filename, Object o) {
        return uploadJson(sid, filename, System.currentTimeMillis(), gsonToString(o));
    }

    public Request uploadJson(StreamId sid, String filename, long mtime, String json) {
        String buildUrl = serverUrl.uploadUrl(sid, filename);
        Builder builder = new Builder().header(LAST_MODIFIED, httpDateFormat(mtime));
        try {
            builder.url(buildUrl + ".gz").post(RequestBody.create(OCTET_STREAM, gzip(json)));
        } catch (IOException e) {
            builder.url(buildUrl).post(RequestBody.create(JSON_MIMETYPE, json));
        }
    
        return builder.build();
    }

    public Request uploadFile(StreamId sid, File file) {
        String buildUrl = serverUrl.uploadUrl(sid, file.getName());
        Builder builder = new Builder().header(LAST_MODIFIED, httpDateFormat(file.lastModified()));
        builder.url(buildUrl).post(RequestBody.create(OCTET_STREAM, file));
        return builder.build();
    }

    public Request uploadLogs(String logs) {
        Builder builder = new Builder().header(LAST_MODIFIED, httpDateFormat(new Date().getTime()));
        builder.url(serverUrl.uploadLog(System.currentTimeMillis())).post(RequestBody.create(HttpUtils.OCTET_STREAM, logs));
        return builder.build();
    }

    public Request login(String email, String password) {
        LoginRequestData lrd = new LoginRequestData(email, password);
        return HttpUtils.postAsJsonRequest(serverUrl.login(), gsonToString(lrd));
    }

    public Request resetPassword(String email) {
        LoginRequestData lrd = new LoginRequestData(email, null);
        return HttpUtils.postAsJsonRequest(serverUrl.resetPassword(), gsonToString(lrd));
    }

    public Request getUser(String userId) {
        return GET(serverUrl.getUser(userId));
    }

    public Request getOrganization(String orgId) {
        return GET(serverUrl.getOrg(orgId));
    }

    public Request createStream(Stream sr) {
        return HttpUtils.postAsJsonRequest(serverUrl.createStreamUrl(), gsonToString(sr));
    }

    public Request accessToken() {
        return HttpUtils.GET(serverUrl.chatToken());
    }

    public Request listMissions(String orgId) {
        return GET(serverUrl.listMissions(orgId));
    }

    public Request getMission(String id) {
        return GET(serverUrl.getMission(id));
    }

    public Request listHw(String orgId) {
        return GET(serverUrl.listHw(orgId));
    }

    public Request createHw(Hardware hw) {
        return HttpUtils.postAsJsonRequest(serverUrl.createHw(), gsonToString(hw));
    }

    public Request createMission(Mission m) {
        return HttpUtils.postAsJsonRequest(serverUrl.createMission(), gsonToString(m));
    }

    public Request updateMission(Mission m) {
        return HttpUtils.putAsJsonRequest(serverUrl.updateMission(), gsonToString(m));
    }

    public Request getStream(StreamId id) {
        return GET(serverUrl.getStream(id));
    }

    public Request getLocations(StreamId id) {
        return GET(serverUrl.getLocations(id));
    }

    public Request updateStreamTitle(String streamId, String title) {
        Stream sr = new Stream();
        sr.title = title;
        return HttpUtils.putAsJsonRequest(serverUrl.updateStreamTitleUrl(streamId), gsonToString(sr));
    }

}
