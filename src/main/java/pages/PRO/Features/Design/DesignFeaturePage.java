package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import pages.PRO.Features.Feature.ResilientFeaturePage;
import selfhealingHandler.ResilientLocator;

/** Reuses feature actions while allowing the Design flow to wait for asynchronously loaded features. */
public class DesignFeaturePage extends ResilientFeaturePage {
    private final Page page;

    public DesignFeaturePage(Page page) {
        super(page);
        this.page = page;
    }

    public Locator workflowDetails(String label) {
        return new ResilientLocator(page, "Open feature workflows")
                .byCss("#menu .sheet-body .pt-2 > button.btn-primary")
                .byCss("#menu button[text='" + label + "']")
                .byRole(AriaRole.BUTTON, label)
                .byXPath("//*[@id='menu']//button[normalize-space()='" + label + "']")
                .resolve();
    }

    @Override
    public Locator selectFeature(String name) {
        return new ResilientLocator(page, "Persisted Design feature: " + name)
                .withFirstStrategyTimeout(30000)
                .custom("Exact persisted feature name", () -> page.getByText(name,
                        new Page.GetByTextOptions().setExact(true)).last())
                .byRole(AriaRole.HEADING, name)
                .byXPath("//h2[normalize-space()='" + name + "']")
                .resolve();
    }

    @Override
    public Locator title() {
        return new ResilientLocator(page, "Feature name input")
                .byCss("input[name='name']")
                .byXPath("//input[@name='name']")
                .custom("Named feature textbox", () -> page.getByRole(AriaRole.TEXTBOX)
                        .and(page.locator("input[name='name']")))
                .resolve();
    }

    @Override
    public Locator description() {
        return new ResilientLocator(page, "Feature description input")
                .byCss("textarea[name='description']")
                .byXPath("//textarea[@name='description']")
                .custom("Named feature description textbox", () -> page.getByRole(AriaRole.TEXTBOX)
                        .and(page.locator("textarea[name='description']")))
                .resolve();
    }
}
