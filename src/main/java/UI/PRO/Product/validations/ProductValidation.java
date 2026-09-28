package UI.PRO.Product.validations;

import UI.PRO.datahelper.ProductData;
import UI.PRO.datahelper.ProductData.ProductAllocationData;
import pages.PRO.Product.ProductTeamsPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions.HasTextOptions;
import pages.PRO.Product.ProductAdditionalDetailsOverviewTabPage;
import pages.PRO.Product.ProductDetailsPage;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class ProductValidation {

    private ProductValidation() {
        // Utility class
    }

    public static void validateProductOverview(Page page, ProductData data) {
        new ProductAdditionalDetailsOverviewTabPage(page).tab(data.getOverviewTab()).click();
        ProductDetailsPage details = new ProductDetailsPage(page);
        assertThat(details.visibility(data.getPublicLabel())).isVisible();
        assertThat(details.owner(data.getOwner())).isVisible();
        data.getExpectedOverviewFields().forEach((label, expected) ->
                assertThat(details.overviewValue(label)).hasText(expected,
                        new HasTextOptions().setUseInnerText(true)));
        assertThat(details.documentationLink(data.getDocumentationLinkText()))
                .hasAttribute("href", data.getDocumentationLinkUrl());
        assertThat(details.overviewValue(data.getDynamicFieldName())).hasText(data.getDynamicFieldValue());
    }

    public static void validateReleaseObjective(ProductData.ProductReleaseData data) {
        org.testng.Assert.assertNotNull(data.getObjective(), data.getObjectiveValidationMessage());
        org.testng.Assert.assertTrue(data.getObjective().length() <= data.getObjectiveMaxLength()
                        && data.getObjective().matches(data.getObjectivePattern()),
                data.getObjectiveValidationMessage());
    }

    public static void validateProductAllocation(Page page, ProductAllocationData allocation) {
        ProductTeamsPage teamsPage = new ProductTeamsPage(page);
        assertThat(teamsPage.allocationRow(allocation.getName())).isVisible();
        if (allocation.getRole() != null) {
            assertThat(teamsPage.allocationCell(allocation.getName(), allocation.getRole()))
                    .hasText(allocation.getRole());
        }
        assertThat(teamsPage.allocationCell(allocation.getName(), allocation.getExpectedAllocation()))
                .hasText(allocation.getExpectedAllocation());
        assertThat(teamsPage.allocationCell(allocation.getName(), allocation.getExpectedPeriod()))
                .hasText(allocation.getExpectedPeriod());
    }

    public static void validateProductDetails(
            Page page,
            ProExecutionData executionData,
            ProductData productData) {

        ProductDetailsPage productDetailsPage =
                new ProductDetailsPage(page);

        ProductExecutionData currentProduct =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        // Validate the product that was just created
        assertThat(
                productDetailsPage.fetchProductName()
        ).hasText(
                currentProduct.getProductName()
        );

        // Validate correct Portfolio
        assertThat(
                productDetailsPage.fetchProductPortfolio()
        ).hasText(
                executionData.getPortfolioName()
        );

        // Validate Business Group
        assertThat(
                productDetailsPage.fetchBusinessGroup()
        ).hasText(
                productData.getBusinessGroup()
        );

        // Validate Description
        assertThat(
                productDetailsPage.fetchProductDescription()
        ).hasText(
                productData.getDescription()
        );
    }
}
