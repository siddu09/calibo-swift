package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Product.Flows.ProductFlows;
import UI.PRO.ProductPortfolio.Flows.ProductPortfolioFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;

public class ProductTest extends BaseUITest {

    private ProductFlows productFlows;
    private ProExecutionData executionData;
    private ProductPortfolioFlows portfolioFlows;


    @BeforeMethod
    @Step("Setup: Login and initialize Productflow")
    public void setUp() {
        LoginBuildingBlock loginBuildingBlock = new LoginBuildingBlock(page);
        loginBuildingBlock.proLogin();
        executionData = new ProExecutionData();
        productFlows = new ProductFlows(page, executionData);
        portfolioFlows = new ProductPortfolioFlows(page, executionData);
        LoggerUtil.LOGGER.info("============================ Login and Setup completed =========================");
    }

    @Test(groups = {Constants.PRO_REGRESSION,Constants.PRO_PORTFOLIO})
    public void createPrivateProductWithMandatoryFields() {
        portfolioFlows.createPortfolioWithMandatoryFields();
        productFlows.createProductWithMandatoryFields();
        ProExecutionResultWriter.write("ProE2E", "createPrivateProductWithMandatoryFields", executionData);

    }
}
