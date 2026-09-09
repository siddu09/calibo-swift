package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/**
 * Auto-generated Page Object for: PortfolioViewPage
 * Source URL : https://accelerate-qa.calibo.com/portfolios/portfolio-view/7e4c45eb-41ce-43de-89d8-efa406db7d15#details
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */

public class ProductPortfolioViewPage {

    private final Page page;

    public ProductPortfolioViewPage(Page page) {
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

    public Locator addProduct() {
        return new ResilientLocator(page, "Add Product")
                .byXPath("//i[@class='icon-add_icon icon-l']")
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

    // <button> index=19  text="Product Portfolios"
    public Locator productPortfolios() {
        return new ResilientLocator(page, "Product Portfolios")
                .byText("Product Portfolios")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=20
    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/button[1]")
                .resolve();
    }

    // <div> index=21  text="Details Products"
    public Locator detailsProducts() {
        return new ResilientLocator(page, "Details Products")
                .byRole(AriaRole.TABLIST, "icon label tabs")
                .byCss("[aria-label=\"icon label tabs\"]")
                .byText("Details Products")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <a> index=22  text="Details"
    public Locator details() {
        return new ResilientLocator(page, "Details")
                .byRole(AriaRole.TAB, "Details")
                .byText("Details")
                .byCss("a[href=\"/portfolios/portfolio-view/7e4c45eb-41ce-43de-89d8-efa406db7d15#details\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=23  text="Products"
    public Locator products() {
        return new ResilientLocator(page, "Products")
                .byRole(AriaRole.TAB, "Products")
                .byText("Products")
                .byCss("a[href=\"/portfolios/portfolio-view/7e4c45eb-41ce-43de-89d8-efa406db7d15#projects\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/a[2]")
                .resolve();
    }

    // <button> index=24  text="more_horiz"
    public Locator moreHoriz() {
        return new ResilientLocator(page, "more_horiz")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[3]/div[2]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    public Locator editPortfolio() {
        return new ResilientLocator(page, "Edit Product Portfolio")
                .byRole(AriaRole.MENUITEM, "Edit")
                .byXPath("//*[self::button or self::li][contains(normalize-space(.),'Edit')]")
                .resolve();
    }

    public Locator auditHistory() {
        return new ResilientLocator(page, "Product Portfolio Audit History")
                .byRole(AriaRole.MENUITEM, "Audit History")
                .byText("Audit History")
                .resolve();
    }

    public Locator auditSearch() {
        return new ResilientLocator(page, "Audit history search")
                .byPlaceholder("Search")
                .byCss("input[type='search']")
                .resolve();
    }

    public Locator auditEventsFilter() {
        return new ResilientLocator(page, "Audit events filter")
                .byText("Events")
                .byXPath("//button[contains(normalize-space(.),'Events')]")
                .resolve();
    }

    public Locator auditObjectsFilter() {
        return new ResilientLocator(page, "Audit objects filter")
                .byText("Objects")
                .byXPath("//button[contains(normalize-space(.),'Objects')]")
                .resolve();
    }

    public Locator auditInitiatedByFilter() {
        return new ResilientLocator(page, "Audit initiated by filter")
                .byText("Initiated by")
                .byXPath("//button[contains(normalize-space(.),'Initiated by')]")
                .resolve();
    }

    public Locator auditMoreFilters() {
        return new ResilientLocator(page, "Audit more filters")
                .byText("More Filters")
                .byXPath("//button[contains(normalize-space(.),'More Filters')]")
                .resolve();
    }

    public Locator resetAuditFilters() {
        return new ResilientLocator(page, "Reset audit filters")
                .byText("Reset")
                .byXPath("//button[contains(normalize-space(.),'Reset')]")
                .resolve();
    }

    public Locator downloadAuditHistory() {
        return new ResilientLocator(page, "Download audit history")
                .byText("Download")
                .byXPath("//button[contains(normalize-space(.),'Download')]")
                .resolve();
    }

    public Locator auditDownloadFormat(String format) {
        return new ResilientLocator(page, "Audit download format: " + format)
                .byText(format)
                .byXPath("//*[self::button or self::li][normalize-space(.)='" + format + "']")
                .resolve();
    }

    public Locator deletePortfolio() {
        return new ResilientLocator(page, "Delete Product Portfolio")
                .byXPath("//*[self::button or self::li][contains(normalize-space(.),'Delete')]")
                .byRole(AriaRole.MENUITEM, "Delete")
                .resolve();
    }

    public Locator deletePortfolioReason() {
        return new ResilientLocator(page, "Product Portfolio deletion reason")
                .byXPath("//textarea")
                .byCss("textarea")
                .byXPath("//*[@contenteditable='true']")
                .resolve();
    }

    public Locator confirmDeletePortfolio() {
        return new ResilientLocator(page, "Confirm Delete Product Portfolio")
                .byXPath("//textarea/ancestor::div[.//button[normalize-space()='Delete']][1]//button[normalize-space()='Delete' and not(@disabled)]")
                .byRole(AriaRole.BUTTON, "Delete")
                .resolve();
    }

    public Locator portfolioLogo() {
        return new ResilientLocator(page, "Product Portfolio logo")
                .byXPath("//img[contains(@alt,'Portfolio') or contains(@src,'portfolio')]")
                .byCss("img[alt*='Portfolio']")
                .resolve();
    }

    // <input> index=26
    public Locator searchStakeholders() {
        return new ResilientLocator(page, "Search stakeholders...")
                .byId("search")
                .byPlaceholder("Search stakeholders...")
                .byCss("input#search")
                .byXPath("//*[@id=\"search\"]")
                .resolve();
    }

    // <a> index=29  text="©2026 Calibo Inc. All rights reserved."
    public Locator a2026CaliboIncAllRightsReserved() {
        return new ResilientLocator(page, "©2026 Calibo Inc. All rights reserved.")
                .byText("©2026 Calibo Inc. All rights reserved.")
                .byCss("a[href=\"https://www.calibo.com/\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=30  text="Privacy Policy"
    public Locator privacyPolicy() {
        return new ResilientLocator(page, "Privacy Policy")
                .byText("Privacy Policy")
                .byCss("a[href=\"/privacy-policy\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[2]")
                .resolve();
    }

    // <a> index=31  text="Terms of Service"
    public Locator termsOfService() {
        return new ResilientLocator(page, "Terms of Service")
                .byText("Terms of Service")
                .byCss("a[href=\"/terms-and-conditions\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[3]")
                .resolve();
    }

    // <button> index=32  text="Support Request"
    public Locator supportRequest() {
        return new ResilientLocator(page, "Support Request")
                .byText("Support Request")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/button[1]")
                .resolve();
    }

    // <iframe> index=33
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

    public Locator fetchPortfolioName() {

        Locator portfolioName =
                new ResilientLocator(
                        page,
                        "Portfolio Name")
                        .byXPath("//h1[@data-cy='portfolio-view-title']")
                        .custom(
                                "Portfolio view title",
                                () -> page.locator(
                                        "h1[data-cy='portfolio-view-title']")
                        )
                        .resolve();

        portfolioName.waitFor();

        return portfolioName;
    }
    public Locator fetchPortfolioDescription() {

        Locator portfolioDescription =
                new ResilientLocator(
                        page,
                        "Portfolio Description")
                        .byXPath("//label[text()='Description']/following-sibling::p[1]")
                        .custom(
                                "Portfolio description",
                                () -> page.locator("label")
                                        .filter(
                                                new Locator.FilterOptions()
                                                        .setHasText("Description")
                                        )
                                        .locator(
                                                "xpath=following-sibling::p[1]"
                                        )
                        )
                        .resolve();

        portfolioDescription.waitFor();

        return portfolioDescription;
    }
    // Help panel close button
    public Locator helpPanelCloseButton() {
        return new ResilientLocator(page, "Help panel close button")
                .byCss("button[data-testid='close'][aria-label='Close']")
                .byXPath("//button[@data-testid='close'][@aria-label='Close']")
                .resolve();
    }
    // "Yes" button in unsaved changes confirmation dialog
    public Locator unsavedChangesConfirmationYesButton() {
        return new ResilientLocator(page, "Unsaved changes confirmation Yes button")
                .byXPath("//div[contains(.,'unsaved changes')]//button[normalize-space()='Yes']")
                .resolve();
    }
}
