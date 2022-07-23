package time;

import org.slf4j.Logger;

import java.util.Date;

public class Blocking {
    // Just simulate some delay on the current thread.
    public static void delay(long timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String blockingCall(Integer input, long timeMs, Logger logger, String prefix) {
        delay(timeMs);

        String msg = String.format("within blockingCall: [%d] on thread [%s] at time [%s]",
            input,
            Thread.currentThread().getName(),
            new Date());

        logger.info("{}: {} on thread {} at time {}",
            prefix,
            input,
            Thread.currentThread().getName(),
            new Date());
        return msg;
    }
}
