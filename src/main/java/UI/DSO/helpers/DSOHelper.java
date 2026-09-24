package UI.DSO.helpers;

import UI.DSO.buildingblocks.DSO_UpdateRunManager;
import com.microsoft.playwright.Page;
import configHandler.ConfigManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.DSO.DeployPage;
import pages.DSO.EditFeaturePage;
import pages.DSO.NewFeaturePage;
import pages.LandingPage;
import pages.PRO.Product.*;
import pages.PRO.ProductPortfolio.ProductPortfolioViewPage;
import utils.CommonMethods;
import utils.LoggerUtil;

import java.util.Locale;

public class DSOHelper {

    private final Page page;
    private final String cloud;
    String tenantName = ConfigManager.getUIProperty("tenantName.DSO");

    public DSOHelper(Page page) {
        this(page, System.getProperty("cloud", "aws"));
    }

    public DSOHelper(Page page, String cloud) {
        this.page = page;
        this.cloud = cloud == null || cloud.trim().isEmpty()
                ? "aws"
                : cloud.trim().toLowerCase(Locale.ROOT);
        try {
            ConfigManager.loadRunManagerDSO(this.cloud);
            ConfigManager.loadDSOTenantProperties(tenantName);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load DSO properties for cloud: " + this.cloud, e);
        }
    }

    public String getDSOProperty(String key) {
        String value = ConfigManager.getDSOProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing DSO property '" + key + "' for cloud '" + cloud + "'");
        }
        return value.trim();
    }

    private String resolvePropertyValue(String cloudKeySuffix, String fallbackValue) {
        String key = cloud + "_" + cloudKeySuffix;
        String value = ConfigManager.getDSOProperty(key);
        if ((value == null || value.trim().isEmpty()) && !"aws".equals(cloud)) {
            value = ConfigManager.getDSOProperty("aws_" + cloudKeySuffix);
        }
        if (value == null || value.trim().isEmpty()) {
            return fallbackValue;
        }
        return value.trim();
    }

    String filePath = resolvePropertyValue("file", "");
    String sheetName = resolvePropertyValue("sheet", "");

    @Step("Open the Portfolio")
    public void openPortfolio(DSOConstants data) {
        String portfolioName = resolvePropertyValue("portfolio_name", data.getPortfolioName());

//        String portfolioSearchValue = "DemoForAWSBitbucketAndOtherCombinations";//resolvePropertyValue("portfolio_search_value", data.getPortfolioSearchValue());

        String portfolioSearchValue = ConfigManager.getDSOTenantProperty("ProductPortfoliosName");
        LoggerUtil.LOGGER.info("Test Case Name - " + data.getTestCaseName());
        Allure.parameter("Test Case Name", data.getTestCaseName());

//        String text = getDSOProperty("text");
//        System.out.println("Text is ==  " + text);

        LandingPage landingPage = new LandingPage(page);
        page.waitForTimeout(5000);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.waitForElementToBeVisible(page, "Welcome");
        landingPage.hoverOnNavigationBar().click();
        landingPage.clickOnOptions("Product Portfolios").click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        CommonMethods.search(page, portfolioName).fill(portfolioSearchValue);
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        CommonMethods.selectSearchedItem(page, portfolioSearchValue).click();
        page.waitForTimeout(2000);
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Create Product")
    public void createProduct(DSOConstants data) {
        String businessGroup = ConfigManager.getDSOTenantProperty("BusinessGroup");

        ProductPortfolioViewPage productPortfolioViewPage = new ProductPortfolioViewPage(page);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.clickOnTab(page, "Products");
        productPortfolioViewPage.addProduct().click();

        ProductPage productPage = new ProductPage(page);
        page.waitForTimeout(2000);
        productPage.clickOnGetStartedButton("Productize");
        CommonMethods.waitForLoaderToDisappear(page);
        Assert.assertTrue(CommonMethods.sectionHeader(page, "Create Product"));

        ProductAddProjectDetailsPage productAddProjectDetailsPage = new ProductAddProjectDetailsPage(page);
        productAddProjectDetailsPage.title().fill(data.getProductName());
        productAddProjectDetailsPage.checkCircleDefine().click();
        productAddProjectDetailsPage.checkCircleDesign().click();
        productAddProjectDetailsPage.checkCircleDevelop().click();
        productAddProjectDetailsPage.description().fill(data.getProductName());
        productAddProjectDetailsPage.selectBusinessGroup(businessGroup);
        page.waitForTimeout(3000);
        productAddProjectDetailsPage.create().click();
        CommonMethods.waitForLoaderToDisappear(page);

        ProductAdditionalDetailsOverviewTabPage productAdditionalDetailsOverviewTabPage = new ProductAdditionalDetailsOverviewTabPage(page);
        page.waitForTimeout(3000);
        productAdditionalDetailsOverviewTabPage.save().click();
        CommonMethods.waitForLoaderToDisappear(page);
        Assert.assertTrue(CommonMethods.pageHeader(page, "Do you want to create a feature  for this product?"));
        CommonMethods.clickButton(page, "Yes").click();
        CommonMethods.waitForLoaderToDisappear(page);
        System.out.println("Product Name - " + data.getProductName());

        DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Product Name", data.getProductName());
    }

    @Step("Create Product For Policy Template")
    public void createProductForPolicyTemplate(DSOConstants data) {
        String businessGroup = ConfigManager.getDSOTenantProperty("BusinessGroup");

        ProductPortfolioViewPage productPortfolioViewPage = new ProductPortfolioViewPage(page);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.clickOnTab(page, "Products");
        productPortfolioViewPage.addProduct().click();

        ProductPage productPage = new ProductPage(page);
        page.waitForTimeout(2000);
        productPage.clickOnGetStartedButton("Productize");
        CommonMethods.waitForLoaderToDisappear(page);
        Assert.assertTrue(CommonMethods.sectionHeader(page, "Create Product"));

        ProductAddProjectDetailsPage productAddProjectDetailsPage = new ProductAddProjectDetailsPage(page);
        productAddProjectDetailsPage.title().fill(data.getProductName());
        productAddProjectDetailsPage.checkCircleDefine().click();
        productAddProjectDetailsPage.checkCircleDesign().click();
        productAddProjectDetailsPage.checkCircleDevelop().click();
        productAddProjectDetailsPage.description().fill(data.getProductName());
        productAddProjectDetailsPage.selectBusinessGroup(businessGroup);
        page.waitForTimeout(3000);
        productAddProjectDetailsPage.create().click();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Create Feature")
    public void createFeature(DSOConstants data) {
        String featureOwner = ConfigManager.getDSOTenantProperty("featureOwner");

        Assert.assertTrue(CommonMethods.pageHeader(page, "New Feature(s)"));
        NewFeaturePage newFeaturePage = new NewFeaturePage(page);
        newFeaturePage.textboxName().fill(data.getFeatureName());
        newFeaturePage.selectOwner(featureOwner);
        page.waitForTimeout(3000);
        CommonMethods.clickButton(page, "Create").click();
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.clickButton(page, "Go To Product").click();

        ProductFeatureTabPage productFeatureTabPage = new ProductFeatureTabPage(page);
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        productFeatureTabPage.editTheFeatureUsingThreeDots("View Details or Edit");

        EditFeaturePage editFeaturePage = new EditFeaturePage(page);
        page.waitForTimeout(3000);
        editFeaturePage.selectPhases("Define").click();
        editFeaturePage.selectPhases("Design").click();
        editFeaturePage.selectPhases("Develop").click();
        editFeaturePage.textboxDescription().fill(data.getFeatureName());
        page.waitForTimeout(3000);
        CommonMethods.clickButton(page, "Save").click();
        CommonMethods.waitForLoaderToDisappear(page);

        productFeatureTabPage.clickOnFeatureName(data.getFeatureName()).click();
        CommonMethods.waitForElementToBeVisible(page, "ID");
        page.waitForTimeout(3000);
        productFeatureTabPage.clickStages("Develop").click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);

        DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Feature Name", data.getFeatureName());
    }

    @Step("Add Technology")
    public void addTechnology(DSOConstants data) {
        String repositoryGroup = ConfigManager.getDSOTenantProperty("repositoryGroup");

        CommonMethods.clickButton(page, "New Technologies").click();
        if (page.locator("//button[text()='Proceed']").isVisible()) {
            CommonMethods.waitForElementToBeVisible(page, "Proceed");
            CommonMethods.clickButton(page, "Proceed").click();
        }
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.search(page, data.getTechnology()).fill(data.getTechnology());

        AddTechnologiesPage addTechnologiesPage = new AddTechnologiesPage(page);

        addTechnologiesPage.selectTechnology(data.getTechnology()).click();
        CommonMethods.clickButton(page, "Add").click();
        page.waitForTimeout(2000);
        addTechnologiesPage.txtTechnologyTitle().fill(data.getTechnologyTitle());
        page.waitForTimeout(5000);

        addTechnologiesPage.selectGroup(repositoryGroup);
        page.waitForTimeout(2000);

        addTechnologiesPage.txtRepositoryName().fill(data.getRepositoryName());
        addTechnologiesPage.verifyValidationIsCompleted();

        addTechnologiesPage.selectVisibility(data.getRepositoryVisibility());

        CommonMethods.waitForElementToBeVisible(page, "Save");
        page.waitForTimeout(3000);
        CommonMethods.clickButton(page, "Save").click();
        addTechnologiesPage.waitForSaveProgressDisappear();
        page.waitForTimeout(2000);
        CommonMethods.waitForLoaderToDisappear(page);
        if (page.locator("//h3[contains(text(),'User Mapping Missing')]").isVisible()) {
            CommonMethods.clickButton(page, "Got it").click();
        }
        page.waitForTimeout(2000);
        addTechnologiesPage.waitForTechnologyToBeAdded();

    }

    @Step("Deploy Code")
    public void editStage(DSOConstants data) {
        String deploymentMode = resolvePropertyValue("deploymentMode", data.getDeploymentMode());
        String accountCluster = resolvePropertyValue("accountCluster", data.getAccountCluster());
        String ciTool = resolvePropertyValue("ciTool", data.getCiTool());
        String artifactory = resolvePropertyValue("artifactory", data.getArtifactory());
        String codeAnalysis = resolvePropertyValue("codeAnalysis", data.getCodeAnalysis());
        String imageScan = resolvePropertyValue("codeAnalysis", data.getImageScan());
        String terraformConfiguration = resolvePropertyValue("codeAnalysis", data.getTerraformConfiguration());
        String agentObservability = resolvePropertyValue("codeAnalysis", data.getAgentObservability());

        DeployPage deployPage = new DeployPage(page);
        page.waitForTimeout(5000);

        deployPage.selectPhase("Deploy");
        CommonMethods.waitForElementToBeVisible(page, "Dev");
        deployPage.EditStage("Dev");
        deployPage.selectValueForDropdownDevelopmentMode(deploymentMode); // development
        page.waitForTimeout(5000);
        deployPage.selectValueForDropdownAccountCluster(deploymentMode, accountCluster);
        page.waitForTimeout(2000);
        if (deploymentMode.equalsIgnoreCase("TERRAFORM")) {
            deployPage.selectValueForDropdownAccountCluster("Terraform Configuration", terraformConfiguration);
        }
        deployPage.selectValueForDropdown("Continuous Integration Tool", ciTool);
        page.waitForTimeout(2000);
        if (!artifactory.isEmpty()) {
            deployPage.selectValueForDropdown("Artifact Management Tool", artifactory);
        }
        page.waitForTimeout(2000);
        if (!codeAnalysis.isEmpty()) {
            deployPage.enterValueInDropdown("Code Analysis Tool (Optional)", codeAnalysis);
        }
        page.waitForTimeout(2000);
        if (!imageScan.isEmpty()) {
            deployPage.enterValueInDropdown("Container Image Scanning Tool (Optional)", imageScan);
        }
        if (!agentObservability.isEmpty()) {
            deployPage.enterValueInDropdown("Agent Observability (Optional)", agentObservability);
        }
//        deployPage.removeTheValues("Container Image Scanning Tool (Optional)");
        CommonMethods.clickButton(page, "Save").click();
        CommonMethods.verifyValidationIsCompleted(page);
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Configure Dev")
    public void configureDevStage(DSOConstants data) {
        String deploymentMode = resolvePropertyValue("deploymentMode", data.getDeploymentMode());
        DeployPage deployPage = new DeployPage(page);
        page.waitForTimeout(2000);
        deployPage.clickConfigure("Dev");
        page.waitForTimeout(3000);
        if (deploymentMode.equalsIgnoreCase("Docker")) {
            addClusterForDocker(deployPage, data);
        }
        if (deploymentMode.equalsIgnoreCase("KUBERNETES")) {
            addTechnologyStage(deployPage, data);
        }
//        CommonMethods.clickButton(page, "Save").click();
    }

    @Step("Run CICD")
    public void runCICD(DSOConstants data) {
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(3000);
        DeployPage deployPage = new DeployPage(page);
        deployPage.startCIPipeline(data.getTechnology(), "Run CI Pipeline");
        CommonMethods.waitForLoaderToDisappear(page);


        deployPage.verifyCIPipelineIsCompleted(data.getTechnology());

        page.waitForTimeout(2000);
        deployPage.startCIPipeline(data.getTechnology(), "Deploy");
        page.waitForTimeout(2000);
        deployPage.btnDeploy().click();
        CommonMethods.waitForLoaderToDisappear(page);
        deployPage.verifyCDPipelineIsCompleted(data.getTechnology());
        page.waitForTimeout(2000);
        CommonMethods.clickButton(page, "View Details").click();
        CommonMethods.waitForLoaderToDisappear(page);
//        CommonMethods.clickButton(page, "Browse").click();
    }

    @Step("Add Technology In stage")
    public void addTechnologyStage(DeployPage deployPage, DSOConstants data) {
        String imageScan = resolvePropertyValue("codeAnalysis", data.getImageScan());
        CommonMethods.clickButton(page, "Add Technologies").click();
        page.waitForTimeout(2000);
        deployPage.addTechnologyToCluster(data.getTechnology());
        deployPage.txtContextPath().fill(data.getContextPath());
        deployPage.selectValueForDropdown("Source Code Branch", data.getSourceCodeBranch());
        page.waitForTimeout(3000);

        if (!imageScan.isEmpty()) {
            deployPage.txtProject().fill("ImageScan");
            deployPage.waitForProgressToComplete();
            page.waitForTimeout(1000);
            deployPage.selectValueForDropdown("Severity", "Medium");
        }
        deployPage.enterValueInDropdown("Namespace", data.getNamespace());
        page.waitForTimeout(3000);
                CommonMethods.clickButton(page, "Add").click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
    }

    @Step("Add Docker")
    public void addClusterForDocker(DeployPage deployPage, DSOConstants data) {
        String imageScan = resolvePropertyValue("codeAnalysis", data.getImageScan());
        CommonMethods.clickButton(page, "Add Instance").click();
        CommonMethods.clickButton(page, "Large").click();
        page.waitForTimeout(3000);
//        CommonMethods.clickButton(page, "Configure").click();
        page.waitForTimeout(3000);
        deployPage.txtInstanceName().fill("Docker Instance");
        page.waitForTimeout(2000);
        deployPage.selectHardDiskSize("50GB");
        page.waitForTimeout(2000);
        deployPage.selectPrivateSubnet(ConfigManager.getDSOTenantProperty("PrivateSubnet"));
        page.waitForTimeout(2000);
//        deployPage.selectSecurityGroups("QA Aws dev env-security-group");
        CommonMethods.clickButton(page, "Save").click();
        CommonMethods.clickButton(page, "Add Technologies").click();
        page.waitForTimeout(2000);
        deployPage.addTechnologyToCluster(data.getTechnology());
        deployPage.txtContextPath().fill(data.getContextPath());
        page.waitForTimeout(3000);
        deployPage.selectValueForDropdown("Source Code Branch", "main");

        if (!imageScan.isEmpty()) {
            deployPage.txtProject().fill("ImageScan");
            deployPage.waitForProgressToComplete();
            page.waitForTimeout(1000);
            deployPage.selectValueForDropdown("Severity", "Medium");
        }
        CommonMethods.clickButton(page, "Add").click();
    }

    @Step("Validate the URl")
    public void validate(DSOConstants data) {
        page.waitForTimeout(10000);
        String technology = resolvePropertyValue("deploymentMode", data.getTechnology()).replace(" ", "").replace("-", "");
        DeployPage deployPage = new DeployPage(page);
        String expectedText = DSOTechnologyConstants.getValue(technology);
        if (deployPage.openBrowseInNewTab(expectedText, technology)) {
            DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Validation", "Completed");
        } else {
            DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Validation", "InCompleted");
            Assert.fail("Validation failed for technology: " + data.getTechnology());
        }
    }

    @Step("Add Policy Template")
    public void addPolicyTemplate(DSOConstants data) {
        String policyTemplate = resolvePropertyValue("policyTemplate", data.getPolicyTemplate());
        DeployPage deployPage = new DeployPage(page);
        page.waitForTimeout(2000);
        CommonMethods.clickOnTab(page, "Others");
        CommonMethods.waitForLoaderToDisappear(page);
        deployPage.selectValueForDropdown("Policy Template (Optional)", policyTemplate);
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(6000);

        ProductAdditionalDetailsOverviewTabPage productAdditionalDetailsOverviewTabPage = new ProductAdditionalDetailsOverviewTabPage(page);
        productAdditionalDetailsOverviewTabPage.save().click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(5000);
        Assert.assertTrue(CommonMethods.pageHeader(page, "Do you want to create a feature  for this product?"));
        CommonMethods.clickButton(page, "Yes").click();
        CommonMethods.waitForLoaderToDisappear(page);
        System.out.println("Product Name - " + data.getProductName());
        DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Product Name", data.getProductName());
    }

    @Step("Run CI")
    public void runCI(DSOConstants data) {
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(3000);
        DeployPage deployPage = new DeployPage(page);
        deployPage.startCIPipeline(data.getTechnology(), "Run CI Pipeline");
        CommonMethods.waitForLoaderToDisappear(page);
        boolean flag = deployPage.checkIfCIIsStarted(data.getTechnology());
        if (flag) {
            DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Stage 1", "Completed");
        } else {
            DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Stage 1", "Not Completed");
        }
    }

    @Step("Run CD")
    public void runCD(DSOConstants data) {
        LoggerUtil.LOGGER.info("Test Case Name - " + data.getTestCaseName());
        Allure.parameter("Test Case Name", data.getTestCaseName());
        DeployPage deployPage = new DeployPage(page);

        String testCaseName = data.getTestCaseName();
        String productName = DSO_UpdateRunManager.getCellUsingFillo(filePath, sheetName, testCaseName, "Product Name");
        String featureName = DSO_UpdateRunManager.getCellUsingFillo(filePath, sheetName, testCaseName, "Feature Name");

        LandingPage landingPage = new LandingPage(page);
        page.waitForTimeout(5000);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.waitForElementToBeVisible(page, "Welcome");
        landingPage.hoverOnNavigationBar().click();
        landingPage.clickOnOptions("Products").click();
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.clickOnTab(page, "All Products");
        CommonMethods.waitForLoaderToDisappear(page);

        CommonMethods.search(page, productName).fill(productName);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.selectSearchedItem(page, productName).click();
        CommonMethods.waitForLoaderToDisappear(page);

        CommonMethods.clickOnTab(page, "Features");
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.selectSearchedItem(page, featureName).click();
        CommonMethods.waitForLoaderToDisappear(page);

        ProductFeatureTabPage productFeatureTabPage = new ProductFeatureTabPage(page);
        page.waitForTimeout(2000);

        productFeatureTabPage.clickStages("Deploy").click();
        CommonMethods.waitForElementToBeVisible(page, "Dev");
        CommonMethods.waitForLoaderToDisappear(page);
        if(page.locator("//div[text()='View Details']").isVisible())
        {
            CommonMethods.clickButton(page, "View Details").click();
        }
        else if (page.locator("//div[text()='Configure']").isVisible())
        {
            CommonMethods.clickButton(page, "Configure").click();
        }


        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        boolean flagCI = deployPage.checkIfCIIsCompleted(data.getTechnology());
        if (flagCI) {
            deployPage.startCIPipeline(data.getTechnology(), "Deploy");
            page.waitForTimeout(2000);
            deployPage.btnDeploy().click();
            CommonMethods.waitForLoaderToDisappear(page);
            boolean flag = deployPage.checkIfCDIsStarted(data.getTechnology());
            if (flag) {
                DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Stage 2", "Completed");
            }
        }
        else {
            DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Stage 2", "Not Completed");
            Assert.fail("There may be some issue in CI Pipeline...");
        }

    }

    @Step("Stage 3 - For Validation")
    public void validateForStage3(DSOConstants data) {
        LoggerUtil.LOGGER.info("Test Case Name - " + data.getTestCaseName());
        Allure.parameter("Test Case Name", data.getTestCaseName());
        DeployPage deployPage = new DeployPage(page);
        String testCaseName = data.getTestCaseName();
        String productName = DSO_UpdateRunManager.getCellUsingFillo(filePath, sheetName, testCaseName, "Product Name");
        String featureName = DSO_UpdateRunManager.getCellUsingFillo(filePath, sheetName, testCaseName, "Feature Name");

        LandingPage landingPage = new LandingPage(page);
        page.waitForTimeout(5000);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.waitForElementToBeVisible(page, "Welcome");
        landingPage.hoverOnNavigationBar().click();
        landingPage.clickOnOptions("Products").click();
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.clickOnTab(page, "All Products");
        CommonMethods.waitForLoaderToDisappear(page);

        CommonMethods.search(page, productName).fill(productName);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.selectSearchedItem(page, productName).click();
        CommonMethods.waitForLoaderToDisappear(page);

        CommonMethods.clickOnTab(page, "Features");
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.selectSearchedItem(page, featureName).click();
        CommonMethods.waitForLoaderToDisappear(page);

        ProductFeatureTabPage productFeatureTabPage = new ProductFeatureTabPage(page);
        page.waitForTimeout(2000);
        productFeatureTabPage.clickStages("Deploy").click();
        CommonMethods.waitForElementToBeVisible(page, "Dev");
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.clickButton(page, "View Details").click();
        CommonMethods.waitForLoaderToDisappear(page);
        boolean flagCD = deployPage.checkIfCDIsCompleted(data.getTechnology());
        if (flagCD) {
            page.waitForTimeout(2000);
            CommonMethods.clickButton(page, "View Details").click();
            CommonMethods.waitForLoaderToDisappear(page);
        }
    }

    @Step("Delete Product and Feature")
    public void deleteProduct(DSOConstants data) {
        if (data.getValidation().equalsIgnoreCase("Completed")) {
            String testCaseName = data.getTestCaseName();
            String productName = DSO_UpdateRunManager.getCellUsingFillo(filePath, sheetName, testCaseName, "Product Name");

            LandingPage landingPage = new LandingPage(page);
            page.waitForTimeout(5000);
            CommonMethods.waitForLoaderToDisappear(page);
            CommonMethods.waitForElementToBeVisible(page, "Welcome");
            landingPage.hoverOnNavigationBar().click();
            landingPage.clickOnOptions("Products").click();
            CommonMethods.waitForLoaderToDisappear(page);
            CommonMethods.clickOnTab(page, "All Products");
            CommonMethods.waitForLoaderToDisappear(page);

            CommonMethods.search(page, productName).fill(productName);
            CommonMethods.waitForLoaderToDisappear(page);
            CommonMethods.selectSearchedItem(page, productName).click();
            CommonMethods.waitForLoaderToDisappear(page);

            ProductPage productPage = new ProductPage(page);
            page.waitForTimeout(2000);
            productPage.clickOnThreeDot().click();
            CommonMethods.waitForLoaderToDisappear(page);
            CommonMethods.clickButton(page, "Delete Product").click();
            CommonMethods.waitForLoaderToDisappear(page);

            if (CommonMethods.isElementPresent(productPage.textFeature())) {
                productPage.selectAllCheckBoxes();
                CommonMethods.clickButton(page, "Delete Resource Permanently").click();
                CommonMethods.waitForLoaderToDisappear(page);
                CommonMethods.clickButton(page, "Yes").click();
                CommonMethods.waitForLoaderToDisappear(page);
                productPage.checkDeletionToComplete();
                CommonMethods.clickButton(page, "Proceed with Product Deletion").click();
                page.waitForTimeout(3000);
            }
            productPage.enterComments().fill("Yes");
            CommonMethods.clickButton(page, "Delete Product").click();
            CommonMethods.waitForLoaderToDisappear(page);
            DSO_UpdateRunManager.updateCellUsingFillo(filePath, sheetName, data.getTestCaseName(), "Deletion", "Completed");
        }
    }

    public void retrieve(DSOConstants data) {
        LandingPage landingPage = new LandingPage(page);
        page.waitForTimeout(5000);
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.waitForElementToBeVisible(page, "Welcome");
        landingPage.hoverOnNavigationBar().click();
        landingPage.clickOnOptions("Products").click();
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.clickOnTab(page, "All Products");
        CommonMethods.waitForLoaderToDisappear(page);

        CommonMethods.search(page, "DemoProduct").fill("DemoProduct");
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.selectSearchedItem(page, "DemoProduct").click();
        CommonMethods.waitForLoaderToDisappear(page);

        CommonMethods.clickOnTab(page, "Features");
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.selectSearchedItem(page, "DemoFeature").click();
        CommonMethods.waitForLoaderToDisappear(page);
        ProductFeatureTabPage productFeatureTabPage = new ProductFeatureTabPage(page);
        productFeatureTabPage.clickStages("Develop").click();

        CommonMethods.clickButton(page, "New Technologies").click();
        if (page.locator("//button[text()='Proceed']").isVisible()) {
            CommonMethods.waitForElementToBeVisible(page, "Proceed");
            CommonMethods.clickButton(page, "Proceed").click();
        }
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
        CommonMethods.clickOnTab(page, "API");
        page.waitForTimeout(2000);
        AddTechnologiesPage addTechnologiesPage = new AddTechnologiesPage(page);
        addTechnologiesPage.extractTheNamesOfTechnology();
        page.waitForTimeout(2000);
        CommonMethods.clickOnTab(page, "Web App");
        page.waitForTimeout(2000);
        addTechnologiesPage.extractTheNamesOfTechnology();


    }
}
