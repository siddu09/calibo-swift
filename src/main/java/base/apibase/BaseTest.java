package base.apibase;

import common.apiCommon.AuthCode;
import common.apiCommon.RequestSpecProvider;
import config.Api.apiConfig.Config;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    @Severity(SeverityLevel.NORMAL)
    @Step("Capturing Auth Token")
    public void setupSuite() {
        System.out.println("🔥CALIBO API TEST SUITE INITIALIZATION STARTED");
        Config.load();
        AuthCode.login();
        RequestSpecProvider.initialize();
        System.out.println("✅Auth Token captured successfully\n");

    }

    @AfterSuite
    public void tearDownSuite() {
    }
}