package UI.PRO.ReleaseTrain.BuildingBlocks;

import UI.PRO.datahelper.ReleaseTrainData;
import com.microsoft.playwright.Page;
import pages.LandingPage;
import pages.PRO.ReleaseTrain.NewReleasePage;
import pages.PRO.ReleaseTrain.ReleaseTrainPage;
import testdatamanager.pro.ProExecutionData;
import utils.CommonMethods;

public class Release {

    private final Page page;
    private final ProExecutionData executionData;
    private final LandingPage landingPage;
    private final ReleaseTrainPage releaseTrainPage;
    private final NewReleasePage newReleasePage;
    public String releaseName;

    public Release(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        this.landingPage = new LandingPage(page);
        this.releaseTrainPage=new ReleaseTrainPage(page);
        this.newReleasePage=new NewReleasePage(page);

    }

    public void createRelease(ReleaseTrainData.Release releaseData) {
        this.releaseName = releaseData != null && releaseData.getReleaseName() != null && !releaseData.getReleaseName().isBlank()
                ? releaseData.getReleaseName()
                : CommonMethods.generateUniqueTitle("Automation Release");
        executionData.setReleaseName(this.releaseName);

        newReleasePage.name().fill(this.releaseName);
        newReleasePage.version().fill(releaseData != null && releaseData.getVersion() != null ? releaseData.getVersion() : "1.0.0");
        newReleasePage.releaseId().fill(releaseData != null && releaseData.getReleaseId() != null ? releaseData.getReleaseId() : "AUT-REL-001");
        newReleasePage.releaseObjective().fill(releaseData != null && releaseData.getReleaseObjective() != null ? releaseData.getReleaseObjective() : "Automation Release Objective");
        newReleasePage.selectDropDown("Release Manager(s)", releaseData != null && releaseData.getReleaseManager() != null ? releaseData.getReleaseManager() : "API Automation");
        newReleasePage.selectDropDown("Release Type", releaseData != null && releaseData.getReleaseType() != null ? releaseData.getReleaseType() : "Hotfix");
        newReleasePage.selectDropDown("Impact", releaseData != null && releaseData.getImpact() != null ? releaseData.getImpact() : "High");
        newReleasePage.selectDropDown("Risk", releaseData != null && releaseData.getRisk() != null ? releaseData.getRisk() : "Medium");
        newReleasePage.create().click();
    }



}
