package tests.ui.AIML;

import UI.AIML.RAGPipeline.workflows.ExecuteRagfromAlreadyCreatedPipelineWorkflow;
import assertionsHandler.AssertionManager;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import tests.base.BaseAuthenticatedAIMLTest;
import utils.LoggerUtil;

/**
 * RAG Validation Test for an already-created pipeline.
 * Structure aligned with RagValidationE2ETest (no functionality changes).
 *
 * <p>Two independent flows, each targeting a DIFFERENT RAG Builder node on the
 * SAME Data Pipeline Studio canvas:
 * <ul>
 *   <li>{@link #validateStructuredRAGAutomationDemoPipeline()}   -> Snowflake "StructuredRAGBuilder3"</li>
 *   <li>{@link #validateUnstructuredRAGAutomationDemoPipeline()} -> Databricks UC "RAGBuilder"</li>
 * </ul>
 */
@Severity(SeverityLevel.CRITICAL)
public class RAGValidationFromalreadyCreatedPipleineTest extends BaseAuthenticatedAIMLTest {
   @Test(enabled = false, groups = {"RAG","RAG-E2E"}, description = "Validate STRUCTURED (Snowflake) RAG Automation Demo pipeline after login and navigation")
    @Description("Execute STRUCTURED RAG validation on already-created pipeline: Login → Navigate → Feature → Validate (StructuredRAGBuilder3)")
    public void validateStructuredRAGAutomationDemoPipeline() {

        currentTestName = "RAG Automation Demo Pipeline Validation - STRUCTURED";
        executionId = "RAG-STRUCTURED-" + System.currentTimeMillis();
        capturedQuestion = RagTestData.QUESTION_WHAT_TABLES;
        capturedGroundTruth = RagTestData.RAG_BUILDER_EVALUATION_CONTEXT;



        ExecuteRagfromAlreadyCreatedPipelineWorkflow workflow =
                new ExecuteRagfromAlreadyCreatedPipelineWorkflow(page);

        workflow.executeStructuredRagValidationWorkflowFromConfig(capturedQuestion, capturedGroundTruth);

        // Collect any soft assertions
        AssertionManager.assertAll();

    }



    @Test(groups = {"RAG","RAG-E2E"}, description = "Validate UNSTRUCTURED (Databricks) RAG Automation Demo pipeline after login and navigation")
    @Description("Execute UNSTRUCTURED RAG validation on already-created pipeline: Login → Navigate → Feature → Validate (RAGBuilder / Databricks UC)")
    public void validateUnstructuredRAGAutomationDemoPipeline() {

        currentTestName = "RAG Automation Demo Pipeline Validation - UNSTRUCTURED";
        executionId = "RAG-UNSTRUCTURED-" + System.currentTimeMillis();
        capturedQuestion = RagTestData.QUESTION_WHAT_TABLES;
        capturedGroundTruth = RagTestData.RAG_BUILDER_EVALUATION_CONTEXT;


        ExecuteRagfromAlreadyCreatedPipelineWorkflow workflow =
                new ExecuteRagfromAlreadyCreatedPipelineWorkflow(page);

        workflow.executeUnstructuredRagValidationWorkflowFromConfig(capturedQuestion, capturedGroundTruth);

        // Collect any soft assertions
        AssertionManager.assertAll();

    }

}
