package tests.ui;

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

public class LoginTest extends BaseUITest {
    String username = ConfigManager.getUIProperty("user");
    String password = ConfigManager.getUIProperty("pass");
    String tenantName = ConfigManager.getUIProperty("tenantName");

    @Test(dataProvider = "loginData", dataProviderClass = tests.dataProviders.LoginDataProvider.class)
    public void verifyLogin(String... string) {

        LoggerUtil.LOGGER.info("Executing login test");

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(username, password, tenantName);

        LandingPage landingPage = new LandingPage(page);
        CommonMethods.waitForLoaderToDisappear(page);
        landingPage.hoverOnNavigationBar().click();
        landingPage.clickOnOptions("Product Portfolios").click();
        page.waitForTimeout(2000);
//      Assert.assertTrue(landingPage.isLandedOnPage("Product Portfolios").isVisible());

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

        LoggerUtil.LOGGER.info("============================ Start Adding Product =========================");

        ProductPage productPage = new ProductPage(page);
        page.waitForTimeout(2000);
        productPage.clickOnGetStartedButton("Productize");
        CommonMethods.waitForLoaderToDisappear(page);
        Assert.assertTrue(CommonMethods.sectionHeader(page, "Create Product"));

        ProductAddProjectDetailsPage productAddProjectDetailsPage = new ProductAddProjectDetailsPage(page);
        productAddProjectDetailsPage.title().fill("Automation E2E Product");
        productAddProjectDetailsPage.checkCircleDefine().click();
        productAddProjectDetailsPage.checkCircleDesign().click();
        productAddProjectDetailsPage.checkCircleDevelop().click();
        productAddProjectDetailsPage.description().fill("Automation E2E Product");
        productAddProjectDetailsPage.selectBusinessGroup("BG_Automation");
        productAddProjectDetailsPage.create().click();
        CommonMethods.waitForLoaderToDisappear(page);
        ProductAdditionalDetailsOverviewTabPage productAdditionalDetailsOverviewTabPage = new ProductAdditionalDetailsOverviewTabPage(page);
        productAdditionalDetailsOverviewTabPage.save().click();

        LoggerUtil.LOGGER.info("============================ Product Added  =========================");

    }
}

