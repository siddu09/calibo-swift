package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/** Page object for the product creation wizard's additional-details Others tab. */
public class ProductAdditionalDetailsOthersTabPage {
    private final Page page;

    public ProductAdditionalDetailsOthersTabPage(Page page) {
        this.page = page;
    }

    public Locator othersTab(String label) {
        return new ResilientLocator(page, "Product additional-details tab: " + label)
                .byRole(AriaRole.TAB, label)
                .byCss("button[role='tab'][data-testid*='others']")
                .byCss("[role='tab'][id*='other']")
                .byXPath("//*[@role='tab' and normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator productCode(String label) {
        return new ResilientLocator(page, "Product code input")
                .byName("productCode")
                .byCss("input[id*='productCode'], input[name*='productCode']")
                .byPlaceholder(label)
                .byXPath("//label[normalize-space()='" + label + "']/following::input[1]")
                .resolve();
    }

    public Locator dropdown(String label) {
        return new ResilientLocator(page, "Product additional-details dropdown: " + label)
                .custom("React select associated with label", () -> page.locator("//label[normalize-space()='"
                        + label + "']/following::div[contains(@class,'react-select__control')][1]"))
                .byCss("[data-field='" + label + "'] [class*='react-select__control']")
                .byXPath("//label[starts-with(normalize-space(), '" + label
                        + "')]/ancestor::div[contains(@class,'form-group')][1]//*[contains(@class,'react-select__control')]")
                .resolve();
    }

    public Locator dropdownSearch(String label) {
        return new ResilientLocator(page, "Product additional-details dropdown search: " + label)
                .custom("Search input in the associated dropdown", () -> dropdown(label).locator("input"))
                .byCss("input[aria-autocomplete='list']")
                .byXPath("//label[normalize-space()='" + label + "']/following::input[@type='text'][1]")
                .resolve();
    }

    public Locator dropdownOption(String value) {
        return new ResilientLocator(page, "Product additional-details dropdown option")
                .custom("Exact option in open menu", () -> page.locator("[class*='react-select__menu']")
                        .getByText(value, new Locator.GetByTextOptions().setExact(true)))
                .byCss("[role='option'][data-value='" + value + "']")
                .byXPath("//*[contains(@class,'react-select__option') and normalize-space()='" + value + "']")
                .resolve();
    }

    public Locator selectedValue(String label) {
        return new ResilientLocator(page, "Selected product additional-details value: " + label)
                .byXPath("//label[starts-with(normalize-space(), '" + label
                        + "')]/following::div[contains(@class,'react-select__single-value') or contains(@class,'react-select__multi-value')][1]")
                .resolve();
    }
}
