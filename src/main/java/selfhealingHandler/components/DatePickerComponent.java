package selfhealingHandler.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.LocatorInfo;
import selfhealingHandler.SelfHealingLocator;
import utils.LoggerUtil;

import java.util.regex.Pattern;

/**
 * Reusable component for the react-datepicker widget used across Calibo
 * Accelerate (e.g. the "CreationDate" field on Edit Product Portfolio, and
 * likely reused for other date fields too).
 *
 * <p>Ported from the original {@code com.qeframework.devsecops.components}
 * version to use SWIFT's self-healing engine ({@link SelfHealingLocator} +
 * {@link LocatorInfo}) instead of {@code ResilientLocator}, so it plugs into
 * the rest of the framework's healing/logging.</p>
 *
 * <p>Selector notes (carried over from the original - hard-won app knowledge):</p>
 * <ul>
 *   <li>Trigger input uses a genuinely stable class {@code lazsa-date-picker-input}
 *       (unlike the generic {@code form-control} on other fields).</li>
 *   <li>Day cells expose {@code role="option"} with a full aria-label
 *       ("Choose Monday, 21 July 2025") - matched via regex, avoiding any need
 *       to click prev/next month arrows repeatedly.</li>
 *   <li>Month / Year use react-select internally; scoped via the wrapper's
 *       {@code .month-dropdown} / {@code .year-dropdown} class rather than the
 *       index-based {@code react-select-N-input} id, which is not stable.</li>
 * </ul>
 *
 * Usage:
 * <pre>
 *   DatePickerComponent creationDate = new DatePickerComponent(page, "CreationDate");
 *   creationDate.setDate("21", "July", "2025");
 * </pre>
 */
public class DatePickerComponent {

    private final Page page;
    private final String elementName;

    public DatePickerComponent(Page page, String elementName) {
        this.page = page;
        this.elementName = elementName;
    }

    // ------------------------------------------------------------- public API

    /**
     * Selects the given day/month/year in the picker.
     *
     * @param day   day of month, e.g. "21"
     * @param month full month name, e.g. "July"
     * @param year  four-digit year, e.g. "2025"
     */
    public void setDate(String day, String month, String year) {
        LoggerUtil.LOGGER.info("[DatePicker:{}] setDate({}, {}, {})",
                elementName, day, month, year);

        openCalendar();
        selectMonth(month);
        selectYear(year);
        selectDay(day, month, year);
    }

    /** Opens the calendar by clicking the trigger input. */
    public void openCalendar() {
        trigger().click();
    }

    // -------------------------------------------------- self-healing locators

    /**
     * The date trigger input. Primary: the stable {@code lazsa-date-picker-input}
     * class; fallbacks widen the search progressively.
     */
    public Locator trigger() {
        return SelfHealingLocator.findElement(page, new LocatorInfo(
                "input.lazsa-date-picker-input",
                "[class*=\"lazsa-date-picker\"] input",
                "input[readonly]"));
    }

    /** Month react-select, scoped via the stable {@code .month-dropdown} wrapper. */
    public Locator monthDropdown() {
        return SelfHealingLocator.findElement(page, new LocatorInfo(
                ".month-dropdown",
                ".react-datepicker__month-dropdown-container",
                "[class*=\"month-dropdown\"]"));
    }

    /** Year react-select, scoped via the stable {@code .year-dropdown} wrapper. */
    public Locator yearDropdown() {
        return SelfHealingLocator.findElement(page, new LocatorInfo(
                ".year-dropdown",
                ".react-datepicker__year-dropdown-container",
                "[class*=\"year-dropdown\"]"));
    }

    // ------------------------------------------------------- internal helpers

    private void selectMonth(String month) {
        monthDropdown().click();
        // option text inside the opened react-select menu
        page.getByText(month, new Page.GetByTextOptions().setExact(true)).first().click();
        LoggerUtil.LOGGER.debug("[DatePicker:{}] month -> {}", elementName, month);
    }

    private void selectYear(String year) {
        yearDropdown().click();
        page.getByText(year, new Page.GetByTextOptions().setExact(true)).first().click();
        LoggerUtil.LOGGER.debug("[DatePicker:{}] year -> {}", elementName, year);
    }

    /**
     * Clicks the target day cell directly via its accessible name, e.g.
     * "Choose Monday, 21 July 2025". We match on the trailing
     * "&lt;day&gt; &lt;month&gt; &lt;year&gt;" so we don't have to know the weekday.
     */
    private void selectDay(String day, String month, String year) {
        // aria-label ends with e.g. "21 July 2025"; regex is weekday-agnostic.
        Pattern dayPattern = Pattern.compile(
                ".*\\b" + Pattern.quote(day + " " + month + " " + year) + "\\b.*");

        Locator dayCell = page.getByRole(
                AriaRole.OPTION,
                new Page.GetByRoleOptions().setName(dayPattern));

        dayCell.first().click();
        LoggerUtil.LOGGER.debug("[DatePicker:{}] day -> {} {} {}",
                elementName, day, month, year);
    }
}
