package UI.DPS.helpers.DataPipeline;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import pages.DPS.DataPipeline.DataPipelineStudioPage;
import pages.DPS.RagIntegrationPage;
import pages.DPS.Utilities.PageUtility;
import utils.CommonMethods;
import utils.LoggerUtil;
import utils.RagJobData;

public class DataPipelineBuilder {

    private final Page page;
    DataPipelineStudioPage dataPipelineStudioPage = null;

    public DataPipelineBuilder(Page page) {
        this.dataPipelineStudioPage = new DataPipelineStudioPage(page);
        this.page = page;
    }

    public void waitForDataPipelinePageLoaded() {
        dataPipelineStudioPage.waitForDataPipelineStudioPageToBeLoaded();
    }

    public void gotoCrawlerPage() {
        dataPipelineStudioPage.buttonDataCrawler().click();
    }

    public void editDataPipeline() {
        dataPipelineStudioPage.buttonPenForEditPipeline().click();
    }

    public void addStage(String stageName, boolean isFirstStage, String previousStageName) {
        dataPipelineStudioPage.page.waitForLoadState();
        CommonMethods.waitForLoaderToDisappear(dataPipelineStudioPage.page);
        dataPipelineStudioPage.page.waitForTimeout(800);

        LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Adding stage: {} (isFirstStage: {})", stageName, isFirstStage);

        Locator addIcon = isFirstStage
                ? dataPipelineStudioPage.iconPlusAddNewStage()
                : dataPipelineStudioPage.iconPlusAddNextStage(previousStageName);

        try {
            addIcon.scrollIntoViewIfNeeded();
        } catch (Exception ignored) {
        }

        addIcon.click();
        LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Clicked add-stage icon");

        try {
            dataPipelineStudioPage.page.waitForCondition(
                    () -> dataPipelineStudioPage.dropdownSelectStageType().isVisible(),
                    new Page.WaitForConditionOptions().setTimeout(10000)
            );
            LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Stage type dropdown appeared successfully");
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[PIPELINE-BUILDER] Dropdown did not appear after click: {}", e.getMessage());
            throw new RuntimeException("Stage type dropdown did not appear", e);
        }

        dataPipelineStudioPage.selectStageTypeFromDropdown(stageName);

        try {
            dataPipelineStudioPage.page.waitForCondition(
                    () -> dataPipelineStudioPage.buttonAddStageConfirm().isVisible(),
                    new Page.WaitForConditionOptions().setTimeout(10000)
            );
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[PIPELINE-BUILDER] Add button did not become visible in time: {}", e.getMessage());
        }

        dataPipelineStudioPage.buttonAddStageConfirm().click();
        LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Added stage: {}", stageName);
    }

    public void addNode(String stageName, String nodeName) {
        dataPipelineStudioPage.page.waitForLoadState();
        CommonMethods.waitForLoaderToDisappear(dataPipelineStudioPage.page);
        dataPipelineStudioPage.page.waitForTimeout(600);

        LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Adding node '{}' to stage '{}'", nodeName, stageName);

        try {
            dataPipelineStudioPage.page.waitForCondition(
                    () -> dataPipelineStudioPage.page.locator("//div[@class='stage-card']").first().isVisible(),
                    new Page.WaitForConditionOptions().setTimeout(8000)
            );
            LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Stage card is visible");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[PIPELINE-BUILDER] Stage card visibility timeout: {}", e.getMessage());
        }

        dataPipelineStudioPage.page.waitForTimeout(800);

        Locator addNodeIcon;

        try {
            addNodeIcon = dataPipelineStudioPage.iconPlusForAddNodeByStageName(stageName);
        } catch (RuntimeException rex) {
            try {
                String pageContent = dataPipelineStudioPage.page.content();
                LoggerUtil.LOGGER.error(
                        "[PIPELINE-BUILDER] ResilientLocator failed to resolve add-node icon for stage '{}'. Page HTML length={}",
                        stageName,
                        pageContent.length()
                );
                LoggerUtil.LOGGER.debug(
                        "[PIPELINE-BUILDER] Page HTML (first 3000 chars): {}",
                        pageContent.substring(0, Math.min(3000, pageContent.length()))
                );
            } catch (Exception dumpEx) {
                LoggerUtil.LOGGER.error("[PIPELINE-BUILDER] Failed to capture page content for debugging: {}", dumpEx.getMessage());
            }
            throw rex;
        }

        try {
            addNodeIcon.scrollIntoViewIfNeeded();
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[PIPELINE-BUILDER] Could not scroll add-node icon into view: {}", e.getMessage());

            String pageContent = dataPipelineStudioPage.page.content();
            LoggerUtil.LOGGER.debug(
                    "[PIPELINE-BUILDER] Page content (first 2000 chars): {}",
                    pageContent.substring(0, Math.min(2000, pageContent.length()))
            );
        }

        try {
            addNodeIcon.click();
            LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Successfully clicked add-node icon for stage '{}'", stageName);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[PIPELINE-BUILDER] Failed to click add-node icon for stage '{}': {}", stageName, e.getMessage());

            String pageContent = dataPipelineStudioPage.page.content();
            LoggerUtil.LOGGER.debug("[PIPELINE-BUILDER] Full page HTML length: {}", pageContent.length());
            LoggerUtil.LOGGER.debug(
                    "[PIPELINE-BUILDER] Page HTML (first 3000 chars): {}",
                    pageContent.substring(0, Math.min(3000, pageContent.length()))
            );

            throw new RuntimeException("Failed to click add-node icon for stage: " + stageName, e);
        }

        try {
            dataPipelineStudioPage.page.waitForCondition(
                    () -> dataPipelineStudioPage.labelNodeNameInPopOverList(nodeName).isVisible(),
                    new Page.WaitForConditionOptions().setTimeout(15000)
            );
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[PIPELINE-BUILDER] Node popover did not appear for node '{}': {}", nodeName, e.getMessage());
            throw new RuntimeException("Node popover did not appear for: " + nodeName, e);
        }

        dataPipelineStudioPage.labelNodeNameInPopOverList(nodeName).hover();
        dataPipelineStudioPage.buttonAddByNodeNameInPopOverList(nodeName).click();

        LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Added node '{}' to stage '{}'", nodeName, stageName);
    }

    public void publishDataPipeline() {
        CommonMethods.clickButton(dataPipelineStudioPage.page, "Publish").click();
    }

    public void publishDataPipelineConfirmation() {
        dataPipelineStudioPage.publishPipelineConfirmation().click();
    }

    public void runDataPipeline() {
        CommonMethods.clickButton(dataPipelineStudioPage.page, "Run Pipeline").click();
    }

    public void connectNodes(String sourceNodeName, String targetNodeName) {
        PageUtility.connectNodes(dataPipelineStudioPage, sourceNodeName, targetNodeName);
    }

    public void addDatabricksNodeDetails(String title, String databricksInstanceName) {
        dataPipelineStudioPage.textboxTechnologyTitle().fill(title);
        dataPipelineStudioPage.selectDatabricksInstanceNameFromDropdown(databricksInstanceName);

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonSaveNodeDetails().isEnabled()
        );

        dataPipelineStudioPage.buttonSaveNodeDetails().click();

        dataPipelineStudioPage.dialogBoxHeadingAddTechnology().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(15000)
        );

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonNodeNameInPipeline(title).isVisible()
        );
    }

    public void addSnowflakeNodeDetails(String title) {
        dataPipelineStudioPage.textboxTechnologyTitle().fill(title);

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonSaveNodeDetails().isEnabled()
        );

        dataPipelineStudioPage.buttonSaveNodeDetails().click();

        dataPipelineStudioPage.dialogBoxHeadingAddTechnology().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(15000)
        );

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonNodeNameInPipeline(title).isVisible()
        );
    }

    public void addStructuredRagBuilderNodeDetails(String title, String snowflakeStructuredRagInstance) {
        dataPipelineStudioPage.textboxTechnologyTitle().fill(title);
        dataPipelineStudioPage.selectSnowflakeStructuredRagInstanceFromDropdown(snowflakeStructuredRagInstance);

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonSaveNodeDetails().isEnabled()
        );

        dataPipelineStudioPage.buttonSaveNodeDetails().click();

        dataPipelineStudioPage.dialogBoxHeadingAddTechnology().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(15000)
        );

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonNodeNameInPipeline(title).isVisible()
        );
    }

    public void addRagBuilderNodeDetails(String title, String ragBuilderType, String instanceName) {
        dataPipelineStudioPage.textboxTechnologyTitle().fill(title);

        if ("Structured RAG Builder".equalsIgnoreCase(ragBuilderType)) {
            dataPipelineStudioPage.selectSnowflakeStructuredRagInstanceFromDropdown(instanceName);
        } else if ("RAG Builder".equalsIgnoreCase(ragBuilderType)) {
            dataPipelineStudioPage.selectDatabricksInstanceNameFromDropdown(instanceName);
        }

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonSaveNodeDetails().isEnabled()
        );

        dataPipelineStudioPage.buttonSaveNodeDetails().click();

        dataPipelineStudioPage.dialogBoxHeadingAddTechnology().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(15000)
        );

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonNodeNameInPipeline(title).isVisible()
        );
    }

    public void configureSnowflakeDatastore(String datastoreName) {
        LoggerUtil.LOGGER.info("[Snowflake Config] Clicking Snowflake node to open configuration panel");

        dataPipelineStudioPage.buttonNodeNameInPipeline("Snowflake").click();
        dataPipelineStudioPage.page.waitForTimeout(1500);

        LoggerUtil.LOGGER.info("[Snowflake Config] Selecting datastore: {}", datastoreName);
        dataPipelineStudioPage.selectSnowflakeDatastoreFromDropdown(datastoreName);

        LoggerUtil.LOGGER.info("[Snowflake Config] Waiting for Save button to be enabled");

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonSaveNodeDetails().isEnabled()
        );

        LoggerUtil.LOGGER.info("[Snowflake Config] Clicking Save");
        dataPipelineStudioPage.buttonSaveNodeDetails().click();

        LoggerUtil.LOGGER.info("[Snowflake Config] Closing configuration panel");
        dataPipelineStudioPage.closeConfigurationPanel();

        LoggerUtil.LOGGER.info("[Snowflake Config] Waiting for panel to fully close");
        dataPipelineStudioPage.page.waitForTimeout(2000);
    }

    public void configureStructuredRagBuilder() {
        LoggerUtil.LOGGER.info("[RAG Builder Config] STEP 5: Clicking Structured RAG Builder node to open config panel");

        dataPipelineStudioPage.buttonNodeNameInPipeline("Structured RAG Builder").click();
        dataPipelineStudioPage.page.waitForTimeout(1500);

        LoggerUtil.LOGGER.info("[RAG Builder Config] Clicking 'Create Job' button");
        dataPipelineStudioPage.clickCreateJobButton();

        LoggerUtil.LOGGER.info("[RAG Builder Config] Waiting for RAG Integration page to load");

        RagIntegrationPage ragPage = new RagIntegrationPage(dataPipelineStudioPage.page);
        dataPipelineStudioPage.page.waitForTimeout(1000);

        RagJobData defaultJob = RagJobData.builder()
                .jobName("AutoJob-" + System.currentTimeMillis())
                .semanticViewName("ragview-" + System.currentTimeMillis())
                .cortexAgentName("RAGCortexAgent")
                .inferenceModel("Claude Opus 4.5")
                .orchestrationInstruction(
                        "You are a business analytics agent designed to answer questions using the available semantic model. " +
                                "Interpret user requests in the context of business intelligence, operations, sales, procurement, " +
                                "logistics, customer analytics, product performance, and regional insights. Use the semantic model " +
                                "to identify relevant entities, metrics, and relationships required to answer the user's question. " +
                                "Prioritize meaningful business metrics such as revenue, order volume, customer counts, supplier counts, " +
                                "quantities, costs, discounts, margins, delivery performance, operational KPIs, and trend analysis. " +
                                "When users request trends, analyze data over the appropriate time dimensions. When users request " +
                                "geographic insights, leverage location and regional attributes available in the semantic model. " +
                                "Generate accurate queries based on the semantic definitions and provide business-focused insights " +
                                "rather than raw data whenever possible."
                )
                .responseFormat(
                        "Return concise, business-friendly responses with clear summaries and actionable insights. " +
                                "Use tables when they improve readability. Highlight key trends, comparisons, and anomalies where relevant. " +
                                "If the request is ambiguous, make the most reasonable business interpretation, state any assumptions briefly, " +
                                "and proceed with the analysis."
                )
                .build();

        try {
            LoggerUtil.LOGGER.info("[RAG Builder Config] Filling Job Name: {}", defaultJob.getJobName());

            ragPage.textboxJobName().fill(defaultJob.getJobName());
            ragPage.buttonNext().click();
            dataPipelineStudioPage.page.waitForTimeout(800);

            if (ragPage.buttonAddTables().count() > 0) {
                ragPage.buttonAddTables().click();
                dataPipelineStudioPage.page.waitForTimeout(800);

                if (ragPage.checkboxSelectAllTables().count() > 0) {
                    ragPage.checkboxSelectAllTables().click();
                }

                ragPage.buttonAddSelectedTables().click();
                dataPipelineStudioPage.page.waitForTimeout(800);
            }

            if (ragPage.textboxSemanticViewName().count() > 0) {
                ragPage.textboxSemanticViewName().fill(defaultJob.getSemanticViewName());
            }

            ragPage.buttonNext().click();
            dataPipelineStudioPage.page.waitForTimeout(800);

            if (ragPage.textboxCortexAgentName().count() > 0) {
                ragPage.textboxCortexAgentName().fill(defaultJob.getCortexAgentName());
            }

            if (defaultJob.getInferenceModel() != null) {
                ragPage.dropdownInferenceModel().click();
                dataPipelineStudioPage.page.waitForTimeout(500);
                ragPage.inferenceModelOption(defaultJob.getInferenceModel()).click();
            }

            if (ragPage.textareaOrchestrationInstruction().count() > 0) {
                ragPage.textareaOrchestrationInstruction().fill(defaultJob.getOrchestrationInstruction());
            }

            if (ragPage.textareaResponseFormat().count() > 0) {
                ragPage.textareaResponseFormat().fill(defaultJob.getResponseFormat());
            }

            ragPage.buttonComplete().click();
            dataPipelineStudioPage.page.waitForTimeout(1500);

            LoggerUtil.LOGGER.info("[RAG Builder Config] Create Job flow completed with default job data");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[RAG Builder Config] Error during Create Job flow: {}", e.getMessage());
            throw e;
        }
    }

    public void clickOnNodeInPipeline(String nodeName) {
        dataPipelineStudioPage.buttonNodeNameInPipeline(nodeName).click();
    }

    public void waitForPipelineSuccess() {
        Locator successStatus = dataPipelineStudioPage.successStatus();

        long timeout = 20 * 60 * 1000;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeout) {
            if (successStatus.isVisible()) {
                LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Pipeline completed successfully.");
                return;
            }

            LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Pipeline still running...");
            page.waitForTimeout(10000);
        }

        throw new RuntimeException("Pipeline did not complete successfully within 20 minutes.");
    }
    /**
     * Fails FAST (2s) if a technology-details / add-technology modal is still open,
     * instead of letting the next click hang for 90s while the modal intercepts pointer
     * events. Call this after saving node details and before adding the next stage.
     *
     * <p>The modal in DPS is: {@code <div role="dialog" aria-modal="true" class="modal show">}.
     */
    public void ensureNoBlockingModal() {
        try {
            Locator modal = dataPipelineStudioPage.page.locator("div[role='dialog'].modal.show");
            if (modal.count() > 0 && modal.first().isVisible()) {
                throw new RuntimeException(
                        "[PIPELINE-BUILDER] A technology-details modal is STILL OPEN — the previous "
                                + "node's details were not saved (Technology Title / Save). Cannot proceed to "
                                + "the next action.");
            }
            LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] ✓ No blocking modal open");
        } catch (RuntimeException re) {
            throw re; // rethrow our explicit guard failure
        } catch (Exception e) {
            // Locator/visibility probe issues should not mask the real flow; just log.
            LoggerUtil.LOGGER.warn("[PIPELINE-BUILDER] Modal check skipped: {}", e.getMessage());
        }
    }

    /**
     * HARDENED replacement for the existing addSnowflakeNodeDetails(String).
     * Robustly fills the "Technology Title" (React-controlled input) and Saves, so the
     * Add-Technology modal actually closes.
     *
     * Improvements vs original:
     *  - waits for the title input to be attached before typing
     *  - uses click + pressSequentially (fires real React input events) instead of fill()
     *  - verifies the value committed; retries via fill + dispatch input/change if needed
     *  - fast-fails (15s) if Save never enables, instead of a downstream 90s hang
     */
    public void addSnowflakeNodeDetailsHardened(String title) {
        Locator titleBox = dataPipelineStudioPage.textboxTechnologyTitle();

        // 1) Ensure the input is present/attached
        titleBox.waitFor(new Locator.WaitForOptions().setTimeout(10000));

        // 2) React-safe input: click, clear, type (real key events enable Save)
        titleBox.click();
        try {
            titleBox.fill("");
        } catch (Exception ignored) {
        }
        titleBox.pressSequentially(
                title,
                new Locator.PressSequentiallyOptions().setDelay(50));

        // 3) Verify the value committed; if not, retry with fill + explicit events
        try {
            if (!title.equals(titleBox.inputValue())) {
                LoggerUtil.LOGGER.warn(
                        "[PIPELINE-BUILDER] Title not committed via typing, retrying via fill + events");
                titleBox.fill(title);
                titleBox.dispatchEvent("input");
                titleBox.dispatchEvent("change");
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[PIPELINE-BUILDER] inputValue verify skipped: {}", e.getMessage());
        }

        // 4) Fast-fail wait for Save to ENABLE (15s), then click
        Locator save = dataPipelineStudioPage.buttonSaveNodeDetails();
        try {
            dataPipelineStudioPage.page.waitForCondition(
                    () -> save.isEnabled(),
                    new Page.WaitForConditionOptions().setTimeout(15000));
        } catch (Exception te) {
            LoggerUtil.LOGGER.error(
                    "[PIPELINE-BUILDER] Snowflake 'Save' never enabled — Technology Title '{}' "
                            + "did not register.", title);
            throw new RuntimeException(
                    "Snowflake Save button never enabled — Technology Title not committed.", te);
        }
        save.click();
        LoggerUtil.LOGGER.info("[PIPELINE-BUILDER] Saved Snowflake technology details (title='{}')", title);

        // 5) Wait for the modal to close, then confirm the node chip appears on canvas
        dataPipelineStudioPage.dialogBoxHeadingAddTechnology().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(15000));

        dataPipelineStudioPage.page.waitForCondition(
                () -> dataPipelineStudioPage.buttonNodeNameInPipeline(title).isVisible());
    }

}