package com.vg.live.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class LoginRequestData {
    public String l;
    public String p;

    //reset password token
    public String t;

    public LoginRequestData(String login, String pass) {
        l = login;
        p = pass;
    }
}
