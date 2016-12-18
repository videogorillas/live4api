package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public interface Doc {
    String getId();
    
    void setId(String id);

    boolean isActive();
}
