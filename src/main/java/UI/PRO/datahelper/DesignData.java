package UI.PRO.datahelper;

import lombok.Data;
import java.util.Map;

@Data
public class DesignData {
    private java.util.List<ProductData.ProductDocumentData> designLinks;
    private String deleteConfirmationMessage;
    private String designDeleteComment;
    private String deletedMessage;
    private java.util.List<MandatoryCase> mandatoryCases;

    @Data
    public static class MandatoryCase {
        private String name;
        private String description;
        private java.util.List<String> expectedErrors;
    }

    private String editButton;
    private String saveButton;
    private String designId;

    private java.util.List<String> designScenarios;
    private int expectedDesignCount;
    private String sourceImageFingerprint;

    private String feature;
    private String category;
    private String source;
    private String name;
    private String description;

    private ProductData product;
    private FeatureData featureData;
    private String productName;
    private String featureName;
    private String designName;
    private String resultSheet;
    private String executionDataFile;
    private String designPhase;
    private String proceedButton;
    private String featureDetailsAction;
    private String goToProductButton;
    private String savedMessage;
    private int loadTimeoutMs;
    private int featureLookupTimeoutMs;
    private Map<String, String> validationMessages;
    private ProductData.ProductDocumentData designLink;
    private ProductData.ProductDocumentData designFile;
    private String fileTitle;
    private String downloadDirectory;
    private DefineData businessRequirement;
    private String businessRequirementName;
    private String definePhase;
    private String businessRequirementsTab;
    private String newBusinessRequirementButton;
    private String createNewRequirementButton;
    private String createRequirementButton;
    private String businessRequirementSavedMessage;
    private String workflowsTab;
    private String backButton;
    private String businessCaseLabel;
    private String viewFeatureDetailsButton;
    private Map<String, String> requirementDropdowns;
    private Map<String, String> requirementTextFields;
    private Map<String, String> additionalAttributes;
}
