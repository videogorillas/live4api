package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class NameEmail {
    public String email;
    public String name;

    public NameEmail(String email, String name) {
        this.email = email;
        this.name = name;
    }
}