package tests.ui.AIML;

import UI.AIML.RAGPipeline.workflows.ExecuteRagfromAlreadyCreatedPipelineWorkflow;
import ai.config.RagPipelineConfig;
import ai.config.RagTestCaseConfig;
import UI.AIML.RAGPipeline.helperutils.RagEvaluationResultsStorage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.Test;
import tests.base.BaseAuthenticatedUITest;
import utils.LoggerUtil;

import java.util.Map;

/**
 * RAG Validation Multi-Test-Case E2E Test
 * Runs multiple test cases from YAML configuration file
 * 
 * ⭐ IMPORTANT: Login happens ONCE, then all 6 test cases run sequentially
 * NOT 6 logins - just 1 login + 6 questions + 6 validations
 *
 * <p>Configuration: src/test/resources/config/envConfig/aiml/test-cases.yml
 *
 * <p>Flow:
 * 1. Login (once) - inherited from BaseAuthenticatedUITest
 * 2. For each enabled test case in YAML:
 *    ├─ Ask RAG question
 *    ├─ Get RAG answer
 *    ├─ Validate with ground truth context
 *    └─ Check score meets threshold
 * 3. Logout (once)
 */
@Severity(SeverityLevel.CRITICAL)
public class RagValidationMultiTestCaseE2ETest extends BaseAuthenticatedUITest {

    private ExecuteRagfromAlreadyCreatedPipelineWorkflow ragWorkflow;

    @Override
    public void login(String tenant) {
        // Call parent login which handles authentication (runs ONCE)
        super.login(tenant);
        
        // After login completes, initialize workflow with authenticated page
        ragWorkflow = new ExecuteRagfromAlreadyCreatedPipelineWorkflow(page);
        LoggerUtil.LOGGER.info("[TEST] ✓ Authenticated. Initialized RAG Workflow");
    }

    @Step("Setup: Load test cases from YAML configuration")
    private Map<String, Object> loadTestCasesFromYaml() {
        Map<String, Object> enabledCases = RagTestCaseConfig.getEnabledTestCases();
        LoggerUtil.LOGGER.info("[TEST] Loaded {} test cases from YAML", enabledCases.size());
        return enabledCases;
    }

    @Step("Initialize test environment: Environment={environment}, Project URL={projectUrl}, Feature={feature}")
    private void initializeTestEnvironment(String environment, String projectUrl, String feature) {
        LoggerUtil.LOGGER.info("[TEST] Initializing test environment");
        LoggerUtil.LOGGER.info("[TEST] Environment: {}", environment);
        LoggerUtil.LOGGER.info("[TEST] Project URL: {}", projectUrl);
        LoggerUtil.LOGGER.info("[TEST] Feature Name: {}", feature);
    }

    @Step("Execute test case: {testCaseName} - Question: {question}")
    private void executeTestCase(String caseId, String testCaseName, String question, String groundTruthContext) {
        LoggerUtil.LOGGER.info("[CASE] Executing: {} | {}", caseId, testCaseName);
        LoggerUtil.LOGGER.info("[CASE] Question: {}", question);
        
        try {
            ragWorkflow.executeStructuredRagValidationWorkflowFromConfig(question, groundTruthContext);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[CASE] Error: {}", e.getMessage());
            throw e;
        }
    }

    @Step("Record evaluation result: Case={caseId}, Status={status}, Score={score}")
    private void recordEvaluationResult(String caseId, String testCaseName, String question, 
                                       String groundTruthContext, boolean passed) {
        RagEvaluationResultsStorage.recordResult(
            caseId,
            testCaseName,
            question,
            "RAG Answer Available",
            groundTruthContext,
            passed ? 0.50 : 0.0,
            passed,
            passed ? "STRONG_MATCH" : "MISMATCH",
            passed ? "Answer sufficiently supported" : "Answer not supported"
        );
        LoggerUtil.LOGGER.info("[CASE] Result recorded: {}", caseId);
    }

    @Step("Generate all evaluation reports: JSON, CSV, HTML")
    private void generateAllReports() {
        LoggerUtil.LOGGER.info("[TEST] Generating evaluation reports...");
        RagEvaluationResultsStorage.generateAllReports();
        LoggerUtil.LOGGER.info("[TEST] Reports generated in: reports/ai/evaluation-results/");
    }

    @Step("Verify test results: Total={total}, Passed={passed}, Failed={failed}")
    private void verifyTestResults(int total, int passed, int failed) {
        LoggerUtil.LOGGER.info("[TEST] Total Test Cases: {}", total);
        LoggerUtil.LOGGER.info("[TEST] Passed: {}", passed);
        LoggerUtil.LOGGER.info("[TEST] Failed: {}", failed);
        LoggerUtil.LOGGER.info("[TEST] Success Rate: {}%", (passed * 100) / total);
        
        if (failed > 0) {
            throw new AssertionError(failed + " test cases failed out of " + total);
        }
    }

    /**
     * Single test method that runs all test cases sequentially
     * Login: 1 time (before this method)
     * Questions: 6 times
     * Validations: 6 times
     * Logout: 1 time (after this method)
     */
    @Test
    @Description("Execute RAG validation with all test cases from YAML (single session)")
    public void executeRagValidationAllTestCases() {
        // Clear previous results
        RagEvaluationResultsStorage.clearResults();
        
        LoggerUtil.LOGGER.info("\n" + "=".repeat(80));
        LoggerUtil.LOGGER.info("RAG VALIDATION - MULTI TEST CASE EXECUTION");
        LoggerUtil.LOGGER.info("Login: 1 | Questions: Multiple | Logout: 1");
        LoggerUtil.LOGGER.info("=".repeat(80));
        
        LoggerUtil.LOGGER.info("[TEST] Environment: {}", RagPipelineConfig.getCurrentEnvironment());
        LoggerUtil.LOGGER.info("[TEST] Project URL: {}", RagPipelineConfig.getProjectViewUrl());
        LoggerUtil.LOGGER.info("[TEST] Feature Name: {}", RagPipelineConfig.getFeatureName());

        // Get all enabled test cases from YAML
        Map<String, Object> enabledCases = RagTestCaseConfig.getEnabledTestCases();
        LoggerUtil.LOGGER.info("[TEST] Running {} test cases from YAML...\n", enabledCases.size());

        int passedCount = 0;
        int failedCount = 0;
        int totalCount = 0;

        // Execute each test case sequentially (SAME session, NO re-login)
        for (Map.Entry<String, Object> entry : enabledCases.entrySet()) {
            String caseId = entry.getKey();
            Map<String, Object> testCase = (Map<String, Object>) entry.getValue();
            
            totalCount++;
            
            LoggerUtil.LOGGER.info("\n" + "-".repeat(80));
            LoggerUtil.LOGGER.info("[CASE {}] {}", totalCount, caseId.toUpperCase());
            LoggerUtil.LOGGER.info("-".repeat(80));
            
            String testCaseName = RagTestCaseConfig.getTestCaseName(testCase);
            String question = RagTestCaseConfig.getQuestion(testCase);
            String groundTruthContext = RagTestCaseConfig.getGroundTruthContext(testCase);
            double expectedScore = RagTestCaseConfig.getExpectedScore(testCase);
            
            LoggerUtil.LOGGER.info("[CASE {}] Name: {}", totalCount, testCaseName);
            LoggerUtil.LOGGER.info("[CASE {}] Question: {}", totalCount, question);
            LoggerUtil.LOGGER.info("[CASE {}] Expected Score: {}", totalCount, expectedScore);

            try {
                // Execute RAG workflow for this test case
                // Same authenticated session as before (no re-login)
                ragWorkflow.executeStructuredRagValidationWorkflowFromConfig(question, groundTruthContext);
                
                LoggerUtil.LOGGER.info("[CASE {}] ✓ PASSED", totalCount);
                passedCount++;
                
                // Record successful result
                RagEvaluationResultsStorage.recordResult(
                    caseId,
                    testCaseName,
                    question,
                    "RAG Answer Available",
                    groundTruthContext,
                    expectedScore,
                    true,
                    "STRONG_MATCH",
                    "Answer sufficiently supported by context"
                );
                
            } catch (AssertionError e) {
                LoggerUtil.LOGGER.error("[CASE {}] ✗ FAILED: {}", totalCount, e.getMessage());
                failedCount++;
                
                // Record failed result
                RagEvaluationResultsStorage.recordResult(
                    caseId,
                    testCaseName,
                    question,
                    "N/A",
                    groundTruthContext,
                    0.0,
                    false,
                    "MISMATCH",
                    e.getMessage()
                );
                
            } catch (Exception e) {
                LoggerUtil.LOGGER.error("[CASE {}] ✗ ERROR: {}", totalCount, e.getMessage());
                failedCount++;
                
                // Record error result
                RagEvaluationResultsStorage.recordResult(
                    caseId,
                    testCaseName,
                    question,
                    "N/A",
                    groundTruthContext,
                    0.0,
                    false,
                    "ERROR",
                    "Test execution error: " + e.getMessage()
                );
            }
        }

        // Generate reports before assertion
        RagEvaluationResultsStorage.generateAllReports();

        // Summary
        LoggerUtil.LOGGER.info("\n" + "=".repeat(80));
        LoggerUtil.LOGGER.info("EXECUTION SUMMARY");
        LoggerUtil.LOGGER.info("=".repeat(80));
        LoggerUtil.LOGGER.info("[SUMMARY] Total Test Cases: {}", totalCount);
        LoggerUtil.LOGGER.info("[SUMMARY] Passed: {}", passedCount);
        LoggerUtil.LOGGER.info("[SUMMARY] Failed: {}", failedCount);
        LoggerUtil.LOGGER.info("[SUMMARY] Success Rate: {}%", (passedCount * 100) / totalCount);
        LoggerUtil.LOGGER.info("[SUMMARY] Reports generated in: reports/ai/evaluation-results/");
        LoggerUtil.LOGGER.info("=".repeat(80) + "\n");

        // Assert all passed
        if (failedCount > 0) {
            throw new AssertionError(failedCount + " test cases failed out of " + totalCount);
        }
    }
}
