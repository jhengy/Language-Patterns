package customLogger;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

public class CustomLogRecorder {
    public static ListAppender<ILoggingEvent> recordLogForClass(Class<?> clazz) {
        Logger logger = (Logger) getLogger(clazz);

        ListAppender<ILoggingEvent> loggingEventListAppender = new ListAppender<>();
        loggingEventListAppender.start();
        logger.addAppender(loggingEventListAppender);

        return loggingEventListAppender;
    }

    // when LogEvents comes with arguments -> filter out certain arguments in the log by type
    public static List<?> getLoggingEvent(ListAppender<ILoggingEvent> loggingEventListAppender, Class<?> clazz) {
        return List.copyOf(loggingEventListAppender.list).stream()
            .filter(event -> event.getArgumentArray() != null)
            .flatMap(event -> stream(event.getArgumentArray()))
            .filter(clazz::isInstance)
            .map(clazz::cast)
            .collect(Collectors.toList());
    }
}
