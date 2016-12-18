package io.live4.model;

import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;

@Namespace("live4api")
public class DataSegment {
    // prod
    public long playerTime;
    public StreamLocation location;
    public String nearBy;

    // debug
    private long width;
    private long left;

    public boolean isEmpty;
    public long widthScaled;
    public long leftScaled;
    public TSFile tsfile;
    public String descr;

    public DataSegment(long playerTime, StreamLocation l) {
        this.playerTime = playerTime;
        this.location = l;
    }

    @Native
    public DataSegment() {
    }

    public long getTime() {
        return left;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    @Override
    public String toString() {
        return String.format("%b; w%d; l%d p%d", isEmpty, width, left, playerTime);
    }

    public void scale(int i) {
        widthScaled = Math.max(width / i, 1);
        leftScaled = left / i;
    }
}
