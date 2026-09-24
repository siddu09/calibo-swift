
package configHandler;

import utils.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class ConfigManager {

    private static final Properties UI_PROPERTIES =
            new Properties();

    private static final Properties API_PROPERTIES =
            new Properties();

    private static final Properties DSO_PROPERTIES =
            new Properties();

    private static final Properties DSO_TENANT_PROPERTIES =
            new Properties();

    private static final Properties DB_PROPERTIES =
            new Properties();

    private static final Properties DPS_PROPERTIES =
           new Properties();


    static {

        try {

           String env = System.getProperty("env", "qa");

           loadUIProperties(env);

           loadAPIProperties(env);

           loadDPSProperties(env);

//            loadDBProperties(env);
           loadRunManagerDSO();

//            loadDSOTenantProperties();

            LoggerUtil.LOGGER.info(
                    "Loaded Environment = "
                            + env);

            LoggerUtil.LOGGER.info(
                    "Configured Browser = "
                            + getUIProperty("browser"));

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to load config files",
                    e);
        }
    }

    public static void loadDSOTenantProperties(String tenantName) throws IOException {
//        String normalizedCloud = tenantName == null ? "" : tenantName.trim().toLowerCase(Locale.ROOT);
        String fileName;
        if ("QA_Tenant_604134".equalsIgnoreCase(tenantName)) {
            fileName = "config/properties/DSO/QA_Tenant.properties";
        } else if ("Automation".equalsIgnoreCase(tenantName)) {
            fileName = "config/properties/DSO/Automation_Tenant.properties";
        } else if ("".equalsIgnoreCase(tenantName)) {
            fileName = "config/properties/DSO/UAT_Tenant.properties";
        } else {
            throw new IllegalArgumentException("Unsupported cloud for DSO config: " + tenantName);
        }


        try (InputStream inputStream = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("DSO Config not found: " + fileName);
            }
            DSO_TENANT_PROPERTIES.clear();
            DSO_TENANT_PROPERTIES.load(inputStream);
        }
    }

    private static void loadUIProperties(
            String env)
            throws Exception {

        String fileName =
                "config/envConfig/ui/"
                        + env
                        + ".properties";

        InputStream inputStream =
                ConfigManager.class
                        .getClassLoader()
                        .getResourceAsStream(
                                fileName);

        if (inputStream == null) {

            throw new RuntimeException(
                    "UI Config not found: "
                            + fileName);
        }

        UI_PROPERTIES.load(inputStream);
    }

    private static void loadAPIProperties(String env) throws Exception {

        String fileName = "config/envConfig/api/"
                + env
                + ".properties";

        InputStream inputStream = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (inputStream == null) {

            throw new RuntimeException("API Config not found: " + fileName);
        }

        API_PROPERTIES.load(inputStream);
    }

    private static void loadDPSProperties(String env) throws Exception {

        String fileName = "config/envConfig/dps/"
                + env
                + ".properties";

        InputStream inputStream = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (inputStream == null) {

            throw new RuntimeException("DPS Config not found: " + fileName);
        }

        DPS_PROPERTIES.load(inputStream);
    }

    public static void loadRunManagerDSO() throws Exception {
        String cloud = System.getProperty("cloud", "aws");
        loadRunManagerDSO(cloud);
    }

    public static void loadRunManagerDSO(String cloud) throws Exception {
        String normalizedCloud = cloud == null ? "" : cloud.trim().toLowerCase(Locale.ROOT);
        String fileName;
        if ("aws".equals(normalizedCloud)) {
            fileName = "config/properties/DSO/DSO_AWS.properties";
        } else if ("azure".equals(normalizedCloud)) {
            fileName = "config/properties/DSO/DSO_Azure.properties";
        } else if ("gcp".equals(normalizedCloud)) {
            fileName = "config/properties/DSO/DSO_GCP.properties";
        } else {
            throw new IllegalArgumentException("Unsupported cloud for DSO config: " + cloud);
        }


        try (InputStream inputStream = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("DSO Config not found: " + fileName);
            }
            DSO_PROPERTIES.clear();
            DSO_PROPERTIES.load(inputStream);
        }
    }

    //    private static void loadDBProperties(
//            String env)
//            throws Exception {
//
//        String fileName =
//                "config/db/"
//                        + env
//                        + ".properties";
//
//        InputStream inputStream =
//                ConfigManager.class
//                        .getClassLoader()
//                        .getResourceAsStream(
//                                fileName);
//
//        if (inputStream == null) {
//
//            throw new RuntimeException(
//                    "DB Config not found: "
//                            + fileName);
//        }
//
//        DB_PROPERTIES.load(inputStream);
//    }
    public static String getUIProperty(
            String key) {

        return UI_PROPERTIES.getProperty(key);
    }

    public static String getAPIProperty(
            String key) {

        return API_PROPERTIES.getProperty(key);
    }

    public static String getDPSProperty(
            String key) {

        return DPS_PROPERTIES.getProperty(key);
    }

    public static String getDBProperty(
            String key) {

        return DB_PROPERTIES.getProperty(key);
    }

    public static String getDSOProperty(
            String key) {

        return DSO_PROPERTIES.getProperty(key);
    }

    public static String getDSOTenantProperty(
            String key) {

        return DSO_TENANT_PROPERTIES.getProperty(key);
    }
}
