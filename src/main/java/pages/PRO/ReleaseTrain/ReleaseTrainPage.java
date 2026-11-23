package pages.PRO.ReleaseTrain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import selfhealingHandler.ResilientLocator;


public class ReleaseTrainPage {

    private final Page page;

    public ReleaseTrainPage(Page page) {
        this.page = page;
    }

    public Locator addNewReleaseTrain() {
        return new ResilientLocator(page, "Add New Release Train")
                .byText("New Release Train")
                .byXPath("//button[text()='New Release Train']")
                .resolve();
    }

    public Locator searchReleaseTrain() {
        return new ResilientLocator(page, "Search Release Train")
                .byPlaceholder("Search Release Trains")
                .byXPath("//input[@placeholder='Search Release Trains']")
                .resolve();
    }

    public Locator selectReleaseTrain(String releaseTrainName) {
        return new ResilientLocator(page, "Select Release Train")
                .custom("Release train heading containing the generated name", () -> {
                    Locator train = page.getByRole(AriaRole.HEADING)
                            .filter(new Locator.FilterOptions().setHasText(releaseTrainName));
                    train.waitFor();
                    return train;
                })
                .byXPath("//h2[normalize-space()='" + releaseTrainName + "']")
                .custom("Exact visible release train name", () -> page.getByText(releaseTrainName,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator addNewRelease(){
        return new ResilientLocator(page, "Add New Release")
                .byText("New Release")
                .byXPath("//button[text()='New Release']")
                .resolve();
    }

    public Locator savedTrainDescription(String name) {
        return new ResilientLocator(page, "Saved release train description: " + name)
                .custom("Description within named train card", () -> selectReleaseTrain(name)
                        .locator("xpath=ancestor::div[contains(concat(' ',normalize-space(@class),' '),' card ')][1]")
                        .locator(".project-description"))
                .byXPath("//h2[contains(normalize-space(),'" + name
                        + "')]/following-sibling::p[contains(@class,'project-description')]")
                .resolve();
    }

    public Locator savedTrainName() {
        return new ResilientLocator(page, "Saved release train title")
                .byCss("h1 .page-title-h1")
                .byXPath("//h1//div[contains(@class,'page-title-h1')]")
                .resolve();
    }

    public Locator viewReleaseDetails(String name, String label) {
        return new ResilientLocator(page, "View details of release: " + name)
                .custom("View Details within named release card", () -> page.locator(".train-card-wrapper .card")
                        .filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.HEADING,
                                new Page.GetByRoleOptions().setName(name).setExact(true))))
                        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(label)))
                .byXPath("//h2[normalize-space()='" + name
                        + "']/ancestor::div[contains(concat(' ',normalize-space(@class),' '),' card ')][1]"
                        + "//button[contains(normalize-space(),'" + label + "')]")
                .resolve();
    }

    public Locator releaseDetailsTab(String label) {
        return new ResilientLocator(page, "Release drawer tab: " + label)
                .custom("Tab within release drawer", () -> page.locator("#menu")
                        .getByRole(AriaRole.TAB, new Locator.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//div[contains(@class,'trial-side-menu')]//button[@role='tab'][normalize-space()='"
                        + label + "']")
                .resolve();
    }

    public Locator savedReleaseName() {
        return new ResilientLocator(page, "Saved release name in drawer")
                .byCss("#menu .side-drawer-title")
                .byXPath("//div[contains(@class,'trial-side-menu')]//div[contains(@class,'side-drawer-title')]")
                .resolve();
    }

    public Locator savedReleaseVersion() {
        return new ResilientLocator(page, "Saved release version in drawer")
                .byCss("#menu .side-drawer-title + p")
                .byXPath("//div[contains(@class,'trial-side-menu')]//div[contains(@class,'side-drawer-title')]/following-sibling::p[1]")
                .resolve();
    }

    public Locator savedReleaseStatus() {
        return new ResilientLocator(page, "Saved release status in drawer")
                .byCss("#menu .side-menu-header .badge")
                .byXPath("//div[contains(@class,'trial-side-menu')]//div[contains(@class,'side-menu-header')]//span[contains(@class,'badge')]")
                .resolve();
    }

    public Locator releaseDetailValue(String label) {
        return new ResilientLocator(page, "Saved release detail: " + label)
                .byXPath("//div[contains(@class,'trial-side-menu')]//*[@role='tabpanel' and not(@hidden)]"
                        + "//label[normalize-space()='" + label + "']/following-sibling::div[1]")
                .custom("Value beside exact label in active drawer panel", () -> page.locator("#menu")
                        .getByRole(AriaRole.TABPANEL)
                        .getByText(label, new Locator.GetByTextOptions().setExact(true))
                        .locator("xpath=following-sibling::div[1]"))
                .resolve();
    }

    public Locator closeReleaseDetails() {
        return new ResilientLocator(page, "Close release Details drawer")
                .byCss("#menu .side-menu-close")
                .byXPath("//div[contains(@class,'trial-side-menu')]//div[contains(@class,'side-menu-close')]")
                .resolve();
    }

    public Locator releaseCards() {
        return new ResilientLocator(page, "Release cards in the current train")
                .byCss(".train-card-wrapper")
                .byXPath("//div[contains(@class,'train-card-wrapper')]")
                .resolve().locator(".card");
    }

    public Locator emptyState(String message) {
        return new ResilientLocator(page, "Release train empty state: " + message)
                .custom("Exact empty-state message ready", () -> {
                    Locator emptyState = page.getByText(message, new Page.GetByTextOptions().setExact(true));
                    emptyState.waitFor();
                    return emptyState;
                })
                .byRole(AriaRole.HEADING, message)
                .resolve();
    }

    public Locator assignedProductPortfolio(String portfolioName) {
        return new ResilientLocator(page, "Assigned product portfolio: " + portfolioName)
                .custom("Portfolio heading in the active release drawer tab", () -> page.locator("#menu")
                        .getByRole(AriaRole.TABPANEL).getByRole(AriaRole.HEADING,
                                new Locator.GetByRoleOptions().setName(portfolioName).setExact(true).setLevel(3)))
                .byXPath("//div[contains(@class,'trial-side-menu')]//*[@role='tabpanel' and not(@hidden)]"
                        + "//h3[normalize-space()='" + portfolioName + "']")
                .resolve();
    }

    public Locator assignedProductNames(String portfolioName) {
        return new ResilientLocator(page, "Assigned products in portfolio: " + portfolioName)
                .custom("Named portfolio card in the release drawer", () -> assignedProductPortfolio(portfolioName)
                        .locator("xpath=ancestor::div[contains(concat(' ',normalize-space(@class),' '),' card ')][1]"))
                .byXPath("//div[contains(@class,'trial-side-menu')]//*[@role='tabpanel' and not(@hidden)]"
                        + "//div[contains(concat(' ',normalize-space(@class),' '),' card ')]"
                        + "[.//h3[normalize-space()='" + portfolioName + "']]")
                .resolve().getByRole(AriaRole.HEADING, new Locator.GetByRoleOptions().setLevel(5).setIncludeHidden(true));
    }

    public Locator assignedProductNavigationControl(String portfolioName, String productName) {
        return new ResilientLocator(page, "Open assigned product: " + productName)
                .custom("Interactive control in the assigned product row", () -> assignedProductPortfolio(portfolioName)
                        .locator("xpath=ancestor::div[contains(concat(' ',normalize-space(@class),' '),' card ')][1]")
                        .getByRole(AriaRole.HEADING,
                                new Locator.GetByRoleOptions().setName(productName).setExact(true).setLevel(5))
                        .locator("xpath=ancestor::*[.//*[self::button or self::a or @role='button' or @role='link']][1]"
                                + "//*[self::button or self::a or @role='button' or @role='link'][1]"))
                .byXPath("//div[contains(@class,'trial-side-menu')]//*[@role='tabpanel' and not(@hidden)]"
                        + "//div[contains(concat(' ',normalize-space(@class),' '),' card ')]"
                        + "[.//h3[normalize-space()='" + portfolioName + "']]"
                        + "//h5[normalize-space()='" + productName + "']/parent::*"
                        + "//*[self::button or self::a or @role='button' or @role='link'][1]")
                .custom("Clickable icon adjacent to assigned product", () -> assignedProductPortfolio(portfolioName)
                        .locator("xpath=ancestor::div[contains(concat(' ',normalize-space(@class),' '),' card ')][1]")
                        .getByText(productName, new Locator.GetByTextOptions().setExact(true))
                        .locator("xpath=following-sibling::*[1]"))
                .resolve();
    }

    public Locator dependencyGraphTab(String label) {
        return new ResilientLocator(page, "Release Trains dependency graph tab")
                .custom("Exact dependencies tab", () -> page.getByRole(AriaRole.TAB,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byCss("a[href='#dependencies']")
                .custom("Exact dependency navigation label", () -> page.getByText(label,
                        new Page.GetByTextOptions().setExact(true)))
                .resolve();
    }

    public Locator dependencyGraphDateRange() {
        return new ResilientLocator(page, "Dependency graph date range filter")
                .byXPath("//*[normalize-space(text())='date_range']/ancestor::*[.//input][1]//input")
                .byCss("input[placeholder*='date range' i], input[aria-label*='date range' i]")
                .byCss("input[value*=' - ']:visible")
                .resolve();
    }

    public Locator dependencyGraphScrollbar(boolean horizontal) {
        String direction = horizontal ? "horizontal" : "vertical";
        return new ResilientLocator(page, "Dependency graph " + direction + " scrollbar")
                .byCss("#mainComponent .yfiles-scrollbar-" + direction + ":visible")
                .byCss(".graphPanel .yfiles-scrollbar-" + direction + ":visible")
                .resolve();
    }

    public Locator dependencyGraphScrollbarTrack(Locator scrollbar) {
        return new ResilientLocator(page, "Dependency graph scrollbar track")
                .custom("yFiles scrollbar range", () -> scrollbar.locator(".yfiles-scrollbar-range"))
                .custom("Scrollbar track with slider", () -> scrollbar.locator(
                        "xpath=./div[.//div[contains(@class,'yfiles-scrollbar-slider')]]"))
                .resolve();
    }

    public Locator dependencyGraphScrollbarThumb(Locator track) {
        return new ResilientLocator(page, "Dependency graph scrollbar thumb")
                .custom("yFiles scrollbar slider", () -> track.locator(".yfiles-scrollbar-slider"))
                .custom("Scrollbar slider by role", () -> track.locator("[role='slider']"))
                .resolve();
    }

    public Locator dependencyGraphLabel(String name) {
        Locator trainPane = page.locator("#taskComponent");
        return trainPane.getByText(name, new Locator.GetByTextOptions().setExact(true))
                .or(trainPane.locator("xpath=.//*[local-name()='text'][normalize-space()='" + name + "']"));
    }

    public Locator dependencyGraphReleaseNode(String releaseName) {
        Locator graphPane = page.locator("#mainComponent");
        return graphPane.getByText(releaseName, new Locator.GetByTextOptions().setExact(true))
                .or(graphPane.locator("xpath=.//*[local-name()='text'][normalize-space()='" + releaseName + "']"));
    }
    public Locator graphRelationshipTab(String label) {
        return new ResilientLocator(page, "Graph relationship tab: " + label)
                .custom("Exact relationship tab", () -> page.getByRole(AriaRole.TAB,
                        new Page.GetByRoleOptions().setName(label).setExact(true)))
                .byXPath("//*[@role='tab'][normalize-space()='" + label + "']")
                .resolve();
    }

    public Locator graphRelationshipRelease(String releaseName) {
        return new ResilientLocator(page, "Related release in the selected graph tab")
                .byXPath("//label[normalize-space()='Dependent Release']/following-sibling::div"
                        + "//h4[normalize-space()='" + releaseName + "']")
                .custom("Exact related release heading", () -> page.getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setLevel(4).setName(releaseName).setExact(true)))
                .resolve();
    }
    public Locator dependencyGraphCanvas() {
        return page.locator("#mainComponent").or(page.locator(".ganttChartContainer .graphPanel"));
    }

    public Locator dependencyGraphCalendarField(String field) {
        return new ResilientLocator(page, "Dependency calendar " + field + " selector")
                .byCss(".react-datepicker__month-container:first-of-type ." + field + "-dropdown .react-select__control")
                .custom("First calendar's " + field + " selector", () -> page.locator(".react-datepicker__month-container")
                        .first().locator("." + field + "-dropdown .react-select__control"))
                .resolve();
    }

    public Locator dependencyGraphCalendarDay(java.time.LocalDate date) {
        String month = date.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);
        String label = "^Choose .*" + month + " " + date.getDayOfMonth()
                + "(?:st|nd|rd|th)?, " + date.getYear() + "$";
        return new ResilientLocator(page, "Dependency calendar day " + date)
                .custom("Exact calendar date in the active month", () -> page.getByRole(AriaRole.OPTION,
                                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile(label)))
                        .and(page.locator(".react-datepicker__day:not(.react-datepicker__day--outside-month)")))
                .byCss(".react-datepicker__month[aria-label='Month " + month + ", " + date.getYear() + "']"
                        + " .react-datepicker__day--" + String.format("%03d", date.getDayOfMonth())
                        + ":not(.react-datepicker__day--outside-month)")
                .resolve();
    }
}
