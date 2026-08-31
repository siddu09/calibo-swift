package testdatamanager.pro;

import java.util.ArrayList;
import java.util.List;

public class FeatureExecutionData {

    private String featureName;

    private final List<String> businessRequirementNames =
            new ArrayList<>();

    private final List<String> designNames =
            new ArrayList<>();

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public List<String> getBusinessRequirementNames() {
        return businessRequirementNames;
    }

    public List<String> getDesignNames() {
        return designNames;
    }
}