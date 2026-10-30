package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class DesignAttachmentsPage {
    public Locator savedLinks() {
        return new ResilientLocator(page, "Saved Design link content")
                .byCss("#menu .sheet-body")
                .byXPath("//*[@id='menu']//*[contains(@class,'sheet-body')]")
                .custom("Content of the saved Design slide", () -> page.locator("#menu").locator(".sheet-body"))
                .resolve().locator("a[href^='https://'], a[href^='http://']");
    }

    private final Page page;
    private static final String ACTIVE_PANE = ".design-define-tabular-block > .tab-pane.active";

    public DesignAttachmentsPage(Page page) {
        this.page = page;
    }

    public Locator title() {
        return new ResilientLocator(page, "Active design attachment title")
                .byCss(ACTIVE_PANE + " input[name='title']")
                .byXPath("//div[contains(@class,'design-define-tabular-block')]/div[contains(concat(' ',normalize-space(@class),' '),' active ')]//input[@name='title']")
                .custom("Attachment title textbox", () -> page.locator(ACTIVE_PANE)
                        .getByRole(AriaRole.TEXTBOX).and(page.locator("[name='title']")))
                .resolve();
    }

    public Locator linkAddress() {
        return new ResilientLocator(page, "Design attachment URL")
                .byCss(ACTIVE_PANE + " input[name='uri']")
                .byXPath("//input[@name='uri' and @type='text']")
                .custom("Attachment URL textbox", () -> page.locator(ACTIVE_PANE)
                        .getByRole(AriaRole.TEXTBOX).and(page.locator("[name='uri']")))
                .resolve();
    }

    public Locator addLink() {
        return new ResilientLocator(page, "Add design link")
                .byCss(ACTIVE_PANE + " .col-2 > button")
                .byXPath("//input[@name='uri']/ancestor::div[contains(concat(' ',normalize-space(@class),' '),' row ')][1]//button")
                .custom("Link row action", () -> page.locator(ACTIVE_PANE + " .col-2").getByRole(AriaRole.BUTTON))
                .resolve();
    }

    public Locator filesTab() {
        return new ResilientLocator(page, "Design Files tab")
                .byCss(".tabbable-line > ul.nav-tabs > li:nth-child(2) > a[data-toggle='tab']")
                .byXPath("//div[contains(@class,'tabbable-line')]/ul/li[2]/a[@data-toggle='tab']")
                .byCss(".tabbable-line > .nav-tabs > li:last-child > a[data-toggle='tab']")
                .resolve();
    }

    public Locator addFile() {
        return new ResilientLocator(page, "Add design file")
                .byCss(ACTIVE_PANE + " > .d-flex.flex-row-reverse > button")
                .byXPath("//input[@id='contained-button-file']/ancestor::div[contains(@class,'tab-pane')][1]//div[contains(@class,'flex-row-reverse')]/button")
                .custom("File pane action", () -> page.locator(ACTIVE_PANE + " > .flex-row-reverse")
                        .getByRole(AriaRole.BUTTON))
                .resolve();
    }

    public Locator stagedAttachment(String title) {
        return new ResilientLocator(page, "Added design attachment: " + title)
                .withFirstStrategyTimeout(30000)
                .custom("Added attachment title", () -> page.locator(".design-define-tabular-block")
                        .getByText(title, new Locator.GetByTextOptions().setExact(true)))
                .byCss(".design-define-tabular-block [title='" + title + "']")
                .byXPath("//*[contains(@class,'design-define-tabular-block')]//*[normalize-space(text())='" + title + "']")
                .resolve();
    }

    public Locator savedLink(String url) {
        return new ResilientLocator(page, "Saved design attachment link")
                .withFirstStrategyTimeout(30000)
                .byCss("#menu a[href='" + url + "']")
                .byXPath("//*[@id='menu']//a[@href='" + url + "']")
                .custom("Design link by href", () -> page.locator("#menu").getByRole(AriaRole.LINK)
                        .and(page.locator("[href='" + url + "']")))
                .resolve();
    }

    public Locator linkIcon(String url) {
        return new ResilientLocator(page, "Open saved design link")
                .custom("Link icon beside the saved URL", () -> savedLink(url)
                        .locator("xpath=following-sibling::*[local-name()='svg']"))
                .byCss("#menu a[href='" + url + "'] + svg")
                .byXPath("//*[@id='menu']//a[@href='" + url + "']/following-sibling::*[local-name()='svg']")
                .resolve();
    }

    public Locator downloadFile(String title) {
        return new ResilientLocator(page, "Download saved design attachment")
                .withFirstStrategyTimeout(30000)
                .custom("Download icon beside the saved file title", () -> page.locator("#menu")
                        .getByText(title, new Locator.GetByTextOptions().setExact(true))
                        .locator("xpath=following-sibling::*[local-name()='svg']"))
                .byXPath("//*[@id='menu']//h5[normalize-space()='" + title + "']/following-sibling::*[local-name()='svg']")
                .byCss("#menu h5.text-primary + svg:has(path[d='M19 9h-4V3H9v6H5l7 7 7-7zM5 18v2h14v-2H5z'])")
                .resolve();
    }
}
