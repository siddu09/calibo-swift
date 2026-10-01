package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductTeamsPage {
    private final Page page;

    public ProductTeamsPage(Page page) {
        this.page = page;
    }

    public Locator memberSearch() {
        return new ResilientLocator(page, "Member or team search")
                .custom("Member or team form ready", () -> {
                    Locator search = page.locator("//input[@name='memberAndTeams']/parent::div//input[@type='text']");
                    search.waitFor();
                    return search;
                })
                .byCss("[class*='react-select__control'] input[type='text']")
                .resolve();
    }

    public Locator dropdownOption(String name) {
        return new ResilientLocator(page, "Allocation option: " + name)
                .custom("Exact dropdown option", () -> {
                    Locator option = page.locator("[class*='react-select__menu-list']")
                            .getByText(name, new Locator.GetByTextOptions().setExact(true));
                    option.waitFor();
                    return option;
                })
                .byXPath("//div[contains(@class,'react-select__option')][normalize-space()='" + name + "']")
                .resolve();
    }

    public Locator memberCategory(String category) {
        return new ResilientLocator(page, "Allocation category: " + category)
                .custom("Dropdown category", () -> {
                    Locator heading = page.locator("[class*='react-select__menu-list']")
                            .getByText(category, new Locator.GetByTextOptions().setExact(true));
                    heading.waitFor();
                    return heading;
                })
                .byXPath("//div[contains(@class,'react-select__group-heading')][normalize-space()='" + category + "']")
                .resolve();
    }

    public Locator allocationRow(String name) {
        return new ResilientLocator(page, "Saved allocation: " + name)
                .custom("Allocation row with exact member name", () -> {
                    Locator row = page.getByRole(AriaRole.ROW).filter(new Locator.FilterOptions()
                            .setHas(page.getByText(name, new Page.GetByTextOptions().setExact(true))));
                    row.first().waitFor();
                    return row;
                })
                .byXPath("//tr[.//*[normalize-space()='" + name + "']]")
                .resolve();
    }

    public Locator allocationCell(String name, String expectedValue) {
        return new ResilientLocator(page, "Saved allocation value: " + expectedValue)
                .custom("Allocation row cell", () -> allocationRow(name)
                        .getByRole(AriaRole.CELL).filter(new Locator.FilterOptions().setHasText(expectedValue)))
                .byXPath("//tr[.//*[normalize-space()='" + name + "']]//td[normalize-space()='" + expectedValue + "']")
                .resolve();
    }

    public Locator roleDropdown(String label) {
        return new ResilientLocator(page, "Allocation role dropdown")
                .byXPath("//label[normalize-space()='" + label + "']/parent::div//div[contains(@class,'react-select__control')]")
                .custom("Role dropdown control", () -> page.locator("div.col-md-12")
                        .filter(new Locator.FilterOptions().setHas(page.locator("label")
                                .filter(new Locator.FilterOptions().setHasText(label))))
                        .locator("div.react-select__control"))
                .resolve();
    }

    public Locator roleSearch(String label) {
        return new ResilientLocator(page, "Allocation role search")
                .byXPath("//label[normalize-space()='" + label + "']/parent::div//input[@type='text']")
                .custom("Role dropdown input", () -> page.locator("div.col-md-12")
                        .filter(new Locator.FilterOptions().setHas(page.locator("label")
                                .filter(new Locator.FilterOptions().setHasText(label))))
                        .locator("input[aria-autocomplete='list']"))
                .resolve();
    }

    public Locator allocationPeriod() {
        return new ResilientLocator(page, "Allocation period")
                .byCss("input.lazsa-date-picker-input.range-picker")
                .byXPath("//input[contains(@class,'range-picker')]")
                .resolve();
    }

    public Locator allocationPercentage() {
        return new ResilientLocator(page, "Allocation percentage")
                .byCss("input[name='allocation']")
                .custom("Allocation number input", () -> page.getByRole(AriaRole.SPINBUTTON))
                .resolve();
    }

    public Locator comments() {
        return new ResilientLocator(page, "Allocation comments")
                .byCss("textarea[name='comment']")
                .byXPath("//textarea[@rows='4']")
                .resolve();
    }

    public Locator addAllocation(String label) {
        return new ResilientLocator(page, "Save allocation")
                .custom("Exact Add button", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//button[normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator tab(String label) {
        return new ResilientLocator(page, "Product Teams tab")
                .byRole(AriaRole.TAB, label)
                .byXPath("//a[@role='tab'][.//*[normalize-space()='" + label + "']]")
                .resolve();
    }

    public Locator addMemberTeam(String label) {
        return new ResilientLocator(page, "Add member or team")
                .custom("Add allocation button", () -> {
                    Locator button = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(label));
                    button.waitFor();
                    return button;
                })
                .byXPath("//button[contains(normalize-space(),'" + label + "')]")
                .resolve();
    }
    public Locator mandatoryFieldError(String message) {
        return new ResilientLocator(page, "Mandatory field warning: " + message)
                .custom("Exact validation message", () -> page.getByText(message,
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//span[normalize-space()='" + message + "']")
                .resolve();
    }

    public Locator closeMemberTeamPopup() {
        return new ResilientLocator(page, "Close Member/Team popup")
                .byCss("#menu:has(input[name='memberAndTeams']) .side-menu-header .sidebar-actions button")
                .byXPath("//input[@name='memberAndTeams']/ancestor::div[@id='menu']//div[contains(@class,'side-menu-header')]//button[.//*[local-name()='svg']]")
                .resolve();
    }

}
