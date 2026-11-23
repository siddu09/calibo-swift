package UI.PRO.ReleaseTrain.Flows;

import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ReleaseTrain.BuildingBlocks.ReleaseBuildingBlock;
import UI.PRO.ReleaseTrain.BuildingBlocks.ReleaseTrainBuildingBlock;
import UI.PRO.datahelper.ReleaseTrainData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ReleaseTrainTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.testng.Assert;

public class ReleaseTrainFlows {

    private final Page page;
    private final ProExecutionData executionData;
    private final ReleaseTrainBuildingBlock releaseTrain;
    private final ReleaseBuildingBlock release;


    public ReleaseTrainFlows(Page page, ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        releaseTrain = new ReleaseTrainBuildingBlock(page, executionData);
        release = new ReleaseBuildingBlock(page, executionData);
    }

    @Step("Create release train and add release")
    public void createReleaseTrainandAddRelease(ReleaseTrainData releaseTrainData) {

        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.createReleaseTrain(releaseTrainData);
        releaseTrain.searchReleaseTrain();
        releaseTrain.viewReleaseTrain();
        releaseTrain.addNewRelease();

        ReleaseTrainData.Release firstRelease = releaseTrainData != null && releaseTrainData.getReleases() != null
                && !releaseTrainData.getReleases().isEmpty()
                ? releaseTrainData.getReleases().get(0)
                : null;
        release.createRelease(firstRelease);

    }

    @Step("Create release train and release with all fields and validate saved details")
    public void CreatenewReleaseTrainAndaddReleaseWithAllfields() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario();
        releaseTrain.createReleaseTrainWithAllFields(data);
        releaseTrain.addReleaseWithAllFields(data);
        releaseTrain.validateSavedReleaseDetails(ReleaseTrainTestData.getScenario());
    }

    @Step("Create one release train with multiple releases and validate each release")
    public void CreatenewReleaseTrainAndAddMultipleReleases() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES);
        Assert.assertEquals(data.getReleases().size(), data.getExpectedReleaseCount(),
                data.getValidationMessages().get("releaseCount"));
        ReleaseTrainTestData.saveValues(ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES, Map.of(
                "releaseTrainName", "", "releaseName", "", "releaseVersion", "", "releaseId", "",
                "releaseNames", "", "createdReleases", List.of()));
        releaseTrain.createReleaseTrainWithAllFields(data);
        List<ReleaseTrainData.Release> createdReleases = new ArrayList<>();
        for (int index = 0; index < data.getExpectedReleaseCount(); index++) {
            if (index > 0) {
                releaseTrain.navigateToReleaseTrainPage();
                releaseTrain.searchReleaseTrain();
                releaseTrain.viewReleaseTrain();
            }
            data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES);
            data.getReleaseTrain().setReleases(List.of(data.getReleases().get(index)));
            releaseTrain.addReleaseWithAllFields(data);
            createdReleases.add(data.getReleases().getFirst());
            ReleaseTrainTestData.saveValues(ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES,
                    Map.of("createdReleases", createdReleases));
            releaseTrain.validateSavedReleaseDetails(
                    ReleaseTrainTestData.getScenario(ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES));
            releaseTrain.closeReleaseDetails();
        }
        releaseTrain.validateMultipleReleases(
                ReleaseTrainTestData.getScenario(ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES));
    }

    @Step("Create a release, edit it and verify the saved changes")
    public void editRelease() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE);
        ReleaseTrainTestData.saveValues(ReleaseTrainTestData.EDIT_RELEASE, Map.of(
                "releaseTrainName", "", "releaseName", "", "releaseVersion", "", "releaseId", "",
                "originalReleaseName", "", "updatedReleaseName", "", "releaseNames", ""));
        releaseTrain.createReleaseTrainWithAllFields(data);
        releaseTrain.addReleaseWithAllFields(data);
        data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE);
        releaseTrain.validateSavedReleaseDetails(data);
        releaseTrain.closeReleaseDetails();
        releaseTrain.editRelease(data);
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.searchReleaseTrain();
        releaseTrain.viewReleaseTrain();
        releaseTrain.validateEditedReleaseDetails(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE));
    }

    @Step("Create a release train, edit its name and description, and verify saved changes")
    public void editReleaseTrain() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE_TRAIN);
        ReleaseTrainTestData.saveValues(ReleaseTrainTestData.EDIT_RELEASE_TRAIN, Map.of(
                "releaseTrainName", "", "originalReleaseTrainName", "",
                "updatedReleaseTrainName", "", "releaseTrainNames", ""));
        releaseTrain.createReleaseTrainWithAllFields(data);
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.searchReleaseTrain();
        releaseTrain.editReleaseTrain(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE_TRAIN));
        releaseTrain.validateEditedReleaseTrainDetails(
                ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE_TRAIN));
    }

    @Step("Create a release, delete it with a comment and verify persisted removal")
    public void deleteRelease() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.DELETE_RELEASE);
        ReleaseTrainTestData.saveValues(ReleaseTrainTestData.DELETE_RELEASE, Map.of(
                "releaseTrainName", "", "releaseName", "", "releaseVersion", "", "releaseId", ""));
        releaseTrain.createReleaseTrainWithAllFields(data);
        releaseTrain.addReleaseWithAllFields(data);
        data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.DELETE_RELEASE);
        releaseTrain.validateSavedReleaseDetails(data);
        releaseTrain.closeReleaseDetails();
        releaseTrain.deleteRelease(data);
    }

    @Step("Create and delete a release train without adding any releases")
    public void deleteReleaseTrain() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.DELETE_RELEASE_TRAIN);
        Assert.assertTrue(data.getReleases().isEmpty(), data.getValidationMessages().get("trainOnly"));
        ReleaseTrainTestData.saveValues(ReleaseTrainTestData.DELETE_RELEASE_TRAIN, Map.of(
                "releaseTrainName", "", "releaseName", "", "releaseVersion", "", "releaseId", ""));
        releaseTrain.createReleaseTrainWithAllFields(data);
        data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.DELETE_RELEASE_TRAIN);
        releaseTrain.validateEmptyReleaseTrain(data);
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.searchReleaseTrain();
        releaseTrain.deleteReleaseTrain(data);
    }

    @Step("Validate required release train fields without creating a train")
    public void validateMandatoryFieldErrorsForReleaseTrain() {
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.validateMandatoryFieldErrorsForReleaseTrain(
                ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE_TRAIN));
    }

    @Step("Create a train and validate release mandatory fields without creating a release")
    public void validateMandatoryFieldErrorsForRelease() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "releaseTrainName", "", "releaseName", "", "releaseVersion", "", "releaseId", ""));
        releaseTrain.createReleaseTrainWithAllFields(data);
        data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE);
        releaseTrain.validateEmptyReleaseTrain(data);
        releaseTrain.addNewRelease();
        releaseTrain.validateMandatoryFieldErrorsForRelease(data);
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.searchReleaseTrain();
        releaseTrain.viewReleaseTrain();
        releaseTrain.validateEmptyReleaseTrain(data);
    }

    @Step("Assign a product using the existing flow and verify it from the release Products tab")
    public void validationOfProductAssignedToreleaseInReleasePage(ProductFlows productFlows) {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_PRODUCT_ASSIGNED_TO_RELEASE_IN_RELEASE_PAGE);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "releaseTrainName", "", "releaseName", "", "portfolioName", "", "productName", ""));
        productFlows.addProductToRelease();
        releaseTrain.recordAssignedProductDetails(data);
        data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_PRODUCT_ASSIGNED_TO_RELEASE_IN_RELEASE_PAGE);
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.searchReleaseTrain();
        releaseTrain.viewReleaseTrain();
        releaseTrain.validateAssignedProductInRelease(data);
        releaseTrain.closeReleaseDetails();
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.searchReleaseTrain();
        releaseTrain.viewReleaseTrain();
        releaseTrain.validateAssignedProductInRelease(data);
    }

    @Step("Reuse product release assignment and validate the product's saved release notes")
    public void ValidateReleaseNotesAddedINProduct(ProductFlows productFlows) {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_RELEASE_NOTES_ADDED_IN_PRODUCT);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "releaseTrainName", "", "releaseName", "", "portfolioName", "", "productName", ""));
        productFlows.addProductToRelease();
        releaseTrain.recordAssignedProductDetails(data);
        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.searchReleaseTrain();
        releaseTrain.viewReleaseTrain();
        releaseTrain.validateReleaseNotesAddedInProduct(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_RELEASE_NOTES_ADDED_IN_PRODUCT));
    }

    @Step("Reuse product release assignment and record the generated names")
    public void addProductToRelease(ProductFlows productFlows) {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.ADD_PRODUCT_TO_RELEASE);
        ReleaseTrainTestData.saveValues(data.getScenarioName(), Map.of(
                "releaseTrainName", "", "releaseName", "", "portfolioName", "", "productName", ""));
        productFlows.addProductToRelease();
        releaseTrain.recordAssignedProductDetails(data);
    }

    @Step("Join a private product to a release and assign its new feature to the release")
    public void addFeatureToRelease(ProductFlows productFlows) {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.ADD_FEATURE_TO_RELEASE);
        addProductToRelease(productFlows);
        productFlows.navigateToFeatureTab();
        releaseTrain.assignNewFeatureToRelease(data);
    }

    @Step("Create an assigned product and navigate to it from the release")
    public void navigateToProductFromReleasePage(ProductFlows productFlows) {
        addProductToRelease(productFlows);
        releaseTrain.navigateToAssignedProduct(
                ReleaseTrainTestData.getScenario(ReleaseTrainTestData.ADD_PRODUCT_TO_RELEASE));
    }

    @Step("Verify navigation to the assigned product")
    public void verifyNavigationFromReleaseToProduct() {
        ProductFlows productFlows = new ProductFlows(page, executionData);
        navigateToProductFromReleasePage(productFlows);
    }

    @Step("Join a product to a release and verify adding a release dependency")
    public void verifyAddReleaseDependencies() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VERIFY_ADD_RELEASE_DEPENDENCIES);
        ProductFlows productFlows = new ProductFlows(page, executionData);
        addProductToRelease(productFlows);
        productFlows.createPrivateProductWithMandatoryFields();
        releaseTrain.verifyAddReleaseDependencies(data);
    }

    @Step("Save a release dependency and verify its release in the graph")
    public void verifyReleasesInDependencyGraph() {
        ReleaseTrainData data = ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VERIFY_RELEASES_IN_DEPENDENCY_GRAPH);
        verifyAddReleaseDependencies();
        releaseTrain.verifyReleasesInDependencyGraph(data);
    }

    @Step("Select the release in the dependency graph and verify its relationships")
    public void verifyDependsOnAndDependentReleasesOnSelectingRelease() {
        verifyReleasesInDependencyGraph();
        releaseTrain.verifyGraphReleaseRelationships(
                ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VERIFY_RELEASE_RELATIONSHIPS));
    }

    @Step("Apply and clear the release date filter in the dependency graph")
    public void verifyReleaseDateFilterAppliedSuccessfullyOnDependencies() {
        verifyReleasesInDependencyGraph();
        releaseTrain.verifyDependencyGraphDateFilter(ReleaseTrainTestData.getScenario(
                ReleaseTrainTestData.VERIFY_RELEASE_DATE_FILTER_APPLIED_SUCCESSFULLY_ON_DEPENDENCIES));
    }

    public void deleteReleaseDependencyProducts() {
        releaseTrain.deleteReleaseDependencyProducts();
    }

    @Step("Create and search for a release train from the Release Trains page")
    public void searchReleaseTrainFromReleaseTrainPage() {
        CreatenewReleaseTrainAndaddReleaseWithAllfields();
        releaseTrain.validateCreatedReleaseTrainSearch();
    }
}
