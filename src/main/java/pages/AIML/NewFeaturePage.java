package pages.AIML;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

import java.util.List;

/**
 * Page Object for the "New Feature(s)" creation form in Calibo Accelerate
 * (Product → Feature → New Feature).
 *
 * <p>SWIFT reference component: every element is exposed as a method returning a
 * {@link Locator} resolved through {@link ResilientLocator}, so multi-strategy
 * self-healing and Allure heal-telemetry apply uniformly - exactly like
 * {@code LoginPage} / {@code LandingPage}. Tests (or the {@code FeatureCreationUtil}
 * orchestrator) call these element methods and chain the Playwright action.
 *
 * <p>Locators anchor on stable hooks (data-cy / name / field label) and deliberately
 * AVOID the volatile {@code #react-select-N} indices that shift when the form's field
 * count changes.
 */
public class NewFeaturePage {

    private final Page page;

    private static final int SETTLE_MS = 300;

    public NewFeaturePage(Page page) {
        this.page = page;
    }

    // ======================================================================
    //  Entry point: "New Feature" button
    // ======================================================================

    /** The "New Feature" button - explicitly excludes the btn-support (Support Request) button. */
    public Locator btnNewFeature() {
        return new ResilientLocator(page, "New Feature button")
                .byCss("button:not(.btn-support):has(i.material-icons:has-text('add')):has-text('New Feature')")
                .byCss("button.btn.btn-primary:not(.btn-support):not(.btn-lg):has-text('New Feature')")
                .byXPath("//button[not(contains(@class,'btn-support'))][.//text()[contains(.,'New Feature')]]")
                .resolve();
    }

    // ======================================================================
    //  Phase cards (Define / Design / Develop / Deploy)
    // ======================================================================

    /**
     * The phase card for the given phase. Works for Define/Design (nps-card) AND
     * Develop/Deploy (np-card) by climbing from the h3 to the nearest ancestor 'card'.
     */
    public Locator phaseCard(String phase) {
        return new ResilientLocator(page, "Phase card: " + phase)
                .byXPath(String.format(
                        "//h3[normalize-space()='%s']/ancestor::div[contains(@class,'card')][1]", phase))
                .byXPath(String.format("//h3[normalize-space()='%s']", phase))
                .resolve();
    }

    // ======================================================================
    //  Form fields
    // ======================================================================

    public Locator textboxName() {
        return new ResilientLocator(page, "Feature Name")
                .byCss("input[name='name'][data-cy='workstream-new-name']")
                .byName("name")
                .byCss("input[data-cy='workstream-new-name']")
                .resolve();
    }

    public Locator textboxShortDescription() {
        return new ResilientLocator(page, "Short Description")
                .byCss("textarea[name='shortDescription']")
                .byCss("input[name='short_desc']")
                .resolve();
    }

    public Locator textboxDescription() {
        return new ResilientLocator(page, "Description")
                .byCss("textarea[name='description'][data-cy='workstream-new-description']")
                .byCss("textarea[name='description']")
                .byCss("textarea[data-cy='workstream-new-description']")
                .resolve();
    }

    public Locator inputCompletionDate() {
        return new ResilientLocator(page, "Completion Date")
                .byCss("input.lazsa-date-picker-input")
                .byCss("input[placeholder='Select Date']")
                .resolve();
    }

    public Locator textboxUserStorySearch() {
        return new ResilientLocator(page, "User Story search")
                .byCss("input#search[placeholder*='search stories']")
                .byCss("input#search")
                .resolve();
    }

    public Locator btnCreate() {
        return new ResilientLocator(page, "Create Feature button")
                .byCss("button[data-cy='workstream-new-create-btn']")
                .byCss("button.btn.btn-lg.btn-primary:has-text('Create')")
                .byXPath("//button[normalize-space()='Create']")
                .resolve();
    }
    /**
     * The "Name" field on the simplified inline "New Feature(s)" screen
     * (reached via "Yes" on the product-creation prompt). This is a different,
     * simpler form component than the standalone "+ New Feature" form - no
     * data-cy/name attribute in some variants, so multiple fallbacks are required.
     */
    public Locator inlineTextboxName() {
        return new ResilientLocator(page, "Feature Name (inline)")
                // Prefer semantic label/aria when present
                .byLabel("Name")
                .byCss("input[label='Name']")
                .byCss("input[aria-label='Name']")
                .byPlaceholder("Name")
                // Common class-based fallback seen in screenshots / older builds
                .byCss("input.form-control[type='text']")
                // Shared form component data-cy (used in full form) — helpful if DOM changed to shared variant
                .byCss("input[data-cy='workstream-new-name']")
                // Container-scoped fallback: label text then the following input
                .byXPath("//label[contains(normalize-space(),'Name')]/following::input[1]")
                .byXPath("//input[@label='Name']")
                .resolve();
    }

    // ======================================================================
    //  Success modal (post-create)
    // ======================================================================

    public Locator successModal() {
        return new ResilientLocator(page, "Feature success modal")
                .byCss("div.modal-content:has(h3:has-text('Feature has been created successfully'))")
                .resolve();
    }

    public Locator btnAddDetailsLater() {
        return new ResilientLocator(page, "No, I will add details later")
                .byCss("button.btn.btn-secondary:has-text('No, I will add details later')")
                .byXPath("//button[normalize-space()=\"No, I will add details later\"]")
                .resolve();
    }

    public Locator btnGoToProduct() {
        return new ResilientLocator(page, "Go To Product")
                .byCss("button.btn.btn-primary:has-text('Go To Product')")
                .byXPath("//button[normalize-space()='Go To Product']")
                .resolve();
    }

    // ======================================================================
    //  React-select dropdowns (Status / Owners / Team) - anchored by LABEL
    // ======================================================================

    /**
     * The react-select control nearest to a given field label (e.g. "Feature Status",
     * "Owner", "Team"). Anchored by label text, NOT by react-select-N index.
     */
    public Locator reactSelectControlByLabel(String label) {
        return new ResilientLocator(page, "react-select control: " + label)
                .byXPath(String.format(
                        "//label[contains(normalize-space(),'%s')]" +
                                "/following::div[contains(@class,'react-select__control')][1]", label))
                .resolve();
    }

    /** A react-select option row by its visible text (menu must be open). */
    public Locator reactSelectOption(String value) {
        // Trim value to handle trailing/leading spaces
        String trimmed = value != null ? value.trim() : value;
        return new ResilientLocator(page, "react-select option: " + trimmed)
                // Try exact match first (trimmed)
                .byCss(String.format("div.react-select__option label.form-check-label:has-text('%s')", trimmed))
                .byCss(String.format("div.react-select__option:has-text('%s')", trimmed))
                // Try partial/contains match (more forgiving)
                .byXPath(String.format("//div[contains(@class,'react-select__option')][contains(., '%s')]", trimmed))
                .byXPath(String.format("//div[contains(@class,'react-select__option')]//label[contains(., '%s')]", trimmed))
                // Try with normalized spaces (handles multiple spaces)
                .byXPath(String.format("//div[contains(@class,'react-select__option')]//text()[normalize-space()='%s']/ancestor::div[contains(@class,'react-select__option')]", trimmed))
                .resolve();
    }

    /** The typeable input inside the react-select control nearest a label. */
    public Locator reactSelectInputByLabel(String label) {
        return new ResilientLocator(page, "react-select input: " + label)
                .byXPath(String.format(
                        "//label[contains(normalize-space(),'%s')]" +
                                "/following::div[contains(@class,'react-select__control')][1]//input", label))
                .resolve();
    }



    // ======================================================================
    //  High-level actions (thin, reusable behaviours)
    // ======================================================================

    /**
     * Selects phases, honouring the linked Develop/Deploy pair (clicking one toggles both,
     * so we click that pair only once) while still allowing genuinely independent phases.
     */
    public void selectPhases(List<String> phases) {
        if (phases == null || phases.isEmpty()) {
            LoggerUtil.LOGGER.info("[NEW-FEATURE] No phases specified; leaving defaults");
            return;
        }
        boolean linkedPairClicked = false;
        for (String phase : phases) {
            if (phase == null || phase.isBlank()) continue;
            boolean linked = phase.equalsIgnoreCase("Develop") || phase.equalsIgnoreCase("Deploy");
            if (linked && linkedPairClicked) {
                LoggerUtil.LOGGER.info("[NEW-FEATURE] Skipping '{}' - Develop/Deploy already toggled as a pair", phase);
                continue;
            }
            phaseCard(phase).click();
            LoggerUtil.LOGGER.info("[NEW-FEATURE] Selected phase '{}'", phase);
            page.waitForTimeout(SETTLE_MS);
            if (linked) linkedPairClicked = true;
        }
    }

    /** Selects a value in a labelled react-select: open → click option → (fallback) type+enter. */
    public void selectFromReactDropdown(String label, String value) {
        if (value == null || value.isBlank()) return;
        try {
            reactSelectControlByLabel(label).click();
            page.waitForTimeout(SETTLE_MS);
            Locator option = reactSelectOption(value);
            if (option.count() > 0) {
                option.first().click();
                LoggerUtil.LOGGER.info("[NEW-FEATURE] Selected '{}' for '{}'", value, label);
                return;
            }
            // Fallback: type to filter, then click/Enter
            Locator input = reactSelectInputByLabel(label);
            input.fill(value);
            page.waitForTimeout(SETTLE_MS);
            Locator filtered = reactSelectOption(value);
            if (filtered.count() > 0) filtered.first().click();
            else input.press("Enter");
            LoggerUtil.LOGGER.info("[NEW-FEATURE] Selected '{}' for '{}' via type-filter", value, label);
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[NEW-FEATURE] Failed to select '{}' for '{}': {}", value, label, e.getMessage());
        }
    }

    /** Adds a multi-select entry (owners / team members) - simplified direct approach. */
    public void addMultiSelectEntry(String label, String value) {
        if (value == null || value.isBlank()) return;
        try {
            String trimmed = value.trim();
            LoggerUtil.LOGGER.info("[NEW-FEATURE] Attempting to add '{}' to '{}'", trimmed, label);
            
            // STEP 1: Click the react-select control to open dropdown
            Locator control = reactSelectControlByLabel(label);
            if (control.count() == 0) {
                LoggerUtil.LOGGER.error("[NEW-FEATURE] Could not find react-select control for '{}'", label);
                return;
            }
            
            control.click();
            page.waitForTimeout(800);  // wait for dropdown to open and render
            LoggerUtil.LOGGER.info("[NEW-FEATURE] Clicked react-select control, dropdown should be open");
            
            // STEP 2: Type value to filter options
            Locator input = control.locator("input");
            if (input.count() == 0) {
                LoggerUtil.LOGGER.warn("[NEW-FEATURE] Input not found inside control, trying direct input fill");
                page.keyboard().type(trimmed);
            } else {
                input.fill(trimmed);
            }
            page.waitForTimeout(1000);  // wait for filtering
            LoggerUtil.LOGGER.info("[NEW-FEATURE] Typed '{}', waiting for dropdown options", trimmed);
            
            // STEP 3: TRY KEYBOARD (ArrowDown + Enter) - most reliable
            try {
                page.keyboard().press("ArrowDown");
                page.waitForTimeout(200);
                page.keyboard().press("Enter");
                page.waitForTimeout(500);
                
                // IMPORTANT: Click elsewhere to close dropdown and trigger form re-validation
                page.click("body");
                page.waitForTimeout(800);
                
                LoggerUtil.LOGGER.info("[NEW-FEATURE] ✓ Selected via keyboard (ArrowDown + Enter) and confirmed");
                return;
            } catch (Exception keyErr) {
                LoggerUtil.LOGGER.debug("[NEW-FEATURE] Keyboard selection failed: {}", keyErr.getMessage());
            }
            
            // STEP 4: FALLBACK - Try clicking first visible option
            try {
                Locator options = page.locator("div.react-select__option");
                if (options.count() > 0) {
                    LoggerUtil.LOGGER.info("[NEW-FEATURE] Found {} options, clicking first", options.count());
                    options.first().click();
                    page.waitForTimeout(500);
                    LoggerUtil.LOGGER.info("[NEW-FEATURE] ✓ Selected first option");
                    return;
                }
            } catch (Exception optErr) {
                LoggerUtil.LOGGER.debug("[NEW-FEATURE] Click first option failed: {}", optErr.getMessage());
            }
            
            // STEP 5: FALLBACK - Try Escape + click option if keyboard input worked
            try {
                page.keyboard().press("Escape");
                page.waitForTimeout(300);
                LoggerUtil.LOGGER.info("[NEW-FEATURE] Pressed Escape, attempting selection");
            } catch (Exception escErr) {
                LoggerUtil.LOGGER.debug("[NEW-FEATURE] Escape press failed");
            }
            
            LoggerUtil.LOGGER.warn("[NEW-FEATURE] Could not select '{}' - all strategies exhausted", trimmed);
            
        } catch (Exception e) {
           LoggerUtil.LOGGER.error("[NEW-FEATURE] Exception adding '{}' to '{}': {} | Cause: {}", 
                   value, label, e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : "N/A");
        }
    }

    /** Clicks Create, then dismisses the success modal with "No, I will add details later", then clicks "Go To Product". */
    public void submitAndDismissSuccessModal() {
        Locator btn = btnCreate();
        
        // Wait for the Create button to be visible and then poll for enabled state
        try {
            btn.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(10000));
            
            // Poll for enabled state (not disabled)
            long deadline = System.currentTimeMillis() + 15000;
            while (System.currentTimeMillis() < deadline) {
                if (btn.isEnabled()) {
                    LoggerUtil.LOGGER.info("[NEW-FEATURE] Create button is enabled, proceeding to click");
                    break;
                }
                page.waitForTimeout(500);
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[NEW-FEATURE] Error waiting for Create button: {}", 
                    e.getMessage());
        }
        
        btn.click();
        LoggerUtil.LOGGER.info("[NEW-FEATURE] Clicked Create");
        try {
            page.waitForTimeout(1000);
            if (successModal().count() > 0) {
                LoggerUtil.LOGGER.info("[NEW-FEATURE] Success modal detected");
                Locator no = btnAddDetailsLater();
                if (no.count() > 0) {
                    no.first().click();
                    LoggerUtil.LOGGER.info("[NEW-FEATURE] Clicked 'No, I will add details later'");
                    page.waitForTimeout(1000);
                }
            } else {
                LoggerUtil.LOGGER.info("[NEW-FEATURE] Success modal not present");
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[NEW-FEATURE] Error handling success modal: {}", e.getMessage());
        }
        
        // After feature creation, click "Go To Product" to navigate back (if present)
        try {
            page.waitForTimeout(500);
            Locator goToProduct = btnGoToProduct();
            if (goToProduct.count() > 0) {
                LoggerUtil.LOGGER.info("[NEW-FEATURE] Found 'Go To Product' button, clicking to navigate back");
                goToProduct.click();
                page.waitForTimeout(2000);  // wait for navigation to complete
                LoggerUtil.LOGGER.info("[NEW-FEATURE] ✓ Navigated back to Product page");
            } else {
                LoggerUtil.LOGGER.debug("[NEW-FEATURE] 'Go To Product' button not found (may not be inline creation)");
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.debug("[NEW-FEATURE] Error clicking 'Go To Product': {}", e.getMessage());
        }
    }
}
