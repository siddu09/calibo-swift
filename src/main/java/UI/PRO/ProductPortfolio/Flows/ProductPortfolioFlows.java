package UI.PRO.ProductPortfolio.Flows;

import UI.PRO.ProductPortfolio.BuildingBlocks.ProductPortfolioBuildingBlock;

import UI.PRO.ProductPortfolio.validations.PortfolioValidation;
import UI.PRO.datahelper.PortfolioData;
import UI.PRO.CommonProValidations.ProValidation;
import UI.PRO.utils.PortfolioExecutionDataReader;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import utils.LoggerUtil;
import org.testng.Assert;


public class ProductPortfolioFlows {

    private final ProductPortfolioBuildingBlock productPortfolioBuildingBlock;
    private final ProExecutionData executionData;
    private final Page page;

    public ProductPortfolioFlows(Page page, ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        productPortfolioBuildingBlock = new ProductPortfolioBuildingBlock(page, executionData);
    }

    @Step("Create portfolio with mandatory fields")
    public void createPortfolioWithMandatoryFields() {

        PortfolioData portfolioData = ProTestData.getPortfolio("createPortfolio");

        productPortfolioBuildingBlock.navigateToProductPortfolioPage();

        productPortfolioBuildingBlock.createNewProductPortfolio(portfolioData);

        ProValidation.validateSuccessMessage(page, "Product Portfolio created successfully.");

        productPortfolioBuildingBlock.saveOrSkipPortfolioAdditionalDetails("Skip for now");

        PortfolioValidation.validatePortfolioDetails(page, executionData, portfolioData);


    }

    public void navigateToProductsTab() {

        productPortfolioBuildingBlock.navigateToProductsTab();
    }

    public void addProductToPortfolio() {

        productPortfolioBuildingBlock.addProductToPortfolio();
    }

    public void openExistingPortfolio() {

        productPortfolioBuildingBlock.navigateToProductPortfolioPage();

        productPortfolioBuildingBlock.searchPortfolio();

        productPortfolioBuildingBlock.selectPortfolio();
    }

    @Step("Create portfolio with mandatory fields")
    public void createPortfolioWithMandatoryFields(PortfolioData portfolioData) {
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.createNewProductPortfolio(portfolioData);
        productPortfolioBuildingBlock
                .saveOrSkipPortfolioAdditionalDetailsWithHelpClose("Skip for now");
    }

    @Step("Cancel portfolio creation")
    public void cancelPortfolioCreation() {
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.openNewPortfolioForm();
        productPortfolioBuildingBlock.cancelPortfolioCreation();
    }

    @Step("Create public portfolio with all fields")
    public void createPublicPortfolioWithAllFields() {
        PortfolioData portfolioData = ProTestData.getPortfolio("createPublicPortfolio");
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.createNewProductPortfolio(portfolioData);
        ProValidation.validateSuccessMessage(page, "Product Portfolio created successfully.");
        productPortfolioBuildingBlock.completeOverviewDetails(portfolioData);
        productPortfolioBuildingBlock.addCustomField(portfolioData);
        productPortfolioBuildingBlock.addCurrentYearFinancials(portfolioData);
        productPortfolioBuildingBlock.configureApprovalWorkflow();
        productPortfolioBuildingBlock.completeOtherDetails(portfolioData);
        productPortfolioBuildingBlock.savePortfolioAdditionalDetails();
        PortfolioValidation.validatePortfolioDetails(page, executionData, portfolioData);

    }

    @Step("Add multiple-year portfolio financials")
    public void addMultipleYearFinancials() {
        PortfolioData portfolioData = ProTestData.getPortfolio("createPublicPortfolio");
        createPortfolioForAdditionalDetails(portfolioData);
        productPortfolioBuildingBlock.addCurrentYearFinancials(portfolioData);
        PortfolioData additionalYear = ProTestData.getPortfolio("addAnotherFinancialYear");
        productPortfolioBuildingBlock.addAnotherFinancialYear(additionalYear);
        productPortfolioBuildingBlock.savePortfolioAdditionalDetails();
        ProValidation.validateSuccessMessage(page, additionalYear.getDetailsSavedMessage());
    }

    @Step("Validate globally configured portfolio custom fields")
    public void validateGloballyConfiguredCustomFields() {
        PortfolioData portfolioData = ProTestData.getPortfolio("createPortfolio");
        createPortfolioForAdditionalDetails(portfolioData);
        productPortfolioBuildingBlock.validateConfiguredGlobalCustomFields();
    }

    @Step("Edit existing portfolio")
    public void editExistingPortfolio() {
        PortfolioData portfolioData = ProTestData.getPortfolio("preparePortfolioForEditing");
        createPortfolioForAdditionalDetails(portfolioData);
        productPortfolioBuildingBlock.preparePortfolioForEditing(portfolioData);
        PortfolioValidation.validatePortfolioDetails(page, executionData, portfolioData);
        productPortfolioBuildingBlock.editExistingPortfolio(ProTestData.getPortfolio("editExistingPortfolio"));
    }

    @Step("Edit portfolio workflow template")
    public void editWorkflowTemplate() {
        createPublicPortfolioWithAllFields();
        productPortfolioBuildingBlock.editWorkflowTemplate();
    }

    @Step("Validate missing mandatory portfolio fields")
    public void validateMissingMandatoryFields() {
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.openNewPortfolioForm();
        productPortfolioBuildingBlock.validateMandatoryFieldErrors();
    }

    @Step("Validate missing portfolio owner and workflow fields")
    public void validateMissingOwnerAndWorkflowFields() {
        PortfolioData portfolioData = ProTestData.getPortfolio("createPortfolio");
        createPortfolioForAdditionalDetails(portfolioData);
        productPortfolioBuildingBlock.validateMissingOwnerAndWorkflowFields();
    }

    @Step("Validate invalid portfolio logo format")
    public void validateInvalidLogoFormat() {
        PortfolioData portfolioData = ProTestData.getPortfolio("invalidPortfolioLogo");
        createPortfolioForAdditionalDetails(portfolioData);
        productPortfolioBuildingBlock.validateInvalidLogo(portfolioData);
    }

    @Step("Delete portfolio without a product")
    public void deletePortfolioWithoutProduct(String sheet, String testCase) {
        PortfolioData portfolioData = PortfolioExecutionDataReader.readPortfolioForDeletion(sheet, testCase);
        deletePortfolioWithoutProduct(portfolioData);
    }

    @Step("Delete current portfolio without a product")
    public void deletePortfolioWithoutProduct() {
        Assert.assertNotNull(executionData.getPortfolioName(), "Current portfolio name is missing");
        Assert.assertFalse(executionData.getPortfolioName().isBlank(), "Current portfolio name is empty");
        Assert.assertNotNull(executionData.getPublicPortfolio(), "Current portfolio visibility is missing");
        PortfolioData portfolioData = new PortfolioData();
        portfolioData.setName(executionData.getPortfolioName());
        portfolioData.setPublicPortfolio(executionData.getPublicPortfolio());
        deletePortfolioWithoutProduct(portfolioData);
    }

    private void deletePortfolioWithoutProduct(PortfolioData portfolioData) {
        if (portfolioData.isPublicPortfolio()) {
            validateAllProductPortfolios();
        } else {
            validateMyProductPortfolios();
        }
        productPortfolioBuildingBlock.searchPortfolio(portfolioData.getName());
        productPortfolioBuildingBlock.selectPortfolio();
        productPortfolioBuildingBlock.deletePortfolio();
        ProValidation.validateSuccessMessage(page, "Product Portfolio deleted successfully.");
        productPortfolioBuildingBlock.validatePortfolioDeleted();
        LoggerUtil.LOGGER.info("[Potlollo test] ✓ delete the portfollo ");
    }

    @Step("Validate My Product Portfolios")
    public void validateMyProductPortfolios() {
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.validatePortfolioListTab("My Product Portfolios");
    }

    @Step("Validate All Product Portfolios")
    public void validateAllProductPortfolios() {
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.validatePortfolioListTab("All Product Portfolios");
    }

    @Step("Validate portfolio audit-history search and filters")
    public void validateAuditHistorySearchAndFilters() {
        PortfolioData portfolioData = ProTestData.getPortfolio("auditHistory");
        createPortfolioWithMandatoryFields(portfolioData);
        productPortfolioBuildingBlock.editPortfolioCustomFields(portfolioData);
        productPortfolioBuildingBlock.validateAuditHistorySearchAndFilters(portfolioData);
    }

    @Step("Download portfolio audit history in PDF and CSV formats")
    public void downloadAuditHistoryInPdfAndCsvFormats() {
        PortfolioData portfolioData = ProTestData.getPortfolio("auditReport");
        createPortfolioWithMandatoryFields(portfolioData);
        productPortfolioBuildingBlock.openPortfolioAuditHistory();
        productPortfolioBuildingBlock.downloadAuditHistory(portfolioData);
    }

    private void createPortfolioForAdditionalDetails(PortfolioData portfolioData) {
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.createNewProductPortfolio(portfolioData);
        ProValidation.validateSuccessMessage(page, "Product Portfolio created successfully.");
    }
}
