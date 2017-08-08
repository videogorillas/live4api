import {Observable} from 'rx';
export class AccessToken  {
    constructor(token: string, secret: string, expires: number);

    secret: string;
    access_token: string;
    expires: number;

}
export class Address  {
    line1: string;
    line2: string;
    city: string;
    state: string;
    zip: string;
    country: string;

    asOneLine(): string;
}
export class BillingInfo  {
    account: string;
    conf: string;
    dataPlan: string;
    amount: string;
    card: string;
    information: string;

}
export class Calendar implements Doc {
    _rev: number;
    id: string;
    intervals: {[id: string]: TimeInterval};

    getId(): string;
    setId(id: string);
    isActive(): boolean;
    isBusyAt(interval: TimeInterval): boolean;
}
export class CameraFile  {
    constructor(file: string, original: string);

    originalSize: string;
    originalName: string;
    size: string;
    file: string;
    lastModified: string;
    static sortByFilename: (h1: CameraFile, h2: CameraFile) => number;

}
export class Comment  {
    uuid: string;
    sid: StreamId;
    streamId: string;
    user: UserResponse;
    body: string;
    startMsec: number;
    ctime: number;
    mtime: number;

    getId(): string;
    getBody(): string;
    setBody(body: string);
}
export class CommentResponse  {
    constructor(comments: Comment[]);

    total_count: number;
    comments: Comment[];

}
export class DataSegment  {
    constructor(playerTime: number, l: StreamLocation);

    playerTime: number;
    location: StreamLocation;
    nearBy: string;
    isEmpty: boolean;
    widthScaled: number;
    leftScaled: number;
    tsfile: TSFile;
    descr: string;

    toString(): string;
    scale(i: number);
    setWidth(width: number);
    setLeft(left: number);
    getTime(): number;
}
export class Dimension  {
    constructor(w: number, h: number);

    width: number;
    height: number;

}
export interface Doc  {

    getId(): string;
    setId(id: string);
    isActive(): boolean;
}
export class EndOfStream  {
    files: CameraFile[];
    static ENDOFSTREAM_JS: string;
    static ENDOFSTREAM_JS_GZ: string;

}
export class HWLogEntry  {
    constructor(hw: Hardware, m: Mission, action: string, missionOwner: string);

    hwId: string;
    missionId: string;
    startTime: Date;
    missionOwner: string;
    action: string;
    hwName: string;
    hwType: string;
    orgId: string;
    hwPort: number;
    missionName: string;
    missionState: string;
    location: string;
    timestamp: Date;

}
export class HWStatus implements Doc {
    id: string;
    static API_HWSTATUS: string;
    static OBJECT: string;
    static LIST: string;
    hwId: string;
    status: HwState;
    mtime: number;

    getId(): string;
    setId(id: string);
    isActive(): boolean;
    static newStatus(hwid: string, status: HwState): HWStatus;
}
export class Hardware implements Doc {
    constructor(name: string, type: string);

    static TYPE_MC_BOX: string;
    static TYPE_DRONE: string;
    static TYPE_ANDROID: string;
    id: string;
    _rev: number;
    name: string;
    type: string;
    manufacturer: string;
    model: string;
    orgId: string;
    active: boolean;
    port: number;
    externalId: string;
    endpoint: string;
    static sortByNameAvailableFirst: (h1: Hardware, h2: Hardware) => number;
    _availability: HwAvailability;
    _calendar: Calendar;
    _orgName: string;

    getId(): string;
    setId(id: string);
    isActive(): boolean;
    static isValidPortNumber(port: number): boolean;
    isMCBox(): boolean;
    isDrone(): boolean;
    static MCBox(name: string): Hardware;
    static drone(name: string): Hardware;
    static android(name: string): Hardware;
    isAvailable(): boolean;
    isScheduled(): boolean;
    setPort(port: number): Hardware;
    belongsToOrg(orgId: string): boolean;
    isAssigned(): boolean;
    static statusLabel(s: HwAvailability): string;
    getAvailabilityFor(ti: TimeInterval): HwAvailability;
}
export enum HwAvailability { AVAILABLE, SCHEDULED, INUSE }
export enum HwState { CLOSED, OPEN, DATA_RECEIVED, DATA_PARSED, BAD_DATA }
export class Like  {
    uuid: string;
    streamId: string;
    sid: StreamId;
    user: UserResponse;
    startMsec: number;
    ctime: number;

    toString(): string;
    getId(): string;
}
export class LikeResponse  {
    constructor(likes: Like[], has_liked: boolean);

    total_count: number;
    likes: Like[];
    can_like: boolean;
    has_liked: boolean;

}
export class LiveMessage  {
    streamId: string;
    stream: StreamResponse;
    nearby: DataSegment[];
    durationHLS: number;
    durationDash: number;
    map: DataSegment[];
    action: string;
    calendar: Calendar;
    org: Organization;
    hardware: Hardware;
    mission: Mission;
    user: User;
    hwStatus: HWStatus;

    hasMap(): boolean;
    hasDuration(): boolean;
    hasNearby(): boolean;
    hasStream(): boolean;
    static subscribeStream(sid: string): LiveMessage;
    static unsubscribeStream(sid: string): LiveMessage;
    static subscribe(what: string): LiveMessage;
}
export enum LiveStatus { SCHEDULED, STANDBY, LIVE, UPLOADING, UPLOADING_METADATA, RECORDED }
export class LoginRequest  {
    constructor(login: string, pass: string);

    l: string;
    p: string;
    t: string;

}
export enum LoginType { Facebook, Twitter, Email }
export class Mission implements Doc {
    _rev: number;
    id: string;
    createdByUserId: string;
    orgId: string;
    mtime: number;
    name: string;
    location: string;
    startTime: Date;
    endTime: Date;
    timeZone: string;
    streamIds: string[];
    roles: {[id: string]: MissionRole};
    pilots: {[id: string]: string};
    joined: {[id: string]: Date};
    hardware: Hardware[];
    state: MissionState;
    static UNASSIGNED: string;

    getId(): string;
    isLive(): boolean;
    setId(id: string);
    isActive(): boolean;
    isScheduled(): boolean;
    addStream(streamId: string);
    hasStreamId(streamId: string): boolean;
    hasOwnerPermissions(u: User): boolean;
    static isOrgAdmin(u: User, m: Mission): boolean;
    static isOwner(u: User, m: Mission): boolean;
    hasPilotPermissions(u: User): boolean;
    hasParticipantPermisisons(u: User): boolean;
    removeUser(userId: string);
    addUser(user: User, role: MissionRole);
    addPilot(streamId: string, pilot: User);
    removePilot(streamId: string);
    getPilotId(streamId: string): string;
    changeMissionName(newMissionName: string);
    hasUser(id: string): boolean;
    getOwnerId(): string;
    hasOwnerRole(): boolean;
    countOwners(): number;
    isCompleted(): boolean;
    addHardware(h: Hardware);
    getTimeInterval(): TimeInterval;
    isRunningNow(): boolean;
    static isScheduler(u: User, m: Mission): boolean;
}
export class MissionPermissions  {

    static canEditMisson(u: User, m: Mission): boolean;
    static canEditStream(u: User, m: Mission): boolean;
    static canAddUser(u: User, m: Mission): boolean;
    static canEndMission(u: User, m: Mission): boolean;
    static canEditLocations(u: User, m: Mission): boolean;
    static canAssignPilot(u: User, m: Mission): boolean;
    static canUseChat(u: User, m: Mission): boolean;
    static canAddSources(u: User, m: Mission): boolean;
    static hasOneOwner(m: Mission): boolean;
    static canViewCompletedMission(u: User, m: Mission): boolean;
    static canShareMission(user: User, mission: Mission): boolean;
    static canRemoveUser(mission: Mission, me: User, you: User): boolean;
    static canJoinMission(mission: Mission, userId: string): boolean;
    static canPreviewMission(mission: Mission): boolean;
    static canViewMission(mission: Mission, user: User): boolean;
    static canStartMission(u: User, m: Mission): boolean;
    static userRemoved(oldMisison: Mission, newMission: Mission, me: User): boolean;
}
export enum MissionRole { UNKNOWN, PILOT, PARTICIPANT, OBSERVER, OWNER }
export class MissionShareToken  {
    token: string;
    missionId: string;
    userId: string;
    invitedId: string;

}
export enum MissionState { PENDING, STARTED, CANCELLED, ENDED }
export class NameEmail  {
    constructor(email: string, name: string);

    email: string;
    name: string;

}
export class NewOrgAdminProfile  {
    constructor(org: Organization, admin: User, profile: UserProfile);

    org: Organization;
    admin: User;
    profile: UserProfile;

}
export class Organization implements Doc {
    constructor(name: string, orgAdminUserId: string);

    _rev: number;
    id: string;
    name: string;
    description: string;
    active: boolean;
    ctime: number;
    orgAdminUserIds: string[];
    logoUrl: string;
    userIds: string[];
    address: Address;
    hardwareIds: string[];
    missionIds: string[];
    billingInfo: BillingInfo;
    externalId: string;
    _orgAdmins: User[];

    getId(): string;
    setId(id: string);
    isActive(): boolean;
    removeUser(userId: string);
    addUser(userId: string);
    addHardware(hardwareId: string);
    getTheBestOrgAdminId(): string;
    addUserOrgAdmin(userId: string);
    addOrgAdmin(userId: string);
    removeOrgAdmin(userId: string);
    containsUser(userId: string): boolean;
    containsHardware(hwId: string): boolean;
    removeHardware(hardwareId: string);
    listHardwareIds(): string[];
    getStatus(): string;
    hasOnlyOneAdmin(): boolean;
}
export enum Privacy { PUBLIC, PRIVATE, UNLISTED }
export class Stream implements Doc {
    _rev: number;
    userId: string;
    filename: string;
    startAddress: string;
    startLocation: StreamLocation;
    onCdn: boolean;
    onYoutube: string;
    mtime: number;
    ctime: number;
    views: number;
    startTimeMsec: number;
    title: string;
    locationHidden: boolean;
    tags2: Tag[];
    avgSpeed: number;
    maxSpeed: number;
    maxAlt: number;
    width: number;
    height: number;
    hardwareId: string;
    liveCodecs: string;
    m3u8: string;
    mpd: string;
    webm: string;
    mp4: string;
    thumb: string;
    md: string;
    nearby: string;
    city: string;

    getId(): string;
    isLive(): boolean;
    setId(id: string);
    isActive(): boolean;
    isClosed(): boolean;
    sid(): StreamId;
    isScheduled(): boolean;
    getStatus(): LiveStatus;
    static createStream(sid: StreamId, privacy: Privacy): Stream;
    isUploading(): boolean;
    isRecorded(): boolean;
    getPrivacy(): Privacy;
    setPrivacy(privacy: Privacy);
    safeStreamId(): string;
    setStatus(status: LiveStatus);
    setClosed(closed: boolean);
    getMp4(): string;
    getThumb(): string;
    getM3u8(): string;
}
export class StreamId  {
    constructor(userId: string, streamId: string);

    userId: string;
    streamId: string;

    equals(obj: Object): boolean;
    toString(): string;
    hashCode(): number;
    static sid(userId: string, streamId: string): StreamId;
}
export class StreamLocation  {
    static sortByTime: (h1: StreamLocation, h2: StreamLocation) => number;
    altitude: number;
    course: number;
    horizontalAccuracy: number;
    latitude: number;
    longitude: number;
    speed: number;
    timestamp: string;
    verticalAccuracy: number;
    playerTime: number;
    streamId: string;
    static accurateLocations: (arg: StreamLocation) => boolean;

    hashCode(): number;
    static speedLocation(timestamp: string, speed: number): StreamLocation;
    static latLng(timestamp: string, latitude: number, longitude: number): StreamLocation;
    getSpeed(): number;
    lalo(): string;
    getTimestamp(): string;
    getTime(): number;
}
export class StreamPermissions  {

    static canUpdateStreamById(sid: StreamId, user: User): boolean;
    static canGetStreamById(sid: StreamId, user: User): boolean;
    static canGetStream(stream: Stream, user: User): boolean;
}
export class StreamResponse  {
    streamId: string;
    user: UserResponse;
    tags: Tag[];
    durationMsec: number;
    embedUrl: string;
    landUrl: string;
    likes: LikeResponse;
    comments: CommentResponse;
    hostUrl: string;
    flash: string;
    _rev: number;
    userId: string;
    filename: string;
    startAddress: string;
    startLocation: StreamLocation;
    onCdn: boolean;
    onYoutube: string;
    mtime: number;
    ctime: number;
    views: number;
    startTimeMsec: number;
    title: string;
    locationHidden: boolean;
    tags2: Tag[];
    avgSpeed: number;
    maxSpeed: number;
    maxAlt: number;
    width: number;
    height: number;
    hardwareId: string;
    liveCodecs: string;
    m3u8: string;
    mpd: string;
    webm: string;
    mp4: string;
    thumb: string;
    md: string;
    nearby: string;
    city: string;

    getHostUrl(): string;
    getFlash(): string;
    userpic(): string;
    username(): string;
    isoDate(): string;
    getId(): string;
    isLive(): boolean;
    setId(id: string);
    isActive(): boolean;
    isClosed(): boolean;
    sid(): StreamId;
    isScheduled(): boolean;
    getStatus(): LiveStatus;
    static createStream(sid: StreamId, privacy: Privacy): Stream;
    isUploading(): boolean;
    isRecorded(): boolean;
    getPrivacy(): Privacy;
    setPrivacy(privacy: Privacy);
    safeStreamId(): string;
    setStatus(status: LiveStatus);
    setClosed(closed: boolean);
    getMp4(): string;
    getThumb(): string;
    getM3u8(): string;
}
export class TSFile  {
    filename: string;
    filesize: number;
    ctime: number;
    startTime: number;
    videoDuration: number;
    timescale: number;
    mseq: number;

    equals(obj: Object): boolean;
    hashCode(): number;
    getVideoDuration(): number;
    getVideoDurationMsec(): number;
    getVideoDurationSec(): number;
    getMseq(): number;
    getFilename(): string;
    getFilesize(): number;
    getStartTimeMsec(): number;
    getStartTime(): number;
    getCtime(): number;
    getTimescale(): number;
    setVideoDuration(videoDuration: number);
}
export class Tag  {
    constructor(id: string, name: string);

    id: string;
    name: string;

    toString(): string;
}
export class TimeInterval  {
    constructor(startTime: Date, endTime: Date);

    start: Date;
    end: Date;

    contains(d: Date): boolean;
    overlaps(that: TimeInterval): boolean;
}
export class TwilioToken  {
    identity: string;
    token: string;

}
export class User implements Doc {
    constructor(id: string, name: string, userpic: string, created: number, social: LoginType, email: string);

    _rev: number;
    id: string;
    name: string;
    lastname: string;
    email: string;
    password: string;
    session: AccessToken;
    emailVerified: boolean;
    resetPasswordToken: string;
    tokenExpireTime: number;
    licenseAgreementAccepted: boolean;
    profiles: {[id: string]: UserProfile};

    toString(): string;
    getName(): string;
    getId(): string;
    getType(): LoginType;
    setId(id: string);
    created(): number;
    isUserActiveInAnyOrg(): boolean;
    isUserActiveInOrg(orgId: string): boolean;
    getProfile(orgId: string): UserProfile;
    hasCommonOrg(checkUser: User): boolean;
    getFullName(): string;
    getAvatarUrl(): string;
    getStatusString(orgId: string): string;
    getEmail(): string;
    setAccessToken(token: AccessToken);
    passwordMatches(password: string): boolean;
    setPassword(password: string);
    setAvatarUrl(url: string);
    isExternal(orgId: string): boolean;
    isOrgAdminInAnyOrg(): boolean;
    getFirstOrgId(): string;
    setRole(orgId: string, role: UserRole);
    createProfile(orgId: string);
    setSuperAdmin(sudo: boolean);
    addProfile(orgId: string, userProfile: UserProfile);
    setUserActive(orgId: string, active: boolean);
    getOrgDepartment(orgId: string): string;
    setOrgDepartment(orgId: string, department: string);
    getOrgTitle(orgId: string): string;
    setOrgTitle(orgId: string, title: string);
    getOrgPhone(orgId: string): string;
    setOrgPhone(orgId: string, phone: string);
    getOrgNotes(orgId: string): string;
    setOrgNotes(orgId: string, notes: string);
    belongsToOrg(orgId: string): boolean;
    isOrgAdmin(orgId: string): boolean;
    getRole(orgId: string): UserRole;
    isSuperAdmin(): boolean;
}
export class UserActivityResponse  {
    thumb: string;
    hashTags: string[];

}
export class UserProfile  {
    department: string;
    title: string;
    phone: string;
    notes: string;
    role: UserRole;
    active: boolean;

}
export class UserResponse  {
    constructor(id: string, name: string, userpic: string, type: LoginType, intoURL: string, homeTown: string, activities: UserActivityResponse[]);

    id: string;
    name: string;
    userpic: string;
    type: LoginType;
    intoURL: string;
    introUrl: string;
    homeTown: string;
    activites: UserActivityResponse[];

}
export enum UserRole { USER, ORG_ADMIN, SUPER_ADMIN, EXTERNAL }
export class CalendarApi  {

    get(id: string): Observable<Calendar>;
    remove(id: string): Observable<Calendar>;
    create(item: Calendar): Observable<Calendar>;
    list(orgId: string): Observable<Calendar[]>;
    getAndUpdate(id: string, transformer: (arg: Calendar) => void): Observable<Calendar>;
    updates(): Observable<Calendar>;
}
export class HWStatusApi  {

    remove(id: string): Observable<HWStatus>;
    get(id: string): Observable<HWStatus>;
    create(item: HWStatus): Observable<HWStatus>;
    list(orgId: string): Observable<HWStatus[]>;
    getAndUpdate(id: string, transformer: (arg: HWStatus) => void): Observable<HWStatus>;
    updates(): Observable<HWStatus>;
}
export class HardwareApi  {

    findByPort(port: number): Observable<Hardware>;
    releaseHardwares(removedHws: string[], mId: string): Observable<string>;
    reassignHardware(orgId: string, hwId: string): Observable<string>;
    logList(hwId: string): Observable<HWLogEntry[]>;
    remove(id: string): Observable<Hardware>;
    get(id: string): Observable<Hardware>;
    create(item: Hardware): Observable<Hardware>;
    list(orgId: string): Observable<Hardware[]>;
    getAndUpdate(id: string, transformer: (arg: Hardware) => void): Observable<Hardware>;
    updates(): Observable<Hardware>;
}
export class JSApiClient  {
    streams: StreamApi;
    missions: MissionApi;
    users: UserApi;
    orgs: OrgApi;
    calendars: CalendarApi;
    hw: HardwareApi;
    hwStatus: HWStatusApi;
    overlays: OverlayApi;

    liveErrors(): Observable<Error>;
    createOrgFull(org: Organization, admin: User, userProfile: UserProfile): Observable<Organization>;
    resetPassword(loginData: LoginRequest): Observable<User>;
    logout(): Observable<string>;
    static mapHardwareWithCalendar(be: JSApiClient, hardware: Hardware): Observable<Hardware>;
    static createApiClient(serverUrl: string): JSApiClient;
    login(loginData: LoginRequest): Observable<User>;
}
export class MissionApi  {

    getShareToken(missionId: string): Observable<string>;
    shareMission(mission: Mission, toEmail: string): Observable<boolean>;
    unshareMission(mission: Mission, toEmail: string): Observable<boolean>;
    splitStreamsOnMissionEnd(mid: string): Observable<string>;
    remove(id: string): Observable<Mission>;
    get(id: string): Observable<Mission>;
    create(item: Mission): Observable<Mission>;
    list(orgId: string): Observable<Mission[]>;
    getAndUpdate(id: string, transformer: (arg: Mission) => void): Observable<Mission>;
    updates(): Observable<Mission>;
}
export class OrgApi  {

    remove(id: string): Observable<Organization>;
    get(id: string): Observable<Organization>;
    create(item: Organization): Observable<Organization>;
    list(orgId: string): Observable<Organization[]>;
    getAndUpdate(id: string, transformer: (arg: Organization) => void): Observable<Organization>;
    updates(): Observable<Organization>;
}
export class OverlayApi  {

    getOverlay(id: string): Observable<string>;
    createOverlay(orgId: string, url: string): Observable<string>;
    deleteOverlay(id: string): Observable<string>;
}
export class StreamApi  {

    list(userId: string): Observable<StreamResponse[]>;
    locationUpdates(sid: string): Observable<StreamLocation>;
    liveMessages(sid: string): Observable<LiveMessage>;
    updateTitle(sid: string, newTitle: string): Observable<StreamResponse>;
    remove(id: string): Observable<StreamResponse>;
    get(id: string): Observable<StreamResponse>;
    create(item: StreamResponse): Observable<StreamResponse>;
    getAndUpdate(id: string, transformer: (arg: StreamResponse) => void): Observable<StreamResponse>;
    updates(): Observable<StreamResponse>;
}
export class UserApi  {

    allUsersUpdates(orgId: string): Observable<User>;
    sendCancelNotification(user: User, missionId: string): Observable<User>;
    joinByMissionToken(user: User, token: string): Observable<User>;
    getUserByEmail(email: string): Observable<User>;
    isUserExists(email: string): Observable<boolean>;
    isTempUser(email: string): Observable<boolean>;
    createOrUpdate(user: User): Observable<User>;
    forceUpdate(user: User): Observable<User>;
    inviteToMission(user: User, missionId: string): Observable<User>;
    remove(id: string): Observable<User>;
    get(id: string): Observable<User>;
    create(item: User): Observable<User>;
    list(orgId: string): Observable<User[]>;
    getAndUpdate(id: string, transformer: (arg: User) => void): Observable<User>;
    updates(): Observable<User>;
}
