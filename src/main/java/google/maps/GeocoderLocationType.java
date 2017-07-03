package google.maps;

import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.STJSBridge;

@Namespace(value = "google.maps")
@STJSBridge
public enum GeocoderLocationType {
    APPROXIMATE, GEOMETRIC_CENTER, RANGE_INTERPOLATED, ROOFTOP, UNKNOWN
}
