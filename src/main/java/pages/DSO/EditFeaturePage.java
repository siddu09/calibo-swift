package pages.DSO;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class EditFeaturePage {
    private final Page page;
    public EditFeaturePage(Page page)
    {
        this.page = page;
    }

    public Locator selectPhases(String phases) {
        return new ResilientLocator(page, "Phases")
                .byXPath("//h3[text()='"+phases+"']")
                .resolve();
    }

    public Locator textboxDescription() {
        return new ResilientLocator(page, "Description")
//                .byLabel("Description")
                .byXPath("//textarea[@name='stream.description']")
                .resolve();
    }


}
