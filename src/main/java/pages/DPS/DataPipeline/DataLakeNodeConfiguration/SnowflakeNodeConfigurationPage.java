package pages.DPS.DataPipeline.DataLakeNodeConfiguration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class SnowflakeNodeConfigurationPage {

    public Page page = null;

    public SnowflakeNodeConfigurationPage(Page page) {
        this.page = page;
    }

    public Locator snowflakeNodeConfigurationHeading() {
        return new ResilientLocator(page, "Snowflake Node Configuration Heading")
                .byXPath("//h3[text()='Snowflake']")
                .resolve();
    }

    public void waitForSnowflakeNodeConfigurationPageToBeLoaded() {
        page.waitForCondition(() -> snowflakeNodeConfigurationHeading().isVisible());
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

    public Locator closeConfig() {
        return new ResilientLocator(page, "Close Config")
                .byCss(".side-menu-close")
                .resolve();
    }

    public Locator navigateToTable(){
        return new ResilientLocator(page, "Navigate To Table")
                .byPlaceholder("Click on any file from the list to get the preview")
                .resolve();
    }

    public Locator selectTable(String tableName){
        return new ResilientLocator(page, "Select Table")
                .byXPath("//span[text()='"+tableName+"']")
                .resolve();
    }

    public Locator dropdownItemsPerPage(){
        return new ResilientLocator(page, "Dropdown Items Per Page")
                .byCss("button#pageDropDown")
                .resolve();
    }

    public Locator selectItemsPerPage(int itemsPerPage){
        return new ResilientLocator(page, "Select Items Per Page")
                .byCss("a[data-page='" + itemsPerPage + "']")
                .resolve();
    }
}
