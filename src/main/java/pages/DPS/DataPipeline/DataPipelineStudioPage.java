package pages.DPS.DataPipeline;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import selfhealingHandler.ResilientLocator;
import utils.CommonMethods;
import utils.LoggerUtil;

public class DataPipelineStudioPage {

    public Page page = null;

    public DataPipelineStudioPage(Page page) {
        this.page = page;
    }

    // ============================================================
    // HELPER METHODS (following best practices from production code)
    // ============================================================

    private Locator elementByAriaLabel(String elementName, String ariaLabel) {
        return new ResilientLocator(page, elementName)
                .byCss("[aria-label='" + ariaLabel + "']")
                .byXPath("//*[@aria-label='" + ariaLabel + "']")
                .resolve();
    }

    private Locator buttonByExactText(String elementName, String buttonText) {
        return new ResilientLocator(page, elementName)
                .byXPath("//button[normalize-space()='" + buttonText + "']")
                .byXPath("//button[contains(normalize-space(),'" + buttonText + "')]")
                .resolve();
    }

    private Locator buttonByContainsText(String elementName, String buttonText) {
        return new ResilientLocator(page, elementName)
                .byXPath("//button[contains(.,'" + buttonText + "')]")
                .byXPath("//*[self::button or @role='button'][contains(.,'" + buttonText + "')]")
                .resolve();
    }

    // ============================================================
    // PAGE LOAD & HEADING
    // ============================================================

    public Locator dataPipeLineStudioHeading() {
        return new ResilientLocator(page, "Data Pipeline Studio Heading")
                .byXPath("//h1[contains(.,'Data Pipeline Studio')]")
                .resolve();
    }

    public void waitForDataPipelineStudioPageToBeLoaded() {

        page.waitForCondition(() -> dataPipeLineStudioHeading().isVisible());
    }

    public Locator buttonDataCrawler() {
        return new ResilientLocator(page, "Data Crawler")
                .byXPath("//button[text()='Data Crawler']")
                .resolve();
    }

    public Locator iconPlusAddNewStage() {
        return new ResilientLocator(page, "Add New Stage Plus Icon")
                // Strategy 1: CRITICAL - Only inside stages-container, NOT anywhere else on page
                .byXPath("//div[@class='stages-container']//div[@data-tour='reactour__add-data-pipeline-new-stage']//i[@class='material-icons']")
                .byXPath("//div[@class='stages-container']//div[@class='add-stage']//i[text()='add']")
                // Strategy 2: The wrapper div inside stages-container only
                .byXPath("//div[@class='stages-container']//div[@aria-label='add' and contains(@class, 'add-stage')]")
                .byXPath("//div[@class='stages-container']//div[@data-tour='reactour__add-data-pipeline-new-stage']")
                // Strategy 3: Nested navigation - stages-container → no-lanes-stage → add-stage
                .byXPath("//div[@class='stages-container']//div[@class='no-lanes-stage']//div[@class='add-stage']//i")
                // Strategy 4: Last resort - icon inside stages-container add-stage
                .byXPath("//div[@class='stages-container']//i[@class='material-icons']")
                .resolve();
    }

    public Locator iconPlusAddNextStage(String stageName) {
        return new ResilientLocator(page, "Add New Stage Plus Icon")
                .byXPath("//h4[contains(.,'" + stageName + "')]/ancestor::div[@class='cardBodyDesigner card-body']" +
                        "/div[@title='Add New Stage']")
                .resolve();
    }

    public Locator dropdownSelectStageType() {
        return new ResilientLocator(page, "Select Stage Type Dropdown")
                .byXPath("//div[text()='Select Stage']")
                .resolve();
    }

    public Locator stageNameInDropdown(String stageName) {
        return new ResilientLocator(page, "Stage Name In Dropdown")
                .byXPath("//label[text()='" + stageName + "']") //label[text()='Data Sources']
                .resolve();
    }

    public void selectStageTypeFromDropdown(String stageName) {
        dropdownSelectStageType().click();
        page.waitForCondition(() -> stageNameInDropdown(stageName).isVisible());
        stageNameInDropdown(stageName).click();
    }

    public Locator buttonAddStageConfirm() {
        return new ResilientLocator(page, "Add Stage Confirm Button")
                // Strategy 1: Dialog-scoped — find the Add New Stage dialog and click its Add button (most reliable)
                .byXPath("//div[contains(., 'Add New Stage')]//button[normalize-space()='Add']")
                .byXPath("//div[@role='dialog' and contains(., 'Add New Stage')]//button[normalize-space()='Add']")
                // Strategy 2: Generic button text (any Add button that is visible)
                .byXPath("//button[normalize-space()='Add']")
                // Strategy 3: Fallback to buttons inside stages-container (previous approach)
                .byXPath("//div[contains(@class,'stages-container')]//button[normalize-space()='Add']")
                .byXPath("//div[contains(@class,'add-stage')]//button[normalize-space()='Add']")
                // Strategy 4: Modal-scoped button with btn-primary class
                .byXPath("//div[contains(@class,'modal') or contains(@class,'side-menu')]//button[contains(@class,'btn') and normalize-space()='Add']")
                // Strategy 5: Any element with role=button and Add text
                .byXPath("//*[@role='button' and normalize-space()='Add']")
                .resolve();
    }

    public Locator iconPlusForAddNodeByStageName(String stageName) {
        return new ResilientLocator(page, "Add Node Plus Icon For Stage: " + stageName)
                // Strategy 1: Stage-scoped with data-tour (most intentional)
                .byXPath("//div[contains(@class,'stage-card')][contains(., '" + stageName + "')]//button[@data-tour='reactour__add-tech-stack-to-stage']")
                // Strategy 2: Stage-scoped with any data-tour containing 'add-tech-stack'
                .byXPath("//div[contains(@class,'stage-card')][contains(., '" + stageName + "')]//button[contains(@data-tour, 'add-tech-stack')]")
                // Strategy 3: cardBodyDesigner variant scoped to stage name
                .byXPath("//div[contains(@class,'cardBodyDesigner')][contains(., '" + stageName + "')]//button[contains(@data-tour, 'tech-stack')]")
                // Strategy 4: add_circle_outline icon scoped to the specific stage card
                .byXPath("//div[contains(@class,'stage-card')][contains(., '" + stageName + "')]//button[.//i[@class='material-icons' and normalize-space(text())='add_circle_outline']]")
                // Strategy 5: Button with plus text scoped to stages-container
                .byXPath("//div[contains(@class,'stages-container')]//div[contains(@class,'stage-card')][contains(., '" + stageName + "')]//button[contains(., '+')]")
                // Strategy 6: Fallback — add_circle_outline icon inside stages-container only
                .byXPath("//div[contains(@class,'stages-container')]//button[.//i[@class='material-icons' and normalize-space(text())='add_circle_outline']]")
                // Strategy 7: Fallback to left panel stages container button with icon
                .byXPath("//div[contains(@class,'stages-container')]//button[contains(@class,'btn') and .//i]")
                .resolve();
    }

    //div[@id='popover-contained']//p[text()='Microsoft SQL Server']
    public Locator labelNodeNameInPopOverList(String nodeName) {
        return new ResilientLocator(page, "Node Name: " + nodeName)
                .byXPath("//div[@id='popover-contained']//div[@role='button' and contains(.,'" + nodeName + "')]")
                .byXPath("//div[@id='popover-contained']//div[contains(., '" + nodeName + "') and not(contains(., 'Unity Catalog')) and not(contains(., 'Databricks'))]")
                .resolve();
    }

    public Locator buttonAddByNodeNameInPopOverList(String nodeName) {
        return new ResilientLocator(page, "Add Button For Node: " + nodeName)
                .byXPath("//div[@id='popover-contained']//div[@role='button' and contains(.,'" + nodeName + "')]" +
                        "/following-sibling::div[contains(.,'Add')]")
                .byXPath("//div[@id='popover-contained']//div[contains(., '" + nodeName + "') and not(contains(., 'Unity Catalog')) and not(contains(., 'Databricks'))]" +
                        "/following-sibling::div[contains(.,'Add')]")
                .resolve();
    }

    public Locator buttonNodeNameInPipeline(String nodeName) {
        return new ResilientLocator(page, "Node Name In Pipeline: " + nodeName)
                .byXPath("//*[local-name()='tspan' and text()='" + nodeName + "']/ancestor::*[contains(@class,'cursor-pointer')]")
                .resolve();
    }

    public Locator buttonPenForEditPipeline() {
        return new ResilientLocator(page, "Edit Pipeline Button")
                .byXPath("//button[@data-tour='reactour__edit-pipeline']/i[text()='edit']")
                .resolve();
    }

    public Locator dialogBoxHeadingAddTechnology() {
        return new ResilientLocator(page, "Technology Addition Dialog Box Heading")
                .byXPath("//h3[text()='You are about to add the selected technologies.']")
                .resolve();
    }

    public Locator textboxTechnologyTitle() {
        return new ResilientLocator(page, "Technology Title Textbox")
                .byPlaceholder("Add a title")
                .byName("repoName")
                .resolve();
    }

    public Locator dropdownSelectDatabricksInstance() {
        return new ResilientLocator(page, "Select Databricks Instance Dropdown")
                .byXPath("//label[text()='Databricks Instance']/../div[contains(.,'Select Instance')]")
                .resolve();
    }

    public Locator databricksInstanceNameInDropdown(String databricksInstanceName) {
        return new ResilientLocator(page, "Databricks Instance Name In Dropdown")
                .byXPath("//label[text()='" + databricksInstanceName + "']")
                .resolve();
    }

    public void selectDatabricksInstanceNameFromDropdown(String databricksInstanceName) {
        dropdownSelectDatabricksInstance().click();
        page.waitForCondition(() -> databricksInstanceNameInDropdown(databricksInstanceName).isVisible());
        databricksInstanceNameInDropdown(databricksInstanceName).click();
    }

    public Locator dropdownSelectSnowflakeStructuredRagInstance() {
        return new ResilientLocator(page, "Select Snowflake Structured RAG Instance Dropdown")
                .byXPath("//label[text()='Snowflake Structured RAG Instance']/../div[contains(.,'Select Instance')]")
                .resolve();
    }

    public Locator snowflakeStructuredRagInstanceNameInDropdown(String instanceName) {
        return new ResilientLocator(page, "Snowflake Structured RAG Instance Name In Dropdown")
                .byXPath("//label[text()='" + instanceName + "']")
                .resolve();
    }

    public void selectSnowflakeStructuredRagInstanceFromDropdown(String instanceName) {
        dropdownSelectSnowflakeStructuredRagInstance().click();
        page.waitForCondition(() -> snowflakeStructuredRagInstanceNameInDropdown(instanceName).isVisible());
        snowflakeStructuredRagInstanceNameInDropdown(instanceName).click();
    }

    public Locator buttonSaveNodeDetails() {
        return CommonMethods.clickButton(page, "Save");
    }

    public Locator buttonCloseConfigurationPanel() {
        return new ResilientLocator(page, "Close Configuration Panel")
                .byCss(".side-menu-close")
                .byCss("div.side-menu-close")
                .byXPath("//div[contains(@class,'side-menu-close')]")
                .byXPath("//*[contains(@class,'side-menu-close')]")
                .byCss(".side-menu-close svg")
                .byXPath("//button[@class='btn-close' or contains(@class, 'close')]")
                .resolve();
    }

    public Locator buttonCreateJob() {
        return new ResilientLocator(page, "Create Job Button")
                .byXPath("//button[text()='Create Job' or contains(text(), 'Create Job')]")
                .byXPath("//button[contains(.,'Create Job')]")
                .resolve();
    }

    public void clickCreateJobButton() {
        LoggerUtil.LOGGER.info("[Create Job] Clicking 'Create Job' button");
        buttonCreateJob().click();
    }

    public void closeConfigurationPanel() {
        LoggerUtil.LOGGER.info("[Config Panel] Closing configuration panel");
        // Try the X button and several fallbacks, then assert panel is closed; fail fast if not.
        Exception lastErr = null;
        try {
            buttonCloseConfigurationPanel().click();
            page.waitForTimeout(500);
        } catch (Exception e) {
            lastErr = e;
            LoggerUtil.LOGGER.warn("[Config Panel] X-button click failed: {}", e.getMessage());
            // Try clicking a common backdrop (offcanvas) to close
            try {
                Locator backdrop = page.locator(".offcanvas-backdrop");
                if (backdrop.count() > 0) {
                    backdrop.first().click();
                    page.waitForTimeout(500);
                    LoggerUtil.LOGGER.info("[Config Panel] Clicked backdrop to close panel");
                    lastErr = null;
                }
            } catch (Exception be) {
                lastErr = be;
                LoggerUtil.LOGGER.warn("[Config Panel] Backdrop click failed: {}", be.getMessage());
            }

            // Try Escape as a last-ditch attempt
            try {
                page.keyboard().press("Escape");
                page.waitForTimeout(300);
                LoggerUtil.LOGGER.info("[Config Panel] Pressed Escape to close panel");
                lastErr = null;
            } catch (Exception esc) {
                lastErr = esc;
                LoggerUtil.LOGGER.warn("[Config Panel] Escape press failed: {}", esc.getMessage());
            }
        }

        // Wait for the offcanvas to be hidden (up to 5s)
        try {
            Locator offcanvas = page.locator("div.offcanvas");
            if (offcanvas.count() > 0) {
                offcanvas.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(5000));
            } else {
                // No offcanvas present — good
            }
        } catch (Exception waitErr) {
            LoggerUtil.LOGGER.warn("[Config Panel] Offcanvas did not hide after attempts: {}", waitErr.getMessage());
            // Fail fast: if we could not close the panel, throw to signal test failure immediately.
            throw new RuntimeException("Could not close configuration panel. Last error: " + (lastErr != null ? lastErr.getMessage() : "unknown"), lastErr);
        }

        LoggerUtil.LOGGER.info("[Config Panel] ✓ Configuration panel closed");
    }

    public Locator dataStoreLabel() {
        return new ResilientLocator(page, "Datastore Label")
                .byXPath("//text()[.='Datastore']/parent::*")
                .byXPath("//div[contains(text(), 'Datastore')]")
                .resolve();
    }

    public Locator dropdownSelectSnowflakeDatastore() {
        return new ResilientLocator(page, "Select Snowflake Datastore Dropdown")
                .byXPath("//label[text()='Datastore']/ancestor::*[@class]//div[contains(@class, 'control') or contains(@class, 'css-')]")
                .byXPath("//label[text()='Datastore']/following-sibling::div")
                .byXPath("//text()[.='Datastore']/ancestor::label/following-sibling::div")
                .resolve();
    }

    public Locator snowflakeDatastoreNameInDropdown(String datastoreName) {
        return new ResilientLocator(page, "Snowflake Datastore Name In Dropdown")
                .byXPath("//div[contains(@class, 'option') and contains(., '" + datastoreName + "')]")
                .byXPath("//div[@class='css-12jo7m5']//div[contains(., '" + datastoreName + "')]")
                .byXPath("//div[text()='" + datastoreName + "']")
                .resolve();
    }

    public void selectSnowflakeDatastoreFromDropdown(String datastoreName) {
        LoggerUtil.LOGGER.info("[Datastore Selection] Attempting to select: " + datastoreName);
        try {
            Locator dropdownField = dropdownSelectSnowflakeDatastore();
            dropdownField.click();
            page.waitForTimeout(300);
            
            page.waitForCondition(() -> {
                Locator option = snowflakeDatastoreNameInDropdown(datastoreName);
                return option.isVisible();
            });
            
            snowflakeDatastoreNameInDropdown(datastoreName).click();
            LoggerUtil.LOGGER.info("[Datastore Selection] ✓ Successfully selected: " + datastoreName);
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[Datastore Selection] Failed with standard approach: " + e.getMessage());
            
            LoggerUtil.LOGGER.info("[Datastore Selection] Trying keyboard navigation approach");
            try {
                Locator dropdownField = dropdownSelectSnowflakeDatastore();
                dropdownField.click();
                page.waitForTimeout(300);
                page.keyboard().type(datastoreName);
                page.waitForTimeout(200);
                page.keyboard().press("ArrowDown");
                page.keyboard().press("Enter");
                LoggerUtil.LOGGER.info("[Datastore Selection] ✓ Successfully selected (keyboard): " + datastoreName);
            } catch (Exception e2) {
                LoggerUtil.LOGGER.warn("[Datastore Selection] Keyboard approach also failed: " + e2.getMessage());
                throw e2;
            }
        }
    }
    public Locator successStatus() {
        return page.locator("span.workflow-status-text-success");

    }

    public Locator publishPipelineConfirmation() {
        return new ResilientLocator(page, "Publish Pipeline")
                .byXPath("//div[contains(@class,'publish-pipeline-pop-up') and contains(@class,'show')]//button[normalize-space()='Publish']")
                .resolve();
    }

}
