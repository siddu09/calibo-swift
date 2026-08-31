package testdatamanager.pro;

import java.util.ArrayList;
import java.util.List;

public class ProductExecutionData {

    private String productName;

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