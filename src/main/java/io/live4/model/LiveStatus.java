package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public enum LiveStatus {
    SCHEDULED, STANDBY, LIVE, UPLOADING, UPLOADING_METADATA, RECORDED;
}
