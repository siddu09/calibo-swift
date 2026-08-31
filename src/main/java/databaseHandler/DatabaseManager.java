package databaseHandler;

import configHandler.ConfigManager;
import utils.LoggerUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private DatabaseManager() {
    }

    public static Connection getOracleConnection()
            throws Exception {


        LoggerUtil.LOGGER.info(
                "Connecting to Oracle Database");
        String url =
                ConfigManager.getDBProperty(
                        "oracle.url");

        String username =
                ConfigManager.getDBProperty(
                        "oracle.username");

        String password =
                ConfigManager.getDBProperty(
                        "oracle.password");

        if (url == null || url.isBlank()) {

            throw new RuntimeException(
                    "Oracle URL is missing");
        }

        Connection connection =
                DriverManager.getConnection(
                        url,
                        username,
                        password);

        LoggerUtil.LOGGER.info(
                "Oracle Connection Established");

        return connection;
    }

    public static void closeConnection(
            Connection connection) {

        try {

            if (connection != null &&
                    !connection.isClosed()) {

                connection.close();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static int getRowCount(
            String query)
            throws Exception {

        try (Connection connection =
                     getOracleConnection();

             Statement statement =
                     connection.createStatement();

             ResultSet resultSet =
                     statement.executeQuery(query)) {

            resultSet.next();

            return resultSet.getInt(1);
        }
    }

    public static String getSingleValue(
            String query)
            throws Exception {

        try (Connection connection =
                     getOracleConnection();

             Statement statement =
                     connection.createStatement();

             ResultSet resultSet =
                     statement.executeQuery(query)) {

            if (resultSet.next()) {

                return resultSet.getString(1);
            }

            return null;
        }
    }

    public static List<Map<String, String>>
    executeQuery(
            String query)
            throws Exception {

        List<Map<String, String>> rows =
                new ArrayList<>();

        try (Connection connection =
                     getOracleConnection();

             Statement statement =
                     connection.createStatement();

             ResultSet resultSet =
                     statement.executeQuery(query)) {

            ResultSetMetaData metaData =
                    resultSet.getMetaData();

            int columnCount =
                    metaData.getColumnCount();

            while (resultSet.next()) {

                Map<String, String> row =
                        new HashMap<>();

                for (int i = 1;
                     i <= columnCount;
                     i++) {

                    row.put(
                            metaData.getColumnName(i),
                            resultSet.getString(i));
                }

                rows.add(row);
            }
        }

        return rows;
    }
}