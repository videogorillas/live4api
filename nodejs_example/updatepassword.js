XMLHttpRequest = require('w3c-xmlhttprequest').XMLHttpRequest;
let Rx = require("rx");
let live4api = require("live4api");

let JSApiClient = live4api.JSApiClient;
let LoginRequest = live4api.LoginRequest;
let LiveStatus = live4api.LiveStatus;
let Observable = Rx.Observable;

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
