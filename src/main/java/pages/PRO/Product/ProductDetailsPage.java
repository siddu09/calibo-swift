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

    public Locator moreHoriz() {
        return new ResilientLocator(page, "Product more actions")
                .byText("more_horiz")
                .byXPath("//button[.//*[normalize-space()='more_horiz']]")
                .resolve();
    }

    public Locator deleteProduct() {
        return new ResilientLocator(page, "Delete Product")
                .byRole(AriaRole.MENUITEM, "Delete")
                .byXPath("//*[self::button or self::li][normalize-space()='Delete' or normalize-space()='Delete Product']")
                .resolve();
    }

    public Locator deleteProductReason() {
        return new ResilientLocator(page, "Product deletion reason")
                .byCss("textarea[placeholder='Enter your comments...']")
                .byXPath("//textarea")
                .resolve();
    }

    public Locator confirmDeleteProduct() {
        return new ResilientLocator(page, "Confirm Delete Product")
                .byXPath("//textarea/ancestor::div[.//button[normalize-space()='Delete']][1]//button[normalize-space()='Delete' and not(@disabled)]")
                .byRole(AriaRole.BUTTON, "Delete")
                .resolve();
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

}
