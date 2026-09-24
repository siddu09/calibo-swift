package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import selfhealingHandler.ResilientLocator;
import utils.CommonMethods;
import utils.LoggerUtil;


public class ProductPage {

    private final Page page;

    public ProductPage(Page page) {
        this.page = page;
    }

    public Locator searchProduct(String placeholder) {
        return new ResilientLocator(page, "Product search")
                .byPlaceholder(placeholder)
                .byCss("input[placeholder*='Search Product']")
                .resolve();
    }

    public Locator productByName(String name) {
        return new ResilientLocator(page, "Product: " + name)
                .custom("Exact product name", () -> {
                    Locator product = page.getByText(name, new Page.GetByTextOptions().setExact(true));
                    product.waitFor();
                    return product;
                })
                .byRole(AriaRole.LINK, name)
                .resolve();
    }

    public Locator newProduct(String label) {
        return new ResilientLocator(page, label)
                .custom("New Product button", () -> {
                    Locator button = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(label));
                    button.waitFor();
                    return button;
                })
                .byXPath("//button[contains(normalize-space(), '" + label + "')]")
                .resolve();
    }

    public Locator featurePrompt(String message) {
        return new ResilientLocator(page, "Feature creation prompt")
                .custom("Exact prompt", () -> page.getByText(message, new Page.GetByTextOptions().setExact(true)))
                .byXPath("//*[normalize-space(text())='" + message + "']")
                .resolve();
    }

    public Locator featureChoice(String label) {
        return new ResilientLocator(page, "Feature choice: " + label)
                .custom("Exact choice button", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//button[normalize-space()='" + label + "']")
                .resolve();
    }

    // <button> index=0
    public Locator openDrawer() {
        return new ResilientLocator(page, "open drawer")
                .byCss("[aria-label=\"open drawer\"]")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <a> index=1
    public Locator buttonA() {
        return new ResilientLocator(page, "buttonA")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[1]/a[1]")
                .resolve();
    }

    // <a> index=2
    public Locator dashboardA() {
        return new ResilientLocator(page, "dashboardA")
                .byCss("a[href=\"/dashboard\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[2]/a[1]")
                .resolve();
    }

    // <a> index=3
    public Locator portfoliosA() {
        return new ResilientLocator(page, "portfoliosA")
                .byCss("a[href=\"/portfolios\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[3]/a[1]")
                .resolve();
    }

    // <a> index=4
    public Locator projectsA() {
        return new ResilientLocator(page, "projectsA")
                .byCss("a[href=\"/projects\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[4]/a[1]")
                .resolve();
    }

    // <a> index=5
    public Locator releaseTrainA() {
        return new ResilientLocator(page, "releaseTrainA")
                .byCss("a[href=\"/release-train\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[5]/a[1]")
                .resolve();
    }

    // <a> index=6
    public Locator opsIntelligenceA() {
        return new ResilientLocator(page, "opsIntelligenceA")
                .byCss("a[href=\"/ops-intelligence\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[6]/a[1]")
                .resolve();
    }

    // <div> index=7
    public Locator buttonDiv() {
        return new ResilientLocator(page, "buttonDiv")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[1]")
                .resolve();
    }

    // <div> index=8  text="OrganizationProductTeam"
    public Locator organizationProductTeam() {
        return new ResilientLocator(page, "OrganizationProductTeam")
                .byRole(AriaRole.REGION, "OrganizationProductTeam")
                .byText("OrganizationProductTeam")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <a> index=9  text="Organization"
    public Locator organization() {
        return new ResilientLocator(page, "Organization")
                .byText("Organization")
                .byCss("a[href=\"/technical-maturity/overall?type=tnt\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[1]")
                .resolve();
    }

    // <button> index=10  text="Organization"
    public Locator organization2() {
        return new ResilientLocator(page, "Organization")
                .byText("Organization")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[1]/li[1]/button[1]")
                .resolve();
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

    // <button> index=12  text="Product"
    public Locator product2() {
        return new ResilientLocator(page, "Product")
                .byText("Product")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[2]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=13  text="Team"
    public Locator team() {
        return new ResilientLocator(page, "Team")
                .byText("Team")
                .byCss("a[href=\"/technical-maturity/overall/teams\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[3]")
                .resolve();
    }

    // <button> index=14  text="Team"
    public Locator team2() {
        return new ResilientLocator(page, "Team")
                .byText("Team")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[3]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=15
    public Locator resourcePlanningDashboardA() {
        return new ResilientLocator(page, "resourcePlanningDashboardA")
                .byCss("a[href=\"/resource-planning-dashboard\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[8]/a[1]")
                .resolve();
    }

    // <a> index=16
    public Locator dataPipelineCenterA() {
        return new ResilientLocator(page, "dataPipelineCenterA")
                .byCss("a[href=\"/data-pipeline-center\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[9]/a[1]")
                .resolve();
    }

    // <a> index=17
    public Locator engLabHomeA() {
        return new ResilientLocator(page, "engLabHomeA")
                .byCss("a[href=\"/tenants/settings/eng-lab-home\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[10]/a[1]")
                .resolve();
    }

    // <div> index=18  text="My Products All Products"
    public Locator myProductsAllProducts() {
        return new ResilientLocator(page, "My Products All Products")
                .byRole(AriaRole.TABLIST, "icon label tabs")
                .byCss("[aria-label=\"icon label tabs\"]")
                .byText("My Products All Products")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <button> index=19  text="My Products"
    public Locator myProducts() {
        return new ResilientLocator(page, "My Products")
                .byRole(AriaRole.TAB, "My Products")
                .byText("My Products")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=20  text="All Products"
    public Locator allProducts() {
        return new ResilientLocator(page, "All Products")
                .byRole(AriaRole.TAB, "All Products")
                .byText("All Products")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/button[2]")
                .resolve();
    }

    // <button> index=21
    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/div[1]/button[1]")
                .resolve();
    }

    // <div> index=22  text="No products/Ideas found add New Product"
    public Locator noProductsIdeasFoundAddNewProduct() {
        return new ResilientLocator(page, "No products/Ideas found add New Product")
                .byId("wrapped-tabpanel-My Products")
                .byRole(AriaRole.TABPANEL, "No products/Ideas found add New Product")
                .byText("No products/Ideas found add New Product")
                .byCss("div#wrapped-tabpanel-My Products")
                .byXPath("//*[@id=\"wrapped-tabpanel-My Products\"]")
                .resolve();
    }

    // <button> index=23  text="add New Product"
    public Locator addNewProduct() {
        return new ResilientLocator(page, "add New Product")
                .byText("add New Product")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <a> index=25  text="©2026 Calibo Inc. All rights reserved."
    public Locator a2026CaliboIncAllRightsReserved() {
        return new ResilientLocator(page, "©2026 Calibo Inc. All rights reserved.")
                .byText("©2026 Calibo Inc. All rights reserved.")
                .byCss("a[href=\"https://www.calibo.com/\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=26  text="Privacy Policy"
    public Locator privacyPolicy() {
        return new ResilientLocator(page, "Privacy Policy")
                .byText("Privacy Policy")
                .byCss("a[href=\"/privacy-policy\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[2]")
                .resolve();
    }

    // <a> index=27  text="Terms of Service"
    public Locator termsOfService() {
        return new ResilientLocator(page, "Terms of Service")
                .byText("Terms of Service")
                .byCss("a[href=\"/terms-and-conditions\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[3]")
                .resolve();
    }

    // <button> index=28  text="Support Request"
    public Locator supportRequest() {
        return new ResilientLocator(page, "Support Request")
                .byText("Support Request")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/button[1]")
                .resolve();
    }

    // <iframe> index=29
    public Locator jSDWidget() {
        return new ResilientLocator(page, "JSD widget")
                .byId("jsd-widget")
                .byName("JSD widget")
                .byRole(AriaRole.APPLICATION, "JSD widget")
                .byCss("[aria-label=\"JSD widget\"]")
                .byCss("iframe#jsd-widget")
                .byXPath("//*[@id=\"jsd-widget\"]")
                .resolve();
    }

    public Locator clickProductType(String productType) {
        return new ResilientLocator(page, "Get Started: " + productType)
                .byXPath("//h3[text()='" + productType + "']/ancestor::div[@class='d-flex flex-column']/following-sibling::div/button")
                .byXPath("//h3[normalize-space()='" + productType + "']/ancestor::div[.//button][1]//button")
                .resolve();
    }

    public void clickOnGetStartedButton(String productize) {
        clickProductType(productize).click();
        LoggerUtil.LOGGER.info(productize + " clicked");
    }

    // ======================================================================
    //  Feature Creation Dialog (post-product creation)
    // ======================================================================

    /**
     * "Yes" button on "Do you want to create a feature for this product?" dialog.
     * Prioritizes semantic role lookup, then falls back to text/XPath.
     */
    public Locator btnFeatureCreationYes() {
        return new ResilientLocator(page, "Feature Creation Yes")
                .byRole(AriaRole.BUTTON, "Yes")
                .byText("Yes")
                .byXPath("//button[normalize-space()='Yes']")
                .byXPath("//button[contains(.,'Yes')]")
                .byCss("button.btn-primary")
                .resolve();
    }

    /**
     * "No" button on "Do you want to create a feature for this product?" dialog.
     */
    public Locator btnFeatureCreationNo() {
        return new ResilientLocator(page, "Feature Creation No")
                .byRole(AriaRole.BUTTON, "No")
                .byText("No")
                .byXPath("//button[normalize-space()='No']")
                .byXPath("//button[contains(.,'No')]")
                .byCss("button.btn-secondary")
                .resolve();
    }

    /**
     * "Skip for now" button on product additional details dialog.
     */
    public Locator btnSkipForNow() {
        return new ResilientLocator(page, "Skip for now")
                .byRole(AriaRole.BUTTON, "Skip for now")
                .byText("Skip for now")
                .byXPath("//button[normalize-space()='Skip for now']")
                .byXPath("//button[contains(.,'Skip for now')]")
                .resolve();
    }

    /**
     * "Maybe Later" link on "Do you want to create a feature?" dialog.
     */
    public Locator btnMaybeLater() {
        return new ResilientLocator(page, "Maybe Later")
                .byRole(AriaRole.LINK, "Maybe Later")
                .byText("Maybe Later")
                .byXPath("//a[normalize-space()='Maybe Later']")
                .byXPath("//button[normalize-space()='Maybe Later']")
                .resolve();
    }

    public Locator clickOnThreeDot() {
        return new ResilientLocator(page, "Click on Three Dot")
                .byXPath("//h1//i[@class='material-icons']")
                .resolve();
    }

    public void selectAllCheckBoxes() {
        Locator locator = page.locator("//span[text()='Select All']/ancestor::label/span/span/input");
        int count = locator.count();
        for (int i = 0; i < count; i++) {
            locator.nth(i).check();
        }
    }

    public Locator deletionInProgress() {
        Locator locator = page.locator("//div[@role='progressbar']");
//        return new ResilientLocator(page, "Deletion In Progress")
//                .byXPath("//div[@role='progressbar']")
//                .resolve();
        return locator;
    }

    public void checkDeletionToComplete() {
        if(CommonMethods.isElementPresent(deletionInProgress())) {
            deletionInProgress().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        }
    }
//    public void waitForDeletionToComplete() {
//        Locator deletionInProgress = deletionInProgress();
//        deletionInProgress.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
//    }

    public Locator enterComments() {
        return new ResilientLocator(page, "Enter Comments")
                .byXPath("//textarea[@placeholder='Enter your comments...']")
                .resolve();
    }

    public Locator textFeature()
    {
        Locator loc = page.locator("//label[text()='Feature']");
//        return new ResilientLocator(page, "Feature Text")
//                .byXPath("//label[text()='Feature']")
//                .resolve();
        return loc;
    }
    public Locator search() {
        return new ResilientLocator(page, "Search")
                .byPlaceholder("Search Product")
                .byCss("#search")
                .byCss("input[type=\"text\"][placeholder=\"Search Product\"]")
                .byCss("'input#search'")
                .resolve();
    }
    public Locator select(String productName) {
        return new ResilientLocator(page, "Select Product")
                .byText(productName)
                .resolve();
    }
}
