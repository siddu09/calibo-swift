package tests.ui.E2E;

import UI.DSO.Flows.DSOFlows;
import UI.DSO.buildingblocks.runManagerDataHandler;
import UI.DSO.helpers.DSOConstants;
import UI.E2E.Flows.E2EFlows;
import UI.E2E.LoginBuildingBlock;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import tests.base.BaseUITest;
import tests.dataProviders.DSO.DSO_DataProvider;
import tests.dataProviders.DSO.DSO_PropertiesLoader;
import utils.LoggerUtil;

import java.util.Locale;
import java.util.Map;

public class E2EAccelerate extends BaseUITest {



    private ProExecutionData executionData;

    private E2EFlows e2eFlows;

    private runManagerDataHandler dataBuilder;
    private DSOFlows dsoFlows;
    private static final String CLOUD = System.getProperty("cloud", "aws").toLowerCase(Locale.ROOT);

    private static final String FILE_PATH = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_file");
    private static final String SHEET_NAME = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_sheet");

    @BeforeMethod
    @Step("Setup: Initialize test environment,and Login")
    public void setUp() {

        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);

        loginBuildingBlock.login();

        executionData = new ProExecutionData();

        e2eFlows = new E2EFlows(page, executionData);

        dataBuilder = new runManagerDataHandler();
        dsoFlows = new DSOFlows(page, CLOUD);


    }

    @DataProvider(name = "DSO_AWS_RunManager")
    public Object[][] getData() {
        return DSO_DataProvider.getExcelData(FILE_PATH, SHEET_NAME);
    }
    @Test(dataProvider = "DSO_AWS_RunManager")
    @Description("Create Product, portfolio and Feature. Add Data Ingestion Pipeline with MSSQL, Snowflake, and Databricks. Run DSO flow for AWS Kubernetes combination. Validate the end-to-end flow.")
    public void AccelerateE2E(Map<String, String> testData) {
        {

            LoggerUtil.LOGGER.info("=================================================");

            LoggerUtil.LOGGER.info("              E2E VALIDATION");

            LoggerUtil.LOGGER.info("=================================================");

            e2eFlows.createProductPortfolioAndFeature();

            e2eFlows.createDataIngestionPipelineWithMSSQLDatabricksSnowflake();

            //DSO
            DSOConstants data = dataBuilder.build(testData);
            dsoFlows.runAwsCombinationFlowForE2E(data);



            LoggerUtil.LOGGER.info("=================================================");

            LoggerUtil.LOGGER.info("          E2E VALIDATION COMPLETED");

            LoggerUtil.LOGGER.info("=================================================");
        }
    }
}