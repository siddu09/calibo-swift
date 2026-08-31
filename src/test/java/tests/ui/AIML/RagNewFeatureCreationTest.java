package tests.ui.AIML;

import assertionsHandler.AssertionManager;
import configHandler.ConfigManager;
import org.testng.annotations.Test;

import pages.LandingPage;
import pages.LoginPage;
import pages.PRO.ProductPortfolio.ProductPortfolioViewPage;
import tests.base.BaseUITest;
import utils.CommonMethods;
import utils.FeatureData;
import utils.LoggerUtil;
import UI.AIML.RAGPipeline.helperutils.NewFeatureCreationUtil;

/**
 * End-to-end test for feature creation via the refactored SWIFT components:
 * {@code NewFeatureCreationUtil} → {@code NewFeaturePage} / {@code FeatureDetailPage}
 * → {@code ResilientLocator} → Playwright.
 *
 * <p>Flow: login → navigate to a Product Portfolio → open a Product → create a feature
 * with {@link FeatureData} → verify the detail side-sheet → surface all soft assertions.
 *
 * <p>Style mirrors {@code tests.ui.DSO.E2EFlow} (login via {@code LoginPage}, navigation
 * via {@code CommonMethods}). Adjust the marked navigation steps to match the exact
 * portfolio/product you use in QA.
 */
public class RagNewFeatureCreationTest extends BaseUITest {

    private final String username   = ConfigManager.getUIProperty("user");
    private final String password   = ConfigManager.getUIProperty("pass");
    private final String tenantName = ConfigManager.getUIProperty("tenantName");

    @Test
    public void createAndVerifyFeature() {
        LoggerUtil.LOGGER.info("[TEST] Starting NewFeatureCreationTest");

        // 1) Login
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(username, password, tenantName);

        // 2) Navigate to a Product Portfolio  (adjust names to your QA data)
        LandingPage landingPage = new LandingPage(page);
        CommonMethods.waitForLoaderToDisappear(page);
        landingPage.hoverOnNavigationBar().click();
        landingPage.clickOnOptions("Product Portfolios").click();
        page.waitForTimeout(2000);

        CommonMethods.search(page, "Default Automation PL").fill("DefaultAutomationPL");
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.selectSearchedItem(page, "DefaultAutomationPL").click();
        CommonMethods.waitForLoaderToDisappear(page);

        // 3) Open the Products tab and pick a product to add a feature under
        ProductPortfolioViewPage portfolioView = new ProductPortfolioViewPage(page);
        CommonMethods.clickOnTab(page, "Products");
        CommonMethods.waitForLoaderToDisappear(page);
        // TODO: open the specific product where the "New Feature" button is available.
        //       e.g. portfolioView.openProduct("AIML AutomatePro");

        // 4) Build the feature data (Lombok builder)
        FeatureData data = FeatureData.builder()
                .name("Autotestfeature_" + System.currentTimeMillis())
                .description("Created by SWIFT automation")
                .phase("Develop")                 // Develop/Deploy handled as a linked pair
                .status("In QA")
                .owner("API Automation")
                .build();

        // 5) Create + verify (soft-asserts against the detail side-sheet)
        String featureName = NewFeatureCreationUtil.createAndVerifyFeature(page, data);
        LoggerUtil.LOGGER.info("[TEST] Created feature: {}", featureName);

        // 6) Surface all soft assertions
        AssertionManager.assertAll();

        LoggerUtil.LOGGER.info("[TEST] NewFeatureCreationTest completed");
    }
}
