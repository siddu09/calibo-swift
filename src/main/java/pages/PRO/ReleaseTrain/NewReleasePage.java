package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;


public class NewReleasePage {

    private final Page page;

    public NewReleasePage(Page page) {
        this.page = page;
    }

    public Locator name() {
        return new ResilientLocator(page, "name")
                .byName("releaseName")
                .byCss("input[name='releaseName']")
                .byXPath("//*[@name='releaseName']")
                .resolve();
    }

    public Locator version() {
        return new ResilientLocator(page, "version")
                .byName("releaseVersion")
                .byCss("input[name='releaseVersion']")
                .byXPath("//*[@name='releaseVersion']")
                .resolve();
    }

    public Locator releaseId() {
        return new ResilientLocator(page, "releaseId")
                .byName("releaseId")
                .byCss("input[name='releaseId']")
                .byXPath("//*[@name='releaseId']")
                .resolve();
    }

    public Locator releaseObjective() {
        return new ResilientLocator(page, "releaseObjective")
                .byName("releaseObjective")
                .byCss("textarea[name='releaseObjective']")
                .byXPath("//*[@name='releaseObjective']")
                .resolve();
    }

    public Locator create() {
        return new ResilientLocator(page, "Create release or release train")
                .byXPath("//button[normalize-space()='Create']")
                .custom("Create button", () -> page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Create").setExact(true)))
                .resolve();
    }

    public Locator dropdown(String label) {
        String fieldLabel = label.replaceFirst("^Select ", "");
        String labelMatch = label.startsWith("Select ")
                ? "normalize-space()='" + fieldLabel + "'"
                : "starts-with(normalize-space(),'" + fieldLabel + "')";
        return new ResilientLocator(page, "Release dropdown: " + label)
                .custom("Labelled dropdown ready", () -> {
                    Locator control = page.locator("//label[" + labelMatch
                            + "]/following::div[contains(@class,'react-select__control')][1]");
                    control.waitFor();
                    return control;
                })
                .byXPath("//label[" + labelMatch + "]/parent::div//div[contains(@class,'react-select__control')]")
                .byXPath("//div[contains(@class,'react-select__control')][.//*[normalize-space()='" + label + "']]")
                .resolve();
    }

    public Locator dropdownSearch(String label) {
        String fieldLabel = label.replaceFirst("^Select ", "");
        return new ResilientLocator(page, "Release dropdown search: " + label)
                .custom("Search in labelled dropdown", () -> dropdown(label).locator("input[type='text']"))
                .byXPath("//label[normalize-space()='" + fieldLabel + "']/following::input[@type='text'][1]")
                .resolve();
    }

    public Locator field(String label) {
        return new ResilientLocator(page, "Release field: " + label)
                .byXPath("//label[starts-with(normalize-space(),'" + label
                        + "')]/following::*[self::input or self::textarea][1]")
                .custom("Field by label", () -> page.getByLabel(label))
                .resolve();
    }

    public void selectDropDown(String dropDownName, String dropDownValue) {
        Locator control = dropdown(dropDownName);
        control.evaluate("element => element.scrollIntoView({block: 'center', inline: 'nearest'})");
        control.click();
        dropdownSearch(dropDownName).fill(dropDownValue);
        selectDropdownValue(dropDownValue).click();
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(dropdown(dropDownName)).containsText(dropDownValue);
        LoggerUtil.LOGGER.info("{} Selected DropDown Value", dropDownValue);
    }

    public Locator selectDropdownValue(String value) {
        return new ResilientLocator(page, "Release dropdown option: " + value)
                .custom("Exact dropdown result", () -> {
                    Locator option = page.locator("[class*='react-select__menu']")
                            .getByText(value, new Locator.GetByTextOptions().setExact(true));
                    option.first().waitFor();
                    return option;
                })
                .byXPath("//div[contains(@class,'react-select__option')][normalize-space()='" + value + "']")
                .resolve();
    }
}
