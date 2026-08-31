package tests.ui.AIML;

import UI.AIML.RAGPipeline.workflows.RagEndtoEndPipelineWorkflow;
import assertionsHandler.AssertionManager;
import configHandler.ConfigManager;
import org.testng.annotations.Test;
import tests.base.BaseUITest;
import utils.LoggerUtil;

public class RagValidationE2ETest extends BaseUITest {

    @Test(groups = {"RAG","RAG-E2E"}, description = "Provision a new RAG pipeline end-to-end and validate via Conversa")
    public void provisionNewRagPipelineAndValidate() {

        RagEndtoEndPipelineWorkflow workflow =
                new RagEndtoEndPipelineWorkflow(page);

        String tenant = System.getProperty("tenantName");
        
        workflow.provisionAndValidateRagPipeline(tenant,RagTestData.QUESTION_WHAT_TABLES,RagTestData.RAG_BUILDER_SCHEMA_CONTEXT);

        AssertionManager.assertAll();

    }
}