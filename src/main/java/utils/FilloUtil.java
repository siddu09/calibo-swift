package utils;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Enhanced, reusable Fillo utility for the Calibo SWIFT framework.
 *
 * Fillo treats an Excel workbook like a database:
 *   - Each SHEET  = a table
 *   - Each COLUMN = a field
 *   - Supports SELECT / INSERT / UPDATE / DELETE with WHERE, AND/OR, LIKE, comparison ops
 *   - Does NOT support JOINs (cross-sheet lookups are done in Java)
 *
 * This class centralizes the workbook path and exposes generic query helpers
 * so any test can read/filter/update Excel-driven data without boilerplate.
 */
public class FilloUtil {

    // Centralized path (was previously hardcoded in every method)
    private static final String WORKBOOK_PATH =
            "src/test/resources/testdata/TestData.xlsx";

    private FilloUtil() {
        // static utility
    }

    // ─────────────────────────────────────────────────────────────
    //  Connection
    // ─────────────────────────────────────────────────────────────

    private static Connection openConnection() {
        try {
            return new Fillo().getConnection(WORKBOOK_PATH);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to open Fillo connection to: " + WORKBOOK_PATH, e);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  Generic read helpers
    // ─────────────────────────────────────────────────────────────

    /**
     * Runs any SELECT query and returns each row as a column->value map.
     * The column list is read dynamically, so it works for ANY sheet.
     *
     * Example:
     *   getRows("SELECT * FROM Customers WHERE Tier='Premium' AND RunMode='Y'");
     */
    public static List<Map<String, String>> getRows(String query) {
        List<Map<String, String>> rows = new ArrayList<>();
        Connection connection = openConnection();
        Recordset recordset = null;
        try {
            recordset = connection.executeQuery(query);
            List<String> columns = recordset.getFieldNames();

            while (recordset.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (String column : columns) {
                    row.put(column, recordset.getField(column));
                }
                rows.add(row);
            }
        } catch (Exception e) {
            throw new RuntimeException("Fillo query failed: " + query, e);
        } finally {
            if (recordset != null) {
                recordset.close();
            }
            connection.close();
        }
        return rows;
    }

    /**
     * Returns a single scalar (first column of first row) — handy for COUNT-style
     * checks or fetching one value. Returns null if no rows.
     */
    public static String getSingleValue(String query, String column) {
        List<Map<String, String>> rows = getRows(query);
        if (rows.isEmpty()) {
            return null;
        }
        return rows.get(0).get(column);
    }

    /**
     * Converts a list of column names from a row-map query into TestNG's
     * Object[][] shape, selecting only the requested columns in order.
     *
     * Example (for a @DataProvider):
     *   toDataProvider(getRows("SELECT * FROM Customers WHERE RunMode='Y'"),
     *                  "CustomerId", "Name", "Email");
     */
    public static Object[][] toDataProvider(
            List<Map<String, String>> rows, String... columns) {

        Object[][] data = new Object[rows.size()][columns.length];
        for (int r = 0; r < rows.size(); r++) {
            Map<String, String> row = rows.get(r);
            for (int c = 0; c < columns.length; c++) {
                data[r][c] = row.get(columns[c]);
            }
        }
        return data;
    }

    // ─────────────────────────────────────────────────────────────
    //  Cross-sheet lookup (Fillo has no JOIN — we do it in Java)
    // ─────────────────────────────────────────────────────────────

    /**
     * Fetches a single related row from another sheet by a key match.
     * Simulates a JOIN, e.g. look up the Customer for a given Order.
     *
     * Example:
     *   lookup("Customers", "CustomerId", "C1001");
     */
    public static Map<String, String> lookup(
            String sheet, String keyColumn, String keyValue) {

        String query = "SELECT * FROM " + sheet
                + " WHERE " + keyColumn + "='" + keyValue + "'";
        List<Map<String, String>> rows = getRows(query);
        return rows.isEmpty() ? null : rows.get(0);
    }

    // ─────────────────────────────────────────────────────────────
    //  Write-back (INSERT / UPDATE)
    // ─────────────────────────────────────────────────────────────

    /**
     * Executes an INSERT / UPDATE / DELETE statement against the workbook.
     * Used to write test outcomes back into the TestExecution sheet.
     *
     * Example:
     *   executeUpdate("UPDATE TestExecution SET Result='PASS', ActualTotal='250.00' "
     *               + "WHERE TestCaseId='TC-ORD-01'");
     */
    public static void executeUpdate(String query) {
        Connection connection = openConnection();
        try {
            connection.executeUpdate(query);
        } catch (Exception e) {
            throw new RuntimeException("Fillo update failed: " + query, e);
        } finally {
            connection.close();
        }
    }

    // ─────────────────────────────────────────────────────────────
//  Backward-compatible login helpers (used by LoginTest)
// ─────────────────────────────────────────────────────────────

    /**
     * Prints all rows from the LoginData sheet (original behavior).
     */
    public static void readLoginData() {
        List<Map<String, String>> rows = getRows("SELECT * FROM LoginData");
        for (Map<String, String> row : rows) {
            LoggerUtil.LOGGER.info("Username = " + row.get("Username"));
            LoggerUtil.LOGGER.info("Password = " + row.get("Password"));
            LoggerUtil.LOGGER.info("Execute  = " + row.get("RunMode"));
            LoggerUtil.LOGGER.info("================================");
        }
    }

    /**
     * Returns active login rows (RunMode='Y') as Object[][] for a @DataProvider.
     * Original signature preserved so LoginTest compiles unchanged.
     */
    public static Object[][] getLoginData() {
        List<Map<String, String>> rows =
                getRows("SELECT * FROM LoginData WHERE RunMode='Y'");
        return toDataProvider(rows, "Username", "Password");
    }


    public static void executeUpdate(
            String workbookPath,
            String query) {

        Connection connection = null;

        try {
            connection =
                    new Fillo().getConnection(workbookPath);

            connection.executeUpdate(query);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Fillo update failed for workbook: "
                            + workbookPath
                            + "\nQuery: "
                            + query,
                    e
            );

        } finally {

            if (connection != null) {
                connection.close();
            }
        }
    }

    public static List<Map<String, String>> getRows(
            String workbookPath,
            String query) {

        List<Map<String, String>> rows = new ArrayList<>();

        Connection connection = null;
        Recordset recordset = null;

        try {
            connection = new Fillo().getConnection(workbookPath);
            recordset = connection.executeQuery(query);

            List<String> columns = recordset.getFieldNames();

            while (recordset.next()) {

                Map<String, String> row = new LinkedHashMap<>();

                for (String column : columns) {
                    row.put(column, recordset.getField(column));
                }

                rows.add(row);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Fillo query failed: " + query,
                    e
            );

        } finally {

            if (recordset != null) {
                recordset.close();
            }

            if (connection != null) {
                connection.close();
            }
        }

        return rows;
    }
}
