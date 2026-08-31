package tests.database;

import databaseHandler.H2DatabaseManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerUtil;

import java.util.List;
import java.util.Map;

/**
 * Real-world DB validation suite for the Calibo SWIFT framework, running against
 * an EMBEDDED H2 database (no server, no network, no credentials — works behind
 * corporate firewalls).
 *
 * SCENARIO: A small e-commerce schema (customers + orders) that the test itself
 * seeds, then validates with the kinds of assertions a real QA engineer writes:
 *   - Row-count validation
 *   - Single-value validation
 *   - Referential integrity (no orphan orders)
 *   - Aggregation / business-rule validation (revenue, top customer)
 *   - Data-quality checks (no NULL emails, no negative amounts)
 *
 * @BeforeClass builds a fresh schema each run, so the suite is fully repeatable
 * and CI-safe.
 */
public class DatabaseValidationTest {

    @BeforeClass
    public void seedDatabase() throws Exception {
        LoggerUtil.LOGGER.info("[H2] Seeding sample schema + data");

        // Fresh slate each run
        H2DatabaseManager.execute("DROP TABLE IF EXISTS orders");
        H2DatabaseManager.execute("DROP TABLE IF EXISTS customers");

        H2DatabaseManager.execute("""
                CREATE TABLE customers (
                    customer_id INT PRIMARY KEY,
                    name        VARCHAR(100) NOT NULL,
                    email       VARCHAR(150),
                    country     VARCHAR(50),
                    tier        VARCHAR(20)
                )
                """);

        H2DatabaseManager.execute("""
                CREATE TABLE orders (
                    order_id    INT PRIMARY KEY,
                    customer_id INT,
                    product     VARCHAR(100),
                    amount      DECIMAL(10,2),
                    status      VARCHAR(20),
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
                )
                """);

        H2DatabaseManager.execute("""
                INSERT INTO customers VALUES
                    (1, 'Rajeev', 'rajeev@calibo.com',  'India',   'Premium'),
                    (2, 'John',   'john@gmail.com',     'USA',     'Standard'),
                    (3, 'Meera',  'meera@calibo.com',   'India',   'Premium'),
                    (4, 'Amit',   'amit@corp.com',      'Germany', 'Premium')
                """);

        H2DatabaseManager.execute("""
                INSERT INTO orders VALUES
                    (5001, 1, 'Laptop',   1000.00, 'CONFIRMED'),
                    (5002, 2, 'Mouse',      40.00, 'CONFIRMED'),
                    (5003, 4, 'Monitor',   900.00, 'PENDING'),
                    (5004, 1, 'Keyboard',   45.00, 'CONFIRMED'),
                    (5005, 3, 'Docking',   250.00, 'CONFIRMED')
                """);

        LoggerUtil.LOGGER.info("[H2] Seed complete");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 1: Row-count validation
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateCustomerCount() throws Exception {
        int count = H2DatabaseManager.getRowCount("SELECT COUNT(*) FROM customers");
        LoggerUtil.LOGGER.info("[H2] Customer count = " + count);
        Assert.assertEquals(count, 4, "Expected exactly 4 customers");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 2: Single-value validation
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateCustomerNameById() throws Exception {
        String name = H2DatabaseManager.getSingleValue(
                "SELECT name FROM customers WHERE customer_id = 1");
        Assert.assertEquals(name, "Rajeev", "Customer 1 should be Rajeev");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 3: Referential integrity — no orphan orders
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateNoOrphanOrders() throws Exception {
        int orphans = H2DatabaseManager.getRowCount("""
                SELECT COUNT(*) FROM orders o
                LEFT JOIN customers c ON o.customer_id = c.customer_id
                WHERE c.customer_id IS NULL
                """);
        LoggerUtil.LOGGER.info("[H2] Orphan orders = " + orphans);
        Assert.assertEquals(orphans, 0, "Found orders with no matching customer");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 4: Aggregation / business rule — total confirmed revenue
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateConfirmedRevenue() throws Exception {
        String revenue = H2DatabaseManager.getSingleValue("""
                SELECT SUM(amount) FROM orders WHERE status = 'CONFIRMED'
                """);
        double total = Double.parseDouble(revenue);
        LoggerUtil.LOGGER.info("[H2] Confirmed revenue = " + total);
        // 1000 + 40 + 45 + 250 = 1335.00
        Assert.assertEquals(total, 1335.00, 0.001, "Confirmed revenue mismatch");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 5: JOIN + GROUP BY — highest-spending customer
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateTopSpendingCustomer() throws Exception {
        List<Map<String, String>> rows = H2DatabaseManager.executeQuery("""
                SELECT c.name AS name, SUM(o.amount) AS total_spent
                FROM customers c
                JOIN orders o ON c.customer_id = o.customer_id
                GROUP BY c.name
                ORDER BY total_spent DESC
                LIMIT 1
                """);

        Assert.assertFalse(rows.isEmpty(), "Expected a top spender row");
        Map<String, String> top = rows.get(0);
        LoggerUtil.LOGGER.info("[H2] Top spender = " + top.get("NAME")
                + " (" + top.get("TOTAL_SPENT") + ")");
        // Rajeev = 1000 + 45 = 1045 → highest
        Assert.assertEquals(top.get("NAME"), "Rajeev", "Top spender should be Rajeev");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 6: Data-quality — no NULL emails, no negative amounts
    // ─────────────────────────────────────────────────────────────

    @Test
    public void validateDataQuality() throws Exception {
        int nullEmails = H2DatabaseManager.getRowCount(
                "SELECT COUNT(*) FROM customers WHERE email IS NULL");
        Assert.assertEquals(nullEmails, 0, "Found customers with NULL email");

        int negativeAmounts = H2DatabaseManager.getRowCount(
                "SELECT COUNT(*) FROM orders WHERE amount < 0");
        Assert.assertEquals(negativeAmounts, 0, "Found orders with negative amount");
    }
}
