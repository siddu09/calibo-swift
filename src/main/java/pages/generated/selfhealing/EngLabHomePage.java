package pages.generated.selfhealing;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/**
 * Auto-generated Page Object for: EngLabHome
 * Source URL : https://accelerate-qa.calibo.com/projects/project-view/76313540-f5e3-460a-a1b4-7b9d563dd820#WORKSTREAMS
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
public class EngLabHomePage {

    private final Page page;

    public EngLabHomePage(Page page) {
        this.page = page;
    }

    // <div> index=0  text="OrganizationProductTeamOntology Manageme..."
    public Locator organizationProductTeamOntologyManagementDataIngestionMinimumViableDataDataAssetsFeatureEngine() {
        return new ResilientLocator(page, "OrganizationProductTeamOntology ManagementData IngestionMini...")
                .byTestId("left-drawer")
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
    public Locator organizationProductTeam() {
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

    // <div> index=18
    public Locator buttonDiv2() {
        return new ResilientLocator(page, "buttonDiv2")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[10]/span[1]/div[1]/div[1]")
                .resolve();
    }

    // <div> index=19  text="Ontology ManagementData Ingestion"
    public Locator ontologyManagementDataIngestion() {
        return new ResilientLocator(page, "Ontology ManagementData Ingestion")
                .byRole(AriaRole.REGION, "Ontology ManagementData Ingestion")
                .byText("Ontology ManagementData Ingestion")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[10]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <a> index=20  text="Ontology Management"
    public Locator ontologyManagement() {
        return new ResilientLocator(page, "Ontology Management")
                .byTestId("left-drawer-semantic-layer-ontology-management")
                .byText("Ontology Management")
                .byCss("a[href=\"/semantic-layer/ontology-management\"]")
                .byCss("a[data-testid=\"left-drawer-semantic-layer-ontology-management\"]")
                .byXPath("//*[@data-testid=\"left-drawer-semantic-layer-ontology-management\"]")
                .resolve();
    }

    // <button> index=21  text="Ontology Management"
    public Locator ontologyManagement2() {
        return new ResilientLocator(page, "Ontology Management")
                .byText("Ontology Management")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[10]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[1]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=22  text="Data Ingestion"
    public Locator dataIngestion() {
        return new ResilientLocator(page, "Data Ingestion")
                .byTestId("left-drawer-semantic-layer-data-ingestion")
                .byText("Data Ingestion")
                .byCss("a[href=\"/semantic-layer/data-integration\"]")
                .byCss("a[data-testid=\"left-drawer-semantic-layer-data-ingestion\"]")
                .byXPath("//*[@data-testid=\"left-drawer-semantic-layer-data-ingestion\"]")
                .resolve();
    }

    // <button> index=23  text="Data Ingestion"
    public Locator dataIngestion2() {
        return new ResilientLocator(page, "Data Ingestion")
                .byText("Data Ingestion")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[10]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[2]/li[1]/button[1]")
                .resolve();
    }

    // <div> index=24
    public Locator buttonDiv3() {
        return new ResilientLocator(page, "buttonDiv3")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[1]")
                .resolve();
    }

    // <div> index=25  text="Minimum Viable DataData AssetsFeature En..."
    public Locator minimumViableDataDataAssetsFeatureEngineeringDataProfilingDataLineageMetadataCatalog() {
        return new ResilientLocator(page, "Minimum Viable DataData AssetsFeature EngineeringData Profil...")
                .byRole(AriaRole.REGION, "Minimum Viable DataData AssetsFeature EngineeringData ProfilingData LineageMetadata Catalog")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <a> index=26  text="Minimum Viable Data"
    public Locator minimumViableData() {
        return new ResilientLocator(page, "Minimum Viable Data")
                .byTestId("left-drawer-data-lab-minimum-viable-data")
                .byText("Minimum Viable Data")
                .byCss("a[href=\"/mvd/\"]")
                .byCss("a[data-testid=\"left-drawer-data-lab-minimum-viable-data\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-lab-minimum-viable-data\"]")
                .resolve();
    }

    // <button> index=27  text="Minimum Viable Data"
    public Locator minimumViableData2() {
        return new ResilientLocator(page, "Minimum Viable Data")
                .byText("Minimum Viable Data")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[1]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=28  text="Data Assets"
    public Locator dataAssets() {
        return new ResilientLocator(page, "Data Assets")
                .byTestId("left-drawer-data-lab-data-assets")
                .byText("Data Assets")
                .byCss("a[href=\"/assets\"]")
                .byCss("a[data-testid=\"left-drawer-data-lab-data-assets\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-lab-data-assets\"]")
                .resolve();
    }

    // <button> index=29  text="Data Assets"
    public Locator dataAssets2() {
        return new ResilientLocator(page, "Data Assets")
                .byText("Data Assets")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[2]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=30  text="Feature Engineering"
    public Locator featureEngineering() {
        return new ResilientLocator(page, "Feature Engineering")
                .byTestId("left-drawer-data-lab-feature-engineering")
                .byText("Feature Engineering")
                .byCss("a[href=\"/feature-engineering\"]")
                .byCss("a[data-testid=\"left-drawer-data-lab-feature-engineering\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-lab-feature-engineering\"]")
                .resolve();
    }

    // <button> index=31  text="Feature Engineering"
    public Locator featureEngineering2() {
        return new ResilientLocator(page, "Feature Engineering")
                .byText("Feature Engineering")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[3]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=32  text="Data Profiling"
    public Locator dataProfiling() {
        return new ResilientLocator(page, "Data Profiling")
                .byTestId("left-drawer-data-lab-data-profiling")
                .byText("Data Profiling")
                .byCss("a[href=\"/profiling\"]")
                .byCss("a[data-testid=\"left-drawer-data-lab-data-profiling\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-lab-data-profiling\"]")
                .resolve();
    }

    // <button> index=33  text="Data Profiling"
    public Locator dataProfiling2() {
        return new ResilientLocator(page, "Data Profiling")
                .byText("Data Profiling")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[4]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=34  text="Data Lineage"
    public Locator dataLineage() {
        return new ResilientLocator(page, "Data Lineage")
                .byTestId("left-drawer-data-lab-data-lineage")
                .byText("Data Lineage")
                .byCss("a[href=\"/view-lineage\"]")
                .byCss("a[data-testid=\"left-drawer-data-lab-data-lineage\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-lab-data-lineage\"]")
                .resolve();
    }

    // <button> index=35  text="Data Lineage"
    public Locator dataLineage2() {
        return new ResilientLocator(page, "Data Lineage")
                .byText("Data Lineage")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[5]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=36  text="Metadata Catalog"
    public Locator metadataCatalog() {
        return new ResilientLocator(page, "Metadata Catalog")
                .byTestId("left-drawer-data-lab-metadata-catalog")
                .byText("Metadata Catalog")
                .byCss("a[href=\"/metadata\"]")
                .byCss("a[data-testid=\"left-drawer-data-lab-metadata-catalog\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-lab-metadata-catalog\"]")
                .resolve();
    }

    // <button> index=37  text="Metadata Catalog"
    public Locator metadataCatalog2() {
        return new ResilientLocator(page, "Metadata Catalog")
                .byText("Metadata Catalog")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/ul[1]/span[11]/span[1]/div[1]/div[2]/div[1]/div[1]/div[1]/ul[1]/a[6]/li[1]/button[1]")
                .resolve();
    }

    // <a> index=38
    public Locator engLabHomeA() {
        return new ResilientLocator(page, "engLabHomeA")
                .byTestId("left-drawer-configuration")
                .byCss("a[href=\"/tenants/settings/eng-lab-home\"]")
                .byCss("a[data-testid=\"left-drawer-configuration\"]")
                .byXPath("//*[@data-testid=\"left-drawer-configuration\"]")
                .resolve();
    }

    // <button> index=39  text="Products"
    public Locator products() {
        return new ResilientLocator(page, "Products")
                .byText("Products")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=40
    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=41
    public Locator button2() {
        return new ResilientLocator(page, "button2")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=42  text="close"
    public Locator close() {
        return new ResilientLocator(page, "close")
                .byText("close")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[4]/div[1]/button[1]")
                .resolve();
    }

    // <div> index=43
    public Locator statusDiv() {
        return new ResilientLocator(page, "statusDiv")
                .byCss("div")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]")
                .resolve();
    }

    // <a> index=44  text="©2026 Calibo Inc. All rights reserved."
    public Locator a2026CaliboIncAllRightsReserved() {
        return new ResilientLocator(page, "©2026 Calibo Inc. All rights reserved.")
                .byText("©2026 Calibo Inc. All rights reserved.")
                .byCss("a[href=\"https://www.calibo.com/\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=45  text="Privacy Policy"
    public Locator privacyPolicy() {
        return new ResilientLocator(page, "Privacy Policy")
                .byText("Privacy Policy")
                .byCss("a[href=\"/privacy-policy\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[2]")
                .resolve();
    }

    // <a> index=46  text="Terms of Service"
    public Locator termsOfService() {
        return new ResilientLocator(page, "Terms of Service")
                .byText("Terms of Service")
                .byCss("a[href=\"/terms-and-conditions\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[3]")
                .resolve();
    }

    // <button> index=47  text="Support Request"
    public Locator supportRequest() {
        return new ResilientLocator(page, "Support Request")
                .byText("Support Request")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/button[1]")
                .resolve();
    }

    // <iframe> index=48
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
