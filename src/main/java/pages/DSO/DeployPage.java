package pages.DSO;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import selfhealingHandler.ResilientLocator;
import utils.CommonMethods;
import utils.LoggerUtil;

public class DeployPage {

    private final Page page;

    public DeployPage(Page page) {
        this.page = page;
    }

    public Locator clickOnStageArrow() {
//        Locator stageArrow = page.locator("(//div[@class='pg-breadcrumb-item link']/descendant::button//*[local-name()='svg'])[2]");
        return new ResilientLocator(page, "Stage Arrow")
                .byXPath("(//div[@class='pg-breadcrumb-item link']/descendant::button//*[local-name()='svg'])[2]")
                .resolve();
//        return stageArrow;
    }


    public void selectPhase(String stage) {
        clickOnStageArrow().click(new Locator.ClickOptions().setForce(true));
        new ResilientLocator(page, "Select Stage "+ stage)
                .byXPath("//div[text()='" + stage + "']")
                .resolve().click();
    }

    public Locator muiIconThreeDots(String stage) {
        return new ResilientLocator(page, "Mui Icon Three Dots")
                .byXPath("//h3[text()='"+stage+"']/parent::div/parent::div/div//button/span")
                .resolve();
    }

    public Locator optionEditDetails() {
        return new ResilientLocator(page, "Edit Details")
                .byXPath("//a[text()='Edit Details']")
                .resolve();
    }

    public void EditStage(String stage) {
        muiIconThreeDots(stage).click();
        optionEditDetails().click();
    }

    public Locator dropdownPrivateSubnet() {
        return new ResilientLocator(page,"Dropdown Private Subnet")
                .byXPath("//label[text()='Private Subnet']/following-sibling::div[1]/div//i[text()='arrow_drop_down']")
                .resolve();
    }

    public Locator dropDownSelectText(String dropdownName)
    {
        return new ResilientLocator(page, "DropDown Select Text")
                .byXPath("//label[text()='"+dropdownName+"']/parent::div/following-sibling::div//i[text()='arrow_drop_down']")
                .byXPath("//label[text()='"+dropdownName+"']/parent::div//following-sibling::div//i[text()='arrow_drop_down']")
//                .byXPath("//label[text()='"+dropdownName+"']/following-sibling::div[1]/div//i[text()='arrow_drop_down']")
                .resolve();
    }

    public void removeTheValues(String value) {
        Locator locator = page.locator("//label[text()='"+value+"']//parent::div//following-sibling::div//i[text()='close']");
        while (locator.count() > 0) {
            locator.first().click();
        }
    }

    public void selectValueForDropdown(String dropdownName, String valueToSelect)
    {
        removeTheValues(dropdownName);
        page.waitForTimeout(2000);
        dropDownSelectText(dropdownName).click();
        Locator loc =  page.locator("//div[contains(text(),'Loading')]");
        if(loc.isVisible())
        {
            loc.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        }
        page.locator("//label[text()='"+valueToSelect+"']").click();
    }
    public void selectValueForDropdownDevelopmentMode(String valueToSelect) {
        removeTheValues("Deployment Mode");
        page.waitForTimeout(2000);
        dropDownSelectText("Deployment Mode").click();
        page.waitForTimeout(2000);
        page.locator("//label[text()='"+valueToSelect+"']").click();
    }

    public void selectValueForDropdownAccountCluster(String deploymentMode, String valueToSelect) {
        if(deploymentMode.equalsIgnoreCase("Kubernetes"))
        {
            removeTheValues("Kubernetes Clusters");
            page.waitForTimeout(2000);
            dropDownSelectText("Kubernetes Clusters").click();
            page.waitForTimeout(2000);
            page.locator("//label[text()='" + valueToSelect + "']").click();
        }
        else if (deploymentMode.equalsIgnoreCase("Docker") || deploymentMode.equalsIgnoreCase("TERRAFORM") || deploymentMode.equalsIgnoreCase("Serverless"))
        {
            removeTheValues("Cloud Account");
            page.waitForTimeout(2000);
            dropDownSelectText("Cloud Account").click();
            page.waitForTimeout(2000);
            page.locator("//label[text()='" + valueToSelect + "']").click();
        }
        else if(deploymentMode.equalsIgnoreCase("OPENSHIFT"))
        {
            removeTheValues("Openshift Clusters");
            page.waitForTimeout(2000);
            dropDownSelectText("Openshift Clusters").click();
            page.waitForTimeout(2000);
            page.locator("//label[text()='" + valueToSelect + "']").click();
        }
    }


    public void selectPrivateSubnet(String subnetName) {
        dropdownPrivateSubnet().click();
        page.waitForTimeout(2000);
        page.locator("//label[text()='"+subnetName+"']").click();
    }

    public Locator dropdownSecurityGroups() {
        return new ResilientLocator(page,"Dropdown Security Groups")
                .byXPath("//label[text()='Security Groups']/following-sibling::div[1]/div//i[text()='arrow_drop_down']")
                .resolve();
    }

    public void selectSecurityGroups(String subnetName) {
        removeTheValues("Security Groups");
        dropdownSecurityGroups().click();
        page.waitForTimeout(2000);
        page.locator("//label[text()='"+subnetName+"']").click();
    }



    public Locator linkConfigure(String value) {
        return new ResilientLocator(page, "Configure Link")
                .byXPath("//h3[text()='"+value+"']/parent::div/following-sibling::a/div[text()='Configure']")
                .resolve();
    }

    public Locator tabDeployBy(String value) {
        return new ResilientLocator(page, "Deploy By Tab")
                .byXPath("//div[text()='"+value+"']")
                .resolve();

    }

    public Locator btnAddTechnologyInCluster(String value) {
        return new ResilientLocator(page, "Deploy By Tab")
                .byXPath("//div[text()='"+value+"']/ancestor::div[@class='techstack-list-item']//button")
                .resolve();

    }

    public void clickConfigure(String dev) {
        linkConfigure(dev).click();
    }

    public void deployByClusters(String deployBy) {
        tabDeployBy(deployBy).click();
    }

    public void addTechnologyToCluster(String technology) {
        btnAddTechnologyInCluster(technology).click();
    }

    public Locator txtContextPath() {
        return new ResilientLocator(page, "Context Path")
                .byXPath("//input[@name='contextPath']")
                .byXPath("//input[@label='Context Path']")
                .resolve();
    }


    public void enterValueInDropdown(String dropdownName, String value) {
        ResilientLocator rl = new ResilientLocator(page, "Dropdown Select Value");
        rl.byXPath("//label[text()='"+dropdownName+"']/parent::div//following-sibling::div//input")
                .resolve()
                .fill(value);
        ResilientLocator rll = new ResilientLocator(page, "Enter Value");
            rll.byXPath("//label[contains(text(),'"+value+"')]")
                .resolve()
                .click();
    }

    public Locator threeDotOnTechCard(String techName) {
        return new ResilientLocator(page,"Click on three dots on Card")
                .byXPath("//div[text()='"+techName+"']/ancestor::div[@class='card new-card bg-white card-spacing-sm mt-3']//i[text()='more_horiz']")
                .resolve();
    }

    public Locator verifyCIIsCompleted(String techName) {
        return new ResilientLocator(page,"Click on three dots on Card")
                .byXPath("//div[text()='"+techName+"']/ancestor::div[@class='card new-card bg-white card-spacing-sm mt-3']//label[text()='CI']/preceding-sibling::i")
                .resolve();
    }

    public Locator verifyCDIsCompleted(String techName) {
        return new ResilientLocator(page,"Click on three dots on Card")
                .byXPath("//div[text()='"+techName+"']/ancestor::div[@class='card new-card bg-white card-spacing-sm mt-3']//label[text()='CI']/following-sibling::i")
                .resolve();
    }


    public void startCIPipeline(String techName, String selectValue) {
        threeDotOnTechCard(techName).click();
        page.waitForTimeout(2000);
        page.locator("//a[text()='"+selectValue+"']").click();
    }

    public Locator btnRefresh(){
        return new ResilientLocator(page, "Refresh Button")
                .byXPath("//button[text()='Refresh']")
                .resolve();
    }

    public Locator btnDeploy(){
        return new ResilientLocator(page,"Deploy Button")
                .byXPath("//div[@id='menu']//button[text()='Deploy']")
                .resolve();
    }

    public Locator btnBrowse(){
        return new ResilientLocator(page,"Browse Button")
                .byXPath("//div[@id='menu']//a[text()='Browse']")
                .resolve();
    }

    public void verifyCIPipelineIsCompleted(String techName) {
        boolean operationIsCompleted;
        page.waitForTimeout(2000);
        do {
            String status = verifyCIIsCompleted(techName).innerText();
            if (status.equalsIgnoreCase("check_circle")) {
                LoggerUtil.LOGGER.info("CI Pipeline is completed for the technology: {}", techName);
                operationIsCompleted = true;
            }
            else
            {
                if(CommonMethods.isElementPresent(btnRefresh()));
                {
                    btnRefresh().click();
                    page.waitForTimeout(15000);
                }
                operationIsCompleted = false;
            }
        } while (!operationIsCompleted);
    }

    public void verifyCDPipelineIsCompleted(String techName) {
        boolean operationIsCompleted;
        page.waitForTimeout(2000);
        do {
            String status = verifyCDIsCompleted(techName).innerText();
            if (status.equalsIgnoreCase("check_circle")) {
                LoggerUtil.LOGGER.info("CD Pipeline is completed for the technology: {}", techName);
                operationIsCompleted = true;
            }
            else
            {
                if(CommonMethods.isElementPresent(btnRefresh()));
                {
                    btnRefresh().click();
                    page.waitForTimeout(15000);
                }
                operationIsCompleted = false;
            }
        } while (!operationIsCompleted);
    }

    public Locator txtInstanceName() {
        return new ResilientLocator(page, "Docker Name")
                .byLabel("Name")
                .byPlaceholder("Name")
                .resolve();
    }

    public void openBrowseInNewTab(String expectedText) {
        Page newPage = page.context().waitForPage(() -> {btnBrowse().click();});
        newPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
        newPage.bringToFront();
        String actualText = newPage.locator("body").innerText();
        isTextPresentInBrowseTab(expectedText, actualText);
    }

    public void isTextPresentInBrowseTab(String expectedText, String actualText) {
//        Assert.assertEquals(expectedText, actualText, "Expected text is not present in the Browse tab.");
        Assert.assertTrue(actualText.contains(expectedText),"Validation completed...");
    }

    public Locator dropdownHardDiskSize() {
        return new ResilientLocator(page, "Hard Disk Size Select Value")
                .byXPath("//label[text()='Hard Disk Size']/parent::div/div//i[text()='arrow_drop_down']")
                .resolve();

    }

    public void selectHardDiskSize(String valueToSelect) {
        dropdownHardDiskSize().click();
        page.waitForTimeout(2000);
        page.locator("//label[text()='" + valueToSelect + "']").click();
    }

}
