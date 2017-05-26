# live4api

LIVE4 Public API

With Maven:

```
<dependency>
    <groupId>io.live4</groupId>
    <artifactId>live4api3</artifactId>
    <version>3.1.26</version>
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
    compile 'io.live4:live4api3:3.1.26'
}
```

### Command line tools

Print stream updates 

```
java -cp live4api3-3.1.26-uberjar.jar io.live4.apiclient.StreamUpdatesMain http://$USER:$PASSWORD@beta.missionkeeper.com/
```

### Usage
NOTE: Live4 public API is based on [ReactiveX library](https://github.com/ReactiveX/RxJava). You can find ReactiveX docs [here](https://github.com/ReactiveX/RxJava/wiki).

1.  Create RxApiClient instance: 
    ```
    String url = "http://1.1.1.1:8042";
    RxApiClient rxApiClient = new RxApiClient(url);
    ```
    
2.  Log in:

    ```
    String email = "your@email.com";
    String password = "123";
    Observable<User> userRx = rxApiClient.login(email, password);
    userRx.subscribe(user -> {
        // Do something with your user here
    });
    ```
3. Select organisation:
    ```
    // This will list all user profiles:
    Map<String, UserProfile> profiles = user.profiles;
    for (String orgId : profiles) {
        // Chose the organisation here
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
5. Create a hardware instance:
    ```
    Hardware hw = Hardware.drone("MyDrone");
    ```
    
6. Create a stream:
    ```
    StreamResponse sr = new StreamResponse();
    sr.title = "Stream from " + user.getName();
    sr.hardwareId = hw.getId();
    Observable<StreamResponse> newStreamRx = rxApiClient.createStream(sr).replay().autoConnect();
    ```
    
7. Add stream to mission:
    ```
    mission.addStream(stream.streamId);
    mission.addHardware(hw);
    rxApiClient.updateMission(m).subscribe(m -> {
        // mission updated
    }, e -> {
        e.printStackTrace();
    });
    ```
    
8. Get mission share token:

    ```
    Observable<Mission.ShareToken> shareTokenRx = rxApiClient.getShareToken(mission.getId());
    ```
    
9. Stream uploads:
    
    Stream uploads include uploading initial json metadata, video data, metadata (stream locations), and "end of stream" data.
    
    Json metadata:
    ```
    StreamId sid = stream.sid();
    
    DebugJs noStripAacDebugJs = new DebugJs();
    noStripAacDebugJs.stripaac = false;
        
    Observable<Request> debugjs = Observable.just(rxApiClient.uploadJsonRequest(sid, DebugJs.DEBUG_JS, noStripAacDebugJs));
    ```
        
    Video data:
    ```
    // You should actually set up your video stream here:
    Observable<Request> videoUploadsRx = ...
    ```
    
    Stream locations metadata:
    ```
    // The next code fakes stream locations for demo purposes. In the real life, you would take these locations from drone data, mobile data, etc.:
    StreamLocation loc = StreamLocation.latLng(sdf.format(new Date()), 42, 42);
    loc.altitude = new Double(42);
    loc.course = new Double(42);
    loc.horizontalAccuracy = new Double(42);
    loc.verticalAccuracy = new Double(42);
    loc.speed = new Double(42);
    
    Observable<StreamLocation> locationsRx = Observable.just(loc).repeat();

        Observable<List<StreamLocation>> chunks = locationsRx.buffer(2, SECONDS).filter(list -> !list.isEmpty());

        Observable<Pair<Integer, List<StreamLocation>>> numberedChunks = chunks.zipWith(
                Observable.range(0, MAX_VALUE), (locations, mseq) -> new Pair(mseq, locations));
        numberedChunks = numberedChunks.onBackpressureBuffer();
        
    Observable<Request> locationUploads = numberedChunks.map(p -> {
            int mseq = p.left;
            List<StreamLocation> locations = p.right;
            String filename = locationsFilename(mseq);
            return rxApiClient.uploadJsonRequest(sid, filename, Collections.singletonMap("locations", locations));
        });
    ```
    
    Video data and stream locations metadata is then merged (we don't care about the order here):
    ```
    Observable<Request> uploadRequests = Observable.merge(videoUploadsRx, locationsRx);
    ```
    
    End of stream data:
    ```
    // End of stream data holds information about all uploaded chunks:
    Observable<Request> eosUploadsRx = ...
    ```
    
    Json metadata, video data, locations metadata, and eos streams are concatenated then:
    ```
    Observable<Response> uploads = Observable.concat(debugjs, uploadRequests, eosUploadsRx)
        .onBackpressureBuffer()
        .concatMap(request -> {
        return uploadWithRetry(client, request);
        });
    ```

# Release
1. Make sure that pom.xml & package.json versions are the same;
2. `mvn clean package`
3. `grunt`
4. `npm login`
5. `npm publish`