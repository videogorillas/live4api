package google.maps;

import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.annotation.STJSBridge;

@STJSBridge
@Namespace(value = "google.maps")
public class LatLngBounds {

    private LatLng southwest, northeast;

    @Native
    //    constructor(sw?: LatLng|LatLngLiteral, ne?: LatLng|LatLngLiteral);
    public LatLngBounds(Object... swne) {
    }

    @Native
    public LatLngBounds extend(LatLng point) {
        throw new RuntimeException("TODO LatLngBounds.extend");
    }

    @Native
    public boolean contains(LatLng latLng) {
        throw new RuntimeException("TODO LatLngBounds.contains");
    }

    @Native
    public LatLng getNorthEast() {
        return northeast;
    }

    @Native
    public LatLng getSouthWest() {
        return southwest;
    }

    @Native
    public boolean isEmpty() {
        throw new RuntimeException("TODO LatLngBounds.isEmpty");
    }

    @Native
    public LatLngBounds union(LatLngBounds other) {
        throw new RuntimeException("TODO LatLngBounds.union");
    }

}
