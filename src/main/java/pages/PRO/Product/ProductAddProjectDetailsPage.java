package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

public class ProductAddProjectDetailsPage {
    private final Page page;

    public ProductAddProjectDetailsPage(Page page) {
        this.page = page;
    }

    public Locator publicProduct(String label) {
        return page.getByText(label, new Page.GetByTextOptions().setExact(true))
                .locator("xpath=..").locator("input[type='checkbox']");
    }

    public Locator selectedFieldValues(String label) {
        return page.getByText(label, new Page.GetByTextOptions().setExact(true))
                .locator("xpath=..").locator("[class*='react-select__single-value'], [class*='react-select__multi-value__label']");
    }

    public Locator actionButton(String label) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(label).setExact(true));
    }

    public Locator newPortfolio(String label) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(label));
    }

    public Locator noPortfolioOptions(String message) {
        return page.locator("[class*='react-select__menu']")
                .getByText(message, new Locator.GetByTextOptions().setExact(true)).last();
    }

    public Locator confirmationMessage(String message) {
        return page.getByText(message, new Page.GetByTextOptions().setExact(true));
    }

    public Locator portfolioDropdown() {
        return page.locator("//label[normalize-space()='Product Portfolio']/parent::div")
                .locator("[class*='react-select__control']");
    }

    public Locator portfolioSearch() {
        return portfolioDropdown().locator("input");
    }

    public Locator portfolioOption(String name) {
        return page.locator("[class*='react-select__menu-list']")
                .getByText(name, new Locator.GetByTextOptions().setExact(true));
    }

    public Locator title() {
        return new ResilientLocator(page, "Name")
                .byCss("input[name='title']")
                .byXPath("//*[@name=\"title\"]")
                .resolve();
    }

    public Locator checkCircleDefine() {
        return new ResilientLocator(page, "Define")
                .byXPath("//h3[text()='Define']")
                .resolve();
    }

    public Locator checkCircleDesign() {
        return new ResilientLocator(page, "Design")
                .byXPath("//h3[text()='Design']")
                .resolve();
    }

    public Locator checkCircleDevelop() {
        return new ResilientLocator(page, "Develop")
                .byXPath("//h3[text()='Develop']")
                .resolve();
    }
    public Locator clickOptionBusinessGroup(String businessGroup) {
        return new ResilientLocator(page, "Business Group option: " + businessGroup)
                .byXPath("//div[contains(@class,'react-select__menu-list')]//label[normalize-space(.)='" + businessGroup + "']")
                .byXPath("//div[contains(@class,'react-select__option')][normalize-space(.)='" + businessGroup + "']")
                .byXPath("//div[contains(@class,'react-select__option')][contains(normalize-space(.),'" + businessGroup + "')]")
                .byText(businessGroup)
                .resolve();
    }
    public Locator description() {
        return new ResilientLocator(page, "Description")
                .byCss("textarea[name=\"description\"]")
                .byXPath("//*[@name=\"description\"]")
                .resolve();
    }


    public Locator dropDownArrow() {
        return new ResilientLocator(page, "dropDownArrow")
                .byXPath("//label[text()='Business Group']/parent::div/div/div/div/div/div[@class='react-select__indicators css-1wy0on6']/div/i[text()='arrow_drop_down']")
                .resolve();
    }

   /** public void selectBusinessGroup(String businessGroup) {
            dropDownArrow().click();
        clickOptionBusinessGroup(businessGroup).click();
        LoggerUtil.LOGGER.info(businessGroup +" Selected Business Group");
    }**/

   public void selectBusinessGroup(String businessGroup) {
       dropDownArrow().click();

       page.locator("//div[contains(@class,'react-select__menu-list')]")
               .first()
               .waitFor(new Locator.WaitForOptions().setTimeout(10000));

       clickOptionBusinessGroup(businessGroup).click();

       LoggerUtil.LOGGER.info(businessGroup + " Selected Business Group");
   }
    public Locator create() {
        return new ResilientLocator(page, "Create")
                .byXPath("//button[@type='submit']")
                .byXPath("//button[text()='Create']")
                .resolve();
    }
}
