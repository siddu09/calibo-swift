package databaseHandler;

import utils.LoggerUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * H2-based DB validation manager for the Calibo SWIFT framework.
 *
 * WHY H2:
 *   - Embedded Java database — runs inside the JVM, NO server, NO network, NO credentials.
 *   - Perfect for enterprise/locked-down environments (e.g. Calibo) where remote
 *     sandbox DBs are blocked by firewall/proxy.
 *   - Speaks standard JDBC + SQL, so it mirrors how the real Oracle DatabaseManager works.
 *
 * MODE:
 *   Uses a FILE-based H2 DB (persists between runs) at ./reports/h2/swiftdb.
 *   Switch to in-memory by using: jdbc:h2:mem:swiftdb;DB_CLOSE_DELAY=-1
 *
 * This class intentionally mirrors your existing DatabaseManager API
 * (getRowCount / getSingleValue / executeQuery) so tests feel identical
 * to real Oracle validation.
 */
public class H2DatabaseManager {

    // File-based H2 — auto-creates the DB file on first use. No install needed.
    private static final String JDBC_URL =
            "jdbc:h2:./reports/h2/swiftdb;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private H2DatabaseManager() {
    }

    public static Connection getConnection() throws Exception {
        // H2 driver auto-registers, but this makes the dependency explicit.
        Class.forName("org.h2.Driver");
        Connection connection =
                DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        LoggerUtil.LOGGER.info("[H2] Connection established: " + JDBC_URL);
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[H2] Failed to close connection", e);
        }
    }

    /** Run a DDL/DML statement (CREATE, INSERT, UPDATE, DELETE). */
    public static void execute(String sql) throws Exception {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    /** Returns a single integer count (e.g. SELECT COUNT(*) ...). */
    public static int getRowCount(String query) throws Exception {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    /** Returns the first column of the first row as a String (or null). */
    public static String getSingleValue(String query) throws Exception {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        }
    }

    /** Returns the full result set as a list of column->value maps. */
    public static List<Map<String, String>> executeQuery(String query) throws Exception {
        List<Map<String, String>> rows = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                rows.add(row);
            }
        }
        return rows;
    }
}
