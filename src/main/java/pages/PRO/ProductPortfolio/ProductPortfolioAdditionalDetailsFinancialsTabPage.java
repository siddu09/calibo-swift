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
}
