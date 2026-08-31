package UI.DPS.Flows;

import UI.DPS.BuildingBlocks.DataIngestionBuildingBlocks;
import UI.DPS.Constants.Constants;
import UI.DPS.dataHelper.DataIngestionData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.dps.DpsExecutionResultWriter;
import testdatamanager.pro.ProExecutionData;
import utils.JSONUtils.JsonDataReader;

public class DataIngestionFlows {

    private static final String DPS_EXCEL_SHEET = "DPS Data";
//    private static final String DPS_TEST_CASE = "DPS Ingestion Pipeline MSSQL to Snowflake";
    private static final String DPS_TEST_CASE = "Calibo Accelerate E2E";

    private final DataIngestionBuildingBlocks dataIngestionBuildingBlocks;
    private final DataIngestionData data;

    public DataIngestionFlows(Page page) {
        this.data = JsonDataReader.read(
                "testdata/files/DPS/ingestion_pipeline.json",
                "buildMSSQLToSnowflakeIngestionPipeline",
                DataIngestionData.class
        );
        this.dataIngestionBuildingBlocks = new DataIngestionBuildingBlocks(page, data);
    }
    
    @Step("Add data source stage")
    public void addDataSourceStage() {
        dataIngestionBuildingBlocks.addSourceStage(data);
    }

    @Step("Add data source node")
    public void addDataSourceNode() {
        dataIngestionBuildingBlocks.addSourceNode(data);
    }

    @Step("Add data ingestion stage")
    public void addDataIngestionStage() {
        dataIngestionBuildingBlocks.addIngestionStage(data);
    }

    @Step("Add data ingestion node")
    public void addDataIngestionNode() {
        dataIngestionBuildingBlocks.addIngestionNode(data);
    }

    @Step("Add data lake stage")
    public void addDataLakeStage() {
        dataIngestionBuildingBlocks.addLakeStage(data);
    }

    @Step("Add data lake node")
    public void addDataLakeNode() {
        dataIngestionBuildingBlocks.addLakeNode(data);
    }
    

    @Step("Connect ingestion pipeline nodes")
    public void connectPipelineNodes() {
        dataIngestionBuildingBlocks.connectIngestionNodes(data);
    }

    @Step("Configure MSSQL node with catalog: {catalogName}")
    public void configureMSSQL(String catalogName) {
        dataIngestionBuildingBlocks.configureMSSQLNode(data, catalogName);
    }

    @Step("Configure Snowflake node")
    public void configureSnowflake() {
        dataIngestionBuildingBlocks.configureSnowflakeNode(data);
    }

    @Step("Configure Databricks node")
    public void configureDatabricks() {
        dataIngestionBuildingBlocks.configureDatabricksNode(data);
    }

    @Step("Configure MSSQL, Snowflake, and Databricks nodes")
    public void configureMSSQLToSnowflakeIngestionNodes(String catalogName) {
        configureMSSQL(catalogName);
        configureSnowflake();
        configureDatabricks();
    }

    @Step("Publish ingestion pipeline")
    public void publishIngestionPipeline() {
        dataIngestionBuildingBlocks.publishIngestionPipeline();
    }

    @Step("Run ingestion pipeline")
    public void runIngestionPipeline() {
        dataIngestionBuildingBlocks.runIngestionPipeline();
    }

    @Step("Publish and run ingestion pipeline")
    public void publishAndRunIngestionPipeline() {
        publishIngestionPipeline();
        runIngestionPipeline();
    }

    @Step("Browse Snowflake data and validate against Excel")
    public void browseAndValidateIngestionData() {
        dataIngestionBuildingBlocks.browseAndValidateIngestionData(
                data.getTargetTableName(),
                Constants.ORDERS_EXCEL,
                Constants.ORDERS_SHEET,
                Constants.ORDERS_COLUMNS
        );
    }

    public String getTargetTableName() {
        String generatedTarget = dataIngestionBuildingBlocks.getDynamicTargetTableName();
        return (generatedTarget != null) ? generatedTarget : data.getTargetTableName();
    }

    public DataIngestionData getIngestionData() {
        return data;
    }

    public String getDataIntegrationJobName() {
        return dataIngestionBuildingBlocks.getDataIntegrationJobName();
    }


    @Step("Write DPS execution data to Excel")
    public void writeExecutionDataToExcel(
            String outputFilePath,
            ProExecutionData executionData,
            CrawlerFlows crawlerFlows,
            String catalogName,
            String testCaseName) {

        DpsExecutionResultWriter.write(
                outputFilePath,
                DPS_EXCEL_SHEET,
                testCaseName,
                executionData,
                crawlerFlows,
                this,
                catalogName
        );
    }
}

