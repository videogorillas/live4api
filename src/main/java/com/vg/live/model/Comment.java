package com.vg.live.model;

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
        return Internal.defaultString(body, "");
    }

    public void setBody(String body) {
        this.body = body;
        this.mtime = Internal.currentTimeMillis();
    }

}
