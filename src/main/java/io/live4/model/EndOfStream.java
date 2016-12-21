package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class EndOfStream {
    public Array<CameraFile> files;
    public static final String ENDOFSTREAM_JS = "endofstream.js";
    public static final String ENDOFSTREAM_JS_GZ = "endofstream.js.gz";
    
    public EndOfStream() {
        files = new Array<>();
    }
}