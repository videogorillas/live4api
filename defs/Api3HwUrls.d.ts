export default class Api3HwUrls {
    static API_3_HW: string;
    static FIND_BY_PORT: string;
    static RELEASE: string;
    static OBJECT: string;
    static LIST: string;

    static createUrl (): string;

    static getUrl (id: string): string ;

    static listUrl (orgId: string): string ;

    static releaseUrl (id: string): string ;

    static findByPortUrl (port: number): string ;
}