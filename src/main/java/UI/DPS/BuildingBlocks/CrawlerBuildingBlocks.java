package UI.DPS.BuildingBlocks;

import UI.DPS.helpers.CrawlerCatalog.DataCatalogPageHelper;
import UI.DPS.helpers.CrawlerCatalog.DataCrawlerConfigurationsHelper;
import UI.DPS.helpers.CrawlerCatalog.DataCrawlerPageHelper;
import UI.DPS.helpers.CrawlerCatalog.ViewDataCrawlerHelper;
import UI.DPS.helpers.DataPipeline.DataPipelineBuilder;
import UI.DPS.dataHelper.CrawlerData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.CommonMethods;

public class CrawlerBuildingBlocks {

    private final Page page;
    private final DataPipelineBuilder dataPipelineBuilder;
    private final DataCrawlerPageHelper dataCrawlerPageHelper;
    private final DataCrawlerConfigurationsHelper dataCrawlerConfigurationsHelper;
    private final ViewDataCrawlerHelper viewDataCrawlerHelper;
    private final DataCatalogPageHelper dataCatalogPageHelper;

    public CrawlerBuildingBlocks(Page page) {
        this.page = page;
        this.dataPipelineBuilder = new DataPipelineBuilder(page);
        this.dataCrawlerPageHelper = new DataCrawlerPageHelper(page);
        this.dataCrawlerConfigurationsHelper =
                new DataCrawlerConfigurationsHelper(page);
        this.viewDataCrawlerHelper = new ViewDataCrawlerHelper(page);
        this.dataCatalogPageHelper = new DataCatalogPageHelper(page);
    }

    @Step("Navigate to Data Pipeline Studio")
    public void navigateToDataPipeline() {
        CommonMethods.clickButton(page, "Data Pipeline Studio").click();
        page.waitForTimeout(5000);

        dataPipelineBuilder.waitForDataPipelinePageLoaded();
    }

    @Step("Create crawler")
    public String createCrawler(CrawlerData data) {
        Allure.parameter("Crawler Source", data.getSourceName());
        Allure.parameter("Crawler Datastore Name", data.getDatastoreName());
        Allure.parameter("Crawler Table Name", data.getTableName());

        dataPipelineBuilder.gotoCrawlerPage();

        dataCrawlerPageHelper.waitForDataCrawlerPageLoaded();
        dataCrawlerPageHelper.clickNewCrawlerButton();

        String crawlerName = generateUniqueName(
                data.getCrawlerName()
        );
        Allure.parameter("Crawler Name", crawlerName);

        dataCrawlerConfigurationsHelper.createDatabaseCrawlerWithConfiguredDataStore(
                crawlerName,
                data.getSourceName(),
                data.getDatastoreName(),
                data.getTableName()
        );

        return crawlerName;
    }

    @Step("Verify crawler")
    public void verifyCrawler(String crawlerName) {
        Allure.parameter("Crawler Name", crawlerName);

        viewDataCrawlerHelper.waitForDataCrawlerViewPageLoaded(
                crawlerName
        );

        viewDataCrawlerHelper.waitForDataCrawlerStatus(
                "SUCCESS"
        );

        // TODO:
        // viewDataCrawlerHelper.verifyCrawlerCrawledTableMetadata();
    }

    @Step("Create catalog")
    public String createCatalog(CrawlerData data) {

        String catalogName = generateUniqueName(
                data.getDataCatalogName()
        );
        Allure.parameter("Catalog Name", catalogName);

        viewDataCrawlerHelper.createCatalog(catalogName);

        return catalogName;
    }

    @Step("Verify catalog")
    public void verifyCatalog(String catalogName) {
        Allure.parameter("Catalog Name", catalogName);

        dataCatalogPageHelper.waitForDataCatalogPageLoaded();

        dataCatalogPageHelper.verifyCatalogNamePresentInCatalogPage(
                catalogName
        );
    }

    @Step("Go to Data Pipeline Studio page")
    public void goToDataPipelineStudio() {
        dataCatalogPageHelper.goToDataPipelineStudioPage();
    }

    private String generateUniqueName(String name) {
        return name + System.currentTimeMillis();
    }
}