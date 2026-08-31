package UI.AIML.RAGPipeline.workflows;

import UI.AIML.RAGPipeline.helperutils.NewFeatureCreationUtil;
import UI.DPS.helpers.DataPipeline.DataPipelineBuilder;
import UI.E2E.LoginBuildingBlock;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import UI.PRO.datahelper.*;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.DSO.EditFeaturePage;
import pages.DPS.CrawlerCatalog.DataCatalogPage;
import pages.PRO.Product.ProductFeatureTabPage;
import testdatamanager.aiml.RagPipelineTestData;
import testdatamanager.pro.ProExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;

/**
 * PRODUCT-LEVEL workflow: provisions a brand-new RAG pipeline end-to-end —
 * Portfolio -> Product -> Feature (inline, via "Yes" prompt) -> Data Pipeline
 * Studio (Data Lake+Snowflake, RAG Builder, connect, run) -> RAG validation.
 */

public class RagEndtoEndPipelineWorkflow {

    private final Page page;
    private final LoginBuildingBlock loginBuildingBlock;
    private final ProductPortfolioFlows portfolioFlows;
    private final ProductFlows productFlows;
    private final FeatureFlows featureFlows;
    private final DataPipelineBuilder pipelineBuilder;
    private final ExecuteRagfromAlreadyCreatedPipelineWorkflow ragValidationWorkflow;
    private final ProExecutionData executionData;
    String aiTenant = configHandler.ConfigManager.getUIProperty("tenantName.AI");
    String aiUser = configHandler.ConfigManager.getUIProperty("user.AI");
    String aiPass = configHandler.ConfigManager.getUIProperty("pass.AI");



    public RagEndtoEndPipelineWorkflow(Page page) {
        this.page = page;
        this.executionData = new ProExecutionData();
        this.loginBuildingBlock = new LoginBuildingBlock(page);
        this.portfolioFlows = new ProductPortfolioFlows(page, executionData);
        this.productFlows = new ProductFlows(page, executionData);
        this.featureFlows = new FeatureFlows(page, executionData);
        this.pipelineBuilder = new DataPipelineBuilder(page);
        this.ragValidationWorkflow = new ExecuteRagfromAlreadyCreatedPipelineWorkflow(page);
    }

    @Step("Provision new RAG pipeline end-to-end and validate")
    public void provisionAndValidateRagPipeline(
            String tenant,
            String question,
            String groundTruthContext) {

        // Now perform login
        loginBuildingBlock.login(tenant);

        PortfolioData portfolioData = RagPipelineTestData.getPortfolio("createPortfolio");
        ProductData productData = RagPipelineTestData.getProduct("createProduct");
        utils.FeatureData featureData = RagPipelineTestData.getAimlFeature("createFeature");

        //Step1
        provisionPortfolio(portfolioData);
        //Step2
        provisionProductAndFeature(productData, featureData);
        
       //Step3
        configureFeatureUsingThreeDots(featureData);

        //Step 4
        buildRagDataPipeline();

        String answer = ragValidationWorkflow.testRagEndpointWithQuestion(question);

        //Step 5
        ragValidationWorkflow.validateAnswerWithAI(question, answer, groundTruthContext);

        //Step 6
        ragValidationWorkflow.assertPipelineComponentsPresent();

        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Pipeline provisioned and validated: {}",
                executionData.getPortfolioName());
    }

    @Step("Create Product Portfolio")
    private void provisionPortfolio(PortfolioData portfolioData) {
        portfolioFlows.createPortfolioWithMandatoryFields(portfolioData);
        portfolioFlows.navigateToProductsTab();
        portfolioFlows.addProductToPortfolio();
    }

    @Step("Create Product and Feature")
    private void provisionProductAndFeature(ProductData productData, utils.FeatureData featureData) {
        productFlows.createProductAndFeatureInline(productData);
        NewFeatureCreationUtil.createFeatureInline(page, featureData);
    }

    @Step("Configure feature using three-dots menu - Edit Details or View Details")
    private void configureFeatureUsingThreeDots(utils.FeatureData featureData) {
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Step 1: Configuring feature phases using three-dots menu");
        
        ProductFeatureTabPage productFeatureTabPage = new ProductFeatureTabPage(page);
        
        // STEP 1: Open feature edit using three-dots menu
        productFeatureTabPage.editTheFeatureUsingThreeDots("View Details or Edit");
        page.waitForTimeout(1000);
        
        EditFeaturePage editFeaturePage = new EditFeaturePage(page);
        
        // Select all required phases
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Selecting feature phases: Define, Design, Develop");
        editFeaturePage.selectPhases("Define").click();
        page.waitForTimeout(300);
        editFeaturePage.selectPhases("Design").click();
        page.waitForTimeout(300);
        editFeaturePage.selectPhases("Develop").click();
        page.waitForTimeout(300);
        
        // Fill description with feature name
        editFeaturePage.textboxDescription().fill(featureData.getName() != null ? featureData.getName() : "RAG Automation Feature");
        page.waitForTimeout(1000);
        
        // Save changes
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Saving feature configuration from three-dots menu");
        CommonMethods.clickButton(page, "Save").click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] ✓ Step 1 Complete: Feature configured and saved");
        
        // STEP 2: Click the feature to OPEN it (navigate to feature details page)
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Step 2: Opening feature to view details");
        productFeatureTabPage.clickOnFeatureName(featureData.getName()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] ✓ Step 2 Complete: Feature opened");
        
        // STEP 3: Click on "Develop" stage
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Step 3: Navigating to Develop stage");
        featureFlows.selectStage("Develop");
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] ✓ Step 3 Complete: Develop stage opened");
        
        // STEP 4: Click "Data Pipeline Studio" button to enter DPS
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Step 4: Clicking Data Pipeline Studio button");
        DataCatalogPage dataCatalogPage = new DataCatalogPage(page);
        dataCatalogPage.buttonDataPipelineStudio().click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(3000);
        
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] ✓ Step 4 Complete: Data Pipeline Studio opened (Ready to configure pipeline)");
    }

    @Step("Build RAG Data Pipeline: Data Lake + Snowflake + RAG Builder")
    private void buildRagDataPipeline() {
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] Building RAG Data Pipeline...");

        pipelineBuilder.waitForDataPipelinePageLoaded();

        // STEP 1: Add Data Lake stage with Snowflake node
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] STEP 1: Adding Data Lake stage with Snowflake");
        pipelineBuilder.addStage("Data Lake", true, "");
        pipelineBuilder.addNode("Data Lake", "Snowflake");


        pipelineBuilder.addSnowflakeNodeDetails("Snowflake");


        pipelineBuilder.ensureNoBlockingModal();

        page.waitForTimeout(2000);

        // STEP 2: Add RAG Builder stage with Structured RAG Builder node
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] STEP 2: Adding RAG Builder stage with Structured RAG Builder");
        pipelineBuilder.addStage("RAG Builder", false, "Data Lake");
        pipelineBuilder.addNode("RAG Builder", "Structured RAG Builder");
        pipelineBuilder.addRagBuilderNodeDetails("Structured RAG Builder", "Structured RAG Builder", "SF_QA_RAG_2");

        pipelineBuilder.ensureNoBlockingModal();

        page.waitForTimeout(2000);

        // STEP 3a: Connect nodes - Forward direction (Snowflake → RAG Builder)
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] STEP 3a: Connecting Snowflake → Structured RAG Builder");
        pipelineBuilder.connectNodes("Snowflake", "Structured RAG Builder");

        page.waitForTimeout(2000);

        // STEP 3b: Connect nodes - Reverse direction (RAG Builder → Snowflake)
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] STEP 3b: Connecting Structured RAG Builder → Snowflake");
        pipelineBuilder.connectNodes("Structured RAG Builder", "Snowflake");

        page.waitForTimeout(2000);

        // STEP 4: Configure Snowflake datastore (Configuration Details panel)
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] STEP 4: Configuring Snowflake datastore");
        pipelineBuilder.configureSnowflakeDatastore("SF_QA_RAG_2");

        page.waitForTimeout(2000);

        // STEP 5: Configure Structured RAG Builder
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] STEP 5: Configure Structured RAG Builder");
        pipelineBuilder.configureStructuredRagBuilder();

        page.waitForTimeout(2000);

        // STEP 6: Run pipeline
        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] STEP 6: Running RAG Data Pipeline");
        pipelineBuilder.runDataPipeline();

        LoggerUtil.LOGGER.info("[RAG-PROVISIONING] ✓ RAG Data Pipeline built and running");
    }

}