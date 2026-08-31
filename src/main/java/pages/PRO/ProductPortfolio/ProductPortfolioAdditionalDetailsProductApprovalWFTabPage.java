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

    public Locator editWorkflowTemplate() {
        return new ResilientLocator(page, "Edit workflow template")
                .byXPath("//button[contains(normalize-space(.),'Edit')]")
                .byText("Edit")
                .resolve();
    }

    public Locator createWorkflowTemplateFromExisting() {
        return new ResilientLocator(page, "Create workflow template from existing")
                .byXPath("//button[contains(normalize-space(.),'Create New')]")
                .byText("Create New")
                .resolve();
    }

    
}
