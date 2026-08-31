package tests.selfhealing;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import selfhealingHandler.pipeline.PipelineResult;
import utils.LoggerUtil;

/**
 * TestNG wrapper for POM generation from authenticated pages.
 * 
 * Supports both parameterized single-run (via testng.xml) and DataProvider batch runs.
 */
public class GeneratePomFromPageTest {

    /**
     * Parameterized single-run: targetUrl, pageName, credentialProfile
     */
    @Test
    @Parameters({"targetUrl", "pageName", "credentialProfile"})
    public void generatePomForPage(String targetUrl, String pageName, String credentialProfile) throws Exception {
        LoggerUtil.LOGGER.info("========================================");
        LoggerUtil.LOGGER.info("TestNG: Generating POM");
        LoggerUtil.LOGGER.info("  Target URL: {}", targetUrl);
        LoggerUtil.LOGGER.info("  Page Name:  {}", pageName);
        LoggerUtil.LOGGER.info("  Profile:    {}", credentialProfile);
        LoggerUtil.LOGGER.info("========================================");
        
        PipelineResult result = CaliboAcceleratePomGenerator.generatePomForPage(targetUrl, pageName, credentialProfile);
        
        LoggerUtil.LOGGER.info("========================================");
        LoggerUtil.LOGGER.info("✓ TestNG: POM generation successful!");
        LoggerUtil.LOGGER.info("  JSON: {}", result.jsonPath());
        LoggerUtil.LOGGER.info("  POM:  {}", result.selfHealingPomPath());
        LoggerUtil.LOGGER.info("========================================");
    }

    /**
     * Batch generate POMs for multiple pages using DataProvider
     */
    @Test(dataProvider = "pageDataProvider")
    public void generatePomForPages(String targetUrl, String pageName, String credentialProfile) throws Exception {
        generatePomForPage(targetUrl, pageName, credentialProfile);
    }

    /**
     * DataProvider for batch POM generation. Default profile = aiuser1
     */
    @org.testng.annotations.DataProvider(name = "pageDataProvider")
    public Object[][] pageDataProvider() {
        return new Object[][] {
            { "https://accelerate-qa.calibo.com/projects/project-view/76313540-f5e3-460a-a1b4-7b9d563dd820#WORKSTREAMS", "FeaturesPage", "aiuser2" },
            // Add more entries here for batch generation
            // { "https://accelerate-qa.calibo.com/dashboard", "Dashboard", "aiuser1" },
        };
    }
}
