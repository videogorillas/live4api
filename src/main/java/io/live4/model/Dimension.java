package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Dimension {
    public int width;
    public int height;

    public Dimension(int w, int h) {
        width = w;
        height = h;
    }

}