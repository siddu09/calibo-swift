package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/** Additional saved metadata checks for multiple designs; shared design actions remain unchanged. */
public class MultipleDesignPage extends ResilientDesignPage {
    private final Page page;

    public MultipleDesignPage(Page page) {
        super(page);
        this.page = page;
    }

    public Locator savedCategoryCard(String category, String name) {
        return new ResilientLocator(page, "Saved card in its category content")
                .custom("Card in the existing category header's container", () -> categorySection(category)
                        .locator("..").locator("h3.design-title[title='" + name + "']"))
                .byXPath("//div[contains(concat(' ',normalize-space(@class),' '),' design-card-body ')]"
                        + "[.//h4[normalize-space()='" + category + "']]//h3[@title='" + name + "']")
                .custom("Existing design card within its category container", () -> designCard(name)
                        .and(page.locator(".design-card-body")
                                .filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.HEADING,
                                        new Page.GetByRoleOptions().setName(category).setExact(true))))
                                .locator("h3.design-title")))
                .resolve();
    }

    public Locator savedFeatureCards() {
        return new ResilientLocator(page, "Saved feature design content")
                .byCss("#app-layout-container")
                .byXPath("//*[@id='app-layout-container']")
                .custom("Application content by stable id", () -> page.locator("[id='app-layout-container']"))
                .resolve().locator(".design-card-body h3.design-title");
    }

    public Locator sourcePickerImage(String source) {
        return new ResilientLocator(page, "Configured source image: " + source)
                .custom("Image in existing source card", () -> selectDesignSource(source).locator("img"))
                .byXPath("//*[normalize-space(text())='" + source + "']/parent::*//div[contains(@class,'design-logo')]/img")
                .custom("Image beside exact source label", () -> page.locator("div:has(> .design-logo)")
                        .filter(new Locator.FilterOptions().setHas(page.getByText(source,
                                new Page.GetByTextOptions().setExact(true)))).locator(".design-logo > img"))
                .resolve();
    }

    public Locator savedSourceImage() {
        return new ResilientLocator(page, "Saved design source image")
                .byCss("#menu .sheet-body > .row:first-child .bg-white > img")
                .byXPath("//*[@id='menu']//*[contains(@class,'sheet-body')]/div[1]//div[contains(@class,'bg-white')]/img")
                .byCss("#menu .sheet-body > .row .col-md-10 > .bg-white > img")
                .custom("Source image in the saved slide header", () -> page.locator("#menu .sheet-body > .row")
                        .first().locator("img"))
                .resolve();
    }

    public Locator categorySection(String category) {
        return new ResilientLocator(page, "Saved category section: " + category)
                .custom("Category containing its exact heading", () -> page.locator(".design-category")
                        .filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.HEADING,
                                new Page.GetByRoleOptions().setName(category).setExact(true)))))
                .byXPath("//div[contains(@class,'design-category')][.//h4[normalize-space()='" + category + "']]")
                .custom("Category containing its label", () -> page.locator(".design-category")
                        .filter(new Locator.FilterOptions().setHas(page.getByText(category,
                                new Page.GetByTextOptions().setExact(true)))))
                .resolve();
    }

    public Locator categoryDesignCard(String category, String name) {
        return new ResilientLocator(page, "Saved design in its configured category")
                .custom("Named card inside category", () -> categorySection(category)
                        .locator("h3.design-title[title='" + name + "']"))
                .byXPath("//div[contains(@class,'design-category')][.//h4[normalize-space()='" + category
                        + "']]//h3[@title='" + name + "']")
                .custom("Titled card inside category", () -> categorySection(category)
                        .locator("[class~='design-title'][title='" + name + "']"))
                .resolve();
    }

    public Locator allDesignCards() {
        return new ResilientLocator(page, "Feature design content")
                .byCss("#app-layout-container")
                .byXPath("//*[@id='app-layout-container']")
                .custom("Application content by id", () -> page.locator("[id='app-layout-container']"))
                .resolve().locator(".design-category h3.design-title");
    }
}
