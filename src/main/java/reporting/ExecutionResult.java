package reporting;

public class ExecutionResult {

    private final String testName;
    private final String status;
    private final String environment;
    private final String timestamp;

    public ExecutionResult(
            String testName,
            String status,
            String environment,
            String timestamp) {

        this.testName = testName;
        this.status = status;
        this.environment = environment;
        this.timestamp = timestamp;
    }

    public String getTestName() {
        return testName;
    }

    public String getStatus() {
        return status;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getTimestamp() {
        return timestamp;
    }
}