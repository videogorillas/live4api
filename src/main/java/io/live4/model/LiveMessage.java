package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class LiveMessage {
    public String streamId;

    public StreamResponse stream;
    public Array<DataSegment> nearby;
    public Long durationHLS;
    public Long durationDash;
    public Array<DataSegment> map;

    public String action;
    public Calendar calendar;
    public Organization org;
    public Hardware hardware;
    public Mission mission;
    public User user;

    public boolean hasMap() {
        return this.map != null;
    }

    public boolean hasDuration() {
        return this.durationDash != null || this.durationHLS != null;
    }

    public boolean hasNearby() {
        return this.nearby != null;
    }

    public boolean hasStream() {
        return this.stream != null;
    }

    public static LiveMessage subscribeStream(String sid) {
        LiveMessage tm = new LiveMessage();
        tm.action = "stream/subscribe";
        tm.streamId = sid;
        return tm;
    }

    public static LiveMessage unsubscribeStream(String sid) {
        LiveMessage tm = new LiveMessage();
        tm.action = "stream/unsubscribe";
        tm.streamId = sid;
        return tm;
    }

    public static LiveMessage subscribe(String what) {
        LiveMessage tm = new LiveMessage();
        tm.action = what + "/subscribe";
        return tm;
    }

}
