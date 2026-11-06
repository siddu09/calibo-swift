package UI.PRO.datahelper;

import java.util.Map;
import lombok.Data;

@Data
public class DefineData {

    private String feature;
    private String section;
    private String type;
    private String title;
    private String description;
    private String userName;
    private String customerName;
    private String priority;
    private String impact;
    private String status;
    private String source;

    private String scenarioName;
    private String existingStoryId;
    private String storySearchPlaceholder;
    private String addStoryButton;
    private String addedStoryMessage;
    private String fixVersionMessage;
    private Map<String, String> expectedStoryDetails;
    private String productScenario;
    private ProductDetailsData productDetails;
    private String featureName;
    private FeatureData featureData;
    private String featureDetailsAction;
    private String goToProductButton;
    private int featureLookupTimeoutMs;
    private String definePhase;
    private String userStoriesTab;
    private String newStoryButton;
    private String storyType;
    private String summary;
    private String storyDescription;
    private String componentFieldLabel;
    private String componentsTab;
    private String componentSearch;
    private String componentName;
    private String reporterFieldLabel;
    private String reporterName;
    private String sprintFieldLabel;
    private String sprintName;
    private String createButton;
    private String createdStoryMessagePattern;
    private String storyIdPattern;
    private String invalidStoryMessage;
    private String userCredentialKey;
    private String passwordCredentialKey;
    private int pageTimeoutMs;
    private String executionDataFile;
    private String resultSheet;
    private String resultColumn;
    private Map<String, String> validationMessages;

    @Data
    public static class ProductDetailsData {
        private String othersTab;
        private String productCodeLabel;
        private String productCode;
        private Map<String, String> dropdowns;
        private String saveButton;
        private String savedMessage;
    }
}
