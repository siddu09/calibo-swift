package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductDetailsPage {
    private final Page page;

    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    public Locator overviewValue(String label) {
        return new ResilientLocator(page, "Product overview value: " + label)
                .byXPath("//label[normalize-space()='" + label + "']/following-sibling::*[1]")
                .custom("Value beside overview label", () -> page.getByText(label,
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=following-sibling::*[1]"))
                .resolve();
    }

    public Locator documentationLink(String text) {
        return new ResilientLocator(page, "Product documentation link")
                .byRole(AriaRole.LINK, text)
                .byXPath("//a[normalize-space()='" + text + "']")
                .resolve();
    }

    public Locator visibility(String label) {
        return new ResilientLocator(page, "Product visibility")
                .custom("Exact visibility label", () -> page.getByText(label,
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//*[normalize-space(text())='" + label + "']")
                .resolve();
    }

    public Locator owner(String name) {
        return new ResilientLocator(page, "Product owner: " + name)
                .byXPath("//label[normalize-space()='" + name + "']")
                .custom("Exact owner name", () -> page.getByText(name,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator moreHoriz() {
        return new ResilientLocator(page, "Product more actions")
                .byText("more_horiz")
                .byXPath("//button[.//*[normalize-space()='more_horiz']]")
                .resolve();
    }

    public Locator viewDetailsOrEdit() {
        return new ResilientLocator(page, "View product details or edit")
                .custom("Current product editor action", () -> productEditorAction("View Details or Edit"))
                .byXPath("//*[self::button or self::li][normalize-space()='View Details' or normalize-space()='View details' or normalize-space()='Edit']")
                .byRole(AriaRole.MENUITEM, "View Details")
                .byRole(AriaRole.MENUITEM, "Edit")
                .resolve();
    }

    public Locator productEditorAction(String label) {
        return new ResilientLocator(page, "Product editor menu action")
                .byRole(AriaRole.BUTTON, label)
                .byXPath("//button[contains(@class,'dropdown-button-item')][normalize-space()='" + label + "']")
                .byRole(AriaRole.MENUITEM, label)
                .resolve();
    }

    public Locator deleteProduct() {
        return new ResilientLocator(page, "Delete Product")
                .custom("Exact product delete action", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("^Delete(?: Product)?$"))))
                .byRole(AriaRole.MENUITEM, "Delete")
                .byXPath("//*[self::button or self::li][normalize-space()='Delete' or normalize-space()='Delete Product']")
                .resolve();
    }

    private Locator productDeletionDialog() {
        Locator withReason = page.locator("textarea");
        return page.getByRole(AriaRole.DIALOG)
                .or(page.getByTestId("confirmation-modal"))
                .filter(new Locator.FilterOptions().setHas(withReason))
                .or(page.locator("//textarea/ancestor::div[.//button[contains(normalize-space(.),'Delete')]][1]"))
                .filter(new Locator.FilterOptions().setVisible(true));
    }

    public Locator deleteProductReason() {
        return new ResilientLocator(page, "Product deletion reason")
                .withFirstStrategyTimeout(30000)
                .custom("Reason within product deletion dialog", () -> productDeletionDialog().locator("textarea"))
                .byCss("textarea[placeholder='Enter your comments...']")
                .resolve();
    }

    public Locator confirmDeleteProduct() {
        return new ResilientLocator(page, "Confirm Delete Product")
                .custom("Exact delete confirmation within dialog", () -> productDeletionDialog()
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions()
                                .setName(java.util.regex.Pattern.compile("^Delete(?: Product)?$"))))
                .resolve();
    }

    // Scope to the product header; the global tenant header also uses text-truncate.
    public Locator productTitle() {
        return new ResilientLocator(page, "Product title")
                .byCss("[data-cy='product-view-header-title']")
                .byCss("div:has(> i.new-product-icon) h1.text-truncate")
                .resolve();
    }

    public Locator fetchProductPortfolio() {

        return new ResilientLocator(
                page,
                "Product Portfolio")
                .custom(
                        "Product portfolio",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Product Portfolio")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]/a"
                                )
                )
                .byXPath("//label[normalize-space()='Product Portfolio']/following-sibling::p/a")
                .resolve();
    }

    public Locator fetchBusinessGroup() {

        return new ResilientLocator(
                page,
                "Business Group")
                .custom(
                        "Business group",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Business Group")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .byXPath("//label[normalize-space()='Business Group']/following-sibling::p")
                .resolve();
    }

    public Locator fetchProductDescription() {

        return new ResilientLocator(
                page,
                "Product Description")
                .custom(
                        "Product description",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText(
                                                        "Product Description")
                                )
                                .locator(
                                        "xpath=following-sibling::pre[1]"
                                )
                )
                .byXPath("//label[normalize-space()='Product Description']/following-sibling::pre")
                .resolve();
    }

    public Locator maturityAssessment(String label) {
        return new ResilientLocator(page, "Product maturity assessment menu action")
                .byXPath("//*[contains(concat(' ',normalize-space(@class),' '),' dropdown-button-item ')][contains(normalize-space(),'" + label + "')]")
                .byRole(AriaRole.MENUITEM, label)
                .custom("Assessment in product dropdown", () -> page.locator(".dropdown-menu")
                        .getByText(label, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }

}
