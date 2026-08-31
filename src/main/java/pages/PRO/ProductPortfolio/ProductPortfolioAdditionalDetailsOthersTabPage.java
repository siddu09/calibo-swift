package pages.PRO.ProductPortfolio;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class ProductPortfolioAdditionalDetailsOthersTabPage {

    private final Page page;

    public ProductPortfolioAdditionalDetailsOthersTabPage(Page page) {
        this.page = page;
    }

    public Locator othersTab() {
        return new ResilientLocator(
                page,
                "Others tab in Edit Product Portfolio"
        )
                .byCss("button[role='tab']")
                .byXPath("//button[@role='tab'][.//*[normalize-space()='Others']]")
                .byXPath("//button[@role='tab'][normalize-space(.)='Others']")
                .byText("Others")
                .resolve();
    }

    public Locator othersTabPanel() {
        return new ResilientLocator(
                page,
                "Product Portfolio additional details panel"
        )
                .byCss("#wrapped-tabpanel-ADDITIONAL_DETAILS")
                .byXPath("//div[@id='wrapped-tabpanel-ADDITIONAL_DETAILS']")
                .resolve();
    }

    public Locator portfolioValue() {
        return new ResilientLocator(
                page,
                "Product Portfolio value input field in Million USD"
        )
                .byCss("#wrapped-tabpanel-ADDITIONAL_DETAILS input[inputmode='numeric']")
                .byXPath("//label[contains(normalize-space(.),'Product Portfolio Value')]/following::input[1]")
                .byXPath("//div[@id='wrapped-tabpanel-ADDITIONAL_DETAILS']//input[@inputmode='numeric']")
                .resolve();
    }

    public Locator strategy() {
        return new ResilientLocator(
                page,
                "Product Portfolio strategy description text area"
        )
                .byCss("#wrapped-tabpanel-ADDITIONAL_DETAILS textarea")
                .byXPath("//label[contains(normalize-space(.),'Strategy')]/following::textarea[1]")
                .byXPath("//div[@id='wrapped-tabpanel-ADDITIONAL_DETAILS']//textarea")
                .resolve();
    }

    public Locator portfolioLogoFileInput() {
        return new ResilientLocator(
                page,
                "Product Portfolio logo file upload input"
        )
                .byCss("#contained-button-file")
                .byCss("input[type='file'][accept*='Image/png']")
                .byXPath("//input[@id='contained-button-file']")
                .byXPath("//div[@id='wrapped-tabpanel-ADDITIONAL_DETAILS']//input[@type='file']")
                .resolve();
    }

    public Locator browseThisComputerButton() {
        return new ResilientLocator(
                page,
                "Browse computer button for uploading Product Portfolio logo"
        )
                .byCss("button[text='Browse this computer']")
                .byXPath("//button[normalize-space()='Browse this computer']")
                .byXPath("//div[@id='wrapped-tabpanel-ADDITIONAL_DETAILS']//button[normalize-space()='Browse this computer']")
                .byText("Browse this computer")
                .resolve();
    }

    public Locator dropImageArea() {
        return new ResilientLocator(
                page,
                "Drag-and-drop area for uploading Product Portfolio logo"
        )
                .byCss("#wrapped-tabpanel-ADDITIONAL_DETAILS .nt-logo-card")
                .byXPath("//div[@id='wrapped-tabpanel-ADDITIONAL_DETAILS']//*[normalize-space()='Drop an image here']")
                .byText("Drop an image here")
                .resolve();
    }

    public Locator supportedFileTypesMessage() {
        return new ResilientLocator(
                page,
                "Product Portfolio logo supported file types information"
        )
                .byXPath("//*[contains(normalize-space(.),'Supported file types: JPEG & PNG')]")
                .byText("Supported file types: JPEG & PNG")
                .resolve();
    }

    public Locator cancelButton() {
        return new ResilientLocator(
                page,
                "Cancel Product Portfolio changes button"
        )
                .byCss("button.btn-secondary")
                .byXPath("//button[normalize-space()='Cancel']")
                .byText("Cancel")
                .resolve();
    }

    public Locator saveButton() {
        return new ResilientLocator(
                page,
                "Save Product Portfolio additional details button"
        )
                .byCss("button.btn-primary")
                .byXPath("//button[normalize-space()='Save']")
                .byText("Save")
                .resolve();
    }
}

