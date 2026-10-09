package UI.PRO.Product.BuildingBlocks;

import pages.PRO.Product.ProductFeatureTabPage;
import pages.PRO.MaturityAssesment.ProductMaturityAssessmentPage;
import UI.PRO.ProductPortfolio.BuildingBlocks.ProductPortfolioBuildingBlock;
import UI.PRO.datahelper.ProductData;
import UI.PRO.ReleaseTrain.BuildingBlocks.ReleaseBuildingBlock;
import UI.PRO.ReleaseTrain.BuildingBlocks.ReleaseTrainBuildingBlock;
import pages.PRO.ReleaseTrain.ProductReleasePage;
import pages.PRO.ReleaseTrain.NewReleaseTrainPage;
import pages.PRO.ReleaseTrain.NewReleasePage;
import UI.PRO.datahelper.ReleaseTrainData;
import UI.PRO.datahelper.ProductData.ProductAllocationData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.PRO.Product.ProductAddProjectDetailsPage;
import pages.PRO.Product.ProductDependencyPage;
import pages.PRO.Product.ProductDetailsPage;
import pages.PRO.Product.ProductPage;
import pages.PRO.Product.ProductTeamsPage;
import pages.PRO.ProCommonPage;
import pages.PRO.Product.ProductAdditionalDetailsOverviewTabPage;
import pages.PRO.Product.ProductAdditionalDetailsCustomFieldsTabPage;
import pages.PRO.Product.ProductAdditionalDetailsMilestonesTabPage;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import pages.LandingPage;
import pages.PRO.ProductPortfolio.NewProductPortfolioPagePage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsCustomFieldsTabPage;
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
        if (productData.getNoPortfolioOptionsText().equals(option.innerText().trim())) {
            portfolioName = createPortfolioFromProduct();
            option = searchProductPortfolio(portfolioName);
            assertThat(option).hasText(portfolioName);
            option.click();
            ProTestData.saveProductPortfolioName(portfolioName);
        } else {
            assertThat(option).hasText(portfolioName);
            option.click();
        }
        executionData.setPortfolioName(portfolioName);
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Selected Product Portfolio: {}", portfolioName);
    }

    private Locator searchProductPortfolio(String portfolioName) {
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Searching Product Portfolio: {}", portfolioName);
        productDetails.portfolioDropdown().click();
        productDetails.portfolioSearch().fill(portfolioName);
        return productDetails.portfolioSearchResult(portfolioName, productData.getNoPortfolioOptionsText());
    }

    @Step("Edit only the Overview of the portfolio containing the product")
    public void editExistingProductPortfolioOverview(ProductData data) {
        ProductPortfolioBuildingBlock portfolio =
                new ProductPortfolioBuildingBlock(page, executionData);
        portfolio.navigateToProductPortfolioPage();
        portfolio.searchPortfolio(executionData.getPortfolioName());
        portfolio.selectPortfolio();
        portfolio.editPortfolioOverviewOnly(data.getPortfolioOverviewUpdate());
    }

    @Step("Verify portfolio creation and update audit events for the existing product")
    public void validateExistingProductPortfolioAudit(ProductData data, String originalName) {
        java.util.Map<String, String> expectedObjects = new java.util.LinkedHashMap<>();
        data.getExpectedPortfolioAuditObjects().forEach((event, name) -> expectedObjects.put(event,
                name.replace("${originalPortfolioName}", originalName)
                        .replace("${updatedPortfolioName}", executionData.getPortfolioName())));
        new ProductPortfolioBuildingBlock(page, executionData)
                .validateReadyPortfolioRenameAudit(expectedObjects);
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
        if (productData.isPublicProduct()) {
            validateConfiguredProductCustomFields(productData);
            if (productData.getDynamicFieldNamePrefix() != null) {
                addProductCustomField(productData);
            }
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
        validateConfiguredProductCustomFields(data);
        ProductAdditionalDetailsCustomFieldsTabPage customFields = new ProductAdditionalDetailsCustomFieldsTabPage(page);
        customFields.richText(data.getOverviewLabel()).fill(data.getOverview());
        assertThat(customFields.richText(data.getOverviewLabel())).hasText(data.getOverview(),
                new com.microsoft.playwright.assertions.LocatorAssertions.HasTextOptions().setUseInnerText(true));
    }

    @Step("Validate configured custom field values")
    public void validateConfiguredProductCustomFields(ProductData data) {
        ProductAdditionalDetailsCustomFieldsTabPage customFields = new ProductAdditionalDetailsCustomFieldsTabPage(page);
        data.getCustomFields().forEach((label, expected) -> {
            Locator value = customFields.valueControl(label);
            if (Boolean.TRUE.equals(value.evaluate("element => 'value' in element"))) {
                assertThat(value).hasValue(expected);
            } else {
                assertThat(value).hasText(expected);
            }
            LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Verified {}: {}", label, expected);
        });
        Assert.assertEquals(customFields.valueInput(data.getDocumentationLabel()).inputValue().trim(),
                data.getDocumentationValue(), data.getDocumentationLabel());
    }

    @Step("Add a dynamic product custom field before creation")
    public void addProductCustomField(ProductData data) {
        ProductPortfolioAdditionalDetailsCustomFieldsTabPage fields =
                new ProductPortfolioAdditionalDetailsCustomFieldsTabPage(page);
        data.setDynamicFieldName(CommonMethods.generateUniqueTitle(data.getDynamicFieldNamePrefix()));
        data.setDynamicFieldValue(CommonMethods.generateUniqueTitle(data.getDynamicFieldValuePrefix()));
        fields.addCustomFields().click();
        fields.addFieldName().fill(data.getDynamicFieldName());
        fields.addFieldName().press("Tab");
        fields.localFieldValue(data.getDynamicFieldName()).fill(data.getDynamicFieldValue());
        fields.localFieldValue(data.getDynamicFieldName()).press("Tab");
        assertThat(fields.addFieldName()).hasValue(data.getDynamicFieldName());
        assertThat(fields.localFieldValue(data.getDynamicFieldName())).hasValue(data.getDynamicFieldValue());
        ProTestData.saveProductValues("validationOfCustomFields", java.util.Map.of(
                "dynamicFieldName", data.getDynamicFieldName(), "dynamicFieldValue", data.getDynamicFieldValue()));
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
        if (productData.getSaveButton().equals(action)) {
            // The feature prompt appears before the saved product is refreshed.
            // Answering it early submits stale details and overwrites the saved priority.
            Response refreshedProduct = page.waitForResponse(
                    response -> response.request().method().equals("GET")
                            && response.url().matches(".*/elab/projects/[^/?]+(?:\\?.*)?")
                            && response.ok(),
                    () -> productDetails.actionButton(action).click());
            refreshedProduct.finished();
        } else {
            productDetails.actionButton(action).click();
        }
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
    }

    @Step("Create release train and release for a product")
    public void createProductRelease(ProductData data) {
        ProductData.ProductReleaseData releaseData = data.getProductRelease();
        ReleaseTrainBuildingBlock train =
                new ReleaseTrainBuildingBlock(page, executionData);
        ReleaseTrainData trainData = new ReleaseTrainData();
        ReleaseTrainData.ReleaseTrain trainDetails =
                new ReleaseTrainData.ReleaseTrain();
        trainDetails.setName(CommonMethods.generateUniqueTitle(releaseData.getTrainNamePrefix()));
        trainDetails.setDescription(releaseData.getTrainDescription());
        trainData.setReleaseTrain(trainDetails);
        train.navigateToReleaseTrainPage();
        train.fillReleaseTrainDetails(trainData);
        NewReleasePage releasePage = new NewReleasePage(page);
        releasePage.selectDropDown(releaseData.getTagsLabel(), releaseData.getTag());
        NewReleaseTrainPage trainPage = new NewReleaseTrainPage(page);
        assertThat(trainPage.name()).hasValue(trainDetails.getName());
        assertThat(trainPage.description()).hasValue(releaseData.getTrainDescription());
        trainPage.create().click();
        ProValidation.validateSuccessMessage(page, releaseData.getTrainCreatedMessage());
        ProTestData.saveProductValues("addProductToRelease", java.util.Map.of("releaseTrainName", trainDetails.getName()));
        train.searchReleaseTrain();
        train.viewReleaseTrain();
        train.addNewRelease();
        ReleaseTrainData.Release release = new ReleaseTrainData.Release();
        release.setReleaseName(CommonMethods.generateUniqueTitle(releaseData.getReleaseNamePrefix()));
        release.setVersion(CommonMethods.generateUniqueTitle(releaseData.getVersionPrefix()));
        release.setReleaseId(CommonMethods.generateUniqueTitle(releaseData.getReleaseIdPrefix()));
        release.setReleaseObjective(releaseData.getObjective());
        release.setReleaseManager(releaseData.getManager());
        release.setReleaseType(releaseData.getType());
        release.setImpact(releaseData.getImpact());
        release.setRisk(releaseData.getRisk());
        new ReleaseBuildingBlock(page, executionData).fillReleaseDetails(release);
        releasePage.field(releaseData.getSprintLabel()).fill(releaseData.getSprintName());
        releasePage.field(releaseData.getTimelineLabel()).fill(releaseData.getTimeline());
        releasePage.field(releaseData.getTimelineLabel()).press("Tab");
        releasePage.field(releaseData.getReleaseDateLabel()).fill(releaseData.getReleaseDate());
        releasePage.field(releaseData.getReleaseDateLabel()).press("Tab");
        releaseData.getReleaseDropdowns().forEach(releasePage::selectDropDown);
        assertThat(releasePage.releaseObjective()).hasValue(releaseData.getObjective());
        assertThat(releasePage.name()).hasValue(release.getReleaseName());
        assertThat(releasePage.version()).hasValue(release.getVersion());
        assertThat(releasePage.releaseId()).hasValue(release.getReleaseId());
        assertThat(releasePage.field(releaseData.getSprintLabel())).hasValue(releaseData.getSprintName());
        assertThat(releasePage.field(releaseData.getTimelineLabel())).hasValue(releaseData.getTimeline());
        assertThat(releasePage.field(releaseData.getReleaseDateLabel())).hasValue(releaseData.getReleaseDate());
        releasePage.create().click();
        ProValidation.validateSuccessMessage(page, releaseData.getReleaseCreatedMessage());
        ProTestData.saveProductValues("addProductToRelease", java.util.Map.of("releaseName", release.getReleaseName()));
    }

    @Step("Join the created release from a private product")
    public void joinProductRelease(ProductData data) {
        ProductReleasePage productReleasePage = new ProductReleasePage(page);
        ProductData.ProductReleaseData releaseData = data.getProductRelease();
        productReleasePage.tab(releaseData.getReleasesTab()).click();
        productReleasePage.button(releaseData.getJoinReleaseButton()).click();
        assertThat(productReleasePage.title(releaseData.getJoinReleaseTitle())).isVisible();
        NewReleasePage releasePage = new NewReleasePage(page);
        releasePage.selectDropDown(releaseData.getTrainSelectLabel(), data.getReleaseTrainName());
        releasePage.selectDropDown(releaseData.getReleaseSelectLabel(), data.getReleaseName());
        productReleasePage.button(releaseData.getSelectButton()).click();
//        releaseData.getProductReleaseFields().forEach((label, value) -> releasePage.field(label).fill(value));
        releaseData.getProductReleaseDropdowns().forEach(releasePage::selectDropDown);
        releaseData.getProductReleaseFields().forEach((label, value) ->
                assertThat(releasePage.field(label)).hasValue(value));
        productReleasePage.button(releaseData.getSaveButton()).click();
        ProValidation.validateSuccessMessage(page, releaseData.getJoinedMessage());
        assertThat(productReleasePage.release(data.getReleaseName())).isVisible();
    }

    @Step("Navigate to product Teams tab")
    public void navigateToTeamsTab(ProductData data) {
        new ProductTeamsPage(page).tab(data.getTeamsTab()).click();
    }

    @Step("Add product allocation: {allocation.name}")
    public void addProductAllocation(ProductData data, ProductAllocationData allocation) {
        ProductTeamsPage teamsPage = new ProductTeamsPage(page);
        teamsPage.addMemberTeam(data.getAddMemberTeamButton()).click();
        teamsPage.memberSearch().fill(allocation.getName());
        assertThat(teamsPage.memberCategory(allocation.getCategory())).isVisible();
        teamsPage.dropdownOption(allocation.getName()).click();
        if (allocation.getRole() != null) {
            teamsPage.roleDropdown(data.getAllocationRoleLabel()).click();
            teamsPage.roleSearch(data.getAllocationRoleLabel()).fill(allocation.getRole());
            teamsPage.dropdownOption(allocation.getRole()).click();
        }
        teamsPage.allocationPeriod().fill(allocation.getStartDate() + " - " + allocation.getEndDate());
        teamsPage.allocationPeriod().press("Tab");
        assertThat(teamsPage.allocationPeriod()).hasValue(allocation.getStartDate() + " - " + allocation.getEndDate());
        teamsPage.allocationPercentage().fill(allocation.getAllocation());
        teamsPage.comments().fill(allocation.getComments());
        assertThat(teamsPage.allocationPercentage()).hasValue(allocation.getAllocation());
        assertThat(teamsPage.comments()).hasValue(allocation.getComments());
        assertThat(teamsPage.addAllocation(data.getAddAllocationButton())).isEnabled();
        teamsPage.addAllocation(data.getAddAllocationButton()).click();
        ProValidation.validateSuccessMessage(page, allocation.getSuccessMessage());
        new ProCommonPage(page).successMessage(allocation.getSuccessMessage())
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        LoggerUtil.LOGGER.info("[PRODUCT-BLOCK] Added allocation member {}", allocation.getName());
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


    @Step("Open the created product for editing")
    public void openProductEditor(ProductData data) {
        ProductDetailsPage details = new ProductDetailsPage(page);
        details.moreHoriz().click();
        details.viewDetailsOrEdit().click();
        new ProductAdditionalDetailsOverviewTabPage(page).editorOverview(data.getOverviewTab()).click();
    }

    @Step("Update product name, description, owner and priority")
    public void updateProductOverview(ProductData data) {
        String name = CommonMethods.generateUniqueTitle(data.getTitle());
        productDetails.title().fill(name);
        productDetails.description().fill(data.getDescription());
        ProductAdditionalDetailsOverviewTabPage overview = new ProductAdditionalDetailsOverviewTabPage(page);
        ProductData priorityData = ProTestData.getProduct("createPublicProduct");
        priorityData.setPriority(data.getPriority());
        completeProductOverview(priorityData);
        overview.ownerSearch(data.getOwnerLabel()).fill(data.getOwnerSearch());
        new ProductTeamsPage(page).dropdownOption(data.getOwnerSearch()).click();
        assertThat(overview.selectedOwnerByName(data.getOwnerLabel(), data.getOwner())).hasText(data.getOwner());
        assertThat(productDetails.title()).hasValue(name);
        assertThat(productDetails.description()).hasValue(data.getDescription());
        executionData.getProducts().get(executionData.getProducts().size() - 1).setProductName(name);
    }

    @Step("Update configured custom fields and add a dynamic product field")
    public void updateProductCustomFields(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getCustomFieldsTab()).click();
        ProductAdditionalDetailsCustomFieldsTabPage fields = new ProductAdditionalDetailsCustomFieldsTabPage(page);
        data.getCustomFields().forEach((label, value) -> {
            if (label.equals(data.getRegionLabel())) {
                fields.dropdown(label).click();
                new ProductDependencyPage(page).selectDropdownValue(value).click();
            } else {
                fields.valueInput(label).fill(value);
                fields.valueInput(label).press("Tab");
            }
        });
        fields.valueInput(data.getDocumentationLabel()).fill(data.getDocumentationValue());
        fields.valueInput(data.getDocumentationLabel()).press("Tab");
        completeProductCustomFields(data);
        addProductCustomField(data);
        ProTestData.saveProductValues("editProductWithAlltheFields", java.util.Map.of(
                "dynamicFieldName", data.getDynamicFieldName(), "dynamicFieldValue", data.getDynamicFieldValue()));
    }

    @Step("Save edited product details and verify the success message")
    public void saveEditedProductDetails(ProductData data) {
        saveOrSkipProductAdditionalDetails(data.getSaveButton());
        ProValidation.validateSuccessMessage(page, data.getDetailsSavedMessage());
        new ProCommonPage(page).successMessage(data.getDetailsSavedMessage())
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Confirm deletion of a product detail")
    public void confirmProductDetailDeletion() {
        ProductAdditionalDetailsCustomFieldsTabPage fields = new ProductAdditionalDetailsCustomFieldsTabPage(page);
        fields.deleteDialog().hover();
        fields.confirmDeleteDetail().click();
    }

    @Step("Delete the persisted dynamic product custom field")
    public void deleteProductCustomField(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getCustomFieldsTab()).click();
        ProductPortfolioAdditionalDetailsCustomFieldsTabPage fields = new ProductPortfolioAdditionalDetailsCustomFieldsTabPage(page);
        assertThat(fields.localFieldValue(data.getDynamicFieldName())).hasValue(data.getDynamicFieldValue());
        new ProductAdditionalDetailsCustomFieldsTabPage(page).deleteDynamicField(data.getDynamicFieldName()).click();
        confirmProductDetailDeletion();
        assertThat(fields.localField(data.getDynamicFieldName())).hasCount(0);
    }

    @Step("Add and persist a product milestone")
    public void addProductMilestone(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getMilestonesTab()).click();
        new ProductPortfolioAdditionalDetailsCustomFieldsTabPage(page).addCustomFields().click();
        ProductAdditionalDetailsMilestonesTabPage milestones = new ProductAdditionalDetailsMilestonesTabPage(page);
        data.setMilestoneName(CommonMethods.generateUniqueTitle(data.getMilestoneNamePrefix()));
        Locator nameInput = milestones.newMilestoneName();
        nameInput.fill(data.getMilestoneName());
        nameInput.press("Tab");
        milestones.timeline(data.getMilestoneName()).fill(data.getMilestoneTimeline());
        milestones.timeline(data.getMilestoneName()).press("Tab");
        assertThat(milestones.timeline(data.getMilestoneName())).hasValue(data.getMilestoneTimeline());
        ProTestData.saveProductValues("editProductWithAlltheFields", java.util.Map.of(
                "milestoneName", data.getMilestoneName(), "milestoneTimeline", data.getMilestoneTimeline()));
    }

    @Step("Delete the persisted product milestone")
    public void deleteProductMilestone(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getMilestonesTab()).click();
        ProductAdditionalDetailsMilestonesTabPage milestones = new ProductAdditionalDetailsMilestonesTabPage(page);
        Locator timeline = milestones.timeline(data.getMilestoneName());
        assertThat(timeline).hasValue(data.getMilestoneTimeline());
        milestones.deleteMilestone(data.getMilestoneName()).click();
        confirmProductDetailDeletion();
        assertThat(timeline).hasCount(0);
    }

    @Step("Delete the added dependency and verify confirmation and success")
    public void deleteProductDependency(ProductData data) {
        ProductDependencyPage dependency = new ProductDependencyPage(page);
        Locator row = dependency.relationshipRow(data.getDependencyProductName());
        dependency.deleteDependency(data.getDependencyProductName()).click();
        ProductAdditionalDetailsCustomFieldsTabPage fields = new ProductAdditionalDetailsCustomFieldsTabPage(page);
        fields.deleteDialog().hover();
        assertThat(fields.deleteDialog()).containsText(data.getDependencyDeleteConfirmation());
        fields.confirmDeleteDetail().click();
        ProValidation.validateSuccessMessage(page, data.getDependencyDeletedMessage());
        assertThat(row).hasCount(0);
    }


    @Step("Verify the dynamic field and milestone remain deleted after reopening")
    public void validateDeletedProductDetails(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getMilestonesTab()).click();
        assertThat(new ProductAdditionalDetailsMilestonesTabPage(page)
                .milestoneNameField(data.getMilestoneName())).hasCount(0);
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getCustomFieldsTab()).click();
        assertThat(new ProductPortfolioAdditionalDetailsCustomFieldsTabPage(page)
                .localField(data.getDynamicFieldName())).hasCount(0);
        validateConfiguredProductCustomFields(data);
    }

    @Step("Return from the product editor")
    public void closeProductEditor() {
        new ProductAdditionalDetailsOverviewTabPage(page).chevronLeftBack().click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Verify saved product owner and priority in the editor")
    public void validateEditedProductOverview(ProductData data) {
        ProductAdditionalDetailsOverviewTabPage overview = new ProductAdditionalDetailsOverviewTabPage(page);
        assertThat(overview.selectedOwnerByName(data.getOwnerLabel(), data.getOwner())).hasText(data.getOwner());
        assertThat(overview.selectedPriority(data.getPriorityLabel())).hasText(data.getPriority());
    }


    @Step("Leave the joined product release and verify confirmation, success and removal")
    public void leaveProductRelease(ProductData data) {
        ProductReleasePage releases = new ProductReleasePage(page);
        ProductData.ProductReleaseData releaseData = data.getProductRelease();
        Locator joinedRelease = releases.release(data.getReleaseName());
        assertThat(joinedRelease).isVisible();
        releases.releaseActions(data.getReleaseName()).click();
        releases.releaseMenuAction(releaseData.getLeaveReleaseAction()).click();
        assertThat(releases.title(releaseData.getLeaveConfirmationTitle()))
                .hasText(releaseData.getLeaveConfirmationTitle());
        releases.leaveComments(releaseData.getLeaveCommentsPlaceholder()).fill(releaseData.getLeaveComments());
        assertThat(releases.leaveComments(releaseData.getLeaveCommentsPlaceholder()))
                .hasValue(releaseData.getLeaveComments());
        assertThat(releases.button(releaseData.getLeaveButton())).isEnabled();
        releases.button(releaseData.getLeaveButton()).click();
        ProValidation.validateSuccessMessage(page, releaseData.getLeftMessage());
        assertThat(joinedRelease).hasCount(0);
    }


    @Step("Remove product phase: {phase} and validate workflow confirmation")
    public void removeProductPhase(String phase, ProductData data) {
        assertThat(productDetails.phaseCard(phase)).hasClass(java.util.regex.Pattern.compile("(^|\\s)"
                        + data.getSelectedPhaseClass() + "(\\s|$)"));
        selectPhases(java.util.List.of(phase));
        Locator confirmation = productDetails.confirmationMessage(data.getPhaseRemovalConfirmation());
        assertThat(confirmation).hasText(data.getPhaseRemovalConfirmation());
        productDetails.actionButton(data.getConfirmButton()).click();
        confirmation.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        assertThat(productDetails.phaseCard(phase)).not().hasClass(java.util.regex.Pattern.compile("(^|\\s)"
                        + data.getSelectedPhaseClass() + "(\\s|$)"));
    }

    @Step("Verify selected and unselected product phases")
    public void validateSelectedProductPhases(ProductData data, List<String> selectedPhases) {
        data.getPhases().forEach(phase -> {
            if (selectedPhases.contains(phase)) {
                assertThat(productDetails.phaseCard(phase)).hasClass(java.util.regex.Pattern.compile("(^|\\s)"
                        + data.getSelectedPhaseClass() + "(\\s|$)"));
            } else {
                assertThat(productDetails.phaseCard(phase)).not().hasClass(java.util.regex.Pattern.compile("(^|\\s)"
                        + data.getSelectedPhaseClass() + "(\\s|$)"));
            }
        });
    }

    @Step("Submit Create with mandatory fields empty and verify every red warning")
    public void submitProductWithoutMandatoryFieldsAndValidateErrors(ProductData data) {
        Locator form = productDetails.creationForm();
        assertThat(productDetails.title()).isEmpty();
        assertThat(productDetails.description()).isEmpty();
        String creationUrl = page.url();
        java.util.List<com.microsoft.playwright.Request> creationRequests = new java.util.ArrayList<>();
        java.util.function.Consumer<com.microsoft.playwright.Request> listener = request -> {
            if (request.method().equals(data.getProductCreationRequestMethod())
                    && request.url().matches(data.getProductCreationUrlPattern())) {
                creationRequests.add(request);
            }
        };
        page.onRequest(listener);
        try {
            productDetails.create().click();
            assertThat(productDetails.mandatoryFieldErrors())
                    .hasText(data.getMandatoryFieldErrors().toArray(String[]::new));
            for (String message : data.getMandatoryFieldErrors()) {
                Locator error = productDetails.confirmationMessage(message);
                assertThat(error).isVisible();
                assertThat(error).hasClass(java.util.regex.Pattern.compile(data.getMandatoryErrorClassPattern()));
            }
            assertThat(form).isVisible();
            assertThat(productDetails.create()).isEnabled();
            assertThat(productDetails.title()).isEmpty();
            assertThat(productDetails.description()).isEmpty();
            Assert.assertEquals(page.url(), creationUrl, data.getCreationPageChangedMessage());
            Assert.assertTrue(creationRequests.isEmpty(), data.getUnexpectedProductCreationMessage());
            Allure.addAttachment("Mandatory product field warnings", "text/plain",
                    String.join("\n", productDetails.mandatoryFieldErrors().allTextContents()));
        } finally {
            page.offRequest(listener);
        }
    }

    @Step("Verify mandatory warning disappears after correcting its field")
    public void validateMandatoryErrorRemoved(String message) {
        assertThat(productDetails.mandatoryFieldError(message)).hasCount(0);
    }

    @Step("Reuse product creation and verify all warnings clear before the creation request")
    public void createProductAfterMandatoryFieldErrors(ProductData data) {
        Locator form = productDetails.creationForm();
        Locator errors = productDetails.mandatoryFieldErrors();
        java.util.List<Integer> errorCountsAtSubmission = new java.util.ArrayList<>();
        java.util.List<Boolean> formVisibilityAtSubmission = new java.util.ArrayList<>();
        java.util.function.Consumer<com.microsoft.playwright.Route> handler = route -> {
            if (route.request().method().equals(data.getProductCreationRequestMethod())
                    && route.request().url().matches(data.getProductCreationUrlPattern())) {
                errorCountsAtSubmission.add(errors.count());
                formVisibilityAtSubmission.add(form.isVisible());
            }
            route.resume();
        };
        page.route(data.getProductCreationRoute(), handler);
        try {
            page.waitForResponse(response -> response.request().method().equals(data.getProductCreationRequestMethod())
                            && response.url().matches(data.getProductCreationUrlPattern()) && response.ok(),
                    () -> createNewProduct(data));
            Assert.assertEquals(errorCountsAtSubmission.size(), 1, data.getMissingCorrectedSubmissionMessage());
            Assert.assertTrue(formVisibilityAtSubmission.getFirst(), data.getCreationFormMissingMessage());
            Assert.assertEquals(errorCountsAtSubmission.getFirst().intValue(), 0, data.getErrorsNotClearedMessage());
        } finally {
            page.unroute(data.getProductCreationRoute(), handler);
        }
    }

    @Step("Submit Join Release without selecting a release train or release")
    public void submitJoinReleaseWithoutMandatoryFields(ProductData data) {
        ProductReleasePage releases = new ProductReleasePage(page);
        ProductData.ProductReleaseData releaseData = data.getProductRelease();
        releases.tab(releaseData.getReleasesTab()).click();
        releases.button(releaseData.getJoinReleaseButton()).click();
        assertThat(releases.title(releaseData.getJoinReleaseTitle())).isVisible();
        releases.button(releaseData.getSelectButton()).click();
        validateProductMandatoryWarnings(data, data.getReleaseMandatoryFieldErrors(), releases::mandatoryFieldError);
        assertThat(releases.title(releaseData.getJoinReleaseTitle())).isVisible();
        releases.back(data.getReleaseBackButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(new ProductDetailsPage(page).fetchProductName()).hasText(
                executionData.getProducts().getLast().getProductName());
    }

    @Step("Submit dependency without selecting a portfolio or product")
    public void submitDependencyWithoutMandatoryFields(ProductData data) {
        ProductDependencyPage dependencies = new ProductDependencyPage(page);
        dependencies.sidebarOption(data.getDependentOnTab()).click();
        dependencies.addButton(data.getAddDependencyButton()).click();
        validateProductMandatoryWarnings(data, data.getDependencyMandatoryFieldErrors(), dependencies::mandatoryFieldError);
        assertThat(dependencies.dropdown(data.getDependencyPortfolioPlaceholder())).isVisible();
        assertThat(dependencies.dropdown(data.getDependencyProductPlaceholder())).isVisible();
        assertThat(dependencies.addButton(data.getAddDependencyButton())).isEnabled();
    }

    @Step("Submit allocation without selecting a member or team")
    public void submitMemberTeamWithoutMandatoryFields(ProductData teamData, ProductData validationData) {
        ProductTeamsPage teams = new ProductTeamsPage(page);
        teams.addMemberTeam(teamData.getAddMemberTeamButton()).click();
        teams.addAllocation(teamData.getAddAllocationButton()).click();
        validateProductMandatoryWarnings(validationData, validationData.getMemberTeamMandatoryFieldErrors(), teams::mandatoryFieldError);
        assertThat(teams.memberSearch()).hasValue("");
        assertThat(teams.addAllocation(teamData.getAddAllocationButton())).isEnabled();
    }

    @Step("Verify captured mandatory warnings are displayed in red")
    private void validateProductMandatoryWarnings(ProductData data, List<String> messages,
            java.util.function.Function<String, Locator> warningLocator) {
        java.util.List<String> capturedMessages = new java.util.ArrayList<>();
        for (String message : messages) {
            Locator warning = warningLocator.apply(message);
            assertThat(warning).isVisible();
            assertThat(warning).hasText(message);
            assertThat(warning).hasClass(java.util.regex.Pattern.compile(data.getMandatoryErrorClassPattern()));
            capturedMessages.add(warning.innerText().trim());
        }
        Allure.addAttachment("Mandatory product selection warnings", "text/plain", String.join("\n", capturedMessages));
    }

    @Step("Submit a new product KPI without entering mandatory fields")
    public void submitKpiWithoutMandatoryFields(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getKpisTab()).click();
        pages.PRO.Product.ProductKpisPage kpis = new pages.PRO.Product.ProductKpisPage(page);
        kpis.button(data.getNewKpiButton()).click();
        kpis.button(data.getKpiCreateButton()).click();
        Locator warnings = kpis.mandatoryWarnings(data.getKpiCreateButton());
        Allure.addAttachment("Mandatory KPI warnings", "text/plain", String.join("\n", warnings.allTextContents()));
        assertThat(warnings).hasText(data.getKpiMandatoryFieldErrors().toArray(String[]::new));
        validateProductMandatoryWarnings(data, data.getKpiMandatoryFieldErrors(), kpis::mandatoryFieldError);
        assertThat(kpis.button(data.getKpiCreateButton())).isEnabled();
    }

    @Step("Close the empty Member/Team popup before opening the product editor")
    public void closeMemberTeamPopup() {
        ProductTeamsPage teams = new ProductTeamsPage(page);
        Locator memberSearch = teams.memberSearch();
        teams.closeMemberTeamPopup().click();
        assertThat(memberSearch).isHidden();
    }

    @Step("Delete the open product using the configured deletion reason")
    public void deleteProduct(ProductData data) {
        CommonMethods.waitForLoaderToDisappear(page);
        ProductDetailsPage details = new ProductDetailsPage(page);
        assertThat(details.fetchProductName()).hasText(executionData.getPrimaryProduct().getProductName());
        details.moreHoriz().click();
        details.deleteProduct().click();
        details.deleteProductReason().fill(data.getProductDeleteReason());
        assertThat(details.confirmDeleteProduct()).isEnabled();
        details.confirmDeleteProduct().click();
    }

    @Step("Verify the deleted feature is absent while its recorded product still exists")
    public void validateLinkedFeatureDeleted(String featureName, ProductData data) {
        openProduct(executionData.getPrimaryProduct().getProductName(), data);
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getFeaturesTab()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(new ProductFeatureTabPage(page).matchingFeature(featureName)).hasCount(0);
    }

    @Step("Search the recorded product and verify it is absent after deletion")
    public void validateRecordedProductDeleted(ProductData data) {
        navigateToProductsTab();
        String name = executionData.getPrimaryProduct().getProductName();
        productPage.searchProduct(data.getProductSearchPlaceholder()).fill(name);
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(productPage.matchingProduct(name)).hasCount(0);
    }

    @Step("Save Operationalize additional details and open the created product")
    public void saveOperationalizeProductAdditionalDetails(ProductData data) {
        saveOrSkipProductAdditionalDetails(data.getSaveButton());
        new ProductDetailsPage(page).productTitle().waitFor();
        validateSavedOperationalizeProductDetails(data);
    }

    @Step("Validate saved Operationalize public visibility, owner, priority, and overview")
    public void validateSavedOperationalizeProductDetails(ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getOverviewTab()).click();
        ProductDetailsPage details = new ProductDetailsPage(page);
        assertThat(details.visibility(data.getPublicLabel())).isVisible();
        assertThat(details.owner(data.getOwner())).isVisible();
        assertThat(details.overviewValue(data.getPriorityLabel().replace(" (Optional)", "")))
                .hasText(data.getPriority());
        assertThat(details.overviewValue(data.getOverviewLabel().replace(" (Optional)", "")))
                .hasText(data.getOverview(),
                        new com.microsoft.playwright.assertions.LocatorAssertions.HasTextOptions().setUseInnerText(true));
    }

    @Step("Skip Operationalize additional details and open the created product")
    public void skipOperationalizeAdditionalDetails(ProductData data) {
        CommonMethods.waitForLoaderToDisappear(page);
        productDetails.actionButton(data.getSkipButton()).click();
        Locator confirmation = productDetails.confirmationMessage(data.getConfirmationMessage());
        assertThat(confirmation).isVisible();
        productDetails.actionButton(data.getConfirmButton()).click();
        assertThat(confirmation).isHidden();
        CommonMethods.waitForLoaderToDisappear(page);
        new ProductDetailsPage(page).productTitle().waitFor();
    }

    @Step("Cancel product creation and confirm the cancellation message")
    public void cancelProductCreation(ProductData data) {
        productDetails.actionButton(data.getCancelButton()).click();
        Locator confirmation = productDetails.confirmationMessage(data.getConfirmationMessage());
        assertThat(confirmation).isVisible();
        assertThat(confirmation).hasText(data.getConfirmationMessage());
        productDetails.actionButton(data.getConfirmButton()).click();
        confirmation.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Verify return to the Products listing page")
    public void validateProductsListingPage(ProductData data) {
        assertThat(productPage.newProduct(data.getNewProductButton())).isVisible();
        assertThat(productPage.searchProduct(data.getProductSearchPlaceholder())).isVisible();
    }

    @Step("Join a labelled release for product maturity assessment")
    public void joinReleaseForProductMaturityAssessment(ProductData data) {
        ProductReleasePage releases = new ProductReleasePage(page);
        ProductData.ProductReleaseData release = data.getProductRelease();
        releases.tab(release.getReleasesTab()).click();
        releases.button(release.getJoinReleaseButton()).click();
        assertThat(releases.title(release.getJoinReleaseTitle())).isVisible();
        NewReleasePage selection = new NewReleasePage(page);
        selection.selectDropDown(release.getTrainSelectLabel(), data.getReleaseTrainName());
        selection.selectDropDown(release.getReleaseSelectLabel(), data.getReleaseName());
        releases.button(release.getSelectButton()).click();
        release.getProductReleaseFields().forEach((label, value) -> {
            releases.field(label).fill(value);
            assertThat(releases.field(label)).hasValue(value);
        });
        release.getProductReleaseDropdowns().forEach(selection::selectDropDown);
        releases.button(release.getSaveButton()).click();
        ProValidation.validateSuccessMessage(page, release.getJoinedMessage());
        assertThat(releases.release(data.getReleaseName())).isVisible();
    }

    @Step("Initiate Product Delivery maturity assessment")
    public void initiateProductMaturityAssessment(ProductData data) {
        ProductData.ProductMaturityAssessmentData assessment = data.getMaturityAssessment();
        ProductMaturityAssessmentPage assessmentPage = new ProductMaturityAssessmentPage(page);
        ProductDetailsPage details = new ProductDetailsPage(page);
        details.moreHoriz().click();
        details.maturityAssessment(assessment.getMenuAction()).click();
        page.waitForURL(url -> url.contains(assessment.getAssessmentPath()));
        CommonMethods.waitForLoaderToDisappear(page);
        assessmentPage.newAssessment(assessment.getNewAssessmentButton()).click();
        String name = CommonMethods.generateUniqueTitle(assessment.getNamePrefix());
        assessmentPage.nameEditor(assessment.getNameLabel(), assessment.getNamePlaceholder()).click();
        assessmentPage.name().fill(name);
        assessmentPage.saveName().click();
        assertThat(assessmentPage.nameValue(name)).isVisible();
        assessmentPage.selectType(assessment.getTypeLabel(), assessment.getType());
        assessmentPage.selectRelease(assessment.getReleaseLabel(), data.getReleaseName());
        assessmentPage.respondentControl(assessment.getSubCategory()).click();
        assessmentPage.respondentSearch(assessment.getSubCategory()).fill(assessment.getRespondent());
        new NewReleasePage(page).selectDropdownValue(assessment.getRespondent()).click();
        assertThat(assessmentPage.respondentControl(assessment.getSubCategory()))
                .containsText(assessment.getRespondent());
        assessmentPage.selectDueDate(assessment.getSubCategory(), assessment.getResponseDueDate());
        assertThat(assessmentPage.dueDate(assessment.getSubCategory())).hasValue(assessment.getResponseDueDate());
        assertThat(assessmentPage.initiate(assessment.getSubCategory(), assessment.getInitiateButton())).isEnabled();
        assessmentPage.initiate(assessment.getSubCategory(), assessment.getInitiateButton()).click();
        ProValidation.validateSuccessMessage(page, assessment.getInitiatedMessage());
    }

    @Step("Prepare and save Product Delivery assessment details without initiating")
    public String saveProductMaturityAssessmentWithoutInitiating(ProductData data) {
        ProductData.ProductMaturityAssessmentData assessment = data.getMaturityAssessment();
        ProductMaturityAssessmentPage assessmentPage = new ProductMaturityAssessmentPage(page);
        ProductDetailsPage details = new ProductDetailsPage(page);
        details.moreHoriz().click();
        details.maturityAssessment(assessment.getMenuAction()).click();
        page.waitForURL(url -> url.contains(assessment.getAssessmentPath()));
        CommonMethods.waitForLoaderToDisappear(page);
        assessmentPage.newAssessment(assessment.getNewAssessmentButton()).click();
        String name = CommonMethods.generateUniqueTitle(assessment.getNamePrefix());
        assessmentPage.nameEditor(assessment.getNameLabel(), assessment.getNamePlaceholder()).click();
        assessmentPage.name().fill(name);
        assessmentPage.saveName().click();
        assertThat(assessmentPage.nameValue(name)).isVisible();
        assessmentPage.selectType(assessment.getTypeLabel(), assessment.getType());
        assessmentPage.selectRelease(assessment.getReleaseLabel(), data.getReleaseName());
        assessmentPage.respondentControl(assessment.getSubCategory()).click();
        assessmentPage.respondentSearch(assessment.getSubCategory()).fill(assessment.getRespondent());
        new NewReleasePage(page).selectDropdownValue(assessment.getRespondent()).click();
        assertThat(assessmentPage.respondentControl(assessment.getSubCategory()))
                .containsText(assessment.getRespondent());
        assessmentPage.selectDueDate(assessment.getSubCategory(), assessment.getResponseDueDate());
        assertThat(assessmentPage.dueDate(assessment.getSubCategory())).hasValue(assessment.getResponseDueDate());
        assertThat(assessmentPage.initiate(assessment.getSubCategory(), assessment.getInitiateButton())).isEnabled();
        return name;
    }

    @Step("Verify the saved assessment remains available to initiate after navigating back")
    public void validateSavedProductMaturityAssessmentDraft(ProductData data, String assessmentName) {
        ProductMaturityAssessmentPage assessmentPage = new ProductMaturityAssessmentPage(page);
        new ProductReleasePage(page).back(data.getAssessmentDraft().getBackButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(assessmentPage.currentAssessments(data.getAssessmentDraft().getCurrentAssessmentsLabel(),
                data.getAssessmentDraft().getLoadTimeoutMs())).isVisible();
        Locator initiate = assessmentPage.savedAssessmentInitiate(assessmentName,
                data.getMaturityAssessment().getInitiateButton(), data.getAssessmentDraft().getLoadTimeoutMs());
        assertThat(initiate).isVisible();
        assertThat(initiate).isEnabled();
        assertThat(assessmentPage.nameValue(assessmentName)).isVisible();
        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(assessmentPage.currentAssessments(data.getAssessmentDraft().getCurrentAssessmentsLabel(),
                data.getAssessmentDraft().getLoadTimeoutMs())).isVisible();
        Locator savedInitiate = assessmentPage.savedAssessmentInitiate(assessmentName,
                data.getMaturityAssessment().getInitiateButton(), data.getAssessmentDraft().getLoadTimeoutMs());
        assertThat(savedInitiate).isVisible();
        assertThat(savedInitiate).isEnabled();
        assertThat(assessmentPage.nameValue(assessmentName)).isVisible();
    }

    @Step("Discard the initiated maturity assessment and verify it is removed from the overview")
    public void discardInitiatedProductMaturityAssessment(ProductData data) {
        ProductMaturityAssessmentPage assessmentPage = new ProductMaturityAssessmentPage(page);
        ProductData.ProductAssessmentDiscardData discard = data.getAssessmentDiscard();
        String assessmentName = assessmentPage.generatedAssessmentName(
                data.getMaturityAssessment().getNamePrefix()).innerText().trim();
        new ProductReleasePage(page).back(discard.getBackButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(assessmentPage.nameValue(assessmentName)).isVisible();
        new ProductDetailsPage(page).moreHoriz().click();
        assessmentPage.discardAssessment(discard.getDiscardAction()).click();
        assertThat(assessmentPage.discardConfirmation(discard.getConfirmationMessage())).isVisible();
        assessmentPage.confirmDiscard(discard.getYesButton()).click();
        ProValidation.validateSuccessMessage(page, discard.getDiscardedMessage());
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(assessmentPage.newAssessment(data.getMaturityAssessment().getNewAssessmentButton())).isVisible();
        assertThat(assessmentPage.assessmentOverview()).not().containsText(assessmentName);
        page.reload();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(assessmentPage.newAssessment(data.getMaturityAssessment().getNewAssessmentButton())).isVisible();
        assertThat(assessmentPage.assessmentOverview()).not().containsText(assessmentName);
    }

    @Step("Open the product editor for Sprint assessment setup")
    public void openProductSprintEditor(ProductData data) {
        ProductDetailsPage details = new ProductDetailsPage(page);
        details.moreHoriz().click();
        details.productEditorAction(data.getSprintSetup().getEditAction()).click();
        new ProductAdditionalDetailsOverviewTabPage(page).editorOverview(data.getOverviewTab()).click();
    }

    @Step("Configure the product Agile project and wiki for Sprint assessment")
    public void configureProductSprintDetails(ProductData data) {
        openProductSprintEditor(data);
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getSprintSetup().getOthersTab()).click();
        NewReleasePage dropdown = new NewReleasePage(page);
        data.getSprintSetup().getProductDropdowns().forEach(dropdown::selectDropDown);
        saveEditedProductDetails(data);
    }

    @Step("Open the product editor and directly configure the Agile team board on Others")
    public void configureProductSprintBoardDirectly(ProductData data) {
        ProductDetailsPage details = new ProductDetailsPage(page);
        details.moreHoriz().click();
        details.productEditorAction(data.getSprintSetup().getEditAction()).click();
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getSprintSetup().getOthersTab()).click();
        NewReleasePage dropdown = new NewReleasePage(page);
        data.getSprintSetup().getBoardDropdowns().forEach(dropdown::selectDropDown);
        saveEditedProductDetails(data);
    }

    @Step("Configure the product Agile team board for Sprint assessment")
    public void configureProductSprintBoard(ProductData data) {
        openProductSprintEditor(data);
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getSprintSetup().getOthersTab()).click();
        NewReleasePage dropdown = new NewReleasePage(page);
        data.getSprintSetup().getBoardDropdowns().forEach(dropdown::selectDropDown);
        saveEditedProductDetails(data);
    }

    @Step("Add the Agile-associated team for Sprint assessment")
    public void addProductSprintTeam(ProductData data) {
        ProductTeamsPage teams = new ProductTeamsPage(page);
        ProductData.ProductSprintSetupData setup = data.getSprintSetup();
        teams.addMemberTeam(data.getAddMemberTeamButton()).click();
        teams.memberSearch().fill(setup.getTeamName());
        assertThat(teams.memberCategory(setup.getTeamCategory())).isVisible();
        teams.dropdownOption(setup.getTeamName()).click();
        teams.addAllocation(data.getAddAllocationButton()).click();
        ProValidation.validateSuccessMessage(page, setup.getTeamAddedMessage());
        assertThat(teams.allocationRow(setup.getTeamName())).isVisible();
    }

    @Step("Initiate Team Practices maturity assessment associated with a sprint")
    public void initiateTeamMaturityAssessmentWithSprint(ProductData data) {
        ProductData.ProductMaturityAssessmentData assessment = data.getMaturityAssessment();
        ProductMaturityAssessmentPage assessmentPage = new ProductMaturityAssessmentPage(page);
        ProductDetailsPage details = new ProductDetailsPage(page);
        details.moreHoriz().click();
        details.maturityAssessment(assessment.getMenuAction()).click();
        page.waitForURL(url -> url.contains(assessment.getAssessmentPath()));
        CommonMethods.waitForLoaderToDisappear(page);
        assessmentPage.newAssessment(assessment.getNewAssessmentButton()).click();
        String name = CommonMethods.generateUniqueTitle(assessment.getNamePrefix());
        assessmentPage.nameEditor(assessment.getNameLabel(), assessment.getNamePlaceholder()).click();
        assessmentPage.name().fill(name);
        assessmentPage.saveName().click();
        assertThat(assessmentPage.nameValue(name)).isVisible();
        assessmentPage.selectType(assessment.getTypeLabel(), assessment.getType());
        assessmentPage.selectType(assessment.getAssociateWithLabel(), assessment.getAssociateWith());
        assessmentPage.selectSprint(assessment.getSprintLabel(), assessment.getSprintName(),
                assessment.getSprintUnavailableMessage());
        assessmentPage.respondentControl(assessment.getSubCategory()).click();
        assessmentPage.respondentSearch(assessment.getSubCategory()).fill(assessment.getRespondent());
        new NewReleasePage(page).selectDropdownValue(assessment.getRespondent()).click();
        assertThat(assessmentPage.respondentControl(assessment.getSubCategory()))
                .containsText(assessment.getRespondent());
        assessmentPage.selectDueDate(assessment.getSubCategory(), assessment.getResponseDueDate());
        assertThat(assessmentPage.dueDate(assessment.getSubCategory())).hasValue(assessment.getResponseDueDate());
        assertThat(assessmentPage.initiate(assessment.getSubCategory(), assessment.getInitiateButton())).isEnabled();
        assessmentPage.initiate(assessment.getSubCategory(), assessment.getInitiateButton()).click();
        ProValidation.validateSuccessMessage(page, assessment.getInitiatedMessage());
    }

}
