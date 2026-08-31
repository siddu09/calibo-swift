package utils;

import configHandler.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    public static Connection getOracleConnection()
            throws Exception {
        System.out.println(
                "URL = " +
                        ConfigManager.getDBProperty("oracle.url"));

        System.out.println(
                "USERNAME = " +
                        ConfigManager.getDBProperty("oracle.username"));

        String url =
                ConfigManager.getDBProperty(
                        "oracle.url");

        if (url == null || url.isBlank()) {

            throw new RuntimeException(
                    "Oracle URL is missing in db config");
        }

        return DriverManager.getConnection(
                ConfigManager.getDBProperty(
                        "oracle.url"),

                ConfigManager.getDBProperty(
                        "oracle.username"),

                ConfigManager.getDBProperty(
                        "oracle.password"));


    }

}