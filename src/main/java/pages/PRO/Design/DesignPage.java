package pages.PRO.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

/**
 * Auto-generated Page Object for: ProductPage
 * Source URL : https://accelerate-qa.calibo.com/projects
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
public class DesignPage {

    private final Page page;

    public DesignPage(Page page) {
        this.page = page;
    }

    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/div[1]/button[1]")
                .resolve();
    }



    public Locator selectDropdownValue(String dropDown) {
        Locator loc = page.locator("//label[text()='"+dropDown+"']");
        return loc;
    }

    public Locator dropDownArrow() {
        return new ResilientLocator(page, "dropDownArrow")
                .byXPath("///i[text()='arrow_drop_down']")
                .resolve();
    }

    public Locator name() {
        return new ResilientLocator(page, "Name")
                .byCss("input[name=\"name\"")
                .byXPath("//*[@name=\"name\"]")
                .resolve();
    }
    public Locator description() {
        return new ResilientLocator(page, "Description")
                .byCss("textarea[name=\"description\"]")
                .byXPath("//*[@name=\"description\"]")
                .resolve();
    }

    public Locator create() {
        return new ResilientLocator(page, "Create")
                .byXPath("//button[text()='Create']")
                .resolve();
    }

    public void chooseCategory(String dropDownValue) {
        page.locator("//div[text()='Choose a category']").click();
        selectDropdownValue(dropDownValue).click();
        LoggerUtil.LOGGER.info(dropDownValue +" Selected DropDown Value");
    }

    public Locator selectDesignSource(String designSource) {
        return page.getByText(designSource)
                .locator("..")
                .locator("div.design-logo");
    }

}
