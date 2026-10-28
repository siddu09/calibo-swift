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
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPortfolioName());

    }

    @Step("Create Public Product with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPublicProductWithAllFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPublicProductWithAllFields");
        productFlows.createPublicProductWithAllFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPublicProductWithAllFields", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for product: {}", executionData.getPrimaryProduct().getProductName());

    }

    @Step("Create Private Product with Define Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDefinePhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDefinePhase");
        productFlows.createPrivateProductWithDefinePhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDefinePhase", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());

    }

    @Step("Create Private Product with Design Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDesignPhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDesignPhase");
        productFlows.createPrivateProductWithDesignPhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDesignPhase", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());

    }

    @Step("Create Private Product with Define and Design Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDefineAndDesignPhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDefineAndDesignPhase");
        productFlows.createPrivateProductWithDefineAndDesignPhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDefineAndDesignPhase", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }

    @Step("Create Private Product with Develop and Design Phase")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createPrivateProductWithDevelopAndDesignPhase() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createPrivateProductWithDevelopAndDesignPhase");
        productFlows.createPrivateProductWithDevelopAndDesignPhase();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createPrivateProductWithDevelopAndDesignPhase", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());

    }
    @Step("Create Private Product and Add Dependency Between Products")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void CreatePrivateProductAndAddDependencyBetweenProducts() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting CreatePrivateProductAndAddDependencyBetweenProducts");
        productFlows.createPrivateProductAndAddDependencyBetweenProducts();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "CreatePrivateProductAndAddDependencyBetweenProducts", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Add Teams and Members to Product")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void AddTeamsAndMembersToProduct() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting AddTeamsAndMembersToProduct");
        productFlows.addTeamsAndMembersToProduct();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "AddTeamsAndMembersToProduct", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Add Product to Release")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void AddProductToRelease() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting AddProductToRelease");
        productFlows.addProductToRelease();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "AddProductToRelease", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] AddProductToRelease completed successfully for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Validate Custom Fields")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void ValidationOfCustomFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting ValidationOfCustomFields");
        productFlows.validationOfCustomFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "ValidationOfCustomFields", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Edit Portfolio Details of Existing Product")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void EditPortfolioDetailsOfExistingProduct() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditPortfolioDetailsOfExistingProduct");
        productFlows.editPortfolioDetailsOfExistingProduct();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditPortfolioDetailsOfExistingProduct", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Edit Product with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void EditProductWithAlltheFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditProductWithAlltheFields");
        productFlows.editProductWithAlltheFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditProductWithAlltheFields", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Edit Product with Add and Remove Release")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void EditProductwithAddAndRemoveRelease() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditProductwithAddAndRemoveRelease");
        productFlows.editProductwithAddAndRemoveRelease();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditProductwithAddAndRemoveRelease", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Edit Product with updating Phases")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void EditProductWithUpdatingPhases() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting EditProductWithUpdatingPhases");
        productFlows.editProductWithUpdatingPhases();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "EditProductWithUpdatingPhases", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Save Product without Mandatory Fields and Verify Error Messages")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT})
    public void SaveProductWithoutMandatoryFieldsAndVerifyErrorMessages() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting SaveProductWithoutMandatoryFieldsAndVerifyErrorMessages");
        productFlows.saveProductWithoutMandatoryFieldsAndVerifyErrorMessages();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "SaveProductWithoutMandatoryFieldsAndVerifyErrorMessages", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Delete Product when Feature is Linked to Product")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PRODUCT},dependsOnMethods = "createPrivateProductWithMandatoryFields")
    public void deleteProductWhenFeatureIsLinkedToProduct() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting deleteProductWhenFeatureIsLinkedToProduct");
        productFlows.deleteProductWhenFeatureIsLinkedToProduct();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "deleteProductWhenFeatureIsLinkedToProduct", executionData);
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }


    @Step("Create Operationalize Private Product with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createOperationalizePrivateProductWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createOperationalizePrivateProductWithMandatoryFields");
        productFlows.createOperationalizePrivateProductWithMandatoryFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createOperationalizePrivateProductWithMandatoryFields", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Create Operationalize Public Product with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void createOperationalizePublicProductWithAllFields() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting createOperationalizePublicProductWithAllFields");
        productFlows.createOperationalizePublicProductWithAllFields();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "createOperationalizePublicProductWithAllFields", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());

    }
    @Step("Validate Cancel Product Creation")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void validateCancelProductCreation() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting validateCancelProductCreation");
        productFlows.validateCancelProductCreation();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed validateCancelProductCreation successfully");
    }
    @Step("Maturity Assessment at Product Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void MaturityAssesmentAtProductLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssesmentAtProductLevel");
        productFlows.MaturityAssesmentAtProductLevel();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "MaturityAssesmentAtProductLevel", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Maturity Assessment at Team Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void MaturityAssesmentAtTeamLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssesmentAtTeamLevel");
        productFlows.MaturityAssesmentAtTeamLevel();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "MaturityAssesmentAtTeamLevel", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed MaturityAssesmentAtTeamLevel successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Maturity Assessment at Team Level with Sprint")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void MaturityAssesmentTeamLevelwithSprint() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssesmentTeamLevelwithSprint");
        productFlows.MaturityAssesmentTeamLevelwithSprint();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "MaturityAssesmentTeamLevelwithSprint", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed MaturityAssesmentTeamLevelwithSprint successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }

    @Step("Initiate and Discard Maturity Assessment")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void IntiateAndDiscardMaturityAssessment() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting IntiateAndDiscardMaturityAssessment");
        productFlows.IntiateAndDiscardMaturityAssessment();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "IntiateAndDiscardMaturityAssessment", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed IntiateAndDiscardMaturityAssessment successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Maturity Assessment Save Without Initiating")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void MaturityAssessmentSaveWithoutInitiating() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssessmentSaveWithoutInitiating");
        productFlows.MaturityAssessmentSaveWithoutInitiating();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "MaturityAssessmentSaveWithoutInitiating", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed MaturityAssessmentSaveWithoutInitiating successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Maturity Assessment Save Without Submitting")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void SaveMaturityAssessmentWithoutSubmitting() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting SaveMaturityAssessmentWithoutSubmitting");
        productFlows.SaveMaturityAssessmentWithoutSubmitting();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "SaveMaturityAssessmentWithoutSubmitting", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed SaveMaturityAssessmentWithoutSubmitting successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Maturity Assessment Completed Assessment Validation")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void MaturityAssessmentCompletedAssessmentValidation() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssessmentCompletedAssessmentValidation");
        productFlows.MaturityAssessmentCompletedAssessmentValidation();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "MaturityAssessmentCompletedAssessmentValidation", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed MaturityAssessmentCompletedAssessmentValidation successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
   @Step("Upload Document As Link")
   @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
   public void UploadDocumentAsLink() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting UploadDocumentAsLink");
        productFlows.uploadDocumentAsLink();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "UploadDocumentAsLink", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed UploadDocumentAsLink successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Upload Document As Image")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void UploadDocumentAsImage() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting UploadDocumentAsImage");
        productFlows.uploadDocumentAsImage();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "UploadDocumentAsImage", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed UploadDocumentAsImage successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Maturity Assessment Completed Team Level With Sprint Assessment Validation")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
        public void MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation");
        productFlows.MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Maturity Assessment Completed Team Level Assessment Validation")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void MaturityAssessmentCompletedTeamLevelAssessmentValidation() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting MaturityAssessmentCompletedTeamLevelAssessmentValidation");
        productFlows.MaturityAssessmentCompletedTeamLevelAssessmentValidation();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "MaturityAssessmentCompletedTeamLevelAssessmentValidation", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed MaturityAssessmentCompletedTeamLevelAssessmentValidation successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Maturity Assessment In Current Assessment")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyMaturityAssessmentInCurrentAssessment() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyMaturityAssessmentInCurrentAssessment");
        productFlows.VerifyMaturityAssessmentInCurrentAssessment();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyMaturityAssessmentInCurrentAssessment", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyMaturityAssessmentInCurrentAssessment successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Maturity Assessment In Baseline Assessment")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyMaturityAssessmentInBaselinedAssessment() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyMaturityAssessmentInBaselinedAssessment");
        productFlows.VerifyMaturityAssessmentInBaselinedAssessment();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyMaturityAssessmentInBaselinedAssessment", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyMaturityAssessmentInBaselinedAssessment successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyTeamLevelMaturityAssessmentInBaselinedAssessment() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyTeamLevelMaturityAssessmentInBaselinedAssessment");
        productFlows.VerifyTeamLevelMaturityAssessmentInBaselinedAssessment();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyTeamLevelMaturityAssessmentInBaselinedAssessment", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyTeamLevelMaturityAssessmentInBaselinedAssessment successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Maturity Assessment In Current Assessment")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyTeamLevelMaturityAssessmentInCurrentAssessment() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyTeamLevelMaturityAssessmentInCurrentAssessment");
        productFlows.VerifyTeamLevelMaturityAssessmentInCurrentAssessment();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyTeamLevelMaturityAssessmentInCurrentAssessment", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyTeamLevelMaturityAssessmentInCurrentAssessment successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Save Maturity Assessment Without Submitting And Verify Resume")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void SaveMaturityAssessmentWithoutSubmittingAndVerifyResume() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting SaveMaturityAssessmentWithoutSubmittingAndVerifyResume");
        productFlows.SaveMaturityAssessmentWithoutSubmittingAndVerifyResume();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "SaveMaturityAssessmentWithoutSubmittingAndVerifyResume", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed SaveMaturityAssessmentWithoutSubmittingAndVerifyResume successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Baselined Assessments Search By Product Name At Product Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyBaselinedAssessmentsSearchByProductNameAtProdcutLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyBaselinedAssessmentsSearchByProductNameAtProdcutLevel");
        productFlows.VerifyBaselinedAssessmentsSearchByProductNameAtProdcutLevel();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyBaselinedAssessmentsSearchByProductNameAtProdcutLevel", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyBaselinedAssessmentsSearchByProductNameAtProdcutLevel successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Current Assessments Search By Product Name At Product Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyCurrentAssessmentsSearchByProductNameAtProdcutLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyCurrentAssessmentsSearchByProductNameAtProdcutLevel");
        productFlows.VerifyCurrentAssessmentsSearchByProductNameAtProdcutLevel();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyCurrentAssessmentsSearchByProductNameAtProdcutLevel", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyCurrentAssessmentsSearchByProductNameAtProdcutLevel successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Current Assessments Page Navigation Validation At Product Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyCurrentAssessmentsPageNavigationValidationAtProductLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyCurrentAssessmentsPageNavigationValidationAtProductLevel");
        productFlows.VerifyCurrentAssessmentsPageNavigationValidationAtProductLevel();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyCurrentAssessmentsPageNavigationValidationAtProductLevel successfully");
    }
    @Step("Verify Baselined Assessments Page Navigation Validation At Product Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyBaselinedAssessmentsPageNavigationValidationAtProductLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyBaselinedAssessmentsPageNavigationValidationAtProductLevel");
        productFlows.VerifyBaselinedAssessmentsPageNavigationValidationAtProductLevel();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyBaselinedAssessmentsPageNavigationValidationAtProductLevel successfully");
    }
    @Step("Verify Baselined Assessments Search By Team Name At Team Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyBaselinedAssessmentsSearchByTeamNameAtTeamLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyBaselinedAssessmentsSearchByTeamNameAtTeamLevel");
        productFlows.VerifyBaselinedAssessmentsSearchByTeamNameAtTeamLevel();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyBaselinedAssessmentsSearchByTeamNameAtTeamLevel", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyBaselinedAssessmentsSearchByTeamNameAtTeamLevel successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Current Assessments Search By Team Name At Team Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyCurrentAssessmentsSearchByTeamNameAtTeamLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyCurrentAssessmentsSearchByTeamNameAtTeamLevel");
        productFlows.VerifyCurrentAssessmentsSearchByTeamNameAtTeamLevel();
        ProExecutionResultWriter.write(ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getResultSheet(), "VerifyCurrentAssessmentsSearchByTeamNameAtTeamLevel", executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyCurrentAssessmentsSearchByTeamNameAtTeamLevel successfully; execution data written for portfolio: {}", executionData.getPrimaryProduct().getProductName());
    }
    @Step("Verify Current Assessments Page Navigation Validation At Team Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyCurrentAssessmentsPageNavigationValidationAtTeamLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyCurrentAssessmentsPageNavigationValidationAtTeamLevel");
        productFlows.VerifyCurrentAssessmentsPageNavigationValidationAtTeamLevel();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyCurrentAssessmentsPageNavigationValidationAtTeamLevel successfully");
    }
    @Step("Verify Baselined Assessments Page Navigation Validation At Team Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_PRODUCT})
    public void VerifyBaselinedAssessmentsPageNavigationValidationAtTeamLevel() {
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Starting VerifyBaselinedAssessmentsPageNavigationValidationAtTeamLevel");
        productFlows.VerifyBaselinedAssessmentsPageNavigationValidationAtTeamLevel();
        LoggerUtil.LOGGER.info("[PRODUCT-TEST] Completed VerifyBaselinedAssessmentsPageNavigationValidationAtTeamLevel successfully");
    }
}
