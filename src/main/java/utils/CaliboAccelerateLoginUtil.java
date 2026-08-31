package utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import pages.LoginPage;
import utils.LoggerUtil;

/**
 * Calibo-specific reusable programmatic login helper (formerly ProgrammaticLoginUtil).
 * Tests can call CaliboAccelerateLoginUtil.loginToPage(page, targetUrl, credentialProfile, tenant)
 */
public final class CaliboAccelerateLoginUtil {

    private CaliboAccelerateLoginUtil() {}

    // Backward-compatible delegate
    public static void loginToPage(Page page, String targetUrl, String credentialProfile) throws Exception {
        loginToPage(page, targetUrl, credentialProfile, null);
    }

    /**
     * Reusable programmatic login helper.
     * If tenantName is null/empty, it falls back to qa.properties tenantName (default "Automation").
     */
    public static void loginToPage(Page page, String targetUrl, String credentialProfile, String tenantName) throws Exception {
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
                // Determine which properties file to load based on credentialProfile
                // If profile starts with "aiml" or "ai", load from aiml/ directory
                // Otherwise, load from ui/ directory (default)
                String propsPath = "src/test/resources/config/envConfig/ui/qa.properties";
                
                if (credentialProfile != null && credentialProfile.toLowerCase().startsWith("ai")) {
                    propsPath = "src/test/resources/config/envConfig/aiml/qa.properties";
                    LoggerUtil.LOGGER.info("[LOGIN-UTIL] Loading credentials from AIML config: {}", propsPath);
                } else {
                    LoggerUtil.LOGGER.info("[LOGIN-UTIL] Loading credentials from UI config: {}", propsPath);
                }
                
                java.util.Properties cfg = new java.util.Properties();
                cfg.load(new java.io.FileInputStream(propsPath));
                // Determine profile: method arg -> system property -> qa.properties credentialProfile -> default 'aiuser2'
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

        // Attempt navigation to trigger login flow (SPA may redirect to login)
        LoggerUtil.LOGGER.info("[LOGIN-UTIL] Navigating to {} to trigger authentication flow", targetUrl);
        page.navigate(targetUrl, new Page.NavigateOptions().setTimeout(60_000).setWaitUntil(WaitUntilState.COMMIT));

        // Resolve tenantName: parameter -> system property -> qa.properties -> default
        String resolvedTenant = tenantName;
        // Normalize patterns like 'YourTenantName=QA_Tenant_604134' -> 'QA_Tenant_604134'
        if (resolvedTenant != null && resolvedTenant.contains("=")) {
            String[] parts = resolvedTenant.split("=", 2);
            resolvedTenant = parts[1].trim();
            LoggerUtil.LOGGER.info("[LOGIN-UTIL] Normalized tenant parameter to '{}'", resolvedTenant);
        }

        if (resolvedTenant == null || resolvedTenant.isEmpty()) {
            resolvedTenant = System.getProperty("tenant");
        }
        if (resolvedTenant != null && resolvedTenant.contains("=")) {
            String[] parts = resolvedTenant.split("=", 2);
            resolvedTenant = parts[1].trim();
            LoggerUtil.LOGGER.info("[LOGIN-UTIL] Normalized tenant system property to '{}'", resolvedTenant);
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
        if (resolvedTenant == null || resolvedTenant.isEmpty()) {
            resolvedTenant = "Automation";
        }

        // Allow SPA time to render login form
        page.waitForTimeout(3000);

        // Wait for username input (best-effort)
        try {
            page.locator("//input[@name='email']").waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(15_000));
            LoggerUtil.LOGGER.info("[LOGIN-UTIL] Login form detected. Authenticating...");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[LOGIN-UTIL] WARNING: Login form not detected, attempting anyway...");
        }

        // Perform login using LoginPage utility
        LoggerUtil.LOGGER.info("[LOGIN-UTIL] Authenticating with tenant={}", resolvedTenant);
        new LoginPage(page).login(user, pass, resolvedTenant);

        // Wait for OAuth / redirect to complete then navigate to target page to ensure content is loaded
        page.waitForTimeout(6_000);
        LoggerUtil.LOGGER.info("[LOGIN-UTIL] Navigating to target page after authentication: {}", targetUrl);
        page.navigate(targetUrl, new Page.NavigateOptions().setTimeout(60_000).setWaitUntil(WaitUntilState.COMMIT));

        try {
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE,
                    new Page.WaitForLoadStateOptions().setTimeout(15_000));
        } catch (Exception ignored) {}
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info("[LOGIN-UTIL] Login flow complete. Current URL: {}", page.url());
    }
}
