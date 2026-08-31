package ai.config;

import utils.LoggerUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Test case configuration loader for RAG validation multi-case tests
 */
public class RagTestCaseConfig {

    private static final String CONFIG_FILE = "src/test/resources/config/envConfig/aiml/test-cases.yml";
    private static Map<String, Object> testCases;

    static {
        load();
    }

    private static void load() {
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(in);
            testCases = (Map<String, Object>) data.get("testCases");
            if (testCases == null) {
                testCases = new HashMap<>();
            }
            LoggerUtil.LOGGER.info("[CONFIG] Loaded RAG test cases from: {}", CONFIG_FILE);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[CONFIG] Failed to load RAG test cases: {}", e.getMessage());
            throw new RuntimeException("Failed to load RAG test cases", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getCase(String caseId) {
        return (Map<String, Object>) testCases.get(caseId);
    }

    /**
     * Get all enabled test cases from the YAML config
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getEnabledTestCases() {
        Map<String, Object> enabled = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : testCases.entrySet()) {
            Map<String, Object> testCase = (Map<String, Object>) entry.getValue();
            Object enabledObj = testCase.get("enabled");
            
            // Default to true if not specified, or check if explicitly true
            boolean isEnabled = enabledObj == null || (boolean) enabledObj;
            
            if (isEnabled) {
                enabled.put(entry.getKey(), testCase);
            }
        }
        
        return enabled;
    }

    /**
     * Extract test case name from a test case map
     */
    public static String getTestCaseName(Map<String, Object> testCase) {
        Object nameObj = testCase.get("name");
        return nameObj != null ? nameObj.toString() : "Unknown";
    }

    /**
     * Extract question from a test case map
     */
    public static String getQuestion(Map<String, Object> testCase) {
        Object questionObj = testCase.get("question");
        return questionObj != null ? questionObj.toString() : "";
    }

    /**
     * Extract ground truth context from a test case map
     */
    public static String getGroundTruthContext(Map<String, Object> testCase) {
        Object contextObj = testCase.get("groundTruthContext");
        return contextObj != null ? contextObj.toString() : "";
    }

    /**
     * Extract expected score from a test case map
     */
    public static double getExpectedScore(Map<String, Object> testCase) {
        Object scoreObj = testCase.get("expectedScore");
        if (scoreObj == null) return 0.50; // default
        
        if (scoreObj instanceof Number) {
            return ((Number) scoreObj).doubleValue();
        }
        
        return Double.parseDouble(scoreObj.toString());
    }
}
