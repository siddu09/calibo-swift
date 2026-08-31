package ai.config;

import utils.LoggerUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * AIML test specific configuration loader
 * Reads properties from src/test/resources/config/envConfig/aiml/qa.properties
 */
public class AimlTestConfig {

    private static final String PROP_FILE = "src/test/resources/config/envConfig/aiml/qa.properties";
    private static Properties props = new Properties();

    static {
        load();
    }

    private static void load() {
        try (InputStream in = new FileInputStream(PROP_FILE)) {
            props.load(in);
            LoggerUtil.LOGGER.info("[CONFIG] Loaded AIML test properties from: {}", PROP_FILE);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[CONFIG] Failed to load AIML test properties: {}", e.getMessage());
            throw new RuntimeException("Failed to load AIML test properties", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * Get RAG question from properties (key: rag.question)
     */
    public static String getRagQuestion() {
        return props.getProperty("rag.question", "");
    }

    /**
     * Get RAG ground truth context from properties (key: rag.groundTruthContext)
     */
    public static String getRagGroundTruthContext() {
        return props.getProperty("rag.groundTruthContext", "");
    }
}
