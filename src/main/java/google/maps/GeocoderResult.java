package google.maps;

import org.stjs.javascript.Array;

public class GeocoderResult {
    public Array<String> types;
    public String formattedAddress;
    public Array<GeocoderAddressComponent> addressComponents;
    public GeocoderGeometry geometry;
    public boolean partialMatch;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public GeocoderGeometry getGeometry() {
        return geometry;
    }
}
