package tests.ui.DPS;

import UI.DPS.Constants.Constants;
import UI.DPS.helpers.CrawlerCatalog.DataCatalogPageHelper;
import UI.DPS.helpers.CrawlerCatalog.DataCrawlerConfigurationsHelper;
import UI.DPS.helpers.CrawlerCatalog.DataCrawlerPageHelper;
import UI.DPS.helpers.CrawlerCatalog.ViewDataCrawlerHelper;
import UI.DPS.helpers.DataPipeline.DataIntegrationNodeConfiguration.DatabricksNodeConfigurationHelper;
import UI.DPS.helpers.DataPipeline.DataLakeNodeConfiguration.SnowflakeNodeConfigurationHelper;
import UI.DPS.helpers.DataPipeline.DataPipelineBuilder;
import UI.DPS.helpers.DataPipeline.DataSourceNodeConfiguration.DatabaseNodeConfigurationHelper;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import configHandler.ConfigManager;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.LoginPage;
import testdatamanager.pro.ProExecutionData;
import tests.base.BaseUITest;
import utils.CommonMethods;
import utils.TableValidationUtil;

public class E2EFirstFlow extends BaseUITest {

    String username = ConfigManager.getUIProperty("user.DPS");
    String password = ConfigManager.getUIProperty("pass.DPS");
    String tenantName = ConfigManager.getUIProperty("tenantName.DPS");

    @Test()
    public void e2eFirstFlow() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(username, password, tenantName);

        LandingPage landingPage = new LandingPage(page);
        CommonMethods.waitForLoaderToDisappear(page);

        ProExecutionData executionData = new ProExecutionData();


        ProductPortfolioFlows  portfolioFlows = new ProductPortfolioFlows(page, executionData);

        ProductFlows productFlows = new ProductFlows(page, executionData);

        FeatureFlows featureFlows = new FeatureFlows(page, executionData);


        // 1. Portfolio
        portfolioFlows.createPortfolioWithMandatoryFields();

        // 2. Portfolio -> Products
        portfolioFlows.navigateToProductsTab();

        // 3. Add first Product to Portfolio
        portfolioFlows.addProductToPortfolio();

        // 4. Create first Product
        productFlows.createProductWithMandatoryFields();

        // 5. Create first Feature
        featureFlows.createFeatureWithMandatoryFields();

        // 6. View Feature Details and Select Stage
        featureFlows.viewFeatureDetails();
        featureFlows.selectStage("Develop");

        CommonMethods.clickButton(page, "Data Pipeline Studio").click();

        page.waitForTimeout(5000);

        DataPipelineBuilder dataPipelineBuilder = new DataPipelineBuilder(page);
        dataPipelineBuilder.waitForDataPipelinePageLoaded();

        //Crawler Creation
        dataPipelineBuilder.gotoCrawlerPage();

        DataCrawlerPageHelper dataCrawlerPageHelper = new DataCrawlerPageHelper(page);
        dataCrawlerPageHelper.waitForDataCrawlerPageLoaded();
        dataCrawlerPageHelper.clickNewCrawlerButton();

        DataCrawlerConfigurationsHelper dataCrawlerConfigurationsHelper = new DataCrawlerConfigurationsHelper(page);
        String crawlerName = "AutoCrawlerOne"+System.currentTimeMillis();
        dataCrawlerConfigurationsHelper.createDatabaseCrawlerWithConfiguredDataStore(crawlerName,
                "Microsoft SQL Server", "MSSQL_Automation", "dbo.OrdersR48");

        //View Crawler
        ViewDataCrawlerHelper viewDataCrawlerHelper = new ViewDataCrawlerHelper(page);
        viewDataCrawlerHelper.waitForDataCrawlerViewPageLoaded(crawlerName);
        viewDataCrawlerHelper.waitForDataCrawlerStatus("SUCCESS");

        //ToDo
        //viewDataCrawlerHelper.verifyCrawlerCrawledTableMetadata();

        //Create Catalog
        String catalogName = "AutoCatalogOne"+System.currentTimeMillis();
        viewDataCrawlerHelper.createCatalog(catalogName);

        //Verify Catalog
        DataCatalogPageHelper dataCatalogPageHelper = new DataCatalogPageHelper(page);
        dataCatalogPageHelper.waitForDataCatalogPageLoaded();
        dataCatalogPageHelper.verifyCatalogNamePresentInCatalogPage(catalogName);
        dataCatalogPageHelper.goToDataPipelineStudioPage();

        //Building Ingestion Data Pipeline

        dataPipelineBuilder.waitForDataPipelinePageLoaded();
        //dataPipelineBuilder.editDataPipeline();

        //Add Data Source Stage with Microsoft SQL Server as node.
        dataPipelineBuilder.addStage("Data Sources", true, "");
        dataPipelineBuilder.addNode("Data Sources", "Microsoft SQL Server");

        //Add Data Integration Stage with Databricks as node.
        dataPipelineBuilder.addStage("Data Integration", false, "Data Sources");
        dataPipelineBuilder.addNode("Data Integration", "Databricks");
        dataPipelineBuilder.addDatabricksNodeDetails("Databricks", "Databricks_Qa_Cluster");

        //Add Data Lake Stage with Snowflake as node.
        dataPipelineBuilder.addStage("Data Lake", false, "Data Integration");
        dataPipelineBuilder.addNode("Data Lake", "Snowflake");
        dataPipelineBuilder.addSnowflakeNodeDetails("Snowflake");

        //Connect the pipeline node
        dataPipelineBuilder.connectNodes("Microsoft SQL Server", "Databricks");
        dataPipelineBuilder.connectNodes("Databricks", "Snowflake");

        //Configure the MsSQL node
        dataPipelineBuilder.clickOnNodeInPipeline("Microsoft SQL Server");

        DatabaseNodeConfigurationHelper databaseNodeConfigurationHelper = new DatabaseNodeConfigurationHelper(page);
        databaseNodeConfigurationHelper.waitForDatabaseNodeConfigurationPageLoaded("Microsoft SQL Server");
        databaseNodeConfigurationHelper.configureDataBaseNodeWithDataIngestionCatalog(catalogName, "dbo");

        //Configure Snowflake node
        dataPipelineBuilder.clickOnNodeInPipeline("Snowflake");

        SnowflakeNodeConfigurationHelper snowflakeNodeConfigurationHelper = new SnowflakeNodeConfigurationHelper(page);
        snowflakeNodeConfigurationHelper.waitForSnowflakeNodeConfigurationPageLoaded();
        snowflakeNodeConfigurationHelper.configureSnowflakeNodeWithConfiguredDatastore("Snowflake_Automation");

        //Configure Databricks node
        dataPipelineBuilder.clickOnNodeInPipeline("Databricks");

        CommonMethods.clickButton(page, "Create Templatized Job").click();
        DatabricksNodeConfigurationHelper databricksNodeConfigurationHelper = new DatabricksNodeConfigurationHelper(page);
        databricksNodeConfigurationHelper.configureDatabricksJobName();
        databricksNodeConfigurationHelper.configureSource();
        databricksNodeConfigurationHelper.configureTarget();
        databricksNodeConfigurationHelper.configureDataManagement("OrdersR48", "OrdersTable1408");
        databricksNodeConfigurationHelper.configureSchemaMapping("OrdersR48", "OrdersTable1408");
        databricksNodeConfigurationHelper.addClusterConfig();
        databricksNodeConfigurationHelper.configureNotifications();
        page.waitForTimeout(15000);
        dataPipelineBuilder.publishDataPipeline();
        dataPipelineBuilder.publishDataPipelineConfirmation();
        dataPipelineBuilder.runDataPipeline();
        dataPipelineBuilder.waitForPipelineSuccess();



        //Pipeline published successfully.
        dataPipelineBuilder.clickOnNodeInPipeline("Snowflake");
        page.waitForTimeout(2000);
        snowflakeNodeConfigurationHelper.navigateDataBrowsing("OrdersTable1408");
        snowflakeNodeConfigurationHelper.selectItemsPerPage(50);

        TableValidationUtil.validateTableAgainstExcel(
                page,
                Constants.ORDERS_EXCEL,
                Constants.ORDERS_SHEET,
                Constants.ORDERS_COLUMNS
        );
    }


}
