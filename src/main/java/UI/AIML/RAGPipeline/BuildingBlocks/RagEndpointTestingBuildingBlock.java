package UI.AIML.RAGPipeline.BuildingBlocks;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.AIML.ConversaPage;
import pages.AIML.DataPipelineStudioPage;
import utils.LoggerUtil;

public class RagEndpointTestingBuildingBlock {

    /** Identifies which RAG Builder node on the canvas this flow targets. */
    public enum RagBuilderType {
        STRUCTURED,   // Snowflake "StructuredRAGBuilder3" node
        DATABRICKS    // Databricks UC "RAGBuilder" node
    }

    private final Page page;
    private final DataPipelineStudioPage dps;

    private final RagBuilderType ragBuilderType;

    private boolean testEndpointPresent;

    /**
     * Default constructor — retains previous behaviour by targeting the
     * Structured (Snowflake) RAG Builder node.
     */
    public RagEndpointTestingBuildingBlock(Page page) {
        this(page, RagBuilderType.STRUCTURED);
    }

    /**
     * Preferred constructor — lets each flow choose which RAG Builder node to open.
     *
     * @param page            Playwright page
     * @param ragBuilderType  STRUCTURED (Snowflake) or DATABRICKS (UC)
     */
    public RagEndpointTestingBuildingBlock(Page page, RagBuilderType ragBuilderType) {
        this.page = page;
        this.dps = new DataPipelineStudioPage(page);
        this.ragBuilderType = ragBuilderType;
    }

    @Step("Test RAG endpoint with question: {question}")
    public String askQuestion(String question) {

        openRagBuilderPanel();

        if (!testEndpointPresent) {

            LoggerUtil.LOGGER.warn(
                    "[RAG-ENDPOINT-BLOCK] Test Endpoint not found");

            return null;
        }

        ConversaPage conversa =
                openConversaTestEndpoint();

        LoggerUtil.LOGGER.info(
                "[RAG-ENDPOINT-BLOCK] Asking: {}",
                question);

        return conversa.ask(question, 60000);
    }

    public boolean isTestEndpointPresent() {
        return testEndpointPresent;
    }

    private void openRagBuilderPanel() {

        LoggerUtil.LOGGER.info(
                "[RAG-ENDPOINT-BLOCK] Opening RAG Builder panel ({})",
                ragBuilderType);

        // Click the correct node based on the flow this block is running,
        // and remember the node title so the panel-heading check matches.
        String nodeTitle;
        switch (ragBuilderType) {
            case DATABRICKS:
                dps.clickDatabricksRagBuilderNode();
                nodeTitle = "RAGBuilder";
                break;
            case STRUCTURED:
            default:
                dps.clickStructuredRagBuilderNode();
                nodeTitle = "StructuredRAGBuilder3";
                break;
        }

        page.waitForTimeout(2000);

        dps.waitForRagPanel(nodeTitle);

        testEndpointPresent =
                dps.isTestRagEndpointPresent();
    }

    private ConversaPage openConversaTestEndpoint() {

        LoggerUtil.LOGGER.info(
                "[RAG-ENDPOINT-BLOCK] Opening Conversa");

        // Use the robust click (handles button/div/span rendering) inside the
        // tab-switch callback so both Snowflake and Databricks panels work.
        Page conversaTab =
                ConversaPage.switchToConversaTab(
                        page,
                        () -> dps.clickTestEndpointUrl());

        return new ConversaPage(conversaTab);
    }
}
