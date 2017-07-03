package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.ServerSide;

@Namespace("live4api")
public class GeocoderResult {
    public Array<String> types;
    public String formattedAddress;
    public Array<GeocoderAddressComponent> addressComponents;
    public GeocoderGeometry geometry;
    public boolean partialMatch;

    @ServerSide
    public String getFormattedAddress() {
        return formattedAddress;
    }

    @ServerSide
    public GeocoderGeometry getGeometry() {
        return geometry;
    }
}
