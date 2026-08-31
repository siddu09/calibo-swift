package tests.selfhealing;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import selfhealingHandler.pipeline.LocatorPageObjectPipeline;
import selfhealingHandler.pipeline.PipelineMode;
import selfhealingHandler.pipeline.PipelineOptions;
import selfhealingHandler.pipeline.PipelineResult;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * End-to-end Self-Healing vNext demo for the Calibo SWIFT framework:
 *   1. Crawl a page      -> PageSnapshot
 *   2. Persist to JSON   -> selfhealing/repository/<Page>.json
 *   3. Generate POMs     -> pages/generated/<Page>Page.java   (PLAIN + SELF_HEALING)
 *
 * Generated classes are STARTING SKELETONS - review locators for
 * repeatable rows / weak css fallbacks before wiring into real tests.
 */
public class PageObjectGenerationTest {

    private static final String REPO_DIR = "src/main/java/selfhealing/repository";
    private static final String GENERATED_DIR = "src/main/java/pages/generated";
    private static final String GENERATED_SH_DIR = "src/main/java/pages/generated/selfhealing";

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeClass
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @Test
    public void crawlThenGenerateBothModes() throws Exception {
        String pageName = "DemoPage";

        page.navigate("https://accelerate-qa.calibo.com/login");
        page.waitForLoadState();

        PipelineOptions options = PipelineOptions.builder()
                .jsonOutputDir(REPO_DIR)
                .pomOutputDir(GENERATED_DIR)
                .selfHealingPomOutputDir(GENERATED_SH_DIR)
                .mode(PipelineMode.BOTH)
                .build();

        PipelineResult result = LocatorPageObjectPipeline.run(page, pageName, options);

        Assert.assertTrue(Files.exists(result.plainPomPath()), "Plain POM not generated");
        Assert.assertTrue(Files.exists(result.selfHealingPomPath()), "Self-healing POM not generated");
        System.out.println("PLAIN        : " + result.plainPomPath().toAbsolutePath());
        System.out.println("SELF_HEALING : " + result.selfHealingPomPath().toAbsolutePath());
    }

    /** Generate the self-healing POM from the existing Calibo JSON. */
    @Test
    public void generateSelfHealingFromExistingJson() throws Exception {
        Path json = Path.of(REPO_DIR, "DemoPage.json");
        if (!Files.exists(json)) {
            throw new org.testng.SkipException("No JSON at " + json + " - skipping");
        }

        PipelineOptions options = PipelineOptions.builder()
                .pomOutputDir(GENERATED_SH_DIR)
                .mode(PipelineMode.SELF_HEALING)
                .build();

        PipelineResult result = LocatorPageObjectPipeline.runFromJson(json.toString(), options);

        Assert.assertTrue(Files.exists(result.selfHealingPomPath()));
        System.out.println("SELF_HEALING : " + result.selfHealingPomPath().toAbsolutePath());
    }

    @AfterClass
    public void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
