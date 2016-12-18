package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
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
