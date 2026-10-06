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

//    @Step("Create Private Product with Mandatory Fields")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createPrivateProductWithMandatoryFields() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithMandatoryFields");
//        productFlows.createPrivateProductWithMandatoryFields();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithMandatoryFields", executionData);
//        // Keep the recorded product for deleteProductWhenFeatureIsLinkedToProduct.
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//
//    }
//
//    @Step("Create Public Product with All Fields")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createPublicProductWithAllFields() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPublicProductWithAllFields");
//        productFlows.createPublicProductWithAllFields();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPublicProductWithAllFields", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//
//    }
//
//    @Step("Create Private Product with Define Phase")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createPrivateProductWithDefinePhase() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDefinePhase");
//        productFlows.createPrivateProductWithDefinePhase();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDefinePhase", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//
//    }
//
//    @Step("Create Private Product with Design Phase")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createPrivateProductWithDesignPhase() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDesignPhase");
//        productFlows.createPrivateProductWithDesignPhase();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDesignPhase", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//
//    }
//
//    @Step("Create Private Product with Define and Design Phase")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createPrivateProductWithDefineAndDesignPhase() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDefineAndDesignPhase");
//        productFlows.createPrivateProductWithDefineAndDesignPhase();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDefineAndDesignPhase", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//
//    }
//
//    @Step("Create Private Product with Develop and Design Phase")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createPrivateProductWithDevelopAndDesignPhase() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDevelopAndDesignPhase");
//        productFlows.createPrivateProductWithDevelopAndDesignPhase();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDevelopAndDesignPhase", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//
//    }
//    @Step("Create Private Product and Add Dependency Between Products")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void CreatePrivateProductAndAddDependencyBetweenProducts() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting CreatePrivateProductAndAddDependencyBetweenProducts");
//        productFlows.createPrivateProductAndAddDependencyBetweenProducts();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "CreatePrivateProductAndAddDependencyBetweenProducts", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Add Teams and Members to Product")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void AddTeamsAndMembersToProduct() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting AddTeamsAndMembersToProduct");
//        productFlows.addTeamsAndMembersToProduct();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "AddTeamsAndMembersToProduct", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Add Product to Release")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void AddProductToRelease() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting AddProductToRelease");
//        productFlows.addProductToRelease();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "AddProductToRelease", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] AddProductToRelease completed successfully for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Validate Custom Fields")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void ValidationOfCustomFields() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting ValidationOfCustomFields");
//        productFlows.validationOfCustomFields();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "ValidationOfCustomFields", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Edit Portfolio Details of Existing Product")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void EditPortfolioDetailsOfExistingProduct() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditPortfolioDetailsOfExistingProduct");
//        productFlows.editPortfolioDetailsOfExistingProduct();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditPortfolioDetailsOfExistingProduct", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Edit Product with All Fields")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void EditProductWithAlltheFields() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditProductWithAlltheFields");
//        productFlows.editProductWithAlltheFields();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditProductWithAlltheFields", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Edit Product with Add and Remove Release")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void EditProductwithAddAndRemoveRelease() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditProductwithAddAndRemoveRelease");
//        productFlows.editProductwithAddAndRemoveRelease();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditProductwithAddAndRemoveRelease", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Edit Product with updating Phases")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void EditProductWithUpdatingPhases() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditProductWithUpdatingPhases");
//        productFlows.editProductWithUpdatingPhases();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditProductWithUpdatingPhases", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Save Product without Mandatory Fields and Verify Error Messages")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
//    public void SaveProductWithoutMandatoryFieldsAndVerifyErrorMessages() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting SaveProductWithoutMandatoryFieldsAndVerifyErrorMessages");
//        productFlows.saveProductWithoutMandatoryFieldsAndVerifyErrorMessages();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "SaveProductWithoutMandatoryFieldsAndVerifyErrorMessages", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Delete Product when Feature is Linked to Product")
//    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT},dependsOnMethods = "createPrivateProductWithMandatoryFields")
//    public void deleteProductWhenFeatureIsLinkedToProduct() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting deleteProductWhenFeatureIsLinkedToProduct");
//        productFlows.deleteProductWhenFeatureIsLinkedToProduct();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "deleteProductWhenFeatureIsLinkedToProduct", executionData);
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//
//
//    @Step("Create Operationalize Private Product with Mandatory Fields")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createOperationalizePrivateProductWithMandatoryFields() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createOperationalizePrivateProductWithMandatoryFields");
//        productFlows.createOperationalizePrivateProductWithMandatoryFields();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createOperationalizePrivateProductWithMandatoryFields", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//    }
//    @Step("Create Operationalize Public Product with All Fields")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void createOperationalizePublicProductWithAllFields() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createOperationalizePublicProductWithAllFields");
//        productFlows.createOperationalizePublicProductWithAllFields();
//        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createOperationalizePublicProductWithAllFields", executionData);
//        productFlows.deleteProduct();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());
//
//    }
//    @Step("Validate Cancel Product Creation")
//    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
//    public void validateCancelProductCreation() {
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting validateCancelProductCreation");
//        productFlows.validateCancelProductCreation();
//        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed validateCancelProductCreation successfully");
//    }
    @Step("Maturity Assessment at Product Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void MaturityAssesmentAtProductLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssesmentAtProductLevel");
        productFlows.MaturityAssesmentAtProductLevel();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed MaturityAssesmentAtProductLevel successfully");
    }

}
