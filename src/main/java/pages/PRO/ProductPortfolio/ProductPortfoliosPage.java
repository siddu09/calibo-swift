package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;


public class ProductPortfoliosPage {

    private final Page page;

    public ProductPortfoliosPage(Page page) {
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
                .byTestId("left-drawer-product-portfolios")
                .byCss("a[href=\"/portfolios\"]")
                .byXPath("//*[@data-testid='left-drawer-product-portfolios']")
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

    public Locator search() {
        return new ResilientLocator(page, "Search")
                .byPlaceholder("Search Product Portfolios")
                .byCss("#search")
                .byCss("input[type=\"text\"][placeholder=\"Search Product Portfolios\"]")
                .byCss("'input#search'")
                .resolve();
    }

    public Locator selectPortfolio(String portfolioName) {
        return new ResilientLocator(page, "Select Portfolio")
                .byCss("h2[title='"+portfolioName+"']")
                .byRole(AriaRole.HEADING, portfolioName)
                .byText(portfolioName)
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

    // <div> index=18  text="My Product Portfolios All Product Portfo..."
    public Locator myProductPortfoliosAllProductPortfolios() {
        return new ResilientLocator(page, "My Product Portfolios All Product Portfolios")
                .byRole(AriaRole.TABLIST, "icon label tabs")
                .byCss("[aria-label=\"icon label tabs\"]")
                .byText("My Product Portfolios All Product Portfolios")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <button> index=19  text="My Product Portfolios"
    public Locator myProductPortfolios() {
        return new ResilientLocator(page, "My Product Portfolios")
                .byRole(AriaRole.TAB, "My Product Portfolios")
                .byText("My Product Portfolios")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=20  text="All Product Portfolios"
    public Locator allProductPortfolios() {
        return new ResilientLocator(page, "All Product Portfolios")
                .byRole(AriaRole.TAB, "All Product Portfolios")
                .byText("All Product Portfolios")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/button[2]")
                .resolve();
    }

    public Locator portfolioCards() {
        return new ResilientLocator(page, "Product portfolio cards")
                .byCss("h2[title]")
                .byXPath("//div[contains(@id,'PORTFOLIOS')]//h2[@title]")
                .resolve();
    }

    public Locator activePortfolioTab(String tabName) {
        return new ResilientLocator(page, "Active portfolio tab: " + tabName)
                .byXPath("//button[@role='tab' and @aria-selected='true'][normalize-space(.)='" + tabName + "']")
                .resolve();
    }

    // <button> index=21
    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=22  text="close"
    public Locator close() {
        return new ResilientLocator(page, "close")
                .byText("close")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[4]/div[1]/button[1]")
                .resolve();
    }

    // <div> index=23  text="No product portfolio found add New Produ..."
    public Locator noProductPortfolioFoundAddNewProductPortfolio() {
        return new ResilientLocator(page, "No product portfolio found add New Product Portfolio")
                .byId("wrapped-tabpanel-MY_PORTFOLIOS")
                .byRole(AriaRole.TABPANEL, "No product portfolio found add New Product Portfolio")
                .byText("No product portfolio found add New Product Portfolio")
                .byCss("div#wrapped-tabpanel-MY_PORTFOLIOS")
                .byXPath("//*[@id=\"wrapped-tabpanel-MY_PORTFOLIOS\"]")
                .resolve();
    }

    // <button> index=24  text="add New Product Portfolio"
    public Locator addNewProductPortfolio() {
        LoggerUtil.LOGGER.info(
                "[DEBUG] Has empty-state panel = {}",
                page.locator("#wrapped-tabpanel-MY_PORTFOLIOS").count()
        );
        return new ResilientLocator(page, "add New Product Portfolio")
                .byText("New Product Portfolio")
                .byXPath("//button[text()='New Product Portfolio']")
                .resolve();
    }

    // <a> index=26  text="©2026 Calibo Inc. All rights reserved."
    public Locator a2026CaliboIncAllRightsReserved() {
        return new ResilientLocator(page, "©2026 Calibo Inc. All rights reserved.")
                .byText("©2026 Calibo Inc. All rights reserved.")
                .byCss("a[href=\"https://www.calibo.com/\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=27  text="Privacy Policy"
    public Locator privacyPolicy() {
        return new ResilientLocator(page, "Privacy Policy")
                .byText("Privacy Policy")
                .byCss("a[href=\"/privacy-policy\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[2]")
                .resolve();
    }

    // <a> index=28  text="Terms of Service"
    public Locator termsOfService() {
        return new ResilientLocator(page, "Terms of Service")
                .byText("Terms of Service")
                .byCss("a[href=\"/terms-and-conditions\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[3]")
                .resolve();
    }

    // <button> index=29  text="Support Request"
    public Locator supportRequest() {
        return new ResilientLocator(page, "Support Request")
                .byText("Support Request")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/button[1]")
                .resolve();
    }

    // <iframe> index=30
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
