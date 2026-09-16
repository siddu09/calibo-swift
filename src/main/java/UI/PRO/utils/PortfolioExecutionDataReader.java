package UI.PRO.utils;

import UI.PRO.datahelper.PortfolioData;
import org.testng.Assert;
import utils.FilloUtil;
import utils.LoggerUtil;

public final class PortfolioExecutionDataReader {

    private PortfolioExecutionDataReader() {
    }

    public static PortfolioData readPortfolioForDeletion(String sheet, String testCase) {
        var rows = FilloUtil.getRows(
                "src/test/resources/output/pro/ProExecutionData.xlsx",
                "SELECT PortfolioName, PublicPortfolio FROM " + sheet
                        + " WHERE TestCase='" + testCase.replace("'", "''") + "'");
        Assert.assertEquals(rows.size(), 1, "Expected one Excel row for " + testCase);
        String portfolioName = rows.get(0).get("PORTFOLIONAME");
        String publicPortfolio = rows.get(0).get("PUBLICPORTFOLIO");
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
}
