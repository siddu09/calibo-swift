package UI.PRO.ProductPortfolio.BuildingBlocks;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.LandingPage;
import pages.PRO.ProductPortfolio.NewProductPortfolioPagePage;
import pages.PRO.ProductPortfolio.ProductPortfolioViewPage;
import pages.PRO.ProductPortfolio.ProductPortfoliosPage;
import utils.CommonMethods;
import utils.LoggerUtil;
import UI.PRO.datahelper.PortfolioData;
import testdatamanager.pro.ProExecutionData;
import utils.OverlayHandler;

public class ProductPortfolioBuildingBlock {

    private final Page page;
    private final ProExecutionData executionData;

    private final LandingPage landingPage;
    private final ProductPortfoliosPage productPortfoliosPage;
    private final NewProductPortfolioPagePage newProductPortfolioPage;
    private final ProductPortfolioViewPage productPortfolioViewPage;
    private String portfolioName;

    public ProductPortfolioBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.landingPage = new LandingPage(page);
        this.productPortfoliosPage = new ProductPortfoliosPage(page);
        this.newProductPortfolioPage = new NewProductPortfolioPagePage(page);
        this.productPortfolioViewPage = new ProductPortfolioViewPage(page);
    }

    @Step("Navigate to Product Portfolio Page")
    public void navigateToProductPortfolioPage() {
        page.waitForTimeout(2000);
        LoggerUtil.LOGGER.info("========== Navigating to Product Portfolio Page ==========");

        landingPage.hoverOnNavigationBar().click();

        landingPage.clickOnOptions("Product Portfolios").click();
    }

    /**
     * delete if the new code works
     @Step("Create new product portfolio")
     public void createNewProductPortfolio(PortfolioData portfolioData) {
     LoggerUtil.LOGGER.info("========== Product Portfolio Creation Started ==========");

     productPortfoliosPage.addNewProductPortfolio().click();

     Assert.assertTrue(CommonMethods.pageHeader(page, "New Product Portfolio"));

     this.portfolioName =
     CommonMethods.generateUniqueTitle(portfolioData.getName());

     Allure.parameter("Portfolio Name", portfolioName);

     newProductPortfolioPage.name().fill(portfolioName);

     executionData.setPortfolioName(portfolioName);

     newProductPortfolioPage.description().fill(portfolioData.getDescription());

     newProductPortfolioPage.create().click();

     CommonMethods.waitForLoaderToDisappear(page);

     LoggerUtil.LOGGER.info("========== Product Portfolio Created ==========");
     }
     **/

    public void createNewProductPortfolio(PortfolioData portfolioData) {
        LoggerUtil.LOGGER.info("========== Product Portfolio Creation Started ==========");
        productPortfoliosPage.addNewProductPortfolio().click();
        Assert.assertTrue(CommonMethods.pageHeader(page, "New Product Portfolio"));

        this.portfolioName = CommonMethods.generateUniqueTitle(portfolioData.getName());
        newProductPortfolioPage.name().fill(portfolioName);
        executionData.setPortfolioName(portfolioName);
        newProductPortfolioPage.description().fill(portfolioData.getDescription());

        if (portfolioData.isPublicPortfolio()) {
            newProductPortfolioPage.enablePublicPortfolio();
        }

        newProductPortfolioPage.create().click();
        CommonMethods.waitForLoaderToDisappear(page);
        LoggerUtil.LOGGER.info("========== Product Portfolio Created ==========");
    }

    @Step("Save or skip portfolio additional details with action: {action}")
    /**
     * Handles the "Skip for now" action on the Additional Details page, and the
     * follow-up confirmation dialog ("Are you sure you want to skip / cancel?")
     * that appears afterward — confirmed by clicking "Yes".
     *
     * VERIFY the confirmation dialog button text against live DOM; "Yes" is
     * used here per the existing pattern in ProductBuildingBlock/DevSecOpsE2E.
     */


    // ======================================================================
    //  NEW (for RAG flow) - does NOT modify the method above.
    //  Overloaded variant that additionally closes the contextual Help panel
    //  (the '?' drawer) so it never intercepts the Skip/Yes clicks.
    // ======================================================================

    /**
     * Closes the contextual Help panel IF it is open, so it doesn't intercept
     * subsequent clicks. Best-effort and never throws.
     *
     * <p>Distinct from {@link OverlayHandler#neutralizeKnownOverlays(Page)} which
     * handles the JSD support widget (#jsd-widget). The Help panel close button is
     * stable: {@code <button data-testid="close" aria-label="Close">}.
     */
    public void closeHelpIfOpen() {
        try {
            Locator closeBtn = page.locator(
                    "button[data-testid='close'][aria-label='Close']");

            if (closeBtn.count() > 0 && closeBtn.first().isVisible()) {
                closeBtn.first().click();
                CommonMethods.waitForLoaderToDisappear(page);
                LoggerUtil.LOGGER.info("[HELP] Closed help panel via data-testid='close'");
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.info("[HELP] No help panel to close");
        }
    }

    public void saveOrSkipPortfolioAdditionalDetails(String action) {
        CommonMethods.clickButton(page, action).click();
        CommonMethods.waitForLoaderToDisappear(page);
        Assert.assertTrue(CommonMethods.tabHeader(page, "Details"));
    }



    /**
     * NEW method for the RAG flow. Same behaviour as
     * {@link #saveOrSkipPortfolioAdditionalDetails(String)} but:
     *  - closes the Help panel first (if open),
     *  - clicks {@code .first()} on the Yes confirmation to avoid strict-mode errors,
     *  - scopes the "Yes" click to the unsaved-changes dialog.
     *
     * The original method is left completely unchanged for existing callers.
     */
    @Step("Skip/Save portfolio additional details (RAG flow, handles Help + unsaved-changes dialog): {action}")
    public void saveOrSkipPortfolioAdditionalDetailsWithHelpClose(String action) {

        // 1) Close Help panel first if it's open (it can intercept clicks)
        closeHelpIfOpen();

        // 2) Click Skip for now / Save
        CommonMethods.clickButton(page, action).click();
        CommonMethods.waitForLoaderToDisappear(page);

        // 3) Handle the "You have unsaved changes" confirmation (Yes/No)
        if ("Skip for now".equalsIgnoreCase(action)) {
            try {
                Locator confirmYes = page.locator(
                        "//div[contains(.,'unsaved changes')]//button[normalize-space()='Yes']");

                if (confirmYes.count() > 0 && confirmYes.first().isVisible()) {
                    confirmYes.first().click();   // .first() avoids strict-mode error
                    CommonMethods.waitForLoaderToDisappear(page);

                    LoggerUtil.LOGGER.info(
                            "[OVERLAY] Widget Count = {}",
                            page.locator("#jsd-widget").count());

                    // Prevent JSD support widget from blocking future clicks
                    OverlayHandler.neutralizeKnownOverlays(page);

                    LoggerUtil.LOGGER.info(
                            "[PORTFOLIO-BLOCK] Confirmed 'unsaved changes' via Yes (RAG flow)");
                } else {
                    LoggerUtil.LOGGER.info(
                            "[PORTFOLIO-BLOCK] No unsaved-changes dialog appeared (RAG flow)");
                }
            } catch (Exception e) {
                LoggerUtil.LOGGER.info(
                        "[PORTFOLIO-BLOCK] No confirmation dialog appeared after Skip (RAG flow)");
            }
        }
    }




    @Step("Navigate to Products Tab")
    public void navigateToProductsTab() {
        CommonMethods.clickOnTab(page, "Products");
    }

    @Step("Add product to portfolio")
    public void addProductToPortfolio() {
        productPortfolioViewPage.addProduct().click();
    }

    @Step("Navigate to portfolio details page")
    public void navigateToPortfolioDetailsPage(){
        page.mouse().click(500, 300);
        CommonMethods.clickButton(page, this.portfolioName).click();
    }

    @Step("Search portfolio by name")
    public void searchPortfolio(){

        productPortfoliosPage.search().fill(this.portfolioName);
        CommonMethods.waitForLoaderToDisappear(page);
        page.mouse().move(500, 300);
    }

    @Step("Select portfolio from list")
    public void selectPortfolio()
    {
        productPortfoliosPage.selectPortfolio(this.portfolioName).click();
    }
}
