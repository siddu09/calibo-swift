package pages.DPS.CrawlerCatalog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class DataCrawlerPage {

    Page page = null;

    public DataCrawlerPage(Page page) {
        this.page = page;
    }

    public Locator dataCrawlerHeading() {
        return new ResilientLocator(page, "Data Crawler Heading")
                .byXPath("//button[@role='tab']/span[text()='Data Crawlers']")
                .resolve();
    }

    public void waitForDataCrawlerPageToBeLoaded() {
        page.waitForCondition(() -> dataCrawlerHeading().isVisible());
    }

    public Locator buttonNewCrawler() {
        return new ResilientLocator(page, "New Crawler")
                .byXPath("//button[@text='New Crawler']")
                .resolve();
    }
}
