package UI.PRO.Product.Flows;

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
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.addDependency(data.getDependencyPortfolioName(), data.getDependencyProductName());
        productBuildingBlock.deleteProductDependency(data);
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
        ProExecutionDataReader.loadProductForDeletion(data, executionData);
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
        deleteProduct(data);
        productBuildingBlock.validateRecordedProductDeleted(data);
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

}
