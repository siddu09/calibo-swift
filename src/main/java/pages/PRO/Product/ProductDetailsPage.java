package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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

}
