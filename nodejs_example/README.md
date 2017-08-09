# NodeJS client example

usage:

```
npm install
node index.js
```

get stream mpd urls with hardware port numbers
```javascript
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

```

outputs:

```
{ port: 4242,
  mpd: 'https://qa.live4.io/gopro/anna.pogribnyak@gmail.com/6262babc-de8f-4bc2-972e-a623ec0eeafa/live.mpd' }
{ port: 4242,
  mpd: 'https://qa.live4.io/gopro/anna.pogribnyak@gmail.com/e2e9f0c5-d777-4522-9365-420f963a528e/live.mpd' }

```

update user

```javascript
let api = JSApiClient.createApiClient("http://eha-vg.mx1ops.com:8642");
let loginOk = api.login(new LoginRequest("doom@videogorillas.com", "PASSWORD_HERE"));

loginOk.concatMap(user => {
    let support = api.users.getAndUpdate("support@mx1-360.com", usr => {
        usr.emailVerified = true;
        usr.licenseAgreementAccepted = true;
    });
    return support;
}).subscribe(x => {
    console.log(x);
});

```