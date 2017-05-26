export default class Api3UserUrls {
    static API_3_USER: string;
    static BYEMAIL: string;
    static OBJECT: string;
    static LIST: string;
    static CHECK: string;


    static createUrl (): string ;

    static updateUrl (): string ;

    static getUrl (id: string): string ;

    static listUrl (orgId: string): string ;

    static byEmailUrl (email: string): string ;

    static checkUserByEmail (email: string): string ;
}