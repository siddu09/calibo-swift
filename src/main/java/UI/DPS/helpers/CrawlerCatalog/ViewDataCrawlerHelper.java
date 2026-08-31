package UI.DPS.helpers.CrawlerCatalog;

import com.microsoft.playwright.Page;
import org.testng.Assert;
import pages.DPS.CrawlerCatalog.Models.TableMetadataForCrawlerCatalog;
import pages.DPS.CrawlerCatalog.ViewDataCrawlerPage;
import pages.DPS.Utilities.PageUtility;

public class ViewDataCrawlerHelper {

    ViewDataCrawlerPage viewDataCrawlerPage = null;

    public ViewDataCrawlerHelper(Page page) {
        this.viewDataCrawlerPage = new ViewDataCrawlerPage(page);
    }

    public void waitForDataCrawlerViewPageLoaded(String crawlerName) {
        viewDataCrawlerPage.waitForViewDataCrawlerPageToBeLoaded(crawlerName);
    }

    public void waitForDataCrawlerStatus(String status) {
        viewDataCrawlerPage.page.waitForCondition(() -> viewDataCrawlerPage.labelCrawlerRunStatus()
                .innerText().equalsIgnoreCase(status));
    }

    public void verifyCrawlerCrawledTableMetadata(TableMetadataForCrawlerCatalog expectedMetadata) {
        Assert.assertEquals(PageUtility.getCrawledTablesMetadata(viewDataCrawlerPage), expectedMetadata);
    }

    public void createCatalog(String catalogName) {
        viewDataCrawlerPage.textboxSelectOrCreateCatalog().fill(catalogName);
        viewDataCrawlerPage.labelCreateCatalog(catalogName).click();
        viewDataCrawlerPage.page.waitForCondition(()
                -> viewDataCrawlerPage.iconRemoveSelectedOrCreatedCatalogName(catalogName).isVisible());
        viewDataCrawlerPage.buttonAdd().click();
    }
}
