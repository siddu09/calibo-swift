package UI.DPS.Flows;

import UI.DPS.BuildingBlocks.CrawlerBuildingBlocks;
import UI.DPS.dataHelper.CrawlerData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import utils.JSONUtils.JsonDataReader;

@Getter
public class CrawlerFlows {
    private final Page page;
    private String lastCrawlerName;
    private String lastCatalogName;
    private CrawlerData crawlerData;


    public CrawlerFlows(Page page) {
        this.page = page;
    }

    @Step("Create MSSQL crawler with data ingestion catalog")
    public String createMSSQLCrawlerWithDataIngestionCatalog() {

        String scenarioName = "createMSSQLCrawler";

        CrawlerData data = JsonDataReader.read(
                "testdata/files/DPS/crawler.json",
                scenarioName,
                CrawlerData.class
        );
        this.crawlerData = data;

        CrawlerBuildingBlocks crawlerBuildingBlocks =
                new CrawlerBuildingBlocks(page);

        crawlerBuildingBlocks.navigateToDataPipeline();

        String crawlerName =
                crawlerBuildingBlocks.createCrawler(data);
        this.lastCrawlerName = crawlerName;

        crawlerBuildingBlocks.verifyCrawler(crawlerName);

        String catalogName =
                crawlerBuildingBlocks.createCatalog(data);
        this.lastCatalogName = catalogName;

        crawlerBuildingBlocks.verifyCatalog(catalogName);

        crawlerBuildingBlocks.goToDataPipelineStudio();

        return catalogName;
    }
}

