package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public enum HwState {
    CLOSED, OPEN, DATA_RECEIVED, DATA_PARSED, BAD_DATA
}