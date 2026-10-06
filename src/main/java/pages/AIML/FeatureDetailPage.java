package pages.PRO.Features.Feature;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class FeatureDetailsPage {
    private final Page page;

    public FeatureDetailsPage(Page page) {
        this.page = page;
    }

    public Locator fetchFeatureName() {

        return new ResilientLocator(
                page,
                "Feature Name")
                .custom(
                        "Feature title",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Feature")
                                )
                                .locator(
                                        "xpath=following-sibling::div//h2"
                                )
                )
                .resolve();
    }

    public Locator fetchFeatureDescription() {

        return new ResilientLocator(
                page,
                "Feature Description")
                .custom(
                        "Feature description",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Description")
                                )
                                .locator(
                                        "xpath=following-sibling::div//pre"
                                )
                )
                .resolve();
    }

    public Locator fetchFeatureStatus() {

        return new ResilientLocator(
                page,
                "Feature Status")
                .custom(
                        "Feature status",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Feature Status")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator featureName() {
        return new ResilientLocator(page, "Feature detail name")
                .byXPath("//label[normalize-space()='Feature']/following-sibling::div//h2")
                .byCss("label:has-text('Feature') + div h2")
                .resolve();
    }

    public Locator featureDescription() {
        return new ResilientLocator(page, "Feature detail description")
                .byXPath("//label[normalize-space()='Description']/following-sibling::div//pre")
                .byCss("label:has-text('Description') + div pre")
                .resolve();
    }

    public Locator featureStatus() {
        return new ResilientLocator(page, "Feature detail status")
                .byXPath("//label[normalize-space()='Feature Status']/following-sibling::p[1]")
                .byCss("label:has-text('Feature Status') + p")
                .resolve();
    }

    public Locator featureActions(String name) {
        return new ResilientLocator(page, "Actions for feature: " + name)
                .byXPath("//h2[normalize-space()='" + name + "']/ancestor::div[@id='menu']//button[.//*[normalize-space()='more_horiz' or normalize-space()='more_vert']]")
                .custom("Actions within the named feature details", () -> page.locator("#menu")
                        .filter(new Locator.FilterOptions().setHas(page.getByText(name,
                                new Page.GetByTextOptions().setExact(true))))
                        .locator("button:has-text('more_horiz'), button:has-text('more_vert')"))
                .resolve();
    }

    public Locator deleteFeature(String label) {
        return new ResilientLocator(page, "Delete feature menu action")
                .byRole(AriaRole.MENUITEM, label)
                .byXPath("//*[self::button or self::li][normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator deletionReason() {
        return new ResilientLocator(page, "Feature deletion reason")
                .byCss("textarea[placeholder='Enter your comments...']")
                .byXPath("//textarea")
                .resolve();
    }

    public Locator confirmDelete(String label) {
        return new ResilientLocator(page, "Confirm feature deletion")
                .byXPath("//textarea/ancestor::div[.//button[normalize-space()='" + label + "']][1]//button[normalize-space()='" + label + "' and not(@disabled)]")
                .byRole(AriaRole.BUTTON, label)
                .resolve();
    }

}
