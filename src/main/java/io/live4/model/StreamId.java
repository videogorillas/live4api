package io.live4.model;

import javax.ws.rs.PathParam;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class StreamId {
    //NOTE: these fields are intentionally final - this class is supposed to be immutable
    public final String userId;
    
    //TODO: rename streamId to ololo
    public final String streamId;

    public StreamId(@PathParam("userId") String userId, @PathParam("streamId") String streamId) {
        this.userId = userId;
        this.streamId = streamId;
    }

    @Override
    public String toString() {
        return userId + "/" + streamId;
    }

    private transient int _hashCode;

    @Override
    public int hashCode() {
        if (_hashCode == 0) {
            _hashCode = toString().hashCode();
        }
        return _hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    public static StreamId sid(String userId, String streamId) {
        return new StreamId(userId, streamId);
    }
}
