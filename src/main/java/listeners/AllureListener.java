package listeners;

import drivers.PlaywrightFactory;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.microsoft.playwright.Page;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;

public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        try {

            System.out.println("[ALLURE] onTestFailure triggered");

            Page page = PlaywrightFactory.getPage();

            if (page == null) {

                System.out.println("[ALLURE] Page is null");
                return;
            }
            LoggerUtil.LOGGER.info(
                    "[ALLURE] Capturing screenshot for failed test: "
                            + result.getName());


            byte[] screenshot = page.screenshot();

            System.out.println(
                    "[ALLURE] Screenshot size = "
                            + screenshot.length);

            Allure.addAttachment(
                    "Failure Screenshot",
                    new ByteArrayInputStream(screenshot));

            LoggerUtil.LOGGER.info(
                    "[ALLURE] Screenshot attached successfully");

        } catch (Exception e) {

            System.out.println(
                    "[ALLURE] Screenshot skipped: "
                            + e.getMessage());

            e.printStackTrace();
        }
        System.out.println("######## LISTENER EXECUTED ########");
    }
}