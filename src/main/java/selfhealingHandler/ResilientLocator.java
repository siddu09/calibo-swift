package selfhealingHandler;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import configHandler.ConfigManager;
import io.qameta.allure.Allure;
import utils.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * SWIFT self-healing element resolver (Playwright).
 *
 * <p>Registers multiple identifier strategies for a single element, tried in
 * the order you add them. The first strategy that actually resolves to an
 * ATTACHED element wins - if it fails (renamed id, removed data-testid,
 * changed label text, etc.), the next one is attempted automatically.</p>
 *
 * <p><b>Fluent usage (preferred, new style):</b></p>
 * <pre>
 *   Locator nameField = new ResilientLocator(page, "Portfolio Name Field")
 *           .byTestId("portfolio-name")
 *           .byId("portfolioName")
 *           .byRole(AriaRole.TEXTBOX, "Portfolio Name")
 *           .byPlaceholder("Enter portfolio name")
 *           .byCss("input.portfolio-name")
 *           .resolve();
 *   nameField.fill("Adai-Portfolio");
 * </pre>
 *
 * <p><b>Backward-compatible usage (existing POMs via {@link LocatorInfo}):</b></p>
 * <pre>
 *   Locator el = ResilientLocator.fromLocatorInfo(page, "Login Button", btnLogin).resolve();
 * </pre>
 *
 * <p>Order matters: put your most reliable/intentional identifiers first
 * (data-testid, id, role+accessible-name) and structural/text-based fallbacks
 * last, since those are the ones most likely to break silently when the DOM
 * changes.</p>
 *
 * <p>Healing telemetry is reported through Log4j ({@link LoggerUtil}) AND
 * attached to Allure, so "resolved via fallback #3" shows up in your reports -
 * a clear signal that the primary identifier needs fixing before it breaks
 * outright.</p>
 */
public class ResilientLocator {

    /** Per-strategy attach timeout. Kept short so fallbacks are tried quickly. */
    private static final int PER_STRATEGY_TIMEOUT_MS = 2000;

    private static final int FIRST_STRATEGY_TIMEOUT_MS = Integer.parseInt(ConfigManager.getUIProperty("timeout"));

    private final Page page;
    private final String elementName;
    private final List<Strategy> strategies = new ArrayList<>();

    public ResilientLocator(Page page, String elementName) {
        this.page = page;
        this.elementName = elementName;
    }

    // ───────────────────────────────────────────────────────────────────────
    //  Fluent identifier registration
    // ───────────────────────────────────────────────────────────────────────

    public ResilientLocator byTestId(String testId) {
        strategies.add(new Strategy("data-testid=" + testId,
                () -> page.getByTestId(testId)));
        return this;
    }

    public ResilientLocator byId(String id) {
        strategies.add(new Strategy("id=" + id,
                () -> page.locator("#" + id)));
        return this;
    }

    public ResilientLocator byName(String attrName) {
        strategies.add(new Strategy("name=" + attrName,
                () -> page.locator("[name='" + attrName + "']")));
        return this;
    }

    public ResilientLocator byLabel(String label) {
        strategies.add(new Strategy("label=" + label,
                () -> page.getByLabel(label)));
        return this;
    }

    public ResilientLocator byPlaceholder(String placeholder) {
        strategies.add(new Strategy("placeholder=" + placeholder,
                () -> page.getByPlaceholder(placeholder)));
        return this;
    }

    public ResilientLocator byRole(AriaRole role, String accessibleName) {
        strategies.add(new Strategy("role=" + role + "[name=" + accessibleName + "]",
                () -> page.getByRole(role,
                        new Page.GetByRoleOptions().setName(accessibleName))));
        return this;
    }

    public ResilientLocator byText(String text) {
        strategies.add(new Strategy("text=" + text,
                () -> page.getByText(text)));
        return this;
    }

    public ResilientLocator byCss(String cssSelector) {
        strategies.add(new Strategy("css=" + cssSelector,
                () -> page.locator(cssSelector)));
        return this;
    }

    public ResilientLocator byXPath(String xpath) {
        strategies.add(new Strategy("xpath=" + xpath,
                () -> page.locator("xpath=" + xpath)));
        return this;
    }

    /** Escape hatch for any other Playwright locator you want in the fallback chain. */
    public ResilientLocator custom(String description, Supplier<Locator> locatorSupplier) {
        strategies.add(new Strategy(description, locatorSupplier));
        return this;
    }

    /**
     * Raw Playwright-selector strategy. Accepts anything {@code page.locator()}
     * understands: css, {@code xpath=...}, {@code text=...}, {@code role=...},
     * {@code [aria-label="..."]}, etc. This is what the {@link #fromLocatorInfo}
     * bridge uses so existing {@link LocatorInfo}-based POMs keep working.
     */
    public ResilientLocator bySelector(String selector) {
        if (selector == null || selector.isBlank()) {
            return this;
        }
        strategies.add(new Strategy("selector=" + selector,
                () -> page.locator(selector)));
        return this;
    }

    // ───────────────────────────────────────────────────────────────────────
    //  Backward-compatible bridge from the legacy LocatorInfo (3 selectors)
    // ───────────────────────────────────────────────────────────────────────

    /**
     * Builds a ResilientLocator from the legacy {@link LocatorInfo} triple
     * (primary / secondary / tertiary). Lets every existing generated POM and
     * component migrate to the new engine WITHOUT changing their locator data.
     */
    public static ResilientLocator fromLocatorInfo(Page page,
                                                   String elementName,
                                                   LocatorInfo info) {
        ResilientLocator rl = new ResilientLocator(page, elementName);
        rl.bySelector(info.getPrimary());
        rl.bySelector(info.getSecondary());
        rl.bySelector(info.getTertiary());
        return rl;
    }

    // ───────────────────────────────────────────────────────────────────────
    //  Resolution
    // ───────────────────────────────────────────────────────────────────────

    /**
     * Tries each registered strategy in order. Returns the first Locator that
     *  the resolves to an ATTACHED element withinper-strategy timeout.
     * Throws a RuntimeException listing every failed attempt if none succeed.
     */
    public Locator resolve() {
        if (strategies.isEmpty()) {
            throw new IllegalStateException(
                    "No identifier strategies registered for '" + elementName + "'");
        }

        List<String> failures = new ArrayList<>();

        for (int i = 0; i < strategies.size(); i++) {
            Strategy strategy = strategies.get(i);
            try {
                Locator candidate = strategy.locatorSupplier.get();
                candidate.first().waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(i == 0 ? FIRST_STRATEGY_TIMEOUT_MS : PER_STRATEGY_TIMEOUT_MS));

                // SUCCESS
                if (i == 0) {
                    logPrimarySuccess(strategy);
                } else {
                    logHealed(strategy, i, failures);
                }
                return candidate.first();

            } catch (Exception e) {
                failures.add("[FAILED] " + strategy.description);
                logAttemptFailed(strategy, i);
                // try the next strategy
            }
        }

        // ALL strategies failed
        return failAll(failures);
    }

    // ───────────────────────────────────────────────────────────────────────
    //  Telemetry (Log4j + Allure)
    // ───────────────────────────────────────────────────────────────────────

    private void logPrimarySuccess(Strategy strategy) {
        LoggerUtil.LOGGER.info("[HEAL-OK] '{}' resolved via PRIMARY {}",
                elementName, strategy.description);
    }

    private void logHealed(Strategy winner, int index, List<String> failures) {
        String msg = "Element '" + elementName + "' HEALED - resolved via fallback #"
                + index + " (" + winner.description + ").\n"
                + "Prior failed attempts:\n  " + String.join("\n  ", failures)
                + "\nACTION: fix the primary identifier before it breaks outright.";
        LoggerUtil.LOGGER.warn("[HEAL-FALLBACK] {}", msg);
        safeAllure("Self-Healing: " + elementName, msg);
    }

    private void logAttemptFailed(Strategy strategy, int index) {
        LoggerUtil.LOGGER.debug("[HEAL-TRY] '{}' strategy #{} did not attach: {}",
                elementName, index, strategy.description);
    }

    private Locator failAll(List<String> failures) {
        String msg = "[ResilientLocator] All " + strategies.size()
                + " strategies failed for element '" + elementName
                + "'.\nAttempted, in order:\n  " + String.join("\n  ", failures);
        LoggerUtil.LOGGER.error(msg);
        safeAllure("Self-Healing FAILED: " + elementName, msg);
        throw new RuntimeException(msg);
    }

    /** Allure may not be on the call stack (e.g. plain main) - never let it break resolution. */
    private void safeAllure(String name, String content) {
        try {
            Allure.addAttachment(name, "text/plain", content);
        } catch (Exception ignored) {
            // reporting is best-effort
        }
    }

    // ───────────────────────────────────────────────────────────────────────
    //  Internal strategy holder
    // ───────────────────────────────────────────────────────────────────────

    private static class Strategy {
        final String description;
        final Supplier<Locator> locatorSupplier;

        Strategy(String description, Supplier<Locator> locatorSupplier) {
            this.description = description;
            this.locatorSupplier = locatorSupplier;
        }
    }public ResilientLocator byLabelInput(String label) {

        strategies.add(new Strategy(
                "label-input=" + label,

                () -> page.locator("label")
                        .filter(new Locator.FilterOptions()
                                .setHasText(label))
                        .locator("xpath=ancestor::div[contains(@class,'col-md-3')][1]")
                        .locator("input")
        ));

        return this;
    }


}
