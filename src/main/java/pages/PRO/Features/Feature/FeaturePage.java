package pages.PRO.Features.Feature;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;


public class FeaturePage {

    private final Page page;

    public FeaturePage(Page page) {
        this.page = page;
    }


    // <a> index=11  text="Product"
    public Locator product() {
        return new ResilientLocator(page, "Product")
                .byText("Product")
                .byCss("a[href=\"/technical-maturity/overall/products\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[2]")
                .resolve();
    }




    // <button> index=21
    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/div[1]/button[1]")
                .resolve();
    }


    public Locator clickProductType(String productType) {
        Locator loc = page.locator("//h3[text()='"+productType+"']/ancestor::div[@class='d-flex flex-column']/following-sibling::div/button");
        return loc;
    }


    public Locator addNewFeature() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("New Feature"));
    }

    public Locator checkCircleDefine() {
        return new ResilientLocator(page, "Define")
                .byXPath("//h3[text()='Define']")
                .resolve();
    }

    public Locator checkCircleDesign() {
        return new ResilientLocator(page, "Design")
                .byXPath("//h3[text()='Design']")
                .resolve();
    }

    public Locator checkCircleDevelop() {
        return new ResilientLocator(page, "Develop")
                .byXPath("//h3[text()='Develop']")
                .resolve();
    }

    public Locator selectFeatureStatusOptions(String featureStatus) {
        Locator loc = page.locator("//label[text()='"+featureStatus+"']");
        return loc;
    }

    public Locator dropDownArrow() {
        return new ResilientLocator(page, "dropDownArrow")
                .byXPath("//i[text()='arrow_drop_down']")
                .resolve();
    }


    public void selectFeatureStatus(String featureStatus) {
        dropDownArrow().click();
        selectFeatureStatusOptions(featureStatus).click();
        LoggerUtil.LOGGER.info(featureStatus +" Selected Feature Status");
    }

    public Locator title() {
        return new ResilientLocator(page, "Name")
                .byCss("input[name='name']")
                .byXPath("//*[@name=\"name\"]")
                .resolve();
    }
    public Locator description() {
        return new ResilientLocator(page, "Description")
                .byCss("textarea[name=\"description\"]")
                .byXPath("//*[@name=\"description\"]")
                .resolve();
    }

    public Locator create() {
        return new ResilientLocator(page, "Create")
                .byXPath("//button[text()='Create']")
                .byXPath("//button[@type='submit']")
                .resolve();
    }
    public Locator selectFeature(String featureName) {
        return new ResilientLocator(page, "Select Feature")
                .byText(featureName)
                .resolve();
    }

    public Locator selectPhase(String phase) {
        return new ResilientLocator(page, "Select Phase")
                .byXPath("//h4[text()='"+phase+"']")
                .byXPath("//h3[text()='"+phase+"']")
                .byText(phase)
                .resolve();
    }

}
