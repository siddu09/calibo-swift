package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/** Controls specific to editing an existing Design; shared form fields are inherited. */
public class EditDesignPage extends ResilientDesignPage {
    private final Page page;

    public EditDesignPage(Page page) {
        super(page);
        this.page = page;
    }

    public Locator edit(String label) {
        return new ResilientLocator(page, "Edit the saved Design")
                .byRole(AriaRole.BUTTON, label)
                .byCss(".dropdown-menu button[text='" + label + "']")
                .byRole(AriaRole.MENUITEM, label)
                .byXPath("//*[contains(@class,'dropdown-menu')]//*[self::button or self::a][normalize-space()='" + label + "']")
                .resolve();
    }

    @Override
    public Locator name() {
        return new ResilientLocator(page, "Edited Design name")
                .byCss(".edit-design-block input[label='Name']")
                .byXPath("//div[contains(@class,'edit-design-block')]//input[@label='Name']")
                .custom("Name textbox in the edit form", () -> page.locator(".edit-design-block .custom-form-bg")
                        .getByRole(AriaRole.TEXTBOX).first())
                .resolve();
    }

    @Override
    public Locator description() {
        return new ResilientLocator(page, "Edited Design description")
                .byCss(".edit-design-block .custom-form-bg textarea")
                .byXPath("//div[contains(@class,'edit-design-block')]//textarea[@rows='3']")
                .custom("Description textbox in the edit form", () -> page.locator(".edit-design-block")
                        .getByRole(AriaRole.TEXTBOX).and(page.locator("textarea")))
                .resolve();
    }

    public Locator clearRequirement() {
        return new ResilientLocator(page, "Remove the associated Design requirement")
                .byCss(".edit-design-block .react-select__clear-indicator")
                .byXPath("//div[contains(@class,'edit-design-block')]//div[contains(@class,'react-select__clear-indicator')]")
                .custom("Clear action in the existing requirement control", () -> businessRequirementControl()
                        .locator(".react-select__clear-indicator"))
                .resolve();
    }

    public Locator editLink(String url) {
        return new ResilientLocator(page, "Edit the existing Design link")
                .byCss(".edit-design-block tr:has(a[href='" + url + "']) .action-btn-wrapper > button:first-child")
                .byXPath("//a[@href='" + url + "']/ancestor::tr//button[1]")
                .custom("First action in the existing link row", () -> page.locator(".add-design-table tbody tr")
                        .filter(new Locator.FilterOptions().setHas(page.locator("a[href='" + url + "']")))
                        .getByRole(AriaRole.BUTTON).first())
                .resolve();
    }

    public Locator inlineLinkField(boolean title) {
        String column = title ? "1" : "2";
        return new ResilientLocator(page, "Inline Design link " + (title ? "title" : "URL"))
                .byCss(".add-design-table tbody tr td:nth-child(" + column + ") input")
                .byXPath("//table[contains(@class,'add-design-table')]/tbody/tr/td[" + column + "]//input")
                .custom("Textbox in the edited link column", () -> page.locator(".add-design-table tbody td")
                        .nth(Integer.parseInt(column) - 1).getByRole(AriaRole.TEXTBOX))
                .resolve();
    }

    public Locator confirmLinkEdit() {
        return new ResilientLocator(page, "Confirm the inline Design link edit")
                .byCss(".add-design-table tbody .action-btn-wrapper .d-flex > button:first-child")
                .byXPath("//table[contains(@class,'add-design-table')]//button[.//i[normalize-space()='done']]")
                .custom("Confirmation action beside inline link fields", () -> page.locator(".add-design-table tbody tr:has(input)")
                        .getByRole(AriaRole.BUTTON).first())
                .resolve();
    }

    public Locator attachmentCells(String title) {
        return new ResilientLocator(page, "Edited Design attachment tables")
                .byCss(".edit-design-block .tab-content")
                .byXPath("//div[contains(@class,'edit-design-block')]//div[contains(@class,'tab-content')]")
                .custom("Tables in the edit form", () -> page.locator(".edit-design-block")
                        .locator("div.tab-content"))
                .resolve().getByRole(AriaRole.CELL, new Locator.GetByRoleOptions().setName(title).setExact(true));
    }

    public Locator removeFile(String title) {
        return new ResilientLocator(page, "Remove the existing Design file")
                .custom("Remove icon in the named file row", () -> attachmentCells(title).locator("..")
                        .locator("td.col-2 > svg:last-child"))
                .byXPath("//div[contains(@class,'edit-design-block')]//tr[td[normalize-space()='" + title
                        + "']]/td/*[local-name()='svg'][last()]")
                .custom("File removal icon after its download icon", () -> attachmentCells(title).locator("..")
                        .locator("svg").last())
                .resolve();
    }

    public Locator fileTitle() {
        return new ResilientLocator(page, "Replacement Design file title")
                .byCss(".edit-design-block .tab-pane.active > .mb-3 input")
                .byXPath("//input[@id='contained-button-file']/ancestor::div[contains(@class,'tab-pane')]//input[@placeholder='Title']")
                .custom("Title textbox in the active file pane", () -> page.locator(".edit-design-block .tab-pane.active")
                        .getByRole(AriaRole.TEXTBOX).first())
                .resolve();
    }

    public Locator addFile() {
        return new ResilientLocator(page, "Add the replacement Design file")
                .byCss(".edit-design-block .tab-pane.active > .flex-row-reverse > button")
                .byXPath("//input[@id='contained-button-file']/ancestor::div[contains(@class,'tab-pane')]//div[contains(@class,'flex-row-reverse')]/button")
                .custom("File pane add action", () -> page.locator(".edit-design-block .tab-pane.active > .flex-row-reverse")
                        .getByRole(AriaRole.BUTTON))
                .resolve();
    }

    public Locator save(String label) {
        return new ResilientLocator(page, "Save the edited Design")
                .byCss(".edit-design-block .text-right > button.btn-primary")
                .byXPath("//div[contains(@class,'edit-design-block')]//div[contains(@class,'text-right')]/button[contains(@class,'btn-primary')]")
                .byRole(AriaRole.BUTTON, label)
                .resolve();
    }

    public Locator savedValue(String value) {
        return new ResilientLocator(page, "Saved Design detail content")
                .byCss("#menu .sheet-body")
                .byXPath("//*[@id='menu']//*[contains(@class,'sheet-body')]")
                .custom("Detail body in the existing slide", () -> page.locator("#menu").locator(".sheet-body"))
                .resolve().getByText(value, new Locator.GetByTextOptions().setExact(true));
    }
}
