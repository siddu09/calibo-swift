package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.LocatorInfo;
import selfhealingHandler.ResilientLocator;

public class LandingPage {
    private final Page page;

//    private final LocatorInfo navBar =
//            new LocatorInfo(
//                    "//div[@class='logo-wrapper jss18']",
//                    ".tlogo-wrapper jss18",
//                    ".tenant-admin-nav jss11"
//            );

    //todo add secondary and tertiary locators
//    private final LocatorInfo options =
//            new LocatorInfo(
//                    "//span/label[text()='Product Portfolios']",
//                    ".tlogo-wrapper jss18",
//                    ".tenant-admin-nav jss11"
//            );

    //todo add secondary and tertiary locators
    private final LocatorInfo headers =
            new LocatorInfo(
                    "//h1[text()='Product Portfolios']",
                    ".tlogo-wrapper jss18",
                    ".tenant-admin-nav jss11"
            );

    public LandingPage(Page page) {
        this.page = page;
    }

//    public void hoverOnNavigationBar()
//    {
//        SelfHealingLocator
//                .findElement(page, navBar)
//                .hover();
//        LoggerUtil.LOGGER.info("Hover on Navigation Bar");
//    }

    public Locator clickOnOptions(String option) {
        return new ResilientLocator(page, "Name")
                .byXPath("//span/label[text()='" + option + "']")
                .resolve();
    }

    public Locator hoverOnNavigationBar() {
        return new ResilientLocator(page, "Name")
                .byXPath("//div[@class='logo-wrapper jss18']")
                .resolve();
    }

    public Locator isLandedOnPage(String pageName) {
        return new ResilientLocator(page, "Name")
                .byXPath("//span/label[text()='" + pageName + "']")
                .resolve();
    }

}
