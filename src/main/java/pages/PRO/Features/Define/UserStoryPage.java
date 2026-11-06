package pages.PRO.Features.Define;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/** Locators for the issue tracker page opened from a feature's User Stories tab. */
public class UserStoryPage {
    private final Page page;

    public UserStoryPage(Page page) {
        this.page = page;
    }

    public Locator loginUser() {
        return new ResilientLocator(page, "Issue tracker username")
                .byName("username")
                .byName("email")
                .byCss("input[autocomplete='username'], input[type='email']")
                .byXPath("//input[@name='username' or @name='email' or @autocomplete='username']")
                .resolve();
    }

    public Locator loginPassword() {
        return new ResilientLocator(page, "Issue tracker password")
                .byName("password")
                .byCss("input[type='password']")
                .byXPath("//input[@type='password' and not(@disabled)]")
                .resolve();
    }

    public Locator loginSubmit(String label) {
        return new ResilientLocator(page, "Issue tracker login submit")
                .byRole(AriaRole.BUTTON, label)
                .byCss("button[type='submit']")
                .byXPath("//button[@type='submit'] | //input[@type='submit']")
                .resolve();
    }

    public Locator loginLink(String label) {
        return new ResilientLocator(page, "Issue tracker login link")
                .byRole(AriaRole.LINK, label)
                .byRole(AriaRole.BUTTON, label)
                .byCss("a[href*='login'], button[data-testid*='login']")
                .resolve();
    }

    public Locator issueType(String label) {
        return new ResilientLocator(page, "Story type selector")
                .custom("Issue type field", () -> page.getByLabel(label, new Page.GetByLabelOptions().setExact(true)))
                .byCss("[data-testid*='issue-type'] [role='combobox']")
                .byCss("[name='issuetype']")
                .byXPath("//*[self::label or self::span][normalize-space()='" + label
                        + "']/following::*[@role='combobox' or self::select][1]")
                .resolve();
    }

    public Locator option(String value) {
        return new ResilientLocator(page, "Issue form option")
                .custom("Exact visible option", () -> page.getByText(value,
                        new Page.GetByTextOptions().setExact(true)))
                .byRole(AriaRole.OPTION, value)
                .byXPath("//*[@role='option' and normalize-space()='" + value + "']")
                .resolve();
    }

    public Locator summary(String label) {
        return new ResilientLocator(page, "Story summary field")
                .byLabel(label)
                .byName("summary")
                .byPlaceholder(label)
                .byCss("input[id*='summary'], input[name*='summary']")
                .resolve();
    }

    public Locator componentField(String label) {
        return new ResilientLocator(page, "Jira component field")
                .byLabel(label)
                .byCss("#components-textarea, input[id*='components'], input[name*='components']")
                .byXPath("//*[self::label or self::span][normalize-space()='" + label
                        + "']/following::input[1]")
                .resolve();
    }

    public Locator reporterField(String label) {
        return new ResilientLocator(page, "Jira reporter field")
                .byLabel(label)
                .byCss("#reporter, input[id*='reporter'], input[name*='reporter']")
                .byXPath("//*[self::label or self::span][normalize-space()='" + label
                        + "']/following::input[1]")
                .resolve();
    }

    public Locator sprintField(String label) {
        return new ResilientLocator(page, "Jira sprint field")
                .byLabel(label)
                .byCss("input[id*='sprint'], input[name*='sprint'], [data-testid*='sprint'] input")
                .byXPath("//*[self::label or self::span][normalize-space()='" + label
                        + "']/following::input[1]")
                .resolve();
    }

    public Locator autocompleteOption(String value) {
        return new ResilientLocator(page, "Jira autocomplete option: " + value)
                .custom("Exact option in autocomplete results", () -> page.locator(
                                "[role='listbox'], [role='menu'], .aui-list, .suggestions, [class*='suggestion']")
                        .getByText(value, new Locator.GetByTextOptions().setExact(true)))
                .byRole(AriaRole.OPTION, value)
                .custom("Exact visible option", () -> page.getByText(value,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator description(String label) {
        return new ResilientLocator(page, "Story description field")
                .byLabel(label)
                .byName("description")
                .byCss("textarea[id*='description'], textarea[name*='description'], [contenteditable='true'][role='textbox']")
                .byXPath("//label[normalize-space()='" + label + "']/following::*[self::textarea or @contenteditable='true'][1]")
                .resolve();
    }

    public Locator componentsTab(String label) {
        return new ResilientLocator(page, "Components tab in story form")
                .byRole(AriaRole.TAB, label)
                .byCss("[data-testid*='component']")
                .byCss("[role='tab'][id*='component']")
                .byXPath("//*[@role='tab' and normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator componentSearch(String label) {
        return new ResilientLocator(page, "Story component search")
                .byPlaceholder(label)
                .byLabel(label)
                .byCss("input[aria-autocomplete='list']")
                .byXPath("//input[contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'component')]")
                .resolve();
    }

    public Locator componentOption(String name) {
        return new ResilientLocator(page, "Story component option")
                .custom("Exact component result", () -> page.locator("[role='listbox'], [class*='menu']")
                        .getByText(name, new Locator.GetByTextOptions().setExact(true)))
                .byRole(AriaRole.OPTION, name)
                .byXPath("//*[@role='option' and normalize-space()='" + name + "']")
                .resolve();
    }

    public Locator selectedComponent(String name) {
        return new ResilientLocator(page, "Selected story component")
                .custom("Selected component chip", () -> page.locator(
                                "[class*='multi-value'], [data-testid*='selected-component']")
                        .getByText(name, new Locator.GetByTextOptions().setExact(true)))
                .byCss("[data-testid*='component-chip']")
                .byXPath("//*[contains(@class,'multi-value__label') and normalize-space()='" + name + "']")
                .resolve();
    }

    public Locator create(String label) {
        return new ResilientLocator(page, "Create user story")
                .byCss("#issue-create-submit, form input[type='submit'][value='Create'], form button[type='submit']")
                .custom("Exact Create button", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byCss("button[type='submit']")
                .byXPath("//button[@type='submit' and not(@disabled)]")
                .resolve();
    }

    public Locator createdStoryMessage(String pattern) {
        // Return a lazy locator so waiting begins immediately, without locator-healing delays.
        java.util.regex.Pattern message = java.util.regex.Pattern.compile(pattern,
                java.util.regex.Pattern.CASE_INSENSITIVE);
        return page.locator("[role='alert'], [role='status'], .aui-message-success, .toast, .notification")
                .filter(new Locator.FilterOptions().setHasText(message))
                .or(page.getByText(message)).filter(new Locator.FilterOptions().setVisible(true)).first();
    }
}
