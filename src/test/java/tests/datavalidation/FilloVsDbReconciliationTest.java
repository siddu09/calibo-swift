package tests.datavalidation;

import databaseHandler.H2DatabaseManager;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import reporting.DataReconciler;
import reporting.ReconciliationResult;
import utils.FilloUtil;
import utils.LoggerUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FilloVsDbReconciliationTest {

    @BeforeClass
    public void seedDatabase() throws Exception {
        LoggerUtil.LOGGER.info("[RECON] Seeding H2 'orders' (actual data)");

        H2DatabaseManager.execute("DROP TABLE IF EXISTS orders");
        H2DatabaseManager.execute("""
                CREATE TABLE orders (
                    order_id INT PRIMARY KEY,
                    product  VARCHAR(100),
                    amount   DECIMAL(10,2),
                    status   VARCHAR(20)
                )
                """);
        H2DatabaseManager.execute("""
                INSERT INTO orders VALUES
                    (5001, 'Laptop',   1000.00, 'CONFIRMED'),
                    (5002, 'Mouse',      40.00, 'CONFIRMED'),
                    (5003, 'Monitor',   900.00, 'PENDING'),
                    (5004, 'Keyboard',   45.00, 'CONFIRMED'),
                    (5005, 'Docking',   250.00, 'CONFIRMED')
                """);
    }

    @Test
    public void reconcileExpectedOrdersWithDatabase() throws Exception {

        List<Map<String, String>> expected =
                FilloUtil.getRows("SELECT * FROM ExpectedOrders");

        List<Map<String, String>> actual =
                H2DatabaseManager.executeQuery(
                        "SELECT order_id, product, amount, status FROM orders");

        Map<String, String> fieldMapping = new LinkedHashMap<>();
        fieldMapping.put("Product", "PRODUCT");
        fieldMapping.put("Amount",  "AMOUNT");
        fieldMapping.put("Status",  "STATUS");

        ReconciliationResult result = DataReconciler.reconcile(
                expected, actual, "OrderId", "ORDER_ID", fieldMapping);

        LoggerUtil.LOGGER.info("\n" + result.summary());

        // 📊 Attach the human-readable summary to the Allure report
        Allure.addAttachment(
                "Reconciliation Report",
                "text/plain",
                result.summary());

        // 📊 Attach a CSV of discrepancies (empty when fully reconciled)
        Allure.addAttachment(
                "Discrepancies (CSV)",
                "text/csv",
                buildDiscrepancyCsv(result));

        Assert.assertEquals(result.getExpectedTotal(), result.getActualTotal(),
                "Row-count mismatch between Excel and DB");
        Assert.assertTrue(result.isReconciled(),
                "Reconciliation found discrepancies:\n" + result.summary());
        Assert.assertEquals(result.getMatchedRecords(), result.getExpectedTotal(),
                "Not all records matched");
    }

    @Test
    public void reconcileRowCounts() throws Exception {
        int excelCount = FilloUtil.getRows("SELECT * FROM ExpectedOrders").size();
        int dbCount = H2DatabaseManager.getRowCount("SELECT COUNT(*) FROM orders");

        LoggerUtil.LOGGER.info("[RECON] Excel rows=" + excelCount + " DB rows=" + dbCount);

        // 📊 Attach the row-count comparison
        Allure.addAttachment(
                "Row Count Comparison",
                "text/plain",
                "Excel (ExpectedOrders) : " + excelCount + "\n"
                        + "DB (orders)            : " + dbCount + "\n"
                        + "Status                 : "
                        + (excelCount == dbCount ? "MATCH" : "MISMATCH"));

        Assert.assertEquals(excelCount, dbCount, "Excel and DB row counts differ");
    }

    @Test
    public void reconcileTotalOrderValue() throws Exception {
        double expectedTotal = FilloUtil.getRows("SELECT * FROM ExpectedOrders").stream()
                .mapToDouble(r -> Double.parseDouble(r.get("Amount").trim()))
                .sum();

        double actualTotal = Double.parseDouble(
                H2DatabaseManager.getSingleValue("SELECT SUM(amount) FROM orders"));

        LoggerUtil.LOGGER.info(String.format(
                "[RECON] Total value — Excel=%.2f DB=%.2f", expectedTotal, actualTotal));

        // 📊 Attach the financial reconciliation
        Allure.addAttachment(
                "Total Value Reconciliation",
                "text/plain",
                String.format(
                        "Expected total (Excel) : %.2f%n"
                                + "Actual total   (DB)    : %.2f%n"
                                + "Difference             : %.2f%n"
                                + "Status                 : %s",
                        expectedTotal, actualTotal,
                        Math.abs(expectedTotal - actualTotal),
                        Math.abs(expectedTotal - actualTotal) < 0.001 ? "MATCH" : "MISMATCH"));

        Assert.assertEquals(actualTotal, expectedTotal, 0.001,
                "Total order value mismatch between Excel and DB");
    }

    // ─────────────────────────────────────────────────────────────
    //  Helper: build a CSV of discrepancies for the Allure attachment
    // ─────────────────────────────────────────────────────────────

    private String buildDiscrepancyCsv(ReconciliationResult result) {
        StringBuilder csv = new StringBuilder();
        csv.append("Key,Type,Field,Expected,Actual\n");
        for (ReconciliationResult.Discrepancy d : result.getDiscrepancies()) {
            csv.append(nz(d.key)).append(",")
                    .append(nz(d.type)).append(",")
                    .append(nz(d.field)).append(",")
                    .append(nz(d.expected)).append(",")
                    .append(nz(d.actual)).append("\n");
        }
        if (result.getDiscrepancies().isEmpty()) {
            csv.append("(none - fully reconciled),,,,\n");
        }
        return csv.toString();
    }

    private String nz(String s) {
        return s == null ? "" : s;
    }
}