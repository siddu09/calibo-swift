package pages.PRO.Dashboard;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

public class DashboardPage {

    private final Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

        public Locator overviewTab() {
            return new ResilientLocator(page, "Overview tab")
                    .byCss("a[role='tab'][href='/dashboard#0']")
                    .byXPath("//a[@role='tab'][.//*[normalize-space()='Overview']]")
                    .byText("Overview")
                    .resolve();
        }

        public Locator releaseTrainsTab() {
            return new ResilientLocator(page, "Release Trains tab")
                    .byCss("a[role='tab'][href='/dashboard#1']")
                    .byXPath("//a[@role='tab'][.//*[normalize-space()='Release Trains']]")
                    .byText("Release Trains")
                    .resolve();
        }

        public Locator roadmapsTab() {
            return new ResilientLocator(page, "Roadmaps tab")
                    .byCss("a[role='tab'][href='/dashboard#2']")
                    .byXPath("//a[@role='tab'][.//*[normalize-space()='Roadmaps']]")
                    .byText("Roadmaps")
                    .resolve();
        }

        public Locator teamsTab() {
            return new ResilientLocator(page, "Teams tab")
                    .byCss("a[role='tab'][href='/dashboard#3']")
                    .byXPath("//a[@role='tab'][.//*[normalize-space()='Teams']]")
                    .byText("Teams")
                    .resolve();
        }

        public Locator businessRequirementsTab() {
            return new ResilientLocator(page, "Business Requirements tab")
                    .byCss("a[role='tab'][href='/dashboard#4']")
                    .byXPath("//a[@role='tab'][.//*[normalize-space()='Business Requirements']]")
                    .byText("Business Requirements")
                    .resolve();
        }

        public Locator agilityTab() {
            return new ResilientLocator(page, "Agility tab")
                    .byCss("a[role='tab'][href='/dashboard#5']")
                    .byXPath("//a[@role='tab'][.//*[normalize-space()='Agility']]")
                    .byText("Agility")
                    .resolve();
        }

        public Locator financialsTab() {
            return new ResilientLocator(page, "Financials tab")
                    .byCss("a[role='tab'][href='/dashboard#6']")
                    .byXPath("//a[@role='tab'][.//*[normalize-space()='Financials']]")
                    .byText("Financials")
                    .resolve();
        }
    }