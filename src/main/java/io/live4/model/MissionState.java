package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public enum MissionState {
    PENDING, STARTED, CANCELLED, ENDED
}