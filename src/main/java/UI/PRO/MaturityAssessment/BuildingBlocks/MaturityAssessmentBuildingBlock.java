package UI.PRO.MaturityAssessment.BuildingBlocks;

import UI.PRO.CommonProValidations.ProValidation;
import UI.PRO.datahelper.MaturityAssessmentData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.LandingPage;
import pages.PRO.MaturityAssesment.MaturityAssesment;
import pages.PRO.MaturityAssesment.OrganizationalMaturityPage;
import pages.PRO.MaturityAssesment.MyAssessmentsPage;
import pages.PRO.MaturityAssesment.ProductMaturityAssessmentResponsePage;
import pages.PRO.MaturityAssesment.ProductMaturityAssessmentPage;
import testdatamanager.pro.ProExecutionData;
import utils.CommonMethods;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MaturityAssessmentBuildingBlock {
    private final Page page;
    private final ProExecutionData executionData;
    private final ProductMaturityAssessmentPage assessmentPage;
    private final OrganizationalMaturityPage organizationPage;

    public MaturityAssessmentBuildingBlock(Page page, ProExecutionData executionData) {
        this.page = page;
        this.executionData = executionData;
        assessmentPage = new ProductMaturityAssessmentPage(page);
        organizationPage = new OrganizationalMaturityPage(page);
    }

    @Step("Navigate to organization maturity assessments")
    public void openOrganizationAssessments() {
        new LandingPage(page).hoverOnNavigationBar().click();
        var navigation = new MaturityAssesment().new ProductAddProjectDetailsPage(page);
        navigation.maturityAssessmentExpandButton().click();
        navigation.organizationMaturityAssessment().click();
        page.mouse().move(500, 300);
    }

    @Step("Create and name an organization maturity assessment")
    public void createOrganizationAssessment(MaturityAssessmentData data) {
        assessmentPage.newAssessment(data.getNewAssessmentButton()).click();
        String name = CommonMethods.generateUniqueTitle(data.getNamePrefix());
        assessmentPage.commitName(data.getNameLabel(), data.getNamePlaceholder(), name);
        executionData.setAssessmentName(name);
    }

    @Step("Assign organization assessment respondents and response due dates")
    public void configureOrganizationRespondents(MaturityAssessmentData data) {
        for (String category : data.getSubCategories()) {
            assessmentPage.respondentControl(category).click();
            assessmentPage.respondentSearch(category).fill(data.getRespondent());
            organizationPage.respondentOption(data.getRespondent()).click();
            assertThat(assessmentPage.respondentControl(category)).containsText(data.getRespondent());
            assessmentPage.selectDueDate(category, data.getResponseDueDate());
            assertThat(assessmentPage.dueDate(category)).hasValue(data.getResponseDueDate());
        }
    }

    @Step("Go back, scroll to the saved organization assessment and verify Initiate is available")
    public void validateOrganizationDraftInitiateAvailable(MaturityAssessmentData data) {
        new pages.PRO.ReleaseTrain.ProductReleasePage(page).back(data.getBackButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assessmentPage.currentAssessments(data.getCurrentAssessmentsTab(), data.getLoadTimeoutMs()).click();
        var card = organizationPage.assessmentCard(executionData.getAssessmentName());
        card.scrollIntoViewIfNeeded();
        assertThat(card).isVisible();
        var initiate = organizationPage.initiateAssessment(executionData.getAssessmentName(), data.getInitiateButton());
        assertThat(initiate).isVisible();
        assertThat(initiate).isEnabled();
        utils.LoggerUtil.LOGGER.info("[MATURITY-TEST] Verified Initiate is available for saved organization assessment: {}",
                executionData.getAssessmentName());
    }

    @Step("Initiate organization assessment and validate current assessment")
    public void initiateOrganizationAssessment(MaturityAssessmentData data) {
        for (String category : data.getSubCategories()) {
            assertThat(assessmentPage.initiate(category, data.getInitiateButton())).isEnabled();
            assessmentPage.initiate(category, data.getInitiateButton()).click();
            ProValidation.validateSuccessMessage(page, data.getInitiatedMessage());
        }
        organizationPage.action(data.getDoneButton()).click();
        assessmentPage.currentAssessments(data.getCurrentAssessmentsTab(), 15000).click();
        assertThat(organizationPage.assessmentCard(executionData.getAssessmentName())).containsText(data.getExpectedStatus());
        assertThat(organizationPage.initiatedCount(executionData.getAssessmentName(), data.getInitiatedCountLabel()))
                .containsText(java.util.regex.Pattern.compile("^" + data.getExpectedInitiatedCount()
                        + " " + data.getInitiatedCountLabel() + "([ |]|$)"));
    }

    @Step("Open My Assessments and start the newly created organization assessment")
    public void openAndStartOrganizationResponse(MaturityAssessmentData data) {
        var response = data.getResponse();
        var assessments = new MyAssessmentsPage(page);
        new LandingPage(page).profileMenu(response.getProfileLabel(), response.getLoadTimeoutMs()).click();
        assessments.menuOption(response.getMyAssessmentsLabel(), response.getLoadTimeoutMs()).click();
        page.mouse().move(500, 300);
        assessments.searchByName(response.getSearchByNamePlaceholder(), response.getLoadTimeoutMs())
                .fill(executionData.getAssessmentName());
        assessments.assessment(executionData.getAssessmentName(), response.getLoadTimeoutMs()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var start = assessments.startAssessment(executionData.getAssessmentName(),
                response.getStartButton(), response.getLoadTimeoutMs());
        assertThat(start).isVisible();
        assertThat(start).isEnabled();
        start.click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Baseline the completed organization assessment and verify it in Baselined Assessments")
    public void baselineCompletedOrganizationAssessment(MaturityAssessmentData data) {
        var baseline = data.getBaseline();
        String assessmentName = executionData.getAssessmentName();
        assessmentPage.currentAssessments(data.getCurrentAssessmentsTab(), data.getLoadTimeoutMs()).click();
        var assessment = organizationPage.assessmentCard(assessmentName);
        assessment.scrollIntoViewIfNeeded();
        assertThat(assessment).containsText(data.getExpectedStatus());
        organizationPage.expandAssessment(assessmentName).click();
        var baselineButton = organizationPage.baselineAssessment(assessmentName, baseline.getBaselineButton());
        assertThat(baselineButton).isEnabled();
        baselineButton.click();
        assertThat(assessmentPage.discardConfirmation(baseline.getConfirmationMessage())).isVisible();
        assessmentPage.confirmDiscard(baseline.getYesButton()).click();
        ProValidation.validateSuccessMessage(page, baseline.getBaselinedMessage());
        CommonMethods.waitForLoaderToDisappear(page);
        assessmentPage.currentAssessments(baseline.getBaselinedAssessmentsTab(), data.getLoadTimeoutMs()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var baselinedAssessment = organizationPage.responseDetailsValue(assessmentName, data.getLoadTimeoutMs());
        baselinedAssessment.scrollIntoViewIfNeeded();
        assertThat(baselinedAssessment).isVisible();
    }

    @Step("Discard the named organization assessment and verify it remains absent after reload")
    public void discardInitiatedOrganizationAssessment(MaturityAssessmentData data) {
        var discard = data.getAssessmentDiscard();
        String assessmentName = executionData.getAssessmentName();
        var assessment = organizationPage.assessmentCard(assessmentName);
        assessment.scrollIntoViewIfNeeded();
        assertThat(assessment).containsText(data.getExpectedStatus());
        organizationPage.assessmentActions(assessmentName).click();
        assessmentPage.discardAssessment(discard.getDiscardAction()).click();
        assertThat(assessmentPage.discardConfirmation(discard.getConfirmationMessage())).isVisible();
        assessmentPage.confirmDiscard(discard.getYesButton()).click();
        ProValidation.validateSuccessMessage(page, discard.getDiscardedMessage());
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(assessmentPage.newAssessment(data.getNewAssessmentButton())).isVisible();
        assertThat(assessmentPage.assessmentOverview()).not().containsText(assessmentName);
        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);
        assessmentPage.currentAssessments(data.getCurrentAssessmentsTab(), data.getLoadTimeoutMs()).click();
        assertThat(assessmentPage.newAssessment(data.getNewAssessmentButton())).isVisible();
        assertThat(assessmentPage.assessmentOverview()).not().containsText(assessmentName);
    }

    @Step("Answer the three organization questions and submit the assessment")
    public void submitOrganizationResponses(MaturityAssessmentData data) {
        var response = data.getResponse();
        for (int index = 0; index < response.getQuestions().size(); index++) {
            String visibleQuestion = organizationPage.responseQuestion(response.getQuestions().stream()
                    .map(MaturityAssessmentData.Question::getQuestion).toList(), response.getLoadTimeoutMs()).innerText().trim();
            var question = response.getQuestions().stream()
                    .filter(candidate -> candidate.getQuestion().equals(visibleQuestion)).findFirst().orElseThrow();
            var answer = organizationPage.responseAnswer(question.getQuestion(), question.getAnswer(),
                    response.getLoadTimeoutMs());
            answer.check();
            assertThat(answer).isChecked();
            organizationPage.action(index + 1 < response.getQuestions().size()
                    ? response.getNextButton() : response.getSubmitButton()).click();
            assertThat(answer).isHidden();
        }
        ProValidation.validateSuccessMessage(page, response.getSubmittedMessage());
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Answer organization radio and checkbox questions and submit the assessment")
    public void submitOrganizationMixedChoiceResponses(MaturityAssessmentData data) {
        var response = data.getResponse();
        var remaining = new java.util.ArrayList<>(response.getQuestions());
        while (!remaining.isEmpty()) {
            String visibleQuestion = organizationPage.responseQuestion(remaining.stream()
                    .map(MaturityAssessmentData.Question::getQuestion).toList(), response.getLoadTimeoutMs())
                    .innerText().trim();
            var question = remaining.stream().filter(candidate -> candidate.getQuestion().equals(visibleQuestion))
                    .findFirst().orElseThrow();
            var answer = organizationPage.responseChoice(question.getQuestion(), question.getAnswer(),
                    response.getLoadTimeoutMs());
            answer.check();
            assertThat(answer).isChecked();
            remaining.remove(question);
            organizationPage.responseNavigationAction(remaining.isEmpty()
                    ? response.getSubmitButton() : response.getNextButton(), response.getLoadTimeoutMs()).click();
            if (!remaining.isEmpty()) {
                assertThat(answer).isHidden();
            }
        }
        ProValidation.validateSuccessMessage(page, response.getSubmittedMessage());
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Verify completed organization assessment and displayed subcategories and answers")
    public void validateCompletedOrganizationMixedChoiceResponses(MaturityAssessmentData data) {
        var response = data.getResponse();
        var responses = new ProductMaturityAssessmentResponsePage(page);
        new MyAssessmentsPage(page).searchByName(response.getSearchByNamePlaceholder(), response.getLoadTimeoutMs())
                .fill(executionData.getAssessmentName());
        assertThat(responses.responseStatus(executionData.getAssessmentName(), response.getCompletedStatus(),
                response.getViewDetailsButton(), response.getLoadTimeoutMs())).hasText(response.getCompletedStatus());
        assertThat(responses.completionProgress(executionData.getAssessmentName(), response.getCompletionPercentage(),
                response.getViewDetailsButton(), response.getLoadTimeoutMs())).hasText(response.getCompletionPercentage());
        var details = responses.viewDetails(executionData.getAssessmentName(), response.getViewDetailsButton(),
                response.getLoadTimeoutMs());
        assertThat(details).isVisible();
        assertThat(details).isEnabled();
        details.click();
        CommonMethods.waitForLoaderToDisappear(page);
        for (var question : response.getQuestions()) {
            var subCategory = organizationPage.responseDetailsValue(question.getSubCategory(), response.getLoadTimeoutMs());
            subCategory.scrollIntoViewIfNeeded();
            assertThat(subCategory).isVisible();
            for (String answer : question.getDisplayedAnswers()) {
                assertThat(organizationPage.responseDetailsValue(answer,
                        response.getLoadTimeoutMs())).isVisible();
            }
        }
    }

    @Step("Verify Completed, 100 percent progress, and all saved organization answers")
    public void validateCompletedOrganizationResponses(MaturityAssessmentData data) {
        var response = data.getResponse();
        var responses = new ProductMaturityAssessmentResponsePage(page);
        new MyAssessmentsPage(page).searchByName(response.getSearchByNamePlaceholder(), response.getLoadTimeoutMs())
                .fill(executionData.getAssessmentName());
        assertThat(responses.responseStatus(executionData.getAssessmentName(), response.getCompletedStatus(),
                response.getViewDetailsButton(), response.getLoadTimeoutMs())).hasText(response.getCompletedStatus());
        assertThat(responses.completionProgress(executionData.getAssessmentName(), response.getCompletionPercentage(),
                response.getViewDetailsButton(), response.getLoadTimeoutMs())).hasText(response.getCompletionPercentage());
        var details = responses.viewDetails(executionData.getAssessmentName(), response.getViewDetailsButton(),
                response.getLoadTimeoutMs());
        assertThat(details).isVisible();
        assertThat(details).isEnabled();
        details.click();
        CommonMethods.waitForLoaderToDisappear(page);
        for (var question : response.getQuestions()) {
            assertThat(responses.submittedAnswer(question.getQuestion(), question.getAnswer(),
                    response.getLoadTimeoutMs())).isVisible();
            assertThat(organizationPage.responseAnswer(question.getQuestion(), question.getAnswer(),
                    response.getLoadTimeoutMs())).isChecked();
        }
    }
}
