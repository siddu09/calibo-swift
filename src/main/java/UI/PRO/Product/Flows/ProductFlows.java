package UI.PRO.Product.Flows;

import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Product.validations.ProductValidation;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import UI.PRO.datahelper.ProductData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;
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
        createPrivateProductWithMandatoryFields(ProTestData.getProduct(ProTestData.CREATE_PRODUCT));
    }

    private void createPrivateProductWithMandatoryFields(ProductData productData) {
        createProductFromProducts(productData);
        productBuildingBlock.saveOrSkipProductAdditionalDetails(productData.getSkipButton());
        productBuildingBlock.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, productData);
        utils.LoggerUtil.LOGGER.info("[PRODUCT-FLOW] Mandatory product details validated successfully");
    }

    @Step("Create private product with Define phase and mandatory fields")
    public void createPrivateProductWithDefinePhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDefinePhase"));
    }

    @Step("Create private product with Design phase and mandatory fields")
    public void createPrivateProductWithDesignPhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDesignPhase"));
    }

    @Step("Create private product with Define and Design phases and mandatory fields")
    public void createPrivateProductWithDefineAndDesignPhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDefineAndDesignPhase"));
    }

    @Step("Create private product with Develop and Design phases and mandatory fields")
    public void createPrivateProductWithDevelopAndDesignPhase() {
        createPrivateProductWithMandatoryFields(ProTestData.getProduct("createPrivateProductWithDevelopAndDesignPhase"));
    }

    @Step("Create private products and verify dependencies in both directions")
    public void createPrivateProductAndAddDependencyBetweenProducts() {
        ProductData data = ProTestData.getProduct(ProTestData.PRODUCT_DEPENDENCY);
        ProTestData.saveDependencyProductNames("", "");
        createPrivateProductWithMandatoryFields(data);
        String product1Name = executionData.getProducts().get(executionData.getProducts().size() - 1).getProductName();
        ProTestData.saveDependencyProductNames(product1Name, "");
        createPrivateProductWithMandatoryFields(data);
        String product2Name = executionData.getProducts().get(executionData.getProducts().size() - 1).getProductName();
        ProTestData.saveDependencyProductNames(product1Name, product2Name);
        data = ProTestData.getProduct(ProTestData.PRODUCT_DEPENDENCY);
        Assert.assertNotEquals(data.getProduct1Name(), data.getProduct2Name(), data.getDistinctProductNamesMessage());
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.addDependency(executionData.getPortfolioName(), data.getProduct1Name());
        productBuildingBlock.openProduct(data.getProduct1Name(), data);
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.validateDependent(executionData.getPortfolioName(), data.getProduct2Name(), data);
        productBuildingBlock.validateDependencyNotification(data.getProduct2Name(), data, true);
        productBuildingBlock.addDependency(executionData.getPortfolioName(), data.getProduct2Name());
        productBuildingBlock.openProduct(data.getProduct2Name(), data);
        productBuildingBlock.navigateToDependencyTab();
        productBuildingBlock.validateDependent(executionData.getPortfolioName(), data.getProduct1Name(), data);
        productBuildingBlock.validateDependencyNotification(data.getProduct1Name(), data, true);
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
