package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
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
        portfolioFlows.deletePortfolioWithoutProduct();
    }
    @Test(groups = {Constants.PRO_REGRESSION})
    public void cancelPortfolioCreation() {
        portfolioFlows.cancelPortfolioCreation();
        portfolioFlows.deletePortfolioWithoutProduct();
   }
    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPublicPortfolioWithAllFields() {
      portfolioFlows.createPublicPortfolioWithAllFields();
      portfolioFlows.deletePortfolioWithoutProduct();
   }
    @Test(groups = {Constants.PRO_REGRESSION})
    public void addMultipleYearFinancials() {portfolioFlows.addMultipleYearFinancials();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateGloballyConfiguredCustomFields() {
        portfolioFlows.validateGloballyConfiguredCustomFields();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void editExistingPortfolio() {
        portfolioFlows.editExistingPortfolio();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void editWorkflowTemplate() {
        portfolioFlows.editWorkflowTemplate();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateMissingMandatoryFieldsDuringCreation() {
        portfolioFlows.validateMissingMandatoryFields();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateMissingOwnerAndWorkflowFields() {
        portfolioFlows.validateMissingOwnerAndWorkflowFields();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateInvalidLogoFormat() {
        portfolioFlows.validateInvalidLogoFormat();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void deletePortfolioWithoutProduct() {
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateMyProductPortfolios() {
        portfolioFlows.validateMyProductPortfolios();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateAllProductPortfolios() {
        portfolioFlows.validateAllProductPortfolios();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateAuditHistorySearchAndFilters() {
        portfolioFlows.validateAuditHistorySearchAndFilters();
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void downloadAuditHistoryInPdfAndCsvFormats() {
        portfolioFlows.downloadAuditHistoryInPdfAndCsvFormats();
        portfolioFlows.deletePortfolioWithoutProduct();
    }
}
