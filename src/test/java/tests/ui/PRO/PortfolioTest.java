package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import tests.base.BaseUITest;
import tests.constants.Constants;

import utils.LoggerUtil;

public class PortfolioTest extends BaseUITest {
    private ProductPortfolioFlows portfolioFlows;
    private ProductFlows productFlows;
    private ProExecutionData executionData;

    @BeforeMethod
    @Step("Setup: Login and Initialize ProductPortfolioFlows")
    public void setUp(java.lang.reflect.Method testMethod) {
        LoggerUtil.LOGGER.info("[Portfolio test] START {}", testMethod.getName());
        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);
        loginBuildingBlock.proLogin();
        executionData = new ProExecutionData();
        portfolioFlows = new ProductPortfolioFlows(page, executionData);
        productFlows = new ProductFlows(page, executionData);
        LoggerUtil.LOGGER.info("============================ Setup completed =========================");
    }
    @Step("Create Private Portfolio with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void createPrivatePortfolioWithMandatoryFields() {
        portfolioFlows.createPortfolioWithMandatoryFields();
        ProExecutionResultWriter.write("ProE2E", "Pro E2E With Mandatory Fields", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Delete Portfolio with Product")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void deletePortfolioWithProduct() {
        portfolioFlows.createPortfolioWithMandatoryFields();
        productFlows.createProductWithMandatoryFields();
        ProExecutionResultWriter.write("ProE2E", "deletePortfolioWithProduct", executionData);
        productFlows.deleteProduct();
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Cancel Portfolio Creation")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void cancelPortfolioCreation() {
        portfolioFlows.cancelPortfolioCreation();
        ProExecutionResultWriter.write("ProE2E", "cancelPortfolioCreation", executionData);
    }
    @Step("Create Public Portfolio with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void createPublicPortfolioWithAllFields() {
        portfolioFlows.createPublicPortfolioWithAllFields();
        ProExecutionResultWriter.write("ProE2E", "createPublicPortfolioWithAllFields", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Create Multiple Year Financials")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void addMultipleYearFinancials () {
        portfolioFlows.addMultipleYearFinancials();
        ProExecutionResultWriter.write("ProE2E", "addMultipleYearFinancials", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Validate Globally Configured portfolio Custom Fields")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void validateGloballyConfiguredCustomFields () {
        portfolioFlows.validateGloballyConfiguredCustomFields();
        ProExecutionResultWriter.write("ProE2E", "validateGloballyConfiguredCustomFields", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Validate Edit Existing Portfolio")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void editExistingPortfolio () {
        portfolioFlows.editExistingPortfolio();
        ProExecutionResultWriter.write("ProE2E", "editExistingPortfolio", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Validate Edit Workflow Template")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void editWorkflowTemplate () {
        portfolioFlows.editWorkflowTemplate();
        ProExecutionResultWriter.write("ProE2E", "editWorkflowTemplate", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Step("Verify that required-field validation is displayed for Name and Description ")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void validateMissingMandatoryFieldsDuringCreation () {
        portfolioFlows.validateMissingMandatoryFields();
        ProExecutionResultWriter.write("ProE2E", "validateMissingMandatoryFieldsDuringCreation", executionData);
    }
    @Step("Validate missing-owner message and save-validation error")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void validateMissingOwnerAndWorkflowFields () {
        portfolioFlows.validateMissingOwnerAndWorkflowFields();
        ProExecutionResultWriter.write("ProE2E", "validateMissingOwnerAndWorkflowFields", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Validate Invalid Logo Format")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void validateInvalidLogoFormat () {
        portfolioFlows.validateInvalidLogoFormat();
        ProExecutionResultWriter.write("ProE2E", "validateInvalidLogoFormat", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Validate My Product Portfolios")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void validateMyProductPortfolios () {
        portfolioFlows.validateMyProductPortfolios();
        ProExecutionResultWriter.write("ProE2E", "validateMyProductPortfolios", executionData);
    }
    @Step("Validate All Product Portfolios")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void validateAllProductPortfolios () {
        portfolioFlows.validateAllProductPortfolios();
        ProExecutionResultWriter.write("ProE2E", "validateAllProductPortfolios", executionData);
    }
    @Step("Validate Audit History Search and Filters")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void validateAuditHistorySearchAndFilters () {
        portfolioFlows.validateAuditHistorySearchAndFilters();
        ProExecutionResultWriter.write("ProE2E", "validateAuditHistorySearchAndFilters", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Step("Download Audit History in PDF and CSV Formats")
    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void downloadAuditHistoryInPdfAndCsvFormats () {
        portfolioFlows.downloadAuditHistoryInPdfAndCsvFormats();
        ProExecutionResultWriter.write("ProE2E", "downloadAuditHistoryInPdfAndCsvFormats", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
    }
}
