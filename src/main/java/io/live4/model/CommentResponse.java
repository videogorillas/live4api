package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
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
