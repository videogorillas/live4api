package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class UserProfile {
    public String orgId;
    public String department;
    public String title;
    public String phone;
    public String notes;
    public UserRole role;
    public boolean active;
}