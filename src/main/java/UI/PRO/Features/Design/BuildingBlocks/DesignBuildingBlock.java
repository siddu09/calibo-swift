package UI.PRO.Features.Design.BuildingBlocks;

import UI.PRO.datahelper.DesignData;
import com.microsoft.playwright.Page;
import pages.PRO.Features.Design.DesignPage;
import pages.PRO.Features.Feature.FeaturePage;
import testdatamanager.pro.FeatureExecutionData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;

public class DesignBuildingBlock {


    private final Page page;
    private final ProExecutionData executionData;

    private final FeaturePage featurePage;
    private final DesignPage designPage;

    public DesignBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.featurePage = new FeaturePage(page);
        this.designPage = new DesignPage(page);
    }

    public void createDesign(DesignData designData) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Design Started ==========");


        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        FeatureExecutionData featureExecutionData =
                productExecutionData.getFeatures()
                        .get(productExecutionData.getFeatures().size() - 1);

        String featureName =
                featureExecutionData.getFeatureName();

        CommonMethods.clickButton(page, featureName).click();

        page.waitForTimeout(2000);

        featurePage.selectPhase("Design").click();

        page.waitForTimeout(2000);

        CommonMethods.waitForLoaderToDisappear(page);

        designPage.chooseCategory(
                designData.getCategory());

        CommonMethods.clickButton(page, "Proceed").click();

        designPage.selectDesignSource(
                designData.getSource()).click();

        String designName =
                CommonMethods.generateUniqueTitle(
                        designData.getName());

        designPage.name().fill(designName);

        designPage.description().fill(
                designData.getDescription());

        designPage.create().click();

        page.waitForTimeout(2000);

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info(
                "========== Design Completed ==========");

        storeDesignExecutionData(designName);
    }

    private void storeDesignExecutionData(String designName) {

        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        FeatureExecutionData featureExecutionData =
                productExecutionData.getFeatures()
                        .get(productExecutionData.getFeatures().size() - 1);

        featureExecutionData.getDesignNames()
                .add(designName);
    }


}
