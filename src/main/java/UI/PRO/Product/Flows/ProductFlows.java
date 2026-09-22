package UI.PRO.Product.Flows;

import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Product.validations.ProductValidation;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import UI.PRO.datahelper.ProductData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;

public class ProductFlows {

    private final ProductBuildingBlock productBuildingBlock;
    private final ProExecutionData executionData;
    private final Page page;
    private final ProductPortfolioFlows productPortfolioFlows;

    public ProductFlows(Page page, ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        productBuildingBlock = new ProductBuildingBlock(page, executionData);
        productPortfolioFlows = new ProductPortfolioFlows(page, executionData);
    }

    @Step("Create private product from Products with mandatory fields")
    public void createPrivateProductWithMandatoryFields() {
        ProductData productData = ProTestData.getProduct(ProTestData.CREATE_PRODUCT);
        createProductFromProducts(productData);
        productBuildingBlock.saveOrSkipProductAdditionalDetails(productData.getSkipButton());
        productBuildingBlock.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, productData);
        utils.LoggerUtil.LOGGER.info("[PRODUCT-FLOW] Mandatory product details validated successfully");
    }
    @Step("Create public product with all fields")
    public void createPublicProductWithAllFields() {
        ProductData productData = ProTestData.getProduct("createPublicProduct");
        createProductFromProducts(productData);
        productBuildingBlock.completeProductOverview(productData);
        productBuildingBlock.completeProductCustomFields(productData);
        productBuildingBlock.validateProductMilestones(productData);
        productBuildingBlock.saveOrSkipProductAdditionalDetails(productData.getSaveButton());
        productBuildingBlock.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, productData);
        utils.LoggerUtil.LOGGER.info("[PRODUCT-FLOW] Public product details validated successfully");
    }


    private void createProductFromProducts(ProductData productData) {
        productBuildingBlock.openNewProductFromProducts();
        productBuildingBlock.selectProductType(productData.getProductType());
        productBuildingBlock.selectOrCreateProductPortfolio();
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, productData.getProductCreatedMessage());
    }

    @Step("Create product with mandatory fields")
    public void createProductWithMandatoryFields() {

        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();

        ProductData productData = ProTestData.getProduct("createProduct");

        productBuildingBlock.selectProductType("Productize");

        productBuildingBlock.createNewProduct(productData);

        ProValidation.validateSuccessMessage(page, "Product created successfully.");

        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");


        productBuildingBlock.chooseFeatureCreationOption();

        ProductValidation.validateProductDetails(page, executionData, productData);


    }

    @Step("Delete product and validate success message")
    public void deleteProduct() {
        productBuildingBlock.deleteProduct();
        ProValidation.validateSuccessMessage(page, "Product deleted successfully.");
    }

    public void navigateToFeatureTab() {
        productBuildingBlock.navigateToFeatureTab();
    }

    public void addDependencyToProduct() {
        productBuildingBlock.navigateToDependencyTab();

        productBuildingBlock.addDependency(executionData.getPortfolioName(), executionData.getProducts().get(0).getProductName());

    }

    public void createProductAndFeatureInline() {
        ProductData productData = ProTestData.getProduct("createProduct");
        productBuildingBlock.selectProductType("Productize");
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");
        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");
        productBuildingBlock.chooseFeatureCreationOption("Yes");   // <-- Yes, not No
        ProductValidation.validateProductDetails(page, executionData, productData);
    }

    public void createProductAndFeatureInline(ProductData productData) {
        productBuildingBlock.selectProductType("Productize");
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");

        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");
        productBuildingBlock.chooseFeatureCreationOption("Yes");

    }
}
