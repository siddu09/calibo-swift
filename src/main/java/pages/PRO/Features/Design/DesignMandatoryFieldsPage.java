package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

/** Validation controls for the existing Design creation form. */
public class DesignMandatoryFieldsPage extends ResilientDesignPage {
    private final Page page;

    public DesignMandatoryFieldsPage(Page page) {
        super(page);
        this.page = page;
    }

    public Locator form() {
        return new ResilientLocator(page, "Design mandatory field form")
                .byCss("#app-layout-container .custom-form-bg")
                .byXPath("//input[@name='name']/ancestor::div[contains(@class,'custom-form-bg')][1]")
                .custom("Form surrounding the existing Design name field", () -> name()
                        .locator("xpath=ancestor::div[contains(@class,'custom-form-bg')][1]"))
                .resolve();
    }

    public Locator mandatoryErrors() {
        return form().locator(".text-danger:visible")
                .filter(new Locator.FilterOptions().setHasText(java.util.regex.Pattern.compile("\\S")));
    }

    public Locator mandatoryError(String message) {
        return form().getByText(message, new Locator.GetByTextOptions().setExact(true));
    }
}
