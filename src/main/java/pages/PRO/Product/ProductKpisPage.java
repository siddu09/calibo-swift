package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductKpisPage {
    private final Page page;

    public ProductKpisPage(Page page) {
        this.page = page;
    }

    public Locator button(String label) {
        return new ResilientLocator(page, "Product KPI action: " + label)
                .custom("Exact KPI action", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byRole(AriaRole.BUTTON, label)
                .byXPath("//button[normalize-space()='" + label + "']")
                .resolve();
    }
    public Locator mandatoryWarnings(String createLabel) {
        Locator form = new ResilientLocator(page, "New KPI form")
                .byXPath("//button[normalize-space()='" + createLabel + "']/ancestor::form[1]")
                .byCss("#menu:has(form)")
                .resolve();
        return form.locator("span.text-danger, .invalid-feedback");
    }

    public Locator mandatoryFieldError(String message) {
        return new ResilientLocator(page, "Mandatory KPI warning: " + message)
                .custom("Exact KPI validation message", () -> page.getByText(message,
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//span[normalize-space()='" + message + "']")
                .resolve();
    }

}
