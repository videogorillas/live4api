package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public enum UserRole {
    USER, ORG_ADMIN, SUPER_ADMIN, EXTERNAL
}
