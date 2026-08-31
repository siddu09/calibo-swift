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
import pages.LoginPage;
import utils.LoggerUtil;

/**
 * Utility to generate self-healing POMs for authenticated pages.
 * 
 * Usage:
 *   1. Set environment variables: CALIBO_USER, CALIBO_PASS
 *   2. Call generatePomForPage(targetUrl, pageName) programmatically OR
 *   3. Run as CLI: java tests.selfhealing.CaliboAcceleratePomGenerator <targetUrl> <pageName>
 */
public class CaliboAcceleratePomGenerator {

    /**
     * Generate a self-healing POM for the given authenticated page.
     * 
     * @param targetUrl The authenticated page URL to crawl (e.g., "https://...tenants/settings/eng-lab-home")
     * @param pageName  The page name for POM/JSON output (e.g., "EngLabHome")
     * @return PipelineResult containing paths to generated JSON and POM files
     * @throws Exception if login fails or crawl fails
     */
    public static PipelineResult generatePomForPage(String targetUrl, String pageName) throws Exception {
        return generatePomForPage(targetUrl, pageName, null);
    }

    public static PipelineResult generatePomForPage(String targetUrl, String pageName, String credentialProfile) throws Exception {
        return generatePomForPage(targetUrl, pageName, credentialProfile, null);
    }

    /**
     * Overload that accepts an explicit tenantName. If tenantName is null, falls
     * back to qa.properties value or default "Automation".
     */
    public static PipelineResult generatePomForPage(String targetUrl, String pageName, String credentialProfile, String tenantName) throws Exception {
        String user = System.getenv("CALIBO_USER");
        String pass = System.getenv("CALIBO_PASS");

        // Fallback order: env vars -> system properties -> qa.properties (specified profile)
        if (user == null || user.isEmpty()) {
            user = System.getProperty("CALIBO_USER");
        }
        if (pass == null || pass.isEmpty()) {
            pass = System.getProperty("CALIBO_PASS");
        }

        if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
            try {
                java.util.Properties cfg = new java.util.Properties();
                cfg.load(new java.io.FileInputStream("src/test/resources/config/envConfig/ui/qa.properties"));
                // Determine profile: method arg -> system property -> qa.properties credentialProfile -> default 'user'
                String profile = credentialProfile;
                if (profile == null || profile.isEmpty()) {
                    profile = System.getProperty("credentialProfile");
                }
                if (profile == null || profile.isEmpty()) {
                    profile = cfg.getProperty("credentialProfile", "aiuser2");
                }

                if (user == null || user.isEmpty()) user = cfg.getProperty(profile);
                if (pass == null || pass.isEmpty()) pass = cfg.getProperty(profile + "Pass");
            } catch (Exception ignored) {
            }
        }

        if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
            throw new IllegalArgumentException("ERROR: Set CALIBO_USER and CALIBO_PASS environment variables before running.");
        }

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        try {
            LoggerUtil.LOGGER.info("[POM-GEN] Target URL: {}", targetUrl);
            LoggerUtil.LOGGER.info("[POM-GEN] Page Name: {}", pageName);
            LoggerUtil.LOGGER.info("[POM-GEN] Navigating to login page...");
            page.navigate(targetUrl, new Page.NavigateOptions().setTimeout(60_000).setWaitUntil(WaitUntilState.COMMIT));

            // Resolve tenantName: parameter -> system property -> qa.properties -> default
            String resolvedTenant = tenantName;
            if (resolvedTenant == null || resolvedTenant.isEmpty()) {
                resolvedTenant = System.getProperty("tenant");
            }
            if (resolvedTenant == null || resolvedTenant.isEmpty()) {
                try {
                    java.util.Properties p = new java.util.Properties();
                    p.load(new java.io.FileInputStream("src/test/resources/config/envConfig/ui/qa.properties"));
                    resolvedTenant = p.getProperty("tenantName", "Automation");
                } catch (Exception ignored) {
                    resolvedTenant = "Automation";
                }
            }

            LoggerUtil.LOGGER.info("[POM-GEN] Using tenant: {}", resolvedTenant);

            // Allow SPA time to render login form
            page.waitForTimeout(5000);
            
            // Wait for username input
            try {
                page.locator("//input[@name='email']").waitFor(
                    new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(15_000));
                LoggerUtil.LOGGER.info("[POM-GEN] Login form detected. Authenticating...");
            } catch (Exception e) {
                LoggerUtil.LOGGER.warn("[POM-GEN] WARNING: Login form not detected, attempting anyway...");
            }

            // Perform login using LoginPage utility
            LoggerUtil.LOGGER.info("[POM-GEN] Step 1: Authenticating with tenant={}", resolvedTenant);
            new LoginPage(page).login(user, pass, resolvedTenant);
            LoggerUtil.LOGGER.info("[POM-GEN] Step 2: Authentication successful, processing OAuth redirect...");
            
            // Wait for OAuth redirect to complete
            page.waitForTimeout(10_000);
            LoggerUtil.LOGGER.info("[POM-GEN] Step 3: OAuth redirect complete. URL: {}", page.url());

            // Navigate to target page
            LoggerUtil.LOGGER.info("[POM-GEN] Step 4: Navigating to target page...");
            page.navigate(targetUrl, new Page.NavigateOptions().setTimeout(60_000).setWaitUntil(WaitUntilState.COMMIT));
            page.waitForTimeout(3000);
            LoggerUtil.LOGGER.info("[POM-GEN] Step 5: Target page loaded. URL: {}", page.url());

            // Wait for SPA to fully render
            try {
                page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE,
                        new Page.WaitForLoadStateOptions().setTimeout(15_000));
            } catch (Exception ignored) {}
            page.waitForTimeout(8_000);

            // Crawl and generate POM
            LoggerUtil.LOGGER.info("[POM-GEN] Step 6: Crawling page and generating POM...");
            PipelineOptions options = PipelineOptions.builder()
                    .jsonOutputDir("src/main/java/selfhealing/repository")
                    .pomOutputDir("src/main/java/pages/generated/selfhealing")
                    .mode(PipelineMode.SELF_HEALING)
                    .build();

            PipelineResult result = LocatorPageObjectPipeline.run(page, pageName, options);
            LoggerUtil.LOGGER.info("[POM-GEN] ✓ POM generation successful!");
            LoggerUtil.LOGGER.info("[POM-GEN] JSON: {}", result.jsonPath());
            LoggerUtil.LOGGER.info("[POM-GEN] POM : {}", result.selfHealingPomPath());
            
            return result;
        } finally {
            browser.close();
            playwright.close();
        }
    }

    /**
     * CLI entry point. Usage: java tests.selfhealing.CaliboAcceleratePomGenerator <targetUrl> <pageName>
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java tests.selfhealing.CaliboAcceleratePomGenerator <targetUrl> <pageName>");
            System.err.println("Example: java tests.selfhealing.CaliboAcceleratePomGenerator https://accelerate-qa.calibo.com/tenants/settings/eng-lab-home EngLabHome");
            System.err.println("\nEnvironment variables required:");
            System.err.println("  CALIBO_USER - Username for authentication");
            System.err.println("  CALIBO_PASS - Password for authentication");
            System.exit(1);
        }

        String targetUrl = args[0];
        String pageName = args[1];
        String credentialProfile = null;
        String tenantName = null;
        if (args.length >= 3) {
            credentialProfile = args[2];
        }
        if (args.length >= 4) {
            tenantName = args[3];
        }
        
        try {
            generatePomForPage(targetUrl, pageName, credentialProfile, tenantName);
            LoggerUtil.LOGGER.info("=====================================================");
            LoggerUtil.LOGGER.info("✓ DONE - POM generated successfully!");
            LoggerUtil.LOGGER.info("=====================================================");
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("=====================================================");
            LoggerUtil.LOGGER.error("❌ FAILED - POM generation failed!");
            LoggerUtil.LOGGER.error("=====================================================");
            LoggerUtil.LOGGER.error("Exception:", e);
            System.exit(1);
        }
    }
}
