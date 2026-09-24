package drivers;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
//import utils.AuthCode;
import utils.LoggerUtil;
import configHandler.ConfigManager;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(null);
        boolean videoRecordingEnabled = Boolean.parseBoolean(
                ConfigManager.getUIProperty("videoRecordingEnabled"));
        if (videoRecordingEnabled) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path videoDir = Paths.get("videos", timestamp);
            try {
                Files.createDirectories(videoDir);
            } catch (Exception e) {
                LoggerUtil.LOGGER.error("Unable to create video directory: {}", videoDir, e);
                throw new RuntimeException("Unable to create video directory: " + videoDir, e);
            }
            contextOptions.setRecordVideoDir(videoDir);
        }

        context = browser.newContext(contextOptions);
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

    /**
     * Registers the page for this thread so listeners can reach it.
     * Needed by suites that build their own browser instead of calling
     * {@link #initBrowser} - otherwise AllureListener attaches no failure screenshot.
     * Call again whenever the page is replaced (e.g. session reuse).
     */
    public static void setPage(Page page) {
        pageThreadLocal.set(page);
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
