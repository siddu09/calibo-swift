package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import selfhealingHandler.ResilientLocator;
import utils.CommonMethods;


public class AddTechnologiesPage {
    private final Page page;
    public AddTechnologiesPage(Page page) {
        this.page = page;
    }
    public Locator selectTechnology(String technologyName)
    {
        return new ResilientLocator(page, "Select Technology "+technologyName)
                .byXPath("//p[text()='"+technologyName+"']")
                .resolve();
    }

    public Locator txtTechnologyTitle() {
        return new ResilientLocator(page, "Technology Title")
                .byXPath("//input[@placeholder='Technology Title']")
                .byPlaceholder("Add a title")
                .resolve();
    }

    public Locator txtRepositoryName() {
        return new ResilientLocator(page, "Technology Title")
                .byXPath("//input[@placeholder='Repository name']")
                .byPlaceholder("Repository name")
                .resolve();
    }

    public Locator dropdownSelectGroup()
    {
        return new ResilientLocator(page,"Select Group ")
                .byXPath("//div[text()='Group']")
                .resolve();
    }

    public Locator dropdownVisibility()
    {
        return new ResilientLocator(page,"Select Group ")
                .byXPath("//div[text()='Visibility']")
                .resolve();
    }

    public void selectGroup(String groupName)
    {
        dropdownSelectGroup().click();
        Locator loc =  page.locator("//div[contains(text(),'Loading')]");
        if(loc.isVisible())
        {
            loc.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        }
        page.locator("//div[@class='react-select__menu-list css-11unzgr']/descendant::label[text()='"+groupName+"']").click();
    }

    public void selectVisibility(String visibilityName) {
        dropdownVisibility().click();
        page.locator("//div[@class='react-select__menu-list css-11unzgr']/descendant::label[text()='"+visibilityName+"']").click();
    }

    public Locator verifyValidationButton()
    {
        return new ResilientLocator(page, "Verify Validation Button")
                .byXPath("//button[text()='Validating']")
                .resolve();
    }

    public void verifyValidationIsCompleted() {
        verifyValidationButton().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public Locator imageAddTechnologyInProgress()
    {
        return new ResilientLocator(page, "Verify Add Technology In Progress")
                .byXPath("//i[text()='watch_later']")
//                .byXPath("//i[@title='In progress']")
                .resolve();
    }
    public Locator btnRefresh()
    {
        return new ResilientLocator(page,"Refresh")
                .byXPath("//i[text()='refresh']")
                .resolve();
    }

    public void waitForTechnologyToBeAdded() {
        boolean flag = false;
        do {
            try {
                if(imageAddTechnologyInProgress().isVisible()) {
                    btnRefresh().click();
                    page.waitForTimeout(15000);
                }
                else  {
                    flag = true;
                }
            }
            catch (Exception e) {
//                e.printStackTrace();
                flag = true;
            }
        }while (!flag);
//            if(CommonMethods.isElementPresent(imageAddTechnologyInProgress()))
//                imageAddTechnologyInProgress().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public Locator btnSaveProgressDisappear()
    {
        return new ResilientLocator(page, "Verify Save Progress Disappear")
                .byXPath("//div[@role='progressbar']")
                .resolve();
    }

    public void waitForSaveProgressDisappear() {
        if (CommonMethods.isElementPresent(btnSaveProgressDisappear()))
            btnSaveProgressDisappear().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }
}
