package pages.PRO.MaturityAssesment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class MyAssessmentsPage {
    private final Page page;

    public MyAssessmentsPage(Page page) {
        this.page = page;
    }

    public Locator menuOption(String label, double timeoutMs) {
        return new ResilientLocator(page, "My Assessments profile menu option")
                .withFirstStrategyTimeout(timeoutMs)
                .byRole(AriaRole.LINK, label)
                .byCss(".user-logout-menu a[href='/my-assessment']")
                .byRole(AriaRole.MENUITEM, label)
                .resolve();
    }

    public Locator searchByName(String placeholder, double timeoutMs) {
        return new ResilientLocator(page, "My Assessments search by name")
                .withFirstStrategyTimeout(timeoutMs)
                .byPlaceholder(placeholder)
                .byRole(AriaRole.TEXTBOX, placeholder)
                .byCss("input[placeholder='" + placeholder + "']")
                .resolve();
    }

    public Locator assessment(String name, double timeoutMs) {
        return new ResilientLocator(page, "Assigned assessment: " + name)
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Exact assessment name", () -> page.getByText(name,
                        new Page.GetByTextOptions().setExact(true)))
                .byRole(AriaRole.LINK, name)
                .byXPath("//*[self::a or self::h2 or self::h3 or self::h4 or self::p][normalize-space()='" + name + "']")
                .resolve();
    }

    public Locator startAssessment(String name, String label, double timeoutMs) {
        return new ResilientLocator(page, "Start the matching assigned assessment")
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Start button in the named assessment row", () -> page.getByRole(AriaRole.ROW)
                        .filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.CELL,
                                new Page.GetByRoleOptions().setName(name).setExact(true))))
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//tr[td[normalize-space()='" + name + "']]//button[normalize-space()='" + label + "']")
                .resolve();
    }
}
