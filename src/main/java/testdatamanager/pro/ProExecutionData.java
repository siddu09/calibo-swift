package testdatamanager.pro;

import java.util.ArrayList;
import java.util.List;

public class ProExecutionData {

    private String portfolioName;
    private String assessmentName;
    private String createdStoryId;
    private String productId;
    private String featureId;
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getFeatureId() { return featureId; }
    public void setFeatureId(String featureId) { this.featureId = featureId; }

    public String getAssessmentName() { return assessmentName; }

    public void setAssessmentName(String assessmentName) { this.assessmentName = assessmentName; }
    public String getCreatedStoryId() { return createdStoryId; }
    public void setCreatedStoryId(String createdStoryId) { this.createdStoryId = createdStoryId; }
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

    public ProductExecutionData getPrimaryProduct() {
        if (products == null || products.isEmpty()) {
            return null;
        }
        return products.get(0);
    }

    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }
}
