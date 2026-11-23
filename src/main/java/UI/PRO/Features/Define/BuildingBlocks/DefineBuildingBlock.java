package UI.PRO.Features.Define.BuildingBlocks;

import UI.PRO.datahelper.DefineData;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import pages.PRO.Features.Define.DefinePage;
import pages.PRO.Features.Define.BusinessRequirementDetailsPage;
import pages.PRO.Features.Define.UserStoryPage;
import UI.PRO.Features.Define.Validations.DefineValidation;
import testdatamanager.pro.FeatureExecutionData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;
import org.testng.Assert;
import utils.CommonMethods;
import utils.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefineBuildingBlock {

    private final Page page;
    private final ProExecutionData executionData;

    private final DefinePage definePage;
    private String defineTitle;

    public DefineBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.definePage = new DefinePage(page);
    }

    public void createBusinessRequirement(DefineData defineData) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Define Started ==========");

        CommonMethods.clickOnTab(page, defineData.getSection());

        definePage.addNewBusinessRequirement(
                defineData.getType()).click();

        definePage.addNewBusinessRequirement("User").click();

        page.waitForTimeout(2000);

        this.defineTitle =
                CommonMethods.generateUniqueTitle(defineData.getTitle());

        definePage.title().fill(defineTitle);

        definePage.description().fill(defineData.getDescription());

        definePage.addUserName().fill(defineData.getUserName());

        definePage.selectDropDown(
                "Customer Name",
                defineData.getCustomerName());

        definePage.selectDropDown(
                "Priority",
                defineData.getPriority());

        definePage.selectDropDown(
                "Impact",
                defineData.getImpact());

        definePage.selectDropDown(
                "Status",
                defineData.getStatus());

        definePage.selectDropDown(
                "Source",
                defineData.getSource());

        definePage.create().click();

        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Define Completed ==========");

        storeBusinessRequirementExecutionData(defineTitle);
    }

    public void viewBusinessRequirement()
    {
        definePage.selectBusinessRequirement(this.defineTitle).click();
    }

    @Step("Open the New User Feedback form")
    public void openNewUserFeedbackForm() {
        definePage.businessRequirementsTab().click();
        CommonMethods.waitForLoaderToDisappear(page);
        definePage.newUserFeedbackAction().click();
        definePage.userFeedbackType().click();
    }

    @Step("Submit the empty User Feedback form")
    public void submitEmptyUserFeedbackForm() {
        openNewUserFeedbackForm();
        definePage.createUserFeedbackButton().click();
    }

    @Step("Create user feedback with every mandatory field")
    public void createUserFeedbackWithAllMandatoryFields(DefineData data) {
        openNewUserFeedbackForm();

        defineTitle = CommonMethods.generateUniqueTitle(data.getTitle());
        definePage.title().fill(defineTitle);
        definePage.description().fill(data.getDescription());
        definePage.addUserName().fill(data.getUserName());
        createFeedbackDropdownValue("Customer Name", data.getCustomerName());
        selectFeedbackDropdownValue("Priority", data.getPriority());
        selectFeedbackDropdownValue("Impact", data.getImpact());
        selectFeedbackDropdownValue("Status", data.getStatus());
        createFeedbackDropdownValue("Source", data.getSource());
        DefineValidation.validateUserFeedbackForm(page, data, defineTitle);
        definePage.createUserFeedbackButton().click();
        CommonMethods.waitForLoaderToDisappear(page);

        storeBusinessRequirementExecutionData(defineTitle);
        executionData.setCreatedStoryId(defineTitle);
    }

    @Step("Create user feedback with mandatory fields and Confluence documentation")
    public void createUserFeedbackWithAllMandatoryFieldsAndConfluence(DefineData data) {
        definePage.businessRequirementsTab().click();
        CommonMethods.waitForLoaderToDisappear(page);
        definePage.newUserFeedbackAction().click();
        definePage.userFeedbackType().click();

        definePage.confluenceDocumentationToggle(data.getConfluencePrompt()).click();
        definePage.confluencePageTitle().fill(data.getConfluencePageTitle());

        defineTitle = CommonMethods.generateUniqueTitle(data.getTitle());
        definePage.title().fill(defineTitle);
        definePage.description().fill(data.getDescription());
        definePage.addUserName().fill(data.getUserName());
        createFeedbackDropdownValue("Customer Name", data.getCustomerName());
        selectFeedbackDropdownValue("Priority", data.getPriority());
        selectFeedbackDropdownValue("Impact", data.getImpact());
        selectFeedbackDropdownValue("Status", data.getStatus());
        createFeedbackDropdownValue("Source", data.getSource());
        DefineValidation.validateUserFeedbackForm(page, data, defineTitle);
        DefineValidation.validateConfluencePageTitle(page, data);
        definePage.createUserFeedbackButton().click();
        CommonMethods.waitForLoaderToDisappear(page);

        storeBusinessRequirementExecutionData(defineTitle);
        executionData.setCreatedStoryId(defineTitle);
    }

    @Step("Open the New Business Requirement form")
    public void openNewBusinessRequirementForm(DefineData data) {
        openBusinessRequirementCreationMenu();
        definePage.createNewBusinessRequirementOption(data.getCreationOption()).click();
    }

    private void openBusinessRequirementCreationMenu() {
        definePage.businessRequirementsTab().click();
        CommonMethods.waitForLoaderToDisappear(page);
        definePage.newBusinessRequirementAction().click();
    }

    @Step("Upload and import a Business Requirement CSV")
    public void importNewBusinessRequirement(DefineData data) {
        openBusinessRequirementCreationMenu();
        definePage.businessRequirementImportOption(data.getBusinessRequirementImportOption()).click();
        Locator browse = definePage.businessRequirementImportBrowseButton();
        page.waitForFileChooser(browse::click).setFiles(Path.of(data.getBusinessRequirementImportFile()));
        definePage.businessRequirementImportButton(data.getBusinessRequirementImportButton()).click();
    }

    @Step("Submit the empty Business Requirement form")
    public void submitEmptyBusinessRequirementForm(DefineData data) {
        openNewBusinessRequirementForm(data);
        definePage.createUserFeedbackButton().click();
    }

    @Step("Create a Business Requirement with every mandatory field")
    public void createBusinessRequirementWithAllMandatoryFields(DefineData data) {
        openNewBusinessRequirementForm(data);

        defineTitle = CommonMethods.generateUniqueTitle(data.getTitle());
        definePage.title().fill(defineTitle);
        definePage.description().fill(data.getDescription());
        selectFeedbackDropdownValue("Priority", data.getPriority());
        selectFeedbackDropdownValue("Impact", data.getImpact());
        selectFeedbackDropdownValue("Status", data.getStatus());
        createFeedbackDropdownValue("Source", data.getSource());
        DefineValidation.validateBusinessRequirementForm(page, data, defineTitle);
        definePage.createUserFeedbackButton().click();
        CommonMethods.waitForLoaderToDisappear(page);

        storeBusinessRequirementExecutionData(defineTitle);
        executionData.setCreatedStoryId(defineTitle);
    }

    @Step("Create a Business Requirement with mandatory fields and Confluence documentation")
    public void createBusinessRequirementWithAllMandatoryFieldsAndConfluence(DefineData data) {
        definePage.businessRequirementsTab().click();
        CommonMethods.waitForLoaderToDisappear(page);
        definePage.newBusinessRequirementAction().click();
        definePage.createNewBusinessRequirementOption(data.getCreationOption()).click();

        definePage.confluenceDocumentationToggle(data.getConfluencePrompt()).click();
        definePage.confluencePageTitle().fill(data.getConfluencePageTitle());

        defineTitle = CommonMethods.generateUniqueTitle(data.getTitle());
        definePage.title().fill(defineTitle);
        definePage.description().fill(data.getDescription());
        selectFeedbackDropdownValue("Priority", data.getPriority());
        selectFeedbackDropdownValue("Impact", data.getImpact());
        selectFeedbackDropdownValue("Status", data.getStatus());
        createFeedbackDropdownValue("Source", data.getSource());
        DefineValidation.validateBusinessRequirementForm(page, data, defineTitle);
        DefineValidation.validateConfluencePageTitle(page, data);
        definePage.createUserFeedbackButton().click();
        CommonMethods.waitForLoaderToDisappear(page);

        storeBusinessRequirementExecutionData(defineTitle);
        executionData.setCreatedStoryId(defineTitle);
    }

    private void createFeedbackDropdownValue(String fieldName, String value) {
        definePage.feedbackDropdown(fieldName).click();
        definePage.feedbackDropdownInput(fieldName).fill(value);
        definePage.createDropdownValue(value).click();
    }

    private void selectFeedbackDropdownValue(String fieldName, String value) {
        definePage.feedbackDropdown(fieldName).click();
        definePage.feedbackDropdownOption(value).click();
    }

    public void captureUserFeedbackExecutionIds(DefineData data) {
        captureStoryExecutionIds(data);
    }

    @Step("Delete the newly created Define item and provide a reason")
    public void deleteDefineItem(DefineData data) {
        openFeedbackCardAction(data, data.getDeleteFeedbackAction());
        Locator dialog = definePage.confirmationDialog(data.getDeleteFeedbackConfirmation());
        definePage.feedbackDeleteComment(dialog).fill(data.getDeleteFeedbackComment());
        definePage.confirmationAction(dialog, data.getDeleteFeedbackAction()).click();
    }

    private void openFeedbackCardAction(DefineData data, String action) {
        definePage.feedbackSearch(data.getFeedbackSearchPlaceholder()).fill(executionData.getCreatedStoryId());
        definePage.feedbackActions(executionData.getCreatedStoryId()).click();
        definePage.feedbackCardAction(action).click();
    }

    @Step("Search for the Define item, edit its title and description, and save")
    public String editDefineItem(DefineData data) {
        openFeedbackCardAction(data, data.getEditFeedbackAction());

        String updatedTitle = CommonMethods.generateUniqueTitle(data.getTitle());
        definePage.editFeedbackTitle().fill(updatedTitle);
        definePage.editFeedbackDescription().fill(data.getDescription());
        definePage.saveFeedbackButton(data.getSaveFeedbackButton()).click();
        return updatedTitle;
    }

    @Step("Reopen the saved Define item by its updated title")
    public void reopenEditedDefineItem(DefineData data, String requirementsListUrl, String updatedTitle) {
        page.navigate(requirementsListUrl);
        CommonMethods.waitForLoaderToDisappear(page);
        definePage.feedbackSearch(data.getFeedbackSearchPlaceholder()).fill(updatedTitle);
        definePage.feedbackAndRequirementSearchResults(updatedTitle).click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Convert the open User Feedback to a Business Requirement")
    public void convertUserFeedbackToBusinessRequirement(DefineData data) {
        new BusinessRequirementDetailsPage(page).convertFeedbackButton(data.getConvertFeedbackButton()).click();
        definePage.create().click();
    }

    @Step("Return to the requirement list and search by the original feedback title")
    public void searchForConvertedBusinessRequirement(DefineData data, String requirementsListUrl) {
        page.goBack();
        CommonMethods.waitForLoaderToDisappear(page);
        // The conversion can add a details page to browser history.
        if (!requirementsListUrl.equals(page.url())) {
            page.navigate(requirementsListUrl);
            CommonMethods.waitForLoaderToDisappear(page);
        }
        definePage.feedbackSearch(data.getFeedbackSearchPlaceholder()).fill(executionData.getCreatedStoryId());
    }

    @Step("Search for and open the newly created User Feedback")
    public void openCreatedUserFeedbackFromSearch(DefineData data) {
        page.goBack();
        CommonMethods.waitForLoaderToDisappear(page);
        definePage.feedbackSearch(data.getFeedbackSearchPlaceholder())
                .fill(executionData.getCreatedStoryId());
        definePage.searchedUserFeedback(executionData.getCreatedStoryId()).click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Add a comment to the opened User Feedback")
    public void addCommentToUserFeedback(DefineData data) {
        BusinessRequirementDetailsPage details = new BusinessRequirementDetailsPage(page);
        details.commentInput(data.getCommentPlaceholder()).fill(data.getComment());
        details.commentButton(data.getCommentButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Add a review to the opened User Feedback")
    public void addReviewToUserFeedback(DefineData data) {
        BusinessRequirementDetailsPage details = new BusinessRequirementDetailsPage(page);
        details.reviewsSection(data.getReviewsSection()).click();
        details.reviewCommentInput(data.getReviewCommentPlaceholder()).fill(data.getReviewComment());
        details.reviewSubmitButton(data.getReviewSubmitButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Create a mandatory user story in the issue tracker and return to Calibo")
    public String createNewUserStory(DefineData data) {
        return createNewUserStory(data, data.getStoryType());
    }

    @Step("Click New → {issueType} to open Jira")
    private Page openNewJiraIssue(DefineData data, String issueType) {
        definePage.newUserStory(data.getNewStoryButton()).click();
        return page.waitForPopup(
                new Page.WaitForPopupOptions().setTimeout(data.getPageTimeoutMs()),
                () -> definePage.newUserStoryType(issueType).click());
    }

    public String createNewUserStory(DefineData data, String issueType) {
        captureStoryExecutionIds(data);
        definePage.userStoriesTab(data.getUserStoriesTab()).click();
        Page issuePage = openJiraIssueWithRecovery(data, issueType);
        try {
            waitForJiraPopupOrReloadPrompt(issuePage, data.getPageTimeoutMs());
            if (isJiraHost(issuePage.url())) {
                issuePage.reload(new Page.ReloadOptions().setWaitUntil(
                                com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                        .setTimeout(data.getPageTimeoutMs()));
                LoggerUtil.LOGGER.info("[JIRA-LOGIN] Reloaded story popup. Page title: {}", issuePage.title());
                clickJiraReloadIfPrompted(issuePage);
                if (isJiraHost(issuePage.url())) {
                    waitForJiraReady(issuePage, data.getPageTimeoutMs());
                }
            }
            issuePage.waitForLoadState(LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(data.getPageTimeoutMs()));
            authenticateIssueTracker(issuePage, data);
            UserStoryPage story = new UserStoryPage(issuePage);

            // Jira's create form is an SPA and can continue rendering after the
            // document load event. Wait for the actual form before interacting.
            issuePage.locator("input#summary, input[name='summary'], textarea#summary")
                    .first().waitFor(new Locator.WaitForOptions()
                            .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                            .setTimeout(data.getPageTimeoutMs()));
            Locator summaryField = story.summary("Summary");
            summaryField.waitFor(new Locator.WaitForOptions().setTimeout(data.getPageTimeoutMs()));
            String summary = CommonMethods.generateUniqueTitle(data.getSummary());
            summaryField.fill(summary);
            Assert.assertEquals(summaryField.inputValue(), summary,
                    data.getValidationMessages().get("storySummary"));
            story.description("Description").fill(data.getStoryDescription());

            Locator componentField = story.componentField(data.getComponentFieldLabel());
            componentField.scrollIntoViewIfNeeded();
            selectJiraValue(componentField, data.getComponentName(), story);
            com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                            issuePage.getByText(data.getComponentName(),
                                    new Page.GetByTextOptions().setExact(true)).filter(new Locator.FilterOptions().setVisible(true)).first())
                    .isVisible();

            selectJiraValue(story.reporterField(data.getReporterFieldLabel()), data.getReporterName(), story);
            selectJiraValue(story.sprintField(data.getSprintFieldLabel()), data.getSprintName(), story);

            Locator createButton = story.create(data.getCreateButton());
            createButton.scrollIntoViewIfNeeded();
            AtomicReference<String> createdIssueKey = new AtomicReference<>();
            issuePage.onResponse(response -> captureCreatedIssueKey(response, createdIssueKey));
            createButton.click();
            String storyId = waitForCreatedIssueKey(issuePage, createdIssueKey, data.getPageTimeoutMs());
            executionData.setCreatedStoryId(storyId);
            LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Captured created story ID {} from {}", storyId, issuePage.url());
            return storyId;
        } finally {
            if (!issuePage.isClosed()) {
                issuePage.close();
            }
            page.bringToFront();
            LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Control returned to Calibo: {}", page.url());
        }
    }

    private Page openJiraIssueWithRecovery(DefineData data, String issueType) {
        IllegalStateException lastFailure = null;
        for (int attempt = 1; attempt <= 2; attempt++) {
            Page candidate = openNewJiraIssue(data, issueType);
            try {
                waitForJiraPopupOrReloadPrompt(candidate, data.getPageTimeoutMs());
                return candidate;
            } catch (IllegalStateException error) {
                lastFailure = error;
                boolean blankPopup = !candidate.isClosed() && "about:blank".equals(candidate.url());
                if (!blankPopup || attempt == 2) {
                    if (!candidate.isClosed()) candidate.close();
                    throw error;
                }
                candidate.close();
                LoggerUtil.LOGGER.info("[JIRA-LOGIN] Blank popup for {}; reopening Jira action (attempt 2)", issueType);
            }
        }
        throw lastFailure;
    }

    private void captureCreatedIssueKey(com.microsoft.playwright.Response response,
                                        AtomicReference<String> createdIssueKey) {
        if (createdIssueKey.get() != null || !"POST".equalsIgnoreCase(response.request().method())) return;
        if (!response.url().contains("/rest/api/") || !response.url().contains("/issue")) return;
        try {
            Matcher matcher = Pattern.compile("\\\"key\\\"\\s*:\\s*\\\"([A-Z][A-Z0-9_]*-\\d+)\\\"")
                    .matcher(response.text());
            if (matcher.find()) createdIssueKey.compareAndSet(null, matcher.group(1));
        } catch (com.microsoft.playwright.PlaywrightException ignored) {
            // A navigation can dispose the response body; URL and page-link fallbacks remain available.
        }
    }

    private String waitForCreatedIssueKey(Page issuePage, AtomicReference<String> responseKey, int timeoutMs) {
        Pattern issuePattern = Pattern.compile("(?:/browse/|\\b)([A-Z][A-Z0-9_]*-\\d+)(?:[/?#\\s]|$)");
        long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(timeoutMs);
        while (System.nanoTime() < deadline) {
            if (responseKey.get() != null) return responseKey.get();
            if (!issuePage.isClosed()) {
                Matcher urlMatcher = issuePattern.matcher(issuePage.url());
                if (urlMatcher.find()) return urlMatcher.group(1);
            }
            page.waitForTimeout(250);
        }
        throw new IllegalStateException("Jira did not return the created issue ID within " + timeoutMs
                + " ms. Current URL: " + (issuePage.isClosed() ? "closed" : issuePage.url()));
    }

    @Step("Activate and verify the Calibo main application window")
    public void returnToCaliboMainApplication() {
        Assert.assertFalse(page.isClosed(), "Calibo main application must remain open.");
        page.bringToFront();
        LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Returned to Calibo main application. Title: {}", page.title());
    }

    @Step("Refresh Calibo and verify the created story under its sprint")
    public void verifyCreatedStoryInSprint(DefineData data) {
        String storyId = executionData.getCreatedStoryId();
        Assert.assertNotNull(storyId, "Created story ID must be available for Calibo verification.");
        for (int attempt = 1; attempt <= 3; attempt++) {
            page.reload(new Page.ReloadOptions().setWaitUntil(
                    com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
            CommonMethods.waitForLoaderToDisappear(page);
            try {
                com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                                definePage.storyInSprint(data.getSprintName(), storyId))
                        .isVisible(new com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions()
                                .setTimeout(data.getPageTimeoutMs() / 3.0));
                LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Verified story {} under sprint {}", storyId, data.getSprintName());
                return;
            } catch (AssertionError error) {
                if (attempt == 3) throw error;
                LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Waiting for story {} to synchronize; refreshing Calibo", storyId);
            }
        }
    }

    @Step("Go back, open the feature slider, and verify its User Stories section")
    public void verifyCreatedStoryInFeatureSlider(String featureName, DefineData data) {
        page.goBack(new Page.GoBackOptions().setWaitUntil(
                com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        CommonMethods.waitForLoaderToDisappear(page);
        Locator feature = new pages.PRO.Product.ProductFeatureTabPage(page).matchingFeature(featureName).first();
        feature.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                .setTimeout(data.getPageTimeoutMs()));
        feature.click();
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                        definePage.storyInFeatureSlider(executionData.getCreatedStoryId()))
                .isVisible(new com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions()
                        .setTimeout(data.getPageTimeoutMs()));
        LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Verified story {} in feature slider User Stories", executionData.getCreatedStoryId());
    }

    @Step("Remove the mapped Jira story from the feature slider")
    public void removeMappedUserStory(DefineData data) {
        String storyId = executionData.getCreatedStoryId();
        definePage.mappedStoryActions(storyId).click();
        definePage.feedbackCardAction(data.getRemoveStoryAction()).click();
        Locator dialog = definePage.confirmationDialog(data.getRemoveStoryConfirmation().formatted(storyId));
        definePage.confirmationAction(dialog, data.getRemoveStoryAction()).click();
    }

    @Step("Search for and add the existing Jira story, then validate its details")
    public void addExistingUserStory(DefineData data) {
        Assert.assertFalse(data.getExpectedStoryDetails().isEmpty(), data.getValidationMessages().get("storyDetails"));
        captureStoryExecutionIds(data);
        definePage.existingStorySearch(data.getStorySearchPlaceholder()).fill(data.getExistingStoryId());
        definePage.existingStorySearchResult(data.getExistingStoryId()).hover();
        definePage.addExistingStory(data.getExistingStoryId(), data.getAddStoryButton()).click();
        UI.PRO.CommonProValidations.ProValidation.validateSuccessMessage(page, data.getAddedStoryMessage());
        UI.PRO.CommonProValidations.ProValidation.validateSuccessMessage(page, data.getFixVersionMessage());
        executionData.setCreatedStoryId(data.getExistingStoryId());
        verifyCreatedStoryInSprint(data);
        Locator row = definePage.existingStoryRow(data.getSprintName(), data.getExistingStoryId());
        for (String value : data.getExpectedStoryDetails().values()) {
            com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(row)
                    .containsText(value, new com.microsoft.playwright.assertions.LocatorAssertions.ContainsTextOptions()
                            .setTimeout(data.getPageTimeoutMs()));
        }
        LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Added and verified existing story {}", data.getExistingStoryId());
    }

    private void captureStoryExecutionIds(DefineData data) {
        java.util.Map<String, String> query = new java.util.HashMap<>();
        for (String pair : java.net.URI.create(page.url()).getRawQuery().split("&")) {
            String[] entry = pair.split("=", 2);
            if (entry.length == 2) query.put(entry[0], java.net.URLDecoder.decode(entry[1], java.nio.charset.StandardCharsets.UTF_8));
        }
        executionData.setProductId(query.get("projectId"));
        executionData.setFeatureId(query.get("streamId"));
        Assert.assertNotNull(executionData.getProductId(), data.getValidationMessages().get("productId"));
        Assert.assertNotNull(executionData.getFeatureId(), data.getValidationMessages().get("featureId"));
    }

    private void selectJiraValue(Locator field, String value, UserStoryPage story) {
        field.scrollIntoViewIfNeeded();
        field.click();
        field.fill(value);
        Locator option = story.autocompleteOption(value);
        option.waitFor(new Locator.WaitForOptions().setState(
                com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        option.click();
    }

    private void waitForJiraReady(Page issuePage, int timeoutMs) {
        issuePage.waitForURL(Pattern.compile("https://calibo\\.atlassian\\.net/.*"),
                new Page.WaitForURLOptions().setTimeout(timeoutMs));
        issuePage.locator("input[type='password'], input[name='username'], input[name='email'], "
                        + "input[type='email'], input#summary, input[name='summary'], "
                        + "a[href*='login'], button[data-testid*='login'], "
                        + "button:has-text('Reload'), [role='button']:has-text('Reload'), a:has-text('Reload')")
                .first().waitFor(new Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                        .setTimeout(timeoutMs));
    }

    /** Waits for Jira's popup URL, but recovers if Jira first renders its Reload screen. */
    private void waitForJiraPopupOrReloadPrompt(Page issuePage, int timeoutMs) {
        long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(timeoutMs);
        long blankPopupDeadline = System.nanoTime() + java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(
                Math.min(timeoutMs, 15000));
        boolean reloadClicked = false;
        while (System.nanoTime() < deadline) {
            if (issuePage.isClosed()) {
                throw new IllegalStateException("Jira issue popup closed before reaching Jira.");
            }
            if (!reloadClicked && hasVisibleJiraReloadPrompt(issuePage)) {
                reloadClicked = clickJiraReloadIfPrompted(issuePage);
                continue;
            }
            if (isSupportedJiraLoginUrl(issuePage.url())) return;
            if ("about:blank".equals(issuePage.url()) && System.nanoTime() >= blankPopupDeadline) {
                throw new IllegalStateException("Jira popup remained about:blank after 15 seconds.");
            }
            issuePage.waitForTimeout(250);
        }
        String currentUrl = issuePage.url();
        String currentTitle = issuePage.title();
        LoggerUtil.LOGGER.error("[JIRA-LOGIN] Popup did not reach Jira. Current URL: {}, title: {}",
                currentUrl, currentTitle);
        try {
            issuePage.screenshot(new Page.ScreenshotOptions()
                    .setPath(Path.of("target", "jira-popup-timeout.png"))
                    .setFullPage(true));
        } catch (com.microsoft.playwright.PlaywrightException screenshotError) {
            LoggerUtil.LOGGER.debug("[JIRA-LOGIN] Could not capture popup timeout screenshot: {}",
                    screenshotError.getMessage());
        }
        throw new IllegalStateException("Jira issue popup did not reach Jira or its sign-in page, or show a Reload recovery action within the timeout. URL: "
                + currentUrl);
    }

    private boolean isJiraHost(String url) {
        try {
            return "calibo.atlassian.net".equalsIgnoreCase(java.net.URI.create(url).getHost());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isSupportedJiraLoginUrl(String url) {
        try {
            String host = java.net.URI.create(url).getHost();
            return host != null && (host.equalsIgnoreCase("calibo.atlassian.net")
                    || host.endsWith(".atlassian.net")
                    || host.equalsIgnoreCase("login.microsoftonline.com")
                    || host.endsWith(".microsoftonline.com")
                    || host.equalsIgnoreCase("login.microsoft.com")
                    || host.equalsIgnoreCase("login.windows.net")
                    || host.equalsIgnoreCase("login.live.com")
                    || host.equalsIgnoreCase("id.atlassian.com"));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Jira can intermittently show a recovery screen with a Reload action instead of
     * the sign-in or create-issue form. Treat it as optional and click it when present.
     */
    private boolean clickJiraReloadIfPrompted(Page issuePage) {
        if (issuePage.isClosed()) return false;

        Locator reload = jiraReloadControl(issuePage);
        if (reload == null || !reload.isVisible()) return false;

        LoggerUtil.LOGGER.info("[JIRA-LOGIN] Jira recovery screen detected; clicking Reload");
        reload.click(new Locator.ClickOptions().setTimeout(10000));
        try {
            issuePage.waitForLoadState(LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(15000));
        } catch (com.microsoft.playwright.PlaywrightException e) {
            // Some Jira recovery screens update in place without a document load.
            LoggerUtil.LOGGER.debug("[JIRA-LOGIN] Reload action did not trigger a document load: {}", e.getMessage());
        }
        return true;
    }

    private boolean hasVisibleJiraReloadPrompt(Page issuePage) {
        Locator reload = jiraReloadControl(issuePage);
        return reload != null && reload.isVisible();
    }

    private Locator jiraReloadControl(Page issuePage) {
        Locator reload = issuePage.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Reload").setExact(true));
        if (reload.count() > 0) return reload.first();
        reload = issuePage.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Reload").setExact(true));
        if (reload.count() > 0) return reload.first();
        reload = issuePage.getByText("Reload", new Page.GetByTextOptions().setExact(true));
        return reload.count() > 0 ? reload.first() : null;
    }

    private void authenticateIssueTracker(Page issuePage, DefineData data) {
        Properties credentials = readQaProperties();
        long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(
                data.getPageTimeoutMs() * 3L);
        while (System.nanoTime() < deadline) {
            for (Page authPage : new java.util.ArrayList<>(issuePage.context().pages())) {
                if (authPage == page || authPage.isClosed()) continue;
                try {
                    if (authPage == issuePage && clickJiraReloadIfPrompted(authPage)) {
                        continue;
                    }
                    String host = java.net.URI.create(authPage.url()).getHost();
                    if (host == null) continue;
                    boolean microsoftHost = host.equals("login.microsoftonline.com")
                            || host.endsWith(".microsoftonline.com")
                            || host.equals("login.microsoft.com")
                            || host.equals("login.windows.net")
                            || host.equals("login.live.com");
                    boolean jiraHost = host.equals("calibo.atlassian.net");
                    boolean atlassianHost = host.equals("id.atlassian.com");
                    if (!microsoftHost && !jiraHost && !atlassianHost) continue;
                    if (authPage == issuePage && jiraHost) {
                        Locator summary = authPage.locator("input#summary:visible, input[name='summary']:visible");
                        if (summary.count() > 0) {
                            LoggerUtil.LOGGER.info("[JIRA-LOGIN] Verified create-story form. Page title: {}", authPage.title());
                            return;
                        }

                    }
                    if (microsoftHost) {
                        Locator email = authPage.locator("input[type='email']:visible");
                        Locator password = authPage.locator("input[type='password']:visible");
                        if (email.count() > 0) {
                            email.first().fill(requiredProperty(credentials, data.getUserCredentialKey()));
                            authPage.locator("input[type='submit']").click();
                        } else if (password.count() > 0) {
                            password.first().fill(requiredProperty(credentials, data.getPasswordCredentialKey()));
                            authPage.locator("input[type='submit']").click();
                        } else {
                            Locator account = authPage.getByText(requiredProperty(credentials, data.getUserCredentialKey()),
                                    new Page.GetByTextOptions().setExact(true));
                            Locator yes = authPage.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                                    new Page.GetByRoleOptions().setName("Yes").setExact(true));
                            if (yes.count() > 0 && yes.first().isVisible()) {
                                LoggerUtil.LOGGER.info("[JIRA-LOGIN] Stay signed in: clicking Yes");
                                yes.first().click();
                            } else if (account.count() > 0 && account.first().isVisible()) account.first().click();
                        }
                    } else {
                        Locator provider = authPage.locator("button:visible, a:visible")
                                .filter(new Locator.FilterOptions().setHasText(Pattern.compile("Microsoft", Pattern.CASE_INSENSITIVE)));
                        Locator login = authPage.locator("a:visible, button:visible")
                                .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Log\\s*in$", Pattern.CASE_INSENSITIVE)));
                        if (provider.count() > 0) provider.first().click();
                        else if (login.count() > 0) login.first().click();
                    }
                } catch (com.microsoft.playwright.PlaywrightException e) {
                    // Authentication redirects can replace the document during a locator check.
                    if (!authPage.isClosed() && !e.getMessage().contains("Execution context was destroyed")) {
                        throw e;
                    }
                }
            }
            issuePage.waitForTimeout(500);
        }
        throw new IllegalStateException("Jira Microsoft sign-in did not reach the create-story form within the timeout.");
    }

    private Properties readQaProperties() {
        Path propertiesPath = Path.of("src/test/resources/config/envConfig/ui/qa.properties");
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(propertiesPath)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read QA UI credentials from " + propertiesPath, e);
        }
    }

    private String requiredProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required UI credential property is missing: " + key);
        }
        return value;
    }

    private void storeBusinessRequirementExecutionData(String defineTitle) {

        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        FeatureExecutionData featureExecutionData =
                productExecutionData.getFeatures()
                        .get(productExecutionData.getFeatures().size() - 1);

        featureExecutionData.getBusinessRequirementNames()
                .add(defineTitle);
    }
}
