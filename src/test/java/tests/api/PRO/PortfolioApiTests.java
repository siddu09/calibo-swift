package tests.api.PRO;

import api.PRO.portfolio.building_blocks.PortfolioApiBuildingBlock;
import api.PRO.portfolio.helper.PortfolioApiHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import base.apibase.BaseTest;
import utils.LoggerUtil;

public class PortfolioApiTests extends BaseTest {

    @Test(groups ="Portfolio")
    @Step("Create Portfolio")
    public void createPortfolioWithMandatoryFields() {
        PortfolioApiHelper helper = new PortfolioApiHelper();
        PortfolioApiBuildingBlock portfolio = new PortfolioApiBuildingBlock();
        JSONObject request = helper.loadPortfolioTestData();
        String portfolioTitle = helper.getUniquePortfolioName(request);
        request.put("title", portfolioTitle);
        Response response = portfolio.addNewProductPortfolio(request);
        portfolio.verifyProductPortfolio(response);
        helper.updateRuntimeData(portfolio.getPortfolioTitle(), portfolio.getPortfolioId());
        System.out.println("Portfolio ID: " + portfolio.getPortfolioId());
    }
}
