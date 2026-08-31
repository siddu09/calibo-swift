package UI.PRO.Features.Design.Flows;

import UI.PRO.Features.Design.BuildingBlocks.DesignBuildingBlock;
import UI.PRO.Features.Design.Validations.DesignValidation;
import UI.PRO.datahelper.DesignData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import pages.PRO.Features.Design.DesignPage;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DesignFlows {

    private final DesignBuildingBlock designBuildingBlock;
    private final Page page;
    private final ProExecutionData executionData;

    public DesignFlows(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        designBuildingBlock =
                new DesignBuildingBlock(
                        page,
                        executionData);
    }

    public void createDesignWithMandatoryFields() {

        DesignData designData =
                ProTestData.getDesign(
                        "createDesign");

        designBuildingBlock
                .createDesign(
                        designData);

        ProValidation.validateSuccessMessage(
                page,
                "Design artifact added successfully."
        );


        DesignPage designPage = new DesignPage(page);

        DesignValidation.validateDesignDetails(
                page,
                executionData,
                designData);

    }
}