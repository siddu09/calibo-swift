package UI.PRO.ProductPortfolio.validations;

import UI.PRO.datahelper.PortfolioData;
import com.microsoft.playwright.Page;
import pages.PRO.ProductPortfolio.ProductPortfolioViewPage;
import testdatamanager.pro.ProExecutionData;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class PortfolioValidation {

    private PortfolioValidation() {
        // Utility class
    }

    public static void validatePortfolioDetails(
            Page page,
            ProExecutionData executionData,
            PortfolioData portfolioData) {

        ProductPortfolioViewPage productPortfolioViewPage =
                new ProductPortfolioViewPage(page);

        // Actual/generated portfolio name
        assertThat(
                productPortfolioViewPage.fetchPortfolioName()
        ).hasText(
                executionData.getPortfolioName()
        );

        // Description remains the same as test data
        assertThat(
                productPortfolioViewPage.fetchPortfolioDescription()
        ).hasText(
                portfolioData.getDescription()
        );
    }

    public static void validatePortfolioLogo(Page page) {
        ProductPortfolioViewPage productPortfolioViewPage =
                new ProductPortfolioViewPage(page);

        assertThat(productPortfolioViewPage.portfolioLogo()).isVisible();
    }
}
