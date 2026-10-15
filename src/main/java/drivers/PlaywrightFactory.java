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


        // Browser is taken from ui/<env>.properties only
        String browserName = ConfigManager.getUIProperty("browser");
        boolean headless = isHeadless();
        String url = ConfigManager.getUIProperty("baseUrl");
        LoggerUtil.LOGGER.info("Browser: {} (headless={})", browserName, headless);

        switch (browserName.toLowerCase()) {
            case "chrome":
                // Screen size is not available in headless mode
                if (!headless && !GraphicsEnvironment.isHeadless()) {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    width = (int) screenSize.getWidth();
                    height = (int) screenSize.getHeight();
                }

                browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setSlowMo(1000)
                        .setHeadless(headless));

//                context = browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));
//                context.setDefaultTimeout(50000);
//                page = context.newPage();
//                pageThreadLocal.set(page);
//                LoggerUtil.LOGGER.info("Browser launched successfully");
//                page.navigate("https://accelerate-qa.calibo.com/login", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

                break;

            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;

            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;

            default:
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        }

        // Headless has no window, so set a fixed viewport size
        Browser.NewContextOptions contextOptions = headless
                ? new Browser.NewContextOptions().setViewportSize(1920, 1080)
                : new Browser.NewContextOptions().setViewportSize(null);
        String locale = browserLocale(headless);
        if (locale != null) {
            contextOptions.setLocale(locale);
        }
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

    /**
     * Run headless when -Dheadless=true or -DisHeadlessBrowser=true is passed.
     * Default is headed (browser window visible).
     */
    public static boolean isHeadless() {
        String flag = System.getProperty("headless", System.getProperty("isHeadlessBrowser", "false"));
        return Boolean.parseBoolean(flag.trim());
    }

    /**
     * Browser language. Headless runs default to en-US: on a Linux agent without LANG set,
     * Chromium reports "en-US@posix", which the app's date formatting cannot handle and the
     * page stays blank. Headed runs keep the machine's own locale. Override with -DbrowserLocale.
     */
    public static String browserLocale(boolean headless) {
        return System.getProperty("browserLocale", headless ? "en-US" : null);
    }

    /**
     * Maximizes a Chromium (headed) window via CDP. Derives the BrowserContext
     * from the given page so it can be called from test flows that only have
     * access to the Page instance (e.g. ProductFlows), not the PlaywrightFactory.
     */
    public static void maximizeChromiumWindow(Page page) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CDPSession session = page.context().newCDPSession(page);
        try {
            com.google.gson.JsonObject window = session.send("Browser.getWindowForTarget");
            com.google.gson.JsonObject parameters = new com.google.gson.JsonObject();
            parameters.addProperty("windowId", window.get("windowId").getAsInt());
            com.google.gson.JsonObject bounds = new com.google.gson.JsonObject();
            bounds.addProperty("windowState", "normal");
            parameters.add("bounds", bounds);
            session.send("Browser.setWindowBounds", parameters);
            bounds.addProperty("left", 0);
            bounds.addProperty("top", 0);
            bounds.addProperty("width", screenSize.width);
            bounds.addProperty("height", screenSize.height);
            session.send("Browser.setWindowBounds", parameters);
            com.google.gson.JsonObject maximized = new com.google.gson.JsonObject();
            maximized.addProperty("windowState", "maximized");
            parameters.add("bounds", maximized);
            session.send("Browser.setWindowBounds", parameters);
            LoggerUtil.LOGGER.info("Browser window maximized to screen size {} x {}",
                    screenSize.width, screenSize.height);
        } finally {
            session.detach();
        }
    }

    public static Page getPage() {
        return pageThreadLocal.get();
    }

    /**
     * Registers the page for this thread so listeners can reach it.
     * Needed by suites that build their own browser instead of calling
     *  - otherwise AllureListener attaches no failure screenshot.
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
