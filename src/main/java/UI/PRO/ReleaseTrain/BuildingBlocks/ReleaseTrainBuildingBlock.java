package UI.PRO.ReleaseTrain.BuildingBlocks;

import UI.PRO.datahelper.ReleaseTrainData;
import UI.PRO.datahelper.ProductData;
import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import pages.PRO.Features.Feature.FeatureDetailsPage;
import pages.PRO.Product.ProductFeatureTabPage;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.LandingPage;
import pages.PRO.Product.ProductDetailsPage;
import pages.PRO.ReleaseTrain.NewReleaseTrainPage;
import pages.PRO.ReleaseTrain.NewReleasePage;
import pages.PRO.ReleaseTrain.ProductReleasePage;
import pages.PRO.ReleaseTrain.ReleaseNotesPage;
import pages.PRO.ReleaseTrain.ReleaseTrainPage;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import testdatamanager.pro.ReleaseTrainTestData;
import utils.CommonMethods;
import utils.LoggerUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ReleaseTrainBuildingBlock {
    private static final double PRODUCT_NAVIGATION_TIMEOUT_MS = 15000;
    private final Page page;
    private final ProExecutionData executionData;
    private final LandingPage landingPage;
    private final ReleaseTrainPage releaseTrainPage;
    private final NewReleaseTrainPage newReleaseTrainPage;
    public String releaseTrainName;

    public ReleaseTrainBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        this.landingPage = new LandingPage(page);
        this.releaseTrainPage=new ReleaseTrainPage(page);
        this.newReleaseTrainPage=new NewReleaseTrainPage(page);

    }

    public void navigateToReleaseTrainPage() {
        page.waitForTimeout(2000);
        LoggerUtil.LOGGER.info("========== Navigating to Release Trains Page ==========");

        landingPage.hoverOnNavigationBar().click();

        landingPage.clickOnOptions("Release Trains").click();
    }

    public void createReleaseTrain(ReleaseTrainData releaseTrainData) {
        fillReleaseTrainDetails(releaseTrainData);
        newReleaseTrainPage.create().click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    public void fillReleaseTrainDetails(ReleaseTrainData releaseTrainData) {
        page.waitForTimeout(2000);
        LoggerUtil.LOGGER.info("========== Release Train Creation Started ==========");

        releaseTrainPage.addNewReleaseTrain().click();

        Assert.assertTrue(CommonMethods.pageHeader(page, "New Release Train"));

        String name = releaseTrainData.getName();
        this.releaseTrainName = name != null && !name.isBlank()
                ? name
                : CommonMethods.generateUniqueTitle("Automation Release Train");
        executionData.setReleaseTrainName(this.releaseTrainName);

        newReleaseTrainPage.name().fill(this.releaseTrainName);

        String description = releaseTrainData.getDescription();
        newReleaseTrainPage.description().fill(description != null && !description.isBlank()
                ? description
                : "Automation Release Train Description");
        page.waitForTimeout(2000);


    }

    public void searchReleaseTrain() {
        LoggerUtil.LOGGER.info("========== Searching for Release Train ==========");

        releaseTrainPage.searchReleaseTrain().fill(this.releaseTrainName);

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info("========== Release Train Found ==========");
    }

    @Step("Search for the created release train and verify the exact result")
    public void validateCreatedReleaseTrainSearch() {
        navigateToReleaseTrainPage();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(releaseTrainPage.searchReleaseTrain()).isVisible();
        searchReleaseTrain();
        Locator searchResult = releaseTrainPage.selectReleaseTrain(releaseTrainName);
        assertThat(searchResult).hasCount(1);
        assertThat(searchResult).containsText(releaseTrainName);
        assertThat(searchResult).isVisible();
    }

    public void viewReleaseTrain(){

        LoggerUtil.LOGGER.info("========== Viewing Release Train ==========");

        releaseTrainPage.selectReleaseTrain(this.releaseTrainName).click();

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info("========== Release Train Viewed ==========");
    }

    public void addNewRelease(){

        LoggerUtil.LOGGER.info("========== Adding New Release ==========");

        releaseTrainPage.addNewRelease().click();

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info("========== New Release Added ==========");
    }

    @Step("Create release train with all fields and validate saved name and description")
    public void createReleaseTrainWithAllFields(ReleaseTrainData data) {
        data.getReleaseTrain().setName(CommonMethods.generateUniqueTitle(data.getName()));
        navigateToReleaseTrainPage();
        fillReleaseTrainDetails(data);
        new NewReleasePage(page).selectDropDown(data.getTagsLabel(), data.getTag());
        assertThat(newReleaseTrainPage.name()).hasValue(data.getName());
        assertThat(newReleaseTrainPage.description()).hasValue(data.getDescription());
        newReleaseTrainPage.create().click();
        ProValidation.validateSuccessMessage(page, data.getTrainCreatedMessage());
        ReleaseTrainTestData.saveValues(data.getScenarioName(),
                Map.of("releaseTrainName", executionData.getReleaseTrainName()));
        searchReleaseTrain();
        assertThat(releaseTrainPage.selectReleaseTrain(executionData.getReleaseTrainName()))
                .containsText(executionData.getReleaseTrainName());
        assertThat(releaseTrainPage.savedTrainDescription(executionData.getReleaseTrainName()))
                .hasText(data.getDescription());
        viewReleaseTrain();
        assertThat(releaseTrainPage.savedTrainName()).hasText(executionData.getReleaseTrainName());
    }

    @Step("Add release with all fields to the created release train")
    public void addReleaseWithAllFields(ReleaseTrainData data) {
        addNewRelease();
        ReleaseTrainData.Release details = data.getReleases().getFirst();
        details.setReleaseName(CommonMethods.generateUniqueTitle(details.getReleaseName()));
        details.setVersion(CommonMethods.generateUniqueTitle(details.getVersion()));
        details.setReleaseId(CommonMethods.generateUniqueTitle(details.getReleaseId()));
        new ReleaseBuildingBlock(page, executionData).fillReleaseDetails(details);
        NewReleasePage releasePage = new NewReleasePage(page);
        data.getReleaseFields().forEach((label, value) -> {
            releasePage.field(label).fill(value);
            releasePage.field(label).press("Tab");
        });
        data.getReleaseDropdowns().forEach(releasePage::selectDropDown);
        assertThat(releasePage.name()).hasValue(details.getReleaseName());
        assertThat(releasePage.version()).hasValue(details.getVersion());
        assertThat(releasePage.releaseId()).hasValue(details.getReleaseId());
        assertThat(releasePage.releaseObjective()).hasValue(details.getReleaseObjective());
        data.getReleaseFields().forEach((label, value) -> assertThat(releasePage.field(label)).hasValue(value));
        releasePage.create().click();
        ProValidation.validateSuccessMessage(page, data.getReleaseCreatedMessage());
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "releaseName", executionData.getReleaseName(),
                "releaseVersion", details.getVersion(),
                "releaseId", details.getReleaseId()));
    }

    @Step("Validate saved release values in the right-side Details panel")
    public void validateSavedReleaseDetails(ReleaseTrainData data) {
        releaseTrainPage.viewReleaseDetails(data.getReleaseName(), data.getViewDetailsButton()).click();
        releaseTrainPage.releaseDetailsTab(data.getDetailsTab()).click();
        assertThat(releaseTrainPage.savedReleaseName()).hasText(data.getReleaseName());
        assertThat(releaseTrainPage.savedReleaseVersion()).hasText(data.getVersionLabel() + " " + data.getReleaseVersion());
        assertThat(releaseTrainPage.releaseDetailValue(data.getReleaseIdLabel())).hasText(data.getReleaseId());
        data.getExpectedReleaseDetails().forEach((label, value) ->
                assertThat(releaseTrainPage.releaseDetailValue(label)).hasText(value));
        assertThat(releaseTrainPage.savedReleaseStatus())
                .hasText(data.getReleaseDropdowns().get(data.getReleaseStatusLabel()));
    }

    @Step("Close release Details drawer")
    public void closeReleaseDetails() {
        Locator releaseTitle = releaseTrainPage.savedReleaseName();
        releaseTrainPage.closeReleaseDetails().click();
        assertThat(releaseTitle).isHidden();
        assertThat(releaseTrainPage.addNewRelease()).isEnabled();
    }

    @Step("Validate both saved releases belong to the created train and record their names")
    public void validateMultipleReleases(ReleaseTrainData data) {
        Assert.assertEquals(data.getCreatedReleases().size(), data.getExpectedReleaseCount(),
                data.getValidationMessages().get("releaseCount"));
        Assert.assertEquals(data.getCreatedReleases().stream().map(ReleaseTrainData.Release::getReleaseName)
                        .distinct().count(), (long) data.getExpectedReleaseCount(),
                data.getValidationMessages().get("distinctNames"));
        Assert.assertEquals(data.getCreatedReleases().stream().map(ReleaseTrainData.Release::getVersion)
                        .distinct().count(), (long) data.getExpectedReleaseCount(),
                data.getValidationMessages().get("distinctVersions"));
        Assert.assertEquals(data.getCreatedReleases().stream().map(ReleaseTrainData.Release::getReleaseId)
                        .distinct().count(), (long) data.getExpectedReleaseCount(),
                data.getValidationMessages().get("distinctIds"));
        assertThat(releaseTrainPage.savedTrainName()).hasText(data.getReleaseTrainName());
        assertThat(releaseTrainPage.releaseCards()).hasCount(data.getExpectedReleaseCount());
        data.getCreatedReleases().forEach(details ->
                assertThat(releaseTrainPage.viewReleaseDetails(details.getReleaseName(), data.getViewDetailsButton()))
                        .isVisible());
        String releaseNames = data.getCreatedReleases().stream().map(ReleaseTrainData.Release::getReleaseName)
                .collect(java.util.stream.Collectors.joining(", "));
        executionData.setReleaseName(releaseNames);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of("releaseNames", releaseNames));
    }

    @Step("Edit the created release name, manager, timeline and objective")
    public void editRelease(ReleaseTrainData data) {
        ReleaseTrainData.ReleaseEditData update = data.getReleaseUpdate();
        ReleaseTrainData.Release original = data.getReleases().getFirst();
        ProductReleasePage actions = new ProductReleasePage(page);
        Locator originalRelease = releaseTrainPage.viewReleaseDetails(data.getReleaseName(), data.getViewDetailsButton());
        actions.releaseActions(data.getReleaseName()).click();
        actions.releaseMenuAction(update.getEditAction()).click();
        assertThat(actions.title(update.getEditPageTitle())).isVisible();

        NewReleasePage editor = new NewReleasePage(page);
        assertThat(editor.name()).hasValue(data.getReleaseName());
        assertThat(editor.version()).hasValue(data.getReleaseVersion());
        assertThat(editor.releaseId()).hasValue(data.getReleaseId());
        assertThat(editor.releaseObjective()).hasValue(original.getReleaseObjective());
        assertThat(editor.dropdown(update.getManagerLabel())).containsText(original.getReleaseManager());
        assertThat(editor.field(update.getTimelineLabel()))
                .hasValue(data.getReleaseFields().get(update.getTimelineLabel()));

        String updatedName = CommonMethods.generateUniqueTitle(update.getNamePrefix());
        Assert.assertNotEquals(updatedName, data.getReleaseName(), data.getValidationMessages().get("updatedName"));
        Assert.assertNotEquals(update.getManager(), original.getReleaseManager(),
                data.getValidationMessages().get("updatedManager"));
        Assert.assertNotEquals(update.getTimeline(), data.getReleaseFields().get(update.getTimelineLabel()),
                data.getValidationMessages().get("updatedTimeline"));
        Assert.assertNotEquals(update.getObjective(), original.getReleaseObjective(),
                data.getValidationMessages().get("updatedObjective"));
        editor.name().fill(updatedName);
        editor.removeSelectedDropdownValue(update.getManagerLabel(), original.getReleaseManager()).click();
        editor.selectDropDown(update.getManagerLabel(), update.getManager());
        assertThat(editor.dropdown(update.getManagerLabel())).not().containsText(original.getReleaseManager());
        editor.field(update.getTimelineLabel()).fill(update.getTimeline());
        editor.field(update.getTimelineLabel()).press("Tab");
        editor.releaseObjective().fill(update.getObjective());
        assertThat(editor.name()).hasValue(updatedName);
        assertThat(editor.field(update.getTimelineLabel())).hasValue(update.getTimeline());
        assertThat(editor.releaseObjective()).hasValue(update.getObjective());
        assertThat(editor.version()).hasValue(data.getReleaseVersion());
        assertThat(editor.releaseId()).hasValue(data.getReleaseId());
        assertThat(actions.button(update.getSaveButton())).isEnabled();
        actions.button(update.getSaveButton()).click();
        ProValidation.validateSuccessMessage(page, update.getUpdatedMessage());
        assertThat(originalRelease).hasCount(0);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "originalReleaseName", data.getReleaseName(),
                "updatedReleaseName", updatedName,
                "releaseName", updatedName));
    }

    @Step("Validate persisted edited release details and record original and updated names")
    public void validateEditedReleaseDetails(ReleaseTrainData data) {
        assertThat(releaseTrainPage.savedTrainName()).hasText(data.getReleaseTrainName());
        assertThat(releaseTrainPage.releaseCards()).hasCount(data.getExpectedReleaseCount());
        data.getExpectedReleaseDetails().putAll(data.getReleaseUpdate().getExpectedReleaseDetails());
        validateSavedReleaseDetails(data);
        Assert.assertEquals(data.getReleaseName(), data.getUpdatedReleaseName(),
                data.getValidationMessages().get("updatedName"));
        String releaseNames = data.getOriginalReleaseName() + ", " + data.getUpdatedReleaseName();
        executionData.setReleaseName(releaseNames);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of("releaseNames", releaseNames));
    }

    @Step("Edit the created release train name and description")
    public void editReleaseTrain(ReleaseTrainData data) {
        ReleaseTrainData.ReleaseTrainEditData update = data.getReleaseTrainUpdate();
        ProductReleasePage actions = new ProductReleasePage(page);
        Locator originalTrain = releaseTrainPage.selectReleaseTrain(data.getReleaseTrainName());
        actions.releaseActions(data.getReleaseTrainName()).click();
        actions.releaseMenuAction(update.getEditAction()).click();
        assertThat(actions.title(update.getEditPageTitle())).isVisible();
        assertThat(newReleaseTrainPage.name()).hasValue(data.getReleaseTrainName());
        assertThat(newReleaseTrainPage.description()).hasValue(data.getDescription());
        assertThat(new NewReleasePage(page).dropdown(data.getTagsLabel())).containsText(data.getTag());

        String updatedName = CommonMethods.generateUniqueTitle(update.getNamePrefix());
        Assert.assertNotEquals(updatedName, data.getReleaseTrainName(),
                data.getValidationMessages().get("updatedTrainName"));
        Assert.assertNotEquals(update.getDescription(), data.getDescription(),
                data.getValidationMessages().get("updatedTrainDescription"));
        newReleaseTrainPage.name().fill(updatedName);
        newReleaseTrainPage.description().fill(update.getDescription());
        assertThat(newReleaseTrainPage.name()).hasValue(updatedName);
        assertThat(newReleaseTrainPage.description()).hasValue(update.getDescription());
        assertThat(actions.button(update.getSaveButton())).isEnabled();
        actions.button(update.getSaveButton()).click();
        ProValidation.validateSuccessMessage(page, update.getUpdatedMessage());
        assertThat(originalTrain).hasCount(0);
        releaseTrainName = updatedName;
        executionData.setReleaseTrainName(updatedName);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "originalReleaseTrainName", data.getReleaseTrainName(),
                "updatedReleaseTrainName", updatedName,
                "releaseTrainName", updatedName));
    }

    @Step("Verify edited train values after reload and record original and updated train names")
    public void validateEditedReleaseTrainDetails(ReleaseTrainData data) {
        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);
        searchReleaseTrain();
        assertThat(releaseTrainPage.selectReleaseTrain(data.getUpdatedReleaseTrainName()))
                .containsText(data.getUpdatedReleaseTrainName());
        assertThat(releaseTrainPage.savedTrainDescription(data.getUpdatedReleaseTrainName()))
                .hasText(data.getReleaseTrainUpdate().getDescription());
        viewReleaseTrain();
        assertThat(releaseTrainPage.savedTrainName()).hasText(data.getUpdatedReleaseTrainName());
        Assert.assertEquals(data.getReleaseTrainName(), data.getUpdatedReleaseTrainName(),
                data.getValidationMessages().get("updatedTrainName"));
        String trainNames = data.getOriginalReleaseTrainName() + ", " + data.getUpdatedReleaseTrainName();
        executionData.setReleaseTrainName(trainNames);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of("releaseTrainNames", trainNames));
    }

    @Step("Delete the created release and verify it remains absent after reload")
    public void deleteRelease(ReleaseTrainData data) {
        ReleaseTrainData.ReleaseDeleteData deletion = data.getReleaseDeletion();
        ProductReleasePage actions = new ProductReleasePage(page);
        Locator createdRelease = actions.release(data.getReleaseName());
        Locator releaseCards = releaseTrainPage.releaseCards();
        assertThat(releaseTrainPage.savedTrainName()).hasText(data.getReleaseTrainName());
        assertThat(createdRelease).isVisible();
        assertThat(releaseCards).hasCount(data.getExpectedReleaseCount());
        actions.releaseActions(data.getReleaseName()).click();
        actions.releaseMenuAction(deletion.getDeleteAction()).click();
        confirmDeletion(deletion);
        validateEmptyReleaseTrain(data);
        assertThat(createdRelease).hasCount(0);
        assertThat(releaseCards).hasCount(deletion.getExpectedReleaseCountAfterDelete());

        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);
        validateEmptyReleaseTrain(data);
        assertThat(createdRelease).hasCount(0);
        assertThat(releaseCards).hasCount(deletion.getExpectedReleaseCountAfterDelete());
    }

    @Step("Verify the created release train contains no releases")
    public void validateEmptyReleaseTrain(ReleaseTrainData data) {
        assertThat(releaseTrainPage.savedTrainName()).hasText(data.getReleaseTrainName());
        assertThat(releaseTrainPage.emptyState(data.getNoReleasesMessage())).hasText(data.getNoReleasesMessage());
        assertThat(releaseTrainPage.addNewRelease()).isEnabled();
    }

    @Step("Delete the empty release train and verify it remains absent after reload")
    public void deleteReleaseTrain(ReleaseTrainData data) {
        ReleaseTrainData.ReleaseDeleteData deletion = data.getReleaseTrainDeletion();
        ProductReleasePage actions = new ProductReleasePage(page);
        Locator createdTrain = releaseTrainPage.selectReleaseTrain(data.getReleaseTrainName());
        assertThat(createdTrain).isVisible();
        actions.releaseActions(data.getReleaseTrainName()).click();
        actions.releaseMenuAction(deletion.getDeleteAction()).click();
        confirmDeletion(deletion);
        assertThat(createdTrain).hasCount(0);

        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);
        searchReleaseTrain();
        assertThat(releaseTrainPage.searchReleaseTrain()).hasValue(data.getReleaseTrainName());
        assertThat(releaseTrainPage.emptyState(deletion.getEmptySearchMessage()))
                .hasText(deletion.getEmptySearchMessage());
        assertThat(createdTrain).hasCount(0);
    }

    @Step("Confirm deletion with the required comment and validate success")
    private void confirmDeletion(ReleaseTrainData.ReleaseDeleteData deletion) {
        ProductReleasePage actions = new ProductReleasePage(page);
        Locator confirmation = actions.title(deletion.getConfirmationMessage());
        assertThat(confirmation).hasText(deletion.getConfirmationMessage());
        Locator comments = actions.leaveComments(deletion.getCommentsPlaceholder());
        Locator deleteButton = actions.button(deletion.getDeleteButton());
        assertThat(comments).hasValue("");
        assertThat(deleteButton).isDisabled();
        comments.fill(deletion.getComments());
        assertThat(comments).hasValue(deletion.getComments());
        assertThat(deleteButton).isEnabled();
        deleteButton.click();
        ProValidation.validateSuccessMessage(page, deletion.getDeletedMessage());
        assertThat(confirmation).isHidden();
    }

    @Step("Submit the empty release train form and validate name and description errors")
    public void validateMandatoryFieldErrorsForReleaseTrain(ReleaseTrainData data) {
        releaseTrainPage.addNewReleaseTrain().click();
        Locator createButton = newReleaseTrainPage.create();
        String creationUrl = page.url();
        createButton.click();

        ProductReleasePage form = new ProductReleasePage(page);
        for (String message : data.getMandatoryFieldErrors()) {
            Locator error = form.mandatoryFieldError(message);
            assertThat(error).isVisible();
            assertThat(error).hasText(message);
        }
        assertThat(form.title(data.getCreatePageTitle())).isVisible();
        assertThat(newReleaseTrainPage.name()).hasValue(data.getName());
        assertThat(newReleaseTrainPage.description()).hasValue(data.getDescription());
        Assert.assertEquals(page.url(), creationUrl, data.getValidationMessages().get("creationPageUnchanged"));
        Assert.assertNull(executionData.getReleaseTrainName(), data.getValidationMessages().get("noEntitiesCreated"));
        Assert.assertNull(executionData.getReleaseName(), data.getValidationMessages().get("noEntitiesCreated"));
    }

    @Step("Submit the untouched release form and validate all required fields")
    public void validateMandatoryFieldErrorsForRelease(ReleaseTrainData data) {
        NewReleasePage form = new NewReleasePage(page);
        Locator createButton = form.create();
        String creationUrl = page.url();
        createButton.click();

        data.getMandatoryReleaseFieldErrors().forEach((label, message) -> {
            Locator error = form.mandatoryFieldError(label);
            assertThat(error).isVisible();
            assertThat(error).hasText(message);
        });
        assertThat(new ProductReleasePage(page).title(data.getCreatePageTitle())).isVisible();
        data.getReleaseFields().forEach((label, value) -> assertThat(form.field(label)).hasValue(value));
        Assert.assertEquals(page.url(), creationUrl, data.getValidationMessages().get("creationPageUnchanged"));
        Assert.assertEquals(executionData.getReleaseTrainName(), data.getReleaseTrainName(),
                data.getValidationMessages().get("createdTrain"));
        Assert.assertNull(executionData.getReleaseName(), data.getValidationMessages().get("noReleaseCreated"));
    }

    @Step("Record the train, release, portfolio and assigned product from the reused setup")
    public void recordAssignedProductDetails(ReleaseTrainData data) {
        Assert.assertEquals(executionData.getProducts().size(), data.getExpectedProductCount(),
                data.getValidationMessages().get("productCount"));
        Assert.assertNotNull(executionData.getPrimaryProduct(), data.getValidationMessages().get("recordedNames"));
        Map<String, String> names = new LinkedHashMap<>();
        names.put("releaseTrainName", executionData.getReleaseTrainName());
        names.put("releaseName", executionData.getReleaseName());
        names.put("portfolioName", executionData.getPortfolioName());
        names.put("productName", executionData.getPrimaryProduct().getProductName());
        names.forEach((key, value) -> {
            Assert.assertNotNull(value, data.getValidationMessages().get("recordedNames"));
            Assert.assertFalse(value.isBlank(), data.getValidationMessages().get("recordedNames"));
        });
        releaseTrainName = executionData.getReleaseTrainName();
        ReleaseTrainTestData.saveValues(data.getScenarioName(), names);
    }

    @Step("Assign a newly created feature to the joined release and verify its release tooltip")
    public void assignNewFeatureToRelease(ReleaseTrainData data) {
        String productReleaseName = executionData.getReleaseName();
        new FeatureFlows(page, executionData).createFeatureWithMandatoryFields(data.getFeatureData());
        String featureName = executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName();
        ProductFeatureTabPage features = new ProductFeatureTabPage(page);
        String featureListUrl = page.url();
        features.featureCardHeading(featureName).click();

        FeatureDetailsPage details = new FeatureDetailsPage(page);
        assertThat(details.featureName()).hasText(featureName);
        Locator editRelease = details.editFeatureRelease(data.getFeatureReleaseLabel());
        editRelease.hover();
        editRelease.click();
        details.featureReleaseDropdown(data.getFeatureReleaseLabel()).click();
        details.featureReleaseOption(productReleaseName).click();
        details.confirmFeatureRelease(data.getFeatureReleaseLabel()).click();
        ProValidation.validateSuccessMessage(page, data.getFeatureReleaseUpdatedMessage());
        ProValidation.validateSuccessMessage(page, data.getFeatureReleaseFixVersionMessage());
        page.navigate(featureListUrl);
        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);

        features.featureReleaseIcon(featureName).hover(new Locator.HoverOptions().setTimeout(PRODUCT_NAVIGATION_TIMEOUT_MS));
        Locator tooltip = features.featureReleaseTooltip(productReleaseName);
        assertThat(tooltip).containsText(data.getFeatureReleaseLabel() + ": " + productReleaseName);
        data.getExpectedFeatureReleaseDetails().forEach((label, value) ->
                assertThat(tooltip).containsText(label + ": " + value));

        // This scenario joins the product and assigns its feature to the same release.
        executionData.setReleaseName(String.join(", ", productReleaseName, productReleaseName));
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "releaseTrainName", executionData.getReleaseTrainName(),
                "releaseName", productReleaseName,
                "featureReleaseName", productReleaseName));
    }

    @Step("Add a release dependency and verify it was saved")
    public void verifyAddReleaseDependencies(ReleaseTrainData data) {
        String sourceRelease = executionData.getReleaseName();
        String trainName = executionData.getReleaseTrainName();
        String sourceProduct = executionData.getPrimaryProduct().getProductName();
        String dependencyProduct = executionData.getProducts().getLast().getProductName();
        ReleaseTrainData.ReleaseDependencyData dependency = data.getReleaseDependency();

        ProductData product = ProTestData.getProduct("addProductToRelease");
        product.setReleaseTrainName(trainName);
        product.setReleaseName(sourceRelease);
        ProductBuildingBlock products = new ProductBuildingBlock(page, executionData);
        products.joinProductRelease(product);
        products.openProduct(sourceProduct, product);

        ProductReleasePage productReleases = new ProductReleasePage(page);
        productReleases.tab(product.getProductRelease().getReleasesTab()).click();
        productReleases.release(sourceRelease).click();
        productReleases.title(sourceRelease).waitFor();
        productReleases.tab(dependency.getTab()).click();
        assertThat(productReleases.dependentOnPanel(dependency.getDependentOn())).isVisible();

        NewReleasePage fields = new NewReleasePage(page);
        fields.selectDropDown(dependency.getTrainField(), trainName);
        fields.selectDropDown(dependency.getReleaseField(), sourceRelease);
        fields.selectDropDown(dependency.getProductField(), dependencyProduct);
        productReleases.button(dependency.getAddButton()).click();
        ProValidation.validateSuccessMessage(page, dependency.getAddedMessage());
        assertThat(productReleases.dependencyEntry(dependencyProduct)).isVisible();

        String releaseNames = String.join(", ", sourceRelease, sourceRelease);
        executionData.setReleaseName(releaseNames);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "releaseTrainName", trainName, "sourceReleaseName", sourceRelease,
                "dependencyReleaseName", sourceRelease, "releaseNames", releaseNames,
                "sourceProductName", sourceProduct, "dependencyProductName", dependencyProduct));
    }

    @Step("Scroll to the generated train and release node in the dependency graph")
    public void verifyReleasesInDependencyGraph(ReleaseTrainData data) {
        navigateToReleaseTrainPage();
        double pageWidth = ((Number) page.evaluate("window.innerWidth")).doubleValue();
        page.mouse().move(pageWidth * 0.75, 90);
        releaseTrainPage.dependencyGraphTab(data.getDependencyGraphTab()).click();
        Locator dateRange = releaseTrainPage.dependencyGraphDateRange();
        dateRange.fill(data.getDependencyGraphDateRange());
        dateRange.press("Tab");
        assertThat(dateRange).hasValue(data.getDependencyGraphDateRange());
        CommonMethods.waitForLoaderToDisappear(page);

        Locator train = releaseTrainPage.dependencyGraphLabel(executionData.getReleaseTrainName());
        scrollDependencyGraphTo(train, false);
        train.scrollIntoViewIfNeeded();
        assertThat(train).isInViewport();

        // Both products are joined to the same release, so the graph has one release node.
        String releaseName = executionData.getReleaseName().split(", ", 2)[0];
        Locator releaseNode = releaseTrainPage.dependencyGraphReleaseNode(releaseName);
        scrollDependencyGraphTo(releaseNode, true);
        releaseNode.scrollIntoViewIfNeeded();
        assertThat(releaseNode).isInViewport();
        assertThat(releaseNode).hasText(releaseName);
    }

    @Step("Verify the selected release shows the saved dependency in both directions")
    public void verifyGraphReleaseRelationships(ReleaseTrainData data) {
        String releaseName = executionData.getReleaseName().split(", ", 2)[0];
        releaseTrainPage.dependencyGraphReleaseNode(releaseName).click();
        Locator dependentOn = releaseTrainPage.graphRelationshipTab(data.getGraphDependentOnLabel());
        Locator dependentReleases = releaseTrainPage.graphRelationshipTab(data.getGraphDependentReleasesLabel());
        assertThat(dependentOn).isVisible();
        assertThat(dependentReleases).isVisible();
        for (Locator tab : java.util.List.of(dependentOn, dependentReleases)) {
            tab.click();
            assertThat(tab).hasAttribute("aria-selected", "true");
            Locator relationship = releaseTrainPage.graphRelationshipRelease(releaseName);
            assertThat(relationship).isVisible();
            assertThat(relationship).hasText(releaseName);
        }
    }

    @Step("Verify the release is filtered out and restored after clearing the date range")
    public void verifyDependencyGraphDateFilter(ReleaseTrainData data) {
        String releaseName = executionData.getReleaseName().split(", ", 2)[0];
        Locator range = releaseTrainPage.dependencyGraphDateRange();
        Locator release = releaseTrainPage.dependencyGraphReleaseNode(releaseName);

        page.reload();
        String defaultRange = range.inputValue();
        range.click();
        String[] dates = data.getDependencyGraphExcludedDateRange().split(" - ", 2);
        selectDependencyGraphCalendarDate(dates[0]);
        // The picker can retain the previous range on the first click after opening.
        if (!range.inputValue().strip().equals(dates[0] + " -")) {
            selectDependencyGraphCalendarDate(dates[0]);
        }
        assertThat(range).hasValue(dates[0] + " - ");
        selectDependencyGraphCalendarDate(dates[1]);
        assertThat(range).hasValue(data.getDependencyGraphExcludedDateRange());
        assertThat(releaseTrainPage.dependencyGraphCanvas()).isHidden();

        range.clear();
        range.press("Tab");
        assertThat(range).hasValue("");
        page.reload();
        assertThat(range).hasValue(defaultRange);
        Locator train = releaseTrainPage.dependencyGraphLabel(executionData.getReleaseTrainName());
        scrollDependencyGraphTo(train, false);
        scrollDependencyGraphTo(release, true);
        assertThat(release).isInViewport();
    }

    private void selectDependencyGraphCalendarDate(String date) {
        var formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MMM-uuuu", java.util.Locale.ENGLISH);
        java.time.LocalDate day = java.time.LocalDate.parse(date, formatter);
        selectDependencyGraphCalendarField("year", Integer.toString(day.getYear()));
        selectDependencyGraphCalendarField("month", day.getMonth()
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH));
        releaseTrainPage.dependencyGraphCalendarDay(day).click();
    }

    private void selectDependencyGraphCalendarField(String field, String value) {
        Locator control = releaseTrainPage.dependencyGraphCalendarField(field);
        if (!control.innerText().strip().equals(value)) {
            control.click();
            new NewReleasePage(page).selectDropdownValue(value).click();
        }
    }

    private void scrollDependencyGraphTo(Locator label, boolean horizontal) {
        if (isDependencyGraphLabelInViewport(label)) {
            return;
        }
        Locator scrollbar = releaseTrainPage.dependencyGraphScrollbar(horizontal);
        Locator track = releaseTrainPage.dependencyGraphScrollbarTrack(scrollbar);
        Locator thumb = releaseTrainPage.dependencyGraphScrollbarThumb(track);
        if (horizontal) {
            var range = track.boundingBox();
            var slider = thumb.boundingBox();
            dragDependencyGraphScrollbar(thumb, range.x - slider.x - 1, true);
        }
        for (int attempt = 0; attempt < 80; attempt++) {
            var range = track.boundingBox();
            var slider = thumb.boundingBox();
            double remaining = horizontal
                    ? range.x + range.width - slider.x - slider.width
                    : range.y + range.height - slider.y - slider.height;
            if (remaining < 1) {
                break;
            }
            double length = horizontal ? slider.width : slider.height;
            double distance = Math.min(remaining + 1, Math.max(2, length * 0.8));
            dragDependencyGraphScrollbar(thumb, distance, horizontal);
            if (isDependencyGraphLabelInViewport(label)) {
                return;
            }
        }
        assertThat(label).isInViewport();
    }

    private void dragDependencyGraphScrollbar(Locator thumb, double distance, boolean horizontal) {
        var slider = thumb.boundingBox();
        double x = slider.x + slider.width / 2;
        double y = slider.y + slider.height / 2;
        page.mouse().move(x, y);
        page.mouse().down();
        try {
            page.mouse().move(x + (horizontal ? distance : 0), y + (horizontal ? 0 : distance),
                    new com.microsoft.playwright.Mouse.MoveOptions().setSteps(4));
        } finally {
            page.mouse().up();
        }
    }

    private boolean isDependencyGraphLabelInViewport(Locator label) {
        try {
            assertThat(label).isInViewport(
                    new com.microsoft.playwright.assertions.LocatorAssertions.IsInViewportOptions().setTimeout(300));
            return true;
        } catch (AssertionError outsideViewport) {
            return false;
        }
    }

    @Step("Delete and verify removal of the products created for the release dependency test")
    public void deleteReleaseDependencyProducts() {
        ProductData data = ProTestData.getProduct(ProTestData.CREATE_PRODUCT);
        ProductBuildingBlock products = new ProductBuildingBlock(page, executionData);
        for (var product : executionData.getProducts()) {
            String name = product.getProductName();
            products.openProduct(name, data);
            products.deleteProduct(name, data);
            ProValidation.validateSuccessMessage(page, data.getProductDeletedMessage());
            products.validateRecordedProductDeleted(name, data);
        }
    }

    @Step("Verify the assigned product under View Details > Products in the release")
    public void validateAssignedProductInRelease(ReleaseTrainData data) {
        assertThat(releaseTrainPage.savedTrainName()).hasText(data.getReleaseTrainName());
        releaseTrainPage.viewReleaseDetails(data.getReleaseName(), data.getViewDetailsButton()).click();
        assertThat(releaseTrainPage.savedReleaseName()).hasText(data.getReleaseName());
        Locator productsTab = releaseTrainPage.releaseDetailsTab(data.getProductsTab());
        productsTab.click();
        assertThat(productsTab).hasAttribute("aria-selected", "true");
        Locator portfolio = releaseTrainPage.assignedProductPortfolio(data.getPortfolioName());
        assertThat(portfolio).hasText(data.getPortfolioName());
        Locator products = releaseTrainPage.assignedProductNames(data.getPortfolioName());
        if (!products.first().isVisible()) {
            portfolio.click();
        }
        assertThat(products).hasCount(data.getExpectedProductCount());
        assertThat(products).hasText(new String[]{data.getProductName()});
        assertThat(products).isVisible();
    }

    @Step("Navigate from the release Products tab to the assigned product")
    public void navigateToAssignedProduct(ReleaseTrainData data) {
        navigateToReleaseTrainPage();
        searchReleaseTrain();
        viewReleaseTrain();
        validateAssignedProductInRelease(data);

        Locator productLink = releaseTrainPage.assignedProductNavigationControl(
                data.getPortfolioName(), data.getProductName());
        assertThat(productLink).isVisible();
        int pageCount = page.context().pages().size();
        productLink.click();
        page.waitForTimeout(1000);
        Page productPage = page.context().pages().size() > pageCount
                ? page.context().pages().getLast()
                : page;
        productPage.waitForLoadState();
        new ProductDetailsPage(productPage).waitForProductTitle(
                data.getProductName(), PRODUCT_NAVIGATION_TIMEOUT_MS);
        if (productPage != page) {
            productPage.close();
        }
    }

    @Step("Open View Release Notes and verify the saved product notes before and after reload")
    public void validateReleaseNotesAddedInProduct(ReleaseTrainData data) {
        assertThat(releaseTrainPage.savedTrainName()).hasText(data.getReleaseTrainName());
        ProductReleasePage actions = new ProductReleasePage(page);
        actions.releaseActions(data.getReleaseName()).click();
        actions.releaseMenuAction(data.getReleaseNotes().getViewAction()).click();
        validateSavedProductReleaseNotes(data);
        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);
        validateSavedProductReleaseNotes(data);
    }

    @Step("Validate the correct release and product display the exact saved release notes")
    private void validateSavedProductReleaseNotes(ReleaseTrainData data) {
        ReleaseTrainData.ReleaseNotesData expected = data.getReleaseNotes();
        ReleaseNotesPage notesPage = new ReleaseNotesPage(page);
        assertThat(notesPage.releaseName(expected.getReleaseLabel(), data.getReleaseName()))
                .hasText(expected.getReleaseLabel() + " " + data.getReleaseName());
        assertThat(new ProductReleasePage(page).title(expected.getPageTitle())).hasText(expected.getPageTitle());
        Locator product = notesPage.productName(data.getProductName());
        assertThat(product).hasText(data.getProductName());
        Locator notes = notesPage.productNotes(data.getProductName());
        if (!notes.isVisible()) {
            product.click();
        }
        assertThat(notes).isVisible();
        assertThat(notes).hasText(expected.getExpectedText());
    }
}
