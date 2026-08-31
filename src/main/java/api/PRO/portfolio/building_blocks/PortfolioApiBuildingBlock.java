package api.PRO.portfolio.building_blocks;

import io.qameta.allure.Step;

import common.apiCommon.RequestSpecProvider;
import constants.ApiEndpoints.ApiEndpints;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.*;

public class PortfolioApiBuildingBlock {
    private String portfolioId;
    private String portfolioTitle;

    @Step("Create New Product Portfolio")
    public Response addNewProductPortfolio(JSONObject request) {
        return given()
                .spec(RequestSpecProvider.get())
                .body(request.toJSONString())
                .post(ApiEndpints.PORTFOLIOS);
    }

    @Step("Verify Created Product Portfolio")
    public void verifyProductPortfolio(Response response) {
        Assert.assertEquals(response.statusCode(), 201, response.asString());
        portfolioId = response.jsonPath().getString("id");
        portfolioTitle = response.jsonPath().getString("title");
        Assert.assertNotNull(response.jsonPath().getString("id"), "Portfolio ID is missing");
    }
    public String getPortfolioId() {
        return portfolioId;
    }

    public String getPortfolioTitle() {
        return portfolioTitle;
    }
}
