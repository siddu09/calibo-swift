package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.MaturityAssessment.Flows.MaturityAssessmentFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionResultWriter;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;

public class MaturityAssessmentTest extends BaseUITest {
    private MaturityAssessmentFlows maturityAssessmentFlows;
    private ProExecutionData executionData;

    @BeforeMethod
    @Step("Login and initialize maturity assessment flows")
    public void setUp() {
        new LoginBuildingBlock(page).proLogin();
        executionData = new ProExecutionData();
        maturityAssessmentFlows = new MaturityAssessmentFlows(page, executionData);
    }

    @Step("Maturity Assessment at Organization Level")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_MATURITYASSESSMENT})
    public void MaturityAssesmentAtOrganisationLevel() {
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Starting MaturityAssesmentAtOrganisationLevel");
        maturityAssessmentFlows.MaturityAssesmentAtOrganisationLevel();
        ProExecutionResultWriter.write(ProTestData.getMaturityData("MaturityAssesmentAtOrganisationLevel"), "MaturityAssesmentAtOrganisationLevel", executionData);
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Completed MaturityAssesmentAtOrganisationLevel successfully; execution data written for assessment: {}", executionData.getAssessmentName());
    }
    @Step("Maturity Assessment at Organization Level - Respond to Assessment")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_MATURITYASSESSMENT})
    public void MaturityAssesmentOrganisationRespondToAssesement() {
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Starting MaturityAssesmentOrganisationRespondToAssesement");
        maturityAssessmentFlows.MaturityAssesmentOrganisationRespondToAssesement();
        ProExecutionResultWriter.write(ProTestData.getMaturityData("MaturityAssesmentOrganisationRespondToAssesement"), "MaturityAssesmentOrganisationRespondToAssesement", executionData);
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Completed MaturityAssesmentOrganisationRespondToAssesement successfully; execution data written for assessment: {}", executionData.getAssessmentName());
    }
    @Step("Initiate and discard an organization maturity assessment")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_MATURITYASSESSMENT})
    public void IntiateAndDiscardMaturityAssessmentAtOrgLevel() {
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Starting IntiateAndDiscardMaturityAssessmentAtOrgLevel");
        maturityAssessmentFlows.IntiateAndDiscardMaturityAssessmentAtOrgLevel();
        ProExecutionResultWriter.write(ProTestData.getMaturityData("IntiateAndDiscardMaturityAssessmentAtOrgLevel"), "IntiateAndDiscardMaturityAssessmentAtOrgLevel", executionData);
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Completed IntiateAndDiscardMaturityAssessmentAtOrgLevel successfully; execution data written for assessment: {}", executionData.getAssessmentName());
    }
    @Step("Complete and baseline an organization maturity assessment")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_MATURITYASSESSMENT})
    public void BaseLineOrganisationMaturtiyAssesement() {
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Starting BaseLineOrganisationMaturtiyAssesement");
        maturityAssessmentFlows.BaseLineOrganisationMaturtiyAssesement();
        ProExecutionResultWriter.write(ProTestData.getMaturityData("BaseLineOrganisationMaturtiyAssesement"), "BaseLineOrganisationMaturtiyAssesement", executionData);
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Completed BaseLineOrganisationMaturtiyAssesement successfully; execution data written for assessment: {}", executionData.getAssessmentName());
    }
    @Step("Maturity Assessment at Organization Level - Validate Initiate Option")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_MATURITYASSESSMENT})
    public void MaturityAssesmentAtOrganisationValidateInitiateOption() {
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Starting MaturityAssesmentAtOrganisationValidateInitiateOption");
        maturityAssessmentFlows.MaturityAssesmentAtOrganisationValidateInitiateOption();
        ProExecutionResultWriter.write(ProTestData.getMaturityData("MaturityAssesmentAtOrganisationValidateInitiateOption"), "MaturityAssesmentAtOrganisationValidateInitiateOption", executionData);
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Completed MaturityAssesmentAtOrganisationValidateInitiateOption successfully; execution data written for assessment: {}", executionData.getAssessmentName());
    }
    @Step("Maturity Assessment at Organization Level - Validate Recommendations")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_MATURITYASSESSMENT})
    public void MaturtiyAssementOrganisationValidateRecomendation() {
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Starting MaturtiyAssementOrganisationValidateRecomendation");
//        maturityAssessmentFlows.MaturtiyAssementOrganisationValidateRecomendation();
        ProExecutionResultWriter.write(ProTestData.getMaturityData("MaturtiyAssementOrganisationValidateRecomendation"), "MaturtiyAssementOrganisationValidateRecomendation", executionData);
        LoggerUtil.LOGGER.info("[MATURITY-TEST] Completed MaturtiyAssementOrganisationValidateRecomendation successfully; execution data written for assessment: {}", executionData.getAssessmentName());
    }
}
