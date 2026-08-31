package pages.PRO.Features.Define;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class BusinessRequirementDetailsPage {
    private final Page page;

    public BusinessRequirementDetailsPage(Page page) {
        this.page = page;
    }

    public Locator fetchBusinessRequirementTitle() {

        return new ResilientLocator(
                page,
                "Business Requirement Title")
                .custom(
                        "Business requirement title",
                        () -> page.locator(
                                        "span.business-req-title")
                                .locator(
                                        "xpath=following-sibling::div[contains(@class,'workflow-status')]"
                                )
                )
                .resolve();
    }

    public Locator fetchPriority() {

        return new ResilientLocator(
                page,
                "Priority")
                .custom(
                        "Priority",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Priority")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator fetchSource() {

        return new ResilientLocator(
                page,
                "Source")
                .custom(
                        "Source",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Source")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator fetchImpact() {

        return new ResilientLocator(
                page,
                "Impact")
                .custom(
                        "Impact",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Impact")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator fetchDescription() {

        return new ResilientLocator(
                page,
                "Description")
                .custom(
                        "Description",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Description")
                                )
                                .locator(
                                        "xpath=following-sibling::div//p"
                                )
                )
                .resolve();
    }

}
