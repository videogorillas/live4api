package google.maps;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

import java.math.BigDecimal;

import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.ServerSide;

@STJSBridge
@Namespace(value = "google.maps")
public class LatLng {

    private double lat;
    private double lng;

    @Native
    public LatLng(double lat, double lng, boolean b) {
        this.lat = lat;
        this.lng = lng;
    }

    @Native
    public LatLng(double latitude, double longitude) {
        this(latitude, longitude, false);
    }

    @Native
    public double lng() {
        return lng;
    }

    @Native
    public double lat() {
        return lat;
    }

    public static final int DEFAULT_PRECISION = 6;

    /**
     * @return Returns a string of the form "lat,lng" for this LatLng. We round the lat/lng values to 6 decimal places by default.
     */
    @Native
    @ServerSide
    public String toUrlValue() {
        return toUrlValue(DEFAULT_PRECISION);
    }

    /**
     * @param precision
     *            We round the lat/lng values
     * @return Returns a string of the form "lat,lng" for this LatLng.
     */
    @Native
    @ServerSide
    public String toUrlValue(int precision) {
        return BigDecimal.valueOf(lat).setScale(precision, ROUND_HALF_EVEN).toString() + ","
                + BigDecimal.valueOf(lng).setScale(precision, ROUND_HALF_EVEN).toString();
    }

}
