package pages.AIML;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

/**
 * Page Object for the Data Pipeline Studio (yFiles SVG canvas) and the RAG Builder
 * side-panel in Calibo Accelerate.
 *
 * <p>SVG graph nodes require XPath with {@code name()='...'} because SVG elements live in
 * a different XML namespace than HTML, so plain {@code //g} / {@code //text} selectors do
 * NOT match them.
 *
 * <p>Two distinct RAG Builder nodes are supported, each identified by its unique icon href:
 * <ul>
 *   <li>{@link #structuredRagBuilderNode()}  -> "StructuredRAGBuilder3"
 *       (icon: calibo_snowflake_structured_rag_builder-*.png)</li>
 *   <li>{@link #databricksRagBuilderNode()}  -> "RAGBuilder"
 *       (icon: calibo_databricks_uc_rag_builder-*.png)</li>
 * </ul>
 *
 * <p>The two RAG Builder side-panels differ:
 * <ul>
 *   <li>Snowflake  : section "Test RAG Endpoint", "RAG Snowflake Serving Endpoint"</li>
 *   <li>Databricks : section "User Testing App", "RAG Databricks Serving Endpoint"</li>
 * </ul>
 * Both, however, expose a "Test Endpoint URL" button and an "Instance" + "Name"/"Status"
 * table, so panel readiness is detected flow-agnostically via those markers.
 */
public class DataPipelineStudioPage {

    private final Page page;

    public DataPipelineStudioPage(Page page) {
        this.page = page;
    }

    // ======================================================================
    //  Pipeline canvas nodes (SVG)
    // ======================================================================

    /**
     * The "Structured RAG Builder" pipeline node (SVG {@code <g>} group).
     * Example DOM:
     *   tspan  -> StructuredRAGBuilder3
     *   image  -> /assets/calibo_snowflake_structured_rag_builder-*.png
     */
    public Locator structuredRagBuilderNode() {
        return new ResilientLocator(page, "Structured RAG Builder node")
                .byXPath("//*[name()='image'][contains(@href,'structured_rag_builder')]"
                        + "/ancestor::*[name()='g'][contains(@class,'cursor-pointer')][1]")
                .byXPath("//*[name()='tspan'][contains(text(),'StructuredRAGBuilder')]"
                        + "/ancestor::*[name()='g'][contains(@class,'cursor-pointer')][1]")
                .resolve();
    }

    /**
     * The "Databricks UC RAG Builder" pipeline node (SVG {@code <g>} group).
     * Example DOM:
     *   tspan  -> RAGBuilder
     *   image  -> /assets/calibo_databricks_uc_rag_builder-*.png
     */
    public Locator databricksRagBuilderNode() {
        return new ResilientLocator(page, "Databricks UC RAG Builder node")
                .byXPath("//*[name()='image'][contains(@href,'calibo_databricks_uc_rag_builder')]"
                        + "/ancestor::*[name()='g'][contains(@class,'cursor-pointer')][1]")
                .byXPath("//*[name()='tspan'][normalize-space()='RAGBuilder']"
                        + "/ancestor::*[name()='g'][contains(@class,'cursor-pointer')][1]")
                .resolve();
    }

    /**
     * The "Data Lake" pipeline node (rendered as node "SNL1" with a snowflake icon).
     */
    public Locator dataLakeNode() {
        return new ResilientLocator(page, "Data Lake node")
                .byXPath("//*[name()='image'][contains(@href,'snowflake')]"
                        + "/ancestor::*[name()='g'][1]")
                .byXPath("//*[name()='text'][@class='pipeline-node-name']"
                        + "[contains(.,'SNL')]/ancestor::*[name()='g'][1]")
                .resolve();
    }

    // --- Raw, non-throwing node locators (used to POLL for canvas readiness) ---

    public Locator structuredRagBuilderNodeRaw() {
        return page.locator("xpath=//*[name()='image']"
                + "[contains(@href,'structured_rag_builder')]"
                + "/ancestor::*[name()='g'][contains(@class,'cursor-pointer')][1]");
    }

    public Locator databricksRagBuilderNodeRaw() {
        return page.locator("xpath=//*[name()='image']"
                + "[contains(@href,'calibo_databricks_uc_rag_builder')]"
                + "/ancestor::*[name()='g'][contains(@class,'cursor-pointer')][1]");
    }

    /** Polls (non-throwing) until the given raw node appears, up to timeoutMs. */
    public boolean waitForNodeRaw(Locator rawNode, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (rawNode.count() > 0) {
                    rawNode.first().scrollIntoViewIfNeeded();
                    return true;
                }
            } catch (Exception ignored) {}
            page.waitForTimeout(1000);
        }
        return false;
    }

    // ======================================================================
    //  Node click helpers (SVG canvas swallows clicks -> escalate)
    // ======================================================================

    public void clickStructuredRagBuilderNode() {
        clickSvgNode(structuredRagBuilderNode(), "Structured RAG Builder");
    }

    public void clickDatabricksRagBuilderNode() {
        clickSvgNode(databricksRagBuilderNode(), "Databricks UC RAG Builder");
    }

    public void clickDataLakeNode() {
        clickSvgNode(dataLakeNode(), "Data Lake");
    }

    private void clickSvgNode(Locator node, String label) {
        node.scrollIntoViewIfNeeded();
        try {
            node.click();
            LoggerUtil.LOGGER.info("[DPS] Clicked {} node (normal)", label);
            return;
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[DPS] {} normal click failed, trying force: {}", label, e.getMessage());
        }
        try {
            node.click(new Locator.ClickOptions().setForce(true));
            LoggerUtil.LOGGER.info("[DPS] Clicked {} node (force)", label);
            return;
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[DPS] {} force click failed, trying bounding-box: {}", label, e.getMessage());
        }
        BoundingBox b = node.boundingBox();
        if (b != null) {
            page.mouse().click(b.x + b.width / 2, b.y + b.height / 2);
            LoggerUtil.LOGGER.info("[DPS] Clicked {} node (bounding-box centre)", label);
        } else {
            node.dispatchEvent("click");
            LoggerUtil.LOGGER.info("[DPS] Dispatched click event on {} node", label);
        }
    }

    // ======================================================================
    //  RAG Builder side panel (opens after clicking the RAG Builder node)
    // ======================================================================

    /**
     * The side-panel heading confirming the RAG Builder panel is open.
     *
     * <p>IMPORTANT: the panel heading is the NODE'S OWN NAME (e.g. "StructuredRAGBuilder3"
     * or "RAGBuilder"), shown next to an edit pencil. As a flow-agnostic fallback we also
     * match the always-present "Instance" label and "Name"/"Status" table headers.
     *
     * @param expectedNodeTitle the node name shown as the panel title
     */
    public Locator ragPanelHeading(String expectedNodeTitle) {
        return new ResilientLocator(page, "RAG Builder panel heading")
                .byXPath("//*[normalize-space()='" + expectedNodeTitle + "']")
                .byXPath("//*[contains(normalize-space(),'" + expectedNodeTitle + "')]")
                .byXPath("//*[normalize-space()='Instance']")
                .byXPath("//*[normalize-space()='Name'][following::*[normalize-space()='Status']]")
                .resolve();
    }

    /** Backward-compatible generic overload (flow-agnostic panel markers). */
    public Locator ragPanelHeading() {
        return new ResilientLocator(page, "RAG Builder panel heading (generic)")
                .byXPath("//*[normalize-space()='Instance']")
                .byXPath("//*[normalize-space()='Name'][following::*[normalize-space()='Status']]")
                .resolve();
    }

    /** The serving-endpoint section heading (Snowflake or Databricks). */
    public Locator servingEndpointSection() {
        return new ResilientLocator(page, "RAG Serving Endpoint section")
                .byXPath("//*[normalize-space()='RAG Snowflake Serving Endpoint']")
                .byXPath("//*[normalize-space()='RAG Databricks Serving Endpoint']")
                .byXPath("//*[contains(normalize-space(),'Serving Endpoint')]")
                .resolve();
    }

    /** The "Copy Endpoint URL" button. */
    public Locator copyEndpointUrlButton() {
        return new ResilientLocator(page, "Copy Endpoint URL button")
                .byXPath("//button[normalize-space()='Copy Endpoint URL']")
                .byXPath("//*[normalize-space()='Copy Endpoint URL'][self::button or self::a]")
                .resolve();
    }

    /** The "View in Snowflake" / "View in Databricks" button. */
    public Locator viewInPlatformButton() {
        return new ResilientLocator(page, "View in platform button")
                .byXPath("//button[normalize-space()='View in Snowflake']")
                .byXPath("//button[normalize-space()='View in Databricks']")
                .byXPath("//*[starts-with(normalize-space(),'View in')][self::button or self::a]")
                .resolve();
    }

    /**
     * The endpoint-testing SECTION. Snowflake calls it "Test RAG Endpoint"; Databricks
     * calls it "User Testing App". We accept either, and also fall back to the shared
     * "Test Endpoint URL" button which both panels render.
     */
    public Locator testRagEndpointSection() {
        return new ResilientLocator(page, "Endpoint testing section")
                .byXPath("//*[normalize-space()='Test RAG Endpoint']")
                .byXPath("//*[normalize-space()='User Testing App']")
                .byXPath("//*[contains(normalize-space(),'Test RAG Endpoint')]")
                .byXPath("//*[contains(normalize-space(),'User Testing App')]")
                .byXPath("//*[normalize-space()='Test Endpoint URL']")
                .resolve();
    }

    /**
     * Raw, non-throwing locator used for polling the async-loaded testing section.
     * Matches BOTH panel variants (Snowflake "Test RAG Endpoint" and Databricks
     * "User Testing App"), plus the shared "Test Endpoint URL" button.
     */
    public Locator testRagEndpointSectionRaw() {
        return page.locator("xpath="
                + "//*[contains(normalize-space(),'Test RAG Endpoint')]"
                + " | //*[contains(normalize-space(),'User Testing App')]"
                + " | //*[normalize-space()='Test Endpoint URL']");
    }

    /**
     * The "Test Endpoint URL" button. Present in BOTH the Snowflake ("Test RAG Endpoint")
     * and Databricks ("User Testing App") panels. The control may render as a button,
     * anchor, or a styled div/span, so we cast a wider net.
     */
    public Locator testEndpointUrlButton() {
        return new ResilientLocator(page, "Test Endpoint URL button")
                .byXPath("//button[normalize-space()='Test Endpoint URL']")
                .byXPath("//*[normalize-space()='Test Endpoint URL'][self::button or self::a]")
                .byXPath("//*[normalize-space()='Test Endpoint URL']"
                        + "[self::div or self::span or @role='button']")
                .byXPath("//*[normalize-space(text())='Test Endpoint URL']")
                .resolve();
    }

    /** Raw, non-throwing locator for the Test Endpoint URL button (flow-agnostic poll). */
    public Locator testEndpointUrlButtonRaw() {
        return page.locator("xpath=//*[normalize-space()='Test Endpoint URL']");
    }

    /** The "Start" button on the RAG Integration / UC RAG Builder Job row. */
    public Locator ragIntegrationJobStartButton() {
        return new ResilientLocator(page, "RAG Integration Job Start button")
                .byXPath("//tr[.//*[contains(normalize-space(),'RAG Integration Job')]]"
                        + "//button[normalize-space()='Start']")
                .byXPath("//tr[.//*[contains(normalize-space(),'UC RAG Builder Job')]]"
                        + "//button[normalize-space()='Start']")
                .byXPath("//button[normalize-space()='Start']")
                .resolve();
    }

    /** Closes the side panel (the X in the top-right of the panel). */
    public Locator panelCloseButton() {
        return new ResilientLocator(page, "Side panel close")
                .byXPath("//*[contains(@class,'panel') or contains(@class,'drawer')]"
                        + "//button[contains(@class,'close') or @aria-label='Close']")
                .byCss("button[aria-label='Close']")
                .resolve();
    }

    // ======================================================================
    //  Panel readiness
    // ======================================================================

    public void waitForRagPanel() {
        waitForRagPanel(null);
    }

    /**
     * Flow-aware variant: pass the expected node title (e.g. "StructuredRAGBuilder3"
     * or "RAGBuilder") so the heading check matches the actual panel header.
     *
     * <p>Readiness is confirmed by polling a non-throwing locator that matches EITHER
     * panel's testing section ("Test RAG Endpoint" / "User Testing App") or the shared
     * "Test Endpoint URL" button.
     */
    public void waitForRagPanel(String expectedNodeTitle) {
        // 1. Heading appears quickly (node-name based, with generic fallbacks)
        Locator heading = (expectedNodeTitle != null && !expectedNodeTitle.isBlank())
                ? ragPanelHeading(expectedNodeTitle)
                : ragPanelHeading();
        heading.first().waitFor(
                new Locator.WaitForOptions().setTimeout(15000));
        LoggerUtil.LOGGER.info("[DPS] RAG panel heading visible ({}), waiting for content to load...",
                expectedNodeTitle != null ? expectedNodeTitle : "generic");

        // 2. Poll up to 45s for the testing section / Test Endpoint URL button
        long deadline = System.currentTimeMillis() + 45000;
        boolean found = false;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (testRagEndpointSectionRaw().count() > 0) {
                    found = true;
                    break;
                }
            } catch (Exception ignored) {}
            page.waitForTimeout(1000);
        }

        if (found) {
            LoggerUtil.LOGGER.info("[DPS] RAG Builder side panel fully rendered");
            // 3. Bring the button/section into view (it can be below the fold)
            try {
                testRagEndpointSectionRaw().first().scrollIntoViewIfNeeded();
            } catch (Exception ignored) {}
        } else {
            LoggerUtil.LOGGER.warn("[DPS] Endpoint testing section did not appear within 45s");
        }
    }

    /**
     * Convenience: true if the endpoint testing area is present (non-throwing).
     * Works for both panels by keying off the shared "Test Endpoint URL" button.
     */
    public boolean isTestRagEndpointPresent() {
        try {
            return testEndpointUrlButtonRaw().count() > 0
                    || testRagEndpointSectionRaw().count() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Robust click of the "Test Endpoint URL" button, escalating like SVG node clicks
     * (some panels render the control as a styled div that swallows a normal click).
     */
    public void clickTestEndpointUrl() {
        Locator btn = testEndpointUrlButton();
        btn.scrollIntoViewIfNeeded();
        try {
            btn.click();
            LoggerUtil.LOGGER.info("[DPS] Clicked 'Test Endpoint URL' (normal)");
            return;
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[DPS] 'Test Endpoint URL' normal click failed, trying force: {}", e.getMessage());
        }
        try {
            btn.click(new Locator.ClickOptions().setForce(true));
            LoggerUtil.LOGGER.info("[DPS] Clicked 'Test Endpoint URL' (force)");
            return;
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[DPS] 'Test Endpoint URL' force click failed, trying bounding-box: {}", e.getMessage());
        }
        BoundingBox b = btn.boundingBox();
        if (b != null) {
            page.mouse().click(b.x + b.width / 2, b.y + b.height / 2);
            LoggerUtil.LOGGER.info("[DPS] Clicked 'Test Endpoint URL' (bounding-box centre)");
        } else {
            btn.dispatchEvent("click");
            LoggerUtil.LOGGER.info("[DPS] Dispatched click event on 'Test Endpoint URL'");
        }
    }
}
