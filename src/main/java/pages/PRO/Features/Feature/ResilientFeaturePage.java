package pages.PRO.Features.Feature;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ResilientFeaturePage extends FeaturePage {
    private final Page page;

    public ResilientFeaturePage(Page page) {
        super(page);
        this.page = page;
    }

    @Override
    public Locator addNewFeature() {
        return new ResilientLocator(page, "New Feature")
                .byRole(AriaRole.BUTTON, "New Feature")
                .byXPath("//button[contains(normalize-space(),'New Feature')]")
                .resolve();
    }

    private Locator phaseHeading(String phase) {
        return new ResilientLocator(page, "Feature phase: " + phase)
                .byXPath("//h3[normalize-space()='" + phase + "']")
                .byRole(AriaRole.HEADING, phase)
                .resolve();
    }

    @Override
    public Locator checkCircleDefine() {
        return phaseHeading("Define");
    }

    @Override
    public Locator checkCircleDesign() {
        return phaseHeading("Design");
    }

    @Override
    public Locator checkCircleDevelop() {
        return phaseHeading("Develop");
    }

    @Override
    public Locator dropDownArrow() {
        return new ResilientLocator(page, "Feature status dropdown")
                .byXPath("//label[normalize-space()='Feature Status']/parent::div//*[contains(@class,'react-select__control')]")
                .byXPath("//i[normalize-space()='arrow_drop_down']")
                .resolve();
    }

    @Override
    public Locator selectFeatureStatusOptions(String status) {
        return new ResilientLocator(page, "Feature status: " + status)
                .byXPath("//div[contains(@class,'react-select__menu')]//label[normalize-space()='" + status + "']")
                .custom("Exact status option", () -> page.locator("[class*='react-select__menu-list']")
                        .getByText(status, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }

    @Override
    public Locator selectFeature(String name) {
        return new ResilientLocator(page, "Created feature: " + name)
                .custom("Exact feature name", () -> page.getByText(name,
                        new Page.GetByTextOptions().setExact(true)).last())
                .byXPath("//h2[normalize-space()='" + name + "']")
                .resolve();
    }
}
