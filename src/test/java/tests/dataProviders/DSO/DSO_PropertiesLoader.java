package tests.dataProviders.DSO;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class DSO_PropertiesLoader {

    private static final String DSO_PROPERTIES_BASE_PATH = "config/properties/DSO/";
    private static final String DSO_PROPERTIES_FILE_PREFIX = "DSO_";
    private static final String DSO_PROPERTIES_FILE_EXTENSION = ".properties";
    private static final Map<String, Properties> PROPERTIES_CACHE = new ConcurrentHashMap<>();

    private DSO_PropertiesLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static String getProperty(String cloud, String key) {
        Properties cloudProperties = PROPERTIES_CACHE.computeIfAbsent(
                normalizeCloud(cloud),
                DSO_PropertiesLoader::loadCloudProperties);

        String value = cloudProperties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            String fallbackKey = buildFallbackAwsKey(key);
            value = cloudProperties.getProperty(fallbackKey);
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing property '" + key + "' for cloud '" + cloud + "'");
        }
        return value.trim();
    }

    private static String buildFallbackAwsKey(String key) {
        int separatorIndex = key.indexOf('_');
        if (separatorIndex < 0 || separatorIndex == key.length() - 1) {
            return key;
        }
        return "aws_" + key.substring(separatorIndex + 1);
    }

    private static Properties loadCloudProperties(String normalizedCloud) {
        String filePath = DSO_PROPERTIES_BASE_PATH
                + DSO_PROPERTIES_FILE_PREFIX
                + normalizedCloud.toUpperCase(Locale.ROOT)
                + DSO_PROPERTIES_FILE_EXTENSION;
        Properties properties = new Properties();

        try (InputStream inputStream = DSO_PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("DSO config not found: " + filePath);
            }
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Unable to load DSO properties: " + filePath, e);
        }
    }

    private static String normalizeCloud(String cloud) {
        if (cloud == null || cloud.trim().isEmpty()) {
            throw new IllegalArgumentException("Cloud cannot be null or empty");
        }
        return cloud.trim().toLowerCase(Locale.ROOT);
    }
}
