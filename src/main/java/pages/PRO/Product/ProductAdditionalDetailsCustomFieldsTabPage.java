package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class ProductAdditionalDetailsCustomFieldsTabPage {
    private final Page page;

    public ProductAdditionalDetailsCustomFieldsTabPage(Page page) {
        this.page = page;
    }

    private Locator field(String label) {
        return new ResilientLocator(page, "Custom field: " + label)
                .custom("Exact field label container", () -> page.getByText(label,
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=../.."))
                .byXPath("//label[normalize-space()='" + label + "']/../..")
                .resolve();
    }

    public Locator valueControl(String label) {
        return new ResilientLocator(page, "Custom field value: " + label)
                .custom("Value in field container", () -> field(label)
                        .locator("input:not([aria-autocomplete]), textarea, [class*='react-select__single-value']"))
                .byXPath("//label[normalize-space()='" + label
                        + "']/../..//*[self::input[not(@aria-autocomplete)] or self::textarea or contains(@class,'react-select__single-value')]")
                .resolve();
    }

    public Locator valueInput(String label) {
        return new ResilientLocator(page, "Custom field input: " + label)
                .custom("Input in field container", () -> field(label).locator("input:not([aria-autocomplete]), textarea"))
                .byXPath("//label[normalize-space()='" + label + "']/../..//*[self::input[not(@aria-autocomplete)] or self::textarea]")
                .resolve();
    }

    public Locator selectedValue(String label) {
        return field(label).locator("[class*='react-select__single-value']");
    }

    public Locator richText(String label) {
        return new ResilientLocator(page, "Custom rich text: " + label)
                .custom("Editor in field container", () -> field(label).locator("[contenteditable='true']"))
                .byXPath("//label[normalize-space()='" + label + "']/../..//*[@contenteditable='true']")
                .resolve();
    }

    public Locator dropdown(String label) {
        return new ResilientLocator(page, "Product custom field dropdown: " + label)
                .custom("Dropdown beside field", () -> field(label).locator("[class*='react-select__control']"))
                .byXPath("//label[normalize-space()='" + label + "']/../..//*[contains(@class,'react-select__control')]")
                .resolve();
    }

    public Locator deleteDynamicField(String name) {
        return new ResilientLocator(page, "Delete product custom field: " + name)
                .custom("Existing portfolio field delete", () ->
                        new pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsCustomFieldsTabPage(page)
                                .deleteLocalField(name))
                .byXPath("//input[@placeholder='Field Name' and @value='" + name
                        + "']/ancestor::div[contains(concat(' ',normalize-space(@class),' '),' row ')][1]//*[contains(@class,'attribute-add-del')]//*[local-name()='svg']")
                .resolve();
    }

    public Locator deleteDialog() {
        return new ResilientLocator(page, "Product detail deletion dialog")
                .byRole(com.microsoft.playwright.options.AriaRole.DIALOG, null)
                .byCss(".modal.show .modal-content")
                .resolve();
    }

    public Locator confirmDeleteDetail() {
        return new ResilientLocator(page, "Confirm product detail deletion")
                .custom("Existing portfolio delete confirmation", () ->
                        new pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsCustomFieldsTabPage(page)
                                .confirmDeleteLocalField())
                .byXPath("//*[@role='dialog']//button[normalize-space()='Delete' and not(@disabled)]")
                .resolve();
    }

}
