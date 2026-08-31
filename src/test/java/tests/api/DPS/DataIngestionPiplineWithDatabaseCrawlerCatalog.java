package tests.api.DPS;

import api.DPS.building_blocks.Catalog.Catalog;
import api.DPS.building_blocks.Crawler.Crawler;
import api.DPS.building_blocks.DataPipeline.DataIntegration;
import api.DPS.building_blocks.DataPipeline.DataLake;
import api.DPS.building_blocks.DataPipeline.DataSource;
import api.DPS.building_blocks.DataPipeline.Workflow;
import api.DPS.helpers.DataIntegrationHelper.DataIntegrationHelper;
import api.DPS.helpers.DataLakeHelper.DataLakeHelper;
import api.DPS.helpers.DataSourceHelper.DataSourceHelper;
import api.DPS.helpers.DpsContext;
import base.apibase.BaseTest;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

public class DataIngestionPiplineWithDatabaseCrawlerCatalog extends BaseTest {
        @Test(groups = {"DataIngestionPipelineDatabaseCrawlerCatalog", "DataIngestion","DPS" })
        @Step("Creating Data Ingestion Pipeline with Database Crawler and Catalog")
        public void mssqlCatalogDatabricksSnowflakePipeline() {
            Workflow workflow = new Workflow();
            DpsContext context = workflow.setup();
            Crawler crawler = new Crawler(context);
            crawler.createDatabaseCrawler();
            crawler.waitForCrawlerRunToComplete();
            crawler.verifyCrawlerResult();
            Catalog catalog = new Catalog(crawler);
            catalog.createCatalog();
            catalog.verifyCatalogDetails();
            new DataSource(new DataSourceHelper(crawler)).addDataSourceStageWithMsSqlNode();
            DataIntegration integration = new DataIntegration(new DataIntegrationHelper(crawler));
            integration.addDataIntegrationStageWithDatabricksNode();
            new DataLake(new DataLakeHelper(crawler)).addDataLakeStageWithSnowflakeNode();
            integration.configureDatabricksJob();
            workflow.runAndVerify();
        }
    }

