package tests.ui.POMgenerator;

import selfhealingHandler.pipeline.LocatorPageObjectPipeline;
import selfhealingHandler.pipeline.PipelineMode;
import selfhealingHandler.pipeline.PipelineOptions;
import selfhealingHandler.pipeline.PipelineResult;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LocatorDiscoveryTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeClass
    public void setUp() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)   // set true if you don't want to see the browser
        );

        page = browser.newPage();
    }

    @Test
    public void captureAllLocatorsFromLoginPage() {

       // page.navigate("https://your-application-url");
        page.navigate("https://accelerate-qa.calibo.com/login");

        //page.navigate("https://the-internet.herokuapp.com/login");

        page.waitForLoadState();

        PipelineOptions options = PipelineOptions.builder()
                .jsonOutputDir("src/main/java/selfhealing/repository")
                .mode(PipelineMode.NONE)
                .build();

        PipelineResult result = LocatorPageObjectPipeline.run(page, "DemoPage", options);

        System.out.println("Locator repository generated: " + result.jsonPath());
    }

    @AfterClass
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}