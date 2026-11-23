package pages.PRO.Features.Feature;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class DeleteFeaturePage {

    private final Page page;

    public DeleteFeaturePage(Page page) {
        this.page = page;
    }

    public Locator moreHorizIcon() {
        return new ResilientLocator(page, "More actions")
                .byXPath("//i[contains(@class,'material-icons') and normalize-space()='more_horiz']")
                .resolve();
    }

    public Locator deleteDropdownItem() {
        return new ResilientLocator(page, "Delete dropdown item")
                .byXPath("//button[contains(@class,'dropdown-button-item') and contains(@class,'dropdown-item') and normalize-space()='Delete']")
                .resolve();
    }

    public Locator proceedWithFeatureDeletionButton() {
        return new ResilientLocator(page, "Proceed with Feature Deletion")
                .byXPath("//button[normalize-space()='Proceed with Feature Deletion']")
                .resolve();
    }

    public Locator commentTextArea() {
        return new ResilientLocator(page, "Delete comment textarea")
                .byXPath("//textarea[@placeholder='Enter your comments...']")
                .resolve();
    }

    public Locator deleteFeatureButton() {
        return new ResilientLocator(page, "Delete Feature")
                .byXPath("//button[normalize-space()='Delete Feature']")
                .resolve();
    }
}

