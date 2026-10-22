package pages.PRO;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProCommonPage {
    private final Page page;

    public ProCommonPage(Page page) {
        this.page = page;
    }

    public Locator successMessage(String expectedMessage) {
        // Check all supported toast renderings together: a transient message can
        // disappear while a sequential fallback waits for an absent CSS class.
        return new ResilientLocator(page, "PRO Success Snackbar")
                .withFirstStrategyTimeout(30000)
                .custom("Visible success message in alert, status or snackbar", () ->
                        page.getByRole(AriaRole.ALERT)
                                .filter(new Locator.FilterOptions().setHasText(expectedMessage))
                                .or(page.getByRole(AriaRole.STATUS)
                                        .filter(new Locator.FilterOptions().setHasText(expectedMessage)))
                                .or(page.locator("div.snackbar-message")
                                        .filter(new Locator.FilterOptions().setHasText(expectedMessage)))
                                .or(page.getByText(expectedMessage,
                                        new Page.GetByTextOptions().setExact(true)))
                                .filter(new Locator.FilterOptions().setVisible(true)))
                .resolve();
    }
}
