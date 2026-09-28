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
}
