package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class LoginRequest {
    public String l;
    public String p;

    //reset password token
    public String t;

    public LoginRequest(String login, String pass) {
        l = login;
        p = pass;
    }
}
