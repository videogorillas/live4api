# live4api

LIVE4 Public API

With Maven:

```
<dependency>
    <groupId>io.live4</groupId>
    <artifactId>live4api3</artifactId>
    <version>3.0.2</version>
</dependency>
```

With Gradle:
```
repositories {
    maven {
        url "http://kote.videogorillas.com/m2repo/"
    }
}

dependencies {
    compile 'io.live4:live4api3:3.0.2'
}
```

Usage

1. Create RxApiClient instance:
```
String url = "http://1.1.1.1:8042";
RxApiClient rxApiClient = new RxApiClient(url);
```

2. Log in:

```
String email = "your@email.com";
String password = "123";
Observable<User> userRx = rxApiClient.login(email, password);
userRx.subscribe(user -> {
    // Do something with your user here
});
```

3. Select organization:
```
// This will list all user profiles:
Map<String, UserProfile> profiles = user.profiles;
for (String orgId : profiles) {
    // Chose the organization here
}
```

```
// Alternatively, you can just select the first organization available for streaming:
String orgId = user.getFirstOrgId();
```

4. Select mission or create a new one:
```
// This will list all available missions in this organization:
Observable<Mission> missionsRx = rxApiClient.listMissions(orgId);
missionsRx.subscribe(mission -> {
    // select mission here
});
```

```
// Alternatively, you can create a new mission:
Mission mission = new Mission();
mission.createdByUserId = user.id;
mission.startTime = new org.stjs.javascript.Date();
long oneDay = 24 * 60 * 60 * 1000;
mission.endTime = new org.stjs.javascript.Date(mission.startTime.getTime() + oneDay);
mission.hardware = $array();
mission.joined = $map();
mission.name = missionName;
mission.orgId = orgId;
mission.state = Mission.State.PENDING;
mission.streamIds = $array();
mission.addUser(user, MissionRole.OWNER);

Observable<Mission> missionRx = rxApiClient.createMission(mission);
missionRx.subscribe(newMission -> {
    // newMission object contains newly created mission
});

```

