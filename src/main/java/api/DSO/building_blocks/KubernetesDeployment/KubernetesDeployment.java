package api.DSO.building_blocks.KubernetesDeployment;

import io.qameta.allure.Step;

import api.DSO.helper.DeploymentStage.DeployStageValidationHelper;
import api.DSO.helper.DeploymentStage.DeploymentStageHelper;
import api.DSO.helper.Kubernetes.KubernetesHelper;
import api.DSO.helper.Pipeline.DeploymentPipelineHelper;
import api.DSO.helper.Validation.DeploymentValidationHelper;

import java.util.Map;

public final class KubernetesDeployment {
    private final DeploymentStageHelper stage;
    private final DeployStageValidationHelper stageValidation;
    private final KubernetesHelper kubernetes;
    private final DeploymentPipelineHelper pipeline;
    private final DeploymentValidationHelper validation;

    public KubernetesDeployment(Map<String, Object> state) {
        stage = new DeploymentStageHelper(state);
        stageValidation = new DeployStageValidationHelper(state);
        kubernetes = new KubernetesHelper(state);
        pipeline = new DeploymentPipelineHelper(state);
        validation = new DeploymentValidationHelper(state);
    }

    @Step("Add Technology")
    public void createStage() {
        stage.loadTestData();
        stage.addOrUpdateStage();
    }
    @Step("Deploy the code")
    public void verifyCreatedStage() {
        stageValidation.verifySetup();
        stageValidation.verifyCreatedStage();
    }

    @Step("Configure dev")
    public void addNewTechnology() {
        kubernetes.configureKubernetes();
        pipeline.runCiPipelines();
        pipeline.waitForCiSuccess();
        pipeline.deployPipelines();
        pipeline.waitForDeploymentSuccess();
        validation.verifyCicdLogs();
        validation.validateLiveUrl();
    }
}
