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

import java.util.List;

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

    @Step("Create private product with mandatory fields")
    public void createPrivateProductWithMandatoryFields() {
        createProductWithMandatoryFields();
    }

    @Step("Create public Product and validate all Additional Details sections")
    public void createPublicProductWithAllFields() {
        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();

        ProductData productData = ProTestData.getProduct("createProduct");
        productBuildingBlock.selectProductType("Productize");
        productBuildingBlock.createNewProduct(productData, true);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");
        productBuildingBlock.setPriority("Medium");
        productBuildingBlock.validateAdditionalDetailsTabs();
        productBuildingBlock.saveOrSkipProductAdditionalDetails("Save");
        productBuildingBlock.chooseFeatureCreationOption();
        productBuildingBlock.navigateToProductPage();
        productBuildingBlock.validateAllProducts();
    }

    @Step("Validate globally configured Product custom fields section")
    public void validateGloballyConfiguredProductCustomFields() {
        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();

        ProductData productData = ProTestData.getProduct("createProduct");
        productBuildingBlock.selectProductType("Productize");
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");
        productBuildingBlock.validateAdditionalDetailsTabs();
        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");
        productBuildingBlock.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, productData);
    }

    @Step("Create product with Design phase")
    public void createProductWithDesignPhase() {
        createProductWithPhases(List.of("Design"), true);
    }

    @Step("Create product with Define and Design phases")
    public void createProductWithDefineAndDesignPhases() {
        createProductWithPhases(List.of("Define", "Design"), true);
    }

    @Step("Create product with Develop and Deploy phases")
    public void createProductWithDevelopAndDeployPhases() {
        createProductWithPhases(List.of("Develop", "Deploy"), false);
    }

    @Step("Cancel product creation")
    public void cancelProductCreation() {
        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();
        productBuildingBlock.selectProductType("Productize");
        productBuildingBlock.cancelProductCreation();
    }

    @Step("Create Operationalize product with mandatory fields")
    public void createOperationalizeProductWithMandatoryFields() {
        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();

        ProductData productData = ProTestData.getProduct("createProduct");
        productData.setPhases(List.of());

        productBuildingBlock.selectProductType("Operationalize");
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");
        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");
        ProductValidation.validateProductDetails(page, executionData, productData);
    }

    @Step("Create Operationalize product and validate all Additional Details sections")
    public void createOperationalizeProductWithAllFields() {
        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();

        ProductData productData = ProTestData.getProduct("createProduct");
        productData.setPhases(List.of());

        productBuildingBlock.selectProductType("Operationalize");
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");
        productBuildingBlock.validateAdditionalDetailsTabs();
        productBuildingBlock.saveOrSkipProductAdditionalDetails("Save");
        productBuildingBlock.navigateToProductPage();
        productBuildingBlock.validateMyProducts();
    }

    @Step("Validate My Products")
    public void validateMyProducts() {
        createProductWithMandatoryFields();
        productBuildingBlock.navigateToProductPage();
        productBuildingBlock.validateMyProducts();
    }

    @Step("Validate All Products")
    public void validateAllProducts() {
        createProductWithMandatoryFields();
        productBuildingBlock.navigateToProductPage();
        productBuildingBlock.validateAllProducts();
    }

    @Step("Validate Product audit history")
    public void validateProductAuditHistory() {
        createProductWithMandatoryFields();
        productBuildingBlock.openProductAuditHistory();
        productBuildingBlock.validateProductAuditHistory();
    }

    @Step("Search Product audit logs")
    public void searchProductAuditLogs() {
        createProductWithMandatoryFields();
        productBuildingBlock.openProductAuditHistory();
        productBuildingBlock.searchProductAuditHistory();
    }

    @Step("Filter Product audit history by events")
    public void filterProductAuditHistoryByEvents() {
        createProductWithMandatoryFields();
        productBuildingBlock.openProductAuditHistory();
        productBuildingBlock.filterProductAuditHistoryByEvents();
    }

    @Step("Filter Product audit history by objects")
    public void filterProductAuditHistoryByObjects() {
        createProductWithMandatoryFields();
        productBuildingBlock.openProductAuditHistory();
        productBuildingBlock.filterProductAuditHistoryByObjects();
    }

    @Step("Download Product audit history as PDF")
    public void downloadProductAuditHistoryAsPdf() {
        createProductWithMandatoryFields();
        productBuildingBlock.openProductAuditHistory();
        productBuildingBlock.downloadProductAuditHistory("PDF");
    }

    @Step("Download Product audit history as CSV")
    public void downloadProductAuditHistoryAsCsv() {
        createProductWithMandatoryFields();
        productBuildingBlock.openProductAuditHistory();
        productBuildingBlock.downloadProductAuditHistory("CSV");
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
        // "Yes" navigates permanently forward into Feature creation via
        // /projects/work-stream-consent — Product Details page (with the
        // "Product title" element) is never shown again in this path, so
        // product-detail validation cannot happen here. If product-level
        // validation is needed, it must be done via a different mechanism
        // (e.g., API call to fetch the created product) — not this UI page.
    }

    private void createProductWithPhases(
            List<String> phases,
            boolean validateProductDetails) {
        productPortfolioFlows.navigateToProductsTab();
        productPortfolioFlows.addProductToPortfolio();

        ProductData productData = ProTestData.getProduct("createProduct");
        productData.setPhases(phases);

        productBuildingBlock.selectProductType("Productize");
        productBuildingBlock.createNewProduct(productData);
        ProValidation.validateSuccessMessage(page, "Product created successfully.");
        productBuildingBlock.saveOrSkipProductAdditionalDetails("Skip for now");
        productBuildingBlock.chooseFeatureCreationOption();
        if (validateProductDetails) {
            ProductValidation.validateProductDetails(page, executionData, productData);
        }
    }
}
