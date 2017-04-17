package io.live4.apiclient;

import static io.live4.model.Internal.defaultArray;

import com.squareup.okhttp.HttpUrl;

import io.live4.model.LiveStatus;
import io.live4.model.Mission;
import io.live4.model.StreamId;
import io.live4.model.StreamResponse;
import rx.Observable;

public class StreamUpdatesMain {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println(
                    "usage " + StreamUpdatesMain.class.getName() + " http://user:password@beta.missionkeeper.com");
            System.exit(1);
            return;
        }

        HttpUrl url = HttpUrl.parse(args[0]);
        String email = url.username();
        String password = url.password();

        String serverUrl = url.toString();
        RxApiClient api = new RxApiClient(serverUrl);

        Observable<Mission> missionUpdates = api.login(email, password).doOnNext(u -> {
            System.out.println("logged in as " + u);
        }).concatMap(u -> api.missionUpdates());
        Observable<StreamId> sids = missionUpdates
                .doOnNext(m -> System.out.println("mission updated " + m.id))
                .flatMap(m -> Observable.from(defaultArray(m.streamIds).toList()))
                .map(sid -> parse(sid))
                .distinct();
        Observable<StreamResponse> flatMap = sids.flatMap(sid -> {
            return api.getStream(sid).filter(s -> !LiveStatus.RECORDED.equals(s.getStatus())).flatMap(s -> {
                Observable<StreamResponse> updates = api
                        .streamUpdates(s.sid())
                        .filter(lm -> lm.hasStream())
                        .map(lm -> lm.stream);
                return Observable.just(s).concatWith(updates);
            }).takeUntil(s -> s.getStatus().equals(LiveStatus.RECORDED));
        });
        flatMap.toBlocking().subscribe(x -> {
            System.out.println(x.sid()+" "+x.getStatus()+" "+x.getM3u8());
        }, err -> {
            err.printStackTrace();
        });
        
        System.out.println("DONE");
    }

    private static StreamId parse(String sid) {
        String[] split = sid.split("/", 2);
        return StreamId.sid(split[0], split[1]);
    }
}
