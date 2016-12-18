package com.vg.live.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Tag {

    public String id;
    public String name;
    long startMsec;
    long stopMsec;

    public Tag(String id, String name) {
        this.id = id;
        this.name = name;
        this.startMsec = 0;
        this.stopMsec = 0;
    }

    @Override
    public String toString() {
        return "Tag<" + id + "=" + name + ">";
    }
}
