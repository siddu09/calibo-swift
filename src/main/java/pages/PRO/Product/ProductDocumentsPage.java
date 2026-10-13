package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductDocumentsPage {
    private final Page page;

    public ProductDocumentsPage(Page page) {
        this.page = page;
    }

    public Locator typeDropdown(String label) {
        return new ResilientLocator(page, "Document type dropdown")
                .byXPath("//label[normalize-space()='" + label + "']/parent::div//*[contains(@class,'react-select__control')]")
                .byLabel(label)
                .byRole(AriaRole.COMBOBOX, label)
                .resolve();
    }

    public Locator typeOption(String type) {
        return new ResilientLocator(page, "Document type: " + type)
                .byXPath("//*[contains(@class,'react-select__option')][normalize-space()='" + type + "']")
                .byRole(AriaRole.OPTION, type)
                .custom("Exact type in dropdown", () -> page.locator("[class*='menu-list']")
                        .getByText(type, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator field(String label) {
        return new ResilientLocator(page, "Document field: " + label)
                .custom("Input following exact field caption", () -> page.getByText(label,
                        new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=following::*[self::input or self::textarea][1]"))
                .byLabel(label)
                .byRole(AriaRole.TEXTBOX, label)
                .byXPath("//label[normalize-space()='" + label + "']/parent::div//*[self::input or self::textarea]")
                .resolve();
    }

    public Locator addButton(String label) {
        return new ResilientLocator(page, "Save product document")
                .custom("Exact Add button", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//button[normalize-space()='" + label + "' and not(@disabled)]")
                .resolve();
    }

    public Locator documentName(String name) {
        return new ResilientLocator(page, "Added document: " + name)
                .byRole(AriaRole.LINK, name)
                .custom("Exact document name", () -> page.getByText(name,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }
}
