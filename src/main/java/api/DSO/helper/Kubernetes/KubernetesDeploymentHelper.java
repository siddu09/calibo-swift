package api.DSO.helper.Kubernetes;

import common.apiCommon.RequestSpecProvider;
import constants.ApiEndpoints.DSOApiEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class KubernetesDeploymentHelper {
    private final Map<String, Object> state;

    public KubernetesDeploymentHelper(Map<String, Object> state) { this.state = state; }
    @Step("Add Technology")
    public void configureKubernetes() {
        JSONObject data = (JSONObject) required("testData");
        JSONArray requests = new KubernetesConfigurationHelper(state).requests();
        Response response = given().spec(RequestSpecProvider.get())
                .queryParam("workstreamId", data.get("workstreamId"))
                .queryParam("releaseId", data.get("releaseId")).body(requests.toJSONString())
                .post(DSOApiEndpoints.STAGE_TECH_STACK_PIPELINE);
        if (response.statusCode() != 200)
            throw new IllegalStateException("configure Kubernetes pipelines failed: HTTP "
                    + response.statusCode() + ": " + response.asString());
    }

    private Object required(String key) { Object value = state.get(key); if (value == null) throw new IllegalStateException("Missing DevSecOps runtime value: " + key); return value; }
}