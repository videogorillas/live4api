package com.vg.live.model;

import static org.stjs.javascript.JSCollections.$array;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;

public class Organization implements Doc {
    public long _rev;
    public String id;
    public String name;
    public String description;
    public boolean active;
    public final long ctime;
    public Array<String> orgAdminUserIds;
    public String logoUrl;
    public Array<String> userIds;
    public Address address;
    public Array<String> hardwareIds;
    public Array<String> missionIds;
    public BillingInfo billingInfo;
    public String externalId;

    // UI only
    public volatile Array<User> _orgAdmins;

    public Organization(String name, String orgAdminUserId) {
        this.name = name;
        ctime = (long) new Date().getTime();
        active = true;
        userIds = $array();
        orgAdminUserIds = $array();
        if (orgAdminUserId != null) {
            userIds.push(orgAdminUserId);
            orgAdminUserIds.push(orgAdminUserId);
        }
        hardwareIds = $array();
        address = new Address();
        billingInfo = new BillingInfo();
    }

    public String getTheBestOrgAdminId() {
        return this.orgAdminUserIds.$get(0);
    }

    public void addUserOrgAdmin(String userId) {
        addUser(userId);
        addOrgAdmin(userId);
    }

    public void addOrgAdmin(String userId) {
        if (orgAdminUserIds.indexOf(userId) == -1) {
            orgAdminUserIds.push(userId);
        }
    }

    public void addUser(String userId) {
        if (userIds.indexOf(userId) == -1) {
            userIds.push(userId);
        }
    }

    public void removeUser(String userId) {
        int idx = userIds.indexOf(userId);
        if (idx != -1) {
            userIds.splice(idx, 1);
        }
        removeOrgAdmin(userId);
    }

    public void removeOrgAdmin(String userId) {
        int idx = orgAdminUserIds.indexOf(userId);
        if (idx != -1) {
            orgAdminUserIds.splice(idx, 1);
        }
    }

    public boolean containsUser(String userId) {
        return userIds != null && userIds.indexOf(userId) != -1;
    }

    public boolean containsHardware(String hwId) {
        return hardwareIds.indexOf(hwId) != -1;
    }


    public void addHardware(String hardwareId) {
        if (hardwareIds.indexOf(hardwareId) == -1) {
            hardwareIds.push(hardwareId);
        }
    }

    public void removeHardware(String hardwareId) {
        int idx = hardwareIds.indexOf(hardwareId);
        if (idx != -1) {
            hardwareIds.splice(idx, 1);
        }
    }

    public Array<String> listHardwareIds() {
        return hardwareIds;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public String getStatus() {
        if (active) {
            return "Active";
        } else {
            return "Inactive";
        }
    }
    
    public boolean hasOnlyOneAdmin(){
        return this.orgAdminUserIds.$length() == 1;
    }
    
}
