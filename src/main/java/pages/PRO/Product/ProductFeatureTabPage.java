package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class ProductFeatureTabPage {
    private final Page page;

    public ProductFeatureTabPage(Page page) {
        this.page = page;
    }

    public Locator muiIconThreeDots() {
        return new ResilientLocator(page, "Mui Icon Three Dots")
                .byXPath("//span[@class='material-icons MuiIcon-root']")
                .byText("more_horiz")
                .resolve();
    }

    public void editTheFeatureUsingThreeDots(String featureName) {
        muiIconThreeDots().click();
        page.locator("//span[@class='material-icons MuiIcon-root']/parent::button/parent::div/div/div/button[text()='"+featureName+"']").click();
    }


    public Locator clickOnFeatureName(String featureName) {
        return new ResilientLocator(page, "Feature Name")
                .byXPath("//h2[text()='" + featureName + "']")
                .resolve();
    }

    public Locator clickStages(String stages) {
        return new ResilientLocator(page, "Stages")
                .byXPath("//h4[text()='"+stages+"']")
                .resolve();
    }
}
