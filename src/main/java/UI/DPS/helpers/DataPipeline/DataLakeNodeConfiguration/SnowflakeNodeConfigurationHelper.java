package UI.DPS.helpers.DataPipeline.DataLakeNodeConfiguration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import pages.DPS.DataPipeline.DataLakeNodeConfiguration.SnowflakeNodeConfigurationPage;

public class SnowflakeNodeConfigurationHelper {
    SnowflakeNodeConfigurationPage snowflakeNodeConfigurationPage = null;

    public SnowflakeNodeConfigurationHelper(Page page) {
        this.snowflakeNodeConfigurationPage = new SnowflakeNodeConfigurationPage(page);
    }

    public void waitForSnowflakeNodeConfigurationPageLoaded() {
        snowflakeNodeConfigurationPage.waitForSnowflakeNodeConfigurationPageToBeLoaded();
    }

    public void configureSnowflakeNodeWithConfiguredDatastore(String datastoreName) {
        Locator radioButtonConfigurationOption = snowflakeNodeConfigurationPage
                .radioButtonConfigurationOption("Configured Datastore");
        radioButtonConfigurationOption.click();
        snowflakeNodeConfigurationPage.selectDatastoreFromDropdown(datastoreName);

        snowflakeNodeConfigurationPage.page.waitForCondition(
                () -> snowflakeNodeConfigurationPage.labelDropdownLabelWarehouse().isVisible());

        snowflakeNodeConfigurationPage.loadingIndicator("Warehouse").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN));

        snowflakeNodeConfigurationPage.buttonSave().click();
        radioButtonConfigurationOption.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        snowflakeNodeConfigurationPage.closeConfig().click();
    }

    public void navigateDataBrowsing(String tableName){
        snowflakeNodeConfigurationPage.navigateToTable().click();
        snowflakeNodeConfigurationPage.navigateToTable().fill(tableName.toUpperCase());
        snowflakeNodeConfigurationPage.selectTable(tableName.toUpperCase()).click();


    }

    public void selectItemsPerPage(int itemsPerPage) {
        snowflakeNodeConfigurationPage.dropdownItemsPerPage().click();
        snowflakeNodeConfigurationPage.selectItemsPerPage(itemsPerPage).click();
    }

}
