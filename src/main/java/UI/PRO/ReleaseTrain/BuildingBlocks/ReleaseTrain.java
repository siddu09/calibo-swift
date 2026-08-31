package UI.PRO.ReleaseTrain.BuildingBlocks;

import UI.PRO.datahelper.ReleaseTrainData;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import pages.LandingPage;
import pages.PRO.ReleaseTrain.NewReleaseTrainPage;
import pages.PRO.ReleaseTrain.ReleaseTrainPage;
import testdatamanager.pro.ProExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;

public class ReleaseTrain {
    private final Page page;
    private final ProExecutionData executionData;
    private final LandingPage landingPage;
    private final ReleaseTrainPage releaseTrainPage;
    private final NewReleaseTrainPage newReleaseTrainPage;
    public String releaseTrainName;

    public ReleaseTrain(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        this.landingPage = new LandingPage(page);
        this.releaseTrainPage=new ReleaseTrainPage(page);
        this.newReleaseTrainPage=new NewReleaseTrainPage(page);

    }

    public void navigateToReleaseTrainPage() {
        page.waitForTimeout(2000);
        LoggerUtil.LOGGER.info("========== Navigating to Product Portfolio Page ==========");

        landingPage.hoverOnNavigationBar().click();

        landingPage.clickOnOptions("Release Trains").click();
    }

    public void createReleaseTrain(ReleaseTrainData releaseTrainData) {
        page.waitForTimeout(2000);
        LoggerUtil.LOGGER.info("========== Release Train Creation Started ==========");

        releaseTrainPage.addNewReleaseTrain().click();

        Assert.assertTrue(CommonMethods.pageHeader(page, "New Release Train"));

        String name = releaseTrainData.getName();
        this.releaseTrainName = name != null && !name.isBlank()
                ? name
                : CommonMethods.generateUniqueTitle("Automation Release Train");
        executionData.setReleaseTrainName(this.releaseTrainName);

        newReleaseTrainPage.name().fill(this.releaseTrainName);

        String description = releaseTrainData.getDescription();
        newReleaseTrainPage.description().fill(description != null && !description.isBlank()
                ? description
                : "Automation Release Train Description");
        page.waitForTimeout(2000);

        CommonMethods.clickButton(page, "Create").click();

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info("========== Release Train Created ==========");
    }

    public void searchReleaseTrain() {
        LoggerUtil.LOGGER.info("========== Searching for Release Train ==========");

        releaseTrainPage.searchReleaseTrain().fill(this.releaseTrainName);

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info("========== Release Train Found ==========");
    }

    public void viewReleaseTrain(){

        LoggerUtil.LOGGER.info("========== Viewing Release Train ==========");

        releaseTrainPage.selectReleaseTrain(this.releaseTrainName).click();

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info("========== Release Train Viewed ==========");
    }

    public void addNewRelease(){

        LoggerUtil.LOGGER.info("========== Adding New Release ==========");

        releaseTrainPage.addNewRelease().click();

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info("========== New Release Added ==========");
    }
}
