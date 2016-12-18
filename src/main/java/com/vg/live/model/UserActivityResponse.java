package com.vg.live.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class UserActivityResponse {
    public String thumb;
    public Array<String> hashTags;
}
