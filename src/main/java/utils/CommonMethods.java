package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import selfhealingHandler.ResilientLocator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

    public class CommonMethods {

//    private final Page page;
//
//    public CommonMethods(Page page) {
//        this.page = page;
//    }

    public static boolean pageHeader(Page page, String headerText) {
        Locator loc = page.locator("//h1[text()='" + headerText + "']");
        return loc.isVisible();
    }

    public static boolean sectionHeader(Page page, String headerText) {
        Locator loc = page.locator("//h5[text()='" + headerText + "']");
        return loc.isVisible();
    }

    public static void waitForLoaderToDisappear(Page page) {

        // Layer 1: Original spinner
        Locator loader = page.locator("//div[@class='spinner-bg']");

        page.waitForCondition(() -> loader.evaluateAll(
                "elements => elements.every(element => !element.getClientRects().length"
                        + " || getComputedStyle(element).visibility === 'hidden')").equals(Boolean.TRUE));

        // Layer 2: Additional loaders/spinners
        try {
            Locator otherLoaders = page.locator(
                    "//*[contains(@class,'MuiCircularProgress')]"
                            + " | //*[contains(@class,'loading')]"
                            + " | //*[contains(@class,'loader')]");

            if (otherLoaders.count() > 0) {
                otherLoaders.first().waitFor(
                        new Locator.WaitForOptions()
                                .setState(WaitForSelectorState.HIDDEN)
                                .setTimeout(20000));
            }
        } catch (Exception ignored) {
            // silently continue
        }

        // Layer 3: Bootstrap "spinner-border" loader (e.g. shown while browsing/searching
        // S3 folders). This spinner is optional - it doesn't always appear - so we only
        // wait for it if it's currently present, rather than requiring it to have existed.
        try {
            Locator spinnerBorder = page.locator("div.spinner-border[role='status']");
            if (spinnerBorder.count() > 0 && spinnerBorder.first().isVisible()) {
                spinnerBorder.first().waitFor(
                        new Locator.WaitForOptions()
                                .setState(WaitForSelectorState.HIDDEN)
                                .setTimeout(30000));
            }
        } catch (Exception ignored) {
            // silently continue - loader may have already disappeared between the check and the wait
        }
    }
    public static void neutralizeKnownOverlays(Page page) {

        page.evaluate("""
() => {

   const widget =
       document.getElementById('jsd-widget');

   if(widget){

      const iframe =
          widget.closest('iframe');

      if(iframe){
         iframe.style.display='none';
      }

      widget.style.display='none';
   }

}
""");

    }


    public static boolean isElementPresent(Locator locator) {
        return locator.count() > 0;
    }

    public static boolean tabHeader(Page page, String tabText) {
        Locator loc = page.locator("//span[text()='" + tabText + "']");
        return loc.isVisible();
    }

    public static void clickOnTab(Page page, String tab) {
        Locator loc = page.locator("//span[text()='" + tab + "']");
        loc.click();
        LoggerUtil.LOGGER.info(tab + " clicked");
    }

    public static Locator search(Page page, String searchText) {
        return new ResilientLocator(page, searchText)
                .byXPath("//input[@placeholder='Search']")
                .byId("search")
                .resolve();
    }

    public static Locator selectSearchedItem(Page page, String item) {
        return new ResilientLocator(page, item)
                .byXPath("//h2[text()='" + item + "']")
                .byXPath("//h2[@title='"+item+"']")
                .byXPath("//div[text()='"+item+"']")
                .resolve();
    }

    public static Locator clickButton(Page page, String buttonText)
    {
        return new ResilientLocator(page, buttonText)
                .byXPath("//button[text()='"+buttonText+"']")
                .byXPath("//button[normalize-space()='"+buttonText+"']")
                .byXPath("//button[contains(.,'"+buttonText+"')]")
                .byXPath("//a[normalize-space()='"+buttonText+"']")
                .byCss("button.btn-primary")
                .byRole(com.microsoft.playwright.options.AriaRole.BUTTON, buttonText)
                .byText(buttonText)
                .resolve();
    }


    public static String generateUniqueTitle(String prefix) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

        return prefix + "_" +
                LocalDateTime.now().format(formatter);
    }

    public static void waitForElementToBeVisible(Page page, String elementText) {
        new ResilientLocator(page, elementText)
                .byXPath("//button[text()='"+elementText+"']")
                .byXPath("//h4[text()='"+elementText+"']")
                .byXPath("//table//th[text()='"+elementText+"']")
                .byXPath("//h3[text()='"+elementText+"']")
                .byXPath("//h2[contains(text(),'"+elementText+"')]")
                .resolve().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public static Locator verifyValidationButton(Page page)
    {
        return new ResilientLocator(page, "Verify Validation Button")
                .byXPath("//button[text()='Validating']")
                .resolve();
    }
    public static void verifyValidationIsCompleted(Page page) {
        if(CommonMethods.isElementPresent(verifyValidationButton(page)))
            verifyValidationButton(page).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public static Locator clickStageInPanel(Page page, String stageName)
    {
        return new ResilientLocator(page, stageName + " Stage")
                .byXPath("//div[contains(@class,'stage')]//text()[contains(.,'" + stageName + "')]/..")
                .byXPath("//div[contains(text(),'" + stageName + "')]")
                .byText(stageName)
                .resolve();
    }

    public static Locator clickFeatureByName(Page page, String featureName)
    {
        return new ResilientLocator(page, featureName)
                .byXPath("//h2[text()='" + featureName + "']")
                .byXPath("//h2[contains(text(),'" + featureName + "')]")
                .byXPath("//div[@class='d-flex align-items-center mb-1']//h2[text()='" + featureName + "']")
                .resolve();
    }

    /* ============================================================
       SCROLL & VISIBILITY UTILITIES (Reusable across all flows)
       ============================================================ */

    /**
     * Scroll element into view if not currently visible
     * Safe for all screen sizes - does nothing if already visible
     *
     * @param locator Element to scroll into view
     * @param elementName Name for logging
     */
    public static void scrollIntoView(Locator locator, String elementName) {
        try {
            LoggerUtil.LOGGER.info("[SCROLL] Scrolling '{}' into view...", elementName);
            locator.scrollIntoViewIfNeeded();
            LoggerUtil.LOGGER.debug("[SCROLL] ✓ '{}' scrolled into view", elementName);
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[SCROLL] Could not scroll '{}': {}", elementName, e.getMessage());
        }
    }

    /**
     * Scroll page down by specified pixels
     * Use after opening a dropdown/menu that appears below the current viewport
     *
     * @param page Playwright page object
     * @param pixels How many pixels to scroll down (default 300)
     * @param reason Reason for logging
     */
    public static void scrollPageDown(Page page, int pixels, String reason) {
        try {
            LoggerUtil.LOGGER.info("[PAGE-SCROLL] Scrolling down {} pixels - {}", pixels, reason);
            page.evaluate("window.scrollBy(0, " + pixels + ")");
            page.waitForTimeout(300);
            LoggerUtil.LOGGER.debug("[PAGE-SCROLL] ✓ Page scrolled down");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[PAGE-SCROLL] Could not scroll page: {}", e.getMessage());
        }
    }

    /**
     * Convenience method: Scroll page down by 300px
     * Perfect for revealing dropdown menus, modals, or elements below fold
     *
     * @param page Playwright page object
     * @param reason Reason for logging (e.g., "to reveal dropdown options")
     */
    public static void scrollPageDown(Page page, String reason) {
        scrollPageDown(page, 300, reason);
    }

    /**
     * Check if element is enabled (not greyed out/disabled)
     * DIFFERENT from isVisible() - element can be visible but disabled
     *
     * @param locator Element to check
     * @return true if element is enabled, false if disabled
     */
    public static boolean isElementEnabled(Locator locator) {
        try {
            // Playwright's own check, because it applies the same rule the click will:
            // a present `disabled` attribute disables the element whatever its value.
            // Reading the attribute ourselves and testing isEmpty() got this wrong -
            // disabled="" is disabled, not enabled.
            boolean nativeEnabled = locator.isEnabled();

            // aria-disabled is the opposite: only the value "true" means disabled
            String ariaDisabled = locator.getAttribute("aria-disabled");
            boolean ariaSaysDisabled = "true".equalsIgnoreCase(ariaDisabled);

            // custom-styled controls that only look disabled
            Boolean hasDisabledClass = Boolean.TRUE.equals(
                locator.evaluate("el => el.classList.contains('disabled')")
            );

            boolean isEnabled = nativeEnabled && !ariaSaysDisabled && !hasDisabledClass;

            LoggerUtil.LOGGER.debug("[ENABLED-CHECK] native={}, aria-disabled={}, class.disabled={}, isEnabled={}",
                    nativeEnabled, ariaDisabled, hasDisabledClass, isEnabled);

            return isEnabled;
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[ENABLED-CHECK] Error checking element state: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Comprehensive element visibility check
     * Checks: (1) Element exists, (2) Element is visible, (3) Element is enabled (not greyed out)
     *
     * @param locator Element to check
     * @param elementName Name for logging
     * @return true if element is ready to interact with
     */
    public static boolean isElementReady(Locator locator, String elementName) {
        try {
            // Check 1: Element exists
            if (!isElementPresent(locator)) {
                LoggerUtil.LOGGER.warn("[READY-CHECK] '{}' not found in DOM", elementName);
                return false;
            }

            // Check 2: Element is visible
            if (!locator.isVisible()) {
                LoggerUtil.LOGGER.warn("[READY-CHECK] '{}' is not visible", elementName);
                return false;
            }

            // Check 3: Element is enabled (not greyed out)
            if (!isElementEnabled(locator)) {
                LoggerUtil.LOGGER.warn("[READY-CHECK] '{}' is visible but DISABLED (greyed out)", elementName);
                return false;
            }

            LoggerUtil.LOGGER.debug("[READY-CHECK] ✓ '{}' is ready for interaction", elementName);
            return true;
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[READY-CHECK] Error checking '{}': {}", elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Scroll element into view and verify it's ready to interact
     * Combines scrolling + visibility + enabled checks
     * RECOMMENDED: Use this before clicking/typing on any element
     *
     * @param locator Element to prepare for interaction
     * @param elementName Name for logging
     * @return true if element is ready, false otherwise
     */
    public static boolean scrollAndVerifyReady(Locator locator, String elementName) {
        LoggerUtil.LOGGER.info("[SCROLL-READY] Preparing '{}' for interaction...", elementName);

        // Step 1: Scroll into view
        scrollIntoView(locator, elementName);

        // Step 2: Wait a bit for animations/scrolling to complete
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 3: Verify element is ready
        boolean isReady = isElementReady(locator, elementName);

        if (isReady) {
            LoggerUtil.LOGGER.info("[SCROLL-READY] ✓ '{}' is ready for interaction", elementName);
        } else {
            LoggerUtil.LOGGER.error("[SCROLL-READY] ✗ '{}' is NOT ready. Check visibility, enabled state, or scroll position", elementName);
        }

        return isReady;
    }

    /**
     * The react-select control nearest a field label.
     * Anchored on the label, not the react-select-N id - those are assigned in mount
     * order and repeat across wizard steps (6 and 10 are each two different fields).
     */
    public static Locator reactSelectByLabel(Page page, String label) {
        // exact text(), not contains: 'Configuration' would also hit "CSL Configuration"
        // and "Cluster Configuration" in the stepper. text() also keeps the trailing
        // info icon out of the comparison.
        // disabled controls excluded - their wrapper swallows pointer events, so a
        // click just retries until timeout.
        String xpath = String.format(
                "//*[normalize-space(text())='%s']/following::div["
                        + "contains(@class,'react-select__control') and "
                        + "not(contains(@class,'react-select__control--is-disabled'))][1]", label);

        Locator candidates = page.locator("xpath=" + xpath);

        // both source branches have a "Data Source" label, prefer the visible one
        int count = candidates.count();
        for (int i = 0; i < count; i++) {
            if (candidates.nth(i).isVisible()) {
                return candidates.nth(i);
            }
        }
        return candidates.first();
    }

    /** Current value of a react-select, or "" if unset. Includes disabled controls. */
    public static String readSelectedValue(Page page, String label) {
        String xpath = String.format(
                "//*[normalize-space(text())='%s']/following::div["
                        + "contains(@class,'react-select__control')][1]"
                        + "//div[contains(@class,'react-select__single-value')]", label);
        try {
            Locator value = page.locator("xpath=" + xpath).first();
            if (value.count() == 0) {
                return "";
            }
            String text = value.textContent();
            return text == null ? "" : text.trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Selects a dropdown value only if it isn't already set.
     * Some fields are rendered read-only on a value we already want (Retriever Mode
     * sits disabled on HybridRAG) - selecting those is a no-op we can't perform, but
     * if the wanted value differs this still goes through selectFromDropdown and
     * fails loudly rather than leaving the wrong one.
     */
    public static void selectFromDropdownIfNeeded(Page page, String label, String value) {
        String current = readSelectedValue(page, label);
        if (!current.isEmpty() && current.equalsIgnoreCase(value.trim())) {
            LoggerUtil.LOGGER.info("[DROPDOWN] '{}' already set to '{}' - skipping", label, current);
            return;
        }
        selectFromDropdown(page, label, value);
    }

    /**
     * Opens a react-select by its label and picks an option by visible text.
     * Clicks the control div, not the inner input - a forced click on the input
     * bypasses React's synthetic events and the menu never opens. Options are
     * matched inside the open menu so stray page text can't be picked.
     */
    public static void selectFromDropdown(Page page, String label, String value) {
        // empty value builds :has-text('') which matches everything and silently picks the first
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "No value supplied for dropdown '" + label + "' - check the CSV column is populated");
        }

        LoggerUtil.LOGGER.info("[DROPDOWN] Selecting '{}' in '{}'", value, label);

        // step may still be mounting after Next, so poll instead of checking once
        Locator control = reactSelectByLabel(page, label);
        long deadline = System.currentTimeMillis() + 20000;
        while (control.count() == 0 && System.currentTimeMillis() < deadline) {
            page.waitForTimeout(250);
            control = reactSelectByLabel(page, label);
        }

        if (control.count() == 0) {
            dumpDropdownAnchors(page, label);
            throw new RuntimeException(String.format(
                    "No react-select found for label '%s' after 20s - see [DROPDOWN-DIAG] "
                            + "above for the labels and placeholders actually on this step", label));
        }
        control.waitFor(new Locator.WaitForOptions().setTimeout(15000));

        // centre it ourselves - competing scrolls make the click retry against a moving target
        control.evaluate("el => el.scrollIntoView({ block: 'center', behavior: 'instant' })");
        page.waitForTimeout(300);

        // CSS not xpath contains() - that also matches react-select__menu-portal
        // :visible skips a menu still closing from the previous field
        Locator menu = page.locator(".react-select__menu:visible");

        // dependent dropdowns can open empty before their fetch returns. reopening picks
        // up the late data, waiting inside the stale menu doesn't.
        int attempts = 2;
        for (int attempt = 1; attempt <= attempts; attempt++) {
            control.click();

            menu.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(15000));

            Locator options = menu.locator(".react-select__option");
            try {
                page.waitForCondition(() -> options.count() > 0,
                        new Page.WaitForConditionOptions().setTimeout(10000));
            } catch (Exception ignored) {
                // fall through, the dump below reports the empty menu
            }

            // exact first - :has-text is substring, and with 160+ ontologies one name
            // being contained in another silently picks the wrong row
            Locator option = menu.locator(
                    String.format(".react-select__option:text-is('%s')", value)).first();
            if (option.count() == 0) {
                option = menu.locator(
                        String.format(".react-select__option:has-text('%s')", value)).first();
            }

            if (option.count() > 0) {
                option.click();
                page.waitForTimeout(500);
                LoggerUtil.LOGGER.info("[DROPDOWN] ✓ '{}' = '{}'", label, value);
                return;
            }

            dumpDropdownOptions(menu, label, value, attempt, attempts);

            if (attempt < attempts) {
                page.keyboard().press("Escape");
                page.waitForTimeout(2000);  // let the pending options request finish
            }
        }

        throw new RuntimeException(String.format(
                "Option '%s' never appeared in dropdown '%s' after %d attempts - "
                        + "see [DROPDOWN-DIAG] above for the options actually offered",
                value, label, attempts));
    }

    /** logs what the dropdown actually offered, so a miss names the real options */
    private static void dumpDropdownOptions(Locator menu, String label, String wanted,
                                            int attempt, int totalAttempts) {
        LoggerUtil.LOGGER.warn("[DROPDOWN-DIAG] '{}' not found in '{}' (attempt {}/{})",
                wanted, label, attempt, totalAttempts);
        try {
            java.util.List<String> texts = menu.locator(".react-select__option").allTextContents();
            if (texts.isEmpty()) {
                LoggerUtil.LOGGER.warn("[DROPDOWN-DIAG] menu is empty - options had not loaded. "
                        + "Menu text: '{}'", menu.textContent().trim());
            } else {
                LoggerUtil.LOGGER.warn("[DROPDOWN-DIAG] {} option(s) offered: {}", texts.size(), texts);
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[DROPDOWN-DIAG] Could not read options: {}", e.getMessage());
        }
    }

    /**
     * Dumps every label and react-select placeholder on the page.
     * Called when a label anchor misses, so a wrong constant costs one run not three.
     */
    @SuppressWarnings("unchecked")
    private static void dumpDropdownAnchors(Page page, String wantedLabel) {
        LoggerUtil.LOGGER.error("[DROPDOWN-DIAG] Label '{}' matched no react-select. Page contents:", wantedLabel);
        try {
            Object result = page.evaluate(
                    "() => ({" +
                            "  labels: [...document.querySelectorAll('label')]" +
                            "    .map(l => l.innerText.trim()).filter(t => t && t.length < 60)," +
                            "  placeholders: [...document.querySelectorAll('.react-select__placeholder')]" +
                            "    .map(p => p.innerText.trim())," +
                            "  values: [...document.querySelectorAll('.react-select__single-value')]" +
                            "    .map(v => v.innerText.trim())," +
                            "  controlCount: document.querySelectorAll('.react-select__control').length" +
                            "})");
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) result;
            LoggerUtil.LOGGER.error("[DROPDOWN-DIAG] react-select controls on step: {}", map.get("controlCount"));
            LoggerUtil.LOGGER.error("[DROPDOWN-DIAG] labels: {}", map.get("labels"));
            LoggerUtil.LOGGER.error("[DROPDOWN-DIAG] placeholders: {}", map.get("placeholders"));
            LoggerUtil.LOGGER.error("[DROPDOWN-DIAG] selected values: {}", map.get("values"));
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[DROPDOWN-DIAG] Could not read page anchors: {}", e.getMessage());
        }
    }

    /**
     * Dumps every visible button with its text attribute (what our locators match on).
     * Says whether a missing button is absent entirely or just named differently.
     */
    @SuppressWarnings("unchecked")
    public static void dumpVisibleButtons(Page page, String context) {
        LoggerUtil.LOGGER.error("[BUTTON-DIAG] {} - visible buttons on page:", context);
        try {
            Object result = page.evaluate(
                    "() => [...document.querySelectorAll('button')]" +
                            "  .filter(b => b.offsetParent !== null)" +
                            "  .map(b => (b.getAttribute('text') || '') + ' | ' + b.innerText.trim())");
            java.util.List<Object> buttons = (java.util.List<Object>) result;
            if (buttons.isEmpty()) {
                LoggerUtil.LOGGER.error("[BUTTON-DIAG] none found");
            } else {
                for (Object b : buttons) {
                    LoggerUtil.LOGGER.error("[BUTTON-DIAG]   text=[{}]", b);
                }
            }
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[BUTTON-DIAG] Could not read buttons: {}", e.getMessage());
        }
    }

    /**
     * Clicks a button once it has stayed enabled for a few consecutive polls.
     * Wizard Next buttons flicker enabled while the form is still validating, and a
     * click landing in that gap is accepted but doesn't advance the step.
     */
    public static void clickWhenStable(Locator locator, String buttonName, int timeoutSeconds) {
        LoggerUtil.LOGGER.info("[STABLE-CLICK] Waiting for '{}' to be stably enabled...", buttonName);

        locator.waitFor(new Locator.WaitForOptions().setTimeout(timeoutSeconds * 1000L));

        int requiredConsecutive = 3;
        int consecutive = 0;
        int maxPolls = timeoutSeconds * 2;

        for (int poll = 0; poll < maxPolls; poll++) {
            consecutive = isElementEnabled(locator) ? consecutive + 1 : 0;
            if (consecutive >= requiredConsecutive) {
                LoggerUtil.LOGGER.info("[STABLE-CLICK] ✓ '{}' stably enabled - clicking", buttonName);
                locator.click();
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        throw new RuntimeException(String.format(
                "'%s' never stayed enabled for %d consecutive checks within %ds - "
                        + "the form is likely still invalid or a required field is unfilled",
                buttonName, requiredConsecutive, timeoutSeconds));
    }
}
