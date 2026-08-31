package UI.PRO.Features.Feature.Validations;

import UI.PRO.datahelper.FeatureData;
import com.microsoft.playwright.Page;
import pages.PRO.Features.Feature.FeatureDetailsPage;
import testdatamanager.pro.ProExecutionData;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class FeatureValidation {

    private FeatureValidation() {
        // Utility class
    }

    public static void validateFeatureDetails(
            Page page,
            ProExecutionData executionData,
            FeatureData featureData) {

        FeatureDetailsPage featureDetailsPage =
                new FeatureDetailsPage(page);

        String currentFeatureName =
                executionData.getProducts()
                        .get(
                                executionData.getProducts().size() - 1
                        )
                        .getFeatures()
                        .get(
                                executionData.getProducts()
                                        .get(
                                                executionData.getProducts().size() - 1
                                        )
                                        .getFeatures()
                                        .size() - 1
                        )
                        .getFeatureName();

        // Validate current Feature name
        assertThat(
                featureDetailsPage.fetchFeatureName()
        ).hasText(
                currentFeatureName
        );

        // Validate Feature description
        assertThat(
                featureDetailsPage.fetchFeatureDescription()
        ).hasText(
                featureData.getDescription()
        );

        // Validate Feature status
        assertThat(
                featureDetailsPage.fetchFeatureStatus()
        ).hasText(
                featureData.getStatus()
        );
    }
}