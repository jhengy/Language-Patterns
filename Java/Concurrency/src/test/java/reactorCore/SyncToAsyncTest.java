package reactorCore;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import customLogger.CustomLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static reactorCore.SyncToAsync.*;
import static time.Blocking.delay;

class SyncToAsyncTest {

    private static final Logger logger = CustomLogger.getLoggerForClass(SyncToAsyncTest.class);
    private static final String BEFORE_BLOCKING_CALL = "before blockingCall";
    private static final String WITHIN_BLOCKING_CALL = "within blockingCall";
    private static final String UPON_BLOCKING_CALL_SUCCESS = "upon blockingCall success";
    private static final String UPON_BLOCKING_CALL_FAILURE = "upon blockingCall failure";
    private static final String AFTER_BLOCKING_CALL = "after blockingCall";

    private final long delayForBlockCalls = 1000;
    private final long delayAfterBlockCalls = delayForBlockCalls * 2;

    ListAppender<ILoggingEvent> iLoggingEventListAppender;

    @BeforeEach
    void setup() {
        iLoggingEventListAppender = CustomLogger.startRecordingLogs(SyncToAsyncTest.class);
    }

    @Test
    void testAttempt1() {
        int ONE = 1;
        logger.info(String.format(BEFORE_BLOCKING_CALL + ": [%d] on thread [%s] at time [%s]",
            ONE,
            Thread.currentThread().getName(),
            new Date()));

        attemptAsync1(ONE, delayForBlockCalls, logger, WITHIN_BLOCKING_CALL)
            .doOnSuccess((resp) -> logger.info(String.format(UPON_BLOCKING_CALL_SUCCESS + ": [%d] on thread [%s] at time [%s]",
                ONE,
                Thread.currentThread().getName(),
                new Date())))
            .doOnError(err -> logger.error(String.format(UPON_BLOCKING_CALL_FAILURE + ": [%d] on thread [%s] at time [%s]",
                ONE,
                Thread.currentThread().getName(),
                new Date())))
            .subscribe();

        logger.info(String.format(AFTER_BLOCKING_CALL + ": [%d] on thread [%s] at time [%s]",
            ONE,
            Thread.currentThread().getName(),
            new Date()));

        delay(delayAfterBlockCalls);

        List<String> logs = iLoggingEventListAppender.list.stream()
            .map(ILoggingEvent::getFormattedMessage)
            .collect(Collectors.toList());

        assertThat(logs, hasSize(4));
        assertThat(logs, containsInRelativeOrder(
            containsString(BEFORE_BLOCKING_CALL),
            containsString(WITHIN_BLOCKING_CALL),
            containsString(UPON_BLOCKING_CALL_SUCCESS),
            containsString(AFTER_BLOCKING_CALL)));
    }

    @Test
    void testAttempt2() {
        int TWO = 2;
        logger.info(String.format(BEFORE_BLOCKING_CALL + ": [%d] on thread [%s] at time [%s]",
            TWO,
            Thread.currentThread().getName(),
            new Date()));

        attemptAsync2(TWO, delayForBlockCalls, logger, WITHIN_BLOCKING_CALL)
            .doOnSuccess((resp) -> logger.info(String.format(UPON_BLOCKING_CALL_SUCCESS + ": [%d] on thread [%s] at time [%s]",
                TWO,
                Thread.currentThread().getName(),
                new Date())))
            .doOnError(err -> logger.error(String.format(UPON_BLOCKING_CALL_FAILURE + ": [%d] on thread [%s] at time [%s]",
                TWO,
                Thread.currentThread().getName(),
                new Date())))
            .subscribe();

        logger.info(String.format(AFTER_BLOCKING_CALL + ": [%d] on thread [%s] at time [%s]",
            TWO,
            Thread.currentThread().getName(),
            new Date()));

        delay(delayAfterBlockCalls);

        List<String> logs = iLoggingEventListAppender.list.stream()
            .map(ILoggingEvent::getFormattedMessage)
            .collect(Collectors.toList());

        assertThat(logs, hasSize(4));
        assertThat(logs, containsInRelativeOrder(
            containsString(BEFORE_BLOCKING_CALL),
            containsString(WITHIN_BLOCKING_CALL),
            containsString(UPON_BLOCKING_CALL_SUCCESS),
            containsString(AFTER_BLOCKING_CALL)));
    }

    @Test
    void testAttempt3() {
        int THREE = 3;
        logger.info(String.format(BEFORE_BLOCKING_CALL + ": [%d] on thread [%s] at time [%s]",
            THREE,
            Thread.currentThread().getName(),
            new Date()));

        attemptAsync3(THREE, delayForBlockCalls, logger, WITHIN_BLOCKING_CALL)
            .doOnSuccess((resp) -> logger.info(String.format(UPON_BLOCKING_CALL_SUCCESS + ": [%d] on thread [%s] at time [%s]",
                THREE,
                Thread.currentThread().getName(),
                new Date())))
            .doOnError(err -> logger.error(String.format(UPON_BLOCKING_CALL_FAILURE + ": [%d] on thread [%s] at time [%s]",
                THREE,
                Thread.currentThread().getName(),
                new Date())))
            .subscribe();

        logger.info(String.format(AFTER_BLOCKING_CALL + ": [%d] on thread [%s] at time [%s]",
            THREE,
            Thread.currentThread().getName(),
            new Date()));

        delay(delayAfterBlockCalls);

        List<String> logs = iLoggingEventListAppender.list.stream()
            .map(ILoggingEvent::getFormattedMessage)
            .collect(Collectors.toList());

        assertThat(logs, containsInRelativeOrder(
            containsString(BEFORE_BLOCKING_CALL),
            containsString(AFTER_BLOCKING_CALL),
            containsString(WITHIN_BLOCKING_CALL),
            containsString(UPON_BLOCKING_CALL_SUCCESS)
        ));
        assertThat(logs, hasSize(4));
    }
}