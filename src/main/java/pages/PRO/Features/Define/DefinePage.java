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
                .byXPath("//*[@label='Title']")
                .resolve();
    }

    public Locator description() {

        return new ResilientLocator(
                page,
                "Description"
        )
                .byCss(
                        "div[contenteditable='true'][role='textbox']"
                )
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
                .byXPath("//*[@name='userName']")
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
