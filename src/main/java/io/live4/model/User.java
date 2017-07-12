package io.live4.model;

import static io.live4.js.internal.InternalJSAdapter.$or;
import static io.live4.model.Internal.defaultMap;
import static org.stjs.javascript.JSCollections.$map;

import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class User implements Doc {
    public long _rev;

    public String id;

    public String name;
    public String lastname;
    private long _created;
    private String userpic;
    public String email;
    private LoginType type;

    public String password;
    public AccessToken session;
    public boolean emailVerified;

    public String resetPasswordToken;
    public Long tokenExpireTime;
    public boolean licenseAgreementAccepted;

    private boolean sudo;
    public Map<String, UserProfile> profiles;

    public User(String id, String name, String userpic, long created, LoginType social, String email) {
        this.id = id;
        this.name = name;
        this._created = created;
        this.userpic = userpic;
        this.type = social;
        this.email = email;
        this.profiles = $map();
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
    @Deprecated
    public boolean isActive() {
        return isUserActiveInAnyOrg();
    }

    public boolean isUserActiveInOrg(String orgId) {
        UserProfile userProfile = getProfile(orgId);
        return userProfile != null && userProfile.active;
    }

    public boolean isUserActiveInAnyOrg() {
        for (String orgId : profiles) {
            if (getProfile(orgId).active) {
                return true;
            }
        }
        return false;
    }

    public boolean belongsToOrg(String orgId) {
        UserProfile profile = getProfile(orgId);
        return profile != null;
    }

    public boolean hasCommonOrg(User checkUser) {
        if (checkUser != null) {
            for (String orgId : this.profiles) {
                if (checkUser.belongsToOrg(orgId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getName() {
        return $or(name, id);
    }

    public String getFullName() {
        return $or(name, "") + " " + $or(lastname, "");
    }

    public String getAvatarUrl() {
        return userpic;
    }

    public String getStatusString(String orgId) {
        UserProfile userProfile = getProfile(orgId);
        return userProfile == null ? null : userProfile.active ? "Active" : "Inactive";
    }

    public String getEmail() {
        return email;
    }

    public long created() {
        return _created;
    }

    public void setAccessToken(AccessToken token) {
        session = token;
    }

    public LoginType getType() {
        return type;
    }

    public UserRole getRole(String orgId) {
        UserProfile profile = this.getProfile(orgId);
        if (profile == null) {
            return this.isSuperAdmin() ? UserRole.SUPER_ADMIN : null;
        } else {
            return profile.role;
        }
    }

    public boolean passwordMatches(String password) {
        if (this.password == null) {
            return password == null;
        } else {
            return this.password.equals(password);
        }
    }

    @Override
    public String toString() {
        return id + "@" + getType();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatarUrl(String url) {
        this.userpic = url;
    }

    public boolean isExternal(String orgId) {
        UserProfile profile = getProfile(orgId);
        return profile == null || UserRole.EXTERNAL.equals(profile.role);
    }

    public boolean isSuperAdmin() {
        return sudo;
    }

    public boolean isOrgAdmin(String orgId) {
        return getRole(orgId) == UserRole.ORG_ADMIN;
    }

    public boolean isOrgAdminInAnyOrg() {
        for (String orgId : profiles) {
            if (getRole(orgId).equals(UserRole.ORG_ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public String getFirstOrgId() {
        for(String orgId : defaultMap(profiles)) {
            UserProfile profile = getProfile(orgId);
            if (profile != null && profile.active) {
                return orgId;
            }
        }
        return null;
    }

    public void setRole(String orgId, UserRole role) {
        createProfile(orgId);
        this.getProfile(orgId).role = role;
    }

    public void setSuperAdmin(boolean sudo) {
        this.sudo = sudo;
    }

    public void createProfile(String orgId) {
        UserProfile profile = this.getProfile(orgId);
        if (profile == null) {
            this.addProfile(orgId, new UserProfile());
        }
    }

    public void setUserActive(String orgId, boolean active) {
        this.getProfile(orgId).active = active;
    }

    public String getOrgDepartment(String orgId) {
        return this.getProfile(orgId).department;
    }

    public void setOrgDepartment(String orgId, String department) {
        this.getProfile(orgId).department = department;
    }

    public String getOrgTitle(String orgId) {
        return this.getProfile(orgId).title;
    }

    public void setOrgTitle(String orgId, String title) {
        this.getProfile(orgId).title = title;
    }

    public String getOrgPhone(String orgId) {
        return this.getProfile(orgId).phone;
    }

    public void setOrgPhone(String orgId, String phone) {
        this.getProfile(orgId).phone = phone;
    }

    public String getOrgNotes(String orgId) {
        return this.getProfile(orgId).notes;
    }

    public void setOrgNotes(String orgId, String notes) {
        this.getProfile(orgId).notes = notes;
    }

    public UserProfile getProfile(String orgId) {
        return profiles.$get(orgId);
    }

    public void addProfile(String orgId, UserProfile userProfile) {
        this.profiles.$put(orgId, userProfile);
    }
}
