package UI.PRO.Features.Design.Validations;

import UI.PRO.datahelper.DesignData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.PRO.Features.Design.DesignPage;
import testdatamanager.pro.FeatureExecutionData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class DesignValidation {

    private DesignValidation() {
    }

    public static void validateDesignCategory(
            Page page,
            String expectedCategory) {

        DesignPage designPage = new DesignPage(page);

        assertThat(designPage.designCategory(expectedCategory))
                .isVisible();
    }

    public static void validateDesignDetails(
            Page page,
            ProExecutionData executionData,
            DesignData designData) {

        DesignPage designPage = new DesignPage(page);

        String expectedDesignName = getLastCreatedDesignName(executionData);

        String expectedDescription = designData.getDescription();
        String expectedCategory = designData.getCategory();

        // 1. Validate category
        assertThat(
                designPage.designCategory(expectedCategory)
        ).hasText(expectedCategory);

        // 2. Expand category
        designPage.expandCategory(expectedCategory).click();

        // 3. Click the actual newly-created design
        designPage.designCard(expectedDesignName).click();

        // 4. Validate title
        assertThat(
                page.locator("div.sheet-body .row h2").first()
        ).hasText(expectedDesignName);

        // 5. Validate description
        assertThat(
                page.locator("div.tab-pane.active label")
                        .filter(new Locator.FilterOptions().setHasText("Description"))
                        .locator("..")
                        .locator("h5")
        ).hasText(expectedDescription);
    }

    private static String getLastCreatedDesignName(ProExecutionData executionData) {

        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        FeatureExecutionData featureExecutionData =
                productExecutionData.getFeatures()
                        .get(productExecutionData.getFeatures().size() - 1);

        return featureExecutionData.getDesignNames()
                .get(featureExecutionData.getDesignNames().size() - 1);
    }
}