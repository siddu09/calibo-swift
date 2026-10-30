package pages.PRO.Features.Define;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

/** Additional Business Requirement controls; existing Define page methods are reused unchanged. */
public class ResilientBusinessRequirementPage extends DefinePage {
    private final Page page;

    public ResilientBusinessRequirementPage(Page page) {
        super(page);
        this.page = page;
    }

    public Locator tab(String label) {
        return new ResilientLocator(page, "Define tab: " + label)
                .withFirstStrategyTimeout(30000)
                .byRole(AriaRole.TAB, label)
                .byXPath("//a[normalize-space()='" + label + "']")
                .custom("Define navigation tab", () -> page.locator("[data-toggle='tab']")
                        .filter(new Locator.FilterOptions().setHasText(label)))
                .resolve();
    }

    public Locator action(String label) {
        return new ResilientLocator(page, "Business Requirement action: " + label)
                .byRole(AriaRole.BUTTON, label)
                .byCss("button[text='" + label + "']")
                .byXPath("//button[normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator field(String name) {
        String selector = switch (name) {
            case "comments" -> ".usecase-section textarea";
            case "value" -> ".usecase-section input[inputmode='numeric']";
            case "attributeName" -> ".usecase-section input[placeholder='Field Name']";
            case "attributeValue" -> ".usecase-section input[placeholder='Value']";
            default -> throw new IllegalArgumentException("Unknown Business Requirement field: " + name);
        };
        return new ResilientLocator(page, "Business Requirement field: " + name)
                .byCss(selector)
                .byCss("input[name='" + name + "'], textarea[name='" + name + "']")
                .custom("Requirement textbox by structure", () -> page.getByRole(AriaRole.TEXTBOX)
                        .and(page.locator(selector)))
                .byXPath(switch (name) {
                    case "comments" -> "//div[contains(@class,'usecase-section')]//textarea[@rows='3']";
                    case "value" -> "//div[contains(@class,'usecase-section')]//input[@inputmode='numeric']";
                    case "attributeName" -> "//div[contains(@class,'usecase-section')]//div[@class='row']/div[1]//input[@placeholder]";
                    default -> "//div[contains(@class,'usecase-section')]//div[@class='row']/div[2]//input[@placeholder]";
                })
                .resolve();
    }

    public Locator addAttribute() {
        return new ResilientLocator(page, "Add Business Requirement attribute")
                .byCss(".usecase-section .row:has(input[placeholder]) .col-2 > button")
                .byXPath("//input[@placeholder='Field Name']/ancestor::div[contains(concat(' ',normalize-space(@class),' '),' row ')][1]//button")
                .byRole(AriaRole.BUTTON, "Add")
                .resolve();
    }

    public Locator attributeValue(String value) {
        return new ResilientLocator(page, "Added requirement attribute")
                .custom("Added attribute value", () -> page.locator("#app-layout-container")
                        .getByText(value, new Locator.GetByTextOptions().setExact(true)))
                .byCss("#app-layout-container [title='" + value + "']")
                .byXPath("//*[@id='app-layout-container']//*[normalize-space(text())='" + value + "']")
                .resolve();
    }

    public Locator workflows(String label) {
        return new ResilientLocator(page, "Define Workflows view")
                .withFirstStrategyTimeout(30000)
                .byRole(AriaRole.HEADING, label)
                .byRole(AriaRole.TAB, label)
                .custom("Workflows view label", () -> page.locator("#app-layout-container")
                        .getByText(label, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator designWorkflow(String phase) {
        return new ResilientLocator(page, "Design workflow")
                .withFirstStrategyTimeout(30000)
                .custom("Design workflow heading", () -> page.locator("#app-layout-container")
                        .getByRole(AriaRole.HEADING, new Locator.GetByRoleOptions().setName(phase).setExact(true)))
                .byXPath("//*[@id='app-layout-container']//h4[normalize-space()='" + phase + "']")
                .byXPath("//*[@id='app-layout-container']//h3[normalize-space()='" + phase + "']")
                .resolve();
    }
}
