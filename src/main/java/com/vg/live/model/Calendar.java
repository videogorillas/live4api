package com.vg.live.model;

import static com.vg.live.model.Internal.mapValues;

import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Calendar implements Doc {
    public long _rev;
    public String id;
    public Map<String, TimeInterval> intervals;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isActive() {
        return true;
    }
    
    public boolean isBusyAt(TimeInterval interval) {
        if (interval == null) {
            return false;
        }
        return mapValues(intervals).some((ti, i, a) -> ti != null && ti.overlaps(interval));
    }
    
}
