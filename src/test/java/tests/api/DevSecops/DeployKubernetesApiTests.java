package tests.api.DevSecops;

import api.DSO.building_blocks.KubernetesDeployment.KubernetesDeployment;
import base.apibase.BaseTest;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class DeployKubernetesApiTests extends BaseTest {
    @Test(groups = {"DevSecOps","KubernetesDeployment"})
    @Step("Deploying DevSecOps Technology Stack on AWS Kubernetes Cluster")
    public void deployKubernetes() {
        KubernetesDeployment deployment = new KubernetesDeployment(new LinkedHashMap<>());
        deployment.createStage();
        deployment.verifyCreatedStage();
        deployment.addNewTechnology();
    }
}