package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class StreamResponse extends Stream {
    public String streamId;

    public UserResponse user;
    
    public Array<Tag> tags;
    public long durationMsec;

    public String embedUrl;
    public String landUrl;

    public LikeResponse likes;
    public CommentResponse comments;
    public String hostUrl;
    public String flash;

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
