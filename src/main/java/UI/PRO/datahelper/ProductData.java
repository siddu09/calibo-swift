package UI.PRO.datahelper;

import lombok.Data;

import java.util.List;

@Data
public class ProductData {
    private ProductDocumentData document;

    @Data
    public static class ProductDocumentData {
        private String menuAction;
        private String addDocumentButton;
        private String typeLabel;
        private String type;
        private String urlLabel;
        private String url;
        private String nameLabel;
        private String name;
        private String descriptionLabel;
        private String description;
        private String browseButton;
        private String filePath;
        private String contentType;
        private String addButton;
        private String successMessage;
        private String linkOpenMessage;
        private String downloadFailureMessage;
    }

    private PortfolioData portfolioToCreate;
    private PortfolioData portfolioOverviewUpdate;
    private java.util.Map<String, String> expectedPortfolioDetails;
    private java.util.Map<String, String> expectedPortfolioAuditObjects;

    private String teamName;
    private String productName;
    private String missingSearchProductNameMessage;
    private String savedSearchProductNameMismatchMessage;
    private ProductTeamAssessmentSearchData teamAssessmentSearch;

    @Data
    public static class ProductTeamAssessmentSearchData {
        private String searchPlaceholder;
        private String missingTeamNameMessage;
        private String savedTeamNameMismatchMessage;
        private String unexpectedTeamNameMessage;
        private String expectedResultStatus;
        private int loadTimeoutMs;
    }

    private ProductBaselinedSearchData baselinedAssessmentSearch;
    private ProductAssessmentPaginationData assessmentPagination;
    private TeamPaginationValidationData teamPaginationValidation;

    @Data
    public static class TeamPaginationValidationData {
        private String missingRecordsMessage;
        private String invalidRecordCountMessage;
        private String emptyRecordMessage;
    }


    @Data
    public static class ProductAssessmentPaginationData {
        private int pageNumber;
        private int initialPageSize;
        private int expandedPageSize;
        private int loadTimeoutMs;
        private String pageNotChangedMessage;
        private String emptyRecordMessage;
    }

    @Data
    public static class ProductBaselinedSearchData {
        private String baselinedAssessmentsTab;
        private String searchPlaceholder;
        private String productColumnLabel;
        private String missingProductNameMessage;
        private String savedProductNameMismatchMessage;
        private int loadTimeoutMs;
    }

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
    private String customFieldDateTimezone;
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

    private ProductSprintSetupData sprintSetup;
    private ProductMaturityAssessmentData maturityAssessment;
    private ProductAssessmentDiscardData assessmentDiscard;
    private ProductAssessmentDraftData assessmentDraft;
    private ProductCurrentAssessmentData currentAssessment;
    private MaturityAssessmentData baselineAssessment;

    @Data
    public static class ProductCurrentAssessmentData {
        private String backButton;
        private String currentAssessmentsLabel;
        private String expectedStatus;
        private int loadTimeoutMs;
    }
    private String assessmentName;
    private ProductAssessmentResponseData assessmentResponse;
    private ProductAssessmentCompletionData assessmentCompletion;

    @Data
    public static class ProductAssessmentCompletionData {
        private String submitButton;
        private String responsesSubmittedMessage;
        private String completedStatus;
        private String completionPercentage;
        private String viewDetailsButton;
    }

    @Data
    public static class ProductAssessmentResponseData {
        private String profileLabel;
        private String myAssessmentsLabel;
        private String searchByNamePlaceholder;
        private String startButton;
        private String question;
        private String answer;
        private String saveAndExitButton;
        private String responsesSavedMessage;
        private String notSubmittedStatus;
        private String resumeButton;
        private int loadTimeoutMs;
    }

    @Data
    public static class ProductAssessmentDraftData {
        private String backButton;
        private String currentAssessmentsLabel;
        private int loadTimeoutMs;
    }

    @Data
    public static class ProductAssessmentDiscardData {
        private String backButton;
        private String discardAction;
        private String confirmationMessage;
        private String yesButton;
        private String discardedMessage;
    }

    @Data
    public static class ProductSprintSetupData {
        private String editAction;
        private String othersTab;
        private java.util.Map<String, String> productDropdowns;
        private java.util.Map<String, String> boardDropdowns;
        private String teamName;
        private String teamCategory;
        private String teamAddedMessage;
    }


    @Data
    public static class ProductMaturityAssessmentData {
        private String menuAction;
        private String assessmentPath;
        private String newAssessmentButton;
        private String nameLabel;
        private String namePlaceholder;
        private String namePrefix;
        private String typeLabel;
        private String releaseLabel;
        private String associateWithLabel;
        private String associateWith;
        private String sprintLabel;
        private String sprintName;
        private String sprintUnavailableMessage;
        private String type;
        private String subCategory;
        private String respondent;
        private String responseDueDate;
        private String initiateButton;
        private String initiatedMessage;
    }

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
