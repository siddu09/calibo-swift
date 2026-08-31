package UI.PRO.Features.Define.BuildingBlocks;

import UI.PRO.datahelper.DefineData;
import com.microsoft.playwright.Page;
import pages.PRO.Features.Define.DefinePage;
import testdatamanager.pro.FeatureExecutionData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;

public class DefineBuildingBlock {

    private final Page page;
    private final ProExecutionData executionData;

    private final DefinePage definePage;
    private String defineTitle;

    public DefineBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.definePage = new DefinePage(page);
    }

    public void createBusinessRequirement(DefineData defineData) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Define Started ==========");

        CommonMethods.clickOnTab(page, defineData.getSection());

        definePage.addNewBusinessRequirement(
                defineData.getType()).click();

        definePage.addNewBusinessRequirement("User").click();

        page.waitForTimeout(2000);

        this.defineTitle =
                CommonMethods.generateUniqueTitle(defineData.getTitle());

        definePage.title().fill(defineTitle);

        definePage.description().fill(defineData.getDescription());

        definePage.addUserName().fill(defineData.getUserName());

        definePage.selectDropDown(
                "Customer Name",
                defineData.getCustomerName());

        definePage.selectDropDown(
                "Priority",
                defineData.getPriority());

        definePage.selectDropDown(
                "Impact",
                defineData.getImpact());

        definePage.selectDropDown(
                "Status",
                defineData.getStatus());

        definePage.selectDropDown(
                "Source",
                defineData.getSource());

        definePage.create().click();

        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Define Completed ==========");

        storeBusinessRequirementExecutionData(defineTitle);
    }

    public void viewBusinessRequirement()
    {
        definePage.selectBusinessRequirement(this.defineTitle).click();
    }

    private void storeBusinessRequirementExecutionData(String defineTitle) {

        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        FeatureExecutionData featureExecutionData =
                productExecutionData.getFeatures()
                        .get(productExecutionData.getFeatures().size() - 1);

        featureExecutionData.getBusinessRequirementNames()
                .add(defineTitle);
    }
}