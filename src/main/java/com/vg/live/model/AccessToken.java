package com.vg.live.model;

public class AccessToken {
    public String secret;
    public String access_token;
    public long expires;

    public AccessToken(String token, String secret, long expires) {
        this.access_token = token;
        this.secret = secret;
        this.expires = expires;
    }
}
