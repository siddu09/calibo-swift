package tests.ui.PRO;

import configHandler.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.LoginPage;
import pages.PRO.Product.ProductAddProjectDetailsPage;
import pages.PRO.Product.ProductAdditionalDetailsOverviewTabPage;
import pages.PRO.Product.ProductPage;
import pages.PRO.ProductPortfolio.NewProductPortfolioPagePage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsOverviewTabPage;
import pages.PRO.ProductPortfolio.ProductPortfolioViewPage;
import pages.PRO.ProductPortfolio.ProductPortfoliosPage;
import tests.base.BaseUITest;
import utils.CommonMethods;
import utils.LoggerUtil;

public class PortfolioTest extends BaseUITest {
    String username = ConfigManager.getUIProperty("user");
    String password = ConfigManager.getUIProperty("pass");
    String tenantName = ConfigManager.getUIProperty("tenantName");

    @Test
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
}

