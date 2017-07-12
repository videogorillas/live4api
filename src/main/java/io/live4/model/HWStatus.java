package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class HWStatus implements Doc{
    public String id;
    public static final String API_HWSTATUS = "/api/3/hwstatus";
    public static final String OBJECT = "/object";
    public static final String LIST = "/list";

    public String hwId;
    public HwState status;
    public long mtime;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return !HwState.CLOSED.equals(this.status);
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public static HWStatus newStatus(String hwid, HwState status) {
        HWStatus hws = new HWStatus();
        hws.hwId = hwid;
        hws.status = status;
        return hws;
    }
}
