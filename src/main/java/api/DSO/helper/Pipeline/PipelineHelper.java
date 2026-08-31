package api.DSO.helper.Pipeline;

import api.DSO.helper.DeploymentStage.DeployStageRequestHelper;
import common.apiCommon.RequestSpecProvider;
import constants.ApiEndpoints.DSOApiEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public final class PipelineHelper {
    private final Map<String, Object> state;

    public PipelineHelper(Map<String, Object> state) { this.state = state; }

    @Step("Run CI Pipelines")
    public void runCiPipeline() {
        for (String id : ids("pipelineIds")) ok(given().spec(RequestSpecProvider.get())
                .pathParam("pipelineDetailsId", id).get(DSOApiEndpoints.PIPELINE_CI_RUN), "run CI pipeline " + id);
    }
    @Step("Run Deployment Pipelines")
    public void runDeploymentPipeline() {
        JSONObject setup = json("setup"), data = json("testData");
        JSONObject body = DeployStageRequestHelper.deploy(data, required("stageDetailsId").toString(),
                ids("successfulPipelineIds"), setup.get("imageTag").toString());
        ok(given().spec(RequestSpecProvider.get()).body(body.toJSONString()).post(DSOApiEndpoints.PIPELINE_DEPLOY),
                "deploy pipelines");
    }

    private Response ok(Response response, String activity) { if (response.statusCode() != 200) throw new IllegalStateException(activity + " failed: HTTP " + response.statusCode() + ": " + response.asString()); return response; }
    @SuppressWarnings("unchecked") private List<String> ids(String key) { return (List<String>) required(key); }
    private JSONObject json(String key) { return (JSONObject) required(key); }
    private Object required(String key) { Object value = state.get(key); if (value == null) throw new IllegalStateException("Missing DevSecOps runtime value: " + key); return value; }
}