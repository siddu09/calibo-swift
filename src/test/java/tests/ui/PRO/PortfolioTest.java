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
        new LoginBuildingBlock(page).login("Automation");
        executionData = new ProExecutionData();

        portfolioFlows = new ProductPortfolioFlows(page, executionData);
        LoggerUtil.LOGGER.info("============================ Setup completed =========================");

    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPrivatePortfolioWithMandatoryFields() {
        portfolioFlows.createPortfolioWithMandatoryFields();
        completeAndRecordResults();
    }
//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void cancelPortfolioCreation() {
//        portfolioFlows.cancelPortfolioCreation();
//        portfolioFlows.deletePortfolioWithoutProduct();
//   }
    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPublicPortfolioWithAllFields() {
      portfolioFlows.createPublicPortfolioWithAllFields();
        ProExecutionResultWriter.write("ProE2E", "createPublicPortfolioWithAllFields", executionData);
        portfolioFlows.deletePortfolioWithoutProduct();
   }
//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void addMultipleYearFinancials() {portfolioFlows.addMultipleYearFinancials();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void validateGloballyConfiguredCustomFields() {
//        portfolioFlows.validateGloballyConfiguredCustomFields();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void editExistingPortfolio() {
//        portfolioFlows.editExistingPortfolio();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void editWorkflowTemplate() {
//        portfolioFlows.editWorkflowTemplate();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void validateMissingMandatoryFieldsDuringCreation() {
//        portfolioFlows.validateMissingMandatoryFields();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void validateMissingOwnerAndWorkflowFields() {
//        portfolioFlows.validateMissingOwnerAndWorkflowFields();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void validateInvalidLogoFormat() {
//        portfolioFlows.validateInvalidLogoFormat();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void deletePortfolioWithoutProduct() {
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void validateMyProductPortfolios() {
//        portfolioFlows.validateMyProductPortfolios();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void validateAllProductPortfolios() {
//        portfolioFlows.validateAllProductPortfolios();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void validateAuditHistorySearchAndFilters() {
//        portfolioFlows.validateAuditHistorySearchAndFilters();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }

//    @Test(groups = {Constants.PRO_REGRESSION})
//    public void downloadAuditHistoryInPdfAndCsvFormats() {
//        portfolioFlows.downloadAuditHistoryInPdfAndCsvFormats();
//        portfolioFlows.deletePortfolioWithoutProduct();
//    }
    @Step("Record execution results")
    private void completeAndRecordResults() {
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] Recording execution results...");
        ProExecutionResultWriter.write("ProE2E", "Pro E2E With Mandatory Fields", executionData);
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] ✓ Results recorded");
    }
}
