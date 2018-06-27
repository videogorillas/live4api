XMLHttpRequest = require('w3c-xmlhttprequest').XMLHttpRequest;
let Rx = require("rx");
let live4api = require("live4api");

let JSApiClient = live4api.JSApiClient;
let LoginRequest = live4api.LoginRequest;
let LiveStatus = live4api.LiveStatus;
let Observable = Rx.Observable;

let api = JSApiClient.createApiClient("https://qa.live4.io");
let loginOk = api.login(new LoginRequest("anna.pogribnyak@gmail.com", "pewpew")).shareReplay(1);

let allStreams = loginOk.concatMap(user => api.streams.list(user.id).concatMap(a => Observable.from(a)).filter(s => s != null));

let latestStream = allStreams.take(1);

let locations = latestStream.concatMap(s => api.streams.locations(s.sid()));

locations.subscribe(x => console.log(x));

//get mission36
let mission36 = loginOk.concatMap(user => api.missions.get("mission36"));

//get location of first stream in mission36
let locations2 = mission36.concatMap(m => api.streams.locations(m.streamIds[0]));

//print
locations2.subscribe(x => console.log(x));




