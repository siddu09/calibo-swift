package pages.PRO.Features.Define;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;


public class DefinePage {

    private final Page page;

    public DefinePage(Page page) {
        this.page = page;
    }

    // -------------------------------------------------------------------------
    // Common Button
    // -------------------------------------------------------------------------

    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath(
                        "/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]" +
                                "/div[1]/div[1]/div[3]/div[2]/div[1]/button[1]"
                )
                .resolve();
    }

    // -------------------------------------------------------------------------
    // Business Requirement
    // -------------------------------------------------------------------------

    public Locator addNewBusinessRequirement(
            String businessRequirement) {

        return new ResilientLocator(
                page,
                "New BR"
        )
                .byText(businessRequirement)
                .byXPath(
                        "//button[text()='" +
                                businessRequirement +
                                "']"
                )
                .resolve();
    }

    // -------------------------------------------------------------------------
    // Dropdown
    // -------------------------------------------------------------------------

    /**
     * IMPORTANT:
     * Keep this locator as-is because it matches the existing
     * application behavior.
     */
    public Locator selectDropdownValue(String dropDown) {

        return page.locator(
                "//label[text()='" + dropDown + "']"
        );
    }

    public Locator dropDownArrow() {

        return new ResilientLocator(
                page,
                "dropDownArrow"
        )
                .byXPath("//i[text()='arrow_drop_down']")
                .resolve();
    }

    public void selectDropDown(
            String dropDownName,
            String dropDownValue) {

        page.locator(
                        "label:text('" + dropDownName + "')"
                )
                .locator(
                        "xpath=following::div" +
                                "[contains(@class,'react-select__control')][1]"
                )
                .click();

        selectDropdownValue(dropDownValue).click();

        LoggerUtil.LOGGER.info(
                dropDownValue +
                        " Selected DropDown Value"
        );
    }

    // -------------------------------------------------------------------------
    // Form Fields
    // -------------------------------------------------------------------------

    public Locator title() {

        return new ResilientLocator(
                page,
                "Name"
        )
                .byCss("input[label='Title']")
                .byCss("input[data-testid*='title']")
                .byCss("input[name='title']")
                .byXPath("//*[@label='Title']")
                .resolve();
    }

    public Locator description() {

        return new ResilientLocator(
                page,
                "Description"
        )
                .byCss("div[contenteditable='true'][role='textbox']")
                .byCss("[data-testid*='description'] [contenteditable='true']")
                .byCss("textarea[name='description']")
                .byXPath(
                        "//div[@role='textbox']"
                )
                .resolve();
    }

    public Locator addUserName() {

        return new ResilientLocator(
                page,
                "Name"
        )
                .byCss("input[name='userName']")
                .byCss("input[name='username']")
                .byCss("input[data-testid*='user-name']")
                .byCss("input[aria-label='User Name']")
                .byXPath("//*[@name='userName']")
                .byXPath("//label[normalize-space()='User Name']/following::input[1]")
                .resolve();
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    public Locator create() {

        return new ResilientLocator(
                page,
                "Create"
        )
                .byXPath("//button[text()='Create']")
                .resolve();
    }

    public Locator selectBusinessRequirement(String BRName) {
        return new ResilientLocator(page, "Select BusinessRequirement")
                .byText(BRName)
                .resolve();
    }

    public Locator feedbackSearch(String placeholder) {
        return new ResilientLocator(page, "Business Requirement and User Feedback search")
                .byCss("input[data-testid*='business-requirement-search']")
                .byCss("input[name*='businessRequirementSearch']")
                .byCss("input[aria-label*='Business Requirement']")
                .byPlaceholder(placeholder)
                .byXPath("//input[contains(@placeholder,'Business Requirement') and contains(@placeholder,'User Feedback')]")
                .resolve();
    }

    public Locator feedbackActions(String title) {
        return defineItemActions(feedbackAndRequirementSearchResults(title), title);
    }

    public Locator mappedStoryActions(String storyId) {
        return defineItemActions(storyInFeatureSlider(storyId), storyId)
                .and(featureSliderUserStories().locator("button, i"));
    }

    private Locator defineItemActions(Locator result, String title) {
        String actionButton = "button[.//*[normalize-space()='more_horiz' or normalize-space()='more_vert']"
                + " or @aria-label='More actions' or @title='More actions']";
        return new ResilientLocator(page, "Actions for Define item: " + title)
                .custom("More actions in the matching item",
                        () -> result.locator("xpath=ancestor::*[.//" + actionButton + "][1]//" + actionButton))
                .custom("More icon in the matching item",
                        () -> result.locator("xpath=ancestor::div[.//i[normalize-space()='more_horiz'"
                                + " or normalize-space()='more_vert']][1]//i[normalize-space()='more_horiz'"
                                + " or normalize-space()='more_vert']"))
                .resolve();
    }

    public Locator feedbackCardAction(String label) {
        return new ResilientLocator(page, "Define item action: " + label)
                .byRole(AriaRole.MENUITEM, label)
                .custom("Exact card action", () -> page.getByText(label,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator confirmationDialog(String message) {
        return page.locator("[data-testid='confirmation-modal'], [role='dialog'], "
                        + ".modal-content:not([role='dialog'] .modal-content)")
                .filter(new Locator.FilterOptions().setVisible(true).setHas(
                        page.getByText(message, new Page.GetByTextOptions().setExact(true))));
    }

    public Locator feedbackDeleteComment(Locator dialog) {
        return dialog.getByRole(AriaRole.TEXTBOX);
    }

    public Locator confirmationAction(Locator dialog, String label) {
        return dialog.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(label).setExact(true));
    }

    public Locator editFeedbackTitle() {
        return page.locator("input.form-control:visible")
                .and(page.locator("input[name='title'], input[placeholder='Title'], input[label='Title'], "
                        + "input:not([name]):not([placeholder]), "
                        + "input[name=''][placeholder=''], input[name='']:not([placeholder]), "
                        + "input:not([name])[placeholder='']"));
    }

    public Locator editFeedbackDescription() {
        return page.locator("div[contenteditable='true'][role='textbox']:visible, "
                + "[data-testid*='description'] [contenteditable='true']:visible, "
                + "textarea[name='description']:visible");
    }

    public Locator saveFeedbackButton(String label) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(label).setExact(true));
    }

    public Locator feedbackAndRequirementSearchResults(String title) {
        return page.getByText(title, new Page.GetByTextOptions().setExact(true));
    }

    public Locator searchedUserFeedback(String title) {
        return new ResilientLocator(page, "Searched User Feedback")
                .byCss("[data-testid*='business-requirement-result']")
                .byCss("[data-testid*='user-feedback-result']")
                .byXPath("//*[@role='row' or @role='option'][.//*[normalize-space()='" + title + "']]")
                .byXPath("//*[contains(@class,'business-requirement') or contains(@class,'feedback')][normalize-space()='" + title + "']")
                .custom("Exact generated User Feedback title", () -> page.getByText(title,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator businessRequirementsTab() {
        return new ResilientLocator(page, "Business Requirements tab")
                .byRole(AriaRole.TAB, "Business Requirements")
                .byCss("[data-tour*='business-requirement']")
                .byCss("[role='tab'][href*='business-requirement']")
                .byXPath("//*[@role='tab' and contains(@href,'requirement')]")
                .resolve();
    }

    public Locator newUserFeedbackAction() {
        return new ResilientLocator(page, "New User Feedback action")
                .byCss("button[data-tour*='user-feedback']")
                .byCss("button[data-testid*='user-feedback']")
                .byXPath("//button[contains(@aria-label,'User Feedback')]")
                .byRole(AriaRole.BUTTON, "New User Feedback")
                .resolve();
    }

    public Locator newBusinessRequirementAction() {
        return new ResilientLocator(page, "New Business Requirement action")
                .byCss("button[data-tour*='business-requirement']")
                .byCss("button[data-testid*='business-requirement']")
                .byXPath("//button[contains(@aria-label,'Business Requirement')]")
                .byRole(AriaRole.BUTTON, "New Business Requirement")
                .resolve();
    }

    public Locator businessRequirementImportOption(String option) {
        return page.getByText(option, new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
    }

    public Locator businessRequirementImportBrowseButton() {
        return page.getByText("Browse this computer", new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
    }

    public Locator businessRequirementImportButton(String label) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(label).setExact(true));
    }

    public Locator createNewBusinessRequirementOption(String option) {
        return new ResilientLocator(page, "Create New Business Requirement option")
                .byCss("[data-testid*='create-new-business-requirement']")
                .byCss("[data-value='create-new']")
                .byXPath("//*[@role='menuitem' or @role='option'][contains(@data-action,'create')]")
                .custom("Create New option in open action menu", () -> page.getByText(option,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator userFeedbackType() {
        return new ResilientLocator(page, "User feedback type")
                .custom("User choice in the open action menu", () -> page.locator(".popover, [role='tooltip'], [role='menu']")
                        .getByText("User", new Locator.GetByTextOptions().setExact(true)))
                .byCss(".popover button[data-tour*='user']")
                .byXPath("//*[contains(@class,'popover') or @role='tooltip']//button[normalize-space()='User']")
                .byCss("[data-testid='user-feedback-type-user']")
                .byText("User")
                .resolve();
    }

    public Locator feedbackDropdown(String fieldName) {
        String fieldKey = fieldName.replace(" ", "-").toLowerCase();
        return new ResilientLocator(page, fieldName + " dropdown")
                .byXPath("//label[normalize-space()='" + fieldName + "']/following::*[contains(@class,'react-select__control')][1]")
                .byCss("[data-testid*='" + fieldKey + "'] .react-select__control")
                .byCss("[id*='" + fieldKey + "'] .react-select__control")
                .custom("Control associated with field label", () -> page.getByText(fieldName,
                                new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=following::*[contains(@class,'react-select__control')][1]"))
                .resolve();
    }

    public Locator feedbackDropdownInput(String fieldName) {
        String fieldKey = fieldName.replace(" ", "-").toLowerCase();
        return new ResilientLocator(page, fieldName + " dropdown input")
                .byXPath("//label[normalize-space()='" + fieldName + "']/following::input[1]")
                .byCss("[data-testid*='" + fieldKey + "'] input")
                .byCss("[id*='" + fieldKey + "'] input")
                .custom("Input inside field control", () -> feedbackDropdown(fieldName).locator("input"))
                .resolve();
    }

    public Locator createDropdownValue(String value) {
        return new ResilientLocator(page, "Create dropdown value")
                .custom("Exact creatable-select prompt", () -> page.getByText("Create \"" + value + "\"",
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//*[normalize-space()=concat('Create ', '&quot;', '" + value + "', '&quot;')]")
                .byCss("[class*='menu'] [class*='option']")
                .byXPath("//*[contains(@class,'menu')]//*[contains(normalize-space(.),'Create')]")
                .custom("Create option containing entered value", () -> page.getByRole(AriaRole.OPTION)
                        .filter(new Locator.FilterOptions().setHasText(value)))
                .resolve();
    }

    public Locator feedbackDropdownOption(String value) {
        return new ResilientLocator(page, "Define dropdown option: " + value)
                .byRole(AriaRole.OPTION, value)
                .byXPath("//*[@role='option' and normalize-space()='" + value + "']")
                .byXPath("//*[contains(@class,'menu')]//*[contains(@class,'option') and normalize-space()='" + value + "']")
                .custom("Exact visible dropdown value", () -> page.getByText(value,
                        new Page.GetByTextOptions().setExact(true)))
                .byCss("[class*='menu'] [class*='option']")
                .resolve();
    }

    public Locator feedbackValidationMessage(String message) {
        return page.getByText(message, new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
    }

    public Locator createUserFeedbackButton() {
        return new ResilientLocator(page, "Create user feedback")
                .byRole(AriaRole.BUTTON, "Create")
                .byCss("button[type='submit']:not([disabled])")
                .byCss("[data-testid*='create-user-feedback']")
                .byXPath("//form//button[@type='submit' and not(@disabled)]")
                .resolve();
    }

    public Locator confluenceDocumentationToggle(String prompt) {
        return new ResilientLocator(page, "Confluence documentation toggle")
                .byCss("input[name*='confluence'][type='checkbox']")
                .byCss("[data-testid*='confluence'] input[type='checkbox']")
                .byXPath("//*[contains(@class,'confluence')]//input[@type='checkbox']")
                .custom("Toggle associated with documentation prompt", () -> page.getByText(prompt,
                                new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=following::input[@type='checkbox'][1]"))
                .resolve();
    }

    public Locator confluencePageTitle() {
        return new ResilientLocator(page, "Confluence Page Title")
                .byCss("input[name='confluencePageTitle']")
                .byCss("input[data-testid*='confluence-page-title']")
                .byXPath("//label[normalize-space()='Confluence Page Title']/following::input[1]")
                .byPlaceholder("Confluence Page Title")
                .resolve();
    }

    public Locator userStoriesTab(String label) {
        return new ResilientLocator(page, "User Stories tab")
                .byRole(AriaRole.TAB, label)
                .byCss("[data-testid*='user-story']")
                .byCss("[role='tab'][href*='story']")
                .byXPath("//*[@role='tab' and normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator newUserStory(String label) {
        return new ResilientLocator(page, "New user story action")
                // The action is the + New button aligned to the right of the story
                // search field. Its accessible name includes the leading plus icon.
                .custom("+ New action beside story search", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(label)))
                .byCss("button[data-testid*='new-story']")
                .byCss("button[aria-label*='new']")
                .byCss("button:has-text('" + label + "')")
                .byXPath("//button[contains(normalize-space(.), '" + label + "')]")
                .resolve();
    }

    public Locator newUserStoryType(String label) {
        return new ResilientLocator(page, "New user story type: " + label)
                .custom("Exact visible story type option", () -> page.getByText(label,
                        new Page.GetByTextOptions().setExact(true)))
                .byText(label)
                .resolve();
    }
    public Locator storyInSprint(String sprint, String storyId) {
        return page.getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setName(sprint).setExact(true))
                .locator("xpath=ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' card ')][1]")
                .getByText(storyId, new Locator.GetByTextOptions().setExact(true));
    }

    public Locator featureSliderUserStories() {
        return page.locator("#menu.trial-side-menu")
                .getByRole(AriaRole.HEADING,
                        new Locator.GetByRoleOptions().setName("User Stories").setExact(true))
                .locator("..");
    }

    public Locator storyInFeatureSlider(String storyId) {
        return featureSliderUserStories().getByText(storyId,
                new Locator.GetByTextOptions().setExact(true));
    }

    public Locator existingStorySearch(String placeholder) {
        return new ResilientLocator(page, "Existing story search")
                .byCss("[data-tour='reactour__define-search-stories'] input#search")
                .byPlaceholder(placeholder)
                .byXPath("//input[@id='search' and @type='text'][1]")
                .resolve();
    }

    public Locator existingStorySearchResult(String storyId) {
        return new ResilientLocator(page, "Exact existing story search result")
                .withFirstStrategyTimeout(10000)
                .custom("Story ID in search popover", () -> page.getByRole(AriaRole.TOOLTIP)
                        .getByText(storyId, new Locator.GetByTextOptions().setExact(true)))
                .custom("Story ID in search result list", () -> page.locator("[data-tour='reactour__define-search-stories'] .popover")
                        .getByText(storyId, new Locator.GetByTextOptions().setExact(true)))
                .byXPath("//*[@role='tooltip']//*[normalize-space()='" + storyId + "']")
                .resolve();
    }

    public Locator addExistingStory(String storyId, String buttonLabel) {
        return new ResilientLocator(page, "Add searched Jira story")
                .custom("Add button beside exact search result", () -> existingStorySearchResult(storyId)
                        .locator("xpath=ancestor::*[.//button[normalize-space()='" + buttonLabel + "']][1]")
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(buttonLabel).setExact(true)))
                .custom("Add button in matching result row", () -> page.getByRole(AriaRole.ROW)
                        .filter(new Locator.FilterOptions().setHasText(storyId))
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(buttonLabel).setExact(true)))
                .byXPath("//*[normalize-space()='" + storyId + "']/ancestor::*[.//button[normalize-space()='" + buttonLabel + "']][1]//button[normalize-space()='" + buttonLabel + "']")
                .resolve();
    }

    public Locator existingStoryRow(String sprint, String storyId) {
        return new ResilientLocator(page, "Existing story in configured sprint")
                .custom("Story row in sprint card", () -> storyInSprint(sprint, storyId).locator("xpath=ancestor::tr[1]"))
                .custom("Sprint accordion story row", () -> page.locator(".plan-items-accordion")
                        .filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.HEADING,
                                new Page.GetByRoleOptions().setName(sprint).setExact(true))))
                        .locator("tr").filter(new Locator.FilterOptions().setHasText(storyId)))
                .byXPath("//div[contains(@class,'card')][.//h4[normalize-space()='" + sprint + "']]//tr[.//a[contains(@href,'/browse/" + storyId + "')]]")
                .resolve();
    }

}
