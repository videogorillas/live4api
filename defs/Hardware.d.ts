import TimeInterval from "./TimeInterval";
import {Calendar} from "./Calendar";
enum Availability {
    AVAILABLE, SCHEDULED, INUSE
}
export default class Hardware implements Doc {
    constructor (name: string, type: string);

    getId (): string;

    setId (id: string): void;

    isActive (): boolean;

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
    endponumber: string;

    static sortByNameAvailableFirst: (h1: Hardware, h2: Hardware) => number;

    static  isValidPortNumber (port: number): boolean;

    static MCBox (name: string): Hardware;

    static drone (name: string): Hardware;

    static android (name: string): Hardware;

    static ios (name: string): Hardware;

    static  statusLabel (s: Availability): string;

    //UI only: meaningless for daos
    //marked as transient to disable GSON serialization
    _availability: Availability;
    _calendar: Calendar;
    _orgName: string;

    isMCBox (): boolean;

    isDrone (): boolean;

    isAvailable (): boolean;

    isScheduled (): boolean;

    setPort (port: number): Hardware;

    belongsToOrg (orgId: string): boolean;

    isAssigned (): boolean;

    getAvailabilityFor (ti: TimeInterval): Availability;
}