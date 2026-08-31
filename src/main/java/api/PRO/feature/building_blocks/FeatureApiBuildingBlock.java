package api.PRO.feature.building_blocks;

import io.qameta.allure.Step;

import common.apiCommon.RequestSpecProvider;
import constants.ApiEndpoints.ApiEndpints;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class FeatureApiBuildingBlock {
    private String featureId;
    private String releaseId;

    @Step("Create New Feature")
    public Response addNewFeature(JSONObject request) {
        return given().spec(RequestSpecProvider.get()).body(request.toJSONString())
                .post(ApiEndpints.FEATURE);
    }

    @Step("Verify Created Feature")
    public void verifyFeature(Response response, String productId) {
        Assert.assertEquals(response.statusCode(), 201, response.asString());
        featureId = response.jsonPath().getString("id");
        releaseId = response.jsonPath().getString("releaseId");
        Assert.assertNotNull(featureId, "Feature ID is missing");
        Assert.assertEquals(response.jsonPath().getString("projectId"), productId);
    }

    public String getFeatureId() {
        return featureId;
    }


    public String getReleaseId() {
        return releaseId;
    }
}
