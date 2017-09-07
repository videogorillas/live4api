package io.live4.model;

import static io.live4.model.Internal.containsKey;
import static io.live4.model.Internal.defaultArray;
import static io.live4.model.Internal.defaultMap;
import static io.live4.model.Internal.eq;
import static io.live4.model.MissionRole.OWNER;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class Mission implements Doc {

    public long _rev;
    public String id;
    public String createdByUserId;
    public String orgId;
    public long mtime;
    public String name;
    public String location;

    public Date startTime;
    public Date endTime;
    public String timeZone;
    public Array<String> streamIds;

    // user id -> MissionRole
    public Map<String, MissionRole> roles;

    public Map<String, String> pilots;

    // user id -> join time
    public Map<String, Date> joined;

    //used in UI only, should not be serialized to db, use ids to serialize
    public Array<Hardware> hardware;

    public MissionState state;
    public static final String UNASSIGNED = "Unassigned";

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return state == MissionState.PENDING || state == MissionState.STARTED;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void addStream(String streamId) {
        streamIds = defaultArray(streamIds);
        streamIds.push(streamId);
    }

    public boolean hasStreamId(String streamId) {
        return defaultArray(streamIds).some((_m, i, a) -> _m.equals(streamId));
    }

    public boolean hasOwnerPermissions(User u) {
        return isOrgAdmin(u, this) || isOwner(u, this);
    }

    public boolean hasPilotPermissions(User u) {
        return MissionRole.PILOT == defaultMap(this.roles).$get(u.id) || this.hasOwnerPermissions(u);
    }

    public boolean hasParticipantPermisisons(User u) {
        return this.hasPilotPermissions(u) || MissionRole.PARTICIPANT == defaultMap(this.roles).$get(u.id);
    }

    public void removeUser(String userId) {
        defaultMap(roles).$delete(userId);
    }

    public void addUser(User user, MissionRole role) {
        roles = defaultMap(roles);
        roles.$put(user.id, role);
    }

    public void addPilot(String streamId, User pilot) {
        pilots = defaultMap(pilots);
        pilots.$put(streamId, pilot.id);
    }

    public void removePilot(String streamId) {
        pilots = defaultMap(pilots);
        pilots.$delete(streamId);
    }

    public String getPilotId(String streamId) {
        return defaultMap(pilots).$get(streamId);
    }

    public void changeMissionName(String newMissionName) {
        name = newMissionName;
    }

    public boolean hasUser(String id) {
        return containsKey(roles, id);
    }

    public String getOwnerId() {
        for (String userId : defaultMap(this.roles)) {
            MissionRole missionRole = this.roles.$get(userId);
            if (OWNER == missionRole) {
                return userId;
            }
        }
        return this.createdByUserId;
    }

    public boolean hasOwnerRole() {
        for (String userId : defaultMap(this.roles)) {
            MissionRole missionRole = this.roles.$get(userId);
            if (OWNER == missionRole) {
                return true;
            }
        }
        return false;
    }

    public int countOwners() {
        int count = 0;
        for (String userId : defaultMap(this.roles)) {
            MissionRole missionRole = this.roles.$get(userId);
            if (OWNER == missionRole) {
                count += 1;
            }
        }
        return count;
    }

    public boolean isLive() {
        return MissionState.STARTED.equals(state);
    }

    public boolean isScheduled() {
        return MissionState.PENDING.equals(state);
    }

    public boolean isCompleted() {
        return MissionState.ENDED.equals(state);
    }

    public void addHardware(Hardware h) {
        Array<Hardware> hw = defaultArray(this.hardware);
        boolean found = false;
        for (int i = 0; i < hw.$length(); i++) {
            if (hw.$get(i).id.equals(h.id)) {
                found = true;
                hw.$set(i, h);
            }
        }
        if (!found) {
            hw.push(h);
        }
        this.hardware = hw;
    }

    public TimeInterval getTimeInterval() {
        return new TimeInterval(startTime, endTime);
    }

    public boolean isRunningNow() {
        return this.getTimeInterval().contains(new Date());
    }

    public static boolean isOrgAdmin(User u, Mission m) {
        return u.isOrgAdmin(m.orgId);
    }

    public static boolean isOwner(User u, Mission m) {
        return MissionRole.OWNER == defaultMap(m.roles).$get(u.id);
    }
    public static boolean isScheduler(User u, Mission m) {
        return eq(m.createdByUserId, u.id);
    }
}
