package io.live4.model;

import static io.live4.model.Internal.tryParseDate;

import org.stjs.javascript.Date;
import org.stjs.javascript.SortFunction;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.functions.Function1;

@Namespace("live4api")
public class StreamLocation {

    public static StreamLocation speedLocation(String timestamp, double speed) {
        StreamLocation l = new StreamLocation(timestamp);
        l.speed = speed;
        return l;
    }

    public static StreamLocation latLng(String timestamp, double latitude, double longitude) {
        StreamLocation l = new StreamLocation(timestamp);
        l.latitude = latitude;
        l.longitude = longitude;
        return l;
    }
 
    public final static SortFunction<StreamLocation> sortByTime = (h1, h2) -> {
        if (h1 != null && h2 != null) {
            return (int) (h1.getTime() - h2.getTime());
        }
        if (h1 == null && h2 == null) {
            return 0;
        }
        if (h1 != null) {
            return 1;
        }
        return -1;
    };
    
    @Override
    public int hashCode() {
        return timestamp == null ? -42 : timestamp.hashCode();
    }

    private StreamLocation(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double altitude; //": 171.9877166748047,
    public Double course; //": -1,
    public Double horizontalAccuracy;//": 65,
    public Double latitude; //": 34.15291825095102,
    public Double longitude;//": -118.3410521886585,
    public Double speed; //": -1,
    public String timestamp; //": "2014-07-15T11:32:57-0700", or "1405449177000"
    public Double verticalAccuracy; //": 31.86548475470511
    public Double playerTime;

    //UI only
    public String streamId;

    public double getSpeed() {
        return speed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private transient long timeMsec = 0;
    
    public static final Function1<StreamLocation, Boolean> accurateLocations = l -> l.speed >= 5
            || l.horizontalAccuracy <= 20;

    public long getTime() {
        if (timeMsec == 0) {
            if (Internal.isJava) {
                timeMsec = tryParseDate(timestamp);
            } else {
                return (long) new Date(timestamp).getTime();
            }
        }
        return timeMsec;
    }

    public String lalo() {
        return latitude + "," + longitude;
    }

}