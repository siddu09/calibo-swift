package pages.generated.selfhealing;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import drivers.PlaywrightFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerUtil;
import utils.CaliboAccelerateLoginUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectsPageLocatorsTest {

    /*private PlaywrightFactory factory;
    private Page page;
    private ProjectsPage projectsPage;

    @BeforeClass
    @org.testng.annotations.Parameters({"tenant"})
    public void setUp(@org.testng.annotations.Optional("ai1.automation@calibo.com") String tenant) {
        factory = new PlaywrightFactory();
        page = factory.initializeBrowser();
        // determine target URL: system property -> test properties -> default derived from browser URL
        String projectsUrl = System.getProperty("DPSURL");
        if (projectsUrl == null || projectsUrl.isEmpty()) {
            try {
                java.util.Properties p = new java.util.Properties();
                p.load(new java.io.FileInputStream("src/test/resources/config/envConfig/ui/qa.properties"));
                projectsUrl = p.getProperty("DPSURL");
            } catch (Exception ignored) {
            }
        }
        if (projectsUrl == null || projectsUrl.isEmpty()) {
            projectsUrl = page.url().replaceAll("/login$", "") + "/projects";
        }

        try {
            CaliboAccelerateLoginUtil.loginToPage(page, projectsUrl, null, tenant);
        } catch (Exception e) {
            // fallback: navigate directly if login util fails
            LoggerUtil.LOGGER.warn("Login failed, falling back to direct navigation: {}", e.getMessage());
            page.navigate(projectsUrl);
        }

        projectsPage = new ProjectsPage(page);
    }

    @AfterClass
    public void tearDown() {
        if (factory == null) return;
        if (Boolean.getBoolean("keepBrowserOpen")) {
            LoggerUtil.LOGGER.info("Keeping browser open for debugging (keepBrowserOpen=true)");
            return;
        }
        factory.closeBrowser();
    }

    @Test
    public void verifyAllLocators() {
        Map<String, Boolean> results = new LinkedHashMap<>();
        List<String> failures = new ArrayList<>();

        // Helper to evaluate a locator safely
        java.util.function.BiConsumer<String, Locator> eval = (name, locator) -> {
            try {
                int count = locator.count();
                boolean found = count > 0;
                results.put(name, found);
                LoggerUtil.LOGGER.info("Locator " + name + " found=" + found + " (matches=" + count + ")");
                if (!found) failures.add(name + " (0 matches)");
            } catch (Exception e) {
                results.put(name, false);
                failures.add(name + " (exception: " + e.getMessage() + ")");
                LoggerUtil.LOGGER.error("Error evaluating locator " + name, e);
            }
        };

        // Evaluate each locator present on ProjectsPage
        eval.accept("organizationProductTeam", projectsPage.organizationProductTeam());
        eval.accept("openDrawer", projectsPage.openDrawer());
        eval.accept("dashboardA", projectsPage.dashboardA());
        eval.accept("portfoliosA", projectsPage.portfoliosA());
        eval.accept("projectsA", projectsPage.projectsA());
        eval.accept("releaseTrainA", projectsPage.releaseTrainA());
        eval.accept("opsIntelligenceA", projectsPage.opsIntelligenceA());
        eval.accept("organization", projectsPage.organization());
        eval.accept("product", projectsPage.product());
        eval.accept("team", projectsPage.team());
        eval.accept("resourcePlanningDashboardA", projectsPage.resourcePlanningDashboardA());
        eval.accept("dataPipelineCenterA", projectsPage.dataPipelineCenterA());
        eval.accept("engLabHomeA", projectsPage.engLabHomeA());
        eval.accept("myProductsAllProducts", projectsPage.myProductsAllProducts());
        eval.accept("myProducts", projectsPage.myProducts());
        eval.accept("allProducts", projectsPage.allProducts());
        // Summary
        LoggerUtil.LOGGER.info("Locator verification results: " + results);

        // Fail test if any locator threw or returned zero matches
        if (!failures.isEmpty()) {
            Assert.fail("Some locators not found or errored: " + String.join(", ", failures));
        }
    }*/
}
