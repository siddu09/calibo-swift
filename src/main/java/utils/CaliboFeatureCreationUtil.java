package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import utils.LoggerUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Product-specific enhanced feature creation helper.
 * Accepts a FeatureData DTO to parameterize phases, name, descriptions, status, owners, team members and user stories.
 * Keeps heuristics and fallbacks so it works across different DOM variations.
 */
public final class CaliboFeatureCreationUtil {

    private CaliboFeatureCreationUtil() {}

    public static class FeatureData {
        public List<String> phases = new ArrayList<>();
        public String name;
        public String shortDescription;
        public String description;
        public String completionDate;
        public String status = "In QA"; // default label expected
        public List<String> owners = new ArrayList<>();
        public List<String> teamMembers = new ArrayList<>();
        public List<String> userStories = new ArrayList<>();
    }

    // Clicks the New Feature control and waits for the form/dialog heuristics
    public static void clickNewFeature(Page page) {
        LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Clicking New Feature control");
        // Reuse existing resilient strategies similar to FeatureCreationUtil
        List<String> selectors = Arrays.asList(
                "button.btn.btn-primary:has-text('New Feature')",
                "button:has-text('New Feature')",
                "[data-testid='new-feature'], [data-test='add-feature'], button[aria-label='New Feature']",
                "button.sc-bBHwJV.hDsuPB.btn.btn-primary, button.btn.btn-primary",
                "button.add-new-feature, a.add-new-feature, .add-feature-btn"
        );

        boolean clicked = false;
        for (String s : selectors) {
            try {
                Locator l = page.locator(s);
                if (l.count() > 0) { l.first().click(); clicked = true; LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Clicked New Feature via {}", s); break; }
            } catch (Exception ignored) {}
        }
        if (!clicked) {
            try {
                Locator txt = page.getByText("New Feature");
                if (txt.count() > 0) {
                    Locator card = txt.locator("xpath=ancestor::button[1]");
                    if (card.count() > 0) { card.first().click(); clicked = true; LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Clicked New Feature via ancestor button"); }
                    else { Locator anc = txt.locator("xpath=ancestor::a[1]"); if (anc.count() > 0) { anc.first().click(); clicked = true; LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Clicked New Feature via ancestor anchor"); } }
                }
            } catch (Exception ignored) {}
        }
        if (!clicked) {
            LoggerUtil.LOGGER.warn("[CALIBO-FEATURE] Failed to click New Feature using fallbacks");
        }
        // short heuristic wait for modal/form
        try {
            page.locator("[role='dialog'], [aria-modal='true'], .modal, .dialog, [data-testid='new-feature-modal']").waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (Exception ignored) {
            LoggerUtil.LOGGER.warn("[CALIBO-FEATURE] Post-click modal heuristics did not match quickly; continuing");
        }
    }

    // High-level API: create feature using provided data
    public static void createFeature(Page page, FeatureData data) {
        if (data == null) data = new FeatureData();

        clickNewFeature(page);

        // 1) phases
        if (data.phases != null && !data.phases.isEmpty()) {
            for (String p : data.phases) selectPhase(page, p);
        }

        // 2) name
        String nameToUse = data.name != null && !data.name.isBlank() ? data.name : generateRandomName();
        fillInputWithFallbacks(page, Arrays.asList(
                "input[name='title']",
                "input[name='name']",
                "input[placeholder*='name']",
                "input[placeholder*='title']",
                "input[aria-label*='name']",
                "#title",
                "#name"
        ), nameToUse);

        // 3) shortDescription
        if (data.shortDescription != null && !data.shortDescription.isBlank()) {
            fillInputWithFallbacks(page, Arrays.asList(
                    "input[name='shortDescription']",
                    "input[name='short_desc']",
                    "textarea[name='shortDescription']",
                    "textarea[placeholder*='short']",
                    "input[placeholder*='short']"
            ), data.shortDescription);
        }

        // 4) description
        if (data.description != null && !data.description.isBlank()) {
            fillInputWithFallbacks(page, Arrays.asList(
                    "textarea[name='description']",
                    "textarea[placeholder*='description']",
                    "textarea[aria-label*='description']",
                    "input[name='description']"
            ), data.description);
        }

        // 5) completion date - optional
        if (data.completionDate != null && !data.completionDate.isBlank()) {
            fillInputWithFallbacks(page, Arrays.asList(
                    "input[name='completionDate']",
                    "input[placeholder*='completion']",
                    "input[type='date']"
            ), data.completionDate);
        }

        // 6) status
        if (data.status != null && !data.status.isBlank()) {
            trySelectDropdownByLabel(page, Arrays.asList(
                    "select[name='status']",
                    "select[aria-label*='status']",
                    "select[id*='status']"
            ), data.status);
        }

        // 7) owners and team members
        if (data.owners != null && !data.owners.isEmpty()) {
            for (String o : data.owners) typeAndConfirm(page, Arrays.asList(
                    "input[placeholder*='owner']",
                    "input[aria-label*='owner']",
                    "input[name*='owner']"
            ), o);
        }
        if (data.teamMembers != null && !data.teamMembers.isEmpty()) {
            for (String t : data.teamMembers) typeAndConfirm(page, Arrays.asList(
                    "input[placeholder*='team']",
                    "input[aria-label*='team']",
                    "input[name*='team']"
            ), t);
        }

        // 8) user stories
        if (data.userStories != null && !data.userStories.isEmpty()) {
            String joined = data.userStories.stream().collect(Collectors.joining("\n- ", "- ", ""));
            fillInputWithFallbacks(page, Arrays.asList(
                    "textarea[name='userStories']",
                    "textarea[placeholder*='user story']",
                    "textarea[aria-label*='user story']",
                    "input[name='userStories']"
            ), joined);
        }

        // 9) click create
        List<String> createButtons = Arrays.asList(
                "button:has-text('Create')",
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
                if (btn.count() > 0) { btn.first().click(); LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Clicked create button: {}", b); clicked = true; break; }
            } catch (Exception ignored) {}
        }
        if (!clicked) LoggerUtil.LOGGER.warn("[CALIBO-FEATURE] Create button not found with fallbacks");
    }

    // ---------------- helpers ----------------
    private static void selectPhase(Page page, String phase) {
        if (phase == null || phase.isBlank()) return;
        String[] candidates = new String[]{
                String.format("div.project-type-section h3:has-text('%s')", escapeSingleQuotes(phase)),
                String.format("h3:has-text('%s')", escapeSingleQuotes(phase)),
                String.format("text=%s", escapeSingleQuotes(phase))
        };
        for (String sel : candidates) {
            try {
                Locator h = page.locator(sel);
                if (h.count() > 0) {
                    try {
                        Locator card = h.locator("xpath=ancestor::div[contains(@class,'card')][1]");
                        if (card.count() > 0) { card.first().click(); LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Selected phase '{}' via {}", phase, sel); return; }
                    } catch (Exception ignored) {}
                    h.first().click(); LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Selected phase '{}' via header {}", phase, sel); return;
                }
            } catch (Exception ignored) {}
        }
        LoggerUtil.LOGGER.warn("[CALIBO-FEATURE] Unable to select phase '{}'; no matching selector", phase);
    }

    private static void fillInputWithFallbacks(Page page, List<String> selectors, String value) {
        for (String s : selectors) {
            try {
                Locator l = page.locator(s);
                if (l.count() > 0) { l.first().fill(value); LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Filled '{}' into {}", value, s); return; }
            } catch (Exception ignored) {}
        }
        LoggerUtil.LOGGER.warn("[CALIBO-FEATURE] No input found for selectors {} to fill value'{}'", selectors, value);
    }

    private static void trySelectDropdownByLabel(Page page, List<String> selectors, String label) {
        for (String s : selectors) {
            try {
                Locator l = page.locator(s);
                if (l.count() > 0) { l.first().selectOption(new SelectOption().setLabel(label)); LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Selected '{}' in dropdown {}", label, s); return; }
            } catch (Exception e) {
                try {
                    Locator l2 = page.locator(s);
                    if (l2.count() > 0) { l2.first().click(); l2.first().type(label); l2.first().press("Enter"); LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Typed '{}' into dropdown {}", label, s); return; }
                } catch (Exception ignored) {}
            }
        }
        LoggerUtil.LOGGER.warn("[CALIBO-FEATURE] Dropdown selection '{}' failed using fallbacks", label);
    }

    private static void typeAndConfirm(Page page, List<String> selectors, String text) {
        for (String s : selectors) {
            try {
                Locator l = page.locator(s);
                if (l.count() > 0) { Locator input = l.first(); input.click(); input.fill(text); input.press("Enter"); LoggerUtil.LOGGER.info("[CALIBO-FEATURE] Typed+confirmed '{}' into {}", text, s); return; }
            } catch (Exception ignored) {}
        }
        LoggerUtil.LOGGER.warn("[CALIBO-FEATURE] Failed to type '{}' into any selectors {}", text, selectors);
    }

    private static String generateRandomName() {
        return "feature-" + Instant.now().getEpochSecond() + "-" + Math.abs(new Random().nextInt() % 1000);
    }

    private static String escapeSingleQuotes(String s) { return s == null ? "" : s.replace("'","\\'"); }
}
