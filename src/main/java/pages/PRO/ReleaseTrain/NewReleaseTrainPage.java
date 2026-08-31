package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/**
 * Auto-generated Page Object for: NewProductPortfolioPage
 * Source URL : https://accelerate-qa.calibo.com/portfolios/add-portfolio
 * Strategy   : fluent ResilientLocator chain (ALL candidates)
 * Generated  : STARTING SKELETON - review before use.
 */
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
        return new ResilientLocator(page, "Create")
                .byText("Create")
                .byCss("button")
                .byXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[4]/button[2]")
                .resolve();
    }

}
