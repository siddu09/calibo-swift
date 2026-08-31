package UI.AIML.RAGPipeline.BuildingBlocks;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.AIML.DataPipelineStudioPage;
import utils.CommonMethods;
import utils.LoggerUtil;

public class PipelineCanvasBuildingBlock {

    /** Identifies which RAG Builder node on the canvas this flow targets. */
    public enum RagBuilderType {
        STRUCTURED,   // Snowflake "StructuredRAGBuilder3" node
        DATABRICKS    // Databricks UC "RAGBuilder" node
    }

    private final Page page;
    private final DataPipelineStudioPage dps;

    private final RagBuilderType ragBuilderType;

    private boolean ragBuilderPresent;
    private boolean dataLakePresent;

    /**
     * Default constructor — retains previous behaviour by targeting the
     * Structured (Snowflake) RAG Builder node.
     */
    public PipelineCanvasBuildingBlock(Page page) {
        this(page, RagBuilderType.STRUCTURED);
    }

    /**
     * Preferred constructor — lets each flow choose which RAG Builder node to
     * wait for / detect on the canvas.
     *
     * @param page            Playwright page
     * @param ragBuilderType  STRUCTURED (Snowflake) or DATABRICKS (UC)
     */
    public PipelineCanvasBuildingBlock(Page page, RagBuilderType ragBuilderType) {
        this.page = page;
        this.dps = new DataPipelineStudioPage(page);
        this.ragBuilderType = ragBuilderType;
    }

    @Step("Open Develop stage and capture pipeline components")
    public void openDevelopStageAndCaptureComponents() {

        clickDevelopStage();

        waitForPipelineCanvas();

        captureComponentPresence();
    }

    public boolean isRagBuilderPresent() {
        return ragBuilderPresent;
    }

    public boolean isDataLakePresent() {
        return dataLakePresent;
    }

    /** Resolves the RAG Builder node locator for the configured flow. */
    private Locator ragBuilderNode() {
        return ragBuilderType == RagBuilderType.DATABRICKS
                ? dps.databricksRagBuilderNode()
                : dps.structuredRagBuilderNode();
    }

    private void clickDevelopStage() {

        LoggerUtil.LOGGER.info(
                "[PIPELINE-BLOCK] Clicking Develop stage");

        Locator developStageLocator =
                new CommonMethods()
                        .clickStageInPanel(page, "Develop");

        developStageLocator.waitFor(
                new Locator.WaitForOptions()
                        .setTimeout(10000));

        developStageLocator.click();

        LoggerUtil.LOGGER.info(
                "[PIPELINE-BLOCK] ✓ Develop stage opened");
    }

    private void waitForPipelineCanvas() {

        LoggerUtil.LOGGER.info(
                "[PIPELINE-BLOCK] Waiting for pipeline canvas ({})",
                ragBuilderType);

        ragBuilderNode()
                .first()
                .waitFor(
                        new Locator.WaitForOptions()
                                .setTimeout(30000));

        LoggerUtil.LOGGER.info(
                "[PIPELINE-BLOCK] ✓ Pipeline rendered");
    }

    private void captureComponentPresence() {

        ragBuilderPresent =
                ragBuilderNode().count() > 0;

        dataLakePresent =
                dps.dataLakeNode().count() > 0;

        LoggerUtil.LOGGER.info(
                "[PIPELINE-BLOCK] RAG Builder={}, Data Lake={}",
                ragBuilderPresent,
                dataLakePresent);
    }
}
