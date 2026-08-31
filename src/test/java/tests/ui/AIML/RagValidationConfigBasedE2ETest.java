package tests.ui.AIML;

import UI.AIML.RAGPipeline.workflows.ExecuteRagfromAlreadyCreatedPipelineWorkflow;
import ai.config.AimlTestConfig;
import ai.config.RagPipelineConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import tests.base.BaseAuthenticatedUITest;
import utils.LoggerUtil;

@Severity(SeverityLevel.CRITICAL)
public class RagValidationConfigBasedE2ETest extends BaseAuthenticatedUITest {

    private ExecuteRagfromAlreadyCreatedPipelineWorkflow ragWorkflow;

    @Override
    public void login(String tenant) {
        // Call parent login which handles authentication
        super.login(tenant);
        // After login completes, initialize workflow with authenticated page
        ragWorkflow = new ExecuteRagfromAlreadyCreatedPipelineWorkflow(page);
        LoggerUtil.LOGGER.info("[TEST] Initialized RAG Workflow with authenticated page");
    }

    @Test
    @Description("Execute RAG validation using properties file for test data")
    public void executeRagValidationWithPropertiesConfig() {
        LoggerUtil.LOGGER.info("[TEST] Starting: executeRagValidationWithPropertiesConfig");
        LoggerUtil.LOGGER.info("[TEST] Environment: {}", RagPipelineConfig.getCurrentEnvironment());
        LoggerUtil.LOGGER.info("[TEST] Project URL: {}", RagPipelineConfig.getProjectViewUrl());
        LoggerUtil.LOGGER.info("[TEST] Feature Name: {}", RagPipelineConfig.getFeatureName());

        // Load test data from properties file
        String question = AimlTestConfig.getRagQuestion();
        String groundTruthContext = AimlTestConfig.getRagGroundTruthContext();
        
        LoggerUtil.LOGGER.info("[TEST] Loaded question from properties: {}", question);
        LoggerUtil.LOGGER.info("[TEST] Loaded ground truth context from properties");

        // Execute workflow using config-based URLs and properties-based test data
        ragWorkflow.executeStructuredRagValidationWorkflowFromConfig(question, groundTruthContext);

        LoggerUtil.LOGGER.info("[TEST] ✓ RAG validation completed successfully with properties config");
    }

    @Test
    @Description("Execute RAG validation using explicit URL and properties file")
    public void executeRagValidationWithExplicitUrlAndProperties() {
        LoggerUtil.LOGGER.info("[TEST] Starting: executeRagValidationWithExplicitUrlAndProperties");

        String projectUrl = RagPipelineConfig.getProjectViewUrl();
        String featureName = RagPipelineConfig.getFeatureName();
        
        // Load test data from properties file
        String question = AimlTestConfig.getRagQuestion();
        String groundTruthContext = AimlTestConfig.getRagGroundTruthContext();

        // Execute workflow with explicit URL parameters and properties-based test data
        ragWorkflow.executeUnstructuredRagValidationWorkflowFromProjectUrl(
                projectUrl,
                featureName,
                question,
                groundTruthContext
        );

        LoggerUtil.LOGGER.info("[TEST] ✓ RAG validation completed successfully with explicit URL and properties");
    }
}
