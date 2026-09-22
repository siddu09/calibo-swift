package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Product.Flows.ProductFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import testdatamanager.pro.ProTestData;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;

public class ProductTest extends BaseUITest {

    private ProductFlows productFlows;
    private ProExecutionData executionData;


    @BeforeMethod
    @Step("Setup: Login and initialize Productflow")
    public void setUp() {
        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);
        loginBuildingBlock.proLogin();
        executionData = new ProExecutionData();
        productFlows = new ProductFlows(page, executionData);
        LoggerUtil.LOGGER.info("============================ Login and Setup completed =========================");
    }

    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void createPrivateProductWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithMandatoryFields");
        productFlows.createPrivateProductWithMandatoryFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithMandatoryFields", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void createPublicProductWithAllFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPublicProductWithAllFields");
        productFlows.createPublicProductWithAllFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPublicProductWithAllFields", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }
}
