package UI.E2E;

import configHandler.ConfigManager;
import pages.LoginPage;
import utils.LoggerUtil;
import com.microsoft.playwright.Page;

public class LoginBuildingBlock {

    private final Page page;

    private final String username =
            ConfigManager.getUIProperty("user.DSO");

    private final String password =
            ConfigManager.getUIProperty("pass.DSO");

    private final String tenantName =
            ConfigManager.getUIProperty("tenantName.DSO");

    public LoginBuildingBlock(Page page) {
        this.page = page;
    }

    public void login() {

        LoggerUtil.LOGGER.info("========== Login Started ==========");

        LoggerUtil.LOGGER.info(
                "[LOGIN DEBUG] Username={} Tenant={}",
                username,
                tenantName
        );

        // Fail-fast if credentials missing: provide clear message rather than Playwright error
        if (username == null || username.isBlank()) {
            LoggerUtil.LOGGER.error("[LOGIN] Missing username (user.DSO). Provide via config file or -Duser.DSO=...");
            throw new IllegalStateException("Missing UI property: user.DSO");
        }
        if (password == null || password.isBlank()) {
            LoggerUtil.LOGGER.error("[LOGIN] Missing password (pass.DSO). Provide via config file or -Dpass.DSO=...");
            throw new IllegalStateException("Missing UI property: pass.DSO");
        }

        LoginPage loginPage = new LoginPage(page);

        loginPage.login(
                username,
                password,
                tenantName
        );

        page.waitForLoadState();

        LoggerUtil.LOGGER.info("========== Login Completed ==========");
    }
    // NEW — overload, only used where an explicit tenant override is needed
    public void login(String tenantOverride) {

        LoggerUtil.LOGGER.info(
                "========== Login Started (tenant override) =========="
        );

        String resolvedTenant = (tenantOverride != null && !tenantOverride.isBlank())
                ? tenantOverride
                : tenantName;

        LoggerUtil.LOGGER.info(
                "[LOGIN] Username={} ResolvedTenant={}",
                username,
                resolvedTenant
        );

        // Fail-fast if credentials missing
        if (username == null || username.isBlank()) {
            LoggerUtil.LOGGER.error("[LOGIN] Missing username (user.DSO). Provide via config file or -Duser.DSO=...");
            throw new IllegalStateException("Missing UI property: user.DSO");
        }
        if (password == null || password.isBlank()) {
            LoggerUtil.LOGGER.error("[LOGIN] Missing password (pass.DSO). Provide via config file or -Dpass.DSO=...");
            throw new IllegalStateException("Missing UI property: pass.DSO");
        }

        LoginPage loginPage = new LoginPage(page);

        loginPage.login(
                username,
                password,
                resolvedTenant
        );

        page.waitForLoadState();

        LoggerUtil.LOGGER.info(
                "========== Login Completed =========="
        );
    }
}