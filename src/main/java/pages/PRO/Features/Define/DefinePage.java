package pages.PRO.Features.Define;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

/**
 * Page Object for: DefinePage
 *
 * Source URL:
 * https://accelerate-qa.calibo.com/projects
 *
 * Strategy:
 * Fluent ResilientLocator chain where applicable.
 *
 * Important:
 * Existing dropdown implementation is preserved because it is
 * application-specific and was already working.
 */
public class DefinePage {

    private final Page page;

    public DefinePage(Page page) {
        this.page = page;
    }

    // -------------------------------------------------------------------------
    // Common Button
    // -------------------------------------------------------------------------

    public Locator button() {
        return new ResilientLocator(page, "button")
                .byCss("button")
                .byXPath(
                        "/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]" +
                                "/div[1]/div[1]/div[3]/div[2]/div[1]/button[1]"
                )
                .resolve();
    }

    // -------------------------------------------------------------------------
    // Business Requirement
    // -------------------------------------------------------------------------

    public Locator addNewBusinessRequirement(
            String businessRequirement) {

        return new ResilientLocator(
                page,
                "New BR"
        )
                .byText(businessRequirement)
                .byXPath(
                        "//button[text()='" +
                                businessRequirement +
                                "']"
                )
                .resolve();
    }

    // -------------------------------------------------------------------------
    // Dropdown
    // -------------------------------------------------------------------------

    /**
     * IMPORTANT:
     * Keep this locator as-is because it matches the existing
     * application behavior.
     */
    public Locator selectDropdownValue(String dropDown) {

        return page.locator(
                "//label[text()='" + dropDown + "']"
        );
    }

    public Locator dropDownArrow() {

        return new ResilientLocator(
                page,
                "dropDownArrow"
        )
                .byXPath("//i[text()='arrow_drop_down']")
                .resolve();
    }

    public void selectDropDown(
            String dropDownName,
            String dropDownValue) {

        page.locator(
                        "label:text('" + dropDownName + "')"
                )
                .locator(
                        "xpath=following::div" +
                                "[contains(@class,'react-select__control')][1]"
                )
                .click();

        selectDropdownValue(dropDownValue).click();

        LoggerUtil.LOGGER.info(
                dropDownValue +
                        " Selected DropDown Value"
        );
    }

    // -------------------------------------------------------------------------
    // Form Fields
    // -------------------------------------------------------------------------

    public Locator title() {

        return new ResilientLocator(
                page,
                "Name"
        )
                .byCss("input[label='Title']")
                .byXPath("//*[@label='Title']")
                .resolve();
    }

    public Locator description() {

        return new ResilientLocator(
                page,
                "Description"
        )
                .byCss(
                        "div[contenteditable='true'][role='textbox']"
                )
                .byXPath(
                        "//div[@role='textbox']"
                )
                .resolve();
    }

    public Locator addUserName() {

        return new ResilientLocator(
                page,
                "Name"
        )
                .byCss("input[name='userName']")
                .byXPath("//*[@name='userName']")
                .resolve();
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    public Locator create() {

        return new ResilientLocator(
                page,
                "Create"
        )
                .byXPath("//button[text()='Create']")
                .resolve();
    }

    public Locator selectBusinessRequirement(String BRName) {
        return new ResilientLocator(page, "Select BusinessRequirement")
                .byText(BRName)
                .resolve();
    }
}