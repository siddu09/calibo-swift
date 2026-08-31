package tests.ui.DSO;

import UI.DSO.Flows.DSOFlows;
import UI.DSO.buildingblocks.runManagerDataHandler;
import UI.DSO.helpers.DSOConstants;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.base.BaseUITest;
import tests.dataProviders.DSO.DSO_DataProvider;
import tests.dataProviders.DSO.DSO_PropertiesLoader;

import java.util.Locale;
import java.util.Map;


public class DSO_AWS_E2E_Test extends BaseUITest {

    private runManagerDataHandler dataBuilder;
    private DSOFlows dsoFlows;
    private static final String CLOUD = System.getProperty("cloud", "aws").toLowerCase(Locale.ROOT);

    private static final String FILE_PATH = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_file");
    private static final String SHEET_NAME = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_sheet");

    @Step("Setting up test environment for DSO AWS E2E Test")
    @BeforeMethod
    public void setUp() throws Exception {
        dataBuilder = new runManagerDataHandler();
        dsoFlows = new DSOFlows(page, CLOUD);
    }

    @DataProvider(name = "DSO_AWS_RunManager")
    public Object[][] getData() {
        return DSO_DataProvider.getExcelData(FILE_PATH, SHEET_NAME);
    }
    @Step("Executing DevSecOps AWS Combinations Test with provided test data")
    @Test(dataProvider = "DSO_AWS_RunManager", groups = "DSO_AWS")
    public void devSecOps_AWS_Combinations_Test(Map<String, String> testData) {
        DSOConstants data = dataBuilder.build(testData);
        dsoFlows.runAwsCombinationFlow(data);
    }

}
