package UI.DSO.helpers;

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

    @Step("Open the Portfolio")
    public void openPortfolio(DSOConstants data) {
        String portfolioName = resolvePropertyValue("portfolio_name", data.getPortfolioName());
        String portfolioSearchValue = resolvePropertyValue("portfolio_search_value", data.getPortfolioSearchValue());
        LoggerUtil.LOGGER.info("Test Case Name - "+data.getTestCaseName());
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
        String businessGroup = resolvePropertyValue("business_group", data.getBusinessGroup());

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
    }

    @Step("Create Feature")
    public void createFeature(DSOConstants data) {
        String featureOwner = resolvePropertyValue("feature_owner", data.getFeatureOwner());

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
    }

    @Step("Add Technology")
    public void addTechnology(DSOConstants data) {
        CommonMethods.clickButton(page, "New Technologies").click();
        CommonMethods.waitForElementToBeVisible(page, "Proceed");
        CommonMethods.clickButton(page, "Proceed").click();
        CommonMethods.waitForLoaderToDisappear(page);
        CommonMethods.search(page, data.getTechnology()).fill(data.getTechnology());

        AddTechnologiesPage addTechnologiesPage = new AddTechnologiesPage(page);
        addTechnologiesPage.selectTechnology(data.getTechnology()).click();
        CommonMethods.clickButton(page, "Add").click();
        page.waitForTimeout(2000);
        addTechnologiesPage.txtTechnologyTitle().fill(data.getTechnologyTitle());
        page.waitForTimeout(5000);
        addTechnologiesPage.selectGroup(data.getRepositoryGroup());
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
        page.waitForTimeout(2000);
        addTechnologiesPage.waitForTechnologyToBeAdded();
    }

    @Step("Deploy Code")
    public void deploy(DSOConstants data) {
        String deploymentMode = resolvePropertyValue("deploymentMode", data.getDeploymentMode());
        String accountCluster = resolvePropertyValue("accountCluster", data.getAccountCluster());

        DeployPage deployPage = new DeployPage(page);
        page.waitForTimeout(5000);

        deployPage.selectPhase("Deploy");
        CommonMethods.waitForElementToBeVisible(page, "Dev");
        deployPage.EditStage("Dev");
        deployPage.selectValueForDropdownDevelopmentMode(deploymentMode); // development
        page.waitForTimeout(5000);
        deployPage.selectValueForDropdownAccountCluster(deploymentMode,accountCluster);
        page.waitForTimeout(2000);
        deployPage.removeTheValues("Container Image Scanning Tool (Optional)");
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
        if(deploymentMode.equalsIgnoreCase("Docker")){
            addClusterForDocker(deployPage, data);
        }

        if(deploymentMode.equalsIgnoreCase("KUBERNETES")){
            addTechnologyStage(deployPage, data);
        }
//        CommonMethods.clickButton(page, "Save").click();
    }

    @Step("Run CICD")
    public void runCICD(DSOConstants data) {
        CommonMethods.waitForLoaderToDisappear(page);
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
        CommonMethods.clickButton(page, "Add Technologies").click();
        page.waitForTimeout(2000);
        deployPage.addTechnologyToCluster(data.getTechnology());
        deployPage.txtContextPath().fill(data.getContextPath());
        deployPage.selectValueForDropdown("Source Code Branch", data.getSourceCodeBranch());
        page.waitForTimeout(3000);
        deployPage.enterValueInDropdown("Namespace", data.getNamespace());
        page.waitForTimeout(3000);
        CommonMethods.clickButton(page, "Add").click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.waitForTimeout(2000);
    }

    @Step("Add Docker")
    public void addClusterForDocker(DeployPage deployPage, DSOConstants data) {
        CommonMethods.clickButton(page, "Add Instance").click();
        CommonMethods.clickButton(page, "Large").click();
        CommonMethods.clickButton(page, "Configure").click();
        deployPage.txtInstanceName().fill("Docker Instance");
        deployPage.selectHardDiskSize("50GB");
        page.waitForTimeout(2000);
        deployPage.selectPrivateSubnet("subnet-06141d9412abdd1d8 (QA Aws dev env-subnet-private)");
        page.waitForTimeout(2000);
//        deployPage.selectSecurityGroups("QA Aws dev env-security-group");
        CommonMethods.clickButton(page, "Save").click();
        CommonMethods.clickButton(page, "Add Technologies").click();
        page.waitForTimeout(2000);
        deployPage.addTechnologyToCluster(data.getTechnology());
        deployPage.txtContextPath().fill(data.getContextPath());
        page.waitForTimeout(3000);
        deployPage.selectValueForDropdown("Source Code Branch", "main");
        CommonMethods.clickButton(page, "Add").click();
    }

    @Step("Validate")
    public void validate(DSOConstants data) {
        page.waitForTimeout(10000);
        String technology = resolvePropertyValue("deploymentMode", data.getTechnology()).replace(" ","").replace("-","");
//        CommonMethods.clickButton(page, "View Details").click();
//        CommonMethods.waitForLoaderToDisappear(page);
//        CommonMethods.clickButton(page, "Browse").click();
        DeployPage deployPage = new DeployPage(page);
        String expectedText = getDSOProperty(technology);
        deployPage.openBrowseInNewTab(expectedText);
    }
}
