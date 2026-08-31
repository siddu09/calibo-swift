package pages.DPS.CrawlerCatalog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.CommonMethods;

public class ViewDataCrawlerPage {

    public Page page = null;

    public ViewDataCrawlerPage(Page page) {
        this.page = page;
    }

    public Locator viewDataCrawlerNameHeading(String crawlerName) {
        return new ResilientLocator(page, "View Data Crawler Name Heading")
                .byXPath("//h1[text()='" + crawlerName + "']")
                .resolve();
    }

    public void waitForViewDataCrawlerPageToBeLoaded(String crawlerName) {
        page.waitForCondition(() -> viewDataCrawlerNameHeading(crawlerName).isVisible());
    }

    public Locator labelCrawlerRunStatus() {
        return new ResilientLocator(page, "View Data Crawler Run Status")
                .byXPath("//label[text()='Crawler Run Status']//following-sibling::div")
                .resolve();
    }

    public Locator textboxSelectOrCreateCatalog() {
        return new ResilientLocator(page, "Select or Create Catalog Textbox")
                .byXPath("//h5[normalize-space()='Add to Data Ingestion Catalog']/ancestor" +
                        "::div[contains(@class,'add-catalog-title')]/following-sibling::div//input[@type='text']")
                .resolve();
    }

    public Locator labelCreateCatalog(String catalogName) {
        return new ResilientLocator(page, "Create Catalog Label")
                .byXPath("//div[contains(@id,'react-select-') and contains(.,'Create \"" + catalogName + "\"')]")
                .resolve();
    }

    public Locator iconRemoveSelectedOrCreatedCatalogName(String catalogName) {
        return new ResilientLocator(page, "Remove Selected or Created Catalog Name Icon")
                .byXPath("//div[text()='" + catalogName + "']/parent::div/following-sibling::div[contains(.,'close')]")
                .resolve();
    }

    public Locator buttonAdd() {
        return CommonMethods.clickButton(page, "Add");
    }

    public Locator labelTables() {
        return new ResilientLocator(page, "Tables Label")
                .byText("//*[local-name()='g' and @data-referencesafe='true']")
                .resolve();
    }

    public Locator labelSchemaTableName() {
        return new ResilientLocator(page, "Schema Table Name Label")
                .byText("//*[local-name()='tspan' and contains(@class,'group-name ')]")
                .resolve();
    }

    public Locator columnRows() {
        return new ResilientLocator(page, "Column Rows")
                .byText("//*[local-name()='g' and contains(@class,'nodeContainer')]")
                .resolve();
    }

    public Locator labelColumnName() {
        return new ResilientLocator(page, "Column Name Label")
                .byText("//*[local-name()='text' and contains(@class,'default-field')]//*[local-name()='tspan']")
                .resolve();
    }

    public Locator labelColumnDataType() {
        return new ResilientLocator(page, "Column Data Type Label")
                .byText("//*[local-name()='foreignObject']//*[local-name()='text']")
                .resolve();
    }
}
