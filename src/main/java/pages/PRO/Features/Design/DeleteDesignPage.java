package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/** Confirmation controls specific to deleting a Design artifact. */
public class DeleteDesignPage {
    private final Page page;

    public DeleteDesignPage(Page page) {
        this.page = page;
    }

    public Locator confirmation(String message) {
        return new ResilientLocator(page, "Design deletion confirmation message")
                .custom("Exact confirmation text in the active modal", () -> page.getByTestId("confirmation-modal")
                        .getByText(message, new Locator.GetByTextOptions().setExact(true)))
                .byXPath("//*[@data-testid='confirmation-modal']//*[normalize-space(text())='" + message + "']")
                .custom("Confirmation in the modal heading", () -> page.locator("[data-testid='confirmation-modal'] h4")
                        .getByText(message, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator comments() {
        return new ResilientLocator(page, "Design deletion comments")
                .byCss("[data-testid='confirmation-modal'] textarea")
                .byXPath("//*[@data-testid='confirmation-modal']//textarea")
                .custom("Comments textbox inside the confirmation modal", () -> page.getByTestId("confirmation-modal")
                        .getByRole(AriaRole.TEXTBOX))
                .resolve();
    }

    public Locator confirmDelete(String label) {
        return new ResilientLocator(page, "Confirm Design deletion")
                .byCss("[data-testid='confirmation-modal'] button.btn-primary:not([disabled])")
                .custom("Enabled confirmation action in the modal", () -> page.getByTestId("confirmation-modal")
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//*[@data-testid='confirmation-modal']//button[normalize-space()='" + label
                        + "' and not(@disabled)]")
                .resolve();
    }
}
