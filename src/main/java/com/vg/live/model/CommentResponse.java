package com.vg.live.model;

import org.stjs.javascript.Array;

public class CommentResponse {
    public final int total_count;
    public final Array<Comment> comments;

    public CommentResponse(Array<Comment> comments) {
        if (comments != null) {
            this.total_count = comments.$length();
            this.comments = comments;
        } else {
            this.total_count = 0;
            this.comments = null;
        }
    }
}
