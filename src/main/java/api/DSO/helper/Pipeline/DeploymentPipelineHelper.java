package api.DSO.helper.Pipeline;

import io.qameta.allure.Step;

import java.util.Map;

public final class DeploymentPipelineHelper {
    private final PipelineHelper pipeline;
    private final PipelineStatusHelper status;

    public DeploymentPipelineHelper(Map<String, Object> state) {
        pipeline = new PipelineHelper(state);
        status = new PipelineStatusHelper(state);
    }
    @Step("Run CI Pipelines")
    public void runCiPipelines() { pipeline.runCiPipeline(); }
    @Step("Wait for CI Success")
    public void waitForCiSuccess() { status.waitForCiSuccess(); }

    public void deployPipelines() { pipeline.runDeploymentPipeline(); }
    @Step("Wait for Deployment Success")
    public void waitForDeploymentSuccess() { status.waitForDeploymentSuccess(); }
}