package pages.AIML.CSL.OntologyManagement;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/**
 * Auto-generated Page Object for: ontology-management
 * Source URL : https://accelerate-qa.calibo.com/semantic-layer/ontology-management
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
public class OntologyManagementPage {

    private final Page page;

    public OntologyManagementPage(Page page) {
        this.page = page;
    }

    // <div> index=0  text="1"
    public Locator div1() {
        return new ResilientLocator(page, "1")
                .byTestId("left-drawer")
                .byText("1")
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
    public Locator minimumViableDataDataAssetsFeatureEngineeringDataProfilingDataLineageCatalogSearch() {
        return new ResilientLocator(page, "Minimum Viable DataData AssetsFeature EngineeringData Profil...")
                .byRole(AriaRole.REGION, "Minimum Viable DataData AssetsFeature EngineeringData ProfilingData LineageCatalog Search")
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

    // <a> index=36  text="Catalog Search"
    public Locator catalogSearch() {
        return new ResilientLocator(page, "Catalog Search")
                .byTestId("left-drawer-data-lab-catalog-search")
                .byText("Catalog Search")
                .byCss("a[href=\"/discover/metadata\"]")
                .byCss("a[data-testid=\"left-drawer-data-lab-catalog-search\"]")
                .byXPath("//*[@data-testid=\"left-drawer-data-lab-catalog-search\"]")
                .resolve();
    }

    // <button> index=37  text="Catalog Search"
    public Locator catalogSearch2() {
        return new ResilientLocator(page, "Catalog Search")
                .byText("Catalog Search")
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

    // <input> index=39
    public Locator searchOntology() {
        return new ResilientLocator(page, "Search ontology")
                .byId("search")
                .byPlaceholder("Search ontology by name, domain, type")
                .byCss("[aria-label=\"Search ontology\"]")
                .byCss("input#search")
                .byXPath("//*[@id=\"search\"]")
                .resolve();
    }

    // <button> index=40  text="tune Filters"
    public Locator tuneFilters() {
        return new ResilientLocator(page, "tune Filters")
                .byText("tune Filters")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/button[1]")
                .resolve();
    }

    // <button> index=41  text="refresh"
    public Locator refresh() {
        return new ResilientLocator(page, "refresh")
                .byText("refresh")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=42  text="add Create Ontology"
    public Locator addCreateOntology() {
        return new ResilientLocator(page, "add Create Ontology")
                .byText("add Create Ontology")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/button[1]")
                .resolve();
    }

    // <th> index=43  text="Domain"
    public Locator domain() {
        return new ResilientLocator(page, "Domain")
                .byCss("[aria-label=\"Domain sortable\"]")
                .byText("Domain")
                .byCss("th")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/thead[1]/tr[1]/th[2]")
                .resolve();
    }

    // <th> index=44  text="Data Category"
    public Locator dataCategory() {
        return new ResilientLocator(page, "Data Category")
                .byCss("[aria-label=\"Data Category sortable\"]")
                .byText("Data Category")
                .byCss("th")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/thead[1]/tr[1]/th[3]")
                .resolve();
    }

    // <th> index=45  text="Created By"
    public Locator createdBy() {
        return new ResilientLocator(page, "Created By")
                .byCss("[aria-label=\"Created By sortable\"]")
                .byText("Created By")
                .byCss("th")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/thead[1]/tr[1]/th[5]")
                .resolve();
    }

    // <th> index=46  text="Created On"
    public Locator createdOn() {
        return new ResilientLocator(page, "Created On")
                .byCss("[aria-label=\"Created On sortable\"]")
                .byText("Created On")
                .byCss("th")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/thead[1]/tr[1]/th[6]")
                .resolve();
    }

    // <th> index=47  text="Last Updated By"
    public Locator lastUpdatedBy() {
        return new ResilientLocator(page, "Last Updated By")
                .byCss("[aria-label=\"Last Updated By sortable\"]")
                .byText("Last Updated By")
                .byCss("th")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/thead[1]/tr[1]/th[7]")
                .resolve();
    }

    // <th> index=48  text="Last Updated On"
    public Locator lastUpdatedOn() {
        return new ResilientLocator(page, "Last Updated On")
                .byCss("[aria-label=\"Last Updated On sort desc\"]")
                .byText("Last Updated On")
                .byCss("th")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/thead[1]/tr[1]/th[8]")
                .resolve();
    }

    // <button> index=49  text="Testing_Ontology_25th Aug"
    public Locator testingOntology25thAug() {
        return new ResilientLocator(page, "Testing_Ontology_25th Aug")
                .byText("Testing_Ontology_25th Aug")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[1]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=50  text="more_horiz"
    public Locator moreHoriz() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[1]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=51  text="Onto test aug"
    public Locator ontoTestAug() {
        return new ResilientLocator(page, "Onto test aug")
                .byText("Onto test aug")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[2]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=52  text="more_horiz"
    public Locator moreHoriz2() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[2]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=53  text="Test_ontology_25th Aug"
    public Locator testOntology25thAug() {
        return new ResilientLocator(page, "Test_ontology_25th Aug")
                .byText("Test_ontology_25th Aug")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[3]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=54  text="more_horiz"
    public Locator moreHoriz3() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[3]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=55  text="Strutured onto crreation"
    public Locator struturedOntoCrreation() {
        return new ResilientLocator(page, "Strutured onto crreation")
                .byText("Strutured onto crreation")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[4]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=56  text="more_horiz"
    public Locator moreHoriz4() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[4]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=57  text="PDF Ingestionlatest"
    public Locator pDFIngestionlatest() {
        return new ResilientLocator(page, "PDF Ingestionlatest")
                .byText("PDF Ingestionlatest")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[5]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=58  text="more_horiz"
    public Locator moreHoriz5() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[5]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=59  text="Test123464757"
    public Locator test123464757() {
        return new ResilientLocator(page, "Test123464757")
                .byText("Test123464757")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[6]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=60  text="more_horiz"
    public Locator moreHoriz6() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[6]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=61  text="pdf onto"
    public Locator pdfOnto() {
        return new ResilientLocator(page, "pdf onto")
                .byText("pdf onto")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[7]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=62  text="more_horiz"
    public Locator moreHoriz7() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[7]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=63  text="Protected PDF"
    public Locator protectedPDF() {
        return new ResilientLocator(page, "Protected PDF")
                .byText("Protected PDF")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[8]/td[1]/button[1]")
                .resolve();
    }

    // <span> index=64  text="info"
    public Locator info() {
        return new ResilientLocator(page, "info")
                .byCss("[aria-label=\"Ontology error message\"]")
                .byText("info")
                .byCss("span")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[8]/td[9]/div[1]/div[1]/span[1]")
                .resolve();
    }

    // <button> index=65  text="more_horiz"
    public Locator moreHoriz8() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[8]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=66  text="System Defined - Retail"
    public Locator systemDefinedRetail() {
        return new ResilientLocator(page, "System Defined - Retail")
                .byText("System Defined - Retail")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[9]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=67  text="more_horiz"
    public Locator moreHoriz9() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[9]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=68  text="System Defined - Finance"
    public Locator systemDefinedFinance() {
        return new ResilientLocator(page, "System Defined - Finance")
                .byText("System Defined - Finance")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[10]/td[1]/button[1]")
                .resolve();
    }

    // <button> index=69  text="more_horiz"
    public Locator moreHoriz10() {
        return new ResilientLocator(page, "more_horiz")
                .byCss("[aria-label=\"More Actions\"]")
                .byText("more_horiz")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[10]/td[10]/div[1]/button[1]")
                .resolve();
    }

    // <button> index=70  text="10"
    public Locator button10() {
        return new ResilientLocator(page, "10")
                .byId("pageDropDown")
                .byText("10")
                .byCss("button#pageDropDown")
                .byXPath("//*[@id=\"pageDropDown\"]")
                .resolve();
    }

    // <input> index=80
    public Locator input() {
        return new ResilientLocator(page, "input")
                .byCss("input")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[2]/div[1]/div[2]/div[1]/input[1]")
                .resolve();
    }

    // <a> index=81  text="1"
    public Locator a1() {
        return new ResilientLocator(page, "1")
                .byText("1")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[3]/ul[1]/li[1]/a[1]")
                .resolve();
    }

    // <a> index=82  text="2"
    public Locator a22() {
        return new ResilientLocator(page, "2")
                .byText("2")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[3]/ul[1]/li[2]/a[1]")
                .resolve();
    }

    // <a> index=83  text="3"
    public Locator a3() {
        return new ResilientLocator(page, "3")
                .byText("3")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[3]/ul[1]/li[3]/a[1]")
                .resolve();
    }

    // <a> index=84  text="4"
    public Locator a4() {
        return new ResilientLocator(page, "4")
                .byText("4")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[3]/ul[1]/li[4]/a[1]")
                .resolve();
    }

    // <a> index=85  text="5"
    public Locator a5() {
        return new ResilientLocator(page, "5")
                .byText("5")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[3]/ul[1]/li[5]/a[1]")
                .resolve();
    }

    // <a> index=86  text=">"
    public Locator a() {
        return new ResilientLocator(page, ">")
                .byText(">")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[3]/ul[1]/li[6]/a[1]")
                .resolve();
    }

    // <a> index=87  text=">>"
    public Locator a2() {
        return new ResilientLocator(page, ">>")
                .byText(">>")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[3]/ul[1]/li[7]/a[1]")
                .resolve();
    }

    // <a> index=88  text="©2026 Calibo Inc. All rights reserved."
    public Locator a2026CaliboIncAllRightsReserved() {
        return new ResilientLocator(page, "©2026 Calibo Inc. All rights reserved.")
                .byText("©2026 Calibo Inc. All rights reserved.")
                .byCss("a[href=\"https://www.calibo.com/\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[1]")
                .resolve();
    }

    // <a> index=89  text="Privacy Policy"
    public Locator privacyPolicy() {
        return new ResilientLocator(page, "Privacy Policy")
                .byText("Privacy Policy")
                .byCss("a[href=\"/privacy-policy\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[2]")
                .resolve();
    }

    // <a> index=90  text="Terms of Service"
    public Locator termsOfService() {
        return new ResilientLocator(page, "Terms of Service")
                .byText("Terms of Service")
                .byCss("a[href=\"/terms-and-conditions\"]")
                .byCss("a")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/footer[1]/div[1]/a[3]")
                .resolve();
    }

    // <button> index=91  text="Cookie Preferences"


    // <button> index=92  text="Support Request"
    public Locator supportRequest() {
        return new ResilientLocator(page, "Support Request")
                .byText("Support Request")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/button[1]")
                .resolve();
    }

    // <iframe> index=93
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
