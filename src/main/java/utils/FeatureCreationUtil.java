package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Feature creation utilities for Calibo Accelerate pages.
 * Provides resilient click helper and a createFeature API accepting FeatureData.
 */
public final class FeatureCreationUtil {

    private FeatureCreationUtil() {}

    public static class FeatureData {
        public List<String> phases = new ArrayList<>();
        public String name;
        public String shortDescription;
        public String description;
        public String completionDate;
        public String status = "In QA";
        public List<String> owners = new ArrayList<>();
        public List<String> teamMembers = new ArrayList<>();
        public List<String> userStories = new ArrayList<>();
    }

    // Preserve previous API
    public static void clickNewFeature(Page page) { clickNewFeature(page, null); }

    public static void clickNewFeature(Page page, String waitForSelector) {
        LoggerUtil.LOGGER.info("[FEATURE-UTIL] Trying to locate 'New Feature' control");

        List<String> selectors = Arrays.asList(
                // Combined selectors from both helpers for maximum resilience
                "button:not(.btn-support):has(i.material-icons:has-text('add')):has-text('New Feature')",
                "button.btn.btn-primary:not(.btn-support):not(.btn-lg):has-text('New Feature')",
                "button:has-text('New Feature'):not(.btn-support')",
                // Calibo specific fallbacks
                "button.btn.btn-primary:has-text('New Feature')",
                "[data-testid='new-feature'], [data-test='add-feature'], button[aria-label='New Feature']",
                "button.sc-bBHwJV.hDsuPB.btn.btn-primary",
                "button.add-new-feature, a.add-new-feature, .add-feature-btn",
                // ultimate fallback to text scanning handled below
                "button:has-text('New Feature')"
        );

        Locator control = null;
        for (String s : selectors) {
            try {
                Locator l = page.locator(s);
                if (l.count() > 0) { 
                    control = l.first(); 
                    LoggerUtil.LOGGER.info("[FEATURE-UTIL] Located New Feature via: {}", s); 
                    break; 
                }
            } catch (Exception ignored) {}
        }

        if (control == null) {
            LoggerUtil.LOGGER.warn("[FEATURE-UTIL] No selector matched; attempting getByText fallback");
            try {
                Locator allButtons = page.locator("button");
                for (int i = 0; i < allButtons.count(); i++) {
                    Locator btn = allButtons.nth(i);
                    String text = btn.textContent();
                    String classes = btn.getAttribute("class");
                    if (text != null && text.contains("New Feature") && (classes == null || !classes.contains("btn-support"))) {
                        control = btn;
                        LoggerUtil.LOGGER.info("[FEATURE-UTIL] Located New Feature via manual button iteration");
                        break;
                    }
                }
            } catch (Exception ignored) {}
        }

        if (control == null) {
            LoggerUtil.LOGGER.error("[FEATURE-UTIL] Could not locate New Feature button at all");
            throw new RuntimeException("Failed to locate New Feature button");
        }

        try {
            control.scrollIntoViewIfNeeded();
            control.click();
            LoggerUtil.LOGGER.info("[FEATURE-UTIL] Clicked 'New Feature' control successfully");
            waitForPostClick(page, waitForSelector);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[FEATURE-UTIL] Failed to click 'New Feature' control", e);
            throw new RuntimeException("Failed to click New Feature control", e);
        }
    }

    private static void waitForPostClick(Page page, String waitForSelector) {
        if (waitForSelector != null && !waitForSelector.isBlank()) {
            try { page.locator(waitForSelector).waitFor(new Locator.WaitForOptions().setTimeout(10_000));
                LoggerUtil.LOGGER.info("[FEATURE-UTIL] Detected post-click selector: {}", waitForSelector);
            } catch (Exception e) {
                LoggerUtil.LOGGER.warn("[FEATURE-UTIL] Post-click selector did not appear: {}", waitForSelector);
            }
            return;
        }

        String[] heuristics = new String[]{
                "[role='dialog']",
                "[aria-modal='true']",
                ".modal, .dialog, [data-testid='modal'], [data-testid='new-feature-modal']",
                "form#newFeatureForm, form[action*='feature'], #new-feature-form, form[data-testid='new-feature-form']",
                "button:has-text('Create'), button:has-text('Save'), button:has-text('Add Feature'), button:has-text('Create Feature')",
                "input[name='title'], input[placeholder*='title'], textarea[name='description']"
        };

        for (String s : heuristics) {
            try { page.locator(s).waitFor(new Locator.WaitForOptions().setTimeout(3000)); LoggerUtil.LOGGER.info("[FEATURE-UTIL] Detected post-click heuristic selector: {}", s); return; } catch (Exception ignored) {}
        }
        LoggerUtil.LOGGER.warn("[FEATURE-UTIL] No heuristic post-click selector detected. Consider providing explicit waitForSelector.");
    }

    // ---------------- high-level createFeature API ----------------
    public static String createFeature(Page page, FeatureData data) {
        if (data == null) data = new FeatureData();

        clickNewFeature(page);

        if (data.phases != null && !data.phases.isEmpty()) {
            // Note: Develop & Deploy are linked - clicking one selects both, clicking again deselects both
            // So if user wants both, we only click once on the first one
            for (String p : data.phases) {
                selectPhase(page, p);
                page.waitForTimeout(300); // Small delay between clicks
                break; // Only click the first phase - it will toggle the pair
            }
        } else {
            LoggerUtil.LOGGER.info("[FEATURE-UTIL] No phases specified; leaving defaults (if any)");
        }

        String nameToUse = data.name != null && !data.name.isBlank() ? data.name : generateRandomName();
        fillInputWithFallbacks(page, Arrays.asList(
                "input[name='name'][data-cy='workstream-new-name']",
                "input[name='name']",
                "input[data-cy='workstream-new-name']"
        ), nameToUse);

        if (data.shortDescription != null && !data.shortDescription.isBlank()) {
            fillInputWithFallbacks(page, Arrays.asList(
                    "textarea[name='shortDescription']",
                    "input[name='short_desc']"
            ), data.shortDescription);
        }

        if (data.description != null && !data.description.isBlank()) {
            fillInputWithFallbacks(page, Arrays.asList(
                    "textarea[name='description'][data-cy='workstream-new-description']",
                    "textarea[name='description']",
                    "textarea[data-cy='workstream-new-description']"
            ), data.description);
        }

        if (data.completionDate != null && !data.completionDate.isBlank()) {
            fillInputWithFallbacks(page, Arrays.asList(
                    "input.lazsa-date-picker-input",
                    "input[placeholder='Select Date']"
            ), data.completionDate);
        }

        if (data.status != null && !data.status.isBlank()) {
            trySelectDropdownByLabel(page, Arrays.asList(
                    "#react-select-4-input",
                    "input#react-select-4-input"
            ), data.status);
        }

        if (data.owners != null && !data.owners.isEmpty()) {
            for (String o : data.owners) typeAndConfirm(page, Arrays.asList(
                    "#react-select-5-input",
                    "input#react-select-5-input"
            ), o);
        }
        if (data.teamMembers != null && !data.teamMembers.isEmpty()) {
            for (String t : data.teamMembers) typeAndConfirm(page, Arrays.asList(
                    "#react-select-6-input",
                    "input#react-select-6-input"
            ), t);
        }

        if (data.userStories != null && !data.userStories.isEmpty()) {
            String joined = data.userStories.stream().collect(Collectors.joining("\n- ", "- ", ""));
            fillInputWithFallbacks(page, Arrays.asList(
                    "input#search[placeholder*='search stories']",
                    "input#search"
            ), joined);
        }

        List<String> createButtons = Arrays.asList(
                "button[data-cy='workstream-new-create-btn']",
                "button.btn.btn-lg.btn-primary:has-text('Create')",
                "button:has-text('Create')",
                // additional fallbacks from CaliboFeatureCreationUtil
                "button:has-text('Create Feature')",
                "button:has-text('Save')",
                "button:has-text('Add Feature')",
                "button.btn.btn-primary:has-text('Create')",
                "[data-testid='create-feature-button']"
        );
        boolean clicked = false;
        for (String b : createButtons) {
            try {
                Locator btn = page.locator(b);
                if (btn.count() > 0) { btn.first().click(); LoggerUtil.LOGGER.info("[FEATURE-UTIL] Clicked create button: {}", b); clicked = true; break; }
            } catch (Exception ignored) {}
        }
        if (!clicked) LoggerUtil.LOGGER.warn("[FEATURE-UTIL] Create button not found with fallbacks");
        
        // Wait for success modal and click "No, I will add details later" by default
        handleFeatureCreationSuccessModal(page);
        
        // Return the name used so callers can act on the newly-created feature
        return nameToUse;
    }

    private static void handleFeatureCreationSuccessModal(Page page) {
        try {
            page.waitForTimeout(1000); // Wait for modal to appear
            
            // Look for the success modal
            Locator modal = page.locator("div.modal-content:has(h3:has-text('Feature has been created successfully'))");
            if (modal.count() > 0) {
                LoggerUtil.LOGGER.info("[FEATURE-UTIL] Success modal detected");
                
                // Click "No, I will add details later" button by default
                Locator noButton = page.locator("button.btn.btn-secondary:has-text('No, I will add details later')");
                if (noButton.count() > 0) {
                    noButton.first().click();
                    LoggerUtil.LOGGER.info("[FEATURE-UTIL] Clicked 'No, I will add details later'");
                    page.waitForTimeout(1000); // Wait for modal to close
                    return;
                }
            }
            LoggerUtil.LOGGER.info("[FEATURE-UTIL] Success modal not found or 'No' button not present");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[FEATURE-UTIL] Error handling success modal: {}", e.getMessage());
        }
    }

    // Tuned selectors based on actual HTML structure
    private static void selectPhase(Page page, String phase) {
        if (phase == null || phase.isBlank()) return;
        try {
            // Find h3 with exact text (works for Define/Design with nps-card AND Develop/Deploy with np-card)
            Locator h3 = page.locator(String.format("h3:has-text('%s')", phase));
            if (h3.count() > 0) {
                // Navigate up to nearest ancestor div with class containing 'card' (handles both nps-card and np-card)
                Locator card = h3.locator("xpath=ancestor::div[contains(@class, 'card')][1]");
                if (card.count() > 0) {
                    card.first().click();
                    LoggerUtil.LOGGER.info("[FEATURE-UTIL] Selected phase '{}' by clicking card", phase);
                    return;
                }
                // Fallback: click the h3 directly
                h3.first().click();
                LoggerUtil.LOGGER.info("[FEATURE-UTIL] Selected phase '{}' by clicking h3 directly", phase);
                return;
            }
            LoggerUtil.LOGGER.warn("[FEATURE-UTIL] Phase '{}' h3 element not found", phase);
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[FEATURE-UTIL] Failed to select phase '{}': {}", phase, e.getMessage());
        }
    }

    private static void fillInputWithFallbacks(Page page, List<String> selectors, String value) {
        for (String s : selectors) {
            try {
                Locator l = page.locator(s);
                if (l.count() > 0) { 
                    l.first().fill(value);
                    LoggerUtil.LOGGER.info("[FEATURE-UTIL] Filled '{}' into {}", value, s);
                    return;
                }
            } catch (Exception ignored) {}
        }
        LoggerUtil.LOGGER.warn("[FEATURE-UTIL] No input found for selectors {} to fill value'{}'", selectors, value);
    }

    private static void trySelectDropdownByLabel(Page page, List<String> selectors, String label) {
        for (String s : selectors) {
            try {
                Locator input = page.locator(s);
                if (input.count() > 0) {
                    Locator inputElem = input.first();

                    // If this is a native select element, prefer selectOption
                    try {
                        String tag = inputElem.evaluate("e => e.tagName").toString();
                        if ("SELECT".equalsIgnoreCase(tag)) {
                            inputElem.selectOption(new SelectOption().setLabel(label));
                            LoggerUtil.LOGGER.info("[FEATURE-UTIL] Selected '{}' using selectOption on {}", label, s);
                            return;
                        }
                    } catch (Exception ignored) {}

                    // For react-select: click the control to open dropdown, then find and click the option
                    Locator control = inputElem.locator("xpath=ancestor::div[contains(@class, 'react-select__control')][1]");
                    if (control.count() > 0) { control.first().click(); } else { inputElem.click(); }
                    page.waitForTimeout(300);

                    // Look for the dropdown option by text
                    Locator option = page.locator(String.format("div.react-select__option:has-text('%s')", label));
                    if (option.count() > 0) { option.first().click(); LoggerUtil.LOGGER.info("[FEATURE-UTIL] Selected '{}' from dropdown {}", label, s); return; }

                    // Fallback: type the label to filter and press Enter
                    try { inputElem.fill(""); inputElem.type(label); inputElem.press("Enter"); } catch (Exception ignored) {}
                    page.waitForTimeout(300);

                    option = page.locator(String.format("div.react-select__option:has-text('%s')", label));
                    if (option.count() > 0) { option.first().click(); LoggerUtil.LOGGER.info("[FEATURE-UTIL] Selected '{}' from dropdown via type {}", label, s); return; }
                }
            } catch (Exception e) {
                LoggerUtil.LOGGER.debug("[FEATURE-UTIL] trySelectDropdownByLabel failed for {}: {}", s, e.getMessage());
            }
        }
        LoggerUtil.LOGGER.warn("[FEATURE-UTIL] Dropdown selection '{}' failed using fallbacks", label);
    }

    private static void typeAndConfirm(Page page, List<String> selectors, String text) {
        for (String s : selectors) {
            try {
                Locator l = page.locator(s);
                if (l.count() > 0) { 
                    Locator input = l.first();
                    input.click();
                    input.fill(text);
                    input.press("Enter");
                    LoggerUtil.LOGGER.info("[FEATURE-UTIL] Typed+confirmed '{}' into {}", text, s);
                    return;
                }
            } catch (Exception ignored) {}
        }
        LoggerUtil.LOGGER.warn("[FEATURE-UTIL] Failed to type '{}' into any selectors {}", text, selectors);
    }

    private static String generateRandomName() { return "feature-" + Instant.now().getEpochSecond() + "-" + Math.abs(new Random().nextInt() % 1000); }

    private static String escapeSingleQuotes(String s) { return s == null ? "" : s.replace("'","\\'"); }
}
