package reactorCore;

import org.slf4j.Logger;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import time.Blocking;

public class SyncToAsync {

    public static Mono<String> attemptAsync1(int n, long ms, Logger logger, String blockingCallPrefix) {
        return Mono.fromSupplier(() -> Blocking.blockingCall(n, ms, logger, blockingCallPrefix));
    }

    public static Mono<String> attemptAsync2(int n, long ms, Logger logger, String blockingCallPrefix) {
        return Mono.fromSupplier(() -> n)
            .flatMap(i -> Mono.just(Blocking.blockingCall(n, ms, logger, blockingCallPrefix)));
    }

    public static Mono<String> attemptAsync3(int n, long ms, Logger logger, String blockingCallPrefix) {
        return Mono.fromCallable(() -> Blocking.blockingCall(n, ms, logger, blockingCallPrefix)) // Define blocking call
            .subscribeOn(Schedulers.boundedElastic()); // Define the execution model
    }
}