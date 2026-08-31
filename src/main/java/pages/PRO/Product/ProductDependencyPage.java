package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

public class ProductDependencyPage {
    private final Page page;

    public ProductDependencyPage(Page page) {
        this.page = page;
    }

    // -------------------------------------------------------------------------
    // Add Dependency
    // -------------------------------------------------------------------------

    public void selectDropDown(
            String dropDownName,
            String dropDownValue) {

        page.getByText(dropDownName)
                .click();
        selectDropdownValue(dropDownValue).click();
        LoggerUtil.LOGGER.info(
                dropDownValue +
                        " Selected DropDown Value"
        );
    }

    public Locator selectDropdownValue(String dropDown) {

        return page.locator(
                "//label[text()='" + dropDown + "']"
        );
    }
}
