XMLHttpRequest = require('w3c-xmlhttprequest').XMLHttpRequest;
let Rx = require("rx");
let live4api = require("live4api");

let JSApiClient = live4api.JSApiClient;
let LoginRequest = live4api.LoginRequest;
let LiveStatus = live4api.LiveStatus;
let Observable = Rx.Observable;

let api = JSApiClient.createApiClient("https://qa.live4.io");
let loginOk = api.login(new LoginRequest("anna.pogribnyak@gmail.com", "pewpew"));

let liveStreamsWithHw = loginOk.concatMap(user => {
    let streams = api.streams.list(user.id).concatMap(a => Observable.from(a)).filter(s => s != null);
    let live = streams.filter(s => LiveStatus.LIVE.equals(s.getStatus()));
    let withHardware = live.filter(s => s.hardwareId != null);
    return withHardware;
});

liveStreamsWithHw.concatMap(s => {
    return api.hw.get(s.hardwareId).filter(hw => hw.port > 0).map(hw => ({
        "port": hw.port,
        "mpd": s.mpd
    }));
}).subscribe(x => {
    console.log(x);
});
