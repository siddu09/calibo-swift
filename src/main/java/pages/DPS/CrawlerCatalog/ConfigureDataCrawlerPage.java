package pages.DPS.CrawlerCatalog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.CommonMethods;

public class ConfigureDataCrawlerPage {

    public Page page = null;

    public ConfigureDataCrawlerPage(Page page) {
        this.page = page;
    }

    public Locator configureDataCrawlerHeading() {
        return new ResilientLocator(page, "Configure Data Crawler Heading")
                .byText("Configure Data Crawler")
                .resolve();
    }

    public void waitForConfigureDataCrawlerPageToBeLoaded() {
        page.waitForCondition(() -> configureDataCrawlerHeading().isVisible());
    }

    public Locator textBoxCrawlerName() {
        return new ResilientLocator(page, "Crawler Name Textbox")
                .byXPath("//input[@name='configurationName']")
                .resolve();
    }

    public Locator buttonNext() {
        return CommonMethods.clickButton(page, "Next");
    }

    public Locator dropdownSource() {
        return new ResilientLocator(page, "Select Source Dropdown")
                .byXPath("//div[text()='Choose Data Source Type']")
                .resolve();
    }

    public Locator sourceNameInDropdown(String sourceName) {
        return new ResilientLocator(page, "Source Name In Dropdown")
                .byXPath("//label[text()='" + sourceName + "']")
                .resolve();
    }

    public void selectSourceFromDropdown(String sourceName) {
        dropdownSource().click();
        page.waitForCondition(() -> sourceNameInDropdown(sourceName).isVisible());
        sourceNameInDropdown(sourceName).click();
    }

    public Locator dropdownDatastore() {
        return new ResilientLocator(page, "Select Datastore Dropdown")
                .byId("multiselectContainerReact")
                .resolve();
    }

    public Locator datastoreNameInDropdown(String datastoreName) {
        return new ResilientLocator(page, "Datastore Name In Dropdown")
                .byText(datastoreName)
                .resolve();
    }

    public void selectDatastoreFromDropdown(String datastoreName) {
        dropdownDatastore().click();
        page.waitForCondition(() -> datastoreNameInDropdown(datastoreName).isVisible());
        datastoreNameInDropdown(datastoreName).click();
    }

    public Locator dropdownSelectTables() {
        return new ResilientLocator(page, "Select Tables Dropdown")
                .byText("Select Tables")
                .resolve();
    }

    public Locator checkboxTableNameInDropdown(String tableName) {
        return new ResilientLocator(page, "Table Name In Dropdown")
                .byXPath("//label[text()='" + tableName + "']/preceding-sibling::input")
                .resolve();
    }

    public void selectTableFromDropdown(String tableNamesCommaSeparated) {
        dropdownSelectTables().click();
        String[] tablesNamesArray = tableNamesCommaSeparated.split(",");
        for (String table : tablesNamesArray) {
            page.waitForCondition(() -> checkboxTableNameInDropdown(table).isVisible());
            checkboxTableNameInDropdown(table).click();
        }
        page.mouse().click(100, 100);
    }

    public Locator buttonSaveAndCrawl() {
        return CommonMethods.clickButton(page, "Save and Crawl");
    }
}
