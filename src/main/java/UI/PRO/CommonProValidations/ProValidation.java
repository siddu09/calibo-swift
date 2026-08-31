package UI.PRO.CommonProValidations;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class ProValidation {

    private ProValidation() {
        // Utility class
    }

    public static void validateSuccessMessage(Page page, String expectedMessage) {

        Locator successMessage = new ResilientLocator(page, "PRO Success Snackbar")
                .custom("Snackbar message containing: " + expectedMessage, () -> page.locator("div.snackbar-message").filter(new Locator.FilterOptions().setHasText(expectedMessage)))
                .byText(expectedMessage)
                .resolve();

        assertThat(successMessage).isVisible();
    }
}