package tests.selfhealing;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import selfhealingHandler.pipeline.LocatorPageObjectPipeline;
import selfhealingHandler.pipeline.PipelineMode;
import selfhealingHandler.pipeline.PipelineOptions;
import selfhealingHandler.pipeline.PipelineResult;

/**
 * Standalone (non-TestNG, no ConfigManager dependency) manual-login POM
 * generator. Opens a HEADED browser, pauses for a human to complete login
 * (including any SSO/MFA), then re-navigates to the target page and runs
 * the unified crawl -> JSON -> self-healing POM pipeline.
 *
 * Run: java -cp <classpath> tests.selfhealing.ManualLoginPomGenerator
 */
public class ManualLoginPomGenerator {

    private static final String TARGET_URL =
            "https://accelerate-qa.calibo.com/tenants/settings/eng-lab-home";
    private static final String PAGE_NAME = "EngLabHome";
    private static final int LOGIN_WAIT_SECONDS = 120;

    public static void main(String[] args) throws Exception {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate(TARGET_URL, new Page.NavigateOptions()
                .setTimeout(60_000)
                .setWaitUntil(WaitUntilState.COMMIT));

        System.out.println("=====================================================");
        System.out.println("A browser window has opened. Please log in now.");
        System.out.println("You have " + LOGIN_WAIT_SECONDS + " seconds to complete login");
        System.out.println("(including any SSO / MFA prompts).");
        System.out.println("=====================================================");

        for (int remaining = LOGIN_WAIT_SECONDS; remaining > 0; remaining -= 10) {
            page.waitForTimeout(10_000);
            System.out.println("... " + Math.max(remaining - 10, 0) + "s remaining, current URL: " + page.url());
        }

        // Ensure we land back on the exact target page after login/redirects.
        page.navigate(TARGET_URL, new Page.NavigateOptions()
                .setTimeout(60_000)
                .setWaitUntil(WaitUntilState.COMMIT));

        // Give the SPA time to actually render its content before crawling.
        try {
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE,
                    new Page.WaitForLoadStateOptions().setTimeout(15_000));
        } catch (Exception ignored) {
            // Some SPAs keep background polling alive indefinitely; fall back to a fixed settle time.
        }
        page.waitForTimeout(8_000);
        System.out.println("Proceeding to crawl. Final URL: " + page.url());

        PipelineOptions options = PipelineOptions.builder()
                .jsonOutputDir("src/main/java/selfhealing/repository")
                .pomOutputDir("src/main/java/pages/generated/selfhealing")
                .mode(PipelineMode.SELF_HEALING)
                .build();

        PipelineResult result = LocatorPageObjectPipeline.run(page, PAGE_NAME, options);

        System.out.println("=====================================================");
        System.out.println("DONE");
        System.out.println("JSON : " + result.jsonPath());
        System.out.println("POM  : " + result.selfHealingPomPath());
        System.out.println("=====================================================");

        browser.close();
        playwright.close();
    }
}
