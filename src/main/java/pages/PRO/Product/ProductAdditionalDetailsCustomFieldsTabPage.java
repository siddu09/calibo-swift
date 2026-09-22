package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductAdditionalDetailsCustomFieldsTabPage {
    private final Page page;

    public ProductAdditionalDetailsCustomFieldsTabPage(Page page) {
        this.page = page;
    }

    private Locator field(String label) {
        return page.getByText(label, new Page.GetByTextOptions().setExact(true))
                .locator("xpath=../..");
    }

    public Locator valueInput(String label) {
        return field(label).locator("input:not([aria-autocomplete]), textarea");
    }

    public Locator selectedValue(String label) {
        return field(label).locator("[class*='react-select__single-value']");
    }

    public Locator richText(String label) {
        return field(label).locator("[contenteditable='true']");
    }
}
