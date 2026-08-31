package tests.base;

import com.microsoft.playwright.Page;
import drivers.PlaywrightFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.LoggerUtil;

public class BaseUITest {

    protected PlaywrightFactory factory;
    protected Page page;

    @BeforeMethod
    public void setup() {

        LoggerUtil.LOGGER.info(
                "Thread ID = "
                        + Thread.currentThread().threadId());

        LoggerUtil.LOGGER.info(
                "Starting test setup");

        factory = new PlaywrightFactory();
        page = factory.initializeBrowser();
    }

    @AfterMethod
    public void tearDown() {

        LoggerUtil.LOGGER.info(
                "Starting test teardown");

        if (factory == null) return;
        
        String keepBrowserOpen = System.getProperty("keepBrowserOpen");
        LoggerUtil.LOGGER.info("DEBUG: keepBrowserOpen property value = '{}'", keepBrowserOpen);
        
        if (keepBrowserOpen != null && "true".equalsIgnoreCase(keepBrowserOpen.trim())) {
            LoggerUtil.LOGGER.info("✓ Keeping browser open for debugging (keepBrowserOpen=true)");
            return;
        }

        factory.closeBrowser();
    }
}