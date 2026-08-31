package selfhealingHandler;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Legacy entry point for self-healing element resolution, retained for
 * backward compatibility.
 *
 * <p><b>History:</b> the original implementation tried the three
 * {@link LocatorInfo} selectors (primary/secondary/tertiary) with an immediate
 * {@code count() > 0} check and Log4j logging. As part of the self-healing
 * consolidation, all resolution logic now lives in the single, unified
 * {@link ResilientLocator} engine (Playwright, unlimited fallbacks,
 * wait-for-attached per strategy, and Log4j + Allure telemetry).</p>
 *
 * <p>This class is now a THIN WRAPPER that delegates to {@link ResilientLocator}
 * so that:</p>
 * <ul>
 *   <li>Existing callers using {@code SelfHealingLocator.findElement(page, info)}
 *       (e.g. {@code LoginPage}, {@code DatePickerComponent}, and previously
 *       generated POMs) keep compiling and running with NO code changes.</li>
 *   <li>Those callers automatically inherit the hardened behaviour: real
 *       {@code waitFor(ATTACHED)} per candidate, richer failure messages, and
 *       self-healing events surfaced in the Allure report.</li>
 * </ul>
 *
 * <p><b>For new code, prefer the fluent {@link ResilientLocator} API directly</b>
 * (typed {@code byTestId}/{@code byRole}/... methods and unlimited fallbacks).
 * The Step 5 {@code PageObjectGenerator} SELF_HEALING mode now emits that fluent
 * style rather than {@link LocatorInfo}.</p>
 *
 * @deprecated Prefer {@link ResilientLocator} for new code. Retained only so
 *             legacy {@link LocatorInfo}-based call sites continue to work.
 */
@Deprecated
public class SelfHealingLocator {

    private SelfHealingLocator() {
        // static utility - no instances
    }

    /**
     * Resolves an element from a legacy {@link LocatorInfo} triple by delegating
     * to the unified {@link ResilientLocator} engine.
     *
     * @param page        the Playwright page
     * @param locatorInfo the primary/secondary/tertiary selectors
     * @return the first attached {@link Locator} among the provided selectors
     * @throws RuntimeException if none of the selectors resolve
     */
    public static Locator findElement(Page page, LocatorInfo locatorInfo) {
        return findElement(page, "element", locatorInfo);
    }

    /**
     * Overload that lets callers pass a human-readable element name so the
     * {@code [SELF-HEAL]} logs and Allure attachments are clearly labelled.
     * Existing callers can migrate to this at their convenience; the original
     * two-argument method remains fully supported.
     *
     * @param page        the Playwright page
     * @param elementName human-readable name for logs/reports
     * @param locatorInfo the primary/secondary/tertiary selectors
     * @return the first attached {@link Locator} among the provided selectors
     */
    public static Locator findElement(Page page, String elementName, LocatorInfo locatorInfo) {
        return ResilientLocator
                .fromLocatorInfo(page, elementName, locatorInfo)
                .resolve();
    }
}
