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

    public Locator newMilestoneName() {
        return new ResilientLocator(page, "New milestone name")
                .byCss("input[placeholder='Milestone Name']:not([disabled]):not([readonly])")
                .byXPath("//input[@name='attribute_name' and not(@disabled) and not(@readonly)]")
                .resolve();
    }

    public Locator deleteMilestone(String name) {
        return new ResilientLocator(page, "Delete milestone: " + name)
                .byXPath("//input[@name='attribute_name' and @value='" + name
                        + "']/ancestor::div[.//input[contains(@class,'range-picker')]][1]//*[contains(@class,'attribute-add-del')]//*[local-name()='svg']")
                .custom("Delete in named milestone row", () -> timeline(name)
                        .locator("xpath=ancestor::div[.//input[@name='attribute_name']][1]")
                        .locator(".attribute-add-del svg"))
                .resolve();
    }

    public Locator milestoneNameField(String name) {
        return new ResilientLocator(page, "Product milestone form")
                .byCss("form")
                .byXPath("//form")
                .resolve().locator("input[name='attribute_name'][value='" + name + "']");
    }

}
