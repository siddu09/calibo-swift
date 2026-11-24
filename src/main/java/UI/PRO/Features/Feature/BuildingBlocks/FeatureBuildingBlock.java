package UI.PRO.Features.Feature.BuildingBlocks;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import UI.PRO.datahelper.ProductData;
import pages.PRO.Features.Feature.FeatureDetailsPage;
import UI.PRO.datahelper.FeatureData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import pages.PRO.Features.Feature.FeaturePage;
import testdatamanager.pro.FeatureExecutionData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;

import java.util.List;

public class FeatureBuildingBlock {

    private final Page page;
    private final ProExecutionData executionData;
    private final FeaturePage featurePage;

    public String featureName;

    public FeatureBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        this.featurePage = new FeaturePage(page);
    }

    public FeatureBuildingBlock(Page page, ProExecutionData executionData, FeaturePage featurePage) {
        this.page = page;
        this.executionData = executionData;
        this.featurePage = featurePage;
    }

    @Step("Select feature phases: {phases}")
    public void selectPhases(List<String> phases) {

        for (String phase : phases) {

            switch (phase) {

                case "Define" ->
                        featurePage.checkCircleDefine().click();

                case "Design" ->
                        featurePage.checkCircleDesign().click();

                case "Develop" ->
                        featurePage.checkCircleDevelop().click();

                case "Deploy" ->
                        LoggerUtil.LOGGER.info(
                                "Deploy is automatically selected. No action required."
                        );

                default ->
                        throw new IllegalArgumentException(
                                "Unsupported feature phase: " + phase
                        );
            }
        }
    }

    @Step("Create feature")
    public void createFeature(FeatureData featureData) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Feature Creation Started ==========");

        featurePage.addNewFeature().click();

        page.waitForTimeout(2000);

        selectPhases(featureData.getPhases());

        this.featureName =
                CommonMethods.generateUniqueTitle(featureData.getTitle());

        Allure.parameter("Feature name", featureName);

        featurePage.title().fill(featureName);

        featurePage.description().fill(featureData.getDescription());

        featurePage.selectFeatureStatus(featureData.getStatus());

        featurePage.create().click();

        CommonMethods.waitForLoaderToDisappear(page);

        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Feature Created ==========");

        storeFeatureExecutionData(featureName);
    }

    @Step("Create feature under the shared prerequisite product")
    public String createFeatureUnderSharedProduct(FeatureData featureData) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Feature Creation Started (shared product mode) ==========");

        featurePage.addNewFeature().click();
        page.waitForTimeout(2000);

        selectPhases(featureData.getPhases());

        this.featureName = CommonMethods.generateUniqueTitle(featureData.getTitle());
        Allure.parameter("Feature name", featureName);

        featurePage.title().fill(featureName);
        featurePage.description().fill(featureData.getDescription());
        featurePage.selectFeatureStatus(featureData.getStatus());
        featurePage.create().click();

        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info("========== Feature Created (shared product mode) ==========");

        ProductExecutionData targetProduct = getSharedProductExecutionData();
        storeFeatureExecutionDataForProduct(featureName, targetProduct);
        return featureName;
    }

    @Step("Create feature under explicit product"
    )
    public void createFeatureUnderProduct(FeatureData featureData, ProductExecutionData targetProduct) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Feature Creation Started (explicit product mode) ==========");

        featurePage.addNewFeature().click();
        page.waitForTimeout(2000);

        selectPhases(featureData.getPhases());

        this.featureName = CommonMethods.generateUniqueTitle(featureData.getTitle());
        Allure.parameter("Feature name", featureName);

        featurePage.title().fill(featureName);
        featurePage.description().fill(featureData.getDescription());
        featurePage.selectFeatureStatus(featureData.getStatus());
        featurePage.create().click();

        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info("========== Feature Created (explicit product mode) ==========");

        storeFeatureExecutionDataForProduct(featureName, targetProduct);
    }

    @Step("Skip or add feature additional details with action: {action}")
    public void skipOrAddFeatureAdditionalDetails(String action) {
        CommonMethods.clickButton(page, action).click();
    }

    @Step("View feature details")
    public void viewFeatureDetails() {
        featurePage.selectFeature(this.featureName).click();
    }

    @Step("Select stage: {phaseName}")
    public void selectStage(String phaseName) {
        featurePage.selectPhase(phaseName).click();

        page.waitForTimeout(2000);

        if ("Develop".equalsIgnoreCase(phaseName)) {
            waitForDevelopStageToLoad();
        }
    }

    /**
     * The "Develop" stage heading click occasionally doesn't register (its locator can resolve to a
     * non-interactive text node during a React re-render), silently leaving the page on the previous
     * stage - "Data Pipeline Studio" then never renders and a later click on it times out. Guard
     * against this by verifying the button becomes visible and re-clicking "Develop" if it doesn't,
     * up to 3 attempts total, instead of trusting a single blind click + fixed sleep.
     */
    private void waitForDevelopStageToLoad() {
        com.microsoft.playwright.Locator studioButton = page.getByRole(
                com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Data Pipeline Studio").setExact(true));

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                studioButton.waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                        .setTimeout(7000));
                return;
            } catch (Exception e) {
                if (attempt == 3) {
                    throw e;
                }
                LoggerUtil.LOGGER.warn(
                        "'Data Pipeline Studio' button not visible after selecting 'Develop' stage (attempt {}/3) - re-clicking 'Develop'.",
                        attempt);
                featurePage.selectPhase("Develop").click();
                page.waitForTimeout(2000);
            }
        }
    }

    @Step("Store feature execution data: {featureName}")
    private void storeFeatureExecutionData(String featureName) {
        storeFeatureExecutionDataForProduct(featureName, getSharedProductExecutionData());
    }

    public ProductExecutionData getSharedProductExecutionData() {
        if (executionData == null || !executionData.hasProducts()) {
            throw new IllegalStateException("No prerequisite product is available in executionData. Create the product before creating features.");
        }
        return executionData.getPrimaryProduct();
    }

    public void storeFeatureExecutionDataForProduct(String featureName, ProductExecutionData productExecutionData) {
        if (productExecutionData == null) {
            throw new IllegalStateException("Target product is null. Create the prerequisite product before adding features.");
        }

        FeatureExecutionData featureExecutionData = new FeatureExecutionData();
        featureExecutionData.setFeatureName(featureName);
        productExecutionData.getFeatures().add(featureExecutionData);
    }
    @Step("Validate the created feature name, description and status")
    public void validateCreatedFeature(FeatureData data) {
        FeatureDetailsPage details = new FeatureDetailsPage(page);
        assertThat(details.featureName()).hasText(featureName);
        assertThat(details.featureDescription()).hasText(data.getDescription());
        assertThat(details.featureStatus()).hasText(data.getStatus());
    }

    @Step("Delete the created feature before deleting its product")
    public void deleteFeature(ProductData data) {
        FeatureDetailsPage details = new FeatureDetailsPage(page);
        assertThat(details.featureName()).hasText(featureName);
        details.featureActions(featureName).click();
        details.deleteFeature(data.getDeleteAction()).click();
        details.deletionReason().fill(data.getFeatureDeleteReason());
        assertThat(details.confirmDelete(data.getDeleteAction())).isEnabled();
        details.confirmDelete(data.getDeleteAction()).click();
    }

}
