package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ReleaseNotesPage {
    private final Page page;

    public ReleaseNotesPage(Page page) {
        this.page = page;
    }

    public Locator releaseName(String label, String name) {
        String text = label + " " + name;
        return new ResilientLocator(page, "Release on the notes page: " + name)
                .custom("Release notes page ready", () -> {
                    Locator release = page.locator("//h1/following-sibling::div[normalize-space()='" + text + "']");
                    release.waitFor();
                    return release;
                })
                .custom("Exact release heading text", () -> page.getByText(text,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator productName(String name) {
        return new ResilientLocator(page, "Product on the notes page: " + name)
                .custom("Named product heading in the notes card", () -> {
                    Locator product = page.locator(".card-header").getByRole(AriaRole.HEADING,
                            new Locator.GetByRoleOptions().setName(name).setExact(true));
                    product.waitFor();
                    return product;
                })
                .byXPath("//div[contains(@class,'card-header')]//h4[normalize-space()='" + name + "']")
                .resolve();
    }

    public Locator productNotes(String productName) {
        return new ResilientLocator(page, "Saved release notes for product: " + productName)
                .custom("Notes body within the named product card", () -> productName(productName)
                        .locator("xpath=ancestor::div[contains(concat(' ',normalize-space(@class),' '),' card ')][1]")
                        .locator(".card-body"))
                .byXPath("//h4[normalize-space()='" + productName
                        + "']/ancestor::div[contains(concat(' ',normalize-space(@class),' '),' card ')][1]"
                        + "//div[contains(@class,'card-body')]")
                .resolve();
    }
}

