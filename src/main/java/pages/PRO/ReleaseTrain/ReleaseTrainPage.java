package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;


public class ReleaseTrainPage {

    private final Page page;

    public ReleaseTrainPage(Page page) {
        this.page = page;
    }

    public Locator addNewReleaseTrain() {
        return new ResilientLocator(page, "Add New Release Train")
                .byText("New Release Train")
                .byXPath("//button[text()='New Release Train']")
                .resolve();
    }

    public Locator searchReleaseTrain() {
        return new ResilientLocator(page, "Search Release Train")
                .byPlaceholder("Search Release Trains")
                .byXPath("//input[@placeholder='Search Release Trains']")
                .resolve();
    }

    public Locator selectReleaseTrain(String releaseTrainName) {
        return new ResilientLocator(page, "Select Release Train")
                .custom("Matching release train ready", () -> {
                    Locator train = page.getByRole(AriaRole.HEADING).filter(new Locator.FilterOptions().setHasText(releaseTrainName));
                    train.waitFor();
                    return train;
                })
                .byXPath("//h2[text()='" + releaseTrainName + "']")
                .resolve();
    }

    public Locator addNewRelease(){
        return new ResilientLocator(page, "Add New Release")
                .byText("New Release")
                .byXPath("//button[text()='New Release']")
                .resolve();
    }

}
