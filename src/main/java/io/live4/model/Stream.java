package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.ServerSide;

import com.google.code.geocoder.model.GeocoderResult;

@Namespace("live4api")
public class Stream {
    public long _rev;

    public static Stream createStream(StreamId sid, Privacy privacy) {
        Stream s = new Stream();
        s.filename = sid.streamId;
        s.userId = sid.userId;
        s.privacy = privacy;
        s.ctime = (long) Date.now();
        return s;
    }

    public String userId;
    public String filename;

    public String startAddress; // TODO: move to StreamLocation object

    @ServerSide
    public Array<GeocoderResult> startGeoCoder; // TODO: move to StreamLocation object
    public StreamLocation startLocation;

    public boolean onCdn;
    public String onYoutube;
    public long mtime;
    public long ctime;

    private LiveStatus status;
    public long views;
    
    //when user started recording
    public long startTimeMsec;

    public String title;

    private Privacy privacy;
    public boolean locationHidden;

    public int avgSpeed;
    public int maxSpeed;
    public int maxAlt;

    public Array<Tag> tags2;
    public int width = -1;
    public int height = -1;

    public String hardwareId;
    
    public String liveCodecs;

    public StreamId sid() {
        return new StreamId(userId, filename);
    }

    public boolean isLive() {
        return getStatus() == LiveStatus.LIVE;
    }

    public boolean isScheduled() {
        return getStatus() == LiveStatus.SCHEDULED || getStatus() == LiveStatus.STANDBY;
    }

    public boolean isUploading() {
        LiveStatus _status = getStatus();
        return _status == LiveStatus.UPLOADING || _status == LiveStatus.UPLOADING_METADATA;
    }

    public boolean isRecorded() {
        return getStatus() == LiveStatus.RECORDED;
    }

    public Privacy getPrivacy() {
        // XXX: default privacy for all streams is PUBLIC;
        if (privacy == null) {
            privacy = Privacy.PUBLIC;
        }
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public String safeStreamId() {
        return userId + "_" + filename.replace(".", "");
    }

    public LiveStatus getStatus() {
        return status;
    }

    public void setStatus(LiveStatus status) {
        this.status = status;
    }

    boolean closed = false;

    //TODO: client explicitly called api close to mark upload finished for ALL files of this stream
    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

}
