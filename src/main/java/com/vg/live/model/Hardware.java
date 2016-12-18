package com.vg.live.model;

import static com.vg.live.model.Hardware.Availability.AVAILABLE;
import static com.vg.live.model.Hardware.Availability.INUSE;
import static com.vg.live.model.Hardware.Availability.SCHEDULED;
import static com.vg.live.model.Internal.isBlank;

import org.stjs.javascript.SortFunction;

public class Hardware implements Doc {

    public static final String TYPE_MC_BOX = "MC_BOX";
    public static final String TYPE_DRONE = "DRONE";

    public String id;
    public long _rev;
    public String name;
    public String type;
    public String manufacturer;
    public String model;
    public String orgId;
    public boolean active;
    public int port;
    public String externalId;
    public String endpoint;

    public final static SortFunction<Hardware> sortByNameAvailableFirst = (h1, h2) -> {
        int diff = statusLabel(h1._availability).compareTo(statusLabel(h2._availability));
        return diff != 0 ? diff : h1.name.compareTo(h2.name);
    };
    //UI only: meaningless for daos
    //marked as transient to disable GSON serialization
    public transient Availability _availability;
    public transient Calendar _calendar;
    public transient String _orgName;

    public static boolean isValidPortNumber(int port) {
        return port > 1024 && port < 0x10000;
    }

    public boolean isMCBox() {
        return type.equals(TYPE_MC_BOX);
    }

    public boolean isDrone() {
        return type.equals(TYPE_DRONE);
    }

    public static Hardware MCBox(String name) {
        return new Hardware(name, TYPE_MC_BOX);
    }

    public static Hardware drone(String name) {
        return new Hardware(name, TYPE_DRONE);
    }

    public boolean isAvailable() {
        return !isScheduled();
    }

    public boolean isScheduled() {
        return _availability == Availability.SCHEDULED || _availability == INUSE;
    }

    public Hardware setPort(int port) {
        this.port = port;
        return this;
    }

    public boolean belongsToOrg(String orgId) {
        return !isBlank(orgId) && orgId.equals(this.orgId);
    }

    public enum Availability {
        AVAILABLE, SCHEDULED, INUSE
    }

    public Hardware(String name, String type) {
        this.name = name;
        this.type = type;
        active = true;
    }

    public boolean isAssigned() {
        return orgId != null;
    }

    public static String statusLabel(Availability s) {
        if (s == AVAILABLE) {
            return "Available";
        } else if (s == SCHEDULED) {
            return "Scheduled";
        } else if (s == INUSE) {
            return "In use";
        }
        return "Unknown";
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    public Availability getAvailabilityFor(TimeInterval ti) {
        if (this._calendar == null)
            return Availability.AVAILABLE;
        return _calendar.isBusyAt(ti) ? SCHEDULED : AVAILABLE;
    }

}
