package UI.PRO.datahelper;

import lombok.Data;

import java.util.List;

@Data
public class ProductData {

    private String portfolioName;
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
    private java.util.Map<String, String> milestones;
    private String title;
    private String description;
    private String businessGroup;
    private List<String> phases;
}