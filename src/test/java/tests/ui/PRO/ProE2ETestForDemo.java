package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Features.Feature.Flows.FeatureFlows;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;
@Test(groups = {"PRO-UI"})
public class ProE2ETestForDemo extends BaseUITest {

    private ProExecutionData executionData;

    private ProductPortfolioFlows portfolioFlows;
    private ProductFlows productFlows;
    private FeatureFlows featureFlows;

    @BeforeMethod
    @Step("Setup: Initialize test environment and Login")
    public void setUp() {

        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);

        loginBuildingBlock.login();

        executionData = new ProExecutionData();

        portfolioFlows = new ProductPortfolioFlows(page, executionData);

        productFlows = new ProductFlows(page, executionData);

        featureFlows = new FeatureFlows(page, executionData);

    }

    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_E2E})
    @Description("Create portfolio, product, and feature using mandatory fields only.")
    public void ProE2EWithMandatoryFieldsForDemo() {

        LoggerUtil.LOGGER.info("=================================================");

        LoggerUtil.LOGGER.info("              PRO E2E VALIDATION");

        LoggerUtil.LOGGER.info("=================================================");

        portfolioFlows.createPortfolioWithMandatoryFields();

        productFlows.createProductWithMandatoryFields();

        featureFlows.createFeatureWithMandatoryFields();

        ProExecutionResultWriter.write("ProE2E", "Pro E2E With Mandatory Fields", executionData);

        LoggerUtil.LOGGER.info("=================================================");

        LoggerUtil.LOGGER.info("          PRO E2E VALIDATION COMPLETED");

        LoggerUtil.LOGGER.info("=================================================");
    }


}