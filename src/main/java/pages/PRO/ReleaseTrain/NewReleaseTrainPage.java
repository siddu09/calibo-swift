package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;


public class NewReleaseTrainPage {

    private final Page page;

    public NewReleaseTrainPage(Page page) {
        this.page = page;
    }

    public Locator name() {
        return new ResilientLocator(page, "name")
                .byName("name")
                .byCss("input[name='name']")
                .byXPath("//*[@name='name']")
                .resolve();
    }

    public Locator description() {
        return new ResilientLocator(page, "description")
                .byName("description")
                .byCss("textarea[name='description']")
                .byXPath("//*[@name='description']")
                .resolve();
    }

    public Locator create() {
        return new ResilientLocator(page, "Create release or release train")
                .byXPath("//button[normalize-space()='Create']")
                .custom("Create button", () -> page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Create").setExact(true)))
                .resolve();
    }

}
