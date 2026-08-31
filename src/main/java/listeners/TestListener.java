package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import reporting.ExecutionResult;
import reporting.ExecutionTracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TestListener implements ITestListener {

    private static final String ENVIRONMENT = "QA";

    private String getTimestamp() {

        return LocalDateTime.now()
                .format(
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public void onTestSuccess(
            ITestResult result) {

        ExecutionTracker.addResult(

                new ExecutionResult(
                        result.getName(),
                        "PASS",
                        ENVIRONMENT,
                        getTimestamp()));
    }

    @Override
    public void onTestFailure(
            ITestResult result) {

        ExecutionTracker.addResult(

                new ExecutionResult(
                        result.getName(),
                        "FAIL",
                        ENVIRONMENT,
                        getTimestamp()));
    }

    @Override
    public void onTestSkipped(
            ITestResult result) {

        ExecutionTracker.addResult(

                new ExecutionResult(
                        result.getName(),
                        "SKIP",
                        ENVIRONMENT,
                        getTimestamp()));
    }
}