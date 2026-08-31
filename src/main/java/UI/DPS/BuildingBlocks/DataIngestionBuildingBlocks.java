package UI.DPS.BuildingBlocks;

import UI.DPS.dataHelper.DataIngestionData;
import UI.DPS.helpers.DataPipeline.DataIntegrationNodeConfiguration.DatabricksNodeConfigurationHelper;
import UI.DPS.helpers.DataPipeline.DataLakeNodeConfiguration.SnowflakeNodeConfigurationHelper;
import UI.DPS.helpers.DataPipeline.DataPipelineBuilder;
import UI.DPS.helpers.DataPipeline.DataSourceNodeConfiguration.DatabaseNodeConfigurationHelper;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import pages.DPS.DataPipeline.DataLakeNodeConfiguration.SnowflakeNodeConfigurationPage;
import utils.CommonMethods;
import utils.TableValidationUtil;

import java.util.List;

public class DataIngestionBuildingBlocks {

	private final DataPipelineBuilder dataPipelineBuilder;
	private final Page page;
	private final DatabaseNodeConfigurationHelper databaseNodeConfigurationHelper;
	private final SnowflakeNodeConfigurationHelper snowflakeNodeConfigurationHelper;
	private final DatabricksNodeConfigurationHelper databricksNodeConfigurationHelper;
	private DataIngestionData data;
	private String dynamicTargetTableName;
	private String dataIntegrationJobName;
    private final SnowflakeNodeConfigurationPage snowflakeNodeConfigurationPage;

	public DataIngestionBuildingBlocks(Page page) {
		this.page = page;
		this.dataPipelineBuilder = new DataPipelineBuilder(page);
		this.databaseNodeConfigurationHelper = new DatabaseNodeConfigurationHelper(page);
		this.snowflakeNodeConfigurationHelper = new SnowflakeNodeConfigurationHelper(page);
		this.databricksNodeConfigurationHelper = new DatabricksNodeConfigurationHelper(page);
        this.snowflakeNodeConfigurationPage = new SnowflakeNodeConfigurationPage(page);
		this.data = null;
	}

	public DataIngestionBuildingBlocks(Page page, DataIngestionData data) {
		this.page = page;
		this.data = data;
		this.dataPipelineBuilder = new DataPipelineBuilder(page);
		this.databaseNodeConfigurationHelper = new DatabaseNodeConfigurationHelper(page);
		this.snowflakeNodeConfigurationHelper = new SnowflakeNodeConfigurationHelper(page);
		this.databricksNodeConfigurationHelper = new DatabricksNodeConfigurationHelper(page);
        this.snowflakeNodeConfigurationPage = new SnowflakeNodeConfigurationPage(page);
    }

	@Step("Build ingestion pipeline")
	public void buildIngestionPipeline(DataIngestionData data) {
		addSourceStage(data);
		addSourceNode(data);
		addIngestionStage(data);
		addIngestionNode(data);
		addLakeStage(data);
		addLakeNode(data);
		connectIngestionNodes(data);
	}

	@Step("Add source stage")
	public void addSourceStage(DataIngestionData data) {

		// Load the builder canvas before adding the first stage.
		dataPipelineBuilder.waitForDataPipelinePageLoaded();

		// Add Data Source stage.
		dataPipelineBuilder.addStage(
				data.getDataSourceStageName(),
				true,
				""
		);
	}

	@Step("Add source node")
	public void addSourceNode(DataIngestionData data) {
		Allure.parameter("Data Source Node", data.getDataSourceNodeName());

		// Add Data Source node.
		dataPipelineBuilder.addNode(
				data.getDataSourceStageName(),
				data.getDataSourceNodeName()
		);
	}

	@Step("Add ingestion stage")
	public void addIngestionStage(DataIngestionData data) {

		// Add Data Integration stage.
		dataPipelineBuilder.addStage(
				data.getDataIntegrationStageName(),
				false,
				data.getDataSourceStageName()
		);
	}

	@Step("Add ingestion node")
	public void addIngestionNode(DataIngestionData data) {
		Allure.parameter("Data Integration Node", data.getDataIntegrationNodeName());
		Allure.parameter("Databricks Instance Name", data.getDatabricksInstanceName());

		// Add Data Integration node.
		dataPipelineBuilder.addNode(
				data.getDataIntegrationStageName(),
				data.getDataIntegrationNodeName()
		);
		dataPipelineBuilder.addDatabricksNodeDetails(
				data.getDataIntegrationNodeName(),
				data.getDatabricksInstanceName()
		);
	}

	@Step("Add lake stage")
	public void addLakeStage(DataIngestionData data) {
		// Add Data Lake stage.
		dataPipelineBuilder.addStage(
				data.getDataLakeStageName(),
				false,
				data.getDataIntegrationStageName()
		);
	}

	@Step("Add lake node")
	public void addLakeNode(DataIngestionData data) {
		Allure.parameter("Data Lake Node", data.getDataLakeNodeName());

		// Add Data Lake node.
		dataPipelineBuilder.addNode(
				data.getDataLakeStageName(),
				data.getDataLakeNodeName()
		);
		dataPipelineBuilder.addSnowflakeNodeDetails(
				data.getDataLakeNodeName()
		);
	}

	@Step("Connect ingestion nodes")
	public void connectIngestionNodes(DataIngestionData data) {
		Allure.parameter("Connection", data.getDataSourceNodeName() + " -> " + data.getDataIntegrationNodeName());
		Allure.parameter("Connection", data.getDataIntegrationNodeName() + " -> " + data.getDataLakeNodeName());

		// Connect node sequence.
		dataPipelineBuilder.connectNodes(
				data.getDataSourceNodeName(),
				data.getDataIntegrationNodeName()
		);
		dataPipelineBuilder.connectNodes(
				data.getDataIntegrationNodeName(),
				data.getDataLakeNodeName()
		);
	}

	@Step("Configure ingestion nodes")
	public void configureIngestionNodes(
			DataIngestionData data,
			String catalogName) {

		configureMSSQLNode(data, catalogName);
		configureSnowflakeNode(data);
		configureDatabricksNode(data);
	}

	@Step("Configure MSSQL node")
	public void configureMSSQLNode(DataIngestionData data, String catalogName) {
		Allure.parameter("Catalog Name", catalogName);
		Allure.parameter("Catalog Schema", data.getCatalogSchema());

		dataPipelineBuilder.clickOnNodeInPipeline(data.getDataSourceNodeName());
		databaseNodeConfigurationHelper.waitForDatabaseNodeConfigurationPageLoaded(
				data.getDataSourceNodeName()
		);
		databaseNodeConfigurationHelper.configureDataBaseNodeWithDataIngestionCatalog(
				catalogName,
				data.getCatalogSchema()
		);
	}

	@Step("Configure Snowflake node")
	public void configureSnowflakeNode(DataIngestionData data) {
		Allure.parameter("Snowflake Datastore Name", data.getSnowflakeDatastoreName());

		dataPipelineBuilder.clickOnNodeInPipeline(data.getDataLakeNodeName());
		snowflakeNodeConfigurationHelper.waitForSnowflakeNodeConfigurationPageLoaded();
		snowflakeNodeConfigurationHelper.configureSnowflakeNodeWithConfiguredDatastore(
				data.getSnowflakeDatastoreName()
		);
	}

	@Step("Configure Databricks node")
	public void configureDatabricksNode(DataIngestionData data) {
		Allure.parameter("Source Table Name", data.getSourceTableName());

		// Generate target table once here so downstream browse/validation uses the same value.
		if (dynamicTargetTableName == null) {
			dynamicTargetTableName = generateUniqueName(data.getTargetTableName());
		}
		Allure.parameter("Dynamic Target Table Name", dynamicTargetTableName);

		dataPipelineBuilder.clickOnNodeInPipeline(data.getDataIntegrationNodeName());
		CommonMethods.clickButton(page, "Create Templatized Job").click();

		dataIntegrationJobName = databricksNodeConfigurationHelper.configureDatabricksJobName();
		Allure.parameter("Data Integration Job Name", dataIntegrationJobName);
		databricksNodeConfigurationHelper.configureSource();
		databricksNodeConfigurationHelper.configureTarget();
		databricksNodeConfigurationHelper.configureDataManagement(
				data.getSourceTableName(),
				dynamicTargetTableName
		);
		databricksNodeConfigurationHelper.configureSchemaMapping(
				data.getSourceTableName(),
				dynamicTargetTableName
		);
		databricksNodeConfigurationHelper.addClusterConfig();
		databricksNodeConfigurationHelper.configureNotifications();

		page.waitForTimeout(15000);
	}

	@Step("Publish ingestion pipeline")
	public void publishIngestionPipeline() {

		dataPipelineBuilder.publishDataPipeline();
		dataPipelineBuilder.publishDataPipelineConfirmation();
	}

	@Step("Run ingestion pipeline")
	public void runIngestionPipeline() {

		dataPipelineBuilder.runDataPipeline();
		dataPipelineBuilder.waitForPipelineSuccess();
	}

	@Step("Publish and run ingestion pipeline")
	public void publishAndRunIngestionPipeline() {

		publishIngestionPipeline();
		runIngestionPipeline();
	}

	@Step("Browse and validate ingestion data")
	public void browseAndValidateIngestionData(
			String targetTableName,
			String excelPath,
			String sheetName,
			List<String> columns) {

		// Use the dynamically generated target table name from configuration.
		String tableToValidate = (dynamicTargetTableName != null) ? dynamicTargetTableName : targetTableName;
		Allure.parameter("Table To Validate", tableToValidate);
		// Navigate to Snowflake data browsing.
		dataPipelineBuilder.clickOnNodeInPipeline(data.getDataLakeNodeName());
		page.waitForTimeout(6000);

		snowflakeNodeConfigurationHelper.navigateDataBrowsing(tableToValidate);
		snowflakeNodeConfigurationHelper.selectItemsPerPage(50);

		// Validate table against Excel.
		TableValidationUtil.validateTableAgainstExcel(
				page,
				excelPath,
				sheetName,
				columns
		);
        snowflakeNodeConfigurationPage.closeConfig().click();

	}

	private String generateUniqueName(String name) {
		return name + System.currentTimeMillis();
	}

	public String getDynamicTargetTableName() {
		return dynamicTargetTableName;
	}

	public String getDataIntegrationJobName() {
		return dataIntegrationJobName;
	}
}

