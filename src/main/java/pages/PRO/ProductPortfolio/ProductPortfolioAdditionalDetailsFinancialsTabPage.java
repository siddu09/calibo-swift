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
                .byRole(AriaRole.BUTTON, "Add Year")
                .byXPath("//button[contains(normalize-space(.),'Add Year')]")
                .resolve();
    }

    public Locator financialYears() {
        return new ResilientLocator(page, "Portfolio financial years")
                .byXPath("//*[contains(@id,'FINANCIAL')]//input[@type='number']/ancestor::*[.//label[contains(.,'Approved Budget')]][1]")
                .byCss("#wrapped-tabpanel-FINANCIALS input[type='number']")
                .resolve();
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
}
