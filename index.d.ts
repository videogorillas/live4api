import {Observable} from 'rx';
export class AccessToken  {
    constructor(arg0: string, arg1: string, arg2: number);

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
    setId(arg0: string);
    isActive(): boolean;
    isBusyAt(arg0: TimeInterval): boolean;
}
export class CameraFile  {
    constructor(arg0: string, arg1: string);

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
    setBody(arg0: string);
}
export class CommentResponse  {
    constructor(arg0: Comment[]);

    total_count: number;
    comments: Comment[];

}
export class DataSegment  {
    constructor(arg0: number, arg1: StreamLocation);

    playerTime: number;
    location: StreamLocation;
    nearBy: string;
    isEmpty: boolean;
    widthScaled: number;
    leftScaled: number;
    tsfile: TSFile;
    descr: string;

    toString(): string;
    scale(arg0: number);
    setWidth(arg0: number);
    setLeft(arg0: number);
    getTime(): number;
}
export class Dimension  {
    constructor(arg0: number, arg1: number);

    width: number;
    height: number;

}
export interface Doc  {

    getId(): string;
    setId(arg0: string);
    isActive(): boolean;
}
export class EndOfStream  {
    files: CameraFile[];
    static ENDOFSTREAM_JS: string;
    static ENDOFSTREAM_JS_GZ: string;

}
export class Hardware implements Doc {
    constructor(arg0: string, arg1: string);

    static TYPE_MC_BOX: string;
    static TYPE_DRONE: string;
    static TYPE_ANDROID: string;
    static TYPE_IOS: string;
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
    static isValidPortNumber(arg0: number): boolean;
    isMCBox(): boolean;
    isDrone(): boolean;
    static MCBox(arg0: string): Hardware;
    static drone(arg0: string): Hardware;
    static android(arg0: string): Hardware;
    static ios(arg0: string): Hardware;
    isAvailable(): boolean;
    isScheduled(): boolean;
    belongsToOrg(arg0: string): boolean;
    isAssigned(): boolean;
    static statusLabel(arg0: HwAvailability): string;
    getAvailabilityFor(arg0: TimeInterval): HwAvailability;
    setId(arg0: string);
    isActive(): boolean;
    setPort(arg0: number): Hardware;
}
export enum HwAvailability { AVAILABLE, SCHEDULED, INUSE }
export class HWLogEntry  {
    constructor(arg0: Hardware, arg1: Mission, arg2: string, arg3: string);

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
export enum HwState { CLOSED, OPEN, DATA_RECEIVED, DATA_PARSED, BAD_DATA }
export class HWStatus implements Doc {
    id: string;
    static API_HWSTATUS: string;
    static OBJECT: string;
    static LIST: string;
    hwId: string;
    status: HwState;
    mtime: number;

    getId(): string;
    setId(arg0: string);
    isActive(): boolean;
    static newStatus(arg0: string, arg1: HwState): HWStatus;
}
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
    constructor(arg0: Like[], arg1: boolean);

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
    static subscribeStream(arg0: string): LiveMessage;
    static unsubscribeStream(arg0: string): LiveMessage;
    static subscribe(arg0: string): LiveMessage;
}
export enum LiveStatus { SCHEDULED, STANDBY, LIVE, UPLOADING, UPLOADING_METADATA, RECORDED }
export class LoginRequest  {
    constructor(arg0: string, arg1: string);

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
    addStream(arg0: string);
    hasStreamId(arg0: string): boolean;
    hasOwnerPermissions(arg0: User): boolean;
    static isOrgAdmin(arg0: User, arg1: Mission): boolean;
    static isOwner(arg0: User, arg1: Mission): boolean;
    hasPilotPermissions(arg0: User): boolean;
    hasParticipantPermisisons(arg0: User): boolean;
    removeUser(arg0: string);
    addUser(arg0: User, arg1: MissionRole);
    addPilot(arg0: string, arg1: User);
    removePilot(arg0: string);
    getPilotId(arg0: string): string;
    changeMissionName(arg0: string);
    hasUser(arg0: string): boolean;
    getOwnerId(): string;
    hasOwnerRole(): boolean;
    countOwners(): number;
    isCompleted(): boolean;
    addHardware(arg0: Hardware);
    getTimeInterval(): TimeInterval;
    isRunningNow(): boolean;
    static isScheduler(arg0: User, arg1: Mission): boolean;
    isScheduled(): boolean;
    setId(arg0: string);
    isActive(): boolean;
}
export class MissionPermissions  {

    static canEditMisson(arg0: User, arg1: Mission): boolean;
    static canEditStream(arg0: User, arg1: Mission): boolean;
    static canAddUser(arg0: User, arg1: Mission): boolean;
    static canEndMission(arg0: User, arg1: Mission): boolean;
    static canEditLocations(arg0: User, arg1: Mission): boolean;
    static canAssignPilot(arg0: User, arg1: Mission): boolean;
    static canUseChat(arg0: User, arg1: Mission): boolean;
    static canAddSources(arg0: User, arg1: Mission): boolean;
    static hasOneOwner(arg0: Mission): boolean;
    static canViewCompletedMission(arg0: User, arg1: Mission): boolean;
    static canShareMission(arg0: User, arg1: Mission): boolean;
    static canRemoveUser(arg0: Mission, arg1: User, arg2: User): boolean;
    static canJoinMission(arg0: Mission, arg1: string): boolean;
    static canPreviewMission(arg0: Mission): boolean;
    static canViewMission(arg0: Mission, arg1: User): boolean;
    static canStartMission(arg0: User, arg1: Mission): boolean;
    static userRemoved(arg0: Mission, arg1: Mission, arg2: User): boolean;
}
export enum MissionRole { UNKNOWN, PILOT, PARTICIPANT, OBSERVER, OWNER }
export class MissionShareToken  {
    token: string;
    missionId: string;
    userId: string;
    invitedId: string;

}
export enum MissionState { PENDING, STARTED, CANCELLED, ENDED, DELETED }
export class NameEmail  {
    constructor(arg0: string, arg1: string);

    email: string;
    name: string;

}
export class NewOrgAdminProfile  {
    constructor(arg0: Organization, arg1: User, arg2: UserProfile);

    org: Organization;
    admin: User;
    profile: UserProfile;

}
export class Organization implements Doc {
    constructor(arg0: string, arg1: string);

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
    removeUser(arg0: string);
    addUser(arg0: string);
    addHardware(arg0: string);
    getStatus(): string;
    setId(arg0: string);
    isActive(): boolean;
    getTheBestOrgAdminId(): string;
    addUserOrgAdmin(arg0: string);
    addOrgAdmin(arg0: string);
    removeOrgAdmin(arg0: string);
    containsUser(arg0: string): boolean;
    containsHardware(arg0: string): boolean;
    removeHardware(arg0: string);
    listHardwareIds(): string[];
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
    sid(): StreamId;
    isScheduled(): boolean;
    getStatus(): LiveStatus;
    setId(arg0: string);
    isActive(): boolean;
    static createStream(arg0: StreamId, arg1: Privacy): Stream;
    isUploading(): boolean;
    isRecorded(): boolean;
    getPrivacy(): Privacy;
    setPrivacy(arg0: Privacy);
    safeStreamId(): string;
    setStatus(arg0: LiveStatus);
    setClosed(arg0: boolean);
    getMp4(): string;
    getThumb(): string;
    getM3u8(): string;
    isClosed(): boolean;
}
export class StreamId  {
    constructor(arg0: string, arg1: string);

    userId: string;
    streamId: string;

    equals(arg0: Object): boolean;
    toString(): string;
    hashCode(): number;
    static sid(arg0: string, arg1: string): StreamId;
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
    static speedLocation(arg0: string, arg1: number): StreamLocation;
    static latLng(arg0: string, arg1: number, arg2: number): StreamLocation;
    getSpeed(): number;
    lalo(): string;
    getTime(): number;
    getTimestamp(): string;
}
export class StreamPermissions  {

    static canUpdateStreamById(arg0: StreamId, arg1: User): boolean;
    static canGetStreamById(arg0: StreamId, arg1: User): boolean;
    static canGetStream(arg0: Stream, arg1: User): boolean;
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
    sid(): StreamId;
    isScheduled(): boolean;
    getStatus(): LiveStatus;
    setId(arg0: string);
    isActive(): boolean;
    static createStream(arg0: StreamId, arg1: Privacy): Stream;
    isUploading(): boolean;
    isRecorded(): boolean;
    getPrivacy(): Privacy;
    setPrivacy(arg0: Privacy);
    safeStreamId(): string;
    setStatus(arg0: LiveStatus);
    setClosed(arg0: boolean);
    getMp4(): string;
    getThumb(): string;
    getM3u8(): string;
    isClosed(): boolean;
}
export class Tag  {
    constructor(arg0: string, arg1: string);

    id: string;
    name: string;

    toString(): string;
}
export class TimeInterval  {
    constructor(arg0: Date, arg1: Date);

    start: Date;
    end: Date;

    contains(arg0: Date): boolean;
    overlaps(arg0: TimeInterval): boolean;
}
export class TSFile  {
    filename: string;
    filesize: number;
    ctime: number;
    startTime: number;
    videoDuration: number;
    timescale: number;
    mseq: number;

    equals(arg0: Object): boolean;
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
    setVideoDuration(arg0: number);
}
export class TwilioToken  {
    identity: string;
    token: string;

}
export class User implements Doc {
    constructor(arg0: string, arg1: string, arg2: string, arg3: number, arg4: LoginType, arg5: string);

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
    isOrgAdmin(arg0: string): boolean;
    getRole(arg0: string): UserRole;
    isSuperAdmin(): boolean;
    isUserActiveInAnyOrg(): boolean;
    isUserActiveInOrg(arg0: string): boolean;
    getProfile(arg0: string): UserProfile;
    hasCommonOrg(arg0: User): boolean;
    getFullName(): string;
    getAvatarUrl(): string;
    getStatusString(arg0: string): string;
    getEmail(): string;
    setAccessToken(arg0: AccessToken);
    passwordMatches(arg0: string): boolean;
    setPassword(arg0: string);
    setAvatarUrl(arg0: string);
    isExternal(arg0: string): boolean;
    isOrgAdminInAnyOrg(): boolean;
    getFirstOrgId(): string;
    setRole(arg0: string, arg1: UserRole);
    createProfile(arg0: string);
    setSuperAdmin(arg0: boolean);
    addProfile(arg0: string, arg1: UserProfile);
    setUserActive(arg0: string, arg1: boolean);
    getOrgDepartment(arg0: string): string;
    setOrgDepartment(arg0: string, arg1: string);
    getOrgTitle(arg0: string): string;
    setOrgTitle(arg0: string, arg1: string);
    getOrgPhone(arg0: string): string;
    setOrgPhone(arg0: string, arg1: string);
    getOrgNotes(arg0: string): string;
    setOrgNotes(arg0: string, arg1: string);
    belongsToOrg(arg0: string): boolean;
    setId(arg0: string);
    created(): number;
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
    constructor(arg0: string, arg1: string, arg2: string, arg3: LoginType, arg4: string, arg5: string, arg6: UserActivityResponse[]);

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

    get(arg0: string): Observable<Calendar>;
    remove(arg0: string): Observable<Calendar>;
    create(arg0: Calendar): Observable<Calendar>;
    list(arg0: string): Observable<Calendar[]>;
    getAndUpdate(arg0: string, arg1: (arg: Calendar) => void): Observable<Calendar>;
    updates(): Observable<Calendar>;
}
export class HardwareApi  {

    findByPort(arg0: number): Observable<Hardware>;
    releaseHardwares(arg0: string[], arg1: string): Observable<string>;
    reassignHardware(arg0: string, arg1: string): Observable<string>;
    logList(arg0: string): Observable<HWLogEntry[]>;
    remove(arg0: string): Observable<Hardware>;
    get(arg0: string): Observable<Hardware>;
    create(arg0: Hardware): Observable<Hardware>;
    list(arg0: string): Observable<Hardware[]>;
    getAndUpdate(arg0: string, arg1: (arg: Hardware) => void): Observable<Hardware>;
    updates(): Observable<Hardware>;
}
export class HWStatusApi  {

    remove(arg0: string): Observable<HWStatus>;
    get(arg0: string): Observable<HWStatus>;
    create(arg0: HWStatus): Observable<HWStatus>;
    list(arg0: string): Observable<HWStatus[]>;
    getAndUpdate(arg0: string, arg1: (arg: HWStatus) => void): Observable<HWStatus>;
    updates(): Observable<HWStatus>;
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

    login(arg0: LoginRequest): Observable<User>;
    static createApiClient(arg0: string): JSApiClient;
    liveErrors(): Observable<Error>;
    logout(): Observable<string>;
    createOrgFull(arg0: Organization, arg1: User, arg2: UserProfile): Observable<Organization>;
    resetPassword(arg0: LoginRequest): Observable<User>;
    static mapHardwareWithCalendar(arg0: JSApiClient, arg1: Hardware): Observable<Hardware>;
}
export class MissionApi  {

    getShareToken(arg0: string): Observable<string>;
    shareMission(arg0: Mission, arg1: string): Observable<boolean>;
    unshareMission(arg0: Mission, arg1: string): Observable<boolean>;
    splitStreamsOnMissionEnd(arg0: string): Observable<string>;
    remove(arg0: string): Observable<Mission>;
    get(arg0: string): Observable<Mission>;
    create(arg0: Mission): Observable<Mission>;
    list(arg0: string): Observable<Mission[]>;
    getAndUpdate(arg0: string, arg1: (arg: Mission) => void): Observable<Mission>;
    updates(): Observable<Mission>;
}
export class OrgApi  {

    remove(arg0: string): Observable<Organization>;
    get(arg0: string): Observable<Organization>;
    create(arg0: Organization): Observable<Organization>;
    list(arg0: string): Observable<Organization[]>;
    getAndUpdate(arg0: string, arg1: (arg: Organization) => void): Observable<Organization>;
    updates(): Observable<Organization>;
}
export class OverlayApi  {

    getOverlay(arg0: string): Observable<string>;
    createOverlay(arg0: string, arg1: string): Observable<string>;
    deleteOverlay(arg0: string): Observable<string>;
}
export class StreamApi  {

    list(arg0: string): Observable<StreamResponse[]>;
    locationUpdates(arg0: string): Observable<StreamLocation>;
    locations(arg0: string): Observable<StreamLocation[]>;
    liveMessages(arg0: string): Observable<LiveMessage>;
    updateTitle(arg0: string, arg1: string): Observable<StreamResponse>;
    remove(arg0: string): Observable<StreamResponse>;
    get(arg0: string): Observable<StreamResponse>;
    create(arg0: StreamResponse): Observable<StreamResponse>;
    getAndUpdate(arg0: string, arg1: (arg: StreamResponse) => void): Observable<StreamResponse>;
    updates(): Observable<StreamResponse>;
}
export class UserApi  {

    createOrUpdate(arg0: User): Observable<User>;
    forceUpdate(arg0: User): Observable<User>;
    inviteToMission(arg0: User, arg1: string): Observable<User>;
    allUsersUpdates(arg0: string): Observable<User>;
    sendCancelNotification(arg0: User, arg1: string): Observable<User>;
    joinByMissionToken(arg0: User, arg1: string): Observable<User>;
    getUserByEmail(arg0: string): Observable<User>;
    isUserExists(arg0: string): Observable<boolean>;
    isTempUser(arg0: string): Observable<boolean>;
    remove(arg0: string): Observable<User>;
    get(arg0: string): Observable<User>;
    create(arg0: User): Observable<User>;
    list(arg0: string): Observable<User[]>;
    getAndUpdate(arg0: string, arg1: (arg: User) => void): Observable<User>;
    updates(): Observable<User>;
}
