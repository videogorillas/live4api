import {UserRole} from "./UserRole";
export interface UserProfile {
    department: string;
    title: string;
    phone: string;
    notes: string;
    role: UserRole;
    active: boolean;
}
interface ProfileMap {
    orgId: string;
    userProfile: UserProfile;
}
interface LoginType {

}
export default class User {
    _rev: number;
    id: string;
    name: string;
    lastname: string;
    _created: number;
    userpic: string;
    email: string;
    // type:LoginType;
    password: string;
    // session:AccessToken ;
    emailVerified: boolean;
    resetPasswordToken: string;
    tokenExpireTime: number;
    licenseAgreementAccepted: boolean;
    sudo: boolean;
    profiles: ProfileMap;

    constructor (id: string, name: string, userpic: string, created: number, social: LoginType, email: string)
}