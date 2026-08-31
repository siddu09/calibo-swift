package pages.PRO.Features.Feature;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class FeatureDetailsPage {
    private final Page page;

    public FeatureDetailsPage(Page page) {
        this.page = page;
    }

    public Locator fetchFeatureName() {

        return new ResilientLocator(
                page,
                "Feature Name")
                .custom(
                        "Feature title",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Feature")
                                )
                                .locator(
                                        "xpath=following-sibling::div//h2"
                                )
                )
                .resolve();
    }

    public Locator fetchFeatureDescription() {

        return new ResilientLocator(
                page,
                "Feature Description")
                .custom(
                        "Feature description",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Description")
                                )
                                .locator(
                                        "xpath=following-sibling::div//pre"
                                )
                )
                .resolve();
    }

    public Locator fetchFeatureStatus() {

        return new ResilientLocator(
                page,
                "Feature Status")
                .custom(
                        "Feature status",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Feature Status")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

}
