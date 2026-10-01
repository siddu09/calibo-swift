package UI.PRO.datahelper;

import lombok.Data;

import java.util.List;

@Data
public class ProductData {
    private PortfolioData portfolioToCreate;
    private PortfolioData portfolioOverviewUpdate;
    private java.util.Map<String, String> expectedPortfolioDetails;
    private java.util.Map<String, String> expectedPortfolioAuditObjects;

    private String product1Name;
    private String product2Name;
    private String dependenciesTab;
    private String dependentsTab;
    private String dependentOnTab;
    private String notificationsTab;
    private String dependencyPortfolioPlaceholder;
    private String dependencyProductPlaceholder;
    private String addDependencyButton;
    private String productSearchPlaceholder;
    private String dependentNotificationTemplate;
    private String unreadNotificationCount;
    private String markAsReadLabel;
    private String deleteNotificationLabel;
    private String distinctProductNamesMessage;
    private String portfolioName;
    private String missingPortfolioNameMessage;
    private String blankPortfolioNameMessage;
    private String portfolioNamePrefix;
    private String portfolioDescription;
    private String productType;
    private String createPageTitle;
    private String newProductButton;
    private String newPortfolioButton;
    private String noPortfolioOptionsText;
    private String portfolioCreatedMessage;
    private String productCreatedMessage;
    private String skipButton;
    private String saveButton;
    private String confirmationMessage;
    private String confirmButton;
    private String cancelButton;
    private String featurePrompt;
    private String featureChoice;
    private String resultSheet;
    private boolean publicProduct;
    private String publicLabel;
    private String ownerLabel;
    private String owner;
    private String priorityLabel;
    private String priority;
    private String overviewTab;
    private String customFieldsTab;
    private String milestonesTab;
    private String overviewLabel;
    private String overview;
    private String documentationLabel;
    private String documentationValue;
    private String businessGroupLabel;
    private java.util.Map<String, String> customFields;
    private java.util.Map<String, String> expectedOverviewFields;
    private String dynamicFieldNamePrefix;
    private String dynamicFieldValuePrefix;
    private String dynamicFieldName;
    private String dynamicFieldValue;
    private String documentationLinkText;
    private String documentationLinkUrl;
    private java.util.Map<String, String> milestones;
    private String title;
    private String description;
    private String businessGroup;
    private List<String> phases;
    private String teamsTab;
    private String allocationRoleLabel;
    private String addMemberTeamButton;
    private String addAllocationButton;
    private String releaseTrainName;
    private String releaseName;
    private List<ProductAllocationData> allocations;
    private ProductReleaseData productRelease;

    @Data
    public static class ProductAllocationData {
        private String category;
        private String name;
        private String startDate;
        private String endDate;
        private String allocation;
        private String comments;
        private String expectedAllocation;
        private String expectedPeriod;
        private String successMessage;
        private String role;
    }

    @Data
    public static class ProductReleaseData {
        private String trainNamePrefix;
        private String trainDescription;
        private String tagsLabel;
        private String tag;
        private String trainCreatedMessage;
        private String releaseNamePrefix;
        private String versionPrefix;
        private String releaseIdPrefix;
        private String objective;
        private int objectiveMaxLength;
        private String objectivePattern;
        private String objectiveValidationMessage;
        private String sprintName;
        private String sprintLabel;
        private String timelineLabel;
        private String timeline;
        private String releaseDateLabel;
        private String releaseDate;
        private String manager;
        private String type;
        private String impact;
        private String risk;
        private java.util.Map<String, String> releaseDropdowns;
        private String releaseCreatedMessage;
        private String releasesTab;
        private String joinReleaseButton;
        private String joinReleaseTitle;
        private String trainSelectLabel;
        private String releaseSelectLabel;
        private String selectButton;
        private java.util.Map<String, String> productReleaseFields;
        private java.util.Map<String, String> productReleaseDropdowns;
        private String saveButton;
        private String joinedMessage;
        private String leaveReleaseAction;
        private String leaveConfirmationTitle;
        private String leaveCommentsPlaceholder;
        private String leaveComments;
        private String leaveButton;
        private String leftMessage;
    }

    private String initialPriority;
    private String regionLabel;
    private String ownerSearch;
    private String detailsSavedMessage;
    private String dependencyDeletedMessage;
    private String dependencyDeleteConfirmation;
    private String dependencyPortfolioName;
    private String dependencyProductName;
    private String milestoneNamePrefix;
    private String milestoneName;
    private String milestoneTimeline;


    private List<String> phasesToRemove;
    private List<String> expectedPhases;
    private String phaseRemovalConfirmation;
    private String selectedPhaseClass;

    private List<String> mandatoryFieldErrors;
    private String mandatoryErrorClassPattern;
    private String productCreationUrlPattern;
    private String productCreationRoute;
    private String productCreationRequestMethod;
    private String unexpectedProductCreationMessage;
    private String creationPageChangedMessage;
    private String missingCorrectedSubmissionMessage;
    private String errorsNotClearedMessage;
    private String creationFormMissingMessage;

    private List<String> releaseMandatoryFieldErrors;
    private List<String> dependencyMandatoryFieldErrors;
    private List<String> memberTeamMandatoryFieldErrors;
    private String releaseBackButton;

    private String kpisTab;
    private String newKpiButton;
    private String kpiCreateButton;
    private List<String> kpiMandatoryFieldErrors;

    private String executionDataFile;
    private String executionSourceTestCase;
    private String sourceRowCountMessage;
    private String missingSourceProductMessage;
    private String missingSourcePortfolioMessage;
    private String ambiguousSourceProductMessage;
    private FeatureData featureToCreate;
    private String featuresTab;
    private String featureDeletedMessage;
    private String productDeletedMessage;
    private String featureDeleteReason;
    private String productDeleteReason;
    private String deleteAction;

}
