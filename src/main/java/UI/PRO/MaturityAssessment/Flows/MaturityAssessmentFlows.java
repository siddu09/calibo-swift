package UI.PRO.MaturityAssessment.Flows;

import UI.PRO.MaturityAssessment.BuildingBlocks.MaturityAssessmentBuildingBlock;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;

public class MaturityAssessmentFlows {
    private final MaturityAssessmentBuildingBlock maturityAssessmentBuildingBlock;

    public MaturityAssessmentFlows(Page page, ProExecutionData executionData) {
        maturityAssessmentBuildingBlock = new MaturityAssessmentBuildingBlock(page, executionData);
    }

    @Step("Complete an organization assessment and baseline it")
    public void BaseLineOrganisationMaturtiyAssesement() {
        MaturityAssesmentOrganisationRespondToAssesement();
        var data = ProTestData.getMaturityData("BaseLineOrganisationMaturtiyAssesement");
        maturityAssessmentBuildingBlock.openOrganizationAssessments();
        maturityAssessmentBuildingBlock.baselineCompletedOrganizationAssessment(data);
    }

    @Step("Initiate the organization assessment and discard it from Current Assessments")
    public void IntiateAndDiscardMaturityAssessmentAtOrgLevel() {
        MaturityAssesmentAtOrganisationLevel();
        var data = ProTestData.getMaturityData("IntiateAndDiscardMaturityAssessmentAtOrgLevel");
        maturityAssessmentBuildingBlock.discardInitiatedOrganizationAssessment(data);
    }

    @Step("Initiate an organization assessment for three subcategories")
    public void MaturityAssesmentAtOrganisationLevel() {
        var data = ProTestData.getMaturityData("MaturityAssesmentAtOrganisationLevel");
        maturityAssessmentBuildingBlock.openOrganizationAssessments();
        maturityAssessmentBuildingBlock.createOrganizationAssessment(data);
        maturityAssessmentBuildingBlock.configureOrganizationRespondents(data);
        maturityAssessmentBuildingBlock.initiateOrganizationAssessment(data);
    }

    @Step("Configure an organization assessment without initiating and verify its Initiate option")
    public void MaturityAssesmentAtOrganisationValidateInitiateOption() {
        var setup = ProTestData.getMaturityData("MaturityAssesmentAtOrganisationLevel");
        maturityAssessmentBuildingBlock.openOrganizationAssessments();
        maturityAssessmentBuildingBlock.createOrganizationAssessment(setup);
        maturityAssessmentBuildingBlock.configureOrganizationRespondents(setup);
        var validation = ProTestData.getMaturityData("MaturityAssesmentAtOrganisationValidateInitiateOption");
        maturityAssessmentBuildingBlock.validateOrganizationDraftInitiateAvailable(validation);
    }

    @Step("Create an organization assessment, submit responses and verify completed answers")
    public void MaturityAssesmentOrganisationRespondToAssesement() {
        MaturityAssesmentAtOrganisationLevel();
        var data = ProTestData.getMaturityData("MaturityAssesmentOrganisationRespondToAssesement");
        maturityAssessmentBuildingBlock.openAndStartOrganizationResponse(data);
        maturityAssessmentBuildingBlock.submitOrganizationMixedChoiceResponses(data);
        maturityAssessmentBuildingBlock.validateCompletedOrganizationMixedChoiceResponses(data);
    }
}
