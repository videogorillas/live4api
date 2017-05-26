export class Api3CalendarUrls {
    static API_3_CALENDAR: string;
    static OBJECT: string;
    static LIST: string;

    static createUrl (): string;

    static getUrl (id: string): string;

    static listUrl (orgId: string): string;
}