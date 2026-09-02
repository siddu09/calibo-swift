package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import tests.base.BaseUITest;
import tests.constants.Constants;

@Test(groups = {"PRO-UI"})
public class ProductTest extends BaseUITest {

    private final ProExecutionData executionData = new ProExecutionData();
    private ProductFlows productFlows;

    @BeforeMethod
    @Step("Setup: Initialize test environment and Login")
    public void setUp() {
        new LoginBuildingBlock(page).login();
        ProductPortfolioFlows portfolioFlows = new ProductPortfolioFlows(page, executionData);
        if (executionData.getPortfolioName() == null) {
            portfolioFlows.createPortfolioWithMandatoryFields();
        } else {
            portfolioFlows.openExistingPortfolio();
        }
        productFlows = new ProductFlows(page, executionData);
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create product using mandatory fields only.")
    public void createProductWithMandatoryFields() {
        productFlows.createProductWithMandatoryFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create private product with mandatory fields within a portfolio.")
    public void createPrivateProductWithMandatoryFields() {
        productFlows.createPrivateProductWithMandatoryFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create public Product and validate all Additional Details sections.")
    public void createPublicProductWithAllFields() {
        productFlows.createPublicProductWithAllFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Validate globally configured Product custom fields section.")
    public void validateGloballyConfiguredProductCustomFields() {
        productFlows.validateGloballyConfiguredProductCustomFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create Product with only the Design phase.")
    public void createProductWithDesignPhase() {
        productFlows.createProductWithDesignPhase();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create product with Define and Design phases.")
    public void createProductWithDefineAndDesignPhases() {
        productFlows.createProductWithDefineAndDesignPhases();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create product with Develop and Deploy phases.")
    public void createProductWithDevelopAndDeployPhases() {
        productFlows.createProductWithDevelopAndDeployPhases();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Cancel Product creation.")
    public void cancelProductCreation() {
        productFlows.cancelProductCreation();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create Operationalize Product with mandatory fields.")
    public void createOperationalizeProductWithMandatoryFields() {
        productFlows.createOperationalizeProductWithMandatoryFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create Operationalize Product and validate all Additional Details sections.")
    public void createOperationalizeProductWithAllFields() {
        productFlows.createOperationalizeProductWithAllFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Validate My Products displays Products created by the current user.")
    public void validateMyProducts() {
        productFlows.validateMyProducts();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Validate All Products displays Products available to the user.")
    public void validateAllProducts() {
        productFlows.validateAllProducts();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Validate Product audit history.")
    public void validateProductAuditHistory() {
        productFlows.validateProductAuditHistory();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Search Product audit logs.")
    public void searchProductAuditLogs() {
        productFlows.searchProductAuditLogs();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Filter Product audit history by events.")
    public void filterProductAuditHistoryByEvents() {
        productFlows.filterProductAuditHistoryByEvents();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Filter Product audit history by objects.")
    public void filterProductAuditHistoryByObjects() {
        productFlows.filterProductAuditHistoryByObjects();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Download Product audit history as PDF.")
    public void downloadProductAuditHistoryAsPdf() {
        productFlows.downloadProductAuditHistoryAsPdf();
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Download Product audit history as CSV.")
    public void downloadProductAuditHistoryAsCsv() {
        productFlows.downloadProductAuditHistoryAsCsv();
    }
}
