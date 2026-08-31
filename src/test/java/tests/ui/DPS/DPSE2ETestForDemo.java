package tests.ui.DPS;

import UI.DPS.Flows.CrawlerFlows;
import UI.DPS.Flows.DataIngestionFlows;
import UI.E2E.LoginBuildingBlock;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import tests.base.BaseUITest;
import utils.LoggerUtil;

public class DPSE2ETestForDemo extends BaseUITest {

    private static final String OUTPUT_FILE_PATH = "src/test/resources/output/dps/DPSExecutionData.xlsx";
    private ProExecutionData executionData;

    private ProductPortfolioFlows portfolioFlows;
    private ProductFlows productFlows;
    private FeatureFlows featureFlows;
    private CrawlerFlows crawlerFlows;

    private DataIngestionFlows dataIngestionFlows;

    @BeforeMethod
    @Step("Setup: Initialize test environment, Login and create portfolio, product, and feature")
    public void setUp() {

        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);

        loginBuildingBlock.login();

        executionData = new ProExecutionData();

        portfolioFlows = new ProductPortfolioFlows(page, executionData);

        productFlows = new ProductFlows(page, executionData);

        featureFlows = new FeatureFlows(page, executionData);

        crawlerFlows = new CrawlerFlows(page);

        dataIngestionFlows = new DataIngestionFlows(page);

        portfolioFlows.createPortfolioWithMandatoryFields();

        productFlows.createProductWithMandatoryFields();

        featureFlows.createFeatureWithMandatoryFieldsAndAddDevelop();

    }

    @Test()
    @Description("Create Data Ingestion Pipeline with MSSQL, Snowflake, and Databricks")
    public void DPSIngestionPipelineMSSQLDatabricksSnowflake() {

        LoggerUtil.LOGGER.info("=================================================");

        LoggerUtil.LOGGER.info("              DPS E2E VALIDATION");

        LoggerUtil.LOGGER.info("=================================================");

        String catalogName = crawlerFlows.createMSSQLCrawlerWithDataIngestionCatalog();

        dataIngestionFlows.addDataSourceStage();

        dataIngestionFlows.addDataSourceNode();

        dataIngestionFlows.addDataIngestionStage();

        dataIngestionFlows.addDataIngestionNode();

        dataIngestionFlows.addDataLakeStage();

        dataIngestionFlows.addDataLakeNode();

        dataIngestionFlows.connectPipelineNodes();

        dataIngestionFlows.configureMSSQL(catalogName);

        dataIngestionFlows.configureSnowflake();

        dataIngestionFlows.configureDatabricks();

        dataIngestionFlows.publishIngestionPipeline();

        dataIngestionFlows.runIngestionPipeline();

        dataIngestionFlows.browseAndValidateIngestionData();

        dataIngestionFlows.writeExecutionDataToExcel(OUTPUT_FILE_PATH, executionData, crawlerFlows, catalogName, "DPSIngestionPipelineMSSQLDatabricksSnowflake");


        LoggerUtil.LOGGER.info("=================================================");

        LoggerUtil.LOGGER.info("          DPS E2E VALIDATION COMPLETED");

        LoggerUtil.LOGGER.info("=================================================");
    }
}