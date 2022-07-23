package customLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogGenerator {
    public static Logger getLoggerForClass(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
