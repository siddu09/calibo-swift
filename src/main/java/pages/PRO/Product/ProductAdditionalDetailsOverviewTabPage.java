package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/**
 * Auto-generated Page Object for: ProductAdditionalDetailsOverviewTabPage
 * Source URL : https://accelerate-qa.calibo.com/projects/edit-project/66cf14fe-2bb2-497f-8149-f65c854928cb?op=create
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
public class ProductAdditionalDetailsOverviewTabPage {

    private final Page page;

    public ProductAdditionalDetailsOverviewTabPage(Page page) {
        this.page = page;
    }

    // <div> index=0  text="OrganizationProductTeam"
    public Locator organizationProductTeam() {
        return new ResilientLocator(page, "OrganizationProductTeam")
                .byTestId("left-drawer")
                .byText("OrganizationProductTeam")
                .byCss("div[data-testid=\"left-drawer\"]")
                .byXPath("//*[@data-testid=\"left-drawer\"]")
                .resolve();
    }

    // <button> index=1
    public Locator openDrawer() {
        return new ResilientLocator(page, "open drawer")
                .byCss("[aria-label=\"open drawer\"]")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <a> index=2
    public Locator buttonA() {
        return new ResilientLocator(page, "buttonA")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[1]/a[1]")
                .resolve();
    }

    // <a> index=3
    public Locator dashboardA() {
        return new ResilientLocator(page, "dashboardA")
                .byTestId("left-drawer-dashboard")
                .byCss("a[href=\"/dashboard\"]")
                .byCss("a[data-testid=\"left-drawer-dashboard\"]")
                .byXPath("//*[@data-testid=\"left-drawer-dashboard\"]")
                .resolve();
    }

    // <a> index=4
    public Locator portfoliosA() {
        return new ResilientLocator(page, "portfoliosA")
                .byTestId("left-drawer-product-portfolios")
                .byCss("a[href=\"/portfolios\"]")
                .byCss("a[data-testid=\"left-drawer-product-portfolios\"]")
                .byXPath("//*[@data-testid=\"left-drawer-product-portfolios\"]")
                .resolve();
    }

    // <a> index=5
    public Locator projectsA() {
        return new ResilientLocator(page, "projectsA")
                .byTestId("left-drawer-products")
                .byCss("a[href=\"/projects\"]")
                .byCss("a[data-testid=\"left-drawer-products\"]")
                .byXPath("//*[@data-testid=\"left-drawer-products\"]")
                .resolve();
    }

    // <a> index=6
    public Locator releaseTrainA() {
        return new ResilientLocator(page, "releaseTrainA")
                .byTestId("left-drawer-release-trains")
                .byCss("a[href=\"/release-train\"]")
                .byCss("a[data-testid=\"left-drawer-release-trains\"]")
                .byXPath("//*[@data-testid=\"left-drawer-release-trains\"]")
                .resolve();
    }

    // <a> index=7
    public Locator opsIntelligenceA() {
        return new ResilientLocator(page, "opsIntelligenceA")
                .byTestId("left-drawer-monitoring")
                .byCss("a[href=\"/ops-intelligence\"]")
                .byCss("a[data-testid=\"left-drawer-monitoring\"]")
                .byXPath("//*[@data-testid=\"left-drawer-monitoring\"]")
                .resolve();
    }

    // <div> index=8
    public Locator buttonDiv() {
        return new ResilientLocator(page, "buttonDiv")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[1]")
                .resolve();
    }

    // <div> index=9  text="OrganizationProductTeam"
    public Locator organizationProductTeam2() {
        return new ResilientLocator(page, "OrganizationProductTeam")
                .byRole(AriaRole.REGION, "OrganizationProductTeam")
                .byText("OrganizationProductTeam")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <a> index=10  text="Organization"
    public Locator organization() {
        return new ResilientLocator(page, "Organization")
                .byTestId("left-drawer-maturity-assessment-organization")
                .byText("Organization")
                .byCss("a[href=\"/technical-maturity/overall?type=tnt\"]")
                .byCss("a[data-testid=\"left-drawer-maturity-assessment-organization\"]")
                .byXPath("//*[@data-testid=\"left-drawer-maturity-assessment-organization\"]")
                .resolve();
    }

    // <button> index=11  text="Organization"
    public Locator organization2() {
        return new ResilientLocator(page, "Organization")
                .byText("Organization")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[1]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=12  text="Product"
    public Locator product() {
        return new ResilientLocator(page, "Product")
                .byTestId("left-drawer-maturity-assessment-product")
                .byText("Product")
                .byCss("a[href=\"/technical-maturity/overall/products\"]")
                .byCss("a[data-testid=\"left-drawer-maturity-assessment-product\"]")
                .byXPath("//*[@data-testid=\"left-drawer-maturity-assessment-product\"]")
                .resolve();
    }

    // <button> index=13  text="Product"
    public Locator product2() {
        return new ResilientLocator(page, "Product")
                .byText("Product")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[2]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=14  text="Team"
    public Locator team() {
        return new ResilientLocator(page, "Team")
                .byTestId("left-drawer-maturity-assessment-team")
                .byText("Team")
                .byCss("a[href=\"/technical-maturity/overall/teams\"]")
                .byCss("a[data-testid=\"left-drawer-maturity-assessment-team\"]")
                .byXPath("//*[@data-testid=\"left-drawer-maturity-assessment-team\"]")
                .resolve();
    }

    // <button> index=15  text="Team"
    public Locator team2() {
        return new ResilientLocator(page, "Team")
                .byText("Team")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[7]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[3]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=16
    public Locator resourcePlanningDashboardA() {
        return new ResilientLocator(page, "resourcePlanningDashboardA")
                .byTestId("left-drawer-resource-planning")
                .byCss("a[href=\"/resource-planning-dashboard\"]")
                .byCss("a[data-testid=\"left-drawer-resource-planning\"]")
                .byXPath("//*[@data-testid=\"left-drawer-resource-planning\"]")
                .resolve();
    }

    // <a> index=17
    public Locator dataPipelineCenterA() {
        return new ResilientLocator(page, "dataPipelineCenterA")
                .byTestId("left-drawer-data-pipeline-center")
                .byCss("a[href=\"/data-pipeline-center\"]")
                .byCss("a[data-testid=\"left-drawer-data-pipeline-center\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-pipeline-center\"]")
                .resolve();
    }

    // <a> index=18
    public Locator engLabHomeA() {
        return new ResilientLocator(page, "engLabHomeA")
                .byTestId("left-drawer-configuration")
                .byCss("a[href=\"/tenants/settings/eng-lab-home\"]")
                .byCss("a[data-testid=\"left-drawer-configuration\"]")
                .byXPath("//*[@data-testid=\"left-drawer-configuration\"]")
                .resolve();
    }

    // <button> index=19  text="chevron_left Back"
    public Locator chevronLeftBack() {
        return new ResilientLocator(page, "chevron_left Back")
                .byText("chevron_left Back")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/button[1]")
                .resolve();
    }

    // <button> index=20
    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <div> index=21  text="Overview Custom Fields Milestones Financ..."
    public Locator overviewCustomFieldsMilestonesFinancialsFeatureApprovalWorkflowOthers() {
        return new ResilientLocator(page, "Overview Custom Fields Milestones Financials Feature Approva...")
                .byRole(AriaRole.TABLIST, "icon label tabs")
                .byCss("[aria-label=\"icon label tabs\"]")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <button> index=22  text="Overview"
    public Locator overview() {
        return new ResilientLocator(page, "Overview")
                .byRole(AriaRole.TAB, "Overview")
                .byText("Overview")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=23  text="Custom Fields"
    public Locator customFields() {
        return new ResilientLocator(page, "Custom Fields")
                .byRole(AriaRole.TAB, "Custom Fields")
                .byText("Custom Fields")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/button[2]")
                .resolve();
    }

    // <button> index=24  text="Milestones"
    public Locator milestones() {
        return new ResilientLocator(page, "Milestones")
                .byRole(AriaRole.TAB, "Milestones")
                .byText("Milestones")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/button[3]")
                .resolve();
    }

    // <button> index=25  text="Financials"
    public Locator financials() {
        return new ResilientLocator(page, "Financials")
                .byRole(AriaRole.TAB, "Financials")
                .byText("Financials")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/button[4]")
                .resolve();
    }

    // <button> index=26  text="Feature Approval Workflow"
    public Locator featureApprovalWorkflow() {
        return new ResilientLocator(page, "Feature Approval Workflow")
                .byRole(AriaRole.TAB, "Feature Approval Workflow")
                .byText("Feature Approval Workflow")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/button[5]")
                .resolve();
    }

    // <button> index=27  text="Others"
    public Locator others() {
        return new ResilientLocator(page, "Others")
                .byRole(AriaRole.TAB, "Others")
                .byText("Others")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/button[6]")
                .resolve();
    }

    // <div> index=29  text="View details expand_more Owner(s) search..."
    public Locator viewDetailsExpandMoreOwnerSearchNikhilRamdasBhaleraoPriorityHighCloseArrow() {
        return new ResilientLocator(page, "View details expand_more Owner search Nikhil Ramdas Bhalerao...")
                .byId("wrapped-tabpanel-OVERVIEW")
                .byRole(AriaRole.TABPANEL, "View details expand_more Owner(s) search Nikhil Ramdas Bhalerao Priority (Optional) High close arrow")
                .byCss("div#wrapped-tabpanel-OVERVIEW")
                .byXPath("//*[@id=\"wrapped-tabpanel-OVERVIEW\"]")
                .resolve();
    }

    // <button> index=30  text="View details expand_more"
    public Locator viewDetailsExpandMore() {
        return new ResilientLocator(page, "View details expand_more")
                .byText("View details expand_more")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[1]/div[2]/div[2]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <input> index=31
    public Locator reactSelect9Input() {
        return new ResilientLocator(page, "react-select-9-input")
                .byId("react-select-9-input")
                .byCss("input#react-select-9-input")
                .byXPath("//*[@id=\"react-select-9-input\"]")
                .resolve();
    }

    // <input> index=32
    public Locator reactSelect10Input() {
        return new ResilientLocator(page, "react-select-10-input")
                .byId("react-select-10-input")
                .byCss("input#react-select-10-input")
                .byXPath("//*[@id=\"react-select-10-input\"]")
                .resolve();
    }

    // <button> index=38  text="Skip for now"
    public Locator skipForNow() {
        return new ResilientLocator(page, "Skip for now")
                .byText("Skip for now")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/form[1]/div[2]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=39  text="Save"
    public Locator save() {
        return new ResilientLocator(page, "Save")
                .byXPath("//button[text()='Save']")
                .resolve();
    }

    // <a> index=40  text="©2026 Calibo Inc. All rights reserved."
    public Locator a2026CaliboIncAllRightsReserved() {
        return new ResilientLocator(page, "©2026 Calibo Inc. All rights reserved.")
                .byText("©2026 Calibo Inc. All rights reserved.")
                .byCss("a[href=\"https://www.calibo.com/\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=41  text="Privacy Policy"
    public Locator privacyPolicy() {
        return new ResilientLocator(page, "Privacy Policy")
                .byText("Privacy Policy")
                .byCss("a[href=\"/privacy-policy\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[2]")
                .resolve();
    }

    // <a> index=42  text="Terms of Service"
    public Locator termsOfService() {
        return new ResilientLocator(page, "Terms of Service")
                .byText("Terms of Service")
                .byCss("a[href=\"/terms-and-conditions\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[3]")
                .resolve();
    }

    // <button> index=43  text="Support Request"
    public Locator supportRequest() {
        return new ResilientLocator(page, "Support Request")
                .byText("Support Request")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/button[1]")
                .resolve();
    }

    // <iframe> index=44
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

    public Locator dropdownOwner() {
        return new ResilientLocator(page, "Dropdown Owner")
                .byCss("react-select__value-container react-select__value-container--is-multi css-1vwlh22")
                .byLabel("Owner(s)")
                .byXPath("//i[@class='material-icons md-18 search-icon']")
                .resolve();
    }


    //div[@class='react-select__menu css-26l3qy-menu']/div/div[text()='"+owner+"']
    public void selectOwner(String owner) {
        dropdownOwner().click();
        page.waitForCondition(() -> page.locator("//div[@class='react-select__menu css-26l3qy-menu']/div/div[text()='"+owner+"']").isVisible());
        page.locator("//div[@class='react-select__menu css-26l3qy-menu']/div/div[text()='"+owner+"']").click();

    }

}
