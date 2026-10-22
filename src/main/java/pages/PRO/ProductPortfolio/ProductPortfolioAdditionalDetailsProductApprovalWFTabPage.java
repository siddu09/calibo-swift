package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;


public class ProductPortfolioAdditionalDetailsProductApprovalWFTabPage {

    private final Page page;

    public ProductPortfolioAdditionalDetailsProductApprovalWFTabPage(Page page) {
        this.page = page;
    }

    public Locator enableWF() {
        return new ResilientLocator(page, "Enable Product Approval Workflow")
                .byCss(".switch")
                .byXPath("//input[@type='checkbox' and @class='switch']")
                .byCss("input[type='checkbox'][class='switch']")
                .resolve();
    }

    public Locator selectDropdownValue(String dropDown) {

        return page.locator(
                "//div[@label='" + dropDown + "']"
        );
    }

    public void selectDropDown(
            String dropDownName,
            String dropDownValue) {

        page.locator(
                        "div:text('Select')"
                )
                .click();

        selectDropdownValue(dropDownValue).click();

        LoggerUtil.LOGGER.info(
                dropDownValue +
                        " Selected DropDown Value"
        );
    }

    public Locator addWFTemplate() {
        return new ResilientLocator(page, "Add WF Template")
                .byCss("button.btn-primary:has(i.material-icons:has-text('check'))")
                .byXPath("//button[@class='btn btn-primary' and .//i[text()='check']]")
                .resolve();
    }

    public Locator workflowTemplateDropdown() {
        return new ResilientLocator(page, "Product approval workflow template dropdown")
                .byXPath("//label[contains(normalize-space(.),'Workflow')]/following::div[contains(@class,'select')][1]")
                .byText("Select")
                .resolve();
    }

    public Locator workflowTemplateOptions() {
        return new ResilientLocator(page, "Product approval workflow template options")
                .byXPath("//div[contains(@class,'menu')]//*[contains(@class,'option')]")
                .byRole(AriaRole.OPTION, "")
                .resolve();
    }

    public Locator workflowRequiredValidation() {
        return new ResilientLocator(page, "Product approval workflow required validation")
                .byXPath("//*[contains(normalize-space(.),'workflow') and contains(normalize-space(.),'required')]")
                .byText("Workflow is required")
                .resolve();
    }

    private Locator templateConfirmation() {
        return page.getByTestId("confirmation-modal")
                .filter(new Locator.FilterOptions().setHas(
                        page.getByPlaceholder("Enter a unique template name")));
    }

    public Locator editWorkflowTemplate() {
        return new ResilientLocator(page, "Edit workflow template")
                .custom("Exact workflow edit button", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Edit Workflow Template").setExact(true)))
                .byXPath("//button[normalize-space()='Edit Workflow Template']")
                .resolve();
    }

    public Locator createWorkflowTemplateFromExisting() {
        return new ResilientLocator(page, "Create workflow template from existing")
                .custom("Exact proceed button", () -> templateConfirmation().getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName("Proceed").setExact(true)))
                .byXPath("//button[normalize-space()='Proceed']")
                .resolve();
    }

    public Locator newWorkflowTemplateName() {
        return new ResilientLocator(page, "New workflow template name")
                .custom("Template name inside confirmation", () -> templateConfirmation()
                        .getByPlaceholder("Enter a unique template name"))
                .byPlaceholder("Enter a unique template name")
                .byXPath("//input[contains(@placeholder,'template name')]")
                .resolve();
    }

    public Locator enableReworkLimit() {
        return new ResilientLocator(page, "Set workflow rework limit")
                .custom("Rework switch inside confirmation", () -> templateConfirmation().getByRole(AriaRole.CHECKBOX))
                .byXPath("//label[normalize-space()='Set rework limit']/parent::div//input[@type='checkbox']")
                .custom("Rework limit switch beside label", () -> page.getByText("Set rework limit",
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=..")
                        .getByRole(AriaRole.CHECKBOX))
                .resolve();
    }

    public Locator workflowReworkLimit() {
        return new ResilientLocator(page, "Workflow rework limit input")
                .custom("Rework input inside confirmation", () -> templateConfirmation().getByPlaceholder("Enter rework limit"))
                .byPlaceholder("Enter rework limit")
                .byXPath("//label[normalize-space()='Set rework limit']/parent::div/parent::div//input[@type='text']")
                .resolve();
    }

    public Locator selectedWorkflowTemplate(String name) {
        return new ResilientLocator(page, "Selected workflow template: " + name)
                .withFirstStrategyTimeout(90000)
                .custom("Exact template name", () -> page.getByText(name,
                        new Page.GetByTextOptions().setExact(true)))
                .custom("Template name in workflow section", () -> page.getByText("Workflow Template",
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=..")
                        .getByText(name, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator selectedWorkflowReworkLimit() {
        return new ResilientLocator(page, "Selected workflow rework limit")
                .byXPath("//label[normalize-space()='Rework Limit']/parent::div/p")
                .custom("Rework limit summary beside label", () -> page.getByText("Rework Limit",
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=..").locator("p"))
                .resolve();
    }

    
}
