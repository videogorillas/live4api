package io.live4.model;

import static io.live4.model.Internal.containsKey;
import static io.live4.model.Internal.defaultMap;
import static io.live4.model.Internal.eq;
import static io.live4.model.Mission.isOrgAdmin;
import static io.live4.model.Mission.isOwner;
import static io.live4.model.Mission.isScheduler;
import static io.live4.model.MissionRole.UNKNOWN;
import static io.live4.model.UserRole.EXTERNAL;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class MissionPermissions {
    public static boolean canEditMisson(User u, Mission m) {
        return m.hasOwnerPermissions(u);
    }

    public static boolean canEditStream(User u, Mission m) {
        return m.hasOwnerPermissions(u);
    }

    public static boolean canAddUser(User u, Mission m) {
        return m.hasOwnerPermissions(u);
    }

    public static boolean canEndMission(User u, Mission m) {
        return m.hasOwnerPermissions(u);
    }

    public static boolean canEditLocations(User u, Mission m) {
        return m.hasOwnerPermissions(u);
    }

    public static boolean canAssignPilot(User u, Mission m) {
        return m.hasPilotPermissions(u);
    }

    public static boolean canUseChat(User u, Mission m) {
        return m.hasParticipantPermisisons(u);
    }

    public static boolean canAddSources(User u, Mission m) {
        return m.hasParticipantPermisisons(u);
    }

    public static boolean hasOneOwner(Mission m) {
        return m.countOwners() == 1;
    }

    public static boolean canViewCompletedMission(User u, Mission m) {
        return (MissionState.ENDED == m.state) && (containsKey(m.roles, u.id) || Mission.isOrgAdmin(u, m));
    }

    public static boolean canShareMission(User user, Mission mission) {
        if (mission == null || user == null)
            return false;

        if (user.isSuperAdmin()) {
            return true;
        }

        if (null == user.getRole(mission.orgId) || EXTERNAL == user.getRole(mission.orgId)) {
            return false;
        }

        MissionRole role = defaultMap(mission.roles).$get(user.id);
        if (UNKNOWN == role || null == role) {
            return false;
        }
        if (!user.belongsToOrg(mission.orgId)) {
            return false;
        }

        return true;
    }

    public static boolean canRemoveUser(Mission mission, User me, User you) {
        String meId = me.id;
        String youId = you.id;
        if (mission == null || meId == null || youId == null)
            return false;
        boolean notRemovingMyself = !eq(youId, meId);
        boolean notRemovingOwnerOfMission = !isOwner(you, mission);
        return mission.hasOwnerPermissions(me) && notRemovingMyself && notRemovingOwnerOfMission;
    }

    public static boolean canJoinMission(Mission mission, String userId) {
        return canPreviewMission(mission) && mission.hasUser(userId);
    }

    public static boolean canPreviewMission(Mission mission) {
        return mission != null && mission.state != MissionState.CANCELLED;
    }

    public static boolean canViewMission(Mission mission, User user) {
        boolean bool = canPreviewMission(mission);
        return (bool && mission.hasUser(user.id)) ||
                (bool && isOrgAdmin(user, mission) && !mission.hasUser(user.id));
    }

    public static boolean canStartMission(User u, Mission m) {
    //        // KMC-388
    //        // A mission can be started by the Mission Owner and / or Mission
    //        // Scheduler, Org Admin
            return isScheduler(u, m)|| isOrgAdmin(u, m) || isOwner(u, m);
        }
    
    public static boolean userRemoved(Mission oldMisison, Mission newMission, User me) {
        return oldMisison != null && oldMisison.roles.$get(me.getId()) != null && newMission.roles.$get(me.getId()) == null;
    }
}
