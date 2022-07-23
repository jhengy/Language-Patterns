package reactiveCore;

import static org.junit.jupiter.api.Assertions.*;

class SyncToAsyncTest {
    private static final Logger loggerForTestClass = LoggerFactory.getLogger(SyncToAsync.class);

    @Test
    void testAsyncCallUsingReactor() {
        int delayForBlockCalls = 3000;
        int delayAfterBlockCalls = 5000;

        startRecordingLogsFor(FraudAnalyticsWebScoreServiceTest.class);

        int ONE = 1;
        loggerForTestClass.info(String.format("before blockingCall: [%d] on thread [%s] at time [%s]",
                ONE,
                Thread.currentThread().getName(),
                new Date()));

        attemptAsync1(ONE, delayForBlockCalls)
                .doOnSuccess((resp) -> loggerForTestClass.info(String.format("upon blockingCall success: [%d] on thread [%s] at time [%s]",
                        ONE,
                        Thread.currentThread().getName(),
                        new Date())))
                .doOnError(err -> loggerForTestClass.error(String.format("upon blockingCall failure: [%d] on thread [%s] at time [%s]",
                        ONE,
                        Thread.currentThread().getName(),
                        new Date())))
                .subscribe();

        loggerForTestClass.info(String.format("after blockingCall: [%d] on thread [%s] at time [%s]",
                ONE,
                Thread.currentThread().getName(),
                new Date()));

        int TWO = 2;
        loggerForTestClass.info(String.format("before blockingCall: [%d] on thread [%s] at time [%s]",
                TWO,
                Thread.currentThread().getName(),
                new Date()));

        attemptAsync2(TWO, delayForBlockCalls)
                .doOnSuccess((resp) -> loggerForTestClass.info(String.format("upon blockingCall success: [%d] on thread [%s] at time [%s]",
                        TWO,
                        Thread.currentThread().getName(),
                        new Date())))
                .doOnError(err -> loggerForTestClass.error(String.format("upon blockingCall failure: [%d] on thread [%s] at time [%s]",
                        TWO,
                        Thread.currentThread().getName(),
                        new Date())))
                .subscribe();

        loggerForTestClass.info(String.format("after blockingCall: [%d] on thread [%s] at time [%s]",
                TWO,
                Thread.currentThread().getName(),
                new Date()));


        int THREE = 3;
        loggerForTestClass.info(String.format("before blockingCall: [%d] on thread [%s] at time [%s]",
                THREE,
                Thread.currentThread().getName(),
                new Date()));

        attemptAsync3(THREE, delayForBlockCalls)
                .doOnSuccess((resp) -> loggerForTestClass.info(String.format("upon blockingCall success: [%d] on thread [%s] at time [%s]",
                        THREE,
                        Thread.currentThread().getName(),
                        new Date())))
                .doOnError(err -> loggerForTestClass.error(String.format("upon blockingCall failure: [%d] on thread [%s] at time [%s]",
                        THREE,
                        Thread.currentThread().getName(),
                        new Date())))
                //            .block();
                .subscribe();

        loggerForTestClass.info(String.format("after blockingCall: [%d] on thread [%s] at time [%s]",
                THREE,
                Thread.currentThread().getName(),
                new Date()));

        delay(delayAfterBlockCalls);

        // TODO: maybe can assert the order of logs to verify async behaviour
    }

    private Mono<String> attemptAsync1(int n, long ms) {
        return Mono.fromSupplier(() -> blockingCall(n, ms));
    }

    private Mono<String> attemptAsync2(int n, long ms) {
        return Mono.fromSupplier(() -> n)
                .flatMap(i -> Mono.just(blockingCall(n, ms)));
    }

    private Mono<String> attemptAsync3(int n, long ms) {
        return Mono.fromCallable(() -> blockingCall(n, ms)) // Define blocking call
                .subscribeOn(Schedulers.boundedElastic()); // Define the execution model
    }

    // Just simulate some delay on the current thread.
    public void delay(long timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String blockingCall(Integer input, long timeMs) {
        delay(timeMs);

        String msg = String.format("within blockingCall: [%d] on thread [%s] at time [%s]",
                input,
                Thread.currentThread().getName(),
                new Date());

        loggerForTestClass.info(msg);
        return msg;
    }
}