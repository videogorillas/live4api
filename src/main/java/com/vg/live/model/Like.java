package com.vg.live.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Like {
    
    public String uuid;
    public String streamId;
    public StreamId sid;
    public UserResponse user;
    public long startMsec;
    public long ctime;

    public String getId() {
        return uuid;
    }

    @Override
    public String toString() {
        String userid = null;
        if (user != null) {
            userid = user.id;
        }
        return "<Like: streamId=" + streamId + "; from=" + userid + "; startMsec=" + startMsec + ">";
    }
}
