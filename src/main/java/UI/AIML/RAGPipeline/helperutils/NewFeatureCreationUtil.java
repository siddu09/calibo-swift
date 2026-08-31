package UI.AIML.RAGPipeline.helperutils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import assertionsHandler.AssertionManager;
import pages.AIML.FeatureDetailPage;
import pages.AIML.NewFeaturePage;
import utils.FeatureData; // ➕ added
import utils.LoggerUtil; // ➕ added
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Thin ORCHESTRATOR for feature creation in Calibo Accelerate.
 *
 * <p>All element locators and per-field interactions now live in {@link NewFeaturePage}
 * (creation form) and {@link FeatureDetailPage} (post-create side-sheet), so this class
 * only sequences business steps. This keeps SWIFT's layering clean:
 * <pre>
 *   Test  →  FeatureCreationUtil (orchestration)  →  NewFeaturePage / FeatureDetailPage (POM)
 *         →  ResilientLocator (self-healing)       →  Playwright
 * </pre>
 *
 * <p>Backward compatible: {@link #createFeature(Page, FeatureData)} keeps the same
 * signature and still returns the feature name actually used.
 */
public final class NewFeatureCreationUtil {

    // Field labels the react-select dropdowns are anchored against.
    private static final String LBL_STATUS = "Feature Status";
    private static final String LBL_OWNER  = "Owner";
    private static final String LBL_TEAM   = "Team";

    private NewFeatureCreationUtil() {}

    // ======================================================================
    //  Public API
    // ======================================================================

    /** Convenience: opens the New Feature form (no data yet). */
    public static void clickNewFeature(Page page) {
        new NewFeaturePage(page).btnNewFeature().click();
    }

    /**
     * Creates a feature from the supplied data and dismisses the success modal.
     *
     * @return the feature name actually used (supplied, or auto-generated), so callers
     *         can search/verify the created feature afterwards.
     */
    @Step("Create feature")
    public static String createFeature(Page page, FeatureData data) {
        if (data == null) data = FeatureData.builder().build();
        NewFeaturePage form = new NewFeaturePage(page);

        form.btnNewFeature().click();

        form.selectPhases(data.getPhases());

        String nameToUse = (data.getName() != null && !data.getName().isBlank())
                ? data.getName() : generateRandomName();
        form.textboxName().fill(nameToUse);

        if (data.getShortDescription() != null && !data.getShortDescription().isBlank()) {
            form.textboxShortDescription().fill(data.getShortDescription());
        }
        if (data.getDescription() != null && !data.getDescription().isBlank()) {
            form.textboxDescription().fill(data.getDescription());
        }
        if (data.getCompletionDate() != null && !data.getCompletionDate().isBlank()) {
            form.inputCompletionDate().fill(data.getCompletionDate());
        }
        if (data.getStatus() != null && !data.getStatus().isBlank()) {
            form.selectFromReactDropdown(LBL_STATUS, data.getStatus());
        }
        if (data.hasOwners()) {
            for (String owner : data.getOwners()) form.addMultiSelectEntry(LBL_OWNER, owner);
        }
        if (data.hasTeamMembers()) {
            for (String member : data.getTeamMembers()) form.addMultiSelectEntry(LBL_TEAM, member);
        }
        if (data.hasUserStories()) {
            String joined = String.join("\n- ", data.getUserStories());
            form.textboxUserStorySearch().fill("- " + joined);
        }

        form.submitAndDismissSuccessModal();

        LoggerUtil.LOGGER.info("[FEATURE-UTIL] createFeature complete. name='{}'", nameToUse);
        return nameToUse;
    }

    /**
     * Creates a feature via the simplified "New Feature(s)" inline screen reached
     * by clicking "Yes" on the "Do you want to create a feature for this product?"
     * prompt (immediately after Product creation). Unlike createFeature(), this
     * screen is already open (no "New Feature" button to click) and only exposes
     * Name + Owners fields - no phases/description/status/completion date.
     *
     * Reuses NewFeaturePage's stable data-cy/label-anchored locators, since the
     * underlying form component is shared with the full creation form.
     *
     * @return the feature name actually used (supplied, or auto-generated).
     */
    @Step("Create feature (inline, from product creation prompt)")
    public static String createFeatureInline(Page page, FeatureData data) {
        if (data == null) data = FeatureData.builder().build();
        NewFeaturePage form = new NewFeaturePage(page);

        String nameToUse = (data.getName() != null && !data.getName().isBlank())
                ? data.getName() : generateRandomName();

        // The inline creation screen is shown after clicking "Yes" on the product prompt.
        // Wait (up to 15s) for the inline name input to appear via several candidate selectors
        int waited = 0;
        final int timeout = 15000;
        while (waited < timeout) {
            try {
                if (page.locator("input[label='Name']").count() > 0
                        || page.locator("input.form-control[type='text']").count() > 0
                        || page.locator("input[aria-label='Name']").count() > 0
                        || page.locator("input[data-cy='workstream-new-name']").count() > 0) {
                    break;
                }
            } catch (Exception ignored) {
                // swallow transient DOM race exceptions and retry
            }
            page.waitForTimeout(500);
            waited += 500;
        }

       LoggerUtil.LOGGER.info("[FEATURE-UTIL] Waiting for inline input done, proceeding to fill: '{}'", nameToUse);
        form.inlineTextboxName().fill(nameToUse);
        LoggerUtil.LOGGER.info("[FEATURE-UTIL] Filled feature name: '{}'", nameToUse);

        if (data.hasOwners()) {
            LoggerUtil.LOGGER.info("[FEATURE-UTIL] Adding {} owner(s)", data.getOwners().size());
            for (String owner : data.getOwners()) {
                form.addMultiSelectEntry(LBL_OWNER, owner);
                page.waitForTimeout(800);  // stabilize between owner selections
            }
            // Wait for form validation to complete after all owners added
            page.waitForTimeout(2000);
            LoggerUtil.LOGGER.info("[FEATURE-UTIL] ✓ All owners added, form stabilizing");
        }

        form.submitAndDismissSuccessModal();

        LoggerUtil.LOGGER.info("[FEATURE-UTIL] createFeatureInline complete. name='{}'", nameToUse);
        return nameToUse;
    }

    /**
     * Creates a feature AND soft-asserts the resulting detail side-sheet reflects the input.
     * Remember to call {@link AssertionManager#assertAll()} in your test to surface mismatches.
     *
     * @return the feature name used.
     */
    @Step("Create feature and verify details")
    public static String createAndVerifyFeature(Page page, FeatureData data) {
        String name = createFeature(page, data);
        verifyFeatureDetails(page, data.toBuilder().name(name).build());
        return name;
    }

    /**
     * Soft-asserts the Feature detail side-sheet against the supplied data using
     * {@link AssertionManager}. Anchors on stable labels via {@link FeatureDetailPage}.
     *
     * @return true if the side-sheet rendered (name heading visible), else false.
     */
    @Step("Verify feature details")
    public static boolean verifyFeatureDetails(Page page, FeatureData data) {
        FeatureDetailPage detail = new FeatureDetailPage(page);

        if (!detail.isRendered()) {
            LoggerUtil.LOGGER.warn("[FEATURE-VERIFY] Detail side-sheet did not render");
            AssertionManager.softAssertTrue(false, "Feature detail side-sheet did not render after creation");
            return false;
        }

        if (data.getName() != null && !data.getName().isBlank()) {
            String actual = detail.getFeatureName();
            AssertionManager.softAssertTrue(
                    actual != null && actual.contains(data.getName()),
                    "Feature name mismatch. Expected to contain '" + data.getName() + "' but was '" + actual + "'");
        }
        if (data.getDescription() != null && !data.getDescription().isBlank()) {
            String actual = detail.getDescription();
            AssertionManager.softAssertTrue(
                    actual != null && actual.contains(data.getDescription()),
                    "Description mismatch. Expected to contain '" + data.getDescription() + "' but was '" + actual + "'");
        }
        if (data.getStatus() != null && !data.getStatus().isBlank()) {
            String actual = detail.getFeatureStatus();
            AssertionManager.softAssertTrue(
                    actual != null && actual.contains(data.getStatus()),
                    "Status mismatch. Expected '" + data.getStatus() + "' but was '" + actual + "'");
        }
        if (data.hasPhases()) {
            for (String phase : data.getPhases()) {
                if (phase == null || phase.isBlank()) continue;
                AssertionManager.softAssertTrue(
                        detail.isStageVisible(phase.trim()),
                        "Expected stage '" + phase + "' not visible in Stages section");
            }
        }
        LoggerUtil.LOGGER.info("[FEATURE-VERIFY] Verification complete for '{}'", data.getName());
        return true;
    }

    // ======================================================================
    //  Helpers
    // ======================================================================

    private static String generateRandomName() {
        return "feature-" + Instant.now().getEpochSecond()
                + "-" + ThreadLocalRandom.current().nextInt(1000);
    }
}
