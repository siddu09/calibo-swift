package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;


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

    public Locator designCategory(String categoryName) {
        return new ResilientLocator(page, "Design category: " + categoryName)
                .byXPath("//div[contains(@class,'design-category')]//h4[normalize-space()='" + categoryName + "']")
                .resolve();
    }

    public Locator expandDesignCategory(String categoryName) {
        return new ResilientLocator(page, "Expand design category: " + categoryName)
                .byXPath(
                        "//div[contains(@class,'design-category')]" +
                                "[.//h4[normalize-space()='" + categoryName + "']]" +
                                "//button[contains(@class,'btn-develop-expand')]"
                )
                .resolve();
    }

    public Locator designByName(String designName) {
        return new ResilientLocator(page, "Design: " + designName)
                .byCss("h3.design-title[title='" + designName + "']")
                .byXPath(
                        "//h3[contains(@class,'design-title') and @title='" + designName + "']"
                )
                .resolve();
    }

    public Locator designDetailsTitle() {
        return new ResilientLocator(page, "Design Details Title")
                .byCss("#menu .sheet-body h2")
                .byXPath("//div[@id='menu']//div[contains(@class,'sheet-body')]//h2")
                .resolve();
    }

    public Locator designDetailsDescription() {
        return new ResilientLocator(page, "Design Details Description")
                .byXPath(
                        "//div[@id='menu']//label[normalize-space()='Description']" +
                                "/following-sibling::h5[1]"
                )
                .resolve();
    }

    public void openDesign(String designName) {
        designByName(designName).click();
    }


    public Locator expandCategory(String category) {
        return new ResilientLocator(page, "Expand Design Category: " + category)
                .byXPath(
                        "//div[contains(@class,'design-category')]" +
                                "[.//h4[normalize-space()='" + category + "']]" +
                                "//button[contains(@class,'btn-develop-expand')]"
                )
                .resolve();
    }

    public Locator designCard(String designName) {
        return new ResilientLocator(page, "Design: " + designName)
                .byCss("h3.design-title[title='" + designName + "']")
                .byXPath(
                        "//h3[contains(@class,'design-title') and @title='" + designName + "']"
                )
                .resolve();
    }

}
