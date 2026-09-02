package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductDetailsPage {
    private final Page page;

    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    public Locator fetchProductName() {

        return new ResilientLocator(
                page,
                "Product Name")
                .custom(
                        "Product title",
                        () -> page.locator(
                                "div.text-truncate")
                )
                .resolve();
    }

    public Locator fetchProductPortfolio() {

        return new ResilientLocator(
                page,
                "Product Portfolio")
                .custom(
                        "Product portfolio",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Product Portfolio")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]/a"
                                )
                )
                .resolve();
    }

    public Locator fetchBusinessGroup() {

        return new ResilientLocator(
                page,
                "Business Group")
                .custom(
                        "Business group",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Business Group")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator fetchProductDescription() {

        return new ResilientLocator(
                page,
                "Product Description")
                .custom(
                        "Product description",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText(
                                                        "Product Description")
                                )
                                .locator(
                                        "xpath=following-sibling::pre[1]"
                                )
                )
                .resolve();
    }

    public Locator moreHoriz() {
        return new ResilientLocator(page, "Product actions")
                .byText("more_horiz")
                .byRole(AriaRole.BUTTON, "more_horiz")
                .resolve();
    }

    public Locator auditHistory() {
        return new ResilientLocator(page, "Product Audit History")
                .byText("Audit History")
                .byRole(AriaRole.MENUITEM, "Audit History")
                .resolve();
    }

    public Locator auditSearch() {
        return new ResilientLocator(page, "Product audit search")
                .byPlaceholder("Search")
                .byCss("input[type='search']")
                .resolve();
    }

    public Locator auditEventsFilter() {
        return new ResilientLocator(page, "Product audit events filter")
                .byText("Events")
                .byXPath("//button[contains(normalize-space(.),'Events')]")
                .resolve();
    }

    public Locator auditObjectsFilter() {
        return new ResilientLocator(page, "Product audit objects filter")
                .byText("Objects")
                .byXPath("//button[contains(normalize-space(.),'Objects')]")
                .resolve();
    }

    public Locator resetAuditFilters() {
        return new ResilientLocator(page, "Reset Product audit filters")
                .byText("Reset")
                .byXPath("//button[contains(normalize-space(.),'Reset')]")
                .resolve();
    }

    public Locator downloadAuditHistory() {
        return new ResilientLocator(page, "Download Product audit history")
                .byText("Download")
                .byXPath("//button[contains(normalize-space(.),'Download')]")
                .resolve();
    }

    public Locator auditDownloadFormat(String format) {
        return new ResilientLocator(page, "Product audit download format: " + format)
                .byText(format)
                .byXPath("//*[self::button or self::li][normalize-space(.)='" + format + "']")
                .resolve();
    }

}
