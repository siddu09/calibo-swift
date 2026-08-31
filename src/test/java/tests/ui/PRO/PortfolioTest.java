package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.PRO.ProductPortfolio.NewProductPortfolioPagePage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsOverviewTabPage;
import pages.PRO.ProductPortfolio.ProductPortfolioViewPage;
import pages.PRO.ProductPortfolio.ProductPortfoliosPage;
import testdatamanager.pro.ProExecutionData;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.CommonMethods;
import utils.LoggerUtil;

public class PortfolioTest extends BaseUITest {

    private ProductPortfolioFlows portfolioFlows;

    @BeforeMethod
    public void setUp() {
        new LoginBuildingBlock(page).login();
        portfolioFlows = new ProductPortfolioFlows(page, new ProExecutionData());
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPortfolioWithMandatoryFields() {
        LandingPage landingPage = new LandingPage(page);
        landingPage.hoverOnNavigationBar().click();
        landingPage.clickOnOptions("Product Portfolios").click();
        page.waitForTimeout(2000);
//        Assert.assertTrue(landingPage.isLandedOnPage("Product Portfolios").isVisible());

        LoggerUtil.LOGGER.info("============================ Product Portfolio Creation started =========================");

        ProductPortfoliosPage productPortfoliosPage = new ProductPortfoliosPage(page);
        productPortfoliosPage.addNewProductPortfolio().click();
        Assert.assertTrue(CommonMethods.pageHeader(page, "New Product Portfolio"));

        NewProductPortfolioPagePage newProductPortfolioPagePage = new NewProductPortfolioPagePage(page);
        newProductPortfolioPagePage.name().fill("Automation E2E Product Portfolio");
        newProductPortfolioPagePage.description().fill("Automation E2E Product Portfolio");
        newProductPortfolioPagePage.create().click();
        page.waitForTimeout(2000);
        Assert.assertTrue(CommonMethods.pageHeader(page, "Additional Details"));

        ProductPortfolioAdditionalDetailsOverviewTabPage productPortfolioAdditionalDetailsOverviewTabPage = new ProductPortfolioAdditionalDetailsOverviewTabPage(page);
        productPortfolioAdditionalDetailsOverviewTabPage.save().click();
        page.waitForTimeout(2000);
        Assert.assertTrue(CommonMethods.tabHeader(page, "Details"));

        ProductPortfolioViewPage productPortfolioViewPage = new ProductPortfolioViewPage(page);
        CommonMethods.clickOnTab(page, "Products");
        productPortfolioViewPage.addProduct().click();

        LoggerUtil.LOGGER.info("============================ Product Portfolio Created =========================");
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPrivatePortfolioWithMandatoryFields() {
        portfolioFlows.createPrivatePortfolioWithMandatoryFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void cancelPortfolioCreation() {
        portfolioFlows.cancelPortfolioCreation();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void createPublicPortfolioWithAllFields() {
        portfolioFlows.createPublicPortfolioWithAllFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void addMultipleYearFinancials() {
        portfolioFlows.addMultipleYearFinancials();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateGloballyConfiguredCustomFields() {
        portfolioFlows.validateGloballyConfiguredCustomFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void editExistingPortfolio() {
        portfolioFlows.editExistingPortfolio();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void editWorkflowTemplate() {
        portfolioFlows.editWorkflowTemplate();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateMissingMandatoryFieldsDuringCreation() {
        portfolioFlows.validateMissingMandatoryFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateMissingOwnerAndWorkflowFields() {
        portfolioFlows.validateMissingOwnerAndWorkflowFields();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateInvalidLogoFormat() {
        portfolioFlows.validateInvalidLogoFormat();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void deletePortfolioWithoutProduct() {
        portfolioFlows.deletePortfolioWithoutProduct();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateMyProductPortfolios() {
        portfolioFlows.validateMyProductPortfolios();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateAllProductPortfolios() {
        portfolioFlows.validateAllProductPortfolios();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void validateAuditHistorySearchAndFilters() {
        portfolioFlows.validateAuditHistorySearchAndFilters();
    }

    @Test(groups = {Constants.PRO_REGRESSION})
    public void downloadAuditHistoryInPdfAndCsvFormats() {
        portfolioFlows.downloadAuditHistoryInPdfAndCsvFormats();
    }
}
