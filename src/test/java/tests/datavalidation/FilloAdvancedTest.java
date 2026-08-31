package tests.datavalidation;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.FilloUtil;
import utils.LoggerUtil;

import java.util.List;
import java.util.Map;

/**
 * Real-world, complex Fillo demonstration for the Calibo SWIFT framework.
 *
 * SCENARIO: E-commerce order validation.
 *
 * Assumes TestData.xlsx contains these sheets (tables):
 *
 *   Customers
 *   ┌────────────┬─────────────┬───────────────────────┬─────────┬─────────┬─────────┐
 *   │ CustomerId │ Name        │ Email                 │ Country │ Tier    │ RunMode │
 *   ├────────────┼─────────────┼───────────────────────┼─────────┼─────────┼─────────┤
 *   │ C1001      │ Rajeev      │ rajeev@calibo.com     │ India   │ Premium │ Y       │
 *   │ C1002      │ John        │ john@gmail.com        │ USA     │ Standard│ Y       │
 *   │ C1003      │ Meera       │ meera@calibo.com      │ India   │ Premium │ N       │
 *   │ C1004      │ Amit        │ amit@corp-mail.com    │ Germany │ Premium │ Y       │
 *   └────────────┴─────────────┴───────────────────────┴─────────┴─────────┴─────────┘
 *
 *   Orders
 *   ┌─────────┬────────────┬───────────┬──────────┬───────┬───────────┬─────────┐
 *   │ OrderId │ CustomerId │ Product   │ Quantity │ Price │ Status    │ RunMode │
 *   ├─────────┼────────────┼───────────┼──────────┼───────┼───────────┼─────────┤
 *   │ O5001   │ C1001      │ Laptop    │ 2        │ 500   │ CONFIRMED │ Y       │
 *   │ O5002   │ C1002      │ Mouse     │ 5        │ 20    │ CONFIRMED │ Y       │
 *   │ O5003   │ C1004      │ Monitor   │ 3        │ 300   │ PENDING   │ Y       │
 *   │ O5004   │ C1001      │ Keyboard  │ 1        │ 45    │ CANCELLED │ N       │
 *   └─────────┴────────────┴───────────┴──────────┴───────┴───────────┴─────────┘
 *
 *   TestExecution   (results are written BACK here)
 *   ┌────────────┬─────────┬───────────────┬─────────────┬────────┬────────────┐
 *   │ TestCaseId │ OrderId │ ExpectedTotal │ ActualTotal │ Result │ ExecutedOn │
 *   ├────────────┼─────────┼───────────────┼─────────────┼────────┼────────────┤
 *   │ TC-ORD-01  │ O5001   │ 1000          │             │        │            │
 *   │ TC-ORD-02  │ O5002   │ 100           │             │        │            │
 *   │ TC-ORD-03  │ O5003   │ 900           │             │        │            │
 *   └────────────┴─────────┴───────────────┴─────────────┴────────┴────────────┘
 *
 * Demonstrates:
 *   1. Multi-condition WHERE (AND / comparison operators)
 *   2. LIKE pattern matching
 *   3. Cross-sheet "JOIN" done in Java (Fillo has no JOIN)
 *   4. Numeric filtering + business-rule assertions
 *   5. Data-driven @DataProvider fed from Excel with RunMode filter
 *   6. Writing results BACK to Excel via UPDATE
 */
public class FilloAdvancedTest {

    // ─────────────────────────────────────────────────────────────
    //  DataProvider: only ACTIVE, CONFIRMED orders (RunMode='Y')
    // ─────────────────────────────────────────────────────────────

    @DataProvider(name = "activeConfirmedOrders")
    public Object[][] activeConfirmedOrders() {
        // WHERE with AND + string equality — Fillo handles this natively
        String query =
                "SELECT * FROM Orders "
                        + "WHERE RunMode='Y' AND Status='CONFIRMED'";

        List<Map<String, String>> rows = FilloUtil.getRows(query);

        LoggerUtil.LOGGER.info(
                "[FILLO] Active confirmed orders fetched: " + rows.size());

        return FilloUtil.toDataProvider(
                rows, "OrderId", "CustomerId", "Quantity", "Price");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 1: Cross-sheet validation + business rule + write-back
    // ─────────────────────────────────────────────────────────────

    @Test(dataProvider = "activeConfirmedOrders")
    public void validateOrderTotalForActiveOrders(
            String orderId, String customerId, String quantity, String price) {

        LoggerUtil.LOGGER.info("[FILLO] Validating order: " + orderId);

        // --- Cross-sheet "JOIN": look up the customer for this order ---
        Map<String, String> customer =
                FilloUtil.lookup("Customers", "CustomerId", customerId);

        Assert.assertNotNull(customer,
                "No customer found for order " + orderId + " (customerId=" + customerId + ")");

        // --- Business rule: compute expected total ---
        int qty = Integer.parseInt(quantity.trim());
        double unitPrice = Double.parseDouble(price.trim());
        double actualTotal = qty * unitPrice;

        LoggerUtil.LOGGER.info(String.format(
                "[FILLO] Order %s | Customer %s (%s, %s) | %d x %.2f = %.2f",
                orderId, customer.get("Name"), customer.get("Tier"),
                customer.get("Country"), qty, unitPrice, actualTotal));

        // --- Assert against the ExpectedTotal in the TestExecution sheet ---
        String expectedStr = FilloUtil.getSingleValue(
                "SELECT * FROM TestExecution WHERE OrderId='" + orderId + "'",
                "ExpectedTotal");

        Assert.assertNotNull(expectedStr,
                "No TestExecution row/ExpectedTotal for order " + orderId);

        double expectedTotal = Double.parseDouble(expectedStr.trim());
        String result = (Math.abs(expectedTotal - actualTotal) < 0.001) ? "PASS" : "FAIL";

        // --- Write the outcome BACK into Excel (UPDATE) ---
        String update = String.format(
                "UPDATE TestExecution SET ActualTotal='%.2f', Result='%s', ExecutedOn='%s' "
                        + "WHERE OrderId='%s'",
                actualTotal, result, java.time.LocalDate.now(), orderId);
        FilloUtil.executeUpdate(update);

        LoggerUtil.LOGGER.info("[FILLO] Wrote result back → " + orderId + " : " + result);

        Assert.assertEquals(actualTotal, expectedTotal, 0.001,
                "Order total mismatch for " + orderId);
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 2: LIKE pattern matching — corporate email customers
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateCorporateCustomersUsingLike() {
        // LIKE with wildcard — find all @calibo.com customers
        String query =
                "SELECT * FROM Customers WHERE Email LIKE '%@calibo.com'";

        List<Map<String, String>> corporate = FilloUtil.getRows(query);

        LoggerUtil.LOGGER.info(
                "[FILLO] Corporate (@calibo.com) customers: " + corporate.size());

        Assert.assertFalse(corporate.isEmpty(),
                "Expected at least one @calibo.com customer");

        // Every matched email must actually end with the corporate domain
        for (Map<String, String> c : corporate) {
            Assert.assertTrue(c.get("Email").endsWith("@calibo.com"),
                    "LIKE returned a non-corporate email: " + c.get("Email"));
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 3: Multi-condition filter — active premium customers
    //          from a specific country
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateActivePremiumIndianCustomers() {
        String query =
                "SELECT * FROM Customers "
                        + "WHERE Tier='Premium' AND Country='India' AND RunMode='Y'";

        List<Map<String, String>> customers = FilloUtil.getRows(query);

        LoggerUtil.LOGGER.info(
                "[FILLO] Active premium Indian customers: " + customers.size());

        // Assert the compound filter actually held for every returned row
        for (Map<String, String> c : customers) {
            Assert.assertEquals(c.get("Tier"), "Premium");
            Assert.assertEquals(c.get("Country"), "India");
            Assert.assertEquals(c.get("RunMode"), "Y");
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 4: Numeric business rule — high-value order detection
    // ─────────────────────────────────────────────────────────────

    @Test
    public void detectHighValueOrders() {
        // Read all active orders, then apply numeric business logic in Java
        List<Map<String, String>> orders =
                FilloUtil.getRows("SELECT * FROM Orders WHERE RunMode='Y'");

        double highValueThreshold = 500.0;
        int highValueCount = 0;

        for (Map<String, String> o : orders) {
            double lineTotal =
                    Integer.parseInt(o.get("Quantity").trim())
                            * Double.parseDouble(o.get("Price").trim());

            if (lineTotal >= highValueThreshold) {
                highValueCount++;
                LoggerUtil.LOGGER.info(String.format(
                        "[FILLO] HIGH-VALUE order %s → %.2f",
                        o.get("OrderId"), lineTotal));
            }
        }

        LoggerUtil.LOGGER.info(
                "[FILLO] High-value orders (>= " + highValueThreshold + "): " + highValueCount);

        Assert.assertTrue(highValueCount >= 1,
                "Expected at least one high-value order in the active set");
    }
}
