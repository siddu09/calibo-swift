package reporting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FailureTracker {

    private static final ConcurrentMap<String, String>
            FAILURES = new ConcurrentHashMap<>();

    public static void addFailure(
            String testName,
            String category) {

        FAILURES.put(testName, category);
    }

    public static String getFailureCategory(
            String testName) {

        return FAILURES.getOrDefault(
                testName,
                "N/A");
    }
}