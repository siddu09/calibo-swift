package UI.PRO.Features.Feature.Flows;

import UI.PRO.CommonProValidations.ProValidation;
import UI.PRO.datahelper.ProductData;
import UI.PRO.Features.Feature.BuildingBlocks.FeatureBuildingBlock;
import pages.PRO.Features.Feature.FeaturePage;
import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Features.Feature.Validations.FeatureValidation;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.datahelper.FeatureData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import utils.CommonMethods;

public class FeatureFlows {

    private final FeatureBuildingBlock featureBuildingBlock;
    private final ProductBuildingBlock productBuildingBlock;
    private final Page page;
    private final ProExecutionData executionData;
    private final ProductFlows productFlows;


    public FeatureFlows(
            Page page,
            ProExecutionData executionData) {
        this.page = page;
        this.executionData = executionData;
        featureBuildingBlock =
                new FeatureBuildingBlock(
                        page,
                        executionData);
        productBuildingBlock = new ProductBuildingBlock(page, executionData);
        productFlows = new ProductFlows(page, executionData);
    }


    public FeatureFlows(Page page, ProExecutionData executionData,
            FeaturePage featurePage) {
        this.page = page;
        this.executionData = executionData;
        featureBuildingBlock = new FeatureBuildingBlock(page, executionData, featurePage);
        productBuildingBlock = new ProductBuildingBlock(page, executionData);
        productFlows = new ProductFlows(page, executionData);
    }

    @Step("Create feature with mandatory fields")
    public void createFeatureWithMandatoryFields() {

        productFlows.navigateToFeatureTab();

        FeatureData featureData =
                ProTestData.getFeature("createFeature");

        featureBuildingBlock
                .createFeature(featureData);

        featureBuildingBlock
                .skipOrAddFeatureAdditionalDetails(
                        "No, I will add details later");

        viewFeatureDetails();
    }

    public void createFeatureWithMandatoryFieldsAndAddDevelop() {

        productFlows.navigateToFeatureTab();

        FeatureData featureData =
                ProTestData.getFeature("createFeature");

        featureBuildingBlock
                .createFeature(featureData);

        featureBuildingBlock
                .skipOrAddFeatureAdditionalDetails(
                        "No, I will add details later");

        viewFeatureDetails();
        selectStage("Develop");
    }

    public void createFeatureWithMandatoryFieldsAndAddDevelopOnSharedProduct(String productName) {

        productBuildingBlock.navigateToProductPage();
        productBuildingBlock.searchProduct(productName);
        productBuildingBlock.selectProduct(productName);


        productFlows.navigateToFeatureTab();

        FeatureData featureData = ProTestData.getFeature("createFeature");

        featureBuildingBlock.createFeatureUnderSharedProduct(featureData);

        featureBuildingBlock.skipOrAddFeatureAdditionalDetails("No, I will add details later");

        viewFeatureDetails();
        selectStage("Develop");
    }

    public void viewFeatureDetails() {
        FeatureData featureData =
                ProTestData.getFeature("createFeature");
        featureBuildingBlock
                .viewFeatureDetails();
        FeatureValidation.validateFeatureDetails(
                page,
                executionData,
                featureData
        );
    }

    public void selectStage(String stage) {

        featureBuildingBlock
                .selectStage(stage);
    }
    /**
     * Overload accepting explicit FeatureData instead of loading via ProTestData.
     * Existing no-arg createFeatureWithMandatoryFields() is untouched.
     */
    public void createFeatureWithMandatoryFields(FeatureData featureData) {
        featureBuildingBlock.createFeature(featureData);
        featureBuildingBlock.skipOrAddFeatureAdditionalDetails("No, I will add details later");
    }

    @Step("View and validate the feature created with supplied test data")
    public void viewFeatureDetails(FeatureData featureData) {
        featureBuildingBlock.viewFeatureDetails();
        featureBuildingBlock.validateCreatedFeature(featureData);
    }

    @Step("Delete the created feature and verify its success message")
    public void deleteFeature(ProductData data) {
        featureBuildingBlock.deleteFeature(data);
        ProValidation.validateSuccessMessage(page, data.getFeatureDeletedMessage());
    }

}
