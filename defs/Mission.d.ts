import User from "./User";
import {MissionRole} from "./MissionRole";
import Hardware from "./Hardware";
import TimeInterval from "./TimeInterval";
enum State {
    PENDING, STARTED, CANCELLED, ENDED
}
class ShareToken {
    token: string;
    missionId: string;
    userId: string;
    invitedId: string;
}
export default class Mission implements Doc {
    getId (): string;

    setId (id: string): void;

    isActive (): boolean;

    static State: State;
    static ShareToken: ShareToken;
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

    //user id -> MissionRole
    roles: { [id: string]: MissionRole};

    pilots: { [id: string]: string };

    //user id -> join time
    joined: { [userId: string]: Date };

    // used in UI only, should not be serialized to db, use ids to serialize
    // hardware: Hardware[];

    state: State;
    static UNASSIGNED: string;

    addStream (streamId: string);

    hasStreamId (streamId: string): boolean;

    hasOwnerPermissions (u: User): boolean;

    hasPilotPermissions (u: User): boolean;

    hasParticipantPermisisons (u: User): boolean;

    removeUser (userId: string);

    addUser (user: User, role: MissionRole);

    addPilot (streamId: string, pilot: User): void;

    removePilot (streamId: string): void;

    getPilotId (streamId: string): string;

    changeMissionName (newMissionName: string): void;

    hasUser (id: string): boolean;

    getOwnerId (): string;

    hasOwnerRole (): boolean;

    countOwners (): number;

    isLive (): boolean;

    isScheduled (): boolean;

    isCompleted (): boolean;

    addHardware (h: Hardware): void;

    getTimeInterval (): TimeInterval;

    isRunningNow (): boolean;

    static isOrgAdmin (u: User, m: Mission): boolean;

    static isOwner (u: User, m: Mission): boolean;
}
