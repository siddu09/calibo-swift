package drivers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
//import utils.AuthCode;
import utils.LoggerUtil;
import configHandler.ConfigManager;

import java.awt.*;

public class PlaywrightFactory {

    private static final ThreadLocal<Page> pageThreadLocal =
            new ThreadLocal<>();

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    int width;
    int height;

    public Page initializeBrowser() {
        LoggerUtil.LOGGER.info("Launching browser");
        playwright = Playwright.create();


        String browserName = ConfigManager.getUIProperty("browser");
        String url = ConfigManager.getUIProperty("baseUrl");

        switch (browserName.toLowerCase()) {
            case "chrome":
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                width = (int) screenSize.getWidth();
                height = (int) screenSize.getHeight();

                browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setSlowMo(1000)
                        .setHeadless(false));

//                context = browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));
//                context.setDefaultTimeout(50000);
//                page = context.newPage();
//                pageThreadLocal.set(page);
//                LoggerUtil.LOGGER.info("Browser launched successfully");
//                page.navigate("https://accelerate-qa.calibo.com/login", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

                break;

            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;

            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;

            default:
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        context.setDefaultTimeout(90000);
        page = context.newPage();
        pageThreadLocal.set(page);
        LoggerUtil.LOGGER.info("Browser launched successfully");
        page.navigate(url+"/login", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        return page;

    }

    public static Page getPage() {
        return pageThreadLocal.get();
    }

    public void closeBrowser() {

        LoggerUtil.LOGGER.info(
                "Closing browser");

        pageThreadLocal.remove();

        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();

        LoggerUtil.LOGGER.info(
                "Browser closed successfully");

    }
}