package UI.DPS.helpers.CrawlerCatalog;

import com.microsoft.playwright.Page;
import pages.DPS.CrawlerCatalog.DataCrawlerPage;

public class DataCrawlerPageHelper {

    DataCrawlerPage dataCrawlerPage = null;

    public DataCrawlerPageHelper(Page page) {
        this.dataCrawlerPage = new DataCrawlerPage(page);
    }

    public void waitForDataCrawlerPageLoaded() {
        dataCrawlerPage.waitForDataCrawlerPageToBeLoaded();
    }

    public void clickNewCrawlerButton() {
        dataCrawlerPage.buttonNewCrawler().click();
    }
}