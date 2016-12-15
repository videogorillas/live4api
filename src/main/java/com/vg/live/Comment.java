package com.vg.live;

import com.vg.live.js.Utils;
import com.vg.live.model.Platform;
import com.vg.live.model.UserResponse;

public class Comment {
    public String uuid;
    public StreamId sid;
    public String streamId;
    public UserResponse user;
    public String body;
    public long startMsec;
    long stopMsec;
    public long ctime;
    public long mtime;

    public String getId() {
        return uuid;
    }

    public String getBody() {
        return Utils.defaultString(body, "");
    }

    public void setBody(String body) {
        this.body = body;
        this.mtime = Platform.currentTimeMillis();
    }

}
