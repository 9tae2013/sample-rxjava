package com.gable.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * Created by realize on 28/11/2558.
 */
public class PageService {
    private static final Logger logger = LoggerFactory.getLogger(PageService.class);

    public Observable<Integer> getPages(int totalPages) {
        return Observable.create(subscriber -> {
            logger.info("Start: Getting pages");
            for (int i = 1; i <= totalPages; i++) {
                logger.info("Emitting {}", "root " + i);
                subscriber.onNext(i);
            }
            logger.info("End: Getting pages");
            subscriber.onCompleted();
        });
    }

    public Observable<String> actOnAPage(int pageNum) {
        return Observable.<String>create(s -> {
            ConcurrentUtils.delay(300);
            logger.info("Acting on page {}",  pageNum);
            s.onNext("Page " + pageNum);
            s.onCompleted();
        });
    }
}
