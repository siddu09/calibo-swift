package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;


public class ProductPortfolioAdditionalDetailsFinancialsTabPage {

    private final Page page;

    public ProductPortfolioAdditionalDetailsFinancialsTabPage(Page page) {
        this.page = page;
    }

    public Locator addCurrentYear() {
        return new ResilientLocator(page, "Add More Custom Fields Drawer Button")
                .byRole(AriaRole.BUTTON, "Start with current year")
                .byCss("button[text='Start with current year']")
                .byXPath("//button[text()='Start with current year']")
                .resolve();
    }

    public Locator setApprovedBudget() {
        return new ResilientLocator(page, "Add Approved Budget")
                .byLabelInput("Approved Budget")
                .byXPath(
                        "//label[normalize-space()='Approved Budget']" +
                                "/ancestor::div[contains(@class,'col-md-3')][1]" +
                                "//input[@type='number']"
                )
                .resolve();
    }

    public Locator setRevenueTarget() {
        {
            return new ResilientLocator(page, "Add Revenue Target")
                    .byLabelInput("Revenue Target")
                    .byXPath(
                            "//label[normalize-space()='Revenue Target']" +
                                    "/ancestor::div[contains(@class,'col-md-3')][1]" +
                                    "//input[@type='number']"
                    )
                    .resolve();
        }
    }

    public Locator addYear() {
        return new ResilientLocator(page, "Add financial year")
                .byXPath("//i[normalize-space()='add']")
                .byCss("div:nth-child(1) div:nth-child(1) div:nth-child(2) div:nth-child(5) div:nth-child(1) div:nth-child(1) div:nth-child(1) div:nth-child(1) button:nth-child(2) i:nth-child(1)")
                .resolve();
    }

    public Locator financialYears() {
        return page.locator("#wrapped-tabpanel-FINANCIALS").getByRole(AriaRole.TAB);
    }

    public Locator yearDropdown(String year) {
        return page.locator("#wrapped-tabpanel-FINANCIALS")
                .getByRole(AriaRole.TAB, new Locator.GetByRoleOptions().setName(
                        java.util.regex.Pattern.compile("^" + year + "\\b")))
                .getByText("expand_more", new Locator.GetByTextOptions().setExact(true));
    }

    public Locator deleteYear() {
        return page.getByText("Delete", new Page.GetByTextOptions().setExact(true));
    }

    public Locator deleteConfirmation() {
        return page.getByRole(AriaRole.DIALOG);
    }

    public Locator confirmDeleteYear() {
        return new ResilientLocator(page, "Confirm delete financial year")
                .byXPath("//button[normalize-space()='Yes']")
                .byCss(".sc-bBHwJV.hDsuPB.btn-lg.ml-3.btn.btn-primary")
                .resolve();
    }

    public Locator addYearsPopup() {
        return page.getByRole(AriaRole.TOOLTIP).filter(new Locator.FilterOptions()
                .setHasText("Add Year(s)"));
    }

    public Locator allYearsDropdown() {
        return addYearsPopup().getByText("All Year(s)", new Locator.GetByTextOptions().setExact(true));
    }

    public Locator yearCheckbox(String year) {
        return addYearsPopup().locator(".react-select__option")
                .filter(new Locator.FilterOptions().setHasText(year)).getByRole(AriaRole.CHECKBOX);
    }

    public Locator confirmAddYears() {
        return addYearsPopup().getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Add").setExact(true));
    }

    public Locator yearTab(String year) {
        return new ResilientLocator(page, "Financial year tab: " + year)
                .custom("Year tab with exact year label", () -> financialYears()
                        .filter(new Locator.FilterOptions().setHas(
                                page.getByText(year, new Page.GetByTextOptions().setExact(true)))))
                .byXPath("//div[@id='wrapped-tabpanel-FINANCIALS']//button[@role='tab']"
                        + "[.//p[normalize-space()='" + year + "']]")
                .resolve();
    }
}
