package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class NewOrgAdminProfile {

    public Organization org;
    public User admin;
    public UserProfile profile;

    public NewOrgAdminProfile(Organization org, User admin, UserProfile profile) {
        this.org = org;
        this.admin = admin;
        this.profile = profile;
    }
}
