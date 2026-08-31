package tests.api.E2E;

import base.apibase.BaseTest;
import org.testng.annotations.Test;
import tests.api.DPS.DataIngestionPiplineWithDatabaseCrawlerCatalog;
import tests.api.DevSecops.DeployKubernetesApiTests;
import tests.api.PRO.PortfolioApiTests;
import tests.api.PRO.ProductApiTest;
import tests.api.PRO.FeatureApiTest;

public class apiEndToEndTest_ProMF_CreateInCatalog_MSSQL_DB_Snowflake_AWS_Kubernetes_GitLab_JFrog_Jenkins_SonarQube_WoO extends BaseTest {

    @Test(groups = "E2E")
    public void EndToEndTest() {
        PortfolioApiTests portfolio = new PortfolioApiTests();
        ProductApiTest product = new ProductApiTest();
        FeatureApiTest feature = new FeatureApiTest();
        DeployKubernetesApiTests deployKubernetes = new DeployKubernetesApiTests();
        DataIngestionPiplineWithDatabaseCrawlerCatalog dataIngestion = new DataIngestionPiplineWithDatabaseCrawlerCatalog();
        portfolio.createPortfolioWithMandatoryFields();
        product.createProduct("product1");
        feature.createFeature("product1", "feature1");
        product.createProduct("product2");
        feature.createFeature("product2", "feature2");
        deployKubernetes.deployKubernetes();
        dataIngestion.mssqlCatalogDatabricksSnowflakePipeline();

    }
}