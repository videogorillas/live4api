package io.live4.model;

import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.ServerSide;
import org.stjs.javascript.annotation.SyntheticType;

import google.maps.GeocoderLocationType;
import google.maps.LatLng;
import google.maps.LatLngBounds;

@Namespace("live4api")
public class GeocoderGeometry {

    public LatLng location;
    public GeocoderLocationType locationType;
    public LatLngBounds viewport;
    public LatLngBounds bounds;

    @ServerSide
    public LatLng getLocation() {
        return location;
    }
}
