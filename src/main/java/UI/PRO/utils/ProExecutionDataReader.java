package UI.PRO.utils;

import UI.PRO.datahelper.PortfolioData;
import UI.PRO.datahelper.ProductData;
import org.testng.Assert;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;
import utils.FilloUtil;
import utils.LoggerUtil;

public final class ProExecutionDataReader {

    private ProExecutionDataReader() {
    }

    public static PortfolioData readPortfolioForDeletion(String sheet, String testCase) {
        var rows = FilloUtil.getRows(
                "src/test/resources/output/pro/ProExecutionData.xlsx",
                "SELECT PortfolioName, PublicPortfolio FROM " + sheet
                        + " WHERE TestCase='" + testCase.replace("'", "''") + "'");
        Assert.assertEquals(rows.size(), 1, "Expected one Excel row for " + testCase);
        String portfolioName = rows.get(0).get("PortfolioName");
        String publicPortfolio = rows.get(0).get("PublicPortfolio");
        Assert.assertTrue(portfolioName != null && !portfolioName.isBlank(),
                "PortfolioName is empty in Excel for " + testCase);
        Assert.assertTrue(publicPortfolio != null
                        && publicPortfolio.trim().matches("(?i)true|false|1|0"),
                "Invalid PublicPortfolio value in Excel for " + testCase);
        boolean isPublic = "true".equalsIgnoreCase(publicPortfolio.trim())
                || "1".equals(publicPortfolio.trim());
        LoggerUtil.LOGGER.info("[Portfolio test] Excel row: {} | Portfolio: {} | Public: {}",
                testCase, portfolioName, isPublic);
        PortfolioData portfolioData = new PortfolioData();
        portfolioData.setName(portfolioName);
        portfolioData.setPublicPortfolio(isPublic);
        return portfolioData;
    }

    public static void loadProductForDeletion(ProductData data, ProExecutionData executionData) {
        var rows = FilloUtil.getRows(data.getExecutionDataFile(),
                "SELECT PortfolioName, ProductName FROM " + data.getResultSheet()
                        + " WHERE TestCase='" + data.getExecutionSourceTestCase().replace("'", "''") + "'");
        Assert.assertEquals(rows.size(), 1, data.getSourceRowCountMessage());
        String productName = rows.getFirst().get("PRODUCTNAME");
        String portfolioName = rows.getFirst().get("PORTFOLIONAME");
        Assert.assertTrue(productName != null && !productName.isBlank(), data.getMissingSourceProductMessage());
        Assert.assertFalse(productName.contains(","), data.getAmbiguousSourceProductMessage());
        Assert.assertTrue(portfolioName != null && !portfolioName.isBlank(), data.getMissingSourcePortfolioMessage());
        ProductExecutionData product = new ProductExecutionData();
        product.setProductName(productName.trim());
        executionData.setPortfolioName(portfolioName.trim());
        executionData.getProducts().add(product);
    }
}
