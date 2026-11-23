package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ReleaseTrain.Flows.ReleaseTrainFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ReleaseTrainTestData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;

public class ReleaseTrainTest extends BaseUITest {
    private ReleaseTrainFlows releaseTrainFlows;
    private ProductFlows productFlows;
    private ProExecutionData executionData;

    @BeforeMethod(alwaysRun = true)
    @Step("Login and initialize Release Train flows")
    public void setUp() {
        new LoginBuildingBlock(page).proLogin();
        executionData = new ProExecutionData();
        releaseTrainFlows = new ReleaseTrainFlows(page, executionData);
        productFlows = new ProductFlows(page, executionData);
    }

    @Step("Create Release Train and Release with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void CreatenewReleaseTrainAndaddReleaseWithAllfields() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting CreatenewReleaseTrainAndaddReleaseWithAllfields");
        releaseTrainFlows.CreatenewReleaseTrainAndaddReleaseWithAllfields();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(), ReleaseTrainTestData.CREATE_RELEASE_TRAIN, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, release={}", executionData.getReleaseTrainName(), executionData.getReleaseName());
    }

    @Step("Create Release Train and Add Multiple Releases")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void CreatenewReleaseTrainAndAddMultipleReleases() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting CreatenewReleaseTrainAndAddMultipleReleases");
        releaseTrainFlows.CreatenewReleaseTrainAndAddMultipleReleases();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES), ReleaseTrainTestData.CREATE_MULTIPLE_RELEASES, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, releases={}", executionData.getReleaseTrainName(), executionData.getReleaseName());
    }

    @Step("Edit Release")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void EditRelease() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting EditRelease");
        releaseTrainFlows.editRelease();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE), ReleaseTrainTestData.EDIT_RELEASE, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, releases={}", executionData.getReleaseTrainName(), executionData.getReleaseName());
    }

    @Step("Edit Release Train")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void EditReleaseTrain() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting EditReleaseTrain");
        releaseTrainFlows.editReleaseTrain();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.EDIT_RELEASE_TRAIN), ReleaseTrainTestData.EDIT_RELEASE_TRAIN, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: trainNames={}", executionData.getReleaseTrainName());
    }

    @Step("Delete Release")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void DeleteRelease() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting DeleteRelease");
        releaseTrainFlows.deleteRelease();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.DELETE_RELEASE), ReleaseTrainTestData.DELETE_RELEASE, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, deletedRelease={}", executionData.getReleaseTrainName(), executionData.getReleaseName());
    }

    @Step("Delete Release Train")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void DeleteReleaseTrain() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting DeleteReleaseTrain");
        releaseTrainFlows.deleteReleaseTrain();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.DELETE_RELEASE_TRAIN), ReleaseTrainTestData.DELETE_RELEASE_TRAIN, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: deletedTrain={}", executionData.getReleaseTrainName());
    }

    @Step("Validate Mandatory Field Errors For Release Train")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void validateMandatoryFieldErrorsForReleaseTrain() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting validateMandatoryFieldErrorsForReleaseTrain");
        releaseTrainFlows.validateMandatoryFieldErrorsForReleaseTrain();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE_TRAIN), ReleaseTrainTestData.VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE_TRAIN, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: required-field errors displayed; no train or release created");
    }

    @Step("Validate Mandatory Field Errors For Release")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void validateMandatoryFieldErrorsForRelease() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting validateMandatoryFieldErrorsForRelease");
        releaseTrainFlows.validateMandatoryFieldErrorsForRelease();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE), ReleaseTrainTestData.VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, required-field errors displayed; no release created", executionData.getReleaseTrainName());
    }

    @Step("Validate Product Assigned to Release in Release Page")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void validationOfProductAssignedToreleaseInReleasePage() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting validationOfProductAssignedToreleaseInReleasePage");
        releaseTrainFlows.validationOfProductAssignedToreleaseInReleasePage(new ProductFlows(page, executionData));
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_PRODUCT_ASSIGNED_TO_RELEASE_IN_RELEASE_PAGE), ReleaseTrainTestData.VALIDATE_PRODUCT_ASSIGNED_TO_RELEASE_IN_RELEASE_PAGE, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, release={}, assignedProduct={}", executionData.getReleaseTrainName(), executionData.getReleaseName(), executionData.getPrimaryProduct().getProductName());
    }

    @Step("Validate Release Notes Added in Product")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void ValidateReleaseNotesAddedINProduct() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting ValidateReleaseNotesAddedINProduct");
        releaseTrainFlows.ValidateReleaseNotesAddedINProduct(new ProductFlows(page, executionData));
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VALIDATE_RELEASE_NOTES_ADDED_IN_PRODUCT), ReleaseTrainTestData.VALIDATE_RELEASE_NOTES_ADDED_IN_PRODUCT, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, release={}, assignedProduct={}", executionData.getReleaseTrainName(), executionData.getReleaseName(), executionData.getPrimaryProduct().getProductName());
    }

    @Step("Add Product to Release")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void AddProductToRelease() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting AddProductToRelease");
        ProductFlows productFlows = new ProductFlows(page, executionData);
        releaseTrainFlows.addProductToRelease(productFlows);
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.ADD_PRODUCT_TO_RELEASE), ReleaseTrainTestData.ADD_PRODUCT_TO_RELEASE, executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: train={}, release={}, product={} assigned and deleted", executionData.getReleaseTrainName(), executionData.getReleaseName(), executionData.getPrimaryProduct().getProductName());
    }

    @Step("Navigate to Product from Release page")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void navigateToProductFromReleasePage() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting navigateToProductFromReleasePage");
        ProductFlows productFlows = new ProductFlows(page, executionData);
        releaseTrainFlows.navigateToProductFromReleasePage(productFlows);
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.ADD_PRODUCT_TO_RELEASE), ReleaseTrainTestData.ADD_PRODUCT_TO_RELEASE, executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: navigated from release={} to product={}", executionData.getReleaseName(), executionData.getPrimaryProduct().getProductName());
    }

    @Step("Search Release Train from Release Trains page")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void searchReleaseTrainFromReleaseTrainPage() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting searchReleaseTrainFromReleaseTrainPage");
        releaseTrainFlows.searchReleaseTrainFromReleaseTrainPage();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(), ReleaseTrainTestData.CREATE_RELEASE_TRAIN, executionData);
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: searchedTrain={}", executionData.getReleaseTrainName());
    }

    @Step("Add feature to Release")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void addFeatureToRelease() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting addFeatureToRelease");
        ProductFlows productFlows = new ProductFlows(page, executionData);
        releaseTrainFlows.addFeatureToRelease(productFlows);
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.ADD_FEATURE_TO_RELEASE), ReleaseTrainTestData.ADD_FEATURE_TO_RELEASE, executionData);
        productFlows.deleteProduct();
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: feature added to release={}", executionData.getReleaseName());
    }
    @Step("Verify add release dependencies")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void verifyAddReleaseDependencies() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting verifyAddReleaseDependencies");
        releaseTrainFlows.verifyAddReleaseDependencies();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VERIFY_ADD_RELEASE_DEPENDENCIES), ReleaseTrainTestData.VERIFY_ADD_RELEASE_DEPENDENCIES, executionData);
        releaseTrainFlows.deleteReleaseDependencyProducts();
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: verified add release dependencies for release={}", executionData.getReleaseName());
    }
    @Step("Verify the release train and release in the dependency graph")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void verifyReleasesInDependencyGraph() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting verifyReleasesInDependencyGraph");
        releaseTrainFlows.verifyReleasesInDependencyGraph();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VERIFY_RELEASES_IN_DEPENDENCY_GRAPH), ReleaseTrainTestData.VERIFY_RELEASES_IN_DEPENDENCY_GRAPH, executionData);
        releaseTrainFlows.deleteReleaseDependencyProducts();
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: verified dependency graph for release={}", executionData.getReleaseName());
    }
    @Step("Verify both dependency relationships when selecting a release")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void verifyDependsOnAndDependentReleasesOnSelectingRelease() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting verifyDependsOnAndDependentReleasesOnSelectingRelease");
        releaseTrainFlows.verifyDependsOnAndDependentReleasesOnSelectingRelease();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VERIFY_RELEASE_RELATIONSHIPS), ReleaseTrainTestData.VERIFY_RELEASE_RELATIONSHIPS, executionData);
        releaseTrainFlows.deleteReleaseDependencyProducts();
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: verified dependency relationships for release={}", executionData.getReleaseName());
    }
    @Step("Verify applying and clearing the release date filter on Dependencies")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_RELEASE_TRAIN})
    public void verifyReleaseDateFilterAppliedSuccessfullyOnDependencies() {
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Starting verifyReleaseDateFilterAppliedSuccessfullyOnDependencies");
        releaseTrainFlows.verifyReleaseDateFilterAppliedSuccessfullyOnDependencies();
        ProExecutionResultWriter.write(ReleaseTrainTestData.getScenario(ReleaseTrainTestData.VERIFY_RELEASE_DATE_FILTER_APPLIED_SUCCESSFULLY_ON_DEPENDENCIES), ReleaseTrainTestData.VERIFY_RELEASE_DATE_FILTER_APPLIED_SUCCESSFULLY_ON_DEPENDENCIES, executionData);
        releaseTrainFlows.deleteReleaseDependencyProducts();
        LoggerUtil.LOGGER.info("[RELEASE-TRAIN-TEST] Completed successfully: verified release date filter applied successfully on dependencies for release={}", executionData.getReleaseName());
    }
}

