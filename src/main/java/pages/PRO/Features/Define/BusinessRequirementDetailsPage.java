package pages.PRO.Features.Define;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;

public class BusinessRequirementDetailsPage {
    private final Page page;

    public BusinessRequirementDetailsPage(Page page) {
        this.page = page;
    }

    public Locator convertFeedbackButton(String label) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(label).setExact(true));
    }

    public Locator fetchBusinessRequirementTitle() {

        return new ResilientLocator(
                page,
                "Business Requirement Title")
                .custom(
                        "Business requirement title",
                        () -> page.locator(
                                        "span.business-req-title")
                                .locator(
                                        "xpath=following-sibling::div[contains(@class,'workflow-status')]"
                                )
                )
                .resolve();
    }

    public Locator userFeedbackTitle(String title) {
        return new ResilientLocator(page, "User feedback title")
                .custom("Exact generated feedback title", () -> page.getByText(title,
                        new Page.GetByTextOptions().setExact(true)))
                .byXPath("//*[self::h1 or self::h2 or self::h3 or self::p][normalize-space()='" + title + "']")
                .byCss("[data-testid*='business-requirement-name']")
                .custom("Title in feedback details content", () -> page.locator("main, [role='main'], #app-layout-container")
                        .getByText(title, new Locator.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator userFeedbackDescription() {
        return new ResilientLocator(page, "User feedback description")
                .byXPath("//label[normalize-space()='Description']/following-sibling::div[1]//p")
                .byCss("[data-testid*='description-value']")
                .byCss("label + div [class*='description']")
                .custom("Description beside exact label", () -> page.getByText("Description",
                                new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=following-sibling::div[1]"))
                .resolve();
    }

    public Locator fetchPriority() {

        return new ResilientLocator(
                page,
                "Priority")
                .custom(
                        "Priority",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Priority")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator fetchSource() {

        return new ResilientLocator(
                page,
                "Source")
                .custom(
                        "Source",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Source")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator fetchImpact() {

        return new ResilientLocator(
                page,
                "Impact")
                .custom(
                        "Impact",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Impact")
                                )
                                .locator(
                                        "xpath=following-sibling::p[1]"
                                )
                )
                .resolve();
    }

    public Locator fetchDescription() {

        return new ResilientLocator(
                page,
                "Description")
                .custom(
                        "Description",
                        () -> page.locator("label")
                                .filter(
                                        new Locator.FilterOptions()
                                                .setHasText("Description")
                                )
                                .locator(
                                        "xpath=following-sibling::div//p"
                                )
                )
                .resolve();
    }

    public Locator confluencePageLink(String pageTitle) {
        return new ResilientLocator(page, "Linked Confluence page")
                .byCss("a[data-testid*='confluence-page']")
                .byCss("a[href*='/wiki/']")
                .byXPath("//label[contains(normalize-space(),'Confluence')]/following::a[1]")
                .byXPath("//a[contains(@href,'confluence') and normalize-space()='" + pageTitle + "']")
                .custom("Exact linked Confluence page title", () -> page.getByText(pageTitle,
                        new Page.GetByTextOptions().setExact(true)).locator("xpath=ancestor-or-self::a[1]"))
                .resolve();
    }

    public Locator commentInput(String placeholder) {
        return new ResilientLocator(page, "User Feedback comment input")
                .byCss("textarea[data-testid*='comment']")
                .byCss("textarea[name='comment']")
                .byCss("textarea[aria-label*='comment']")
                .byPlaceholder(placeholder)
                .byXPath("//textarea[contains(@placeholder,'comment')]")
                .resolve();
    }

    public Locator commentButton(String label) {
        return new ResilientLocator(page, "Save User Feedback comment")
                .byCss("button[data-testid*='add-comment']")
                .byCss("button[type='submit'][name*='comment']")
                .byXPath("//textarea[contains(@placeholder,'comment')]/following::button[@type='submit'][1]")
                .byRole(AriaRole.BUTTON, label)
                .resolve();
    }

    public Locator savedComment(String comment) {
        return new ResilientLocator(page, "Saved User Feedback comment")
                .byCss("[data-testid*='comment-item']")
                .byCss("[class*='comment-item'], [class*='comment-content']")
                .byXPath("//*[contains(@class,'comment') and normalize-space()='" + comment + "']")
                .custom("Exact saved comment", () -> page.getByText(comment,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator reviewsSection(String label) {
        return new ResilientLocator(page, "User Feedback Reviews section")
                .byCss("[data-testid*='reviews-tab']")
                .byRole(AriaRole.TAB, label)
                .byCss("a[href*='review']")
                .byCss("button[name*='review']")
                .byXPath("//*[@role='tab' and contains(@data-testid,'review')]")
                .byText(label)
                .resolve();
    }

    public Locator reviewCommentInput(String placeholder) {
        return new ResilientLocator(page, "Review comment input")
                .byCss("textarea[data-testid*='review-comment']")
                .byCss("textarea[name*='review']")
                .byCss("[data-testid*='review-comment'][contenteditable='true']")
                .byCss("[contenteditable='true'][data-placeholder*='Comments']")
                .byCss("[contenteditable='true'][aria-label*='Comments']")
                .byCss("textarea[aria-label*='Comments']")
                .byPlaceholder(placeholder)
                .byXPath("//*[self::textarea or @contenteditable='true'][contains(@placeholder,'Comments') or contains(@data-placeholder,'Comments')]")
                .custom("Editable control beside Your Comments label", () -> page.getByText(placeholder,
                                new Page.GetByTextOptions().setExact(true))
                        .locator("xpath=following::*[@contenteditable='true' or self::textarea][1]"))
                .resolve();
    }

    public Locator reviewSubmitButton(String label) {
        return new ResilientLocator(page, "Submit User Feedback review")
                .byCss("button[data-testid*='submit-review']")
                .byCss("[data-testid*='reviews'] button[type='submit']")
                .byXPath("//textarea[contains(@placeholder,'Comments')]/following::button[@type='submit'][1]")
                .byRole(AriaRole.BUTTON, label)
                .resolve();
    }

    public Locator savedReview(String reviewComment) {
        return new ResilientLocator(page, "Saved User Feedback review")
                .byCss("[data-testid*='review-item']")
                .byCss("[class*='review-item'], [class*='review-comment']")
                .byXPath("//*[contains(@class,'review') and normalize-space()='" + reviewComment + "']")
                .custom("Exact saved review comment", () -> page.getByText(reviewComment,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

}
