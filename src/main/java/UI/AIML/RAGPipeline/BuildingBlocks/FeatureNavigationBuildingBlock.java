package UI.AIML.RAGPipeline.BuildingBlocks;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import utils.CommonMethods;
import utils.LoggerUtil;

/**
 * Building block responsible for opening a feature from the Products page.
 *
 * This class owns only the feature-navigation capability.
 * It does not know about RAG Builder, pipeline canvas, Conversa, or AI validation.
 */
public class FeatureNavigationBuildingBlock {

    private final Page page;
    private final CommonMethods commonMethods;

    public FeatureNavigationBuildingBlock(Page page) {
        this.page = page;
        this.commonMethods = new CommonMethods();
    }

    /**
     * Navigates to the given feature from the Products page.
     *
     * @param featureName feature name visible on the Products page
     */
    @Step("Open feature: {featureName}")
    public void openFeature(String featureName) {
        LoggerUtil.LOGGER.info("[RAG-FEATURE-BLOCK] Opening feature '{}'", featureName);

        waitForPageReady(featureName);

        Locator featureLocator = commonMethods.clickFeatureByName(page, featureName);
        featureLocator.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        featureLocator.click();

        LoggerUtil.LOGGER.info("[RAG-FEATURE-BLOCK] Feature '{}' opened successfully", featureName);

        page.waitForTimeout(2000);
    }

    private void waitForPageReady(String expectedElementText) {
        page.waitForTimeout(1000);
        CommonMethods.waitForLoaderToDisappear(page);

        try {
            page.locator(String.format("//h2[contains(text(),'%s')]", expectedElementText))
                    .first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(45000));
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn(
                    "[RAG-FEATURE-BLOCK] '{}' not visible within 45s, proceeding anyway",
                    expectedElementText
            );
        }
    }
}