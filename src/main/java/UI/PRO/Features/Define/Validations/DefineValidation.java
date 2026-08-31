package UI.PRO.Features.Define.Validations;

import UI.PRO.datahelper.DefineData;
import com.microsoft.playwright.Page;
import pages.PRO.Features.Define.BusinessRequirementDetailsPage;
import testdatamanager.pro.ProExecutionData;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class DefineValidation {

    private DefineValidation() {
        // Utility class
    }

    public static void validateBusinessRequirementDetails(
            Page page,
            ProExecutionData executionData,
            DefineData defineData) {

        BusinessRequirementDetailsPage
                businessRequirementDetailsPage =
                new BusinessRequirementDetailsPage(page);

        // Get the currently created Business Requirement
        var currentProduct =
                executionData.getProducts()
                        .get(
                                executionData.getProducts().size() - 1
                        );

        var currentFeature =
                currentProduct.getFeatures()
                        .get(
                                currentProduct.getFeatures().size() - 1
                        );

        String currentBusinessRequirementTitle =
                currentFeature
                        .getBusinessRequirementNames()
                        .get(
                                currentFeature
                                        .getBusinessRequirementNames()
                                        .size() - 1
                        );

        // Validate Business Requirement Title
        assertThat(
                businessRequirementDetailsPage
                        .fetchBusinessRequirementTitle()
        ).hasText(
                currentBusinessRequirementTitle
        );

        // Validate Priority
        assertThat(
                businessRequirementDetailsPage
                        .fetchPriority()
        ).hasText(
                defineData.getPriority()
        );

        // Validate Source
        assertThat(
                businessRequirementDetailsPage
                        .fetchSource()
        ).hasText(
                defineData.getSource()
        );

        // Validate Impact
        assertThat(
                businessRequirementDetailsPage
                        .fetchImpact()
        ).hasText(
                defineData.getImpact()
        );

        // Validate Description
        assertThat(
                businessRequirementDetailsPage
                        .fetchDescription()
        ).hasText(
                defineData.getDescription()
        );
    }
}