package UI.DSO.Flows;

import UI.DSO.helpers.DSOHelper;
import UI.DSO.helpers.DSOConstants;
import UI.E2E.LoginBuildingBlock;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;


public class DSOFlows {

    private final LoginBuildingBlock loginBuildingBlock;
    private final DSOHelper devSecOpsHelper;

    public DSOFlows(Page page) {
        this(page, System.getProperty("cloud", "aws"));
    }

    public DSOFlows(Page page, String cloud) {
        this.loginBuildingBlock = new LoginBuildingBlock(page);
        this.devSecOpsHelper = new DSOHelper(page, cloud);
    }
    @Step("Running AWS Combination Flow with provided test data")
    public void runAwsCombinationFlow(DSOConstants data) {
        loginBuildingBlock.login();
        devSecOpsHelper.openPortfolio(data);
        devSecOpsHelper.createProduct(data);
        devSecOpsHelper.createFeature(data);
        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.deploy(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }

    @Step("Running AWS Combination Flow with provided test data")
    public void runAwsCombinationFlowForE2E(DSOConstants data) {

        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.deploy(data);
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
        devSecOpsHelper.deploy(data);
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
        devSecOpsHelper.deploy(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }

    public void runDSO_E2E_Flow(DSOConstants data) {
        devSecOpsHelper.addTechnology(data);
        devSecOpsHelper.deploy(data);
        devSecOpsHelper.configureDevStage(data);
        devSecOpsHelper.runCICD(data);
        devSecOpsHelper.validate(data);
    }
}
