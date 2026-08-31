package pages.DSO;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class NewFeaturePage {

    private final Page page;
    public NewFeaturePage(Page page) {
        this.page = page;
    }

    public Locator textboxName() {
        return new ResilientLocator(page, "Feature Name")
//                .byLabel("Name")
                .byXPath("//input[@label='Name']")
                .resolve();
    }

    public Locator dropdownOwner() {
        return new ResilientLocator(page, "Select Owner")
                .byXPath("//div[text()='Select']")
                .byXPath("//div[@class='react-select__value-container css-fbi23h']")
                .resolve();
    }

    public void selectOwner(String owner) {
        dropdownOwner().click();
        page.waitForCondition(() -> page.locator("//div[@class='react-select__menu-list css-11unzgr']/div//div/label[text()='"+owner+"']").isVisible());
        page.locator("//div[@class='react-select__menu-list css-11unzgr']/div//div/label[text()='"+owner+"']").click();

    }


    public Locator clickGoToProduct() {
        return new ResilientLocator(page, "Click Go To Product")
                .byXPath("//button[normalize-space()='Go To Product']")
                .resolve();
    }
}
