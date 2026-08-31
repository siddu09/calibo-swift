package UI.PRO.ReleaseTrain.Flows;

import UI.PRO.ProductPortfolio.BuildingBlocks.ProductPortfolioBuildingBlock;

import UI.PRO.ProductPortfolio.validations.PortfolioValidation;
import UI.PRO.ReleaseTrain.BuildingBlocks.Release;
import UI.PRO.ReleaseTrain.BuildingBlocks.ReleaseTrain;
import UI.PRO.datahelper.PortfolioData;
import UI.PRO.CommonProValidations.ProValidation;
import UI.PRO.datahelper.ReleaseTrainData;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;

public class ReleaseTrainFlows {

    private final ProductPortfolioBuildingBlock productPortfolioBuildingBlock;
    private final ProExecutionData executionData;
    private final ReleaseTrain releaseTrain;
    private final Release release;
    private final Page page;


    public ReleaseTrainFlows(Page page, ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        productPortfolioBuildingBlock = new ProductPortfolioBuildingBlock(page, executionData);
        releaseTrain = new ReleaseTrain(page, executionData);
        release = new Release(page, executionData);
    }

    @Step("Create release train and add release")
    public void createReleaseTrainandAddRelease(ReleaseTrainData releaseTrainData) {

        releaseTrain.navigateToReleaseTrainPage();
        releaseTrain.createReleaseTrain(releaseTrainData);
        releaseTrain.searchReleaseTrain();
        releaseTrain.viewReleaseTrain();
        releaseTrain.addNewRelease();

        ReleaseTrainData.Release firstRelease = releaseTrainData != null && releaseTrainData.getReleases() != null
                && !releaseTrainData.getReleases().isEmpty()
                ? releaseTrainData.getReleases().get(0)
                : null;
        release.createRelease(firstRelease);

    }




}