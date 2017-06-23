package io.live4.model;

import static io.live4.model.Privacy.PRIVATE;

public class StreamPermissions {
    public static boolean canUpdateStreamById(StreamId sid, User user) {
        if (sid == null || user == null) {
            return false;
        }
        return userOwnsStream(sid, user);
    }

    private static boolean userOwnsStream(StreamId sid, User user) {
        return user.isSuperAdmin() || sid.userId.equals(user.getId());
    }
    
    public static boolean canGetStreamById(StreamId sid, User user) {
        if (sid == null || user == null) {
            return false;
        }
        return userOwnsStream(sid, user);
    }
    
    public static boolean canGetStream(Stream stream, User user) {
        if (stream == null || user == null) {
            return false;
        }

        return userOwnsStream(stream.sid(), user) || !PRIVATE.equals(stream.getPrivacy());
    }
}
