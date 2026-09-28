package pages.PRO;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class ProCommonPage {
    private final Page page;

    public ProCommonPage(Page page) {
        this.page = page;
    }

    public Locator successMessage(String expectedMessage) {
        return new ResilientLocator(page, "PRO Success Snackbar")
                .custom("Snackbar message containing: " + expectedMessage, () -> {
                    Locator message = page.locator("div.snackbar-message")
                            .filter(new Locator.FilterOptions().setHasText(expectedMessage));
                    message.waitFor();
                    return message;
                })
                .byText(expectedMessage)
                .resolve();
    }
}
