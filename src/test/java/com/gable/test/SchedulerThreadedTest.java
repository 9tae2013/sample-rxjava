package com.gable.test;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by realize on 28/11/2558.
 */
public class SchedulerThreadedTest {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerThreadedTest.class);
    private GeneralService aService = new GeneralService();
    private ExecutorService executor1 = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setNameFormat("SubscribeOn-%d").build());
    private ExecutorService executor2 = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setNameFormat("ObserveOn-%d").build());

    @Test
    public void testDefaultThreadedObservable() throws Exception { // Main Thread
        Observable<String> ob1 = aService.getData();

        CountDownLatch latch = new CountDownLatch(1);

        ob1.subscribe(
                s -> logger.info("Got {}", s),
                e -> logger.error(e.getMessage(), e),
                () -> {
                    logger.info("On complete");
                    latch.countDown();
                });

        latch.await();
    }

    @Test
    public void testSubscribeOn() throws Exception { // Thread pool executor1 (SubscribeOn)
        Observable<String> ob1 = aService.getData();

        CountDownLatch latch = new CountDownLatch(1);

        ob1
                .subscribeOn(Schedulers.from(executor1))
                .subscribe(
                        s -> logger.info("Got {}", s),
                        e -> logger.error(e.getMessage(), e),
                        () -> {
                            logger.info("On complete");
                            latch.countDown();
                        });

        latch.await();
    }

    @Test
    public void testObserveOn() throws Exception { // Thread pool executor2 (ObserveOn)
        Observable<String> ob1 = aService.getData();

        CountDownLatch latch = new CountDownLatch(1);

        ob1
                .observeOn(Schedulers.from(executor2))
                .subscribe(
                        s -> logger.info("Got {}", s),
                        e -> logger.error(e.getMessage(), e),
                        () -> {
                            logger.info("On complete");
                            latch.countDown();
                        });

        latch.await();
    }

    @Test
    public void testSubscribeOnAndObserveOn() throws Exception { // Emissions on executor1, Subscription on executor2
        Observable<String> ob1 = aService.getData();

        CountDownLatch latch = new CountDownLatch(1);

        ob1
                .subscribeOn(Schedulers.from(executor1))
                .observeOn(Schedulers.from(executor2))
                .subscribe(
                        s -> {
                            logger.info("Got {}", s);
                        },
                        e -> logger.error(e.getMessage(), e),
                        () -> {
                            logger.info("On complete");
                            latch.countDown();
                        });

        latch.await();
    }
}
