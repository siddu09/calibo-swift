package UI.PRO.ProductPortfolio.Flows;

import UI.PRO.ProductPortfolio.BuildingBlocks.ProductPortfolioBuildingBlock;

import UI.PRO.ProductPortfolio.validations.PortfolioValidation;
import UI.PRO.datahelper.PortfolioData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;

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

    /**
     * Overload accepting explicit PortfolioData (e.g. from RagPipelineTestData)
     * instead of loading via ProTestData. Existing no-arg
     * createPortfolioWithMandatoryFields() is untouched.
     *
     * <p>This overload drives the RAG pipeline flow, so it uses the help-aware
     * skip method ({@code saveOrSkipPortfolioAdditionalDetailsWithHelpClose}) which
     * closes the contextual Help panel before Skip and confirms the
     * "unsaved changes" dialog. The shared no-arg method above is NOT affected.
     */
    public void createPortfolioWithMandatoryFields(PortfolioData portfolioData) {
        productPortfolioBuildingBlock.navigateToProductPortfolioPage();
        productPortfolioBuildingBlock.createNewProductPortfolio(portfolioData);
        productPortfolioBuildingBlock
                .saveOrSkipPortfolioAdditionalDetailsWithHelpClose("Skip for now");
    }
}
