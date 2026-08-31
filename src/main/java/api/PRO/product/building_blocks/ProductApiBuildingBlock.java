package api.PRO.product.building_blocks;

import io.qameta.allure.Step;

import common.apiCommon.RequestSpecProvider;
import constants.ApiEndpoints.ApiEndpints;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class ProductApiBuildingBlock {
    private String productId;

    @Step("Create New Product")
    public Response addNewProduct(JSONObject request) {
        return given().spec(RequestSpecProvider.get()).body(request.toJSONString())
                .post(ApiEndpints.PRODUCT);
    }

    @Step("Verify Created Product")
    public void verifyProduct(Response response) {
        Assert.assertEquals(response.statusCode(), 201, response.asString());
        productId = response.jsonPath().getString("id");
        Assert.assertNotNull(productId, "Product ID is missing");
    }

    public String getProductId() {
        return productId;
    }
}
