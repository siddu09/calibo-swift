package pages.PRO.MaturityAssesment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

public class MaturityAssesment {

    public class ProductAddProjectDetailsPage {
        private final Page page;

        public ProductAddProjectDetailsPage(Page page) {
            this.page = page;
        }
        public Locator maturityAssessmentMenu() {
            return new ResilientLocator(page, "Maturity Assessment menu")
                    .byCss("div[title='Maturity Assessment']")
                    .byXPath("//div[@title='Maturity Assessment']")
                    .byXPath("//*[@title='Maturity Assessment']")
                    .byText("Maturity Assessment")
                    .resolve();
        }

        public Locator maturityAssessmentExpandButton() {
            return new ResilientLocator(page, "Maturity Assessment expand button")
                    .byCss("div[title='Maturity Assessment'] div[role='button']")
                    .byXPath("//div[@title='Maturity Assessment']//div[@role='button']")
                    .byXPath("//*[@title='Maturity Assessment']//*[@role='button']")
                    .resolve();
        }

        public Locator organizationMaturityAssessment() {
            return new ResilientLocator(
                    page,
                    "Organization Maturity Assessment"
            )
                    .byCss("[data-testid='left-drawer-maturity-assessment-organization']")
                    .byCss("a[href='/technical-maturity/overall?type=tnt']")
                    .byXPath("//a[@data-testid='left-drawer-maturity-assessment-organization']")
                    .byXPath("//a[@href='/technical-maturity/overall?type=tnt']")
                    .byText("Organization")
                    .resolve();
        }

        public Locator productMaturityAssessment() {
            return new ResilientLocator(
                    page,
                    "Product Maturity Assessment"
            )
                    .byCss("[data-testid='left-drawer-maturity-assessment-product']")
                    .byCss("a[href='/technical-maturity/overall/products']")
                    .byXPath("//a[@data-testid='left-drawer-maturity-assessment-product']")
                    .byXPath("//a[@href='/technical-maturity/overall/products']")
                    .byText("Product")
                    .resolve();
        }

        public Locator teamMaturityAssessment() {
            return new ResilientLocator(
                    page,
                    "Team Maturity Assessment"
            )
                    .byCss("[data-testid='left-drawer-maturity-assessment-team']")
                    .byCss("a[href='/technical-maturity/overall/teams']")
                    .byXPath("//a[@data-testid='left-drawer-maturity-assessment-team']")
                    .byXPath("//a[@href='/technical-maturity/overall/teams']")
                    .byText("Team")
                    .resolve();
        }
    }


    }

