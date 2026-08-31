package pages.DPS.DataPipeline.DataIntegrationNodeConfiguration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

public class DatabricksNodeConfigurationPage {

    public Page page = null;

    public DatabricksNodeConfigurationPage(Page page) {
        this.page = page;
    }

    public Locator databricksNodeConfigurationHeading() {
        return new ResilientLocator(page, "Databricks Node Configuration Heading")
                .byXPath("//h3[text()='Databricks']")
                .resolve();
    }

    public void waitForDatabricksNodeConfigurationPageToBeLoaded() {
        page.waitForCondition(() -> databricksNodeConfigurationHeading().isVisible());
    }

    public void databricksIntegrationPageTitle() {
        new ResilientLocator(page, "Databricks Integration Page Title")
                .byXPath("//h1[text()='Databricks Integration']")
                .resolve();
    }

    public Locator databricksJobName(){
        return new ResilientLocator(page, "Databricks Job Name")
                .byCss("input[name='configurationName']")
                .byXPath("//input[@name='configurationName']")
                .resolve();
    }

    public Locator next(){
        return new ResilientLocator(page, "Next Button")
                .byText("Next")
                .byXPath("//button[text()='Next']")
                .byCss("button.data-widget-complete-button")
                .resolve();
    }

    public Locator complete(){
        return new ResilientLocator(page, "Complete Button")
                .byText("Complete")
                .byXPath("//button[text()='Complete']")
                .byCss("button.data-widget-complete-button")
                .resolve();
    }

    public Locator selectDropDownValue(String dropDown) {
        return new ResilientLocator(page, "Select Dropdown Value")
                .byXPath("//label[text()='" + dropDown + "']")
                .resolve();
    }

    public Locator radioButtonConfigurationOption(String configurationOption) {
        return new ResilientLocator(page, "Configuration Option Radio Button")
                .byXPath("//label[text()='" + configurationOption + "']")
                .resolve();
    }

    public Locator arrowDropdownForDatastore() {
        return new ResilientLocator(page, "Arrow Dropdown For Datastore")
                .byXPath("//i[text()='arrow_drop_down']/parent::div")
                .resolve();
    }

    public Locator datastoreNameInDropdown(String datastoreName) {
        return new ResilientLocator(page, "Datastore Name In Dropdown")
                .byXPath("//label[text()='" + datastoreName + "']/parent::div")
                .resolve();
    }

    public void selectTargetTableNameFromDropdown(String targetTableName) {
        page.getByText("Click to create \"" + targetTableName + "\"",
                        new Page.GetByTextOptions().setExact(true))
                .click();
    }

    public void selectDropDown(
            String dropDownName,
            String dropDownValue) {

        page.locator(
                        "label:text('" + dropDownName + "')"
                )
                .locator(
                        "xpath=following::div" +
                                "[contains(@class,'react-select__control')][1]"
                )
                .click();

        selectDropdownValue(dropDownValue).click();

        LoggerUtil.LOGGER.info(
                dropDownValue +
                        " Selected DropDown Value"
        );
    }

    public Locator selectDropdownValue(String dropDown) {

        return page.locator(
                "//label[text()='" + dropDown + "']"
        );
    }


    public Locator addSchemaMapping(){
        return new ResilientLocator(page, "Add Schema Mapping")
                .byText("Add Schema Mapping")
                .byXPath("//button[text()='Add Schema Mapping']")
                .resolve();
    }

    public void selectDatastoreFromDropdown(String datastoreName) {
        arrowDropdownForDatastore().click();
        page.waitForCondition(() -> datastoreNameInDropdown(datastoreName).isVisible());
        datastoreNameInDropdown(datastoreName).click();
    }

    public Locator labelDropdownLabelWarehouse() {
        return new ResilientLocator(page, "Warehouse Dropdown Label")
                .byXPath("//label[text()='Warehouse']")
                .resolve();
    }

    public Locator loadingIndicator(String dropdownLabel) {
        return new ResilientLocator(page, "Loading Indicator")
                .byXPath("//div[contains(.,'" + dropdownLabel + "')]//div[@class='react-select__indicator" +
                        " react-select__loading-indicator css-at12u2-loadingIndicator']")
                .resolve();
    }

    public Locator buttonSave() {
        return new ResilientLocator(page, "Save Button")
                .byXPath("//button[text()='Save']")
                .resolve();
    }
}
