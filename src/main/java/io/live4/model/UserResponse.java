package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;

/* Public View for User object.
 This class is using on the WEB side so do not store private secrets,
 tokens, passwords or any field that compromise user private data
 */
@Namespace("live4api")
public class UserResponse {
    public final String id;
    public final String name;
    public final String userpic;
    public final LoginType type;
    public final String intoURL;

    // FullProfile properties
    public String introUrl;
    public String homeTown;
    private final Array<UserActivityResponse> activities;
    public Array<UserActivityResponse> activites;

    public UserResponse(String id, String name, String userpic, LoginType type, String intoURL, String homeTown,
            Array<UserActivityResponse> activities) {
        this.id = id;
        this.name = name;
        this.userpic = userpic;
        this.type = type;
        this.intoURL = intoURL;
        this.homeTown = homeTown;
        this.activities = activities;
    }
}
