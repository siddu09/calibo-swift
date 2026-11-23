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

    @Step("Create user feedback with all mandatory fields without Confluence")
    public void createUserFeedbackWithAllMandatoryFieldsWithoutConfluence() {
        DefineData data = DefineTestData.getScenario(
                DefineTestData.CREATE_USER_FEEDBACK_WITHOUT_CONFLUENCE);
        createAndOpenUserFeedback(data);
    }

    @Step("Verify mandatory fields prevent submitting empty User Feedback")
    public void verifyMandatoryFieldValidationForUserFeedback() {
        DefineData data = DefineTestData.getScenario(DefineTestData.CREATE_USER_FEEDBACK_WITHOUT_CONFLUENCE);
        prepareUserFeedbackFeature(data);
        defineBuildingBlock.submitEmptyUserFeedbackForm();
        DefineData validation = DefineTestData.getScenario(
                DefineTestData.VERIFY_MANDATORY_FIELD_VALIDATION_FOR_USER_FEEDBACK);
        DefineValidation.validateMandatoryFieldErrors(page, validation);
    }

    @Step("Verify mandatory fields prevent submitting an empty Business Requirement")
    public void verifyMandatoryFieldValidationForBusinessRequirement() {
        DefineData data = DefineTestData.getScenario(DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITHOUT_CONFLUENCE);
        DefineData validation = DefineTestData.getScenario(
                DefineTestData.VERIFY_MANDATORY_FIELD_VALIDATION_FOR_BUSINESS_REQUIREMENT);
        prepareUserFeedbackFeature(data);
        defineBuildingBlock.submitEmptyBusinessRequirementForm(data);
        DefineValidation.validateMandatoryFieldErrors(page, validation);
    }

    private String createAndOpenUserFeedback(DefineData data) {
        createUserFeedback(data);
        String requirementsListUrl = page.url();
        defineBuildingBlock.viewBusinessRequirement();
        DefineValidation.validateUserFeedbackDetails(page, executionData, data);
        return requirementsListUrl;
    }

    private void createUserFeedback(DefineData data) {
        prepareUserFeedbackFeature(data);
        defineBuildingBlock.captureUserFeedbackExecutionIds(data);
        defineBuildingBlock.createUserFeedbackWithAllMandatoryFields(data);
        ProValidation.validateSuccessMessage(page, data.getFeedbackSavedMessage());
        DefineValidation.validateUserFeedbackExecutionData(executionData, data);
        DefineValidation.validateUserFeedbackUnderCurrentFeature(page, executionData, data);
    }

    @Step("Create and delete User Feedback, then verify the deletion message")
    public void deleteUserFeedback() {
        DefineData feedback = DefineTestData.getScenario(DefineTestData.CREATE_USER_FEEDBACK_WITHOUT_CONFLUENCE);
        DefineData deletion = DefineTestData.getScenario(DefineTestData.DELETE_USER_FEEDBACK);
        createUserFeedback(feedback);
        defineBuildingBlock.deleteDefineItem(deletion);
        ProValidation.validateSuccessMessage(page, deletion.getFeedbackSavedMessage());
    }

    @Step("Create and delete a Business Requirement, then verify the deletion message")
    public void deleteBusinessRequirement() {
        DefineData requirement = DefineTestData.getScenario(DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITHOUT_CONFLUENCE);
        DefineData deletion = DefineTestData.getScenario(DefineTestData.DELETE_BUSINESS_REQUIREMENT);
        createBusinessRequirement(requirement);
        defineBuildingBlock.deleteDefineItem(deletion);
        ProValidation.validateSuccessMessage(page, deletion.getFeedbackSavedMessage());
    }

    @Step("Edit User Feedback and verify the saved title and description")
    public void editUserFeedback() {
        DefineData feedback = DefineTestData.getScenario(DefineTestData.CREATE_USER_FEEDBACK_WITHOUT_CONFLUENCE);
        createUserFeedback(feedback);
        editAndVerifyDefineItem(DefineTestData.EDIT_USER_FEEDBACK);
    }

    @Step("Edit a Business Requirement and verify the saved title and description")
    public void editBusinessRequirement() {
        DefineData requirement = DefineTestData.getScenario(DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITHOUT_CONFLUENCE);
        createBusinessRequirement(requirement);
        editAndVerifyDefineItem(DefineTestData.EDIT_BUSINESS_REQUIREMENT);
    }

    private void editAndVerifyDefineItem(String scenario) {
        DefineData edit = DefineTestData.getScenario(scenario);
        String requirementsListUrl = page.url();
        String updatedTitle = defineBuildingBlock.editDefineItem(edit);
        ProValidation.validateSuccessMessage(page, edit.getFeedbackSavedMessage());
        defineBuildingBlock.reopenEditedDefineItem(edit, requirementsListUrl, updatedTitle);
        DefineValidation.validateEditedDefineItem(page, updatedTitle, edit);
        executionData.setCreatedStoryId(updatedTitle);
    }

    @Step("Convert User Feedback and verify the resulting Business Requirement")
    public void convertUserFeedbackToBusinessRequirement() {
        DefineData feedback = DefineTestData.getScenario(DefineTestData.CREATE_USER_FEEDBACK_WITHOUT_CONFLUENCE);
        DefineData conversion = DefineTestData.getScenario(DefineTestData.CONVERT_USER_FEEDBACK_TO_BUSINESS_REQUIREMENT);
        String requirementsListUrl = createAndOpenUserFeedback(feedback);
        defineBuildingBlock.convertUserFeedbackToBusinessRequirement(conversion);
        ProValidation.validateSuccessMessage(page, conversion.getFeedbackSavedMessage());
        defineBuildingBlock.searchForConvertedBusinessRequirement(conversion, requirementsListUrl);
        DefineValidation.validateFeedbackAndConvertedRequirementSearchResults(page, executionData);
    }

    @Step("Create User Feedback, then add and validate its comment and review")
    public void addCommentsAndReviewsToExistingUserFeedback() {
        createUserFeedbackWithAllMandatoryFieldsWithoutConfluence();
        DefineData data = DefineTestData.getScenario(
                DefineTestData.ADD_COMMENTS_AND_REVIEWS_TO_EXISTING_USER_FEEDBACK);
        defineBuildingBlock.openCreatedUserFeedbackFromSearch(data);
        defineBuildingBlock.addCommentToUserFeedback(data);
        DefineValidation.validateUserFeedbackComment(page, data);
        defineBuildingBlock.addReviewToUserFeedback(data);
        DefineValidation.validateUserFeedbackReview(page, data);
    }

    @Step("Create a Business Requirement, then add and validate its comment and review")
    public void addCommentsAndReviewsToExistingBusinessRequirement() {
        createBusinessRequirementWithAllMandatoryFieldsWithoutConfluence();
        DefineData data = DefineTestData.getScenario(
                DefineTestData.ADD_COMMENTS_AND_REVIEWS_TO_EXISTING_BUSINESS_REQUIREMENT);
        defineBuildingBlock.openCreatedUserFeedbackFromSearch(data);
        defineBuildingBlock.addCommentToUserFeedback(data);
        DefineValidation.validateUserFeedbackComment(page, data);
        defineBuildingBlock.addReviewToUserFeedback(data);
        DefineValidation.validateUserFeedbackReview(page, data);
    }

    @Step("Create user feedback with all mandatory fields and Confluence")
    public void createUserFeedbackWithAllMandatoryFieldsWithConfluence() {
        DefineData data = DefineTestData.getScenario(
                DefineTestData.CREATE_USER_FEEDBACK_WITH_CONFLUENCE);
        prepareStoryFeature(data);
        defineBuildingBlock.captureUserFeedbackExecutionIds(data);
        defineBuildingBlock.createUserFeedbackWithAllMandatoryFieldsAndConfluence(data);
        ProValidation.validateSuccessMessage(page, data.getFeedbackSavedMessage());
        DefineValidation.validateUserFeedbackExecutionData(executionData, data);
        DefineValidation.validateUserFeedbackUnderCurrentFeature(page, executionData, data);
        defineBuildingBlock.viewBusinessRequirement();
        DefineValidation.validateUserFeedbackDetails(page, executionData, data);
    }

    @Step("Import a Business Requirement from CSV and verify the upload message")
    public void importNewBusinessRequirement() {
        DefineData setup = DefineTestData.getScenario(DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITHOUT_CONFLUENCE);
        DefineData data = DefineTestData.getScenario(DefineTestData.IMPORT_NEW_BUSINESS_REQUIREMENT);
        prepareUserFeedbackFeature(setup);
        defineBuildingBlock.importNewBusinessRequirement(data);
        ProValidation.validateSuccessMessage(page, data.getFeedbackSavedMessage());
    }

    @Step("Create a Business Requirement with all mandatory fields without Confluence")
    public void createBusinessRequirementWithAllMandatoryFieldsWithoutConfluence() {
        DefineData data = DefineTestData.getScenario(
                DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITHOUT_CONFLUENCE);
        createBusinessRequirement(data);
        defineBuildingBlock.viewBusinessRequirement();
        DefineValidation.validateUserFeedbackDetails(page, executionData, data);
    }

    private void createBusinessRequirement(DefineData data) {
        prepareUserFeedbackFeature(data);
        defineBuildingBlock.captureUserFeedbackExecutionIds(data);
        defineBuildingBlock.createBusinessRequirementWithAllMandatoryFields(data);
        ProValidation.validateSuccessMessage(page, data.getFeedbackSavedMessage());
        DefineValidation.validateUserFeedbackExecutionData(executionData, data);
        DefineValidation.validateUserFeedbackUnderCurrentFeature(page, executionData, data);
    }

    @Step("Create a Business Requirement with all mandatory fields and Confluence")
    public void createBusinessRequirementWithAllMandatoryFieldsWithConfluence() {
        DefineData data = DefineTestData.getScenario(
                DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITH_CONFLUENCE);
        prepareStoryFeature(data);
        defineBuildingBlock.captureUserFeedbackExecutionIds(data);
        defineBuildingBlock.createBusinessRequirementWithAllMandatoryFieldsAndConfluence(data);
        ProValidation.validateSuccessMessage(page, data.getFeedbackSavedMessage());
        DefineValidation.validateUserFeedbackExecutionData(executionData, data);
        DefineValidation.validateUserFeedbackUnderCurrentFeature(page, executionData, data);
        defineBuildingBlock.viewBusinessRequirement();
        DefineValidation.validateUserFeedbackDetails(page, executionData, data);
        DefineValidation.validateBusinessRequirementConfluenceLink(page, data);
    }

    private void prepareUserFeedbackFeature(DefineData data) {
        new ProductFlows(page, executionData).createPrivateProductWithMandatoryFields();
        String featureName = ensureStoryFeature(data);
        new ProductFeatureTabPage(page).clickOnFeatureName(featureName).click();
        CommonMethods.waitForLoaderToDisappear(page);
        new DesignFeaturePage(page).selectPhase(data.getDefinePhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
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

    @Step("Create a Jira story, remove its mapping, and verify both notifications")
    public void removeMappedUserStory() {
        DefineData data = DefineTestData.getScenario(DefineTestData.REMOVE_MAPPED_USER_STORY);
        CreateNewUserStoryWithMandatoryFields();
        defineBuildingBlock.removeMappedUserStory(data);
        ProValidation.validateSuccessMessage(page, data.getRemovedStoryMessage().formatted(executionData.getCreatedStoryId()));
        ProValidation.validateSuccessMessage(page, data.getFixVersionMessage());
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
        openProductOthersTab(tabs, data);
        ProductAdditionalDetailsOthersTabPage others = new ProductAdditionalDetailsOthersTabPage(page);

        String productCode = data.getProductDetails().getProductCode();
        Locator productCodeInput = others.productCode(data.getProductDetails().getProductCodeLabel());
        productCodeInput.fill(productCode);
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

        // Verify the first save before making further edits. Save integration fields in
        // their own edit so changing them cannot silently drop the product code in UAT.
        products.openProductSprintEditor(productData);
        openProductOthersTab(tabs, data);
        productCodeInput = others.productCode(data.getProductDetails().getProductCodeLabel());
        if (!productCode.equals(productCodeInput.inputValue())) {
            // UAT occasionally acknowledges the update before the optional code has
            // reached the persisted product. Commit that field on its own and reload
            // the editor before continuing with integration settings.
            productCodeInput.fill(productCode);
            products.saveEditedProductDetails(productData);
            products.openProduct(productName, productData);
            products.openProductSprintEditor(productData);
            openProductOthersTab(tabs, data);
        }
        Assert.assertEquals(others.productCode(data.getProductDetails().getProductCodeLabel()).inputValue(),
                productCode, "Product code must persist after the first product save.");

        NewReleasePage sprintDropdowns = new NewReleasePage(page);
        productData.getSprintSetup().getProductDropdowns().forEach(sprintDropdowns::selectDropDown);
        products.saveEditedProductDetails(productData);

        products.openProduct(productName, productData);
        products.reloadProductDetails(productData);
        products.openProductSprintEditor(productData);
        openProductOthersTab(tabs, data);

        // Reapply the code as a dedicated update after integrations are committed. The
        // UAT product editor can omit an untouched optional field from an integration
        // update, so explicitly saving it last prevents the value from being cleared.
        productCodeInput = others.productCode(data.getProductDetails().getProductCodeLabel());
        if (!productCode.equals(productCodeInput.inputValue())) {
            productCodeInput.fill(productCode);
            products.saveEditedProductDetails(productData);
            products.openProduct(productName, productData);
            products.reloadProductDetails(productData);
            products.openProductSprintEditor(productData);
            openProductOthersTab(tabs, data);
        }

        Map<String, String> persistedDropdownValues = new LinkedHashMap<>(
                productData.getSprintSetup().getProductDropdowns());
        persistedDropdownValues.putAll(policyTagValues);
        Assert.assertEquals(others.productCode(data.getProductDetails().getProductCodeLabel()).inputValue(),
                productCode, "Product code must persist after reopening the product.");
        persistedDropdownValues.forEach((label, value) ->
                com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(others.selectedValue(label))
                        .containsText(value, new com.microsoft.playwright.assertions.LocatorAssertions.ContainsTextOptions()
                                .setTimeout(data.getPageTimeoutMs())));
        products.openProduct(productName, productData);
    }

    private void openProductOthersTab(ProductAdditionalDetailsOverviewTabPage tabs, DefineData data) {
        tabs.tab(data.getProductDetails().getOthersTab()).click();
        // The UAT edit form can render its optional integration fields before the
        // product details request has populated them. Wait for that load to finish
        // before reading or changing Product Code.
        CommonMethods.waitForLoaderToDisappear(page);
        new ProductAdditionalDetailsOthersTabPage(page)
                .productCode(data.getProductDetails().getProductCodeLabel())
                .waitFor(new Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
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
