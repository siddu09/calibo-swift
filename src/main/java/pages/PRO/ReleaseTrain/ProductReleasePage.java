package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductReleasePage {
    private final Page page;

    public ProductReleasePage(Page page) {
        this.page = page;
    }

    public Locator field(String label) {
        return new ResilientLocator(page, "Product release field: " + label)
                .byXPath("//label[normalize-space()='" + label
                        + "']/parent::div//*[self::input or self::textarea or @contenteditable='true']")
                .custom("Editable control beside field label", () -> page.getByText(label,
                                new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=..")
                        .locator("input, textarea, [contenteditable='true']"))
                .resolve();
    }

    public Locator tab(String label) {
        return new ResilientLocator(page, "Product release tab")
                .byRole(AriaRole.TAB, label)
                .byXPath("//a[@role='tab'][normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator button(String label) {
        return new ResilientLocator(page, "Release action: " + label)
                .custom("Exact release action", () -> {
                    Locator button = page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName(label).setExact(true));
                    button.waitFor();
                    return button;
                })
                .byXPath("//button[normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator title(String title) {
        return new ResilientLocator(page, "Release dialog title")
                .byRole(AriaRole.HEADING, title)
                .byXPath("//*[self::h2 or self::h3 or self::h4][normalize-space()='" + title + "']")
                .resolve();
    }

    public Locator release(String name) {
        return new ResilientLocator(page, "Joined release: " + name)
                .custom("Joined release name", () -> {
                    Locator release = page.getByText(name, new Page.GetByTextOptions().setExact(true));
                    release.first().waitFor();
                    return release;
                })
                .byRole(AriaRole.LINK, name)
                .resolve();
    }

    public Locator releaseActions(String name) {
        return new ResilientLocator(page, "Actions for joined release: " + name)
                .byXPath("//*[normalize-space(text())='" + name
                        + "']/ancestor::div[.//button[.//*[normalize-space()='more_horiz' or normalize-space()='more_vert']]][1]//button[.//*[normalize-space()='more_horiz' or normalize-space()='more_vert']]")
                .custom("Button within named release card", () -> release(name)
                        .locator("xpath=ancestor::div[.//button][1]").getByRole(AriaRole.BUTTON))
                .resolve();
    }

    public Locator releaseMenuAction(String label) {
        return new ResilientLocator(page, "Product release menu action: " + label)
                .byText(label)
                .byRole(AriaRole.MENUITEM, label)
                .resolve();
    }

    public Locator leaveComments(String placeholder) {
        return new ResilientLocator(page, "Leave release comments")
                .byPlaceholder(placeholder)
                .byXPath("//*[@role='dialog']//textarea")
                .resolve();
    }

    public Locator back(String label) {
        return new ResilientLocator(page, "Back from Join Release")
                .byXPath("//*[@id='app-layout-header']//button[.//i[normalize-space()='chevron_left']]")
                .byRole(AriaRole.BUTTON, label)
                .resolve();
    }

    public Locator mandatoryFieldError(String message) {
        return new ResilientLocator(page, "Mandatory field warning: " + message)
                .custom("Exact validation message", () -> page.getByText(message,
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//span[normalize-space()='" + message + "']")
                .resolve();
    }

}
