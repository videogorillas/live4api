export default class Api3OrgUrls {
    static API_3_ORG: string;
    static CREATEWITHADMIN: string;
    static OBJECT: string;
    static LIST: string;


    static baseUrl (): string;

    static createUrl (): string;

    static getUrl (orgId: string): string;

    static listUrl (orgId: string): string;

    static createWithAdminUrl (): string;
}