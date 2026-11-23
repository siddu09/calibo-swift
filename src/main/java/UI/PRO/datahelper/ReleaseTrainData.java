package UI.PRO.datahelper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseTrainData {

    @JsonProperty("releaseTrain")
    private ReleaseTrain releaseTrain;

    private String executionDataFile;
    private String resultSheet;
    private String releaseTrainName;
    private String releaseName;
    private String releaseVersion;
    private String releaseId;
    private String tagsLabel;
    private String tag;
    private String trainCreatedMessage;
    private String releaseCreatedMessage;
    private String viewDetailsButton;
    private String detailsTab;
    private String releaseIdLabel;
    private String versionLabel;
    private String releaseStatusLabel;
    private Map<String, String> releaseFields;
    private Map<String, String> releaseDropdowns;
    private Map<String, String> expectedReleaseDetails;
    private String scenarioName;
    private int expectedReleaseCount;
    private List<Release> createdReleases;
    private Map<String, String> validationMessages;
    private String originalReleaseName;
    private String updatedReleaseName;
    private ReleaseEditData releaseUpdate;
    private String originalReleaseTrainName;
    private String updatedReleaseTrainName;
    private ReleaseTrainEditData releaseTrainUpdate;
    private ReleaseDeleteData releaseDeletion;
    private ReleaseDeleteData releaseTrainDeletion;
    private String noReleasesMessage;
    private String createPageTitle;
    private List<String> mandatoryFieldErrors;
    private Map<String, String> mandatoryReleaseFieldErrors;
    private String productsTab;
    private String portfolioName;
    private String productName;
    private int expectedProductCount;
    private FeatureData featureData;
    private String featureReleaseLabel;
    private String featureReleaseUpdatedMessage;
    private String featureReleaseFixVersionMessage;
    private Map<String, String> expectedFeatureReleaseDetails;
    private String graphDependentOnLabel;
    private String graphDependentReleasesLabel;
    private String dependencyGraphTab;
    private String dependencyGraphExcludedDateRange;
    private String dependencyGraphDateRange;
    private ReleaseDependencyData releaseDependency;

    @Data
    public static class ReleaseDependencyData {
        private String tab;
        private String dependentOn;
        private String trainField;
        private String releaseField;
        private String productField;
        private String addButton;
        private String addedMessage;
    }

    private ReleaseNotesData releaseNotes;

    public static ReleaseTrainData fromFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), ReleaseTrainData.class);
    }

    public String getName() {
        return releaseTrain != null ? releaseTrain.getName() : null;
    }

    public String getDescription() {
        return releaseTrain != null ? releaseTrain.getDescription() : null;
    }

    public List<Release> getReleases() {
        return releaseTrain != null ? releaseTrain.getReleases() : null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseTrain {
        private String name;
        private String description;
        private List<Release> releases;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseNotesData {
        private String viewAction;
        private String pageTitle;
        private String releaseLabel;
        private String expectedText;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseDeleteData {
        private String deleteAction;
        private String confirmationMessage;
        private String commentsPlaceholder;
        private String comments;
        private String deleteButton;
        private String deletedMessage;
        private String emptySearchMessage;
        private int expectedReleaseCountAfterDelete;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseTrainEditData {
        private String namePrefix;
        private String description;
        private String editAction;
        private String editPageTitle;
        private String saveButton;
        private String updatedMessage;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseEditData {
        private String namePrefix;
        private String managerLabel;
        private String manager;
        private String timelineLabel;
        private String timeline;
        private String objective;
        private String editAction;
        private String editPageTitle;
        private String saveButton;
        private String updatedMessage;
        private Map<String, String> expectedReleaseDetails;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Release {
        @JsonProperty("releaseName")
        private String releaseName;
        private String version;
        private String releaseId;
        private String releaseObjective;
        private String releaseManager;
        private String releaseType;
        private String impact;
        private String risk;
    }
}
