package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

/**
 * Auto-generated Page Object for: NewProductPortfolioPage
 * Source URL : https://accelerate-qa.calibo.com/portfolios/add-portfolio
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
public class NewReleasePage {

    private final Page page;

    public NewReleasePage(Page page) {
        this.page = page;
    }

    public Locator name() {
        return new ResilientLocator(page, "name")
                .byName("releaseName")
                .byCss("input[name='releaseName']")
                .byXPath("//*[@name='releaseName']")
                .resolve();
    }

    public Locator version() {
        return new ResilientLocator(page, "version")
                .byName("releaseVersion")
                .byCss("input[name='releaseVersion']")
                .byXPath("//*[@name='releaseVersion']")
                .resolve();
    }

    public Locator releaseId() {
        return new ResilientLocator(page, "releaseId")
                .byName("releaseId")
                .byCss("input[name='releaseId']")
                .byXPath("//*[@name='releaseId']")
                .resolve();
    }

    public Locator releaseObjective() {
        return new ResilientLocator(page, "releaseObjective")
                .byName("releaseObjective")
                .byCss("textarea[name='releaseObjective']")
                .byXPath("//*[@name='releaseObjective']")
                .resolve();
    }

    public Locator create() {
        return new ResilientLocator(page, "Create")
                .byText("Create")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[4]/button[2]")
                .resolve();
    }

    public void selectDropDown(
            String dropDownName,
            String dropDownValue) {

        page.locator(
                        "label:text('" + dropDownName + "')"
                )
                .locator(
                        "xpath=following::div" +
                                "[contains(@class,'react-select__control')][1]"
                )
                .click();

        selectDropdownValue(dropDownValue).click();

        LoggerUtil.LOGGER.info(
                dropDownValue +
                        " Selected DropDown Value"
        );
    }

    public Locator selectDropdownValue(String dropDown) {

        return page.locator(
                "//div[text()='" + dropDown + "']"
        );
    }



}
