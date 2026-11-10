package UI.PRO.Features.Define.Flows;

import UI.PRO.Features.Define.BuildingBlocks.DefineBuildingBlock;
import UI.PRO.Features.Feature.BuildingBlocks.FeatureBuildingBlock;
import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.Features.Define.Validations.DefineValidation;
import UI.PRO.datahelper.DefineData;
import UI.PRO.datahelper.FeatureData;
import UI.PRO.datahelper.ProductData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.PRO.Features.Design.DesignFeaturePage;
import pages.PRO.Product.ProductAdditionalDetailsOverviewTabPage;
import pages.PRO.Product.ProductAdditionalDetailsOthersTabPage;
import pages.PRO.Product.ProductFeatureTabPage;
import pages.PRO.ReleaseTrain.NewReleasePage;
import testdatamanager.pro.DefineTestData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import utils.CommonMethods;
import utils.LoggerUtil;

import java.util.LinkedHashMap;
import java.util.Map;


public class DefineFlows {

    private final DefineBuildingBlock defineBuildingBlock;
    private final Page page;
    private final ProExecutionData executionData;

    public DefineFlows(
            Page page,
            ProExecutionData executionData) {

        this.page=page;
        this.executionData = executionData;
        defineBuildingBlock =
                new DefineBuildingBlock(
                        page,
                        executionData);
    }

    public void createUserFeedbackWithMandatoryFields() {

        DefineData defineData =
                ProTestData.getDefine(
                        "createUserFeedback");

        defineBuildingBlock
                .createBusinessRequirement(
                        defineData);

        ProValidation.validateSuccessMessage(
                page,
                "User Feedback added successfully."
        );

        defineBuildingBlock.viewBusinessRequirement();
        DefineValidation.validateBusinessRequirementDetails(
                page,
                executionData,
                defineData);

    }

    @Step("Create a private product, configure its integration fields, and create a user story")
    public void CreateNewUserStoryWithMandatoryFields() {
        CreateNewUserStoryWithMandatoryFields(DefineTestData.getScenario().getStoryType());
    }

    @Step("Create a Jira {issueType} with mandatory fields and verify it in Calibo")
    public void CreateNewUserStoryWithMandatoryFields(String issueType) {
        if (!java.util.Set.of("Story", "Task", "Epic", "Feature").contains(issueType)) {
            throw new IllegalArgumentException("Unsupported Jira issue type: " + issueType);
        }
        DefineData data = DefineTestData.getScenario();
        prepareStoryFeature(data);

        defineBuildingBlock.createNewUserStory(data, issueType);
        verifyCreatedStoryInCalibo();
    }

    @Step("Return to Calibo and verify the created Jira story in its sprint and feature slider")
    public void verifyCreatedStoryInCalibo() {
        defineBuildingBlock.returnToCaliboMainApplication();
        DefineData data = DefineTestData.getScenario();
        String featureName = executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName();
        defineBuildingBlock.verifyCreatedStoryInSprint(data);
        defineBuildingBlock.verifyCreatedStoryInFeatureSlider(featureName, data);
    }

    @Step("Create a product and feature, then add an existing Jira story")
    public void AddExistingUserStory() {
        DefineData data = DefineTestData.getScenario(DefineTestData.ADD_EXISTING_STORY);
        prepareStoryFeature(data);
        new pages.PRO.Features.Define.DefinePage(page).userStoriesTab(data.getUserStoriesTab()).click();
        defineBuildingBlock.addExistingUserStory(data);
    }

    @Step("Prepare the product integration and feature for a Define story scenario")
    private void prepareStoryFeature(DefineData data) {
        ProductData productData = ProTestData.getProduct("maturityAssesmentTeamLevelwithSprint",
                ProTestData.CREATE_PRODUCT);

        // Reuse the existing ProductTest flow for private product creation and mandatory validation.
        new ProductFlows(page, executionData).createPrivateProductWithMandatoryFields();
        configureProductStoryFields(data, productData);
        String featureName = ensureStoryFeature(data);

        new ProductFeatureTabPage(page).clickOnFeatureName(featureName).click();
        CommonMethods.waitForLoaderToDisappear(page);
        new DesignFeaturePage(page).selectPhase(data.getDefinePhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);

    }

    @Step("Configure Others tab fields on the created product")
    private void configureProductStoryFields(DefineData data, ProductData productData) {
        ProductBuildingBlock products = new ProductBuildingBlock(page, executionData);
        products.openProductSprintEditor(productData);
        ProductAdditionalDetailsOverviewTabPage tabs = new ProductAdditionalDetailsOverviewTabPage(page);
        tabs.tab(data.getProductDetails().getOthersTab()).click();
        ProductAdditionalDetailsOthersTabPage others = new ProductAdditionalDetailsOthersTabPage(page);

        String productCode = data.getProductDetails().getProductCode();
        others.productCode(data.getProductDetails().getProductCodeLabel()).fill(productCode);
        Assert.assertEquals(others.productCode(data.getProductDetails().getProductCodeLabel()).inputValue(),
                productCode, data.getValidationMessages().get("productCode"));

        NewReleasePage dropdowns = new NewReleasePage(page);
        Map<String, String> policyTagValues = new LinkedHashMap<>();
        data.getProductDetails().getDropdowns().forEach((label, value) -> {
            if (label.startsWith("Policy Template") || label.startsWith("Tags")) {
                policyTagValues.put(label, value);
            }
        });
        policyTagValues.forEach((label, value) -> {
            dropdowns.selectDropDown(label, value);
            Assert.assertTrue(others.selectedValue(label).innerText().contains(value),
                    "Selected value for " + label + " must match its configured value.");
        });
        products.saveEditedProductDetails(productData);

        String productName = executionData.getPrimaryProduct().getProductName();
        products.openProduct(productName, productData);

        // Use the ProductTest path for Agile key and Confluence values. Save them after
        // the template and tag so React's product update includes the integration fields.
        products.configureProductSprintDetails(productData);
        products.openProduct(productName, productData);
        products.reloadProductDetails(productData);
        products.openProductSprintEditor(productData);
        tabs.tab(data.getProductDetails().getOthersTab()).click();
        Map<String, String> persistedDropdownValues = new LinkedHashMap<>(
                productData.getSprintSetup().getProductDropdowns());
        persistedDropdownValues.putAll(policyTagValues);
        Assert.assertEquals(others.productCode(data.getProductDetails().getProductCodeLabel()).inputValue(),
                productCode, "Product code must persist after reopening the product.");
        persistedDropdownValues.forEach((label, value) ->
                Assert.assertTrue(others.selectedValue(label).innerText().contains(value),
                        "Product value for " + label + " must persist after reopening the product."));
        products.openProduct(productName, productData);
    }

    @Step("Reuse the configured feature or create one under the test product")
    private String ensureStoryFeature(DefineData data) {
        ProductBuildingBlock products = new ProductBuildingBlock(page, executionData);
        ProductData productData = ProTestData.getProduct("maturityAssesmentTeamLevelwithSprint",
                ProTestData.CREATE_PRODUCT);
        products.navigateToFeatureTab();
        CommonMethods.waitForLoaderToDisappear(page);

        String savedFeatureName = data.getFeatureName();
        ProductFeatureTabPage featureTab = new ProductFeatureTabPage(page);
        Locator savedFeature = savedFeatureName == null || savedFeatureName.isBlank()
                ? page.locator("[data-testid='feature-card-never-match']")
                : featureTab.matchingFeature(savedFeatureName);
        if (savedFeature.count() > 0) {
            FeatureBuildingBlock features = new FeatureBuildingBlock(page, executionData);
            if (executionData.getPrimaryProduct().getFeatures().stream()
                    .noneMatch(feature -> savedFeatureName.equals(feature.getFeatureName()))) {
                features.storeFeatureExecutionDataForProduct(savedFeatureName, executionData.getPrimaryProduct());
            }
            LoggerUtil.LOGGER.info("[DEFINE-FLOW] Reusing feature saved in Define data: {}", savedFeatureName);
            return savedFeatureName;
        }

        FeatureData featureData = data.getFeatureData();
        if (savedFeatureName != null && !savedFeatureName.isBlank()) {
            featureData.setTitle(savedFeatureName);
        }
        FeatureBuildingBlock features = new FeatureBuildingBlock(page, executionData);
        features.createFeatureUnderProduct(featureData, executionData.getPrimaryProduct());
        String createdFeatureName = features.featureName;
        features.skipOrAddFeatureAdditionalDetails(data.getFeatureDetailsAction());
        // QA does not show the "Go to Product" CTA after feature details are skipped.
        // Reopen through the normal product list, following the ProductTest flow.
        products.openProduct(executionData.getPrimaryProduct().getProductName(), productData);
        products.navigateToFeatureTab();
        Locator createdFeature = featureTab.matchingFeature(createdFeatureName).first();
        createdFeature.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                .setTimeout(data.getFeatureLookupTimeoutMs()));
        Assert.assertTrue(createdFeature.isVisible(), data.getValidationMessages().get("featureName"));
        return createdFeatureName;
    }

    @Step("Delete the created feature and product after recording the story")
    public void deleteDefineProductAndFeature() {
        if (executionData.getPrimaryProduct() == null) return;
        String featureName = executionData.getPrimaryProduct().getFeatures().isEmpty()
                ? null : executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName();
        ProductData cleanupData = ProTestData.getProduct("deleteProductWhenFeatureIsLinkedToProduct",
                ProTestData.CREATE_PRODUCT);
        ProductBuildingBlock products = new ProductBuildingBlock(page, executionData);
        products.openProduct(executionData.getPrimaryProduct().getProductName(), cleanupData);
        if (featureName != null) {
            products.navigateToFeatureTab();
            Locator feature = new ProductFeatureTabPage(page).matchingFeature(featureName).first();
            feature.waitFor(new Locator.WaitForOptions()
                    .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));
            feature.click();
            FeatureBuildingBlock features = new FeatureBuildingBlock(page, executionData);
            features.featureName = featureName;
            features.deleteFeature(cleanupData);
            ProValidation.validateSuccessMessage(page, cleanupData.getFeatureDeletedMessage());
            products.validateLinkedFeatureDeleted(featureName, cleanupData);
        }
        new ProductFlows(page, executionData).deleteProduct();
    }
}
