package pages.PRO.Product;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;


public class ProductDependencyPage {
    private final Page page;

    public ProductDependencyPage(Page page) {
        this.page = page;
    }

    public Locator notificationMessage(String message) {
        return new ResilientLocator(page, "Dependent association notification")
                .custom("Exact notification message", () -> {
                    Locator notification = page.getByRole(AriaRole.CELL)
                            .filter(new Locator.FilterOptions().setHasText(message));
                    notification.waitFor();
                    return notification;
                })
                .byXPath("//td[normalize-space()='" + message + "']")
                .resolve();
    }

    public Locator markAsRead(String message, String label) {
        return notificationAction(message, label, "check");
    }

    public Locator deleteNotification(String message, String label) {
        return notificationAction(message, label, "clear");
    }

    private Locator notificationAction(String message, String label, String icon) {
        return new ResilientLocator(page, "Notification action: " + label)
                .custom("Action tooltip in matching notification", () -> page.getByRole(AriaRole.ROW)
                        .filter(new Locator.FilterOptions().setHasText(message))
                        .getByTitle(label, new Locator.GetByTitleOptions().setExact(true)).getByRole(AriaRole.BUTTON))
                .byXPath("//tr[contains(.,'" + message + "')]//button[.//i[normalize-space()='" + icon + "']]")
                .resolve();
    }

    public Locator notificationBadge(String label) {
        return new ResilientLocator(page, "Unread dependency notifications")
                .byCss("button[data-rb-event-key='" + label + "'] .badge-bg-primary")
                .byXPath("//button[@data-rb-event-key='" + label + "']//span")
                .resolve();
    }

    public Locator relationshipRow(String productName) {
        return new ResilientLocator(page, "Dependency relationship: " + productName)
                .custom("Relationship table row", () -> {
                    Locator row = page.getByRole(AriaRole.ROW)
                            .filter(new Locator.FilterOptions().setHasText(productName));
                    row.waitFor();
                    return row;
                })
                .byXPath("//tr[contains(.,'" + productName + "')]")
                .resolve();
    }

    public Locator sidebarOption(String label) {
        return new ResilientLocator(page, "Dependency sidebar: " + label)
                .byCss("button[data-rb-event-key='" + label + "']")
                .custom("Exact sidebar label", () -> page.getByText(label, new Page.GetByTextOptions().setExact(true)))
                .byXPath("//*[self::a or self::button or self::li][contains(normalize-space(),'" + label + "')]")
                .resolve();
    }

    public Locator tab(String label) {
        return new ResilientLocator(page, label + " tab")
                .byRole(AriaRole.TAB, label)
                .byXPath("//*[self::a or self::button][normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator dropdown(String placeholder) {
        return new ResilientLocator(page, placeholder)
                .custom("Exact dropdown placeholder", () -> page.getByText(placeholder,
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//div[contains(@class,'react-select__placeholder')][normalize-space()='" + placeholder + "']")
                .resolve();
    }

    public Locator dropdownSearch(String placeholder) {
        return new ResilientLocator(page, "Dependency dropdown search: " + placeholder)
                .byXPath("//div[contains(@class,'react-select__control')][.//*[normalize-space()='" + placeholder + "']]//input")
                .custom("Input beside dropdown placeholder", () -> page.getByText(placeholder,
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=..").locator("input"))
                .resolve();
    }

    public Locator addButton(String label) {
        return new ResilientLocator(page, "Add product dependency")
                .byRole(AriaRole.BUTTON, label)
                .byXPath("//button[normalize-space()='" + label + "']")
                .resolve();
    }

    // -------------------------------------------------------------------------
    // Add Dependency
    // -------------------------------------------------------------------------

    public void selectDropDown(
            String dropDownName,
            String dropDownValue) {

        dropdown(dropDownName).click();
        dropdownSearch(dropDownName).fill(dropDownValue);
        selectDropdownValue(dropDownValue).click();
        LoggerUtil.LOGGER.info(
                dropDownValue +
                        " Selected DropDown Value"
        );
    }

    public Locator selectDropdownValue(String dropDown) {

        return new ResilientLocator(page, "Dependency option: " + dropDown)
                .custom("Exact dropdown option", () -> {
                    Locator option = page.locator("[class*='react-select__menu-list']")
                            .getByText(dropDown, new Locator.GetByTextOptions().setExact(true));
                    option.waitFor();
                    return option;
                })
                .byXPath("//div[contains(@class,'react-select__option')][normalize-space()='" + dropDown + "']")
                .resolve();
    }
}
