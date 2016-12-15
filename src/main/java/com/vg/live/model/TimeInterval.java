package com.vg.live.model;

import org.stjs.javascript.Date;

public class TimeInterval {
    public Date start;
    public Date end;

    public TimeInterval(Date startTime, Date endTime) {
        start = startTime;
        end = endTime;
    }

    public boolean contains(Date d) {
        double stime = start.getTime();
        double etime = end.getTime();
        double time = d.getTime();
        return stime <= time && time <= etime;
    }

    public boolean overlaps(TimeInterval that) {
        return contains(that.start) || contains(that.end);
    }
}
