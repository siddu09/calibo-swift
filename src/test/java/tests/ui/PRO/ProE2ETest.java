package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Features.Define.Flows.DefineFlows;
import UI.PRO.Features.Design.Flows.DesignFlows;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;

public class ProE2ETest extends BaseUITest {

    private ProExecutionData executionData;

    private ProductPortfolioFlows portfolioFlows;
    private ProductFlows productFlows;
    private FeatureFlows featureFlows;
    private DefineFlows defineFlows;
    private DesignFlows designFlows;

    @BeforeMethod
    @Step("Setup: Initialize test environment with all flows")
    public void setUp() {

        LoggerUtil.LOGGER.info("[PRO-E2E-SETUP] Logging in...");
        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);
        loginBuildingBlock.login();

        executionData = new ProExecutionData();

        portfolioFlows = new ProductPortfolioFlows(page, executionData);
        productFlows = new ProductFlows(page, executionData);
        featureFlows = new FeatureFlows(page, executionData);
        defineFlows = new DefineFlows(page, executionData);
        designFlows = new DesignFlows(page, executionData);
        
        LoggerUtil.LOGGER.info("[PRO-E2E-SETUP] ✓ Test environment ready");
    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Complete PRO E2E workflow: Portfolio → Product → Feature → Define → Design with dependency")
    @Severity(SeverityLevel.CRITICAL)
    public void ProE2EWithMandatoryFields() {

        LoggerUtil.LOGGER.info("=================================================");
        LoggerUtil.LOGGER.info("              PRO E2E VALIDATION");
        LoggerUtil.LOGGER.info("=================================================");

        createFirstPortfolioAndProduct();
        createFirstFeatureWithDefineAndDesign();
        createSecondProductWithDependency();
        completeAndRecordResults();

        LoggerUtil.LOGGER.info("=================================================");
        LoggerUtil.LOGGER.info("          PRO E2E VALIDATION COMPLETED");
        LoggerUtil.LOGGER.info("=================================================");
    }

    @Step("Create first portfolio with product")
    private void createFirstPortfolioAndProduct() {
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] Creating first portfolio and product...");

        // 1. Portfolio
        portfolioFlows.createPortfolioWithMandatoryFields();

        // 2. Portfolio -> Products
        portfolioFlows.navigateToProductsTab();

        // 3. Add first Product to Portfolio
        portfolioFlows.addProductToPortfolio();

        // 4. Create first Product
        productFlows.createProductWithMandatoryFields();
        productFlows.navigateToFeatureTab();
        
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] ✓ First portfolio and product created");
    }

    @Step("Create first feature with define and design stages")
    private void createFirstFeatureWithDefineAndDesign() {
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] Creating first feature with define and design...");

        // 5. Create first Feature
        featureFlows.createFeatureWithMandatoryFields();

        // 6. View Feature Details and Select Stage
        featureFlows.viewFeatureDetails();
        featureFlows.selectStage("Define");

        // 7. Define
        defineFlows.createUserFeedbackWithMandatoryFields();

        // 8. Design
        designFlows.createDesignWithMandatoryFields();
        
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] ✓ Feature with define and design created");
    }

    @Step("Create second product with dependency")
    private void createSecondProductWithDependency() {
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] Creating second product with dependency...");

        // 9. Go back to existing Portfolio
        portfolioFlows.openExistingPortfolio();

        // 10. Portfolio -> Products
        portfolioFlows.navigateToProductsTab();

        // 11. Add second Product
        portfolioFlows.addProductToPortfolio();

        // 12. Create second Product
        productFlows.createProductWithMandatoryFields();

        // 13. Add dependency to second Product
        productFlows.addDependencyToProduct();

        // 14. Create second Feature
        productFlows.navigateToFeatureTab();
        featureFlows.createFeatureWithMandatoryFields();
        
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] ✓ Second product with dependency created");
    }

    @Step("Record execution results")
    private void completeAndRecordResults() {
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] Recording execution results...");
        // 15. Write execution result
        ProExecutionResultWriter.write("ProE2E", "Pro E2E With Mandatory Fields", executionData);
        LoggerUtil.LOGGER.info("[PRO-E2E-STEP] ✓ Results recorded");
    }
}