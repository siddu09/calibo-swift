package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/**
 * Auto-generated Page Object for: NewProductPortfolioPage
 * Source URL : https://accelerate-qa.calibo.com/portfolios/add-portfolio
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
public class NewProductPortfolioPagePage {

    private final Page page;

    public NewProductPortfolioPagePage(Page page) {
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

    // <input> index=20
    public Locator name() {
        return new ResilientLocator(page, "name")
                .byName("name")
                .byCss("input[name=\"name\"]")
                .byXPath("//*[@name=\"name\"]")
                .resolve();
    }

    // <input> index=21
    public Locator input() {
        return new ResilientLocator(page, "input")
                .byCss("input")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/input[1]")
                .resolve();
    }

    // <textarea> index=22
    public Locator description() {
        return new ResilientLocator(page, "description")
                .byName("description")
                .byCss("textarea[name=\"description\"]")
                .byXPath("//*[@name=\"description\"]")
                .resolve();
    }

    // <button> index=23  text="Cancel"
    public Locator cancel() {
        return new ResilientLocator(page, "Cancel")
                .byText("Cancel")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[4]/button[1]")
                .resolve();
    }

    // <button> index=24  text="Create"
    public Locator create() {
        return new ResilientLocator(page, "Create")
                .byText("Create")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[4]/button[2]")
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

    // Enable Public toggle/checkbox — NOT captured in original auto-generated skeleton.
// VERIFY against live DOM before relying on this in production runs.
    public Locator enablePublicToggle() {
        return new ResilientLocator(page, "Enable Public")
                .byXPath("//*[contains(normalize-space(),'Public')]//input[@type='checkbox']")
                .byXPath("//label[contains(normalize-space(),'Public')]/preceding-sibling::input[@type='checkbox']")
                .byXPath("//label[contains(normalize-space(),'Public')]/input[@type='checkbox']")
                .byCss("input[type='checkbox'][name*='public' i]")
                .resolve();
    }

    public void enablePublicPortfolio() {
        Locator toggle = enablePublicToggle();
        if (!toggle.isChecked()) {
            toggle.click();
        }
    }

    public Locator nameRequiredValidation() {
        return new ResilientLocator(page, "Portfolio name required validation")
                .byXPath("//*[@name='name']/following::*[contains(normalize-space(.),'required')][1]")
                .byText("Name is required")
                .resolve();
    }

    public Locator descriptionRequiredValidation() {
        return new ResilientLocator(page, "Portfolio description required validation")
                .byXPath("//*[@name='description']/following::*[contains(normalize-space(.),'required')][1]")
                .byText("Description is required")
                .resolve();
    }

}
