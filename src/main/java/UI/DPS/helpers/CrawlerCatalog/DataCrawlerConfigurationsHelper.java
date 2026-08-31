package UI.DPS.helpers.CrawlerCatalog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import pages.DPS.CrawlerCatalog.ConfigureDataCrawlerPage;

public class DataCrawlerConfigurationsHelper {

    ConfigureDataCrawlerPage configureDataCrawlerPage = null;

    public DataCrawlerConfigurationsHelper(Page page) {
        this.configureDataCrawlerPage = new ConfigureDataCrawlerPage(page);
    }

    public void waitForDataPipelinePageLoaded() {
        configureDataCrawlerPage.waitForConfigureDataCrawlerPageToBeLoaded();
    }

    public void enterGeneratedCrawlerName(String crawlerName) {
        configureDataCrawlerPage.textBoxCrawlerName().fill(crawlerName);
    }

    public void clickNextButton() {
        configureDataCrawlerPage.buttonNext().click();
    }

    public void configureDatabaseCrawlerWithConfiguredDataStore(String sourceName, String dataStoreName
            , String tableNamesCommaSeparated) {
        configureDataCrawlerPage.selectSourceFromDropdown(sourceName);
        configureDataCrawlerPage.selectDatastoreFromDropdown(dataStoreName);
        configureDataCrawlerPage.selectTableFromDropdown(tableNamesCommaSeparated);

    }

    public void createDatabaseCrawlerWithConfiguredDataStore(String crawlerName, String sourceName, String dataStoreName
            , String tableNamesCommaSeparated) {
        enterGeneratedCrawlerName(crawlerName);
        clickNextButton();
        configureDatabaseCrawlerWithConfiguredDataStore(sourceName, dataStoreName, tableNamesCommaSeparated);
        Locator buttonSaveAndCrawl = configureDataCrawlerPage.buttonSaveAndCrawl();
        buttonSaveAndCrawl.click();
        buttonSaveAndCrawl.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }
}
