package UI.PRO.Product.BuildingBlocks;

import UI.PRO.datahelper.ProductData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.PRO.Product.ProductAddProjectDetailsPage;
import pages.PRO.Product.ProductDependencyPage;
import pages.PRO.Product.ProductDetailsPage;
import pages.PRO.Product.ProductPage;
import pages.PRO.Product.ProductAdditionalDetailsOverviewTabPage;
import pages.PRO.Product.ProductAdditionalDetailsCustomFieldsTabPage;
import pages.PRO.Product.ProductAdditionalDetailsMilestonesTabPage;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import pages.LandingPage;
import pages.PRO.ProductPortfolio.NewProductPortfolioPagePage;
import testdatamanager.pro.ProductExecutionData;
import testdatamanager.pro.ProExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;


import UI.PRO.CommonProValidations.ProValidation;
import testdatamanager.pro.ProTestData;
import java.util.List;

public class ProductBuildingBlock {

    private final Page page;
    private final ProExecutionData executionData;

    private final ProductAddProjectDetailsPage productDetails;
    private final ProductPage productPage;
    private final ProductData productData;
    private final LandingPage landingPage;

    public ProductBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.productDetails = new ProductAddProjectDetailsPage(page);
        this.productPage = new ProductPage(page);
        this.landingPage = new LandingPage(page);
        this.productData = ProTestData.getProduct(ProTestData.CREATE_PRODUCT);
    }

    @Step("Navigate to Products and open New Product")
    public void openNewProductFromProducts() {
        navigateToProductsTab();
        productPage.newProduct(productData.getNewProductButton()).click();
    }

    @Step("Navigate to Products and move away from the navigation menu")
    public void navigateToProductsTab() {
//        LandingPage landingPage = new LandingPage(page);
        landingPage.hoverOnNavigationBar().click();
        productPage.projectsA().click();
        page.mouse().move(500, 300);
    }

    @Step("Select or create portfolio on the Create Product page")
    public void selectOrCreateProductPortfolio() {
        String portfolioName = ProTestData.getProduct(ProTestData.CREATE_PRODUCT).getPortfolioName();
        Assert.assertNotNull(portfolioName, productData.getMissingPortfolioNameMessage());
        Assert.assertFalse(portfolioName.isBlank(), productData.getBlankPortfolioNameMessage());
        Locator option = searchProductPortfolio(portfolioName);
        option.or(productDetails.noPortfolioOptions(productData.getNoPortfolioOptionsText())).first().waitFor();
        if (!option.isVisible()) {
            portfolioName = createPortfolioFromProduct();
            option = searchProductPortfolio(portfolioName);
            option.click();
            ProTestData.saveProductPortfolioName(portfolioName);
        } else {
            option.click();
        }
        executionData.setPortfolioName(portfolioName);
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Selected Product Portfolio: {}", portfolioName);
    }

    private Locator searchProductPortfolio(String portfolioName) {
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Searching Product Portfolio: {}", portfolioName);
        productDetails.portfolioDropdown().click();
        productDetails.portfolioSearch().fill(portfolioName);
        return productDetails.portfolioOption(portfolioName);
    }

    private String createPortfolioFromProduct() {
        productDetails.portfolioSearch().press("Escape");
        productDetails.newPortfolio(productData.getNewPortfolioButton()).click();
        String name = CommonMethods.generateUniqueTitle(productData.getPortfolioNamePrefix());
        NewProductPortfolioPagePage portfolioPage = new NewProductPortfolioPagePage(page);
        portfolioPage.name().fill(name);
        portfolioPage.description().fill(productData.getPortfolioDescription());
        portfolioPage.create().click();
        ProValidation.validateSuccessMessage(page, productData.getPortfolioCreatedMessage());
        productDetails.title().waitFor();
        executionData.setPublicPortfolio(false);
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Created portfolio and returned to Create Product: {}", name);
        return name;
    }

    @Step("Delete product")
    public void deleteProduct() {
        LoggerUtil.LOGGER.info("========== Deleting Product ==========");
        CommonMethods.waitForLoaderToDisappear(page);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(page);
        productDetailsPage.moreHoriz().click();
        productDetailsPage.deleteProduct().click();
        productDetailsPage.deleteProductReason()
                .fill("Delete the product before deleting its portfolio for automated regression validation");
        productDetailsPage.confirmDeleteProduct().click();
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
                CommonMethods.sectionHeader(page, productData.getCreatePageTitle()));
    }

    @Step("Create new product")
    public void createNewProduct(ProductData productData) {

        String productName =
                CommonMethods.generateUniqueTitle(productData.getTitle());
        Allure.parameter("Product name", productName);

        productDetails.title().fill(productName);

        if (productData.isPublicProduct()) {
            productDetails.publicProduct(productData.getPublicLabel()).check();
            assertThat(productDetails.publicProduct(productData.getPublicLabel())).isChecked();
        } else {
            productDetails.publicProduct(productData.getPublicLabel()).uncheck();
            assertThat(productDetails.publicProduct(productData.getPublicLabel())).not().isChecked();
        }
        selectPhases(productData.getPhases());

        productDetails.description().fill(productData.getDescription());

        productDetails.selectBusinessGroup(
                productData.getBusinessGroup());

        if (productData.isPublicProduct()) {
            assertThat(productDetails.selectedFieldValues(productData.getBusinessGroupLabel()))
                    .hasText(productData.getBusinessGroup());
            assertThat(productDetails.selectedFieldValues(productData.getOwnerLabel()))
                    .hasText(productData.getOwner());
        }
        productDetails.create().click();

        LoggerUtil.LOGGER.info(
                "========== Product Created ==========");
        storeProductExecutionData(productName);
    }

    @Step("Validate product owner and update priority")
    public void completeProductOverview(ProductData data) {
        ProductAdditionalDetailsOverviewTabPage overview = new ProductAdditionalDetailsOverviewTabPage(page);
        overview.tab(data.getOverviewTab()).click();
        assertThat(overview.selectedOwner(data.getOwnerLabel())).hasText(data.getOwner());
        overview.priorityControl(data.getPriorityLabel()).click();
        overview.priorityOption(data.getPriority()).click();
        assertThat(overview.selectedPriority(data.getPriorityLabel())).hasText(data.getPriority());
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Verified owner {} and selected priority {}", data.getOwner(), data.getPriority());
    }

    @Step("Validate product custom fields and enter product overview")
    public void completeProductCustomFields(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getCustomFieldsTab()).click();
        ProductAdditionalDetailsCustomFieldsTabPage customFields = new ProductAdditionalDetailsCustomFieldsTabPage(page);
        data.getCustomFields().forEach((label, expected) -> {
            Locator input = customFields.valueInput(label);
            if (input.count() > 0) {
                assertThat(input).hasValue(expected);
            } else {
                assertThat(customFields.selectedValue(label)).hasText(expected);
            }
            LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Verified {}: {}", label, expected);
        });
        Assert.assertEquals(customFields.valueInput(data.getDocumentationLabel()).inputValue().trim(),
                data.getDocumentationValue(), data.getDocumentationLabel());
        customFields.richText(data.getOverviewLabel()).fill(data.getOverview());
        assertThat(customFields.richText(data.getOverviewLabel())).hasText(data.getOverview(),
                new com.microsoft.playwright.assertions.LocatorAssertions.HasTextOptions().setUseInnerText(true));
    }

    @Step("Validate configured product milestones")
    public void validateProductMilestones(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getMilestonesTab()).click();
        ProductAdditionalDetailsMilestonesTabPage milestones = new ProductAdditionalDetailsMilestonesTabPage(page);
        data.getMilestones().forEach((name, dates) -> {
            assertThat(milestones.timeline(name)).hasValue(dates);
            LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Verified {}: {}", name, dates);
        });
    }

    @Step("Save or skip product additional details with action: {action}")
    public void saveOrSkipProductAdditionalDetails(String action) {
        CommonMethods.waitForLoaderToDisappear(page);
        productDetails.actionButton(action).click();
        CommonMethods.waitForLoaderToDisappear(page);
        if (productData.getSkipButton().equals(action)) {
            confirmUnsavedProductDetails();
        }
    }

    private void confirmUnsavedProductDetails() {
        Locator nextStep = productDetails.additionalDetailsPrompt(
                productData.getConfirmationMessage(), productData.getFeaturePrompt());
        if (!productData.getConfirmationMessage().equals(nextStep.innerText().trim())) {
            return;
        }
        Locator cancellation = productDetails.confirmationMessage(productData.getConfirmationMessage());
        productDetails.actionButton(productData.getConfirmButton()).click();
        cancellation.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Confirmed unsaved changes");
        nextStep = productDetails.additionalDetailsPrompt(productData.getSaveButton(), productData.getFeaturePrompt());
        if (productData.getSaveButton().equals(nextStep.innerText().trim())) {
            nextStep.click();
            CommonMethods.waitForLoaderToDisappear(page);
            LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Saved product additional details");
        }
    }

    @Step("Complete feature creation choice")
    public void chooseFeatureCreationOption() {
        productPage.featurePrompt(productData.getFeaturePrompt()).waitFor();
        productPage.featureChoice(productData.getFeatureChoice()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        new ProductDetailsPage(page).productTitle().waitFor();
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Product details page opened");
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

    @Step("Search and open product: {productName}")
    public void openProduct(String productName, ProductData data) {
        navigateToProductsTab();
        productPage.searchProduct(data.getProductSearchPlaceholder()).fill(productName);
        productPage.productByName(productName).click();
        assertThat(new ProductDetailsPage(page).fetchProductName()).hasText(productName);
    }

    @Step("Verify dependent product: {productName}")
    public void validateDependent(String portfolioName, String productName, ProductData data) {
        ProductDependencyPage dependencyPage = new ProductDependencyPage(page);
        dependencyPage.sidebarOption(data.getDependentsTab()).click();
        assertThat(dependencyPage.relationshipRow(productName)).containsText(portfolioName);
        assertThat(dependencyPage.relationshipRow(productName)).containsText(productName);
    }

    @Step("Verify dependency notification for: {dependentName}")
    public void validateDependencyNotification(String dependentName, ProductData data, boolean markAsRead) {
        ProductDependencyPage dependencyPage = new ProductDependencyPage(page);
        Locator unreadBadge = dependencyPage.notificationBadge(data.getNotificationsTab());
        assertThat(unreadBadge).hasText(data.getUnreadNotificationCount());
        dependencyPage.sidebarOption(data.getNotificationsTab()).click();
        String message = data.getDependentNotificationTemplate().formatted(dependentName);
        assertThat(dependencyPage.notificationMessage(message)).hasText(message);
        Locator readAction = dependencyPage.markAsRead(message, data.getMarkAsReadLabel());
        Locator deleteAction = dependencyPage.deleteNotification(message, data.getDeleteNotificationLabel());
        assertThat(readAction).isVisible();
        assertThat(readAction).isEnabled();
        assertThat(deleteAction).isVisible();
        assertThat(deleteAction).isEnabled();
        if (markAsRead) {
            readAction.click();
            assertThat(unreadBadge).isHidden();
        }
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Validated dependent notification for {}", dependentName);
    }

    @Step("Navigate to Dependencies Tab")
    public void navigateToDependencyTab() {
        new ProductDependencyPage(page).tab(productData.getDependenciesTab()).click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Add dependency - Portfolio: {portfolioName}, Product: {productName}")
    public void addDependency(String portfolioName, String productName) {

        LoggerUtil.LOGGER.info(
                "========== Add Dependency to Product ==========");
        ProductDependencyPage productdependency =
                new ProductDependencyPage(page);

        productdependency.sidebarOption(productData.getDependentOnTab()).click();
        productdependency.selectDropDown(productData.getDependencyPortfolioPlaceholder(), portfolioName);
        productdependency.selectDropDown(productData.getDependencyProductPlaceholder(), productName);
        productdependency.addButton(productData.getAddDependencyButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(productdependency.relationshipRow(productName)).containsText(portfolioName);

        LoggerUtil.LOGGER.info(
                "========== Dependency Added ==========");

    }

    @Step("Navigate to Product Page")
    public void navigateToProductPage() {
        page.waitForTimeout(2000);
        LoggerUtil.LOGGER.info("========== Navigating to Product Page ==========");

        landingPage.hoverOnNavigationBar().click();

        landingPage.clickOnOptions("Products").click();
    }

    @Step("Search product by name")
    public void searchProduct(String productName){

        productPage.search().fill(productName);
        CommonMethods.waitForLoaderToDisappear(page);
        page.mouse().move(500, 300);
    }

    @Step("Select product by name")
    public void selectProduct(String productName){

        productPage.select(productName).click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

}
