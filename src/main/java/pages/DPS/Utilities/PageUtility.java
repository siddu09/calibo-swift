package pages.DPS.Utilities;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitForSelectorState;
import pages.DPS.CrawlerCatalog.Models.TableColumnForCrawlerCatalog;
import pages.DPS.CrawlerCatalog.Models.TableMetadataForCrawlerCatalog;
import pages.DPS.CrawlerCatalog.ViewDataCrawlerPage;
import pages.DPS.DataPipeline.DataPipelineStudioPage;
import utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PageUtility {

    /**
     * Waits until the node is ready for mouse interaction after an SVG re-render.
     */
    private static void waitForNodeInteractable(Page page, Locator node) {

        page.waitForCondition(() -> {

            BoundingBox box = node.boundingBox();

            if (box == null) {
                return false;
            }

            double x = box.x + box.width / 2;
            double y = box.y + box.height / 2;

            Object result = page.evaluate(
                    "([x, y]) => {" +
                            "    const element = document.elementFromPoint(x, y);" +
                            "    return element !== null && " +
                            "           element.closest('.cursor-pointer') !== null;" +
                            "}",
                    Arrays.asList(x, y));

            return Boolean.TRUE.equals(result);

        }, new Page.WaitForConditionOptions()
                .setTimeout(10000));
    }

    /**
     * Returns the bounding box only after it stops changing between two reads,
     * so we don't grab coordinates mid SVG re-render/animation.
     */
    private static BoundingBox getStableBoundingBox(Page page, Locator node) {

        BoundingBox[] stableBox = new BoundingBox[1];

        page.waitForCondition(() -> {

            BoundingBox first = node.boundingBox();
            if (first == null) {
                return false;
            }

            page.waitForTimeout(150);

            BoundingBox second = node.boundingBox();
            if (second == null) {
                return false;
            }

            boolean stable = Double.compare(first.x, second.x) == 0
                    && Double.compare(first.y, second.y) == 0
                    && Double.compare(first.width, second.width) == 0
                    && Double.compare(first.height, second.height) == 0;

            if (stable) {
                stableBox[0] = second;
            }

            return stable;

        }, new Page.WaitForConditionOptions().setTimeout(10000));

        return stableBox[0];
    }
// should be in Helper class, but for now in PageUtility to avoid circular dependency issues
    public static void connectNodes(DataPipelineStudioPage dataPipelineStudioPage, String sourceNodeName, String targetNodeName) {

        Page page = dataPipelineStudioPage.page;

        Locator source = dataPipelineStudioPage.buttonNodeNameInPipeline(sourceNodeName);
        Locator target = dataPipelineStudioPage.buttonNodeNameInPipeline(targetNodeName);

        source.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        target.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));

        waitForNodeInteractable(page, source);
        waitForNodeInteractable(page, target);
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info("[CONNECT-NODES] Attempting to connect {} -> {}", sourceNodeName, targetNodeName);

        // Try bounding box approach first (for non-SVG scenarios)
        BoundingBox sourceBox = source.boundingBox();
        BoundingBox targetBox = target.boundingBox();

        if (sourceBox != null && targetBox != null) {
            LoggerUtil.LOGGER.info("[CONNECT-NODES] Using drag-based connection (bounding boxes available)");
            connectNodesByDrag(page, source, target, sourceBox, targetBox, sourceNodeName, targetNodeName);
        } else {
            LoggerUtil.LOGGER.warn("[CONNECT-NODES] Bounding boxes unavailable (SVG elements). Source null: {}, Target null: {}", sourceBox == null, targetBox == null);
            LoggerUtil.LOGGER.info("[CONNECT-NODES] Falling back to click-based connection method");
            connectNodesByClick(page, source, target, sourceNodeName, targetNodeName);
        }
    }

    private static void connectNodesByDrag(Page page, Locator source, Locator target, BoundingBox sourceBox, BoundingBox targetBox, String sourceNodeName, String targetNodeName) {
        double sourceX = sourceBox.x + sourceBox.width / 2;
        double sourceY = sourceBox.y + sourceBox.height;   // bottom border

        double targetX = targetBox.x + targetBox.width / 2;
        double targetY = targetBox.y;                       // top border

        Locator paths = page.locator("svg path[fill='none'][stroke-width='3']");
        int pathsBefore = paths.count();

        Locator portCandidate = page.locator("g.yfiles-port-candidate-template.yfiles-valid");

        page.mouse().move(sourceX, sourceY);
        page.waitForTimeout(300);
        page.mouse().down();
        page.waitForTimeout(300);
        page.mouse().move(sourceX, sourceY + 10, new Mouse.MoveOptions().setSteps(5));

        try {
            page.waitForCondition(() -> portCandidate.count() > 0,
                    new Page.WaitForConditionOptions().setTimeout(3000));
        } catch (Exception e) {
            page.waitForTimeout(600);
        }

        double midX = (sourceX + targetX) / 2;
        double midY = (sourceY + targetY) / 2;
        page.mouse().move(midX, midY, new Mouse.MoveOptions().setSteps(15));
        page.waitForTimeout(300);
        page.mouse().move(targetX, targetY, new Mouse.MoveOptions().setSteps(15));
        page.waitForTimeout(300);
        page.mouse().up();
        page.waitForTimeout(800);

        try {
            page.waitForCondition(() -> paths.count() > pathsBefore,
                    new Page.WaitForConditionOptions().setTimeout(10000));
            LoggerUtil.LOGGER.info("[CONNECT-NODES] ✓ Successfully connected {} -> {} (drag method)", sourceNodeName, targetNodeName);
        } catch (Exception e) {
            page.waitForTimeout(2000);
            if (paths.count() > pathsBefore) {
                LoggerUtil.LOGGER.info("[CONNECT-NODES] ✓ Connection successful after retry {} -> {}", sourceNodeName, targetNodeName);
            } else {
                throw new RuntimeException("Failed to create drag-based connection: " + sourceNodeName + " -> " + targetNodeName, e);
            }
        }
    }

    private static void connectNodesByClick(Page page, Locator source, Locator target, String sourceNodeName, String targetNodeName) {
        try {
            // Click source node to select it
            LoggerUtil.LOGGER.info("[CONNECT-NODES] Clicking source node: {}", sourceNodeName);
            source.click();
            page.waitForTimeout(500);

            // Look for connection port or handle on the source node
            Locator connectionPort = page.locator("svg g.yfiles-port");
            page.waitForTimeout(500);

            // Double-click or long-click source to activate connection mode
            LoggerUtil.LOGGER.info("[CONNECT-NODES] Activating connection from source: {}", sourceNodeName);
            source.dblclick();
            page.waitForTimeout(1000);

            // Click target node to complete connection
            LoggerUtil.LOGGER.info("[CONNECT-NODES] Clicking target node: {}", targetNodeName);
            target.click();
            page.waitForTimeout(2000);

            LoggerUtil.LOGGER.info("[CONNECT-NODES] ✓ Successfully connected {} -> {} (click method)", sourceNodeName, targetNodeName);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[CONNECT-NODES] Click-based connection failed, trying alternative approach", e);
            // Last resort: try using keyboard shortcuts or context menu
            connectNodesByContextMenu(page, source, target, sourceNodeName, targetNodeName);
        }
    }

    private static void connectNodesByContextMenu(Page page, Locator source, Locator target, String sourceNodeName, String targetNodeName) {
        try {
            LoggerUtil.LOGGER.info("[CONNECT-NODES] Attempting context menu connection: {} -> {}", sourceNodeName, targetNodeName);
            
            source.click();
            page.waitForTimeout(300);
            source.click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
            page.waitForTimeout(500);

            // Look for "Connect" option in context menu
            Locator connectOption = page.locator("//div[@role='menuitem' and contains(., 'Connect')]");
            if (connectOption.isVisible()) {
                connectOption.click();
                page.waitForTimeout(500);
                target.click();
                LoggerUtil.LOGGER.info("[CONNECT-NODES] ✓ Successfully connected {} -> {} (context menu method)", sourceNodeName, targetNodeName);
            } else {
                LoggerUtil.LOGGER.warn("[CONNECT-NODES] No context menu 'Connect' option found");
            }
        } catch (Exception e) {
            throw new RuntimeException("All connection methods failed for " + sourceNodeName + " -> " + targetNodeName, e);
        }
    }

    public static List<TableMetadataForCrawlerCatalog> getCrawledTablesMetadata(ViewDataCrawlerPage viewCrawlerPage) {

        Locator tables = viewCrawlerPage.labelTables();

        List<TableMetadataForCrawlerCatalog> result = new ArrayList<>();

        for (int tableCounter = 0; tableCounter < tables.count(); tableCounter++) {

            Locator table = tables.nth(tableCounter);

            String tableName = viewCrawlerPage.labelSchemaTableName().innerText().trim();

            Locator rows = viewCrawlerPage.labelSchemaTableName().locator(viewCrawlerPage.columnRows());

            List<TableColumnForCrawlerCatalog> columns = new ArrayList<>();

            for (int rowCounter = 0; rowCounter < rows.count(); rowCounter++) {

                Locator row = rows.nth(rowCounter);

                String columnName = row.locator(viewCrawlerPage.labelColumnName()).innerText().trim();

                String columnDataType = row.locator(viewCrawlerPage.labelColumnDataType()).innerText().trim();

                columns.add(new TableColumnForCrawlerCatalog(columnName, columnDataType));
            }

            // UI renders columns bottom-to-top.
            // Reverse to match the actual source/schema order.
            Collections.reverse(columns);

            result.add(new TableMetadataForCrawlerCatalog(tableName, columns));
        }

        return result;
    }

    /**
     * Clicks the given button and verifies the click actually took effect by waiting
     * for {@code verifyLocator} to reach {@code expectedState}. Retries a few times
     * with a short pause in between, to handle cases where the button appears
     * enabled slightly before the app's internal state is actually ready to act on it.
     *
     * @param page          page instance
     * @param clickTarget   the element to click
     * @param verifyLocator the element whose state confirms the click succeeded
     * @param expectedState the state verifyLocator should reach after a successful click
     * @param maxAttempts   max number of click attempts
     * @param retryDelayMs  delay between retries in milliseconds
     * @throws RuntimeException if the click did not take effect after maxAttempts
     */
    public static void clickWithRetry(Page page, Locator clickTarget, Locator verifyLocator,
                                WaitForSelectorState expectedState, int maxAttempts, int retryDelayMs) {

        boolean succeeded = false;

        for (int attempt = 0; attempt < maxAttempts && !succeeded; attempt++) {
            clickTarget.click();
            try {
                verifyLocator.waitFor(new Locator.WaitForOptions()
                        .setState(expectedState)
                        .setTimeout(1000));
                succeeded = true;
            } catch (Exception e) {
                page.waitForTimeout(retryDelayMs);
            }
        }

        if (!succeeded) {
            throw new RuntimeException("Click did not take effect after " + maxAttempts + " attempts.");
        }
    }
}