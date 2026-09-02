package UI.PRO.ProductPortfolio.BuildingBlocks;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.PRO.ProductPortfolio.NewProductPortfolioPagePage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsCustomFieldsTabPage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsFinancialsTabPage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsOthersTabPage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsOverviewTabPage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsProductApprovalWFTabPage;
import pages.PRO.ProductPortfolio.ProductPortfolioViewPage;
import pages.PRO.ProductPortfolio.ProductPortfoliosPage;
import utils.CommonMethods;
import utils.LoggerUtil;
import UI.PRO.datahelper.PortfolioData;
import testdatamanager.pro.ProExecutionData;
import utils.OverlayHandler;

import java.nio.file.Path;

public class ProductPortfolioBuildingBlock {

    private final Page page;
    private final ProExecutionData executionData;

    private final ProductPortfoliosPage productPortfoliosPage;
    private final NewProductPortfolioPagePage newProductPortfolioPage;
    private final ProductPortfolioViewPage productPortfolioViewPage;
    private final ProductPortfolioAdditionalDetailsOverviewTabPage overviewTabPage;
    private final ProductPortfolioAdditionalDetailsCustomFieldsTabPage customFieldsTabPage;
    private final ProductPortfolioAdditionalDetailsFinancialsTabPage financialsTabPage;
    private final ProductPortfolioAdditionalDetailsProductApprovalWFTabPage workflowTabPage;
    private final ProductPortfolioAdditionalDetailsOthersTabPage othersTabPage;
    private String portfolioName;

    public ProductPortfolioBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.productPortfoliosPage = new ProductPortfoliosPage(page);
        this.newProductPortfolioPage = new NewProductPortfolioPagePage(page);
        this.productPortfolioViewPage = new ProductPortfolioViewPage(page);
        this.overviewTabPage = new ProductPortfolioAdditionalDetailsOverviewTabPage(page);
        this.customFieldsTabPage = new ProductPortfolioAdditionalDetailsCustomFieldsTabPage(page);
        this.financialsTabPage = new ProductPortfolioAdditionalDetailsFinancialsTabPage(page);
        this.workflowTabPage = new ProductPortfolioAdditionalDetailsProductApprovalWFTabPage(page);
        this.othersTabPage = new ProductPortfolioAdditionalDetailsOthersTabPage(page);
        this.portfolioName = executionData.getPortfolioName();
    }

    @Step("Navigate to Product Portfolio Page")
    public void navigateToProductPortfolioPage() {
        page.waitForTimeout(2000);
        LoggerUtil.LOGGER.info("========== Navigating to Product Portfolio Page ==========");

        productPortfoliosPage.portfoliosA().click();
        page.waitForURL("**/portfolios");
        CommonMethods.waitForLoaderToDisappear(page);
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

        OverlayHandler.neutralizeKnownOverlays(page);
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

    @Step("Open new product portfolio form")
    public void openNewPortfolioForm() {
        productPortfoliosPage.addNewProductPortfolio().click();
        Assert.assertTrue(CommonMethods.pageHeader(page, "New Product Portfolio"));
    }

    @Step("Cancel product portfolio creation")
    public void cancelPortfolioCreation() {
        newProductPortfolioPage.cancel().click();
        Assert.assertTrue(productPortfoliosPage.myProductPortfolios().isVisible());
    }

    @Step("Validate mandatory portfolio fields")
    public void validateMandatoryFieldErrors() {
        newProductPortfolioPage.create().click();
        Assert.assertTrue(newProductPortfolioPage.nameRequiredValidation().isVisible());
        Assert.assertTrue(newProductPortfolioPage.descriptionRequiredValidation().isVisible());
    }

    @Step("Complete portfolio overview details")
    public void completeOverviewDetails(PortfolioData portfolioData) {
        overviewTabPage.businessOutcome().fill(portfolioData.getBusinessOutcome());
        overviewTabPage.priority().click();
        overviewTabPage.priorityOption(portfolioData.getPriority()).click();
        overviewTabPage.owners().fill(portfolioData.getOwner());
        overviewTabPage.ownerOption(portfolioData.getOwner()).click();
    }

    @Step("Add local portfolio custom field")
    public void addCustomField(PortfolioData portfolioData) {
        overviewTabPage.customFields().click();
        customFieldsTabPage.configuredCustomField(portfolioData.getCustomFieldName()).click();
        customFieldsTabPage.configuredCustomFieldOption(portfolioData.getCustomFieldValue()).click();
    }

    @Step("Add current-year portfolio financials")
    public void addCurrentYearFinancials(PortfolioData portfolioData) {
        overviewTabPage.financials().click();
        financialsTabPage.addCurrentYear().click();
        financialsTabPage.setApprovedBudget().fill(portfolioData.getApprovedBudget());
        financialsTabPage.setRevenueTarget().fill(portfolioData.getRevenueTarget());
    }

    @Step("Add another portfolio financial year")
    public void addAnotherFinancialYear() {
        financialsTabPage.addYear().click();
        Assert.assertTrue(financialsTabPage.financialYears().count() > 1);
    }

    @Step("Configure portfolio approval workflow")
    public void configureApprovalWorkflow() {
        overviewTabPage.productApprovalWorkflow().click();
        workflowTabPage.enableWF().check();
        workflowTabPage.workflowTemplateDropdown().click();
        workflowTabPage.workflowTemplateOptions().first().click();
        workflowTabPage.addWFTemplate().click();
    }

    @Step("Complete other portfolio details")
    public void completeOtherDetails(PortfolioData portfolioData) {
        overviewTabPage.others().click();
        othersTabPage.portfolioValue().fill(portfolioData.getPortfolioValue());
        othersTabPage.strategy().fill(portfolioData.getStrategy());
        othersTabPage.portfolioLogoFileInput().setInputFiles(Path.of(portfolioData.getValidLogoPath()));
        Assert.assertTrue(othersTabPage.logoPreview().isVisible());
    }

    @Step("Save portfolio additional details")
    public void savePortfolioAdditionalDetails() {
        othersTabPage.saveButton().click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Validate configured global portfolio custom fields")
    public void validateConfiguredGlobalCustomFields() {
        overviewTabPage.customFields().click();
        Assert.assertTrue(customFieldsTabPage.configuredCustomFields().count() > 0,
                "No globally configured portfolio custom fields are visible");
    }

    @Step("Edit existing portfolio")
    public void editExistingPortfolio(PortfolioData portfolioData) {
        productPortfolioViewPage.moreHoriz().click();
        productPortfolioViewPage.editPortfolio().click();
        String updatedName = CommonMethods.generateUniqueTitle(portfolioData.getName() + " Edited");
        String updatedDescription = portfolioData.getDescription() + " Edited";
        newProductPortfolioPage.name().fill(updatedName);
        newProductPortfolioPage.description().fill(updatedDescription);
        othersTabPage.saveButton().click();
        CommonMethods.waitForLoaderToDisappear(page);
        Assert.assertEquals(productPortfolioViewPage.fetchPortfolioName().textContent(), updatedName);
        Assert.assertEquals(productPortfolioViewPage.fetchPortfolioDescription().textContent(), updatedDescription);
        executionData.setPortfolioName(updatedName);
    }

    @Step("Edit portfolio workflow template")
    public void editWorkflowTemplate() {
        productPortfolioViewPage.moreHoriz().click();
        productPortfolioViewPage.editPortfolio().click();
        overviewTabPage.productApprovalWorkflow().click();
        workflowTabPage.editWorkflowTemplate().click();
        workflowTabPage.createWorkflowTemplateFromExisting().click();
    }

    @Step("Validate missing owner and workflow fields")
    public void validateMissingOwnerAndWorkflowFields() {
        overviewTabPage.clearOwners().click();
        CommonMethods.clickButton(page, "Save").click();
        Assert.assertTrue(overviewTabPage.ownerRequiredValidation().isVisible());
        overviewTabPage.productApprovalWorkflow().click();
        workflowTabPage.enableWF().check();
        CommonMethods.clickButton(page, "Save").click();
        Assert.assertTrue(workflowTabPage.workflowRequiredValidation().isVisible());
    }

    @Step("Validate invalid portfolio logo")
    public void validateInvalidLogo(PortfolioData portfolioData) {
        overviewTabPage.others().click();
        othersTabPage.portfolioLogoFileInput().setInputFiles(Path.of(portfolioData.getInvalidLogoPath()));
        Assert.assertTrue(page.getByText("JPEG & PNG", new Page.GetByTextOptions().setExact(false)).isVisible());
    }

    @Step("Delete product portfolio")
    public void deletePortfolio() {
        productPortfolioViewPage.moreHoriz().click();
        productPortfolioViewPage.deletePortfolio().click();
        productPortfolioViewPage.deletePortfolioReason()
                .fill("Delete the portfolio for automated regression validation");
        productPortfolioViewPage.confirmDeletePortfolio().click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Validate deleted product portfolio is absent")
    public void validatePortfolioDeleted() {
        productPortfoliosPage.search().fill(portfolioName);
        CommonMethods.waitForLoaderToDisappear(page);
        Assert.assertEquals(page.getByText(
                        portfolioName,
                        new Page.GetByTextOptions().setExact(true)
                ).count(), 0,
                "Deleted portfolio is still visible");
    }

    @Step("Validate portfolio list tab: {tabName}")
    public void validatePortfolioListTab(String tabName) {
        Locator tab = "My Product Portfolios".equals(tabName)
                ? productPortfoliosPage.myProductPortfolios()
                : productPortfoliosPage.allProductPortfolios();
        tab.click();
        Assert.assertTrue(productPortfoliosPage.activePortfolioTab(tabName).isVisible());
        Assert.assertTrue(productPortfoliosPage.portfolioCards().count() > 0);
    }

    @Step("Validate portfolio audit-history search and filters")
    public void validateAuditHistorySearchAndFilters() {
        openAuditHistory();
        productPortfolioViewPage.auditSearch().fill("Portfolio");
        productPortfolioViewPage.auditEventsFilter().click();
        page.getByRole(AriaRole.OPTION).first().click();
        productPortfolioViewPage.auditObjectsFilter().click();
        page.getByRole(AriaRole.OPTION).first().click();
        productPortfolioViewPage.auditInitiatedByFilter().click();
        page.getByRole(AriaRole.OPTION).first().click();
        productPortfolioViewPage.auditMoreFilters().click();
        productPortfolioViewPage.resetAuditFilters().click();
    }

    @Step("Open portfolio audit history")
    public void openPortfolioAuditHistory() {
        openAuditHistory();
    }

    @Step("Download portfolio audit history as {format}")
    public void downloadAuditHistory(String format) {
        productPortfolioViewPage.downloadAuditHistory().click();
        Download download = page.waitForDownload(
                () -> productPortfolioViewPage.auditDownloadFormat(format).click());
        Assert.assertTrue(download.suggestedFilename().toLowerCase()
                .endsWith("." + format.toLowerCase()));
    }

    private void openAuditHistory() {
        productPortfolioViewPage.moreHoriz().click();
        productPortfolioViewPage.auditHistory().click();
    }
}
