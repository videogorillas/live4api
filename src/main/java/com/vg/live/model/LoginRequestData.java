package com.vg.live.model;

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
