package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class DesignDetailsPage {

    private final Page page;

    public DesignDetailsPage(Page page) {
        this.page = page;
    }

    public Locator designTitle() {
        return new ResilientLocator(page, "Design Title")
                .custom(
                        "Design details title",
                        () -> page.locator("#menu .sheet-body h2")
                )
                .resolve();
    }

    public Locator designDescription() {
        return new ResilientLocator(page, "Design Description")
                .custom(
                        "Design details description",
                        () -> page.locator("#menu .sheet-body")
                                .locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Description")
                                )
                                .locator("xpath=following-sibling::h5[1]")
                )
                .resolve();
    }
}