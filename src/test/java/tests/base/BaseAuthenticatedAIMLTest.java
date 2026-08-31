package tests.base;

import com.microsoft.playwright.options.LoadState;
import ai.config.RagPipelineConfig;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import UI.AIML.RAGPipeline.helperutils.RagEvaluationResultsStorage;
import assertionsHandler.AIAssert;
import ai.models.EvaluationResult;
import utils.CaliboAccelerateLoginUtil;
import utils.LoggerUtil;

/**
 * Base class for AI/ML (AIML) tests that require an authenticated session.
 * 
 * <p>Differences from BaseAuthenticatedUITest:
 * - Loads credentials from: src/test/resources/config/envConfig/aiml/qa.properties
 * - NOT from: src/test/resources/config/envConfig/ui/qa.properties
 * - Ensures AI tests use their own test data and environment configuration
 * 
 * <p>Inherits browser lifecycle (setup/teardown) as-is from {@link BaseUITest}.
 * TestNG runs superclass @BeforeMethod before subclass @BeforeMethod, so the
 * browser is guaranteed to be initialized before {@link #login} runs.
 *
 * <p>Test classes extending this get a ready-to-use, authenticated `page` and
 * should contain ONLY business scenario logic.
 */
public class BaseAuthenticatedAIMLTest extends BaseUITest {

    @BeforeMethod
    @Parameters({"tenant"})
    public void login(@Optional("QA_Tenant_604134") String tenant) {

        // Use target URL from config file instead of default resolver
        String targetUrl = RagPipelineConfig.getLoginTargetUrl();
        LoggerUtil.LOGGER.info("[LOGIN-AIML] Using config-based target URL: {}", targetUrl);
        LoggerUtil.LOGGER.info("[LOGIN-AIML] Using credentials from: src/test/resources/config/envConfig/aiml/qa.properties");

        try {

            // Login with credentials from AIML-specific properties file
            // Pass "aiuser1" (the actual credential profile from aiml/qa.properties)
            // It starts with "ai" so CaliboAccelerateLoginUtil will load from aiml config
            CaliboAccelerateLoginUtil.loginToPage(page, targetUrl, "aiuser1", tenant);
            LoggerUtil.LOGGER.info("[LOGIN-AIML] ✓ Login completed successfully via CaliboAccelerateLoginUtil");
            
            // WAIT for page to fully load after login completes
            waitForPageLoadAfterLogin();

        } catch (Exception e) {

            LoggerUtil.LOGGER.warn(
                    "[LOGIN-AIML] Login helper failed, attempting fallback: {}",
                    e.getMessage());

            // Fallback: Navigate directly and wait for page to load
            page.navigate(targetUrl);
            
            // Wait for page to navigate away from /login
            waitForPageLoadAfterLogin();
            
            LoggerUtil.LOGGER.info("[LOGIN-AIML] ✓ Fallback navigation complete");
        }
    }

    /**
     * Wait for page to load after login - handles async Azure AD redirect
     * Polls the URL every 500ms until we're no longer on /login
     * Then waits for NETWORKIDLE
     */
    private void waitForPageLoadAfterLogin() {
        LoggerUtil.LOGGER.info("[LOGIN-AIML] Waiting for page to load after login...");
        
        long startTime = System.currentTimeMillis();
        long timeout = 10_000; // 10 seconds
        long pollInterval = 500; // 500ms
        
        while (System.currentTimeMillis() - startTime < timeout) {
            String currentUrl = page.url();
            LoggerUtil.LOGGER.debug("[LOGIN-AIML] Current URL: {}", currentUrl);
            
            // Check if we're still on login page
            if (!currentUrl.contains("/login")) {
                LoggerUtil.LOGGER.info("[LOGIN-AIML] ✓ Navigated away from login. URL: {}", currentUrl);
                
                // Wait for DOM to stabilize
                page.waitForTimeout(2000);
                
                // Wait for network to be idle (all resources loaded)
                try {
                    page.waitForLoadState(LoadState.NETWORKIDLE, 
                        new com.microsoft.playwright.Page.WaitForLoadStateOptions().setTimeout(10_000));
                    LoggerUtil.LOGGER.info("[LOGIN-AIML] ✓ Page fully loaded (NETWORKIDLE)");
                } catch (Exception e) {
                    LoggerUtil.LOGGER.warn("[LOGIN-AIML] ⚠️ NETWORKIDLE timeout, continuing anyway: {}", e.getMessage());
                }
                
                return;
            }
            
            try {
                Thread.sleep(pollInterval);
            } catch (InterruptedException ignored) {}
        }
        
        LoggerUtil.LOGGER.warn("[LOGIN-AIML] ⚠️ Timeout waiting for page load. Still on login page.");
        throw new RuntimeException("Login page redirect timed out after 10 seconds");
    }

    // Reporting/test-result fields used by AIML tests (accessible to subclasses)
    protected String executionId;
    protected String currentTestName;
    protected String capturedQuestion;
    protected String capturedAnswer;
    protected String capturedGroundTruth;
    protected double evaluationScore;
    protected boolean testPassed;
    protected String evaluationVerdict;
    protected String evaluationReason;

    @AfterMethod
    public void recordTestResults() {
        try {
            EvaluationResult evalResult = AIAssert.getLastEvaluationResult();

            if (evalResult != null) {
                evaluationScore = evalResult.getScore();
                evaluationVerdict = evalResult.getVerdict();
                evaluationReason = evalResult.getReason();
                testPassed = evalResult.isPassed();

              LoggerUtil.LOGGER.info("[RAG-VALIDATION] Captured evaluation result: Score={}, Verdict={}, Passed={}",
                        evaluationScore, evaluationVerdict, testPassed);
            } else {
                evaluationScore = testPassed ? 1.0 : 0.0;
                evaluationVerdict = testPassed ? "PASS" : "FAIL";
                evaluationReason = testPassed ? "RAG validation successful" : "RAG validation failed";
            }

            LoggerUtil.LOGGER.info("[RAG-VALIDATION] Recording test results to Excel storage...");

            RagEvaluationResultsStorage.recordResult(
                    executionId,
                    currentTestName,
                    capturedQuestion != null ? capturedQuestion : "N/A",
                    capturedAnswer != null ? capturedAnswer : "N/A",
                    capturedGroundTruth != null ? capturedGroundTruth : "N/A",
                    evaluationScore,
                    testPassed,
                    evaluationVerdict,
                    evaluationReason,
                    "RAG-Pipeline-Validation"
            );

        LoggerUtil.LOGGER.info("[RAG-VALIDATION] ✓ Test results recorded to Excel");

        // Generate reports after recording
            generateAllReports();

    } catch (Exception e) {
            LoggerUtil.LOGGER.error("[RAG-VALIDATION] Failed to record test results: {}", e.getMessage(), e);
        }
    }

    public static void generateAllReports() {
        try {
            LoggerUtil.LOGGER.info("[RAG-VALIDATION] Generating comprehensive reports...");
            RagEvaluationResultsStorage.generateAllReports();
            LoggerUtil.LOGGER.info("[RAG-VALIDATION] ✅ All reports generated successfully");
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[RAG-VALIDATION] Failed to generate reports: {}", e.getMessage(), e);
        }
    }
}

