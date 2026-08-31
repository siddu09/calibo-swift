package UI.DPS.helpers.DataPipeline.DataIntegrationNodeConfiguration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.h2.command.Command;
import pages.DPS.DataPipeline.DataIntegrationNodeConfiguration.DatabricksNodeConfigurationPage;
import pages.DPS.DataPipeline.DataSourceNodeCofiguration.DatabaseNodeConfigurationPage;
import pages.DPS.Utilities.PageUtility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabricksNodeConfigurationHelper {
    private final Page page;
    DatabricksNodeConfigurationPage databricksNodeConfigurationPage = null;

    public DatabricksNodeConfigurationHelper(Page page) {
        this.page=page;
        this.databricksNodeConfigurationPage = new DatabricksNodeConfigurationPage(page);
    }

    public void waitForDatabaseNodeConfigurationPageLoaded(String nodeName) {
        databricksNodeConfigurationPage.waitForDatabricksNodeConfigurationPageToBeLoaded();
    }

    public String configureDatabricksJobName() {
        Locator jobName = page.locator("input[name='configurationName']");

        String currentName = jobName.inputValue();

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String configuredJobName = currentName + timestamp;

        jobName.fill(configuredJobName);

        Locator nextBtn= databricksNodeConfigurationPage.next();
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        nextBtn.click();

        return configuredJobName;


    }

    public void configureSource()
    {
        Locator nextBtn= databricksNodeConfigurationPage.next();
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        nextBtn.click();
    }

    public void configureTarget()
    {
        Locator nextBtn= databricksNodeConfigurationPage.next();
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        nextBtn.click();
    }

    public void configureDataManagement(String sourceTableName, String targetTableName)
    {
        Locator row = page.locator("div.d-flex.my-2.pt-2.flex-row")
                .filter(new Locator.FilterOptions()
                        .setHasText(sourceTableName));

        Locator targetInput = row.locator("div.react-select__input input").first();

        targetInput.click();
        targetInput.fill(targetTableName);
        databricksNodeConfigurationPage.selectTargetTableNameFromDropdown(targetTableName);

        Locator nextBtn= databricksNodeConfigurationPage.next();
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        nextBtn.click();
    }

    public void configureSchemaMapping  (String sourceTableName, String targetTableName)
    {
        String dropdownValue = "dbo." + sourceTableName + " | " + targetTableName.toLowerCase();
        databricksNodeConfigurationPage.selectDropDown("Mapped Data",dropdownValue);
        databricksNodeConfigurationPage.addSchemaMapping().click();
        Locator nextBtn= databricksNodeConfigurationPage.next();
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        nextBtn.click();


    }

    public void addClusterConfig(){
        Locator nextBtn= databricksNodeConfigurationPage.next();
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        nextBtn.click();
    }

    public void configureNotifications(){
        Locator complete= databricksNodeConfigurationPage.complete();
        complete.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        complete.click();
    }
}