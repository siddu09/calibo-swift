package pages.DPS.CrawlerCatalog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.CommonMethods;

public class DataCatalogPage {

    public Page page = null;

    public DataCatalogPage(Page page) {
        this.page = page;
    }

    public Locator dataCatalogHeading() {
        return new ResilientLocator(page, "Data Catalog Heading")
                .byXPath("//button[@role='tab' and contains(.,'Data Ingestion Catalogs')]")
                .resolve();
    }

    public void waitForDataCatalogPageToBeLoaded() {
        page.waitForCondition(() -> "true".equals(dataCatalogHeading().getAttribute("aria-selected")));
    }

    public Locator buttonCatalogNameInTable(String catalogName) {
        return new ResilientLocator(page, "Catalog Name In Table")
                .byXPath("//div[text()='" + catalogName + "']")
                .resolve();
    }

    public Locator buttonDataPipelineStudio() {
        return CommonMethods.clickButton(page, "Data Pipeline Studio");
    }
}
