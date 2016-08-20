package com.gable.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.subscriptions.Subscriptions;

/**
 * Created by realize on 28/11/2558.
 */
public class GeneralService {
    private static final Logger logger = LoggerFactory.getLogger(GeneralService.class);

    public Observable<String> getData() {
        return Observable.<String>create(s -> {
            s.add(Subscriptions.create(() -> logger.info("Unsubscribe")));
            logger.info("Start: Executing a Service");
            for (int i = 1; i <= 3; i++) {
                ConcurrentUtils.delay(200);
                logger.info("Emitting {}", "root " + i);
                if (!s.isUnsubscribed()) {
                    s.onNext("root " + i);
                } else {
                    break;
                }
            }
            logger.info("End: Executing a Service");
            if (!s.isUnsubscribed()) {
                s.onCompleted();
            }
        });
    }

    public String oldMethodLongProcess() {
        logger.info("Start: Emitting long process");
        ConcurrentUtils.delay(300);
        logger.info("End: Emitting long process");
        return "Hello world!";
    }

    public Observable<String> newMethod() {
        return Observable.just(oldMethodLongProcess());
    }

    public Observable<String> newDeferedMethod() {
        return Observable.defer(() -> Observable.just(oldMethodLongProcess())); // for long process
    }
}
