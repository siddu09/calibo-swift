package UI.PRO.datahelper;

import lombok.Data;
import java.util.List;

@Data
public class MaturityAssessmentData {
    private String namePrefix;
    private String newAssessmentButton;
    private String nameLabel;
    private String namePlaceholder;
    private String respondent;
    private List<String> subCategories;
    private String responseDueDate;
    private String initiateButton;
    private String initiatedMessage;
    private String doneButton;
    private String currentAssessmentsTab;
    private String backButton;
    private String expectedStatus;
    private String initiatedCountLabel;
    private String expectedInitiatedCount;
    private String resultSheet;
    private String executionDataFile;
    private String missingAssessmentMessage;
    private ResponseData response;
    private ProductData.ProductAssessmentDiscardData assessmentDiscard;
    private int loadTimeoutMs;
    private BaselineData baseline;

    @Data
    public static class BaselineData {
        private String baselineButton;
        private String confirmationMessage;
        private String yesButton;
        private String baselinedMessage;
        private String baselinedAssessmentsTab;
    }

    @Data
    public static class ResponseData {
        private String profileLabel;
        private String myAssessmentsLabel;
        private String searchByNamePlaceholder;
        private String startButton;
        private String nextButton;
        private String submitButton;
        private String submittedMessage;
        private String completedStatus;
        private String completionPercentage;
        private String viewDetailsButton;
        private int loadTimeoutMs;
        private List<Question> questions;
    }

    @Data
    public static class Question {
        private String subCategory;
        private String question;
        private String answer;
        private List<String> displayedAnswers;
    }
}
