package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }


    public Locator enterPassword()
    {
        return new ResilientLocator(page,"Proceed Button")
                .byXPath("//input[@type='password']")
                .resolve();
    }

    public Locator btnProceed()
    {
        return new ResilientLocator(page,"Proceed Button")
                .byCss("button[data-test='proceed-button']")
                .byXPath("//button[@class='btn btn-lg btn-primary login-btn']")
                .byXPath("//button[text()='Proceed']")
                .resolve();
    }
    public Locator enterUsername()
    {
        return new ResilientLocator(page,"Username")
                .byXPath("//input[@name='email']")
                .byName("email")
                .byCss("input[type='email']")
                .byXPath("//input[@label='Email Address']")
                .resolve();
    }

    public Locator dropdownSelectTenant() {
        return new ResilientLocator(page, "Select Tenant")
                .byXPath("//div[text()='Select Tenant']")
                .resolve();
    }

    public Locator clickOptionTenant(String tenant) {
        Locator loc = page.locator("//div[@class='react-select__menu-list css-11unzgr']/div/div/label[text()='"+tenant+"']");
        return loc;
    }

    public void selectBusinessGroup(String Tenant) {
        LoggerUtil.LOGGER.info("[LOGIN] selectBusinessGroup: selecting tenant='" + Tenant + "'");
        // Wait up to 10s for the tenant dropdown to appear
        try {
            page.locator("//div[text()='Select Tenant']").waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(10_000));
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[LOGIN] selectBusinessGroup: dropdown did not appear within 10s: " + e.getMessage());
        }

        int attempts = 2;
        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                LoggerUtil.LOGGER.info("[LOGIN] selectBusinessGroup: attempt " + attempt + " to open dropdown");
                dropdownSelectTenant().click();
                // brief wait for options to render
                Thread.sleep(1000);

                // Primary: exact-match label
                try {
                    Locator option = clickOptionTenant(Tenant);
                    option.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(8_000));
                    option.click();
                    LoggerUtil.LOGGER.info(Tenant + " Tenant Selected (exact match)");
                    return;
                } catch (Exception primaryEx) {
                    LoggerUtil.LOGGER.warn("[LOGIN] selectBusinessGroup: exact match failed: " + primaryEx.getMessage());
                }

                // Fallback: iterate available options and try contains/case-insensitive match
                Locator allOptions = page.locator("//div[contains(@class,'react-select__menu-list')]//label");
                int count = allOptions.count();
                LoggerUtil.LOGGER.info("[LOGIN] selectBusinessGroup: found " + count + " tenant options to inspect");
                String requestedLower = Tenant == null ? "" : Tenant.trim().toLowerCase();
                for (int i = 0; i < count; i++) {
                    try {
                        String text = allOptions.nth(i).innerText().trim();
                        String lower = text.toLowerCase();
                        if (lower.equals(requestedLower) || lower.contains(requestedLower) || requestedLower.contains(lower)) {
                            allOptions.nth(i).click();
                            LoggerUtil.LOGGER.info("[LOGIN] selectBusinessGroup: selected tenant via fuzzy match: '" + text + "'");
                            return;
                        }
                    } catch (Exception e) {
                        // ignore option-level failures
                    }
                }

                LoggerUtil.LOGGER.warn("[LOGIN] selectBusinessGroup: no fuzzy match found among options");

            } catch (Exception ex) {
                LoggerUtil.LOGGER.warn("[LOGIN] selectBusinessGroup: attempt " + attempt + " failed: " + ex.getMessage());
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            }
        }

        // If we get here, list available options for debugging
        try {
            Locator allOptions = page.locator("//div[contains(@class,'react-select__menu-list')]//label");
            int count = allOptions.count();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count; i++) {
                try { sb.append(allOptions.nth(i).innerText()).append("; "); } catch (Exception ignored) {}
            }
            throw new RuntimeException("Failed to select tenant: " + Tenant + ". Available options: " + sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to select tenant: " + Tenant);
        }
    }

    public Locator verifyImage() {
        return new ResilientLocator(page, "Microsoft Logo")
                .byXPath("//img[@role='img' and @alt='Microsoft']")
                .resolve();
    }

    public Locator txtMSSSO_EmailID() {
        return new ResilientLocator(page, "Microsoft TestBox")
                .byXPath("//input[@type='email']")
                .resolve();
    }

    public Locator btnMSSSO_Next() {
        return new ResilientLocator(page, "Microsoft TestBox")
                .byXPath("//input[@type='submit' and @value='Next']")
                .resolve();
    }


    public Locator btnMSSSO_SignIn() {
        return new ResilientLocator(page, "Microsoft TestBox")
                .byXPath("//input[@type='submit' and @value='Sign in']")
                .resolve();
    }

    public Locator btnMSSSO_Yes() {
        return new ResilientLocator(page, "Microsoft TestBox")
                .byXPath("//input[@type='submit' and @value='Yes']")
                .resolve();
    }

    public void login(String username, String password,String tenant) {
        try {
            LoggerUtil.LOGGER.info("[LOGIN] Step 1: Filling username...");
            enterUsername().fill(username);
            LoggerUtil.LOGGER.info("[LOGIN] Step 2: Clicking Proceed...");
            btnProceed().click();
            Thread.sleep(5000);
            LoggerUtil.LOGGER.info("[LOGIN] Step 3: Selecting business group...");
            selectBusinessGroup(tenant);
            LoggerUtil.LOGGER.info("[LOGIN] Step 4: Clicking Proceed after tenant selection...");
            btnProceed().click();
            Thread.sleep(5000);
            LoggerUtil.LOGGER.info("[LOGIN] Step 5: Verifying Microsoft logo...");
//            verifyImage().isVisible();
            Thread.sleep(5000);
            LoggerUtil.LOGGER.info("[LOGIN] Step 6: Filling MS SSO email...");
            txtMSSSO_EmailID().fill(username);
            LoggerUtil.LOGGER.info("[LOGIN] Step 7: Clicking MS Next...");
            btnMSSSO_Next().click();
            Thread.sleep(5000);
            LoggerUtil.LOGGER.info("[LOGIN] Step 8: Filling password...");
            enterPassword().fill(password);
            LoggerUtil.LOGGER.info("[LOGIN] Step 9: Clicking MS Sign In...");
            btnMSSSO_SignIn().click();
            Thread.sleep(5000);
            LoggerUtil.LOGGER.info("[LOGIN] Step 10: Clicking Yes...");
            btnMSSSO_Yes().click();
            Thread.sleep(5000);
            LoggerUtil.LOGGER.info("[LOGIN] ✓ Login completed successfully");
        }
        catch (Exception e) {
            LoggerUtil.LOGGER.error("[LOGIN] ✗ Login failed at some step: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }
}