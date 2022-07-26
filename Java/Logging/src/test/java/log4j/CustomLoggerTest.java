package log4j;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import customLogger.CustomLogger;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CustomLoggerTest {
    Logger logger = LoggerFactory.getLogger(CustomLoggerTest.class);

    @Test
    void testRecordLogForClass() {
        ListAppender<ILoggingEvent> iLoggingEventListAppender = CustomLogger.startRecordingLogs(CustomLoggerTest.class);

        int numEvents = 3;
        IntStream.range(0, numEvents)
            .forEach(i -> logger.info("log {}", i));

        List<ILoggingEvent> logs = iLoggingEventListAppender.list;
        assertThat(logs, hasSize(numEvents));
        IntStream.range(0, numEvents)
            .forEach(i -> {
                ILoggingEvent event = logs.get(i);
                assertThat(event.getFormattedMessage(), containsString("log"));
                assertThat(event.getArgumentArray().length, is(1));
                assertThat(event.getArgumentArray()[0], is(i));
            });
    }

    @Test
    void testGetLoggingEvent() {
        ListAppender<ILoggingEvent> iLoggingEventListAppender = CustomLogger.startRecordingLogs(CustomLoggerTest.class);
        logger.info("log to test with param={}", 1);
        logger.info("log to test with param={}", new TestClass(1));

        List<?> intArgs = CustomLogger.getArgsListFromLogs(iLoggingEventListAppender, Integer.class);
        assertThat(intArgs.size(), is(1));

        List<?> testClassArgs = CustomLogger.getArgsListFromLogs(iLoggingEventListAppender, Integer.class);
        assertThat(testClassArgs.size(), is(1));

    }

    private static class TestClass {
        int i;

        public TestClass(int i) {
            this.i = i;
        }
    }
}