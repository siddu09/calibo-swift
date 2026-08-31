package UI.PRO.Features.Feature.Flows;

import UI.PRO.Features.Feature.BuildingBlocks.FeatureBuildingBlock;
import UI.PRO.Features.Feature.Validations.FeatureValidation;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.datahelper.FeatureData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;

public class FeatureFlows {

    private final FeatureBuildingBlock featureBuildingBlock;
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

}