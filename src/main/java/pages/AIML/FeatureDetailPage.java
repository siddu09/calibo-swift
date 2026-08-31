package pages.AIML;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

/**
 * Page Object for the Feature detail side-sheet that renders after a feature is created.
 *
 * <p>All fields are anchored on STABLE labels (Feature / Description / Feature Status /
 * Stages) rather than the generated styled-component classes (e.g. {@code sc-gsDJrp}),
 * so verification survives CSS-hash churn.
 */
public class FeatureDetailPage {

    private final Page page;

    public FeatureDetailPage(Page page) {
        this.page = page;
    }

    /** Feature name heading: {@code <label>Feature</label> ... <h2>NAME</h2>}. */
    public Locator featureNameHeading() {
        return new ResilientLocator(page, "Feature name heading")
                .byXPath("//label[normalize-space()='Feature']/following::h2[1]")
                .resolve();
    }

    /** Description value: {@code <label>Description</label> ... <pre>...</pre>}. */
    public Locator descriptionValue() {
        return new ResilientLocator(page, "Feature description")
                .byXPath("//label[normalize-space()='Description']/following::pre[1]")
                .resolve();
    }

    /** Feature Status value: {@code <label>Feature Status</label><p>In QA</p>}. */
    public Locator featureStatusValue() {
        return new ResilientLocator(page, "Feature status")
                .byXPath("//label[normalize-space()='Feature Status']/following-sibling::p[1]")
                .resolve();
    }

    /** A Stage entry (Develop / Deploy) rendered as an {@code <h4>} under "Stages". */
    public Locator stage(String stageName) {
        return new ResilientLocator(page, "Stage: " + stageName)
                .byXPath(String.format("//h4[normalize-space()='%s']", stageName))
                .resolve();
    }

    public String getFeatureName()   { return safe(featureNameHeading()); }
    public String getDescription()   { return safe(descriptionValue()); }
    public String getFeatureStatus() { return safe(featureStatusValue()); }

    public boolean isStageVisible(String stageName) {
        try { return stage(stageName).count() > 0; } catch (Exception e) { return false; }
    }

    public boolean isRendered() {
        try { return featureNameHeading().count() > 0; } catch (Exception e) { return false; }
    }

    private String safe(Locator l) {
        try { return l.innerText().trim(); }
        catch (Exception e) {
            LoggerUtil.LOGGER.warn("[FEATURE-DETAIL] Could not read text: {}", e.getMessage());
            return null;
        }
    }
}
