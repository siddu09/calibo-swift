package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductAdditionalDetailsMilestonesTabPage {
    private final Page page;

    public ProductAdditionalDetailsMilestonesTabPage(Page page) {
        this.page = page;
    }

    public Locator timeline(String name) {
        return page.locator("input[name='attribute_name'][value='" + name + "']")
                .locator("xpath=ancestor::div[.//input[contains(@class,'range-picker')]][1]")
                .locator("input.range-picker");
    }
}
