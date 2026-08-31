package reporting;

import java.util.ArrayList;
import java.util.List;

public class ExecutionTracker {

    private static final List<ExecutionResult>
            RESULTS = new ArrayList<>();

    public static void addResult(
            ExecutionResult result) {

        RESULTS.add(result);
    }

    public static List<ExecutionResult>
    getResults() {

        return RESULTS;
    }
}
