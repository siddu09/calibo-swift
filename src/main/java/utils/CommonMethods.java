package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import selfhealingHandler.ResilientLocator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonMethods {

//    private final Page page;
//
//    public CommonMethods(Page page) {
//        this.page = page;
//    }

    public static boolean pageHeader(Page page, String headerText) {
        Locator loc = page.locator("//h1[text()='" + headerText + "']");
        return loc.isVisible();
    }

    public static boolean sectionHeader(Page page, String headerText) {
        Locator loc = page.locator("//h5[text()='" + headerText + "']");
        return loc.isVisible();
    }

    public static void waitForLoaderToDisappear(Page page) {

        // Layer 1: Original spinner
        Locator loader = page.locator("//div[@class='spinner-bg']");

        if (isElementPresent(loader)) {
            loader.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN));
        }

        // Layer 2: Additional loaders/spinners
        try {
            Locator otherLoaders = page.locator(
                    "//*[contains(@class,'MuiCircularProgress')]"
                            + " | //*[contains(@class,'loading')]"
                            + " | //*[contains(@class,'loader')]");

            if (otherLoaders.count() > 0) {
                otherLoaders.first().waitFor(
                        new Locator.WaitForOptions()
                                .setState(WaitForSelectorState.HIDDEN)
                                .setTimeout(20000));
            }
        } catch (Exception ignored) {
            // silently continue
        }
    }
    public static void neutralizeKnownOverlays(Page page) {

        page.evaluate("""
() => {

   const widget =
       document.getElementById('jsd-widget');

   if(widget){

      const iframe =
          widget.closest('iframe');

      if(iframe){
         iframe.style.display='none';
      }

      widget.style.display='none';
   }

}
""");

    }


    public static boolean isElementPresent(Locator locator) {
        return locator.count() > 0;
    }

    public static boolean tabHeader(Page page, String tabText) {
        Locator loc = page.locator("//span[text()='" + tabText + "']");
        return loc.isVisible();
    }

    public static void clickOnTab(Page page, String tab) {
        Locator loc = page.locator("//span[text()='" + tab + "']");
        loc.click();
        LoggerUtil.LOGGER.info(tab + " clicked");
    }

    public static Locator search(Page page, String searchText) {
        return new ResilientLocator(page, searchText)
                .byXPath("//input[@placeholder='Search']")
                .byId("search")
                .resolve();
    }

    public static Locator selectSearchedItem(Page page, String item) {
        return new ResilientLocator(page, item)
                .byXPath("//h2[text()='" + item + "']")
                .resolve();
    }

    public static Locator clickButton(Page page, String buttonText)
    {
        return new ResilientLocator(page, buttonText)
                .byXPath("//button[text()='"+buttonText+"']")
                .byXPath("//button[normalize-space()='"+buttonText+"']")
                .byXPath("//button[contains(.,'"+buttonText+"')]")
                .byXPath("//a[normalize-space()='"+buttonText+"']")
                .byCss("button.btn-primary")
                .byRole(com.microsoft.playwright.options.AriaRole.BUTTON, buttonText)
                .byText(buttonText)
                .resolve();
    }


    public static String generateUniqueTitle(String prefix) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

        return prefix + "_" +
                LocalDateTime.now().format(formatter);
    }

    public static void waitForElementToBeVisible(Page page, String elementText) {
        new ResilientLocator(page, elementText)
                .byXPath("//button[text()='"+elementText+"']")
                .byXPath("//h4[text()='"+elementText+"']")
                .byXPath("//table//th[text()='"+elementText+"']")
                .byXPath("//h3[text()='"+elementText+"']")
                .byXPath("//h2[contains(text(),'"+elementText+"')]")
                .resolve().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public static Locator verifyValidationButton(Page page)
    {
        return new ResilientLocator(page, "Verify Validation Button")
                .byXPath("//button[text()='Validating']")
                .resolve();
    }
    public static void verifyValidationIsCompleted(Page page) {
        if(CommonMethods.isElementPresent(verifyValidationButton(page)))
            verifyValidationButton(page).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public static Locator clickStageInPanel(Page page, String stageName)
    {
        return new ResilientLocator(page, stageName + " Stage")
                .byXPath("//div[contains(@class,'stage')]//text()[contains(.,'" + stageName + "')]/..")
                .byXPath("//div[contains(text(),'" + stageName + "')]")
                .byText(stageName)
                .resolve();
    }

    public static Locator clickFeatureByName(Page page, String featureName)
    {
        return new ResilientLocator(page, featureName)
                .byXPath("//h2[text()='" + featureName + "']")
                .byXPath("//h2[contains(text(),'" + featureName + "')]")
                .byXPath("//div[@class='d-flex align-items-center mb-1']//h2[text()='" + featureName + "']")
                .resolve();
    }
}
