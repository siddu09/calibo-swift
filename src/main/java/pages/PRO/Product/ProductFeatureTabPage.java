package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductFeatureTabPage {
    private final Page page;

    public ProductFeatureTabPage(Page page) {
        this.page = page;
    }

    public Locator muiIconThreeDots() {
        return new ResilientLocator(page, "Mui Icon Three Dots")
                .byXPath("//span[@class='material-icons MuiIcon-root']")
                .byText("more_horiz")
                .resolve();
    }

    public void editTheFeatureUsingThreeDots(String featureName) {
        muiIconThreeDots().click();
        page.locator("//span[@class='material-icons MuiIcon-root']/parent::button/parent::div/div/div/button[text()='"+featureName+"']").click();
    }


    public Locator clickOnFeatureName(String featureName) {
        return new ResilientLocator(page, "Feature Name")
                .byXPath("//h2[text()='" + featureName + "']")
                .resolve();
    }

    public Locator clickStages(String stages) {
        return new ResilientLocator(page, "Stages " + stages)
                .byXPath("//h4[normalize-space(.)='" + stages + "']")
                .byXPath("//h3[normalize-space(.)='" + stages + "']")
                .byRole(AriaRole.TAB, stages)
                .byText(stages)
                .resolve();
    }
    public Locator matchingFeature(String name) {
        return page.getByText(name, new Page.GetByTextOptions().setExact(true))
                .or(page.getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setName(name).setExact(true)));
    }

    public Locator featureCardHeading(String featureName) {
        return new ResilientLocator(page, "Feature card heading")
                .custom("Exact feature heading", () -> page.getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setName(featureName).setExact(true)))
                .custom("Exact title in the feature card", () -> matchingFeature(featureName)
                        .filter(new Locator.FilterOptions().setVisible(true)))
                .resolve();
    }

    public Locator featureReleaseIcon(String featureName) {
        Locator heading = featureCardHeading(featureName);
        return new ResilientLocator(page, "Release icon for " + featureName)
                .custom("Release link on the matching feature card", () -> heading.locator(
                        "xpath=ancestor::*[.//a[contains(@href,'/releases/')]][1]//a[contains(@href,'/releases/')]"))
                .custom("Labeled release icon on the matching feature card", () -> heading.locator(
                        "xpath=ancestor::*[.//*[@aria-label='Release' or @title='Release']][1]"
                                + "//*[@aria-label='Release' or @title='Release']"))
                .resolve();
    }

    public Locator featureReleaseTooltip(String releaseName) {
        return new ResilientLocator(page, "Feature release details tooltip")
                .custom("Tooltip for the joined release", () -> page.getByRole(AriaRole.TOOLTIP)
                        .filter(new Locator.FilterOptions().setHasText(releaseName)))
                .custom("Visible tooltip content for the joined release", () -> page.locator(
                                ".tooltip-inner:visible, .MuiTooltip-tooltip:visible")
                        .filter(new Locator.FilterOptions().setHasText(releaseName)))
                .resolve();
    }
}
