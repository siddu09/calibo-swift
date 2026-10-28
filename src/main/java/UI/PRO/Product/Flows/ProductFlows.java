package UI.PRO.Product.Flows;

import java.util.Map;

import drivers.PlaywrightFactory;
import pages.PRO.Product.ProductAdditionalDetailsOverviewTabPage;
import pages.PRO.Features.Feature.ResilientFeaturePage;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.utils.ProExecutionDataReader;
import UI.PRO.ProductPortfolio.validations.PortfolioValidation;
import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Product.validations.ProductValidation;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import UI.PRO.datahelper.ProductData;
import UI.PRO.datahelper.ProductData.ProductAllocationData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import testdatamanager.pro.ProExecutionResultWriter;

public class ProductFlows {

    private final ProductBuildingBlock productBuildingBlock;
    private final ProExecutionData executionData;
    private final Page page;
    private final ProductPortfolioFlows productPortfolioFlows;

    public ProductFlows(Page page, ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        productBuildingBlock = new ProductBuildingBlock(page, executionData);
        productPortfolioFlows = new ProductPortfolioFlows(page, executionData);
    }

    @Step("Upload product document as a link and verify it opens")
    public void uploadDocumentAsLink() {
        PlaywrightFactory.maximizeChromiumWindow(page);
        ProductData data = ProTestData.getProduct("UploadDocumentAsLink");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.addProductDocument(data.getDocument());
        productBuildingBlock.verifyDocumentLinkOpens(data.getDocument());
    }

    @Step("Upload product document as an image and verify it downloads")
    public void uploadDocumentAsImage() {
        PlaywrightFactory.maximizeChromiumWindow(page);
        ProductData data = ProTestData.getProduct("UploadDocumentAsImage");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.addProductDocument(data.getDocument());
        productBuildingBlock.verifyDocumentDownloads(data.getDocument());
    }

    @Step("Create private product from Products with mandatory fields")
    public void createPrivateProductWithMandatoryFields() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct(ProTestData.CREATE_PRODUCT));
    }

    private void createPrivateProductWithMandatoryFields(ProductData productData) {
        createProductFromProducts(productData);
        productBuildingBlock.saveOrSkipProductAdditionalDetails(productData.getSkipButton());
        productBuildingBlock.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, productData);
        utils.LoggerUtil.LOGGER.info("[PRODUCT-FLOW] Mandatory product details validated successfully");
    }

    @Step("Create private Operationalize product with mandatory fields")
    public void createOperationalizePrivateProductWithMandatoryFields() {
        ProductData data = ProTestData.getProduct("createOperationalizePrivateProductWithMandatoryFields");
        createProductFromProducts(data);
        productBuildingBlock.skipOperationalizeAdditionalDetails(data);
        ProductValidation.validateProductDetails(page, executionData, data);
    }

    @Step("Create private product with Define phase and mandatory fields")
    public void createPrivateProductWithDefinePhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDefinePhase"));
    }

    @Step("Create private product with Design phase and mandatory fields")
    public void createPrivateProductWithDesignPhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDesignPhase"));
    }

    @Step("Create private product with Define and Design phases and mandatory fields")
    public void createPrivateProductWithDefineAndDesignPhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDefineAndDesignPhase"));
    }

    @Step("Create private product with Develop and Design phases and mandatory fields")
    public void createPrivateProductWithDevelopAndDesignPhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDevelopAndDesignPhase"));
    }

    @Step("Create private products and verify dependencies in both directions")
    public void createPrivateProductAndAddDependencyBetweenProducts() {
        ProductData data = ProTestData.getProduct(ProTestData.PRODUCT_DEPENDENCY);
        ProTestData.saveDependencyProductNames("", "");
        createPrivateProductWithMandatoryFields(data);
        String product1Name = executionData.getProducts().get(executionData.getProducts().size() - 1).getProductName();
        ProTestData.saveDependencyProductNames(product1Name, "");
        createPrivateProductWithMandatoryFields(data);
        String product2Name = executionData.getProducts().get(executionData.getProducts().size() - 1).getProductName();
        ProTestData.saveDependencyProductNames(product1Name, product2Name);
        data = ProTestData.getProduct(ProTestData.PRODUCT_DEPENDENCY);
        Assert.assertNotEquals(data.getProduct1Name(), data.getProduct2Name(), data.getDistinctProductNamesMessage());
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.addDependency(executionData.getPortfolioName(), data.getProduct1Name());
        productBuildingBlock.openProduct(data.getProduct1Name(), data);
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.validateDependent(executionData.getPortfolioName(), data.getProduct2Name(), data);
        productBuildingBlock.validateDependencyNotification(data.getProduct2Name(), data, true);
        productBuildingBlock.addDependency(executionData.getPortfolioName(), data.getProduct2Name());
        productBuildingBlock.openProduct(data.getProduct2Name(), data);
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.validateDependent(executionData.getPortfolioName(), data.getProduct1Name(), data);
        productBuildingBlock.validateDependencyNotification(data.getProduct1Name(), data, true);
    }

    @Step("Create a release and join it with a private product")
    public void addProductToRelease() {
        ProductData data = ProTestData.getProduct("addProductToRelease");
        ProductValidation.validateReleaseObjective(data.getProductRelease());
        productBuildingBlock.createProductRelease(data);
        data = ProTestData.getProduct("addProductToRelease");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.joinProductRelease(data);
    }

    @Step("Add a team and a member to a private product")
    public void addTeamsAndMembersToProduct() {
        ProductData data = ProTestData.getProduct("addTeamsAndMembersToProduct");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.navigateToTeamsTab(data);
        for (ProductAllocationData allocation : data.getAllocations()) {
            productBuildingBlock.addProductAllocation(data, allocation);
            ProductValidation.validateProductAllocation(page, allocation);
        }
        for (ProductAllocationData allocation : data.getAllocations()) {
            ProductValidation.validateProductAllocation(page, allocation);
        }
    }

    @Step("Create public product with all fields")
    public void createPublicProductWithAllFields() {
        createPublicProductWithAllFields(ProTestData.getProduct("createPublicProduct"));
    }

    private void createPublicProductWithAllFields(ProductData productData) {
        createProductFromProducts(productData);
        productBuildingBlock.completeProductOverview(productData);
        productBuildingBlock.completeProductCustomFields(productData);
        productBuildingBlock.validateProductMilestones(productData);
        productBuildingBlock.saveOrSkipProductAdditionalDetails(productData.getSaveButton());
        productBuildingBlock.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, productData);
        utils.LoggerUtil.LOGGER.info("[PRODUCT-FLOW] Public product details validated successfully");
    }

    @Step("Create public Operationalize product with all fields")
    public void createOperationalizePublicProductWithAllFields() {
        ProductData data = ProTestData.getProduct(
                "createOperationalizePublicProductWithAllFields", "createPublicProduct");
        createProductFromProducts(data);
        productBuildingBlock.completeProductOverview(data);
        productBuildingBlock.completeProductCustomFields(data);
        productBuildingBlock.saveOperationalizeProductAdditionalDetails(data);
        ProductValidation.validateProductDetails(page, executionData, data);
    }

    @Step("Validate configured and dynamic custom fields on a public product")
    public void validationOfCustomFields() {
        ProductData data = ProTestData.getProduct("validationOfCustomFields", "createPublicProduct");
        createPublicProductWithAllFields(data);
        ProductValidation.validateProductOverview(page, data);
    }


    @Step("Edit portfolio overview with an existing product and validate audit events")
    public void editPortfolioDetailsOfExistingProduct() {
        ProductData data = ProTestData.getProduct("editPortfolioDetailsOfExistingProduct");
        productPortfolioFlows.createPortfolioWithMandatoryFields(data.getPortfolioToCreate());
        productPortfolioFlows.openExistingPortfolio();
        PortfolioValidation.validatePortfolioDetails(
                page, executionData, data.getPortfolioToCreate());
        String originalName = executionData.getPortfolioName();
        createProductWithMandatoryFields();
        productBuildingBlock.editExistingProductPortfolioOverview(data);
        ProductValidation.validateEditedProductPortfolio(page, executionData, data);
        productBuildingBlock.validateExistingProductPortfolioAudit(data, originalName);
    }

    private void createProductFromProducts(ProductData productData) {
        productBuildingBlock.openNewProductFromProducts();
        productBuildingBlock.selectProductType(productData.getProductType());
        productBuildingBlock.selectOrCreateProductPortfolio();
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, productData.getProductCreatedMessage());
    }

    @Step("Create product with mandatory fields")
    public void createProductWithMandatoryFields() {

        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();

        ProductData productData = ProTestData.getProduct("createProduct");

        productBuildingBlock.selectProductType("Productize");

        productBuildingBlock.createNewProduct(productData);

        ProValidation.validateSuccessMessage(page, "Product created successfully.");

        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");


        productBuildingBlock.chooseFeatureCreationOption();

        page.waitForTimeout(5000); // Wait for 2 seconds to ensure the page has loaded before validation

        ProductValidation.validateProductDetails(page, executionData, productData);


    }
    @Step("Delete product and validate success message")
    public void deleteProduct() {
        ProductData data = ProTestData.getProduct(ProTestData.CREATE_PRODUCT);
        Assert.assertNotNull(executionData.getPrimaryProduct(), data.getMissingSourceProductMessage());
        productBuildingBlock.openProduct(executionData.getPrimaryProduct().getProductName(), data);
        deleteProduct(data);
        validateProductDeleted();
    }

    public void navigateToFeatureTab() {
        productBuildingBlock.navigateToFeatureTab();
    }

    public void addDependencyToProduct() {
        productBuildingBlock.navigateToDependencyTab();

        productBuildingBlock.addDependency(executionData.getPortfolioName(), executionData.getProducts().get(0).getProductName());

    }
    public void createProductAndFeatureInline(ProductData productData) {
        productBuildingBlock.selectProductType("Productize");
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");
        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");
        productBuildingBlock.chooseFeatureCreationOption("Yes");

    }

    @Step("Edit all product fields, remove added custom field and milestone, and add/delete a dependency")
    public void editProductWithAlltheFields() {
        ProductData creation = ProTestData.getProduct("createPublicProduct");
        creation.setPriority(ProTestData.getProduct("editProductWithAlltheFields").getInitialPriority());
        createPublicProductWithAllFields(creation);
        ProductData data = ProTestData.getProduct("editProductWithAlltheFields", "createPublicProduct");
        productBuildingBlock.openProductEditor(data);
        productBuildingBlock.updateProductOverview(data);
        productBuildingBlock.updateProductCustomFields(data);
        productBuildingBlock.saveEditedProductDetails(data);
        ProductValidation.validateProductDetails(page, executionData, data);
        ProductValidation.validateProductOverview(page, data);
        data = ProTestData.getProduct("editProductWithAlltheFields", "createPublicProduct");
        productBuildingBlock.openProductEditor(data);
        productBuildingBlock.validateEditedProductOverview(data);
        productBuildingBlock.completeProductCustomFields(data);
        productBuildingBlock.deleteProductCustomField(data);
        productBuildingBlock.saveEditedProductDetails(data);
        productBuildingBlock.openProductEditor(data);
        productBuildingBlock.addProductMilestone(data);
        productBuildingBlock.saveEditedProductDetails(data);
        data = ProTestData.getProduct("editProductWithAlltheFields", "createPublicProduct");
        productBuildingBlock.openProductEditor(data);
        productBuildingBlock.deleteProductMilestone(data);
        productBuildingBlock.validateProductMilestones(data);
        productBuildingBlock.saveEditedProductDetails(data);
        // Keep the edited product's execution record separate from its dependency fixture.
        String editedProductName = executionData.getPrimaryProduct().getProductName();
        ProExecutionData dependencyExecution = new ProExecutionData();
        ProductFlows dependencyFlow = new ProductFlows(page, dependencyExecution);
        ProductData dependency = ProTestData.getProduct(ProTestData.CREATE_PRODUCT);
        dependency.setTitle(data.getTitle() + " Dependency");
        ProTestData.saveProductValues("editProductWithAlltheFields", Map.of(
                "dependencyPortfolioName", "", "dependencyProductName", ""));
        dependencyFlow.createPrivateProductWithMandatoryFields(dependency);
        executionData.getProducts().add(dependencyExecution.getPrimaryProduct());
        ProTestData.saveProductValues("editProductWithAlltheFields", Map.of(
                "dependencyPortfolioName", dependencyExecution.getPortfolioName(),
                "dependencyProductName", dependencyExecution.getPrimaryProduct().getProductName()));
        data = ProTestData.getProduct("editProductWithAlltheFields", "createPublicProduct");
        Assert.assertNotEquals(data.getDependencyProductName(), executionData.getPrimaryProduct().getProductName(),
                "Dependency fixture must be a different product from the edited product");
        productBuildingBlock.openProduct(editedProductName, data);
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.addDependency(data.getDependencyPortfolioName(), data.getDependencyProductName());
        productBuildingBlock.deleteProductDependency(data);
        dependencyFlow.deleteProduct();
    }


    @Step("Create a mandatory private product, join an existing release and leave it")
    public void editProductwithAddAndRemoveRelease() {
        ProductData data = ProTestData.getProduct("editProductwithAddAndRemoveRelease", "addProductToRelease");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.joinProductRelease(data);
        productBuildingBlock.leaveProductRelease(data);
    }


    @Step("Remove Define and Design from a private product and verify saved phases")
    public void editProductWithUpdatingPhases() {
        ProductData data = ProTestData.getProduct("editProductWithUpdatingPhases");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.openProductEditor(data);
        productBuildingBlock.validateSelectedProductPhases(data, data.getPhases());
        data.getPhasesToRemove().forEach(phase -> productBuildingBlock.removeProductPhase(phase, data));
        productBuildingBlock.validateSelectedProductPhases(data, data.getExpectedPhases());
        productBuildingBlock.saveEditedProductDetails(data);
        ProductValidation.validateProductDetails(page, executionData, data);
        productBuildingBlock.openProductEditor(data);
        productBuildingBlock.validateSelectedProductPhases(data, data.getExpectedPhases());
    }

    @Step("Validate mandatory fields for product creation, releases, dependencies, teams and KPIs")
    public void saveProductWithoutMandatoryFieldsAndVerifyErrorMessages() {
        ProductData data = ProTestData.getProduct("saveProductWithoutMandatoryFieldsAndVerifyErrorMessages");
        productBuildingBlock.openNewProductFromProducts();
        productBuildingBlock.selectProductType(data.getProductType());
        productBuildingBlock.submitProductWithoutMandatoryFieldsAndValidateErrors(data);
        productBuildingBlock.selectOrCreateProductPortfolio();
        productBuildingBlock.validateMandatoryErrorRemoved(data.getMandatoryFieldErrors().getFirst());
        productBuildingBlock.createProductAfterMandatoryFieldErrors(data);
        ProValidation.validateSuccessMessage(page, data.getProductCreatedMessage());
        productBuildingBlock.saveOrSkipProductAdditionalDetails(data.getSkipButton());
        productBuildingBlock.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, data);
        validateProductRelatedMandatoryFields();
        validateProductKpiMandatoryFields();
    }

    @Step("Validate mandatory selections for product release, dependencies and teams")
    public void validateProductRelatedMandatoryFields() {
        ProductData data = ProTestData.getProduct("saveProductWithoutMandatoryFieldsAndVerifyErrorMessages", "addProductToRelease");
        productBuildingBlock.submitJoinReleaseWithoutMandatoryFields(data);
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.submitDependencyWithoutMandatoryFields(data);
        ProductData teamData = ProTestData.getProduct("addTeamsAndMembersToProduct");
        productBuildingBlock.navigateToTeamsTab(teamData);
        productBuildingBlock.submitMemberTeamWithoutMandatoryFields(teamData, data);
    }

    @Step("Validate mandatory fields when creating a product KPI")
    public void validateProductKpiMandatoryFields() {
        ProductData data = ProTestData.getProduct("saveProductWithoutMandatoryFieldsAndVerifyErrorMessages", "createPublicProduct");
        productBuildingBlock.closeMemberTeamPopup();
        productBuildingBlock.openProductEditor(data);
        productBuildingBlock.submitKpiWithoutMandatoryFields(data);
    }

    @Step("Delete product and validate the configured success message")
    public void deleteProduct(ProductData data) {
        productBuildingBlock.deleteProduct(data);
        ProValidation.validateSuccessMessage(page, data.getProductDeletedMessage());
    }

    @Step("Create and delete a linked feature, then delete the product recorded in execution data")
    public void deleteProductWhenFeatureIsLinkedToProduct() {
        ProductData data = ProTestData.getProduct("deleteProductWhenFeatureIsLinkedToProduct");
        createPrivateProductWithMandatoryFields(ProTestData.getProduct(ProTestData.CREATE_PRODUCT));
        String productName = executionData.getPrimaryProduct().getProductName();
        productBuildingBlock.openProduct(productName, data);
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getFeaturesTab()).click();
        FeatureFlows featureFlows = new FeatureFlows(
                page, executionData, new ResilientFeaturePage(page));
        featureFlows.createFeatureWithMandatoryFields(data.getFeatureToCreate());
        featureFlows.viewFeatureDetails(data.getFeatureToCreate());
        String featureName = executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName();
        ProExecutionResultWriter.write(data.getExecutionDataFile(), data.getResultSheet(),
                "deleteProductWhenFeatureIsLinkedToProduct", executionData);
        featureFlows.deleteFeature(data);
        productBuildingBlock.validateLinkedFeatureDeleted(featureName, data);
        productBuildingBlock.openProduct(productName, data);
        deleteProduct();
    }

    @Step("Verify the current test's recorded product is absent after deletion")
    public void validateProductDeleted() {
        productBuildingBlock.validateRecordedProductDeleted(
                ProTestData.getProduct(ProTestData.CREATE_PRODUCT));
    }

    @Step("Validate cancellation of Productize product creation")
    public void validateCancelProductCreation() {
        ProductData data = ProTestData.getProduct("validateCancelProductCreation");
        productBuildingBlock.openNewProductFromProducts();
        productBuildingBlock.selectProductType(data.getProductType());
        productBuildingBlock.cancelProductCreation(data);
        productBuildingBlock.validateProductsListingPage(data);
    }

    @Step("Initiate maturity assessment for a private product")
    public void MaturityAssesmentAtProductLevel() {
        ProductData data = ProTestData.getProduct("maturityAssesmentAtProductLevel", "addProductToRelease");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.joinReleaseForProductMaturityAssessment(data);
        productBuildingBlock.initiateProductMaturityAssessment(data);
    }

    @Step("Save and resume a Product Delivery assessment response without submitting")
    public void SaveMaturityAssessmentWithoutSubmitting() {
        MaturityAssesmentAtProductLevel();
        ProductData data = ProTestData.getProduct("SaveMaturityAssessmentWithoutSubmitting", "maturityAssesmentAtProductLevel");
        String assessmentName = productBuildingBlock.initializedProductMaturityAssessmentName(data);
        ProTestData.saveProductValues("SaveMaturityAssessmentWithoutSubmitting",
                Map.of("assessmentName", assessmentName));
        data = ProTestData.getProduct("SaveMaturityAssessmentWithoutSubmitting", "maturityAssesmentAtProductLevel");
        productBuildingBlock.openAndStartMyProductMaturityAssessment(data);
        productBuildingBlock.saveProductMaturityAssessmentResponseWithoutSubmitting(data);
        productBuildingBlock.validateAndResumeSavedProductMaturityAssessmentResponse(data);
    }

    @Step("Save the response without submitting, verify Resume is available, and reopen the saved answer")
    public void SaveMaturityAssessmentWithoutSubmittingAndVerifyResume() {
        SaveMaturityAssessmentWithoutSubmitting();
    }

    @Step("Submit a Product Delivery assessment and verify the completed response")
    public void MaturityAssessmentCompletedAssessmentValidation() {
        MaturityAssesmentAtProductLevel();
        ProductData data = ProTestData.getProduct("MaturityAssessmentCompletedAssessmentValidation", "maturityAssesmentAtProductLevel");
        String assessmentName = productBuildingBlock.initializedProductMaturityAssessmentName(data);
        ProTestData.saveProductValues("MaturityAssessmentCompletedAssessmentValidation",
                Map.of("assessmentName", assessmentName));
        data = ProTestData.getProduct("MaturityAssessmentCompletedAssessmentValidation", "maturityAssesmentAtProductLevel");
        productBuildingBlock.openAndStartMyProductMaturityAssessment(data);
        productBuildingBlock.submitMyProductMaturityAssessmentResponse(data);
        productBuildingBlock.validateMyCompletedProductMaturityAssessmentResponse(data);
    }

    @Step("Complete a product maturity assessment and verify it in Baselined Assessments")
    public void VerifyMaturityAssessmentInBaselinedAssessment() {
        MaturityAssessmentCompletedAssessmentValidation();
        ProductData data = ProTestData.getProduct("VerifyMaturityAssessmentInBaselinedAssessment",
                "maturityAssesmentAtProductLevel");
        String assessmentName = ProTestData.getProduct("MaturityAssessmentCompletedAssessmentValidation")
                .getAssessmentName();
        executionData.setAssessmentName(assessmentName);
        productBuildingBlock.openProduct(executionData.getPrimaryProduct().getProductName(), data);
        productBuildingBlock.baselineCompletedProductMaturityAssessment(data);
    }

    @Step("Save a Product Delivery maturity assessment without initiating")
    public void MaturityAssessmentSaveWithoutInitiating() {
        ProductData data = ProTestData.getProduct("MaturityAssessmentSaveWithoutInitiating", "maturityAssesmentAtProductLevel");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.joinReleaseForProductMaturityAssessment(data);
        String assessmentName = productBuildingBlock.saveProductMaturityAssessmentWithoutInitiating(data);
        productBuildingBlock.validateSavedProductMaturityAssessmentDraft(data, assessmentName);
    }

    @Step("Verify an initiated product maturity assessment remains in Current Assessments")
    public void VerifyMaturityAssessmentInCurrentAssessment() {
        MaturityAssesmentAtProductLevel();
        ProductData data = ProTestData.getProduct("VerifyMaturityAssessmentInCurrentAssessment",
                "maturityAssesmentAtProductLevel");
        String assessmentName = productBuildingBlock.initializedProductMaturityAssessmentName(data);
        productBuildingBlock.validateInitiatedProductMaturityAssessmentInCurrentAssessments(data, assessmentName);
    }

    @Step("Initiate and discard a Product Delivery maturity assessment")
    public void IntiateAndDiscardMaturityAssessment() {
        MaturityAssesmentAtProductLevel();
        ProductData data = ProTestData.getProduct("IntiateAndDiscardMaturityAssessment", "maturityAssesmentAtProductLevel");
        productBuildingBlock.discardInitiatedProductMaturityAssessment(data);
    }

    @Step("Initiate Team Practices maturity assessment for a private product")
    public void MaturityAssesmentAtTeamLevel() {
        ProductData data = ProTestData.getProduct("maturityAssesmentAtTeamLevel", "maturityAssesmentAtProductLevel");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.joinReleaseForProductMaturityAssessment(data);
        productBuildingBlock.initiateProductMaturityAssessment(data);
        productBuildingBlock.storeMaturityAssessmentName(data, "maturityAssesmentAtTeamLevel");
    }

    @Step("Complete a Team Practices assessment and verify the submitted response")
    public void MaturityAssessmentCompletedTeamLevelAssessmentValidation() {
        MaturityAssesmentAtTeamLevel();
        String assessmentName = ProTestData.getProduct("maturityAssesmentAtTeamLevel")
                .getAssessmentName();
        ProTestData.saveProductValues("MaturityAssessmentCompletedTeamLevelAssessmentValidation",
                Map.of("assessmentName", assessmentName));
        ProductData data = ProTestData.getProduct("MaturityAssessmentCompletedTeamLevelAssessmentValidation");
        productBuildingBlock.openAndStartMyProductMaturityAssessment(data);
        productBuildingBlock.submitMyProductMaturityAssessmentResponse(data);
        productBuildingBlock.validateMyCompletedProductMaturityAssessmentResponse(data);
    }

    @Step("Complete a Team Practices Sprint assessment and verify the submitted response")
    public void MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation() {
        MaturityAssesmentTeamLevelwithSprint();
        String assessmentName = ProTestData.getProduct("maturityAssesmentTeamLevelwithSprint")
                .getAssessmentName();
        ProTestData.saveProductValues("MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation",
                Map.of("assessmentName", assessmentName));
        ProductData data = ProTestData.getProduct(
                "MaturityAssessmentCompletedTeamLevelWithSprintAssessmentValidation");
        productBuildingBlock.openAndStartMyProductMaturityAssessment(data);
        productBuildingBlock.submitMyProductMaturityAssessmentResponse(data);
        productBuildingBlock.validateMyCompletedProductMaturityAssessmentResponse(data);
    }

    @Step("Initiate Team Practices maturity assessment associated with a sprint")
    public void MaturityAssesmentTeamLevelwithSprint() {
        ProductData data = ProTestData.getProduct("maturityAssesmentTeamLevelwithSprint", "maturityAssesmentAtProductLevel");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.configureProductSprintDetails(data);
        productBuildingBlock.navigateToTeamsTab(data);
        productBuildingBlock.addProductSprintTeam(data);
        productBuildingBlock.joinReleaseForProductMaturityAssessment(data);
        productBuildingBlock.openProduct(executionData.getPrimaryProduct().getProductName(), data);
        productBuildingBlock.reloadProductDetails(data);
        productBuildingBlock.configureProductSprintBoard(data);
        productBuildingBlock.initiateTeamMaturityAssessmentWithSprint(data);
    }

    @Step("Complete a Team Practices assessment and verify it in Baselined Assessments")
    public void VerifyTeamLevelMaturityAssessmentInBaselinedAssessment() {
        MaturityAssessmentCompletedTeamLevelAssessmentValidation();
        ProductData data = ProTestData.getProduct("VerifyTeamLevelMaturityAssessmentInBaselinedAssessment",
                "maturityAssesmentAtTeamLevel");
        String assessmentName = ProTestData.getProduct("MaturityAssessmentCompletedTeamLevelAssessmentValidation")
                .getAssessmentName();
        executionData.setAssessmentName(assessmentName);
        productBuildingBlock.openProduct(executionData.getPrimaryProduct().getProductName(), data);
        productBuildingBlock.baselineCompletedProductMaturityAssessment(data);
    }

    @Step("Verify an initiated Team Practices assessment remains In Progress in Current Assessments")
    public void VerifyTeamLevelMaturityAssessmentInCurrentAssessment() {
        MaturityAssesmentAtTeamLevel();
        ProductData data = ProTestData.getProduct("VerifyTeamLevelMaturityAssessmentInCurrentAssessment",
                "maturityAssesmentAtTeamLevel");
        String assessmentName = data.getAssessmentName();
        executionData.setAssessmentName(assessmentName);
        productBuildingBlock.validateInitiatedProductMaturityAssessmentInCurrentAssessments(data, assessmentName);
    }

    @Step("Delete product after DPS execution is successful")
    public void deleteDPSProduct() {
        productBuildingBlock.deleteProduct();
    }

    @Step("Baseline an assessment and search for its product from the main Product maturity page")
    public void VerifyBaselinedAssessmentsSearchByProductNameAtProdcutLevel() {
        String scenario = "VerifyBaselinedAssessmentsSearchByProductNameAtProdcutLevel";
        ProductData searchData = ProTestData.getProduct(scenario);
        ProductData data = ProTestData.getProduct("MaturityAssessmentCompletedAssessmentValidation",
                "maturityAssesmentAtProductLevel");
        createPrivateProductWithMandatoryFields(data);
        String productName = executionData.getPrimaryProduct().getProductName();
        ProTestData.saveProductValues(scenario, Map.of("productName", productName));
        Assert.assertEquals(ProTestData.getProduct(scenario).getProductName(), productName,
                searchData.getBaselinedAssessmentSearch().getSavedProductNameMismatchMessage());
        productBuildingBlock.joinReleaseForProductMaturityAssessment(data);
        productBuildingBlock.initiateProductMaturityAssessment(data);
        String assessmentName = productBuildingBlock.initializedProductMaturityAssessmentName(data);
        ProTestData.saveProductValues(scenario, Map.of("assessmentName", assessmentName));
        data.setAssessmentName(assessmentName);
        productBuildingBlock.openAndStartMyProductMaturityAssessment(data);
        productBuildingBlock.submitMyProductMaturityAssessmentResponse(data);
        productBuildingBlock.validateMyCompletedProductMaturityAssessmentResponse(data);
        ProductData baselineData = ProTestData.getProduct("VerifyMaturityAssessmentInBaselinedAssessment",
                "maturityAssesmentAtProductLevel");
        executionData.setAssessmentName(assessmentName);
        productBuildingBlock.openProduct(productName, baselineData);
        productBuildingBlock.baselineCompletedProductMaturityAssessment(baselineData);
        productBuildingBlock.searchBaselinedAssessmentsByProductName(ProTestData.getProduct(scenario));
    }

    @Step("Initiate an assessment and search its saved product name from main Current Assessments")
    public void VerifyCurrentAssessmentsSearchByProductNameAtProdcutLevel() {
        String scenario = "VerifyCurrentAssessmentsSearchByProductNameAtProdcutLevel";
        ProductData data = ProTestData.getProduct("VerifyMaturityAssessmentInCurrentAssessment",
                "maturityAssesmentAtProductLevel");
        ProductData searchData = ProTestData.getProduct(scenario, "VerifyMaturityAssessmentInCurrentAssessment");
        createPrivateProductWithMandatoryFields(data);
        String productName = executionData.getPrimaryProduct().getProductName();
        ProTestData.saveProductValues(scenario, Map.of("productName", productName));
        Assert.assertEquals(ProTestData.getProduct(scenario).getProductName(), productName,
                searchData.getSavedSearchProductNameMismatchMessage());
        productBuildingBlock.joinReleaseForProductMaturityAssessment(data);
        productBuildingBlock.initiateProductMaturityAssessment(data);
        String assessmentName = productBuildingBlock.initializedProductMaturityAssessmentName(data);
        ProTestData.saveProductValues(scenario, Map.of("assessmentName", assessmentName));
        executionData.setAssessmentName(assessmentName);
        productBuildingBlock.validateInitiatedProductMaturityAssessmentInCurrentAssessments(data, assessmentName);
        productBuildingBlock.searchCurrentAssessmentsByProductName(
                ProTestData.getProduct(scenario, "VerifyMaturityAssessmentInCurrentAssessment"));
    }

    @Step("Verify Current Assessments pagination and Showing page size at Product level")
    public void VerifyCurrentAssessmentsPageNavigationValidationAtProductLevel() {
        ProductData data = ProTestData.getProduct("VerifyCurrentAssessmentsPageNavigationvaldaitonAtProdcutLevel");
        productBuildingBlock.validateCurrentAssessmentsPageNavigation(data);
    }

    @Step("Verify optional page two and Showing size in Product Baselined Assessments")
    public void VerifyBaselinedAssessmentsPageNavigationValidationAtProductLevel() {
        ProductData data = ProTestData.getProduct("VerifyBaselinedAssessmentsPageNavigationValidationAtProductLevel");
        productBuildingBlock.validateBaselinedAssessmentsPageNavigation(data);
    }

    @Step("Baseline a team assessment and search its saved team name from the main Team maturity page")
    public void VerifyBaselinedAssessmentsSearchByTeamNameAtTeamLevel() {
        VerifyTeamLevelMaturityAssessmentInBaselinedAssessment();
        String scenario = "VerifyBaselinedAssessmentsSearchByTeamNameAtTeamLevel";
        ProductData searchData = ProTestData.getProduct(scenario);
        productBuildingBlock.searchBaselinedAssessmentsByTeamName(searchData);
    }

    @Step("Initiate a team assessment and search Current Assessments using its saved team name")
    public void VerifyCurrentAssessmentsSearchByTeamNameAtTeamLevel() {
        String scenario = "VerifyCurrentAssessmentsSearchByTeamNameAtTeamLevel";
        ProductData data = ProTestData.getProduct("maturityAssesmentAtTeamLevel",
                "maturityAssesmentAtProductLevel");
        ProductData teamData = ProTestData.getProduct("maturityAssesmentTeamLevelwithSprint",
                "maturityAssesmentAtProductLevel");
        createPrivateProductWithMandatoryFields(data);
        productBuildingBlock.navigateToTeamsTab(teamData);
        productBuildingBlock.addProductSprintTeam(teamData);
        String teamName = teamData.getSprintSetup().getTeamName();
        ProTestData.saveProductValues(scenario, Map.of("teamName", teamName));
        Assert.assertEquals(ProTestData.getProduct(scenario).getTeamName(), teamName,
                ProTestData.getProduct(scenario).getTeamAssessmentSearch().getSavedTeamNameMismatchMessage());
        productBuildingBlock.joinReleaseForProductMaturityAssessment(data);
        productBuildingBlock.initiateProductMaturityAssessment(data);
        productBuildingBlock.storeMaturityAssessmentName(data, scenario);
        executionData.setAssessmentName(data.getAssessmentName());
        ProductData searchData = ProTestData.getProduct(scenario, "maturityAssesmentAtTeamLevel");
        productBuildingBlock.validateInitiatedProductMaturityAssessmentInCurrentAssessments(
                searchData, searchData.getAssessmentName());
        productBuildingBlock.searchCurrentAssessmentsByTeamName(searchData);
    }

    @Step("Verify optional page two and Showing size in main Team Current Assessments")
    public void VerifyCurrentAssessmentsPageNavigationValidationAtTeamLevel() {
        ProductData data = ProTestData.getProduct("VerifyCurrentAssessmentsPageNavigationValidationAtTeamLevel");
        productBuildingBlock.validateTeamCurrentAssessmentsOptionalPageNavigation(data);
    }

    @Step("Verify optional page two and Showing size in Team Baselined Assessments")
    public void VerifyBaselinedAssessmentsPageNavigationValidationAtTeamLevel() {
        ProductData data = ProTestData.getProduct("VerifyBaselinedAssessmentsPageNavigationValidationAtTeamLevel");
        productBuildingBlock.validateTeamBaselinedAssessmentsPageNavigation(data);
    }

}
