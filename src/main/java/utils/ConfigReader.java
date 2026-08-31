package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties =
            new Properties();

    static {

        try {

            String environment =
                    System.getProperty(
                            "environment",
                            "qa");

            LoggerUtil.LOGGER.info(
                    "Loading configuration for environment: "
                            + environment);

            InputStream inputStream =
                    ConfigReader.class
                            .getClassLoader()
                            .getResourceAsStream(
                                    "environment/"
                                            + environment
                                            + ".properties");

            if (inputStream == null) {

                LoggerUtil.LOGGER.error(
                        "Configuration file not found: "
                                + environment
                                + ".properties");

                throw new RuntimeException(
                        "Configuration file not found: "
                                + environment
                                + ".properties");
            }

            properties.load(inputStream);

            LoggerUtil.LOGGER.info(
                    "Configuration loaded successfully");

        } catch (Exception e) {

            LoggerUtil.LOGGER.error(
                    "Unable to load configuration",
                    e);

            throw new RuntimeException(
                    "Unable to load configuration",
                    e);
        }
    }

    public static String get(String key) {

        return properties.getProperty(key);
    }
}