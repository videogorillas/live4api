package com.vg.live.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public interface Doc {
    String getId();
    
    void setId(String id);

    boolean isActive();
}
