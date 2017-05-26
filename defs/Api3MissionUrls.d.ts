export default class Api3MissionUrls {
    static API_3_MISSION: string;
    static TOKEN: string;
    static SHARE: string;
    static JOIN: string;
    static UNSHARE: string;
    static CANCEL_NOTIFICATION: string;
    static INVITE: string;
    static OBJECT: string;
    static LIST: string;
    static _MISSIONS: string;
    static MISSION_SHARE_PARAM: string;
    static CHAT_TOKEN: string;
    static AUDIO_CHAT_TOKEN: string;


    static baseUrl (): string ;

    static createUrl (): string ;

    static updateUrl (): string ;

    static getUrl (id: string): string ;

    static listUrl (orgId: string): string ;

    static tokenUrl (missionId: string): string ;

    static shareUrl (missionId: string): string ;

    static unshareUrl (missionId: string): string ;

    static joinUrl (missionId: string): string ;

    static baseJoinUrl (): string ;

    static cancelNotificationUrl (missionId: string): string ;

    static inviteUrl (missionId: string): string ;

    static shareMissionUrl (missionId: string, shareToken: string): string ;
}