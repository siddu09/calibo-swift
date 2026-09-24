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

    @Step("Create Private Product with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithMandatoryFields");
        productFlows.createPrivateProductWithMandatoryFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithMandatoryFields", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }

    @Step("Create Public Product with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPublicProductWithAllFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPublicProductWithAllFields");
        productFlows.createPublicProductWithAllFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPublicProductWithAllFields", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }

    @Step("Create Private Product with Define Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDefinePhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDefinePhase");
        productFlows.createPrivateProductWithDefinePhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDefinePhase", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }

    @Step("Create Private Product with Design Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDesignPhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDesignPhase");
        productFlows.createPrivateProductWithDesignPhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDesignPhase", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }

    @Step("Create Private Product with Define and Design Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDefineAndDesignPhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDefineAndDesignPhase");
        productFlows.createPrivateProductWithDefineAndDesignPhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDefineAndDesignPhase", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }

    @Step("Create Private Product with Develop and Design Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDevelopAndDesignPhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDevelopAndDesignPhase");
        productFlows.createPrivateProductWithDevelopAndDesignPhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDevelopAndDesignPhase", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }
    @Step("Create Private Product and Add Dependency Between Products")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void CreatePrivateProductAndAddDependencyBetweenProducts() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting CreatePrivateProductAndAddDependencyBetweenProducts");
        productFlows.createPrivateProductAndAddDependencyBetweenProducts();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "CreatePrivateProductAndAddDependencyBetweenProducts", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
    }
}
