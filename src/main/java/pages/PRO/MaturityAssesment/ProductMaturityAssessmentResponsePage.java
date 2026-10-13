package pages.PRO.MaturityAssesment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class ProductMaturityAssessmentResponsePage {
    private final Page page;

    public ProductMaturityAssessmentResponsePage(Page page) {
        this.page = page;
    }

    public Locator savedResponseAnswer(String question, String value, double timeoutMs) {
        return new ResilientLocator(page, "Saved response checkbox for question: " + question)
                .withFirstStrategyTimeout(timeoutMs)
                .byCss("input[type='checkbox'][name='" + question + "'][value='" + value + "']")
                .byXPath("//h4[normalize-space()='" + question
                        + "']/following-sibling::div//input[@type='checkbox' and @value='" + value + "']")
                .resolve();
    }

    private Locator questionGroup(String question, double timeoutMs) {
        return new ResilientLocator(page, "Assessment response question")
                .withFirstStrategyTimeout(timeoutMs)
                .byXPath("//*[normalize-space(text())='" + question
                        + "']/ancestor::*[.//input[@type='radio']][1]")
                .byRole(AriaRole.GROUP, question)
                .resolve();
    }

    public Locator answer(String question, String value, double timeoutMs) {
        return new ResilientLocator(page, "Answer for assessment question: " + question)
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Answer radio in question", () -> questionGroup(question, timeoutMs)
                        .getByRole(AriaRole.RADIO, new Locator.GetByRoleOptions().setName(value).setExact(true)))
                .custom("Answer by associated label", () -> questionGroup(question, timeoutMs)
                        .getByLabel(value, new Locator.GetByLabelOptions().setExact(true)))
                .resolve();
    }

    private Locator responseEntry(String name, String resumeLabel, double timeoutMs) {
        return new ResilientLocator(page, "Saved respondent assessment: " + name)
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Assessment response table row", () -> page.getByRole(AriaRole.ROW)
                        .filter(new Locator.FilterOptions().setHasText(name)))
                .byXPath("//*[normalize-space(text())='" + name
                        + "']/ancestor::*[.//*[self::button or self::a][normalize-space()='" + resumeLabel + "']][1]")
                .resolve();
    }

    public Locator responseStatus(String name, String status, String resumeLabel, double timeoutMs) {
        return new ResilientLocator(page, "Status for saved respondent assessment")
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Exact status in assessment entry", () -> responseEntry(name, resumeLabel, timeoutMs)
                        .getByText(status, new Locator.GetByTextOptions().setExact(true)))
                .custom("Status cell in assessment entry", () -> responseEntry(name, resumeLabel, timeoutMs)
                        .getByRole(AriaRole.CELL, new Locator.GetByRoleOptions().setName(status).setExact(true)))
                .resolve();
    }

    public Locator completionProgress(String name, String percentage, String viewDetailsLabel, double timeoutMs) {
        return new ResilientLocator(page, "Completion percentage for submitted assessment")
                .custom("Exact completion percentage in assessment entry", () -> responseEntry(name, viewDetailsLabel, timeoutMs)
                        .getByText(percentage, new Locator.GetByTextOptions().setExact(true)))
                .custom("Completion percentage table cell", () -> responseEntry(name, viewDetailsLabel, timeoutMs)
                        .getByRole(AriaRole.CELL, new Locator.GetByRoleOptions().setName(percentage).setExact(true)))
                .resolve();
    }

    public Locator viewDetails(String name, String label, double timeoutMs) {
        return new ResilientLocator(page, "View details of submitted assessment")
                .custom("View Details button in assessment entry", () -> responseEntry(name, label, timeoutMs)
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .custom("View Details link in assessment entry", () -> responseEntry(name, label, timeoutMs)
                        .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .resolve();
    }

    public Locator submittedAnswer(String question, String value, double timeoutMs) {
        return new ResilientLocator(page, "Submitted answer for assessment question: " + question)
                .withFirstStrategyTimeout(timeoutMs)
                .byXPath("//*[normalize-space(text())='" + question
                        + "']/ancestor::*[.//*[normalize-space(text())='" + value
                        + "'] and not(.//input[@type='radio'] or .//*[@role='radio'])][1]"
                        + "//*[normalize-space(text())='" + value + "']")
                .custom("Checked answer in submitted question", () -> questionGroup(question, timeoutMs)
                        .getByRole(AriaRole.RADIO, new Locator.GetByRoleOptions()
                                .setName(value).setExact(true).setChecked(true)))
                .resolve();
    }

    public Locator resume(String name, String label, double timeoutMs) {
        return new ResilientLocator(page, "Resume saved respondent assessment")
                .withFirstStrategyTimeout(timeoutMs)
                .custom("Resume button in assessment entry", () -> responseEntry(name, label, timeoutMs)
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .custom("Resume link in assessment entry", () -> responseEntry(name, label, timeoutMs)
                        .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .resolve();
    }
}
