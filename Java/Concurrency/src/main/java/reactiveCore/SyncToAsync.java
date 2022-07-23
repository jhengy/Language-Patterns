package reactiveCore;

import org.slf4j.Logger;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;

public class SyncToAsync {

    public static Mono<String> attemptAsync1(int n, long ms, Logger logger) {
        return Mono.fromSupplier(() -> blockingCall(n, ms, logger));
    }

    public static Mono<String> attemptAsync2(int n, long ms, Logger logger) {
        return Mono.fromSupplier(() -> n)
            .flatMap(i -> Mono.just(blockingCall(n, ms, logger)));
    }

    public static Mono<String> attemptAsync3(int n, long ms, Logger logger) {
        return Mono.fromCallable(() -> blockingCall(n, ms, logger)) // Define blocking call
            .subscribeOn(Schedulers.boundedElastic()); // Define the execution model
    }

    // Just simulate some delay on the current thread.
    public static void delay(long timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String blockingCall(Integer input, long timeMs, Logger logger) {
        delay(timeMs);

        String msg = String.format("within blockingCall: [%d] on thread [%s] at time [%s]",
            input,
            Thread.currentThread().getName(),
            new Date());

        logger.info(msg);
        return msg;
    }
}