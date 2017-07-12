package io.live4.model;

import org.stjs.javascript.Date;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class HWLogEntry {

    public final String hwId;
    public final String missionId;
    public final Date startTime;
    public final String missionOwner;
    public final String action;
    public final String hwName;
    public final String hwType;
    public final String orgId;
    public final int hwPort;
    public final String missionName;
    public final String missionState;
    public final String location;
    public final Date timestamp;

    public HWLogEntry(Hardware hw, Mission m, String action, String missionOwner) {
        this.timestamp = new Date();
        this.hwId = hw.id;
        this.missionId = m.getId();
        this.action = action;
        this.hwName = hw.name;
        this.hwType = hw.type;
        this.orgId = hw.orgId;
        this.hwPort = hw.port;
        this.missionName = m.name;
        this.missionState = m.state.toString();
        this.location = m.location;
        this.startTime = m.startTime;
        this.missionOwner = missionOwner;
    }
}
