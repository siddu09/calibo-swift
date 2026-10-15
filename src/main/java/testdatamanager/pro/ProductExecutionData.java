package testdatamanager.pro;

import java.util.ArrayList;
import java.util.List;

public class ProductExecutionData {

    private String productName;
    private boolean deleted;

    public boolean isDeleted() { return deleted; }

    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    private final List<FeatureExecutionData> features =
            new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<FeatureExecutionData> getFeatures() {
        return features;
    }
}
