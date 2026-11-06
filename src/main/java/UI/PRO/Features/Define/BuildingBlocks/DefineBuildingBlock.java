package UI.PRO.Features.Define.BuildingBlocks;

import UI.PRO.datahelper.DefineData;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import pages.PRO.Features.Define.DefinePage;
import pages.PRO.Features.Define.UserStoryPage;
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
        Page issuePage = openNewJiraIssue(data, issueType);
        try {
            issuePage.waitForURL(Pattern.compile("https://calibo\\.atlassian\\.net/.*"),
                    new Page.WaitForURLOptions().setTimeout(data.getPageTimeoutMs()));
            issuePage.reload(new Page.ReloadOptions().setWaitUntil(
                    com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                    .setTimeout(data.getPageTimeoutMs()));
            LoggerUtil.LOGGER.info("[JIRA-LOGIN] Reloaded story popup. Page title: {}", issuePage.title());
            waitForJiraReady(issuePage, data.getPageTimeoutMs());
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
            createButton.click();
            issuePage.waitForURL(Pattern.compile("https://calibo\\.atlassian\\.net/browse/[A-Z][A-Z0-9_]*-\\d+(?:[/?#].*)?"),
                    new Page.WaitForURLOptions().setTimeout(data.getPageTimeoutMs()));
            Matcher matcher = Pattern.compile("/browse/([A-Z][A-Z0-9_]*-\\d+)").matcher(issuePage.url());
            Assert.assertTrue(matcher.find(), "Created story URL must contain its generated issue ID.");
            String storyId = matcher.group(1);
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
        new pages.PRO.Product.ProductFeatureTabPage(page).clickOnFeatureName(featureName).click();
        definePage.featureSliderUserStories().scrollIntoViewIfNeeded();
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                        definePage.storyInFeatureSlider(executionData.getCreatedStoryId()))
                .isVisible(new com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions()
                        .setTimeout(data.getPageTimeoutMs()));
        LoggerUtil.LOGGER.info("[DEFINE-BLOCK] Verified story {} in feature slider User Stories", executionData.getCreatedStoryId());
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
                        + "a[href*='login'], button[data-testid*='login']")
                .first().waitFor(new Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                        .setTimeout(timeoutMs));
    }

    private void authenticateIssueTracker(Page issuePage, DefineData data) {
        Properties credentials = readQaProperties();
        long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(
                data.getPageTimeoutMs() * 3L);
        while (System.nanoTime() < deadline) {
            for (Page authPage : new java.util.ArrayList<>(issuePage.context().pages())) {
                if (authPage == page || authPage.isClosed()) continue;
                try {
                    String host = java.net.URI.create(authPage.url()).getHost();
                    if (host == null) continue;
                    boolean microsoftHost = host.equals("login.microsoftonline.com") || host.equals("login.live.com");
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
