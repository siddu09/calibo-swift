package pages.PRO.MaturityAssesment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class OrganizationalMaturityPage {
    private final Page page;

    public OrganizationalMaturityPage(Page page) { this.page = page; }

    public Locator action(String label) {
        return new ResilientLocator(page, "Organization assessment action: " + label)
                .withFirstStrategyTimeout(15000)
                .custom("Exact action button", () -> page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//button[normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator respondentOption(String name) {
        return new ResilientLocator(page, "Organization assessment respondent")
                .withFirstStrategyTimeout(10000)
                .custom("Exact respondent in open menu", () -> page.locator("[class*='react-select__menu-list']:visible")
                        .getByText(name, new Locator.GetByTextOptions().setExact(true)))
                .byRole(AriaRole.OPTION, name)
                .resolve();
    }

    public Locator responseNavigationAction(String label, double timeoutMs) {
        return new ResilientLocator(page, "Organization response navigation: " + label)
                .withFirstStrategyTimeout(timeoutMs)
                .byRole(AriaRole.BUTTON, label)
                .byXPath("//button[normalize-space()='" + label
                        + "' or .//*[normalize-space(text())='" + label + "']]")
                .resolve();
    }

    public Locator responseChoice(String question, String answer, double timeoutMs) {
        return new ResilientLocator(page, "Organization response choice: " + question)
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Associated answer label in the question", () -> page.getByText(question,
                        new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=ancestor::*[.//input][1]")
                        .getByLabel(answer, new Locator.GetByLabelOptions().setExact(true)))
                .custom("Answer radio by accessible label", () -> page.getByRole(AriaRole.RADIO,
                        new Page.GetByRoleOptions().setName(answer).setExact(true)))
                .custom("Answer checkbox by accessible label", () -> page.getByRole(AriaRole.CHECKBOX,
                        new Page.GetByRoleOptions().setName(answer).setExact(true)))
                .byXPath("//label[normalize-space()='" + answer + "']//input")
                .resolve();
    }

    public Locator responseDetailsValue(String value, double timeoutMs) {
        return new ResilientLocator(page, "Organization response details value: " + value)
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Exact displayed value", () -> page.getByText(value,
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//*[normalize-space(text())='" + value + "']")
                .resolve();
    }

    public Locator assessmentCard(String name) {
        return new ResilientLocator(page, "Organization assessment: " + name)
                .withFirstStrategyTimeout(15000)
                .byXPath("//*[normalize-space(text())='" + name + "']/ancestor::div[contains(@class,'card')][1]")
                .custom("Assessment row", () -> page.getByRole(AriaRole.ROW).filter(new Locator.FilterOptions()
                        .setHas(page.getByText(name, new Page.GetByTextOptions().setExact(true)))))
                .resolve();
    }

    public Locator initiateAssessment(String name, String label) {
        Locator card = assessmentCard(name);
        return new ResilientLocator(page, "Initiate saved organization assessment: " + name)
                .withFirstStrategyTimeout(15000)
                .custom("Exact Initiate button in the named assessment", () -> card.getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .custom("Initiate action in the named assessment", () -> card.locator(
                        "xpath=.//button[normalize-space()='" + label + "']"))
                .resolve();
    }

    public Locator initiatedCount(String name, String label) {
        Locator card = assessmentCard(name);
        return new ResilientLocator(page, "Organization assessment initiated count")
                .custom("Initiated summary in the named assessment", () -> card.getByText(label))
                .custom("Summary containing the initiated label", () -> card.locator(
                        "xpath=.//*[contains(text(),'" + label + "')]"))
                .resolve();
    }

    public Locator assessmentActions(String name) {
        Locator card = assessmentCard(name);
        return new ResilientLocator(page, "Organization assessment actions: " + name)
                .custom("More actions in the named assessment", () -> card.getByText("more_horiz",
                        new Locator.GetByTextOptions().setExact(true)))
                .custom("More actions button in the named assessment", () -> card.locator(
                        "xpath=.//button[.//*[normalize-space()='more_horiz']]"))
                .custom("Assessment actions dropdown", () -> card.locator("[aria-haspopup='true']"))
                .resolve();
    }

    public Locator expandAssessment(String name) {
        Locator card = assessmentCard(name);
        return new ResilientLocator(page, "Expand organization assessment: " + name)
                .custom("Collapsed assessment arrow", () -> card.getByText("expand_more",
                        new Locator.GetByTextOptions().setExact(true)))
                .custom("Assessment header arrow", () -> card.locator(".card-header i.arrow"))
                .custom("Assessment expand button", () -> card.getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName(java.util.regex.Pattern.compile("(?i)expand"))))
                .resolve();
    }

    public Locator baselineAssessment(String name, String label) {
        Locator card = assessmentCard(name);
        return new ResilientLocator(page, "Baseline organization assessment: " + name)
                .custom("Baseline button in the named assessment", () -> card.getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .custom("Baseline action in the named assessment", () -> card.locator(
                        "xpath=.//button[normalize-space()='" + label + "']"))
                .resolve();
    }

    public Locator responseQuestion(java.util.List<String> questions, double timeoutMs) {
        return new ResilientLocator(page, "Visible organization response question")
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Exact configured question", () -> {
                    Locator question = page.getByText(questions.get(0), new Page.GetByTextOptions().setExact(true));
                    for (String text : questions.subList(1, questions.size())) {
                        question = question.or(page.getByText(text, new Page.GetByTextOptions().setExact(true)));
                    }
                    return question.filter(new Locator.FilterOptions().setVisible(true));
                })
                .byXPath("//*[self::h4 or self::h5][" + questions.stream()
                        .map(question -> "normalize-space()='" + question + "'")
                        .collect(java.util.stream.Collectors.joining(" or ")) + "]")
                .resolve();
    }

    public Locator responseAnswer(String question, String answer, double timeoutMs) {
        return new ResilientLocator(page, "Organization response answer: " + question)
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Checkbox label in the question", () -> page.getByText(question,
                        new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=ancestor::*[.//input[@type='checkbox']][1]")
                        .getByLabel(answer, new Locator.GetByLabelOptions().setExact(true)))
                .byXPath("//h4[normalize-space()='" + question
                        + "']/following-sibling::div//label[normalize-space()='" + answer
                        + "']//input[@type='checkbox']")
                .custom("Answer checkbox by accessible label", () -> page.getByRole(AriaRole.CHECKBOX,
                        new Page.GetByRoleOptions().setName(answer).setExact(true)))
                .resolve();
    }
}
