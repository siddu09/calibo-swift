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
}
