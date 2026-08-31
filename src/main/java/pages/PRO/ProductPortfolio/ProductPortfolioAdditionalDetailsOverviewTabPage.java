package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/**
 * Auto-generated Page Object for: ProductPortfolioAdditionalDetailsOverviewTab
 * Source URL : https://accelerate-qa.calibo.com/portfolios/b976beb1-8aa2-4848-8bc1-66c627d7c6f4/additional-details?op=create
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
public class ProductPortfolioAdditionalDetailsOverviewTabPage {

    private final Page page;

    public ProductPortfolioAdditionalDetailsOverviewTabPage(Page page) {
        this.page = page;
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

    // <button> index=18  text="chevron_left Back"
    public Locator chevronLeftBack() {
        return new ResilientLocator(page, "chevron_left Back")
                .byText("chevron_left Back")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/button[1]")
                .resolve();
    }

    // <button> index=19
    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <div> index=20  text="Overview Custom Fields Financials Produc..."
    public Locator overviewCustomFieldsFinancialsProductApprovalWorkflowOthers() {
        return new ResilientLocator(page, "Overview Custom Fields Financials Product Approval Workflow ...")
                .byRole(AriaRole.TABLIST, "icon label tabs")
                .byCss("[aria-label=\"icon label tabs\"]")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <button> index=21  text="Overview"
    public Locator overview() {
        return new ResilientLocator(page, "Overview")
                .byRole(AriaRole.TAB, "Overview")
                .byText("Overview")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=22  text="Custom Fields"
    public Locator customFields() {
        return new ResilientLocator(page, "Custom Fields")
                .byRole(AriaRole.TAB, "Custom Fields")
                .byText("Custom Fields")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[2]")
                .resolve();
    }

    // <button> index=23  text="Financials"
    public Locator financials() {
        return new ResilientLocator(page, "Financials")
                .byRole(AriaRole.TAB, "Financials")
                .byText("Financials")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[3]")
                .resolve();
    }

    // <button> index=24  text="Product Approval Workflow"
    public Locator productApprovalWorkflow() {
        return new ResilientLocator(page, "Product Approval Workflow")
                .byRole(AriaRole.TAB, "Product Approval Workflow")
                .byText("Product Approval Workflow")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[4]")
                .resolve();
    }

    // <button> index=25  text="Others"
    public Locator others() {
        return new ResilientLocator(page, "Others")
                .byRole(AriaRole.TAB, "Others")
                .byText("Others")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[5]")
                .resolve();
    }

    // <div> index=26  text="View details expand_more Business Outcom..."
    public Locator viewDetailsExpandMoreBusinessOutcomePriorityMediumOwnersSearchNikhilRamdasBha() {
        return new ResilientLocator(page, "View details expand_more Business Outcome Priority Medium Ow...")
                .byId("wrapped-tabpanel-OVERVIEW")
                .byRole(AriaRole.TABPANEL, "View details expand_more Business Outcome (Optional) Priority Medium Owners search Nikhil Ramdas Bha")
                .byCss("div#wrapped-tabpanel-OVERVIEW")
                .byXPath("//*[@id=\"wrapped-tabpanel-OVERVIEW\"]")
                .resolve();
    }

    // <button> index=27  text="View details expand_more"
    public Locator viewDetailsExpandMore() {
        return new ResilientLocator(page, "View details expand_more")
                .byText("View details expand_more")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <textarea> index=28
    public Locator textarea() {
        return new ResilientLocator(page, "textarea")
                .byCss("textarea")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/textarea[1]")
                .resolve();
    }

    // <input> index=30
    public Locator reactSelect8Input() {
        return new ResilientLocator(page, "react-select-8-input")
                .byId("react-select-8-input")
                .byCss("input#react-select-8-input")
                .byXPath("//*[@id=\"react-select-8-input\"]")
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

    public Locator businessOutcome() {
        return new ResilientLocator(page, "Business Outcome")
                .byXPath("//label[contains(normalize-space(.),'Business Outcome')]/following::textarea[1]")
                .byLabelInput("Business Outcome")
                .resolve();
    }

    public Locator priority() {
        return new ResilientLocator(page, "Portfolio Priority")
                .byXPath("//label[normalize-space()='Priority']/following::div[contains(@class,'select')][1]")
                .byText("Medium")
                .resolve();
    }

    public Locator priorityOption(String priority) {
        return new ResilientLocator(page, "Portfolio priority option: " + priority)
                .byText(priority)
                .byXPath("//div[contains(@class,'menu')]//*[normalize-space()='" + priority + "']")
                .resolve();
    }

    public Locator ownerRequiredValidation() {
        return new ResilientLocator(page, "Portfolio owner required validation")
                .byXPath("//*[contains(normalize-space(.),'Owner') and contains(normalize-space(.),'required')]")
                .byText("Owner is required")
                .resolve();
    }

    public Locator clearOwners() {
        return new ResilientLocator(page, "Clear portfolio owners")
                .byXPath("//label[contains(normalize-space(.),'Owners')]/following::div[contains(@class,'clear-indicator')][1]")
                .byCss("[class*='clear-indicator']")
                .resolve();
    }

    public Locator owners() {
        return new ResilientLocator(page, "Portfolio owners")
                .byXPath("//label[contains(normalize-space(.),'Owners')]/following::input[1]")
                .byPlaceholder("Search")
                .resolve();
    }

    public Locator ownerOption(String owner) {
        return new ResilientLocator(page, "Portfolio owner: " + owner)
                .byXPath("//div[contains(@class,'menu')]//*[contains(normalize-space(.),'" + owner + "')]")
                .byText(owner)
                .resolve();
    }

    // <button> index=37  text="Skip for now"
    public Locator skipForNow() {
        return new ResilientLocator(page, "Skip for now")
                .byText("Skip for now")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[3]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=38  text="Save"
    public Locator save() {
        return new ResilientLocator(page, "Save")
                .byText("Save")
                .byXPath("//button[text()='Save']")
                .byCss("text=Save")
                .resolve();
    }

    // <a> index=39  text="©2026 Calibo Inc. All rights reserved."
    public Locator a2026CaliboIncAllRightsReserved() {
        return new ResilientLocator(page, "©2026 Calibo Inc. All rights reserved.")
                .byText("©2026 Calibo Inc. All rights reserved.")
                .byCss("a[href=\"https://www.calibo.com/\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=40  text="Privacy Policy"
    public Locator privacyPolicy() {
        return new ResilientLocator(page, "Privacy Policy")
                .byText("Privacy Policy")
                .byCss("a[href=\"/privacy-policy\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[2]")
                .resolve();
    }

    // <a> index=41  text="Terms of Service"
    public Locator termsOfService() {
        return new ResilientLocator(page, "Terms of Service")
                .byText("Terms of Service")
                .byCss("a[href=\"/terms-and-conditions\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[3]")
                .resolve();
    }

    // <button> index=42  text="Support Request"
    public Locator supportRequest() {
        return new ResilientLocator(page, "Support Request")
                .byText("Support Request")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/button[1]")
                .resolve();
    }

    // <iframe> index=43
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

}
