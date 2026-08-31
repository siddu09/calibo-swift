package UI.E2E.Flows;

import UI.DPS.Flows.CrawlerFlows;
import UI.DPS.Flows.DataIngestionFlows;
import UI.PRO.CommonProValidations.ProValidation;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.Product.validations.ProductValidation;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import UI.PRO.datahelper.ProductData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import testdatamanager.pro.ProTestData;
import utils.CommonMethods;

public class E2EFlows {

    private static final String OUTPUT_FILE_PATH = "src/test/resources/output/e2e/E2EExecutionData.xlsx";
    private static final String TESTCASE_NAME = "Calibo Accelerate E2E";

    private final ProductFlows productFlows;
    private final FeatureFlows featureFlows;
    private final ProExecutionData executionData;
    private final DataIngestionFlows dataIngestionFlows;
    private final CrawlerFlows crawlerFlows;
    private final Page page;
    private final ProductPortfolioFlows portfolioFlows;

    public E2EFlows(Page page, ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        productFlows = new ProductFlows(page, executionData);
        portfolioFlows = new ProductPortfolioFlows(page, executionData);
        featureFlows = new FeatureFlows(page, executionData);
        dataIngestionFlows = new DataIngestionFlows(page);
        crawlerFlows = new CrawlerFlows(page);
    }

    @Step("Create Portfolio, Product and Feature")
    public void createProductPortfolioAndFeature() {
        portfolioFlows.createPortfolioWithMandatoryFields();

        productFlows.createProductWithMandatoryFields();

        featureFlows.createFeatureWithMandatoryFieldsAndAddDevelop();

        ProExecutionResultWriter.write(OUTPUT_FILE_PATH, "ProE2E", TESTCASE_NAME, executionData);
    }

    @Step("Create Data Ingestion Pipeline with MSSQL, Snowflake, and Databricks")
    public void createDataIngestionPipelineWithMSSQLDatabricksSnowflake() {
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

        dataIngestionFlows.writeExecutionDataToExcel(OUTPUT_FILE_PATH, executionData, crawlerFlows, catalogName, TESTCASE_NAME);

        CommonMethods.clickButton(page, "Develop").click();

    }

//    @Step("Create Data Ingestion Pipeline with MSSQL, Snowflake, and Databricks")
//    public void createDataIngestionPipelineWithMSSQLDatabricksSnowflake() {
//
//    }

}