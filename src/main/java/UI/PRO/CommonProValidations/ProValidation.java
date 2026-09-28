package UI.PRO.CommonProValidations;

import com.microsoft.playwright.Page;
import pages.PRO.ProCommonPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class ProValidation {

    private ProValidation() {
        // Utility class
    }

    public static void validateSuccessMessage(Page page, String expectedMessage) {

        assertThat(new ProCommonPage(page).successMessage(expectedMessage)).isVisible();
    }
}