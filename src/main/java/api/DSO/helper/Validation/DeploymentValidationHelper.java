package api.DSO.helper.Validation;

import common.apiCommon.RequestSpecProvider;
import constants.ApiEndpoints.DSOApiEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public final class DeploymentValidationHelper {
    private final Map<String, Object> state;

    public DeploymentValidationHelper(Map<String, Object> state) { this.state = state; }

    @SuppressWarnings("unchecked")
    @Step("Verify CI/CD Logs")
    public void verifyCicdLogs() {
        List<String> ids = pipelineIds();
        if (ids.isEmpty()) throw new IllegalStateException("No successful pipelines are available for log verification");
        JSONArray values = new JSONArray(); values.addAll(ids);
        JSONObject body = new JSONObject(); body.put("pipelineIds", values);
        Response response = given().spec(RequestSpecProvider.get()).body(body.toJSONString())
                .post(DSOApiEndpoints.PIPELINE_STAGE_LOGS);
        if (response.statusCode() != 200) throw new IllegalStateException("CI/CD logs request failed: " + response.asString());
        for (String id : ids) {
            Object stages = response.jsonPath().get(id);
            if (stages == null || stages instanceof java.util.Collection<?> items && items.isEmpty())
                throw new IllegalStateException("CI/CD logs missing for pipeline " + id);
        }
    }

    @Step("Validate Live URL")
    public void validateLiveUrl() {
        new LiveUrlValidationHelper(state).validateLiveUrl();
    }
    @Step("Configure Kubernetes Deployment")
    private List<String> pipelineIds() {
        Object value = state.get("successfulPipelineIds");
        return value instanceof List<?> items ? items.stream().map(Object::toString).toList() : List.of();
    }
}
