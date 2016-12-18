package com.vg.live.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class LikeResponse {
    public final int total_count;
    public final Array<Like> likes;
    public final boolean can_like = true;
    public final Boolean has_liked;

    public LikeResponse(Array<Like> likes, Boolean has_liked) {
        if (likes != null) {
            this.total_count = likes.$length();
            this.likes = likes;
            this.has_liked = has_liked;
        } else {
            this.total_count = 0;
            this.likes = null;
            this.has_liked = false;
        }
    }
}
