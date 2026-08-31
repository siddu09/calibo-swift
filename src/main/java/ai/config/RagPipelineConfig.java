package ai.config;

import org.yaml.snakeyaml.Yaml;
import utils.LoggerUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * RAG Pipeline Configuration Manager
 * Reads environment-specific URLs and settings from YAML config file
 */
public class RagPipelineConfig {

    private static final String CONFIG_FILE = "src/main/resources/config/rag-pipeline-config.yml";
    private static String environment = System.getProperty("environment", "qa");
    
    private static Map<String, Object> configMap;

    static {
        loadConfig();
    }

    /**
     * Load configuration from YAML file
     */
    private static void loadConfig() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            configMap = data;
            LoggerUtil.LOGGER.info("[CONFIG] Loaded RAG Pipeline config from: {}", CONFIG_FILE);
            LoggerUtil.LOGGER.info("[CONFIG] Environment: {}", environment);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[CONFIG] Failed to load config file: {}", e.getMessage());
            throw new RuntimeException("Failed to load RAG Pipeline configuration", e);
        }
    }

    /**
     * Get environment-specific configuration section
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getEnvironmentConfig() {
        Map<String, Object> environments = (Map<String, Object>) configMap.get("environments");
        Map<String, Object> envConfig = (Map<String, Object>) environments.get(environment);
        
        if (envConfig == null) {
            throw new RuntimeException("Environment '" + environment + "' not found in config");
        }
        return envConfig;
    }

    /**
     * Get Project View URL for the configured environment
     */
    public static String getProjectViewUrl() {
        Map<String, Object> envConfig = getEnvironmentConfig();
        String url = (String) envConfig.get("projectViewUrl");
        LoggerUtil.LOGGER.info("[CONFIG] Project View URL: {}", url);
        return url;
    }

    /**
     * Get Base URL for the configured environment
     */
    public static String getBaseUrl() {
        Map<String, Object> envConfig = getEnvironmentConfig();
        String url = (String) envConfig.get("baseUrl");
        LoggerUtil.LOGGER.info("[CONFIG] Base URL: {}", url);
        return url;
    }

    /**
     * Get Login Target URL for the configured environment
     */
    public static String getLoginTargetUrl() {
        Map<String, Object> envConfig = getEnvironmentConfig();
        String url = (String) envConfig.get("loginTargetUrl");
        LoggerUtil.LOGGER.info("[CONFIG] Login Target URL: {}", url);
        return url;
    }

    /**
     * Get Feature Name to navigate to
     */
    public static String getFeatureName() {
        Map<String, Object> envConfig = getEnvironmentConfig();
        String name = (String) envConfig.get("featureName");
        LoggerUtil.LOGGER.info("[CONFIG] Feature Name: {}", name);
        return name;
    }

    /**
     * Get Tenant ID for the configured environment
     */
    public static String getTenant() {
        Map<String, Object> envConfig = getEnvironmentConfig();
        String tenant = (String) envConfig.get("tenant");
        LoggerUtil.LOGGER.info("[CONFIG] Tenant: {}", tenant);
        return tenant;
    }

    /**
     * Get wait timeouts configuration
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getWaits() {
        return (Map<String, Object>) configMap.get("waits");
    }

    /**
     * Get specific timeout value
     */
    public static long getTimeout(String timeoutKey) {
        @SuppressWarnings("unchecked")
        Map<String, Object> waits = (Map<String, Object>) configMap.get("waits");
        Object value = waits.get(timeoutKey);
        
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    /**
     * Set environment (for testing different environments)
     */
    public static void setEnvironment(String env) {
        environment = env;
        loadConfig();
        LoggerUtil.LOGGER.info("[CONFIG] Switched to environment: {}", environment);
    }

    /**
     * Get current environment
     */
    public static String getCurrentEnvironment() {
        return environment;
    }
}
