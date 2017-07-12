package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class MissionShareToken {
    public String token;
    public String missionId;
    public String userId;
    public String invitedId;
}