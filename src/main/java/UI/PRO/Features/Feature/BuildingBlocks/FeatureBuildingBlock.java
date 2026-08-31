package UI.PRO.Features.Feature.BuildingBlocks;

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
    }

    @Step("Store feature execution data: {featureName}")
    private void storeFeatureExecutionData(String featureName) {

        FeatureExecutionData featureExecutionData =
                new FeatureExecutionData();

        featureExecutionData.setFeatureName(featureName);

        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        productExecutionData.getFeatures()
                .add(featureExecutionData);
    }
}