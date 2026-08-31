package UI.PRO.Product.validations;

import UI.PRO.datahelper.ProductData;
import com.microsoft.playwright.Page;
import pages.PRO.Product.ProductDetailsPage;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class ProductValidation {

    private ProductValidation() {
        // Utility class
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