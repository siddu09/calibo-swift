package testdatamanager.pro;

import java.util.ArrayList;
import java.util.List;

public class ProExecutionData {

    private String portfolioName;
    private Boolean publicPortfolio;
    private String releaseTrainName;
    private String releaseName;

    private final List<ProductExecutionData> products =
            new ArrayList<>();

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public Boolean getPublicPortfolio() {
        return publicPortfolio;
    }

    public void setPublicPortfolio(boolean publicPortfolio) {
        this.publicPortfolio = publicPortfolio;
    }

    public String getReleaseTrainName() {
        return releaseTrainName;
    }

    public void setReleaseTrainName(String releaseTrainName) {
        this.releaseTrainName = releaseTrainName;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public List<ProductExecutionData> getProducts() {
        return products;
    }
}
