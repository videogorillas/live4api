package io.live4.model;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.SyntheticType;

@Namespace("live4api")
public class GeocoderAddressComponent {

    public String longName;
    public String shortName;
    public Array<String> types;
}