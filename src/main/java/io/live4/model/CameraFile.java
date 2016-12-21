package io.live4.model;

import org.stjs.javascript.SortFunction;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class CameraFile {
    public String originalSize; //":"30456","
    public String originalName; //":"402.ts","
    public String size; //":30456,"
    public String file; //":"402.ts","

    public String lastModified; //http date format

    public CameraFile(String file, String original) {
        this.file = file;
        this.originalName = original;
    }

    public final static SortFunction<CameraFile> sortByFilename = (h1, h2) -> {
        if (h1 != null && h2 != null) {
            return h1.file.compareTo(h2.file);
        }
        if (h1 == null && h2 == null) {
            return 0;
        }
        if (h1 != null) {
            return 1;
        }
        return -1;
    };
}