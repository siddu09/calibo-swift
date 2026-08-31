package reporting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RetryTracker {

    private static final ConcurrentMap<
            String,
            Integer> RETRIES =
            new ConcurrentHashMap<>();

    public static void addRetry(
            String testName) {

        RETRIES.merge(
                testName,
                1,
                Integer::sum);
    }

    public static int getRetryCount(
            String testName) {

        return RETRIES.getOrDefault(
                testName,
                0);
    }
}