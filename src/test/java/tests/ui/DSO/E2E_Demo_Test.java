package tests.ui.DSO;

import UI.DSO.Flows.DSOFlows;
import UI.DSO.buildingblocks.runManagerDataHandler;
import UI.DSO.helpers.DSOConstants;
import UI.DSO.helpers.DSOHelper;
import UI.E2E.LoginBuildingBlock;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import tests.base.BaseUITest;
import tests.dataProviders.DSO.DSO_DataProvider;
import tests.dataProviders.DSO.DSO_PropertiesLoader;

import java.util.Locale;
import java.util.Map;
@Test(groups = {"DSO-UI"})
public class E2E_Demo_Test extends BaseUITest {

    private static final String CLOUD = System.getProperty("cloud", "aws").toLowerCase(Locale.ROOT);

    private static final String FILE_PATH = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_file");
    private static final String SHEET_NAME = DSO_PropertiesLoader.getProperty(CLOUD, CLOUD + "_sheet");


    private ProExecutionData executionData;
    private DSOFlows dsoFlows;

    private ProductPortfolioFlows portfolioFlows;
    private ProductFlows productFlows;
    private FeatureFlows featureFlows;
    private runManagerDataHandler dataBuilder;

    @BeforeMethod
    public void setUp() throws Exception {
        executionData = new ProExecutionData();
        dataBuilder = new runManagerDataHandler();
        dsoFlows = new DSOFlows(page, CLOUD);
        portfolioFlows = new ProductPortfolioFlows(page, executionData);
        productFlows = new ProductFlows(page, executionData);
        featureFlows = new FeatureFlows(page, executionData);
    }

    @DataProvider(name = "DSO_AWS_RunManager")
    public Object[][] getData() {
        return DSO_DataProvider.getExcelData(FILE_PATH, SHEET_NAME);
    }
    @Test(dataProvider = "DSO_AWS_RunManager")
    public void E2E_Demo_Test(Map<String, String> testData){
        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);

        loginBuildingBlock.login();
        portfolioFlows.createPortfolioWithMandatoryFields();
        productFlows.createProductWithMandatoryFields();
        featureFlows.createFeatureWithMandatoryFieldsAndAddDevelop();
        DSOConstants data = dataBuilder.build(testData);

        dsoFlows.runDSO_E2E_Flow(data);
    }
}
