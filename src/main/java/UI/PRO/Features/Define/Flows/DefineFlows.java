package UI.PRO.Features.Define.Flows;

import UI.PRO.Features.Define.BuildingBlocks.DefineBuildingBlock;
import UI.PRO.Features.Define.Validations.DefineValidation;
import UI.PRO.datahelper.DefineData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;

public class DefineFlows {

    private final DefineBuildingBlock defineBuildingBlock;
    private final Page page;
    private final ProExecutionData executionData;

    public DefineFlows(
            Page page,
            ProExecutionData executionData) {

        this.page=page;
        this.executionData = executionData;
        defineBuildingBlock =
                new DefineBuildingBlock(
                        page,
                        executionData);
    }

    public void createUserFeedbackWithMandatoryFields() {

        DefineData defineData =
                ProTestData.getDefine(
                        "createUserFeedback");

        defineBuildingBlock
                .createBusinessRequirement(
                        defineData);

        ProValidation.validateSuccessMessage(
                page,
                "User Feedback added successfully."
        );

        defineBuildingBlock.viewBusinessRequirement();
        DefineValidation.validateBusinessRequirementDetails(
                page,
                executionData,
                defineData);

    }
}