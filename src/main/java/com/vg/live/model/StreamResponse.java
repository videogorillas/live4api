package com.vg.live.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;

public class StreamResponse extends Stream {
    public String streamId;

    public UserResponse user;
    
    public Array<Tag> tags;
    public String m3u8;
    public String dash;
    public String webm;
    public String mp4;
    public String thumb;
    public String md;
    public long durationMsec;

    public String nearby;
    public String embedUrl;
    public String landUrl;

    public String city;

    public LikeResponse likes;
    public CommentResponse comments;
    public String hostUrl;
    public String flash;

    public String getMp4() {
        return mp4;
    }

    public String getThumb() {
        return thumb;
    }

    public String getM3u8() {
        return m3u8;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public String getFlash() {
        return flash;
    }

    public String userpic() {
        return user != null ? user.userpic : null;
    }
    
    public String username() {
        return user != null ? user.name : null;
    }

    public String isoDate() {
        return new Date(ctime).toISOString();
    }
}
