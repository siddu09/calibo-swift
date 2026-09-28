package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class ProductAdditionalDetailsMilestonesTabPage {
    private final Page page;

    public ProductAdditionalDetailsMilestonesTabPage(Page page) {
        this.page = page;
    }

    public Locator timeline(String name) {
        return new ResilientLocator(page, "Product milestone: " + name)
                .custom("Named milestone range", () -> page.locator("input[name='attribute_name'][value='" + name + "']")
                .locator("xpath=ancestor::div[.//input[contains(@class,'range-picker')]][1]")
                .locator("input.range-picker"))
                .byXPath("//input[@name='attribute_name' and @value='" + name
                        + "']/ancestor::div[.//input[contains(@class,'range-picker')]][1]//input[contains(@class,'range-picker')]")
                .resolve();
    }
}
