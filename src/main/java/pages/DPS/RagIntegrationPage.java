package pages.DPS;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

/**
 * Page object for the RAG Integration (Create Job) flow.
 */
public class RagIntegrationPage {

    private final Page page;

    public RagIntegrationPage(Page page) {
        this.page = page;
    }

    public Locator textboxJobName() {
        return new ResilientLocator(page, "Job Name")
                .byPlaceholder("Job Name")
                .byCss("input[name='jobName']")
                .byXPath("//label[contains(.,'Job Name')]/following::input[1]")
                .resolve();
    }

    public Locator buttonNext() {
        return new ResilientLocator(page, "Next Button")
                .byXPath("//button[normalize-space()='Next']")
                .byCss("button.btn-primary:has-text('Next')")
                .resolve();
    }

    public Locator buttonAddTables() {
        return new ResilientLocator(page, "Add Tables")
                .byXPath("//button[contains(.,'Add Tables') or contains(.,'Add table')]")
                .byCss("button:has-text('Add Tables')")
                .resolve();
    }

    public Locator checkboxSelectAllTables() {
        return new ResilientLocator(page, "Select All Tables Checkbox")
                .byXPath("//input[@type='checkbox' and @id='select-all-tables']")
                .byXPath("//label[contains(.,'Select all')]/preceding::input[@type='checkbox'][1]")
                .resolve();
    }

    public Locator buttonAddSelectedTables() {
        return new ResilientLocator(page, "Add Selected Tables")
                .byXPath("//button[normalize-space()='Add']")
                .byCss("button:has-text('Add')")
                .resolve();
    }

    public Locator textboxSemanticViewName() {
        return new ResilientLocator(page, "Semantic View Name")
                .byPlaceholder("Enter semantic view name")
                .byCss("input[name='semanticViewName']")
                .resolve();
    }

    public Locator textboxCortexAgentName() {
        return new ResilientLocator(page, "Cortex Agent Name")
                .byPlaceholder("Cortex Agent Name")
                .byCss("input[name='cortexAgentName']")
                .resolve();
    }

    public Locator dropdownInferenceModel() {
        return new ResilientLocator(page, "Inference Model")
                .byXPath("//label[contains(.,'Inference Model')]/following::div[contains(@class,'react-select__control')][1]")
                .resolve();
    }

    public Locator inferenceModelOption(String model) {
        return new ResilientLocator(page, "Inference Model Option: " + model)
                .byXPath("//div[contains(@class,'react-select__option') and contains(.,'" + model + "')]")
                .byCss("div.react-select__option:has-text('" + model + "')")
                .resolve();
    }

    public Locator textareaOrchestrationInstruction() {
        return new ResilientLocator(page, "Orchestration Instruction")
                .byCss("textarea[name='orchestrationInstruction']")
                .byPlaceholder("Orchestration instruction")
                .resolve();
    }

    public Locator textareaResponseFormat() {
        return new ResilientLocator(page, "Response Format")
                .byCss("textarea[name='responseFormat']")
                .byPlaceholder("Response format")
                .resolve();
    }

    public Locator buttonComplete() {
        return new ResilientLocator(page, "Complete Button")
                .byXPath("//button[normalize-space()='Complete']")
                .byCss("button:has-text('Complete')")
                .resolve();
    }

    public Locator buttonStartJob() {
        return new ResilientLocator(page, "Start Job")
                .byXPath("//button[normalize-space()='Start']")
                .resolve();
    }
}
