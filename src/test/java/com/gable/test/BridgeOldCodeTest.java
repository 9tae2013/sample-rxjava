package com.gable.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * Created by realize on 28/11/2558.
 */
public class BridgeOldCodeTest {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerThreadedTest.class);
    private GeneralService aService = new GeneralService();

    @Test
    public void testBlockThread() {
        Observable<String> ob1 = aService.newMethod(); // old method will be call
        logger.info("Obtained Observable");
        ob1.subscribe(
                s -> {
                    logger.info("Got {}", s);
                });
    }

    @Test
    public void testDeferedThread() {
        Observable<String> ob1 = aService.newDeferedMethod();
        logger.info("Obtained Observable");
        ob1.subscribe(
                s -> {
                    logger.info("Got {}", s);
                });
    }
}
