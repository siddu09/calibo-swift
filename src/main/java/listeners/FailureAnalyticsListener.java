package listeners;

import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reporting.FailureTracker;

public class FailureAnalyticsListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        String category = classify(result.getThrowable());
        FailureTracker.addFailure(
                result.getName(),
                category);

        Allure.label("failureCategory", category);

        System.out.println(
                "[FAILURE ANALYTICS] "
                        + result.getName()
                        + " -> "
                        + category);
    }

    private String classify(Throwable throwable) {

        if (throwable == null) {
            return "UNKNOWN";
        }

        String msg = throwable.getMessage();

        if (msg == null) {
            return "UNKNOWN";
        }

        if (msg.contains("Timeout")) return "UI_FAILURE";
        if (msg.contains("401")) return "AUTH_FAILURE";
        if (msg.contains("500")) return "API_FAILURE";

        return "UNKNOWN";
    }
}