package io.live4.model;

import static org.stjs.javascript.JSCollections.$castArray;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
@ReadOnly //all fields can only be updated by super admin except marked with @ReadWrite
public class Stream implements Doc {
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

    //these fields can be updated by owner of this stream via API
    @ReadWrite public String title;
    @ReadWrite private Privacy privacy;
    @ReadWrite public boolean locationHidden;
    @ReadWrite public Array<Tag> tags2;

    public int avgSpeed;
    public int maxSpeed;
    public int maxAlt;

    public int width = -1;
    public int height = -1;

    public String hardwareId;
    
    public String liveCodecs;

    boolean closed = false;

    public String m3u8;
    public String mpd;
    @Deprecated
    public String dash;
    public String webm;
    public String mp4;
    public String thumb;
    public String md;

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

    //TODO: client explicitly called api close to mark upload finished for ALL files of this stream
    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getMp4() {
        return mp4;
    }

    public String getThumb() {
        return thumb;
    }

    public String getM3u8() {
        return m3u8;
    }

    @Override
    public String getId() {
        return sid().toString();
    }

    @Override
    public void setId(String id) {
        Array<String> split = $castArray(id.split("/"));
        userId = split.$get(0);
        filename = split.$get(1);
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
