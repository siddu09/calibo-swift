package tests.base;

import com.microsoft.playwright.options.LoadState;
import ai.config.RagPipelineConfig;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.CaliboAccelerateLoginUtil;
import utils.LoggerUtil;

/**
 * Base class for UI tests that require an authenticated session.
 *
 * <p>Inherits browser lifecycle (setup/teardown) as-is from {@link BaseUITest}.
 * TestNG runs superclass @BeforeMethod before subclass @BeforeMethod, so the
 * browser is guaranteed to be initialized before {@link #login} runs.
 *
 * <p>Test classes extending this get a ready-to-use, authenticated `page` and
 * should contain ONLY business scenario logic.
 */
public class BaseAuthenticatedUITest extends BaseUITest {

    @BeforeMethod
    @Parameters({"tenant"})
    public void login(@Optional("QA_Tenant_604134") String tenant) {

        // Use target URL from config file instead of default resolver
        String targetUrl = RagPipelineConfig.getLoginTargetUrl();
        LoggerUtil.LOGGER.info("[LOGIN] Using config-based target URL: {}", targetUrl);

        try {

            CaliboAccelerateLoginUtil.loginToPage(page, targetUrl, null, tenant);
            LoggerUtil.LOGGER.info("[LOGIN-UI] ✓ Login completed successfully via CaliboAccelerateLoginUtil");
            
            // WAIT for page to fully load after login completes
            waitForPageLoadAfterLogin();

        } catch (Exception e) {

            LoggerUtil.LOGGER.warn(
                    "[LOGIN-UI] Login helper failed, attempting fallback: {}",
                    e.getMessage());

            // Fallback: Navigate directly and wait for page to load
            page.navigate(targetUrl);
            
            // Wait for page to navigate away from /login
            waitForPageLoadAfterLogin();
            
            LoggerUtil.LOGGER.info("[LOGIN-UI] ✓ Fallback navigation complete");
        }
    }

    /**
     * Wait for page to load after login - handles async Azure AD redirect
     * Polls the URL every 500ms until we're no longer on /login
     * Then waits for NETWORKIDLE
     */
    private void waitForPageLoadAfterLogin() {
        LoggerUtil.LOGGER.info("[LOGIN-UI] Waiting for page to load after login...");
        
        long startTime = System.currentTimeMillis();
        long timeout = 10_000; // 10 seconds
        long pollInterval = 500; // 500ms
        
        while (System.currentTimeMillis() - startTime < timeout) {
            String currentUrl = page.url();
            LoggerUtil.LOGGER.debug("[LOGIN-UI] Current URL: {}", currentUrl);
            
            // Check if we're still on login page
            if (!currentUrl.contains("/login")) {
                LoggerUtil.LOGGER.info("[LOGIN-UI] ✓ Navigated away from login. URL: {}", currentUrl);
                
                // Wait for DOM to stabilize
                page.waitForTimeout(2000);
                
                // Wait for network to be idle (all resources loaded)
                try {
                    page.waitForLoadState(LoadState.NETWORKIDLE, 
                        new com.microsoft.playwright.Page.WaitForLoadStateOptions().setTimeout(10_000));
                    LoggerUtil.LOGGER.info("[LOGIN-UI] ✓ Page fully loaded (NETWORKIDLE)");
                } catch (Exception e) {
                    LoggerUtil.LOGGER.warn("[LOGIN-UI] ⚠️ NETWORKIDLE timeout, continuing anyway: {}", e.getMessage());
                }
                
                return;
            }
            
            try {
                Thread.sleep(pollInterval);
            } catch (InterruptedException ignored) {}
        }
        
        LoggerUtil.LOGGER.warn("[LOGIN-UI] ⚠️ Timeout waiting for page load. Still on login page.");
        throw new RuntimeException("Login page redirect timed out after 10 seconds");
    }
}
