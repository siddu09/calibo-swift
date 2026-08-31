package tests.components;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import selfhealingHandler.components.DatePickerComponent;
import utils.LoggerUtil;

/**
 * Smoke test for {@link DatePickerComponent} (ported to SWIFT's SelfHealingLocator).
 *
 * <p>Drives the react-datepicker widget on a Calibo Accelerate form and verifies
 * the selected value is reflected back in the trigger input. Watch the logs for
 * {@code [DatePicker:...]} and any {@code SelfHealingLocator} fallback messages.</p>
 *
 * <p><b>Configure before running:</b> set {@code PAGE_URL} to a screen that
 * actually contains the date field (e.g. Edit Product Portfolio), and adjust the
 * expected-value assertion to match how Calibo renders the chosen date.</p>
 */
public class DatePickerComponentTest {

    // Local, network-free fixture that mimics the Calibo react-datepicker DOM.
    // Place datepicker-fixture.html under src/test/resources/fixtures/.
    // Swap to the real Calibo URL once network access is available.
    private static final String PAGE_URL = fixtureUrl("fixtures/datepicker-fixture.html");

    private static final String FIELD_NAME = "CreationDate";

    /** Resolves a test-resources file to a file:// URL that Playwright can open. */
    private static String fixtureUrl(String resourcePath) {
        java.nio.file.Path p = java.nio.file.Paths.get("src/test/resources", resourcePath)
                .toAbsolutePath();
        return p.toUri().toString();   // file:///.../datepicker-fixture.html
    }

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeClass
    public void setUp() {
        java.nio.file.Path f = java.nio.file.Paths.get(
                "src/test/resources/fixtures/datepicker-fixture.html").toAbsolutePath();
        if (!java.nio.file.Files.exists(f)) {
            throw new IllegalStateException("Fixture not found at: " + f
                    + " — copy datepicker-fixture.html there first.");
        }

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));   // ← was missing
        page = browser.newContext().newPage();                        // ← was missing

        page.navigate(f.toUri().toString());
        page.waitForLoadState();

        System.out.println("FIXTURE URL = " + f.toUri());
        System.out.println("PAGE TITLE  = " + page.title());          // should print the fixture title
    }

    @DataProvider(name = "dates")
    public Object[][] dates() {
        return new Object[][]{
                {"21", "July", "2025"},
                {"01", "January", "2026"},
                {"15", "December", "2025"},
        };
    }

    @Test(dataProvider = "dates")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Selects a date via the react-datepicker and verifies it is applied.")
    public void setDate_selectsExpectedDate(String day, String month, String year) {
        DatePickerComponent datePicker = new DatePickerComponent(page, FIELD_NAME);

        selectDate(datePicker, day, month, year);

        String actual = readTriggerValue(datePicker);
        LoggerUtil.LOGGER.info("[DatePickerTest] trigger value after set = '{}'", actual);

        // Fixture renders exactly "21 July 2025". Against real Calibo the format
        // may differ (e.g. "21/07/2025") - relax to contains(day) && contains(year) then.
        Assert.assertTrue(actual != null && !actual.isBlank(),
                "Date trigger should not be empty after selection");
        Assert.assertEquals(actual.trim(), day + " " + month + " " + year,
                "Trigger value mismatch");
    }

    @Test
    @Description("Calendar opens when the trigger is clicked.")
    public void openCalendar_showsPicker() {
        DatePickerComponent datePicker = new DatePickerComponent(page, FIELD_NAME);
        datePicker.openCalendar();

        Assert.assertTrue(datePicker.monthDropdown().isVisible()
                        || datePicker.yearDropdown().isVisible(),
                "Month/Year dropdown should be visible once the calendar opens");
    }

    // ------------------------------------------------------------- steps

    @Step("Select date {day} {month} {year}")
    private void selectDate(DatePickerComponent datePicker,
                            String day, String month, String year) {
        datePicker.setDate(day, month, year);
    }

    @Step("Read date trigger value")
    private String readTriggerValue(DatePickerComponent datePicker) {
        Locator trigger = datePicker.trigger();
        String val = trigger.inputValue();
        if (val == null || val.isBlank()) {
            val = trigger.getAttribute("value");   // fallback for some widgets
        }
        return val;
    }

    @AfterClass
    public void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
