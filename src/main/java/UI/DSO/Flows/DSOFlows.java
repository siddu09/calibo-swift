package UI.DSO.Flows;

import UI.DSO.helpers.DSOHelper;
import UI.DSO.helpers.DSOConstants;
import UI.E2E.LoginBuildingBlock;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.testng.Assert;


public class DSOFlows {

    private final LoginBuildingBlock loginBuildingBlock;
    private final DSOHelper devSecOpsHelper;

    public DSOFlows(Page page, String cloud) {
        this.loginBuildingBlock = new LoginBuildingBlock(page);
        this.devSecOpsHelper = new DSOHelper(page, cloud);
    }
    @Step("Running AWS Combination Flow with provided test data")
    public void runAwsCombinationFlow(DSOConstants data) {
        loginBuildingBlock.login();
        devSecOpsHelper.openPortfolio(data);
        devSecOpsHelper.createProductForPolicyTemplate(data);
        devSecOpsHelper.addPolicyTemplate(data);
        devSecOpsHelper.createFeature(data);
        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.editStage(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }

    @Step("Running AWS Combination Flow with provided test data")
    public void runAwsCombinationFlowForE2E(DSOConstants data) {

        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.editStage(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }


    @Step("Running Azure Combination Flow with provided test data")
    public void runAzureCombinationFlow(DSOConstants data) {
        loginBuildingBlock.login();
        devSecOpsHelper.openPortfolio(data);
        devSecOpsHelper.createProduct(data);
        devSecOpsHelper.createFeature(data);
        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.editStage(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }

    @Step("Running GCP Combination Flow with provided test data")
    public void runGCPCombinationFlow(DSOConstants data) {
        loginBuildingBlock.login();
        devSecOpsHelper.openPortfolio(data);
        devSecOpsHelper.createProduct(data);
        devSecOpsHelper.createFeature(data);
        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.editStage(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }


    public void runDSO_E2E_Flow(DSOConstants data) {
        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.editStage(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }

    @Step("Running Stage 1")
    public void stage1(DSOConstants data) {
        loginBuildingBlock.login();
        devSecOpsHelper.openPortfolio(data);
        devSecOpsHelper.createProductForPolicyTemplate(data);
        devSecOpsHelper.addPolicyTemplate(data);
        devSecOpsHelper.createFeature(data);
        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.editStage(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCI(data);
    }

    @Step("Running Stage 2")
    public void stage2(DSOConstants data) {
        if(data.getStage1Status().equalsIgnoreCase("Completed")) {
            loginBuildingBlock.login();
            devSecOpsHelper.runCD(data);
        }
        else
        {
            Assert.assertTrue(false,"Stage 1 is not completed...");
        }
    }

    @Step("Running Stage 3")
    public void stage3(DSOConstants data) {
        if(data.getStage2Status().equalsIgnoreCase("Completed")) {
            loginBuildingBlock.login();
            devSecOpsHelper.validateForStage3(data);
            devSecOpsHelper.validate(data);
        }
        else
        {
            Assert.assertTrue(false,"Stage 2 is not completed...");
        }
    }

    @Step("Running Deletion Of Product And Feature")
    public void deletionOfProductAndFeature(DSOConstants data) {
        loginBuildingBlock.login();
        devSecOpsHelper.deleteProduct(data);
    }

    public void retrieveALLTechnologyForAPIAndWebApp(DSOConstants data) {
        loginBuildingBlock.login();
        devSecOpsHelper.retrieve(data);
    }
}
