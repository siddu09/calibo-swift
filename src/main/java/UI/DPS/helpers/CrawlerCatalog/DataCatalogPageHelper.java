package UI.DPS.helpers.CrawlerCatalog;

import com.microsoft.playwright.Page;
import pages.DPS.CrawlerCatalog.DataCatalogPage;

public class DataCatalogPageHelper {

    DataCatalogPage dataCatalogPage = null;

    public DataCatalogPageHelper(Page page) {
        this.dataCatalogPage = new DataCatalogPage(page);
    }

    public void waitForDataCatalogPageLoaded() {
        dataCatalogPage.waitForDataCatalogPageToBeLoaded();
    }

    public void verifyCatalogNamePresentInCatalogPage(String catalogName) {
        dataCatalogPage.buttonCatalogNameInTable(catalogName).isVisible();
    }

    public void goToDataPipelineStudioPage() {
        dataCatalogPage.buttonDataPipelineStudio().click();
    }
}