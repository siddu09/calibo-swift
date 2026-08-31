package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;


public class ProductPortfolioAdditionalDetailsCustomFieldsTabPage {

    private final Page page;

    public ProductPortfolioAdditionalDetailsCustomFieldsTabPage(Page page) {
        this.page = page;
    }

    public Locator addCustomFields() {
        return new ResilientLocator(page, "Add More Custom Fields Drawer Button")
                .byRole(AriaRole.BUTTON,"Add More")
                .byCss("button[text='Add More']")
                .byXPath("//button[@text='Add More']")
                .resolve();
    }

    public Locator addFieldName() {
        return new ResilientLocator(page, "Add Custom Field Name Button")
                .byPlaceholder("Field Name")
                .byCss("input[placeholder='Field Name']")
                .byCss("input[name='attributeName']")
                .byXPath("//input[@placeholder='Field Name']")
                .resolve();
    }

    public Locator addFieldValue() {
        return new ResilientLocator(page, "Add Custom Field Value Button")
                .byPlaceholder("Value")
                .byCss("input[placeholder='Value']")
                .byXPath("//input[@placeholder='Value']")
                .resolve();
    }

    public Locator configuredCustomFields() {
        return new ResilientLocator(page, "Configured portfolio custom fields")
                .byCss("#wrapped-tabpanel-CUSTOM_FIELDS input")
                .byXPath("//div[contains(@id,'CUSTOM_FIELDS')]//input")
                .resolve();
    }

    public Locator configuredCustomField(String fieldName) {
        return new ResilientLocator(page, "Configured portfolio custom field: " + fieldName)
                .byXPath("//label[normalize-space()='" + fieldName + "']/following::input[1]")
                .byText(fieldName)
                .resolve();
    }

    public Locator configuredCustomFieldOption(String value) {
        return new ResilientLocator(page, "Configured portfolio custom field option: " + value)
                .byXPath("//div[contains(@class,'menu')]//*[normalize-space()='" + value + "']")
                .byText(value)
                .resolve();
    }





}
