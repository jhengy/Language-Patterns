package reactiveCore;

import customLogger.CustomLogGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Date;

import static reactiveCore.SyncToAsync.*;

class SyncToAsyncTest {

    private static final Logger logger = CustomLogGenerator.getLoggerForClass(SyncToAsyncTest.class);

    @Test
    void testAsyncCallUsingReactor() {
        int delayForBlockCalls = 3000;
        int delayAfterBlockCalls = 5000;

        int ONE = 1;
        logger.info(String.format("before blockingCall: [%d] on thread [%s] at time [%s]",
            ONE,
            Thread.currentThread().getName(),
            new Date()));

        attemptAsync1(ONE, delayForBlockCalls, logger)
            .doOnSuccess((resp) -> logger.info(String.format("upon blockingCall success: [%d] on thread [%s] at time [%s]",
                ONE,
                Thread.currentThread().getName(),
                new Date())))
            .doOnError(err -> logger.error(String.format("upon blockingCall failure: [%d] on thread [%s] at time [%s]",
                ONE,
                Thread.currentThread().getName(),
                new Date())))
            .subscribe();

        logger.info(String.format("after blockingCall: [%d] on thread [%s] at time [%s]",
            ONE,
            Thread.currentThread().getName(),
            new Date()));

        int TWO = 2;
        logger.info(String.format("before blockingCall: [%d] on thread [%s] at time [%s]",
            TWO,
            Thread.currentThread().getName(),
            new Date()));

        attemptAsync2(TWO, delayForBlockCalls, logger)
            .doOnSuccess((resp) -> logger.info(String.format("upon blockingCall success: [%d] on thread [%s] at time [%s]",
                TWO,
                Thread.currentThread().getName(),
                new Date())))
            .doOnError(err -> logger.error(String.format("upon blockingCall failure: [%d] on thread [%s] at time [%s]",
                TWO,
                Thread.currentThread().getName(),
                new Date())))
            .subscribe();

        logger.info(String.format("after blockingCall: [%d] on thread [%s] at time [%s]",
            TWO,
            Thread.currentThread().getName(),
            new Date()));


        int THREE = 3;
        logger.info(String.format("before blockingCall: [%d] on thread [%s] at time [%s]",
            THREE,
            Thread.currentThread().getName(),
            new Date()));

        attemptAsync3(THREE, delayForBlockCalls, logger)
            .doOnSuccess((resp) -> logger.info(String.format("upon blockingCall success: [%d] on thread [%s] at time [%s]",
                THREE,
                Thread.currentThread().getName(),
                new Date())))
            .doOnError(err -> logger.error(String.format("upon blockingCall failure: [%d] on thread [%s] at time [%s]",
                THREE,
                Thread.currentThread().getName(),
                new Date())))
            //            .block();
            .subscribe();

        logger.info(String.format("after blockingCall: [%d] on thread [%s] at time [%s]",
            THREE,
            Thread.currentThread().getName(),
            new Date()));

        delay(delayAfterBlockCalls);

        // TODO: maybe can assert the order of logs to verify async behaviour
    }
}