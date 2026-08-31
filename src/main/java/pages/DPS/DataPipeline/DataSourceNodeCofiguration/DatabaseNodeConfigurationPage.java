package pages.DPS.DataPipeline.DataSourceNodeCofiguration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class DatabaseNodeConfigurationPage {

    public Page page = null;

    public DatabaseNodeConfigurationPage(Page page) {
        this.page = page;
    }

    public Locator databaseNodeConfigurationHeading(String nodeName) {
        return new ResilientLocator(page, "Database Node Configuration Heading")
                .byXPath("//h3[text()='" + nodeName + "']")
                .resolve();
    }

    public void waitForDatabaseNodeConfigurationPageToBeLoaded(String nodeName) {
        page.waitForCondition(() -> databaseNodeConfigurationHeading(nodeName).isVisible());
    }

    public Locator radioButtonConfigurationOption(String configurationOption) {
        return new ResilientLocator(page, "Configuration Option Radio Button")
                .byXPath("//label[text()='" + configurationOption + "']")
                .resolve();
    }

    public Locator dropdownChooseCatalog() {
        return new ResilientLocator(page, "Choose Catalog Dropdown")
                .byXPath("//div[text()='Choose Catalog']/parent::div")
                .resolve();
    }

    public Locator catalogNameInDropdown(String catalogName) {
        return new ResilientLocator(page, "Catalog Name In Dropdown")
                .byXPath("//label[text()='" + catalogName + "']/parent::div")
                .resolve();
    }

    public void selectCatalogFromDropdown(String catalogName) {
        dropdownChooseCatalog().click();
        page.waitForCondition(() -> catalogNameInDropdown(catalogName).isVisible());
        catalogNameInDropdown(catalogName).click();
    }

    public Locator dropdownCatalogSchema() {
        return new ResilientLocator(page, "Catalog Schema Dropdown")
                .byXPath("//label[text()='Catalog Schema']/following-sibling::div")
                .resolve();
    }

    public Locator catalogSchemaInDropdown(String catalogSchema) {
        return new ResilientLocator(page, "Catalog Schema In Dropdown")
                .byXPath("//label[text()='" + catalogSchema + "']/parent::div")
                .resolve();
    }

    public void selectCatalogSchemaFromDropdown(String catalogSchema) {
        dropdownCatalogSchema().click();
        page.waitForCondition(() -> catalogSchemaInDropdown(catalogSchema).isVisible());
        catalogSchemaInDropdown(catalogSchema).click();
    }

    public Locator labelSchemaTables() {
        return new ResilientLocator(page, "Schema Tables Label")
                .byXPath("//label[text()=' Schema Tables ']/parent::div")
                .resolve();
    }

    public Locator buttonSave() {
        return new ResilientLocator(page, "Save Button")
                .byXPath("//button[text()='Save']")
                .resolve();
    }


}
