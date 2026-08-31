package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.Arrays;
import java.util.List;

/**
 * Utility for interacting with feature stages (Develop, Deploy, Define, Design).
 * Provides resilient click helpers and stage navigation with multiple fallback strategies.
 */
public final class FeatureStageUtil {

    private FeatureStageUtil() {}

    /**
     * Click on a stage by name using resilient selectors.
     * Works for stages: Develop, Deploy, Define, Design
     * 
     * Stage button structure:
     * <div class="d-flex align-items-center col-3 cursor-pointer">
     *   <img class="feature-phase-logo" src="...">
     *   <h4 class="sc-hKwCoD fgBDdK">Develop</h4>
     * </div>
     *
     * @param page Playwright Page object
     * @param stageName Stage name (e.g., "Develop", "Deploy")
     */
    public static void clickStage(Page page, String stageName) {
        if (stageName == null || stageName.isBlank()) {
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] Stage name is empty");
            return;
        }

        LoggerUtil.LOGGER.info("[STAGE-UTIL] Attempting to click stage: {}", stageName);

        // Resilient selector strategy with multiple fallbacks
        List<String> selectors = Arrays.asList(
                // Strategy 1: Parent div with col-3 and h4 inside (most specific for current structure)
                String.format("div.col-3.cursor-pointer:has(h4:has-text('%s'))", stageName),
                // Strategy 2: Parent div with d-flex, align-items-center, cursor-pointer
                String.format("div.d-flex.align-items-center.cursor-pointer:has(h4:has-text('%s'))", stageName),
                // Strategy 3: h4 with exact text and specific class
                String.format("h4.sc-hKwCoD:has-text('%s')", stageName),
                // Strategy 4: Generic h4 with text
                String.format("h4:has-text('%s')", stageName),
                // Strategy 5: Parent div with img + h4 pattern
                String.format("div.d-flex.align-items-center:has(img.feature-phase-logo):has(h4:has-text('%s'))", stageName),
                // Strategy 6: Div with cursor-pointer and h4
                String.format("div.cursor-pointer:has(h4:has-text('%s'))", stageName),
                // Strategy 7: Text-based locator
                String.format("text=%s", stageName)
        );

        Locator stageElement = null;
        for (String selector : selectors) {
            try {
                Locator l = page.locator(selector);
                if (l.count() > 0 && l.first().isVisible()) {
                    stageElement = l.first();
                    LoggerUtil.LOGGER.info("[STAGE-UTIL] Located stage '{}' via selector: {}", stageName, selector);
                    break;
                }
            } catch (Exception ignored) {}
        }

        // Fallback: Manual iteration through divs with cursor-pointer class
        if (stageElement == null) {
            try {
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Fallback: manual iteration through cursor-pointer divs");
                Locator cursorDivs = page.locator("div.cursor-pointer");
                for (int i = 0; i < cursorDivs.count(); i++) {
                    Locator div = cursorDivs.nth(i);
                    String text = div.textContent();
                    if (text != null && text.contains(stageName) && div.isVisible()) {
                        stageElement = div;
                        LoggerUtil.LOGGER.info("[STAGE-UTIL] Located stage '{}' via manual iteration at index {}", stageName, i);
                        break;
                    }
                }
            } catch (Exception ignored) {}
        }

        // Fallback 2: Search by col-3 divs
        if (stageElement == null) {
            try {
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Fallback 2: manual iteration through col-3 divs");
                Locator colDivs = page.locator("div.col-3.cursor-pointer");
                for (int i = 0; i < colDivs.count(); i++) {
                    Locator div = colDivs.nth(i);
                    String text = div.textContent();
                    if (text != null && text.contains(stageName) && div.isVisible()) {
                        stageElement = div;
                        LoggerUtil.LOGGER.info("[STAGE-UTIL] Located stage '{}' via col-3 iteration at index {}", stageName, i);
                        break;
                    }
                }
            } catch (Exception ignored) {}
        }

        // Execute click
        if (stageElement != null) {
            try {
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Scrolling stage element into view...");
                stageElement.scrollIntoViewIfNeeded();
                page.waitForTimeout(500); // Brief pause after scroll
                
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Clicking stage element...");
                stageElement.click();
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Successfully clicked stage: {}", stageName);
                
                // Wait for stage details to load
                waitForStageDetailsLoad(page);
            } catch (Exception e) {
                LoggerUtil.LOGGER.error("[STAGE-UTIL] Failed to click stage '{}': {}", stageName, e.getMessage());
                throw new RuntimeException("Failed to click stage: " + stageName, e);
            }
        } else {
            LoggerUtil.LOGGER.error("[STAGE-UTIL] Could not locate stage: {}", stageName);
            throw new RuntimeException("Failed to locate stage: " + stageName);
        }
    }

    /**
     * Wait for stage details panel/modal to appear and load
     *
     * @param page Playwright Page object
     */
    private static void waitForStageDetailsLoad(Page page) {
        try {
            // Heuristics to detect when stage details have loaded
            String[] heuristics = new String[]{
                    "[role='dialog']",           // Modal dialog
                    "[aria-modal='true']",       // Modal
                    ".modal, .dialog",           // Modal/dialog classes
                    ".stage-details, .stage-panel",  // Stage-specific panels
                    ".sheet-body",               // Side sheet body
                    "h3, h2, h4",                // Heading indicating new content
                    "button:has-text('Add')",    // Add buttons in stage details
                    "button:has-text('Create')"  // Create workstream button
            };

            for (String heuristic : heuristics) {
                try {
                    page.locator(heuristic).waitFor(new Locator.WaitForOptions().setTimeout(3000));
                    LoggerUtil.LOGGER.info("[STAGE-UTIL] Stage details detected via: {}", heuristic);
                    page.waitForTimeout(500); // Brief pause for UI to settle
                    return;
                } catch (Exception ignored) {}
            }
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] No stage details heuristics matched; continuing without explicit wait");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] Error waiting for stage details: {}", e.getMessage());
        }
    }

    /**
     * Get all available stages on the current feature page
     *
     * @param page Playwright Page object
     * @return List of stage names
     */
    public static List<String> getAvailableStages(Page page) {
        try {
            Locator stages = page.locator("div.d-flex.align-items-center h4");
            java.util.List<String> stageNames = new java.util.ArrayList<>();
            for (int i = 0; i < stages.count(); i++) {
                String text = stages.nth(i).textContent();
                if (text != null && !text.isBlank()) {
                    stageNames.add(text.trim());
                }
            }
            LoggerUtil.LOGGER.info("[STAGE-UTIL] Available stages: {}", stageNames);
            return stageNames;
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] Error fetching available stages: {}", e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Click the "+ Add Stage" button in Data Pipeline Studio (Develop stage).
     * Uses resilient locator strategies to handle various button rendering patterns.
     *
     * @param page Playwright Page object
     */
    public static void clickAddStageButton(Page page) {
        LoggerUtil.LOGGER.info("[STAGE-UTIL] Attempting to click '+ Add Stage' button");

        // Resilient selector strategy with multiple fallbacks
        List<String> selectors = Arrays.asList(
                // Strategy 1: Button with aria-label
                "button[aria-label*='Add']:has-text('+')",
                // Strategy 2: Button containing + symbol and Add text
                "button:has-text('+'):has-text('Add')",
                // Strategy 3: Icon button with + symbol near pipeline stages
                "button:has-text('+')",
                // Strategy 4: SVG plus icon in button
                "button:has(svg[data-icon='plus'])",
                // Strategy 5: Left sidebar button for adding stages
                "div.sidebar button:has-text('+')",
                // Strategy 6: Text-based locator for plus
                "text=+",
                // Strategy 7: Button with class patterns for add action
                "button.btn-outline, button.btn-add, button[class*='add']",
                // Strategy 8: Icon button pattern (small clickable element)
                "button[class*='icon']:has-text('+')"
        );

        Locator addStageButton = null;
        for (String selector : selectors) {
            try {
                Locator l = page.locator(selector);
                if (l.count() > 0) {
                    // Filter for visible buttons
                    for (int i = 0; i < l.count(); i++) {
                        Locator btn = l.nth(i);
                        if (btn.isVisible()) {
                            addStageButton = btn;
                            LoggerUtil.LOGGER.info("[STAGE-UTIL] Located '+ Add Stage' button via selector: {}", selector);
                            break;
                        }
                    }
                    if (addStageButton != null) break;
                }
            } catch (Exception ignored) {}
        }

        // Fallback: Manual iteration through all buttons looking for + symbol
        if (addStageButton == null) {
            try {
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Fallback: manual iteration through visible buttons");
                Locator allButtons = page.locator("button");
                for (int i = 0; i < allButtons.count(); i++) {
                    Locator btn = allButtons.nth(i);
                    String text = btn.textContent();
                    if (btn.isVisible() && text != null && text.contains("+")) {
                        addStageButton = btn;
                        LoggerUtil.LOGGER.info("[STAGE-UTIL] Located '+ Add Stage' button via manual iteration at index {}", i);
                        break;
                    }
                }
            } catch (Exception ignored) {}
        }

        // Execute click
        if (addStageButton != null) {
            try {
                addStageButton.scrollIntoViewIfNeeded();
                addStageButton.click();
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Successfully clicked '+ Add Stage' button");
                
                // Wait for stage creation modal/form to appear
                waitForAddStageModalLoad(page);
            } catch (Exception e) {
                LoggerUtil.LOGGER.error("[STAGE-UTIL] Failed to click '+ Add Stage' button: {}", e.getMessage());
                throw new RuntimeException("Failed to click '+ Add Stage' button", e);
            }
        } else {
            LoggerUtil.LOGGER.error("[STAGE-UTIL] Could not locate '+ Add Stage' button");
            throw new RuntimeException("Failed to locate '+ Add Stage' button");
        }
    }

    /**
     * Wait for stage creation modal/form to appear after clicking + Add Stage
     *
     * @param page Playwright Page object
     */
    private static void waitForAddStageModalLoad(Page page) {
        try {
            // Heuristics to detect when add stage modal/form has loaded
            String[] heuristics = new String[]{
                    "[role='dialog']",                          // Modal dialog
                    "[aria-modal='true']",                      // Modal
                    ".modal, .dialog",                          // Modal/dialog classes
                    "input[placeholder*='Stage'], input[placeholder*='stage']",  // Stage name input
                    "button:has-text('Create')",                // Create button
                    "button:has-text('Save')",                  // Save button
                    "h2, h3, h4",                               // Heading in modal
                    ".modal-content, .sheet-content"            // Modal content containers
            };

            for (String heuristic : heuristics) {
                try {
                    page.locator(heuristic).waitFor(new Locator.WaitForOptions().setTimeout(3000));
                    LoggerUtil.LOGGER.info("[STAGE-UTIL] Add stage modal detected via: {}", heuristic);
                    page.waitForTimeout(500); // Brief pause for UI to settle
                    return;
                } catch (Exception ignored) {}
            }
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] No add stage modal heuristics matched; continuing without explicit wait");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] Error waiting for add stage modal: {}", e.getMessage());
        }
    }

    /**
     * Select a stage type from the "Stage Type" dropdown in the Add Stage modal.
     * Uses react-select dropdown interaction pattern.
     * 
     * Available options: Analytical Data Store, Data Analytics, Data Integration, Data Lake,
     * Data Management, Data Quality, Data Sources, Data Transformation, Data Visualization,
     * RAG Builder, Workflow
     *
     * @param page Playwright Page object
     * @param stageType Stage type to select (e.g., "Data Sources", "Data Transformation")
     */
    public static void selectStageType(Page page, String stageType) {
        if (stageType == null || stageType.isBlank()) {
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] Stage type is empty");
            return;
        }

        LoggerUtil.LOGGER.info("[STAGE-UTIL] Attempting to select stage type: {}", stageType);

        try {
            // Step 1: Click dropdown indicator to open the menu
            Locator dropdownIndicator = page.locator(".react-select__dropdown-indicator").first();
            if (dropdownIndicator.isVisible()) {
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Clicking dropdown indicator to open menu");
                dropdownIndicator.click();
                page.waitForTimeout(800); // Wait for dropdown menu to appear
            } else {
                // Fallback: Click on the input field
                Locator input = page.locator(".react-select__input input").first();
                if (input.isVisible()) {
                    LoggerUtil.LOGGER.info("[STAGE-UTIL] Clicking input field to open dropdown");
                    input.click();
                    page.waitForTimeout(800);
                }
            }

            // Step 2: Wait for dropdown menu to be visible
            page.locator(".react-select__menu").waitFor(new Locator.WaitForOptions().setTimeout(3000));
            LoggerUtil.LOGGER.info("[STAGE-UTIL] Dropdown menu appeared");

            // Step 3: Find and click the option
            // Strategy 1: Search by label text within form-check-label
            Locator option = page.locator(String.format(".react-select__option .form-check-label:has-text('%s')", stageType));
            
            // Strategy 2: Fallback to option with has-text directly
            if (option.count() == 0) {
                option = page.locator(String.format(".react-select__option:has-text('%s')", stageType));
            }

            // Strategy 3: Fallback to generic text search within menu
            if (option.count() == 0) {
                option = page.locator(String.format(".react-select__menu-list :text('%s')", stageType));
            }

            // Strategy 4: Last resort - text locator
            if (option.count() == 0) {
                option = page.locator(String.format("text=%s", stageType));
            }

            if (option.count() > 0) {
                Locator clickTarget = option.first();
                // If we matched a label, click its parent option div
                if (option.nth(0).evaluate("el => el.className").toString().contains("form-check-label")) {
                    clickTarget = option.first().locator("xpath=ancestor::.react-select__option").first();
                }
                
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Found stage type option: {}", stageType);
                clickTarget.scrollIntoViewIfNeeded();
                clickTarget.click();
                page.waitForTimeout(800); // Wait for selection to register and menu to close
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Successfully selected stage type: {}", stageType);
            } else {
                LoggerUtil.LOGGER.error("[STAGE-UTIL] Could not locate stage type option: {}", stageType);
                throw new RuntimeException("Failed to locate stage type option: " + stageType);
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[STAGE-UTIL] Failed to select stage type '{}': {}", stageType, e.getMessage());
            throw new RuntimeException("Failed to select stage type: " + stageType, e);
        }
    }

    /**
     * Fill in the "Stage Name" input field in the Add Stage modal.
     *
     * @param page Playwright Page object
     * @param stageName Name to enter for the stage
     */
    public static void fillStageName(Page page, String stageName) {
        if (stageName == null || stageName.isBlank()) {
            LoggerUtil.LOGGER.warn("[STAGE-UTIL] Stage name is empty");
            return;
        }

        LoggerUtil.LOGGER.info("[STAGE-UTIL] Attempting to fill stage name: {}", stageName);

        try {
            // Resilient selectors for the stage name input
            List<String> selectors = Arrays.asList(
                    // Strategy 1: Input with placeholder
                    "input[placeholder='Stage Name']",
                    // Strategy 2: Input with label
                    ".form-group input[type='']",
                    // Strategy 3: Input with form-control class
                    "input.form-control",
                    // Strategy 4: Input within form-group
                    ".form-group input",
                    // Strategy 5: Generic text input near label
                    "label:has-text('Stage Name') ~ input"
            );

            Locator stageNameInput = null;
            for (String selector : selectors) {
                try {
                    Locator l = page.locator(selector);
                    if (l.count() > 0 && l.first().isVisible()) {
                        stageNameInput = l.first();
                        LoggerUtil.LOGGER.info("[STAGE-UTIL] Located stage name input via selector: {}", selector);
                        break;
                    }
                } catch (Exception ignored) {}
            }

            if (stageNameInput != null) {
                stageNameInput.scrollIntoViewIfNeeded();
                stageNameInput.clear();
                stageNameInput.type(stageName);
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Successfully filled stage name: {}", stageName);
            } else {
                LoggerUtil.LOGGER.error("[STAGE-UTIL] Could not locate stage name input field");
                throw new RuntimeException("Failed to locate stage name input field");
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[STAGE-UTIL] Failed to fill stage name '{}': {}", stageName, e.getMessage());
            throw new RuntimeException("Failed to fill stage name: " + stageName, e);
        }
    }

    /**
     * Click the "Add" button to finalize stage creation.
     * Handles the Add Stage modal button structure.
     * Button structure:
     * <button text="Add" type="button" class="sc-bBHwJV hDsuPB btn-lg btn btn-primary">Add</button>
     *
     * @param page Playwright Page object
     */
    public static void clickCreateStageButton(Page page) {
        LoggerUtil.LOGGER.info("[STAGE-UTIL] Attempting to click Add button");

        try {
            // Resilient selectors for add/create/save button
            List<String> selectors = Arrays.asList(
                    // Strategy 1: Primary button with Add text
                    "button.btn-primary:has-text('Add')",
                    // Strategy 2: Button with Add text (highest priority)
                    "button:has-text('Add'):not(:has-text('Cancel'))",
                    // Strategy 3: Button with text 'Add' (generic)
                    "button:has-text('Add')",
                    // Strategy 4: Primary button with Create text
                    "button.btn-primary:has-text('Create')",
                    // Strategy 5: Button with Create text
                    "button:has-text('Create')",
                    // Strategy 6: Button with Save text
                    "button.btn-primary:has-text('Save')",
                    // Strategy 7: Button with Save text
                    "button:has-text('Save')",
                    // Strategy 8: Submit button
                    "button[type='submit']",
                    // Strategy 9: Primary button type
                    "button.btn-primary"
            );

            Locator addButton = null;
            for (String selector : selectors) {
                try {
                    Locator l = page.locator(selector);
                    if (l.count() > 0 && l.first().isVisible()) {
                        addButton = l.first();
                        LoggerUtil.LOGGER.info("[STAGE-UTIL] Located Add button via selector: {}", selector);
                        break;
                    }
                } catch (Exception ignored) {}
            }

            if (addButton != null) {
                addButton.scrollIntoViewIfNeeded();
                addButton.click();
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Successfully clicked Add button");
                
                // Wait for stage to be created and modal to close
                page.waitForTimeout(2000);
            } else {
                LoggerUtil.LOGGER.error("[STAGE-UTIL] Could not locate Add button");
                throw new RuntimeException("Failed to locate Add button");
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[STAGE-UTIL] Failed to click Add button: {}", e.getMessage());
            throw new RuntimeException("Failed to click Add button", e);
        }
    }

    /**
     * Complete the entire Add Stage workflow: select type, fill name, and create.
     * Convenience method for end-to-end stage creation.
     *
     * @param page Playwright Page object
     * @param stageType Stage type to select
     * @param stageName Stage name to enter
     */
    public static void completeAddStageWorkflow(Page page, String stageType, String stageName) {
        LoggerUtil.LOGGER.info("[STAGE-UTIL] Starting Add Stage workflow: type={}, name={}", stageType, stageName);
        
        try {
            selectStageType(page, stageType);
            fillStageName(page, stageName);
            clickCreateStageButton(page);
            
            LoggerUtil.LOGGER.info("[STAGE-UTIL] Add Stage workflow completed successfully");
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[STAGE-UTIL] Add Stage workflow failed: {}", e.getMessage());
            throw new RuntimeException("Add Stage workflow failed", e);
        }
    }

    /**
     * Click the "Add Node" button to add a technology/node to the stage.
     * Button structure:
     * <button class="btn btn-link btn-sm p-0" data-tour="reactour__add-tech-stack-to-stage">
     *   <i class="material-icons">add_circle_outline</i>
     * </button>
     *
     * @param page Playwright Page object
     */
    public static void clickAddNodeButton(Page page) {
        LoggerUtil.LOGGER.info("[STAGE-UTIL] Attempting to click Add Node button");

        try {
            // Resilient selectors for add node button
            List<String> selectors = Arrays.asList(
                    // Strategy 1: By data-tour attribute (most specific)
                    "button[data-tour='reactour__add-tech-stack-to-stage']",
                    // Strategy 2: By icon name
                    "button:has(.material-icons:has-text('add_circle_outline'))",
                    // Strategy 3: By class pattern with icon
                    "button.btn-link:has(.material-icons)",
                    // Strategy 4: By btn-sm and btn-link classes
                    "button.btn.btn-link.btn-sm",
                    // Strategy 5: Button with add icon
                    "button:has(i.material-icons:has-text('add'))",
                    // Strategy 6: Any button with add_circle icon
                    "button:has(.material-icons:contains('add_circle'))"
            );

            Locator addNodeButton = null;
            for (String selector : selectors) {
                try {
                    Locator l = page.locator(selector);
                    if (l.count() > 0 && l.first().isVisible()) {
                        addNodeButton = l.first();
                        LoggerUtil.LOGGER.info("[STAGE-UTIL] Located Add Node button via selector: {}", selector);
                        break;
                    }
                } catch (Exception ignored) {}
            }

            if (addNodeButton != null) {
                addNodeButton.scrollIntoViewIfNeeded();
                addNodeButton.click();
                LoggerUtil.LOGGER.info("[STAGE-UTIL] Successfully clicked Add Node button");
                page.waitForTimeout(1000); // Wait for UI response
            } else {
                LoggerUtil.LOGGER.error("[STAGE-UTIL] Could not locate Add Node button");
                throw new RuntimeException("Failed to locate Add Node button");
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[STAGE-UTIL] Failed to click Add Node button: {}", e.getMessage());
            throw new RuntimeException("Failed to click Add Node button", e);
        }
    }
}
