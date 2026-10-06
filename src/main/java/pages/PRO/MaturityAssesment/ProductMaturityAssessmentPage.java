package pages.PRO.MaturityAssesment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

public class ProductMaturityAssessmentPage {
    private final Page page;

    public ProductMaturityAssessmentPage(Page page) {
        this.page = page;
    }

    public Locator newAssessment(String label) {
        return new ResilientLocator(page, "New product assessment")
                .byRole(AriaRole.BUTTON, label)
                .byXPath("//button[contains(normalize-space(),'" + label + "')]")
                .byText(label)
                .resolve();
    }

    public Locator nameEditor(String label, String placeholder) {
        return new ResilientLocator(page, "Assessment name editor")
                .byText(placeholder)
                .byXPath("//label[normalize-space()='" + label + "']/..//*[contains(@class,'cursor-pointer')]")
                .resolve();
    }

    public Locator name() {
        return new ResilientLocator(page, "Assessment name")
                .byCss("input.assessment-name-input, .assessment-name-input input")
                .byXPath("//*[contains(@class,'assessment-name-input')]//input")
                .resolve();
    }

    public Locator saveName() {
        return new ResilientLocator(page, "Save assessment name")
                .byCss(".assessment-name-input button:has(i:text-is('check'))")
                .byXPath("//*[contains(@class,'assessment-name-input')]//button[.//*[normalize-space()='check']]")
                .resolve();
    }

    public Locator nameValue(String name) {
        return new ResilientLocator(page, "Committed assessment name")
                .custom("Exact name", () -> page.getByText(name, new Page.GetByTextOptions().setExact(true)))
                .byRole(AriaRole.HEADING, name)
                .resolve();
    }

    private Locator typeContainer(String label) {
        return new ResilientLocator(page, "Assessment type container")
                .byXPath("//label[normalize-space()='" + label + "']/parent::div")
                .custom("Type field", () -> page.getByText(label, new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=.."))
                .resolve();
    }

    public void selectType(String label, String value) {
        Locator container = typeContainer(label);
        new ResilientLocator(page, "Edit assessment type")
                .custom("Type edit icon", () -> container.locator(".edit-icon"))
                .custom("Type dropdown", () -> container.locator(".search-wrapper, [class*='react-select__control']"))
                .resolve().click();
        new ResilientLocator(page, "Open assessment type dropdown")
                .custom("Type dropdown wrapper", () -> container.locator(".search-wrapper"))
                .custom("Type combobox", () -> container.getByRole(AriaRole.COMBOBOX))
                .resolve().click();
        new ResilientLocator(page, "Assessment type option")
                .byXPath("//*[contains(@class,'optionListContainer')]//li[normalize-space()='" + value + "']")
                .custom("Type dropdown option", () -> page.locator(".optionListContainer")
                        .getByText(value, new Locator.GetByTextOptions().setExact(true)))
                .byRole(AriaRole.OPTION, value)
                .custom("React select type option", () -> container.locator("[class*='react-select__menu']")
                        .getByText(value, new Locator.GetByTextOptions().setExact(true)))
                .resolve().click();
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(container).containsText(value);
    }

    public void selectRelease(String label, String value) {
        Locator container = typeContainer(label);
        new ResilientLocator(page, "Edit assessment release")
                .custom("Release edit icon", () -> container.locator(".edit-icon"))
                .custom("Release dropdown", () -> container.locator("[class*='react-select__control']"))
                .resolve().click();
        pages.PRO.ReleaseTrain.NewReleasePage dropdown = new pages.PRO.ReleaseTrain.NewReleasePage(page);
        dropdown.dropdown(label).click();
        dropdown.dropdownSearch(label).fill(value);
        new ResilientLocator(page, "Assessment release option")
                .custom("Release name with product release label", () -> page.locator("[class*='react-select__menu-list']")
                        .getByText(value, new Locator.GetByTextOptions().setExact(false)))
                .custom("Unique filtered release", () -> {
                    Locator options = page.locator("[class*='react-select__menu-list'] [class*='react-select__option']");
                    com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(options).hasCount(1);
                    return options;
                })
                .resolve().click();
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(container).containsText(value);
    }

    private Locator subCategoryRow(String subCategory) {
        return new ResilientLocator(page, "Assessment subcategory: " + subCategory)
                .byXPath("//*[self::h4 or self::h5][normalize-space()='" + subCategory
                        + "']/ancestor::div[contains(concat(' ',normalize-space(@class),' '),' row ')][1]")
                .custom("Subcategory row", () -> page.getByText(subCategory,
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=ancestor::div[contains(@class,'row')][1]"))
                .resolve();
    }

    public Locator respondentControl(String subCategory) {
        return new ResilientLocator(page, "Assessment respondent control")
                .custom("Respondent in subcategory", () -> subCategoryRow(subCategory).locator("[class*='react-select__control']"))
                .custom("Respondent input container", () -> respondentSearch(subCategory)
                        .locator("xpath=ancestor::div[contains(@class,'control')][1]"))
                .resolve();
    }

    public Locator respondentSearch(String subCategory) {
        return new ResilientLocator(page, "Assessment respondent search")
                .custom("React select search", () -> subCategoryRow(subCategory).locator("input[aria-autocomplete='list']"))
                .custom("Respondent text input", () -> subCategoryRow(subCategory).locator("[class*='react-select__input'] input"))
                .resolve();
    }

    public Locator dueDate(String subCategory) {
        return new ResilientLocator(page, "Assessment response due date")
                .custom("Subcategory date picker", () -> subCategoryRow(subCategory).locator("input.lazsa-date-picker-input"))
                .custom("Subcategory date input", () -> subCategoryRow(subCategory).locator(".single-date-popper input"))
                .resolve();
    }

    public void selectDueDate(String subCategory, String value) {
        LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH));
        dueDate(subCategory).click();
        // The existing date-picker component selects the same month/year/day controls,
        // but its trigger is global; this assessment has one picker per subcategory.
        new ResilientLocator(page, "Due date month")
                .byCss(".month-dropdown")
                .byCss(".react-datepicker__month-dropdown-container")
                .resolve().click();
        new ResilientLocator(page, "Due date month option")
                .byText(date.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)))
                .byRole(AriaRole.OPTION, date.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)))
                .resolve().click();
        new ResilientLocator(page, "Due date year")
                .byCss(".year-dropdown")
                .byCss(".react-datepicker__year-dropdown-container")
                .resolve().click();
        new ResilientLocator(page, "Due date year option")
                .byText(Integer.toString(date.getYear()))
                .byRole(AriaRole.OPTION, Integer.toString(date.getYear()))
                .resolve().click();
        String dayClass = String.format(Locale.ROOT, "%03d", date.getDayOfMonth());
        String accessibleDay = date.format(DateTimeFormatter.ofPattern("MMMM d", Locale.ENGLISH));
        new ResilientLocator(page, "Due date day")
                .byCss(".react-datepicker__day--" + dayClass
                        + ":not(.react-datepicker__day--outside-month):not(.react-datepicker__day--disabled)")
                .custom("Accessible calendar day", () -> page.getByRole(AriaRole.OPTION,
                        new Page.GetByRoleOptions().setName(Pattern.compile(".*" + Pattern.quote(accessibleDay)
                                + ".*" + date.getYear() + ".*"))))
                .resolve().click();
    }

    public Locator initiate(String subCategory, String label) {
        return new ResilientLocator(page, "Initiate subcategory assessment")
                .custom("Subcategory initiate button", () -> subCategoryRow(subCategory).getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .custom("Subcategory action", () -> subCategoryRow(subCategory)
                        .locator("button").filter(new Locator.FilterOptions().setHasText(label)))
                .resolve();
    }
}
