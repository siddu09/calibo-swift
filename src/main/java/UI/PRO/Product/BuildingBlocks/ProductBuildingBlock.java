package UI.PRO.Product.BuildingBlocks;

import UI.PRO.datahelper.ProductData;
import com.microsoft.playwright.Locator;
import UI.PRO.datahelper.ProductData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.PRO.Product.ProductAddProjectDetailsPage;
import pages.PRO.Product.ProductDependencyPage;
import pages.PRO.Product.ProductPage;
import testdatamanager.pro.ProductExecutionData;
import testdatamanager.pro.ProExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;

import java.util.List;

public class ProductBuildingBlock {

    private final Page page;
    private final ProExecutionData executionData;

    private final ProductAddProjectDetailsPage productDetails;
    private final ProductPage productPage;

    public ProductBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.productDetails = new ProductAddProjectDetailsPage(page);
        this.productPage = new ProductPage(page);
    }

    @Step("Select product phases: {phases}")
    public void selectPhases(List<String> phases) {

        for (String phase : phases) {

            switch (phase) {

                case "Define" ->
                        productDetails.checkCircleDefine().click();

                case "Design" ->
                        productDetails.checkCircleDesign().click();

                case "Develop" ->
                        productDetails.checkCircleDevelop().click();

                case "Deploy" ->
                        LoggerUtil.LOGGER.info(
                                "Deploy is automatically selected after Develop. No action required."
                        );

                default ->
                        throw new IllegalArgumentException(
                                "Unsupported product phase: " + phase
                        );
            }
        }
    }

    @Step("Select product type: {productType}")
    public void selectProductType(String productType) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Product Creation Started ==========");

        productPage.clickOnGetStartedButton(productType);

        CommonMethods.waitForLoaderToDisappear(page);

        Assert.assertTrue(
                CommonMethods.sectionHeader(page, "Create Product"));
    }

    @Step("Create new product")
    public void createNewProduct(ProductData productData) {

        String productName =
                CommonMethods.generateUniqueTitle(productData.getTitle());
        Allure.parameter("Product name", productName);

        productDetails.title().fill(productName);

        selectPhases(productData.getPhases());

        productDetails.description().fill(productData.getDescription());

        productDetails.selectBusinessGroup(
                productData.getBusinessGroup());

        productDetails.create().click();

        LoggerUtil.LOGGER.info(
                "========== Product Created ==========");
        storeProductExecutionData(productName);
    }

    @Step("Save or skip product additional details with action: {action}")
    public void saveOrSkipProductAdditionalDetails(String action) {

        CommonMethods.waitForLoaderToDisappear(page);

        CommonMethods.clickButton(page, action).click();

        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);

//        if ("Skip for now".equalsIgnoreCase(action)) {
//            try {
//                Locator confirmYes = CommonMethods.clickButton(page, "Yes");
//                if (confirmYes.count() > 0 && confirmYes.first().isVisible()) {
//                    confirmYes.click();
//                    CommonMethods.waitForLoaderToDisappear(page);
//                    LoggerUtil.LOGGER.info(
//                            "[PRODUCT-BLOCK] Confirmed 'unsaved changes' dialog via 'Yes'");
//                }
//            } catch (Exception e) {
//                LoggerUtil.LOGGER.info(
//                        "[PRODUCT-BLOCK] No confirmation dialog appeared after Skip");
//            }
//        }
    }

    @Step("Choose feature creation option")
    public void chooseFeatureCreationOption() {
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);

        CommonMethods.clickButton(page, "No").click();
    }

    @Step("Navigate to Features Tab")
    public void chooseFeatureCreationOption(String choice) {
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(1000);
        LoggerUtil.LOGGER.info("[PRODUCT-BUILD] Attempting to choose feature-creation option: '{}'", choice);

        Locator button = null;
        if ("Yes".equalsIgnoreCase(choice)) {
            button = productPage.btnFeatureCreationYes();
        } else if ("No".equalsIgnoreCase(choice)) {
            button = productPage.btnFeatureCreationNo();
        } else if ("Skip for now".equalsIgnoreCase(choice)) {
            button = productPage.btnSkipForNow();
        } else if ("Maybe Later".equalsIgnoreCase(choice)) {
            button = productPage.btnMaybeLater();
        } else {
            // Fallback to generic button click for any other text
            button = CommonMethods.clickButton(page, choice);
        }

        // Robust click with retries + fallbacks
        boolean clicked = false;
        Locator nameInput = page.locator("input[data-cy='workstream-new-name'], input[placeholder='Name'], input[name='name']");
        for (int attempt = 1; attempt <= 3 && !clicked; attempt++) {
            try {
                if (!button.isVisible()) {
                    try { button.scrollIntoViewIfNeeded(); } catch (Exception ignored) {}
                }
                button.click();
                clicked = true;
            } catch (Exception e) {
                LoggerUtil.LOGGER.warn("[PRODUCT-BUILD] Click attempt #{} failed for '{}' (normal click): {}", attempt, choice, e.getMessage());
                try {
                    // try force click as fallback
                    button.click(new Locator.ClickOptions().setForce(true));
                    clicked = true;
                    LoggerUtil.LOGGER.info("[PRODUCT-BUILD] Force-click succeeded on attempt #{} for '{}'", attempt, choice);
                } catch (Exception ex) {
                    LoggerUtil.LOGGER.warn("[PRODUCT-BUILD] Force-click attempt #{} also failed for '{}': {}", attempt, choice, ex.getMessage());
                }
            }

            if (!clicked) {
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }

        if (!clicked) {
            LoggerUtil.LOGGER.error("[PRODUCT-BUILD] Failed to click '{}' after retries", choice);
            throw new RuntimeException("Failed to click feature-creation option: " + choice);
        }

        LoggerUtil.LOGGER.info("[PRODUCT-BUILD] Clicked '{}' — waiting for inline feature form or navigation", choice);

        // Give the UI a short moment to render the inline form or navigate; wait for loaders to finish.
        try {
            CommonMethods.waitForLoaderToDisappear(page);
            page.waitForTimeout(1500);
        } catch (Exception ignored) {
        }

        // Verify that click had desired effect: either inline name input visible or navigation to work-streams page
        boolean seenInline = CommonMethods.isElementPresent(nameInput) && nameInput.isVisible();
        boolean navigated = page.url() != null && page.url().contains("/projects/create-work-streams");

        if (!seenInline && !navigated) {
            LoggerUtil.LOGGER.warn("[PRODUCT-BUILD] Post-click check: neither inline form nor expected page detected. Trying one more force click then re-checking.");
            try {
                button.click(new Locator.ClickOptions().setForce(true));
                CommonMethods.waitForLoaderToDisappear(page);
                page.waitForTimeout(1200);
            } catch (Exception ignored) {}
        }

    }

    public void navigateToFeatureTab() {
        CommonMethods.clickOnTab(page, "Features");
    }

    @Step("Store product execution data: {productName}")
    private void storeProductExecutionData(String productName) {
        ProductExecutionData productExecutionData =
                new ProductExecutionData();

        productExecutionData.setProductName(productName);

        executionData.getProducts().add(productExecutionData);
    }

    @Step("Navigate to Dependencies Tab")
    public void navigateToDependencyTab() {
        CommonMethods.clickOnTab(page, "Dependencies");
    }

    @Step("Add dependency - Portfolio: {portfolioName}, Product: {productName}")
    public void addDependency(String portfolioName, String productName) {

        LoggerUtil.LOGGER.info(
                "========== Add Dependency to Product ==========");
        ProductDependencyPage productdependency =
                new ProductDependencyPage(page);

        productdependency.selectDropDown("Select Product Portfolio",portfolioName);
        productdependency.selectDropDown("Select Product",productName);
        CommonMethods.clickButton(page, "Add").click();

        LoggerUtil.LOGGER.info(
                "========== Dependency Added ==========");

    }
}