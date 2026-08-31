package reporting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ApiExecutionTracker {

    private static final ConcurrentMap<String, String>
            REQUESTS = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, String>
            RESPONSES = new ConcurrentHashMap<>();

    public static void addRequest(
            String testName,
            String request) {

        REQUESTS.put(testName, request);
    }

    public static void addResponse(
            String testName,
            String response) {

        RESPONSES.put(testName, response);
    }

    public static String getRequest(
            String testName) {

        return REQUESTS.getOrDefault(
                testName,
                "N/A");
    }

    public static String getResponse(
            String testName) {

        return RESPONSES.getOrDefault(
                testName,
                "N/A");
    }
}