// Type definitions for live4api
// Project: live4api
// Definitions by: videogorillas <[~A URL FOR YOU~]>

import User from "./defs/User";
import {Mission} from "./defs/Mission";
import Hardware from "./defs/Hardware";
import Api3MissionUrls from "./defs/Api3MissionUrls";
import {Calendar} from "./defs/Calendar";
import {MissionRole} from "./defs/MissionRole";
import {UserRole} from "./defs/UserRole";
import TimeInterval from "./defs/TimeInterval";
import {Api3CalendarUrls} from "./defs/Api3CalendarUrls";
import Api3HwUrls from "./defs/Api3HwUrls";
import Api3OrgUrls from "./defs/Api3OrgUrls";
import {Api3Urls} from "./defs/Api3Urls";
import Api3UserUrls from "./defs/Api3UserUrls";
declare module "live4api" {

    /*~ You can also add new properties to existing interfaces from
     *~ the original module by writing interface augmentations */

    export interface live4api {
        Api3MissionUrls: Api3MissionUrls;
        Api3CalendarUrls: Api3CalendarUrls;
        Api3HwUrls: Api3HwUrls;
        Api3OrgUrls: Api3OrgUrls;
        Api3Urls: Api3Urls;
        Api3UserUrls: Api3UserUrls;
        Calendar: Calendar;
        Hardware: Hardware;
        Mission: Mission;
        MissionRole: MissionRole;
        TimeInterval: TimeInterval;
        User: User;
        UserRole: UserRole;
    }
}
