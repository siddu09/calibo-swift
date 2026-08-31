package reporting;

import configHandler.ConfigManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentWriter {

    public static void writeEnvironmentInfo() {

        try {

            Properties env = new Properties();

            env.setProperty(
                    "Environment",
                    ConfigManager.getUIProperty("environment"));

            env.setProperty(
                    "Browser",
                    ConfigManager.getUIProperty("browser"));

            env.setProperty(
                    "Headless",
                    ConfigManager.getUIProperty("headless"));

            env.setProperty(
                    "Framework",
                    "Playwright + TestNG");

            env.setProperty(
                    "Java Version",
                    System.getProperty("java.version"));

            env.setProperty(
                    "Execution",
                    "Local");

            FileOutputStream outputStream =
                    new FileOutputStream(
                            "reports/allure-results/environment.properties");

            env.store(outputStream, null);

            outputStream.close();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}