package tests.ui.DSO;

import UI.DSO.Flows.DSOFlows;
import UI.DSO.buildingblocks.runManagerDataHandler;
import UI.DSO.helpers.DSOConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.base.BaseUITest;
import tests.dataProviders.DSO.DSO_PropertiesLoader;
import tests.dataProviders.DSO.DSO_DataProvider;

import java.util.Locale;
import java.util.Map;

public class DSO_GCP_E2E_Test extends BaseUITest {

    private runManagerDataHandler dataBuilder;
    private DSOFlows dsoFlows;
    private static final String CLOUD = System.getProperty("cloud", "GCP").toLowerCase(Locale.ROOT);

    private static final String FILE_PATH = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_file");
    private static final String SHEET_NAME = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_sheet");


    @BeforeMethod
    public void setUp() throws Exception {
        dataBuilder = new runManagerDataHandler();
        dsoFlows = new DSOFlows(page, CLOUD);
    }

    @DataProvider(name = "DSO_GCP_RunManager")
    public Object[][] getData() {
        return DSO_DataProvider.getExcelData(FILE_PATH, SHEET_NAME);
    }

    @Test(dataProvider = "DSO_GCP_RunManager",groups = "DSO_GCP")
    public void devSecOps_GCP_Combinations_Test(Map<String, String> testData) {
        DSOConstants data = dataBuilder.build(testData);
        dsoFlows.runGCPCombinationFlow(data);
    }

}
