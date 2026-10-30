package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/** Attribute-based alternatives for the new Design scenario; legacy page methods remain unchanged. */
public class ResilientDesignPage extends DesignPage {
    private final Page page;

    public ResilientDesignPage(Page page) {
        super(page);
        this.page = page;
    }

    @Override
    public Locator name() {
        return new ResilientLocator(page, "Design artifact name")
                .byCss("input[name='name']")
                .byXPath("//input[@name='name']")
                .custom("Named design textbox", () -> page.getByRole(AriaRole.TEXTBOX)
                        .and(page.locator("input[name='name']")))
                .resolve();
    }

    @Override
    public Locator description() {
        return new ResilientLocator(page, "Design artifact description")
                .byCss("textarea[name='description']")
                .byXPath("//textarea[@name='description']")
                .custom("Named design description textbox", () -> page.getByRole(AriaRole.TEXTBOX)
                        .and(page.locator("textarea[name='description']")))
                .resolve();
    }

    @Override
    public Locator create() {
        return new ResilientLocator(page, "Create design artifact")
                .byCss("#app-layout-container .text-right.mt-5 > button.btn-primary")
                .byXPath("//*[@id='app-layout-container']//div[contains(@class,'text-right')]/button[contains(concat(' ',normalize-space(@class),' '),' btn-primary ')]")
                .custom("Primary artifact form action", () -> page.locator("#app-layout-container .text-right")
                        .getByRole(AriaRole.BUTTON).and(page.locator(".btn-primary")))
                .resolve();
    }

    public Locator proceed(String label) {
        return new ResilientLocator(page, "Proceed with design category")
                .byCss("#app-layout-container button.btn-lg.btn-primary")
                .byXPath("//*[@id='app-layout-container']//button[contains(concat(' ',normalize-space(@class),' '),' btn-lg ') and contains(concat(' ',normalize-space(@class),' '),' btn-primary ')]")
                .custom("Primary category action", () -> page.locator("#app-layout-container .btn-lg")
                        .and(page.getByRole(AriaRole.BUTTON)))
                .resolve();
    }

    public Locator categoryControl() {
        return new ResilientLocator(page, "Design category control")
                .byCss("#app-layout-container .react-select__control")
                .byXPath("//*[@id='app-layout-container']//input[@aria-autocomplete='list']/ancestor::div[contains(@class,'react-select__control')][1]")
                .byCss("#app-layout-container input[id^='react-select-'][aria-autocomplete='list']")
                .resolve();
    }

    @Override
    public Locator selectDropdownValue(String category) {
        return new ResilientLocator(page, "Configured design category")
                .custom("Category in select options", () -> page.locator(".react-select__option")
                        .filter(new Locator.FilterOptions().setHas(page.getByText(category,
                                new Page.GetByTextOptions().setExact(true)))))
                .custom("Category label in open menu", () -> page.locator(".react-select__menu-list")
                        .getByText(category, new Locator.GetByTextOptions().setExact(true)))
                .byXPath("//div[contains(@class,'react-select__menu-list')]//label[normalize-space()='" + category + "']")
                .resolve();
    }

    @Override
    public void chooseCategory(String category) {
        categoryControl().click();
        selectDropdownValue(category).click();
    }

    @Override
    public Locator selectDesignSource(String source) {
        return new ResilientLocator(page, "Configured design source")
                .custom("Source card logo", () -> page.getByText(source,
                        new Page.GetByTextOptions().setExact(true)).locator("..").locator("div.design-logo"))
                .byXPath("//*[normalize-space(text())='" + source + "']/parent::*//div[contains(@class,'design-logo')]")
                .custom("Logo beside the configured source", () -> page.locator("div:has(> .design-logo)")
                        .filter(new Locator.FilterOptions().setHas(page.getByText(source,
                                new Page.GetByTextOptions().setExact(true))))
                        .locator(".design-logo"))
                .resolve();
    }

    @Override
    public Locator expandCategory(String category) {
        return new ResilientLocator(page, "Expand saved design category")
                .custom("Configured category expand control", () -> page.locator("div.design-category")
                        .filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.HEADING,
                                new Page.GetByRoleOptions().setName(category).setExact(true))))
                        .locator("button.btn-develop-expand"))
                .byXPath("//div[contains(@class,'design-category')][.//h4[normalize-space()='" + category + "']]//button[contains(@class,'btn-develop-expand')]")
                .custom("Expand control below configured category", () -> page.locator("div.design-category")
                        .filter(new Locator.FilterOptions().setHas(page.getByText(category,
                                new Page.GetByTextOptions().setExact(true))))
                        .getByRole(AriaRole.BUTTON).and(page.locator(".btn-develop-expand")))
                .resolve();
    }

    public Locator returnToProduct(String productId, String label) {
        return new ResilientLocator(page, "Return to feature's product")
                .byCss("a[href='/projects/project-view/" + productId + "']")
                .byXPath("//a[@href='/projects/project-view/" + productId + "']")
                .byRole(AriaRole.BUTTON, label)
                .resolve();
    }

    @Override
    public Locator designCard(String name) {
        return new ResilientLocator(page, "Created design artifact")
                .withFirstStrategyTimeout(30000)
                .byCss("h3.design-title[title='" + name + "']")
                .byXPath("//h3[@title='" + name + "']")
                .byCss("[class~='design-title'][title='" + name + "']")
                .resolve();
    }

    @Override
    public Locator designDetailsTitle() {
        return new ResilientLocator(page, "Design details title")
                .byCss("#menu .sheet-body h2")
                .byXPath("//*[@id='menu']//*[contains(@class,'sheet-body')]//h2")
                .custom("Design sheet heading", () -> page.locator("#menu .sheet-body")
                        .getByRole(AriaRole.HEADING, new Locator.GetByRoleOptions().setLevel(2)))
                .resolve();
    }

    @Override
    public Locator designDetailsDescription() {
        return new ResilientLocator(page, "Design details description")
                .byCss("#menu .tab-pane.active label + h5")
                .byXPath("//*[@id='menu']//*[contains(@class,'tab-pane') and contains(@class,'active')]//label/following-sibling::h5[1]")
                .byCss("#menu .sheet-body label + h5")
                .resolve();
    }

    public Locator businessRequirementControl() {
        return new ResilientLocator(page, "Design Business Requirement dropdown")
                .byCss(".custom-form-bg .react-select__control")
                .byXPath("//textarea[@name='description']/ancestor::div[contains(@class,'custom-form-bg')][1]//*[contains(@class,'react-select__control')]")
                .custom("Requirement dropdown surrounding searchable input", () -> businessRequirementSearch()
                        .locator("xpath=ancestor::div[contains(@class,'react-select__control')][1]"))
                .resolve();
    }

    public Locator businessRequirementSearch() {
        return new ResilientLocator(page, "Design Business Requirement search")
                .byCss(".custom-form-bg input[aria-autocomplete='list']")
                .byXPath("//div[contains(@class,'custom-form-bg')]//input[@aria-autocomplete='list']")
                .byCss(".custom-form-bg input[id^='react-select-'][type='text']")
                .resolve();
    }

    public Locator businessCaseValue(String name) {
        return new ResilientLocator(page, "Linked Design business case")
                .withFirstStrategyTimeout(30000)
                .custom("Linked requirement in the design slide", () -> page.locator("#menu .sheet-body")
                        .getByText(name, new Locator.GetByTextOptions().setExact(true)))
                .byCss("#menu [title='" + name + "']")
                .byXPath("//*[@id='menu']//*[normalize-space(text())='" + name + "']")
                .resolve();
    }

    public Locator businessCaseLabel(String label) {
        return new ResilientLocator(page, "Design business case label")
                .custom("Business case field label", () -> page.locator("#menu label")
                        .getByText(label, new Locator.GetByTextOptions().setExact(true)))
                .byXPath("//*[@id='menu']//label[normalize-space()='" + label + "']")
                .custom("Business case label in design details", () -> page.locator("#menu .tab-pane.active")
                        .getByText(label, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }
}
