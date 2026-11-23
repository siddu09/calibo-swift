package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Features.Define.Flows.DefineFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.DefineTestData;
import testdatamanager.pro.ProExecutionResultWriter;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;

public class DefineTest extends BaseUITest {
    private DefineFlows defineFlows;
    private ProExecutionData executionData;

    @BeforeMethod
    @Step("Login and initialize Define flows")
    public void setUp() {
        new LoginBuildingBlock(page).proLogin();
        executionData = new ProExecutionData();
        defineFlows = new DefineFlows(page, executionData);
    }

    @Step("Create a User Story with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void CreateNewUserStoryWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting CreateNewUserStoryWithMandatoryFields");
        defineFlows.CreateNewUserStoryWithMandatoryFields();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_DEFINE),DefineTestData.CREATE_DEFINE, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with story ID: {}", executionData.getCreatedStoryId());
    }
    @Step("Add an existing Jira story to a feature")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void AddExistingUserStory() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting AddExistingUserStory");
        defineFlows.AddExistingUserStory();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.ADD_EXISTING_STORY),DefineTestData.ADD_EXISTING_STORY, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed AddExistingUserStory: {}", executionData.getCreatedStoryId());
    }
    @Step("Create a Task with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void CreateNewTaskWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting CreateNewTaskWithMandatoryFields");
        defineFlows.CreateNewUserStoryWithMandatoryFields("Task");
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_DEFINE),DefineTestData.CREATE_TASK, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with task ID: {}", executionData.getCreatedStoryId());
    }
    @Step("Create a Feature with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void CreateNewFeatureWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting CreateNewFeatureWithMandatoryFields");
        defineFlows.CreateNewUserStoryWithMandatoryFields("Feature");
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_DEFINE),DefineTestData.CREATE_FEATURE, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with feature ID: {}", executionData.getCreatedStoryId());
    }
    @Step("Create a Epic with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void CreateNewEpicWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting CreateNewEpicWithMandatoryFields");
        defineFlows.CreateNewUserStoryWithMandatoryFields("Epic");
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_DEFINE),DefineTestData.CREATE_EPIC, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with epic ID: {}", executionData.getCreatedStoryId());
    }
    @Step("Create a User Feedback with All Mandatory Fields Without Confluence")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void createUserFeedbackWithAllMandatoryFieldsWithoutConfluence() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting createUserFeedbackWithAllMandatoryFieldsWithoutConfluence");
        defineFlows.createUserFeedbackWithAllMandatoryFieldsWithoutConfluence();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_USER_FEEDBACK_WITHOUT_CONFLUENCE),DefineTestData.CREATE_USER_FEEDBACK_WITHOUT_CONFLUENCE, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with User Feedback title: {}", executionData.getCreatedStoryId());
    }
    @Step("Create a User Feedback with All Mandatory Fields With Confluence")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void createUserFeedbackWithAllMandatoryFieldsWithConfluence() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting createUserFeedbackWithAllMandatoryFieldsWithConfluence");
        defineFlows.createUserFeedbackWithAllMandatoryFieldsWithConfluence();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_USER_FEEDBACK_WITH_CONFLUENCE),DefineTestData.CREATE_USER_FEEDBACK_WITH_CONFLUENCE, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with User Feedback title: {}", executionData.getCreatedStoryId());
    }
    @Step("Create a Business Requirement with All Mandatory Fields Without Confluence")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void createBusinessRequirementWithAllMandatoryFieldsWithoutConfluence() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting createBusinessRequirementWithAllMandatoryFieldsWithoutConfluence");
        defineFlows.createBusinessRequirementWithAllMandatoryFieldsWithoutConfluence();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITHOUT_CONFLUENCE),DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITHOUT_CONFLUENCE, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with Business Requirement title: {}", executionData.getCreatedStoryId());
    }
    @Step("Create a Business Requirement with All Mandatory Fields With Confluence")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void createBusinessRequirementWithAllMandatoryFieldsWithConfluence() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting createBusinessRequirementWithAllMandatoryFieldsWithConfluence");
        defineFlows.createBusinessRequirementWithAllMandatoryFieldsWithConfluence();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITH_CONFLUENCE),DefineTestData.CREATE_BUSINESS_REQUIREMENT_WITH_CONFLUENCE, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with Business Requirement title: {}", executionData.getCreatedStoryId());
    }
    @Step("Add Comments and Reviews to Existing User Feedback")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void addCommentsAndReviewsToExistingUserFeedback() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting addCommentsAndReviewsToExistingUserFeedback");
        defineFlows.addCommentsAndReviewsToExistingUserFeedback();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.ADD_COMMENTS_AND_REVIEWS_TO_EXISTING_USER_FEEDBACK),DefineTestData.ADD_COMMENTS_AND_REVIEWS_TO_EXISTING_USER_FEEDBACK, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with User Feedback title: {}", executionData.getCreatedStoryId());
    }
    @Step("Add Comments and Reviews to Existing Business Requirement")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void addCommentsAndReviewsToExistingBusinessRequirement() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting addCommentsAndReviewsToExistingBusinessRequirement");
        defineFlows.addCommentsAndReviewsToExistingBusinessRequirement();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.ADD_COMMENTS_AND_REVIEWS_TO_EXISTING_BUSINESS_REQUIREMENT),DefineTestData.ADD_COMMENTS_AND_REVIEWS_TO_EXISTING_BUSINESS_REQUIREMENT, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with Business Requirement title: {}", executionData.getCreatedStoryId());
    }

    @Step("Convert User Feedback to a Business Requirement")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void convertUserFeedbackToBusinessRequirement() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting convertUserFeedbackToBusinessRequirement");
        defineFlows.convertUserFeedbackToBusinessRequirement();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.CONVERT_USER_FEEDBACK_TO_BUSINESS_REQUIREMENT),DefineTestData.CONVERT_USER_FEEDBACK_TO_BUSINESS_REQUIREMENT, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with Business Requirement title: {}", executionData.getCreatedStoryId());
    }
    @Step("Edit User Feedback")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void editUserFeedback() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting editUserFeedback");
        defineFlows.editUserFeedback();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.EDIT_USER_FEEDBACK),DefineTestData.EDIT_USER_FEEDBACK, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with User Feedback title: {}", executionData.getCreatedStoryId());
    }
    @Step("Edit Business Requirement")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void editBusinessRequirement() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting editBusinessRequirement");
        defineFlows.editBusinessRequirement();
        ProExecutionResultWriter.write(DefineTestData.getScenario(DefineTestData.EDIT_BUSINESS_REQUIREMENT),DefineTestData.EDIT_BUSINESS_REQUIREMENT, executionData);
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed successfully with Business Requirement title: {}", executionData.getCreatedStoryId());
    }
    @Step("Verify Mandatory Field Validation For User Feedback")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void verifyMandatoryFieldValidationForUserFeedback() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting verifyMandatoryFieldValidationForUserFeedback");
        defineFlows.verifyMandatoryFieldValidationForUserFeedback();
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed verifyMandatoryFieldValidationForUserFeedback");
    }
    @Step("Verify Mandatory Field Validation For Business Requirement")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void verifyMandatoryFieldValidationForBusinessRequirement() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting verifyMandatoryFieldValidationForBusinessRequirement");
        defineFlows.verifyMandatoryFieldValidationForBusinessRequirement();
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed verifyMandatoryFieldValidationForBusinessRequirement");
    }
    @Step("Delete User Feedback")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void deleteUserFeedback() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting deleteUserFeedback");
        defineFlows.deleteUserFeedback();
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed deleteUserFeedback");
    }
    @Step("Delete Business Requirement")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void deleteBusinessRequirement () {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting deleteBusinessRequirement");
        defineFlows.deleteBusinessRequirement();
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed deleteBusinessRequirement");
    }
    @Step("Remove Mapped User Story")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void removeMappedUserStory() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting removeMappedUserStory");
        defineFlows.removeMappedUserStory();
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed removeMappedUserStory");
    }
    @Step("Import New Business Requirement")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DEFINE})
    public void importNewBusinessRequirement() {
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Starting importNewBusinessRequirement");
        defineFlows.importNewBusinessRequirement();
        defineFlows.deleteDefineProductAndFeature();
        LoggerUtil.LOGGER.info("[DEFINE-TEST] Completed importNewBusinessRequirement");
    }
}
