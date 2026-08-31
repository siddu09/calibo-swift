package UI.AIML.RAGPipeline.BuildingBlocks;

import ai.config.RagPipelineConfig;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import utils.LoggerUtil;

/**
 * Building block for project-level navigation operations.
 * Handles page load waits, project URL navigation, and feature selection.
 */
public class ProjectNavigationBuildingBlock {

    private final Page page;

    public ProjectNavigationBuildingBlock(Page page) {
        this.page = page;
    }

    @Step("Wait for page to load after login")
    public void waitForPageLoadAfterLogin() {
        LoggerUtil.LOGGER.info("[PROJECT-NAV] Waiting for page to load after login...");
        
        long loginRedirectTimeout = RagPipelineConfig.getTimeout("loginRedirectTimeout");
        
        try {
            // Poll for URL to change from /login
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < loginRedirectTimeout) {
                String currentUrl = page.url();
                LoggerUtil.LOGGER.debug("[PROJECT-NAV] Current URL: {}", currentUrl);
                
                // If URL is no longer the login page, page has loaded
                if (!currentUrl.contains("/login")) {
                    LoggerUtil.LOGGER.info("[PROJECT-NAV] ✓ Page loaded successfully. Current URL: {}", currentUrl);
                    page.waitForTimeout(2000);  // Additional wait for DOM to stabilize
                    return;
                }
                
                page.waitForTimeout(500);  // Check every 500ms
            }
            
            LoggerUtil.LOGGER.warn("[PROJECT-NAV] Login redirect timeout after {}ms", loginRedirectTimeout);
        } catch (Exception e) {
            LoggerUtil.LOGGER.error("[PROJECT-NAV] Error waiting for page load: {}", e.getMessage());
        }
    }

    @Step("Navigate to project URL from config")
    public void navigateToProjectFromConfig() {
        String projectUrl = RagPipelineConfig.getProjectViewUrl();
        navigateToProject(projectUrl);
    }

    @Step("Navigate to project URL: {projectUrl}")
    public void navigateToProject(String projectUrl) {
        LoggerUtil.LOGGER.info("[PROJECT-NAV] Navigating to project URL: {}", projectUrl);
        page.navigate(projectUrl);
        long navigationTimeout = RagPipelineConfig.getTimeout("navigationTimeout");
        page.waitForTimeout(navigationTimeout);
        LoggerUtil.LOGGER.info("[PROJECT-NAV] ✓ Successfully navigated to project");
    }

    @Step("Click on feature from Workstreams tab: {featureName}")
    public void selectFeatureFromWorkstreams(String featureName) {
        LoggerUtil.LOGGER.info("[PROJECT-NAV] Selecting feature from workstreams: {}", featureName);
        // Prefer clicking the title element's ancestor anchor — handles nested markup like <a><div><h2>Title</h2></div></a>
        String xpath = "//h2[normalize-space(text())='" + featureName + "']/ancestor::a[1] | //a[.//h2[contains(normalize-space(.), '" + featureName + "')]]";
        try {
            page.locator(xpath).first().click();
            LoggerUtil.LOGGER.info("[PROJECT-NAV] ✓ Clicked feature: {}", featureName);
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[PROJECT-NAV] Primary feature click failed, falling back to link search: {}", e.getMessage());
            // Fallback: search any anchor that contains the feature name in text anywhere inside
            page.locator("//a[contains(., '" + featureName + "')]").first().click();
            LoggerUtil.LOGGER.info("[PROJECT-NAV] ✓ Clicked feature (fallback): {}", featureName);
        }
        page.waitForTimeout(2000);  // Wait for feature page to load
    }
}
