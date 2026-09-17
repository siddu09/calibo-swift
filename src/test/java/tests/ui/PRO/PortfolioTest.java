package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
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
    private ProExecutionData executionData;


    @BeforeMethod
    @Step("Setup: Login and initialize ProductPortfolioFlows")
    public void setUp() {
        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);
        loginBuildingBlock.proLogin();
        executionData = new ProExecutionData();
        portfolioFlows = new ProductPortfolioFlows(page, executionData);
        LoggerUtil.LOGGER.info("============================ Setup completed =========================");
    }


    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPrivatePortfolioWithMandatoryFields() {
        portfolioFlows.createPortfolioWithMandatoryFields();
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] Recording execution results...");
        ProExecutionResultWriter.write("ProE2E", "Pro E2E With Mandatory Fields", executionData);
        portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "Pro E2E With Mandatory Fields");
   }
    @Test(groups = {Constants.PRO_REGRESSION})
    public void cancelPortfolioCreation() {
        portfolioFlows.cancelPortfolioCreation();
        ProExecutionResultWriter.write("ProE2E", "cancelPortfolioCreation", executionData);
   }
    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPublicPortfolioWithAllFields() {
        portfolioFlows.createPublicPortfolioWithAllFields();
        ProExecutionResultWriter.write("ProE2E", "createPublicPortfolioWithAllFields", executionData);
        portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "createPublicPortfolioWithAllFields");
}
        @Test(groups = {Constants.PRO_REGRESSION})
        public void addMultipleYearFinancials () {
            portfolioFlows.addMultipleYearFinancials();
            ProExecutionResultWriter.write("ProE2E", "addMultipleYearFinancials", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "addMultipleYearFinancials");
        }
        @Test(groups = {Constants.PRO_REGRESSION})
        public void validateGloballyConfiguredCustomFields () {
            portfolioFlows.validateGloballyConfiguredCustomFields();
            ProExecutionResultWriter.write("ProE2E", "validateGloballyConfiguredCustomFields", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "validateGloballyConfiguredCustomFields");
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void editExistingPortfolio () {
            portfolioFlows.editExistingPortfolio();
            ProExecutionResultWriter.write("ProE2E", "editExistingPortfolio", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "editExistingPortfolio");
        }
//
        @Test(groups = {Constants.PRO_REGRESSION})
        public void editWorkflowTemplate () {
            portfolioFlows.editWorkflowTemplate();
            ProExecutionResultWriter.write("ProE2E", "editWorkflowTemplate", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "editWorkflowTemplate");
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void validateMissingMandatoryFieldsDuringCreation () {
            portfolioFlows.validateMissingMandatoryFields();
            ProExecutionResultWriter.write("ProE2E", "validateMissingMandatoryFieldsDuringCreation", executionData);
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void validateMissingOwnerAndWorkflowFields () {
            portfolioFlows.validateMissingOwnerAndWorkflowFields();
            ProExecutionResultWriter.write("ProE2E", "validateMissingOwnerAndWorkflowFields", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "validateMissingOwnerAndWorkflowFields");
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void validateInvalidLogoFormat () {
            portfolioFlows.validateInvalidLogoFormat();
            ProExecutionResultWriter.write("ProE2E", "validateInvalidLogoFormat", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "validateInvalidLogoFormat");
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void validateMyProductPortfolios () {
            portfolioFlows.validateMyProductPortfolios();
            ProExecutionResultWriter.write("ProE2E", "validateMyProductPortfolios", executionData);
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void validateAllProductPortfolios () {
            portfolioFlows.validateAllProductPortfolios();
            ProExecutionResultWriter.write("ProE2E", "validateAllProductPortfolios", executionData);
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void validateAuditHistorySearchAndFilters () {
            portfolioFlows.validateAuditHistorySearchAndFilters();
            ProExecutionResultWriter.write("ProE2E", "validateAuditHistorySearchAndFilters", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "validateAuditHistorySearchAndFilters");
        }

        @Test(groups = {Constants.PRO_REGRESSION})
        public void downloadAuditHistoryInPdfAndCsvFormats () {
            portfolioFlows.downloadAuditHistoryInPdfAndCsvFormats();
            ProExecutionResultWriter.write("ProE2E", "downloadAuditHistoryInPdfAndCsvFormats", executionData);
            portfolioFlows.deletePortfolioWithoutProduct("ProE2E", "downloadAuditHistoryInPdfAndCsvFormats");
        }

    }

