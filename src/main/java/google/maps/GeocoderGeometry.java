package google.maps;

public class GeocoderGeometry {

    public LatLng location;
    public GeocoderLocationType locationType;
    public LatLngBounds viewport;
    public LatLngBounds bounds;

    public LatLng getLocation() {
        return location;
    }
}
