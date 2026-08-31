package UI.AIML.RAGPipeline.workflows;

import UI.AIML.RAGPipeline.BuildingBlocks.FeatureNavigationBuildingBlock;
import UI.AIML.RAGPipeline.BuildingBlocks.PipelineCanvasBuildingBlock;
import UI.AIML.RAGPipeline.BuildingBlocks.RAGBuildingBlock;
import UI.AIML.RAGPipeline.BuildingBlocks.RagEndpointTestingBuildingBlock;
import UI.AIML.RAGPipeline.BuildingBlocks.ProjectNavigationBuildingBlock;
import ai.config.RagPipelineConfig;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import utils.LoggerUtil;

/**
 * PRODUCT-LEVEL workflow for RAG Automation Demo end-to-end scenario.
 *
 * <p>Pure orchestrator pattern:
 * - Delegates all operations to dedicated Building Blocks
 * - Maintains business scenario flow clarity
 * - No duplicated logic — all helpers in BuildingBlocks
 *
 * <p><b>Two canvas flows are supported</b> and each targets a DIFFERENT RAG Builder
 * node on the pipeline canvas:
 * <ul>
 *   <li>STRUCTURED  -> Snowflake "StructuredRAGBuilder3" node</li>
 *   <li>DATABRICKS  -> Databricks UC "RAGBuilder" node</li>
 * </ul>
 * The correct node type is bound PER FLOW (not in the constructor), so the
 * pipeline-canvas and endpoint building blocks always act on the right node.
 *
 * <pre>
 *   Test (1-4 lines)
 *     -> ExecuteRagfromAlreadyCreatedPipelineWorkflow (orchestration)
 *       -> ProjectNavigationBuildingBlock (login/navigation)
 *       -> FeatureNavigationBuildingBlock (feature selection)
 *       -> PipelineCanvasBuildingBlock (pipeline interaction, node-type aware)
 *       -> RagEndpointTestingBuildingBlock (endpoint testing, node-type aware)
 *       -> RAGBuildingBlock (validation/assertions)
 *         -> Page Objects -> ResilientLocator -> Playwright
 * </pre>
 */
public class ExecuteRagfromAlreadyCreatedPipelineWorkflow {

    private final Page page;

    // Navigation + validation blocks are node-type agnostic, so they can be shared.
    private final ProjectNavigationBuildingBlock projectNavBlock;
    private final FeatureNavigationBuildingBlock featureNavigationBlock;
    private final RAGBuildingBlock ragValidationBlock;

    // Node-type-aware blocks are (re)created per flow via bindBlocks(...).
    private PipelineCanvasBuildingBlock pipelineBlock;
    private RagEndpointTestingBuildingBlock endpointBlock;

    public ExecuteRagfromAlreadyCreatedPipelineWorkflow(Page page) {
        this.page = page;
        this.projectNavBlock = new ProjectNavigationBuildingBlock(page);
        this.featureNavigationBlock = new FeatureNavigationBuildingBlock(page);
        this.ragValidationBlock = new RAGBuildingBlock();

        // Default binding so any legacy getter/wrapper call is safe before a flow runs.
        bindBlocks(PipelineCanvasBuildingBlock.RagBuilderType.STRUCTURED,
                RagEndpointTestingBuildingBlock.RagBuilderType.STRUCTURED);
    }

    /**
     * Binds the node-type-aware building blocks to a specific RAG Builder node.
     * Called at the start of each flow so both blocks target the SAME node.
     */
    private void bindBlocks(PipelineCanvasBuildingBlock.RagBuilderType canvasType,
                            RagEndpointTestingBuildingBlock.RagBuilderType endpointType) {
        this.pipelineBlock = new PipelineCanvasBuildingBlock(page, canvasType);
        this.endpointBlock = new RagEndpointTestingBuildingBlock(page, endpointType);
    }

    // ======================================================================
    //  PRIMARY ORCHESTRATION METHODS
    // ======================================================================

    @Step("Execute STRUCTURED RAG Validation Workflow - Direct Project Navigation (from config)")
    public void executeStructuredRagValidationWorkflowFromConfig(
            String question,
            String groundTruthContext) {

        LoggerUtil.LOGGER.info("[RAG-WORKFLOW] Starting STRUCTURED RAG validation from config-based project URL");

        // Bind both node-type-aware blocks to the Snowflake Structured RAG Builder node.
        bindBlocks(PipelineCanvasBuildingBlock.RagBuilderType.STRUCTURED,
                RagEndpointTestingBuildingBlock.RagBuilderType.STRUCTURED);

        // Step 1: Retrieve feature name from external config
        String featureName = RagPipelineConfig.getFeatureName();

        // Step 2: Poll for login redirect to complete (URL changes from /login)
        projectNavBlock.waitForPageLoadAfterLogin();
        // Step 3: Navigate to project view URL stored in config
        projectNavBlock.navigateToProjectFromConfig();
        // Step 4: Click feature link in workstreams tab to open feature page
        featureNavigationBlock.openFeature(featureName);
        // Step 5: Click Develop stage and capture pipeline canvas components
        pipelineBlock.openDevelopStageAndCaptureComponents();
        // Step 6: Ask RAG endpoint the test question and capture response
        String answer = endpointBlock.askQuestion(question);
        // Step 7: Assert that LLM returned a non-empty answer
        ragValidationBlock.validateAnswerExists(answer);
        // Step 8: Validate answer quality using AI evaluator against ground truth
        ragValidationBlock.validateAnswerWithAI(question, answer, groundTruthContext);
        // Step 9: Assert all expected pipeline components are present and configured
        ragValidationBlock.assertPipelineComponentsPresent(
                pipelineBlock.isRagBuilderPresent(),
                pipelineBlock.isDataLakePresent(),
                endpointBlock.isTestEndpointPresent());

        LoggerUtil.LOGGER.info("[RAG-WORKFLOW] ✓ STRUCTURED RAG validation workflow completed successfully");
    }

    @Step("Execute UNSTRUCTURED (Databricks) RAG Validation Workflow - Direct Project Navigation (from config)")
    public void executeUnstructuredRagValidationWorkflowFromConfig(
            String question,
            String groundTruthContext) {

        LoggerUtil.LOGGER.info("[RAG-WORKFLOW] Starting UNSTRUCTURED (Databricks) RAG validation from config-based project URL");

        // Bind both node-type-aware blocks to the Databricks UC "RAGBuilder" node.
        bindBlocks(PipelineCanvasBuildingBlock.RagBuilderType.DATABRICKS,
                RagEndpointTestingBuildingBlock.RagBuilderType.DATABRICKS);

        // Step 1: Retrieve feature name from external config (same pipeline as structured)
        String featureName = RagPipelineConfig.getFeatureName();

        // Step 2: Poll for login redirect to complete (URL changes from /login)
        projectNavBlock.waitForPageLoadAfterLogin();
        // Step 3: Navigate to project view URL stored in config
        projectNavBlock.navigateToProjectFromConfig();
        // Step 4: Click feature link in workstreams tab to open feature page
        featureNavigationBlock.openFeature(featureName);
        // Step 5: Click Develop stage and capture pipeline canvas components (Databricks node)
        pipelineBlock.openDevelopStageAndCaptureComponents();
        // Step 6: Ask RAG endpoint the test question and capture response
        String answer = endpointBlock.askQuestion(question);
        // Step 7: Assert that LLM returned a non-empty answer
        ragValidationBlock.validateAnswerExists(answer);
        // Step 8: Validate answer quality using AI evaluator against ground truth
        ragValidationBlock.validateAnswerWithAI(question, answer, groundTruthContext);
        // Step 9: Assert all expected pipeline components are present and configured
        ragValidationBlock.assertPipelineComponentsPresent(
                pipelineBlock.isRagBuilderPresent(),
                pipelineBlock.isDataLakePresent(),
                endpointBlock.isTestEndpointPresent());

        LoggerUtil.LOGGER.info("[RAG-WORKFLOW] ✓ UNSTRUCTURED (Databricks) RAG validation workflow completed successfully");
    }

    @Step("Execute UNSTRUCTURED (Databricks) RAG Validation Workflow - Direct Project Navigation")
    public void executeUnstructuredRagValidationWorkflowFromProjectUrl(
            String projectUrl,
            String featureName,
            String question,
            String groundTruthContext) {

        LoggerUtil.LOGGER.info("[RAG-WORKFLOW] Starting UNSTRUCTURED (Databricks) RAG validation from project URL");

        // Bind both node-type-aware blocks to the Databricks UC RAG Builder node.
        bindBlocks(PipelineCanvasBuildingBlock.RagBuilderType.DATABRICKS,
                RagEndpointTestingBuildingBlock.RagBuilderType.DATABRICKS);

        // Step 1: Poll for login redirect to complete (URL changes from /login)
        projectNavBlock.waitForPageLoadAfterLogin();
        // Step 2: Navigate to project URL passed as parameter (external caller provides URL)
        projectNavBlock.navigateToProject(projectUrl);
        // Step 3: Click feature link in workstreams tab to open feature page
        featureNavigationBlock.openFeature(featureName);
        // Step 4: Click Develop stage and capture pipeline canvas components
        pipelineBlock.openDevelopStageAndCaptureComponents();
        // Step 5: Ask RAG endpoint the test question and capture response
        String answer = endpointBlock.askQuestion(question);
        // Step 6: Assert that LLM returned a non-empty answer
        ragValidationBlock.validateAnswerExists(answer);
        // Step 7: Validate answer quality using AI evaluator against ground truth
        ragValidationBlock.validateAnswerWithAI(question, answer, groundTruthContext);
        // Step 8: Assert all expected pipeline components are present and configured
        ragValidationBlock.assertPipelineComponentsPresent(
                pipelineBlock.isRagBuilderPresent(),
                pipelineBlock.isDataLakePresent(),
                endpointBlock.isTestEndpointPresent());

        LoggerUtil.LOGGER.info("[RAG-WORKFLOW] ✓ UNSTRUCTURED (Databricks) RAG validation workflow completed successfully");
    }

    @Step("Execute RAG Validation Workflow")
    public void executeRagValidationWorkflow(
            String featureName,
            String question,
            String groundTruthContext) {

        bindBlocks(PipelineCanvasBuildingBlock.RagBuilderType.STRUCTURED,
                RagEndpointTestingBuildingBlock.RagBuilderType.STRUCTURED);

        // Step 1: Click feature link in workstreams tab to open feature page
        featureNavigationBlock.openFeature(featureName);
        // Step 2: Click Develop stage and capture pipeline canvas components
        pipelineBlock.openDevelopStageAndCaptureComponents();
        // Step 3: Ask RAG endpoint the test question and capture response
        String answer = endpointBlock.askQuestion(question);
        // Step 4: Assert that LLM returned a non-empty answer
        ragValidationBlock.validateAnswerExists(answer);
        // Step 5: Validate answer quality using AI evaluator against ground truth
        ragValidationBlock.validateAnswerWithAI(question, answer, groundTruthContext);
        // Step 6: Assert all expected pipeline components are present and configured
        ragValidationBlock.assertPipelineComponentsPresent(
                pipelineBlock.isRagBuilderPresent(),
                pipelineBlock.isDataLakePresent(),
                endpointBlock.isTestEndpointPresent());
    }



    public boolean isRagBuilderPresent()   { return pipelineBlock.isRagBuilderPresent(); }
    public boolean isDataLakePresent()     { return pipelineBlock.isDataLakePresent(); }
    public boolean isTestEndpointPresent() { return endpointBlock.isTestEndpointPresent(); }
    public boolean didAiEvaluationRun()    { return ragValidationBlock.didAiEvaluationRun(); }

    // ======================================================================
    //  PUBLIC WRAPPER METHODS (for backward compatibility with RagEndtoEndPipelineWorkflow)
    // ======================================================================

    public String testRagEndpointWithQuestion(String question) {
        return endpointBlock.askQuestion(question);
    }

    public void validateAnswerWithAI(String question, String answer, String groundTruthContext) {
        ragValidationBlock.validateAnswerExists(answer);
        ragValidationBlock.validateAnswerWithAI(question, answer, groundTruthContext);
    }

    public void assertPipelineComponentsPresent() {
        ragValidationBlock.assertPipelineComponentsPresent(
                pipelineBlock.isRagBuilderPresent(),
                pipelineBlock.isDataLakePresent(),
                endpointBlock.isTestEndpointPresent());
    }
}
