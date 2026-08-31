package UI.DPS.helpers.DataPipeline.DataSourceNodeConfiguration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import pages.DPS.DataPipeline.DataSourceNodeCofiguration.DatabaseNodeConfigurationPage;
import pages.DPS.Utilities.PageUtility;

public class DatabaseNodeConfigurationHelper {
    DatabaseNodeConfigurationPage databaseNodeConfigurationPage = null;

    public DatabaseNodeConfigurationHelper(Page page) {
        this.databaseNodeConfigurationPage = new DatabaseNodeConfigurationPage(page);
    }

    public void waitForDatabaseNodeConfigurationPageLoaded(String nodeName) {
        databaseNodeConfigurationPage.waitForDatabaseNodeConfigurationPageToBeLoaded(nodeName);
    }

    public void configureDataBaseNodeWithDataIngestionCatalog(String catalogName, String catalogSchema) {
        Locator radioButtonConfigurationOption = databaseNodeConfigurationPage.radioButtonConfigurationOption("Data Ingestion Catalog");
        radioButtonConfigurationOption.click();
        databaseNodeConfigurationPage.selectCatalogFromDropdown(catalogName);
        databaseNodeConfigurationPage.selectCatalogSchemaFromDropdown(catalogSchema);
        databaseNodeConfigurationPage.page.waitForCondition(()
                -> databaseNodeConfigurationPage.labelSchemaTables().isVisible());

        PageUtility.clickWithRetry(databaseNodeConfigurationPage.page, databaseNodeConfigurationPage.buttonSave(),
                databaseNodeConfigurationPage.buttonSave(), WaitForSelectorState.HIDDEN, 5, 500);




    }
}