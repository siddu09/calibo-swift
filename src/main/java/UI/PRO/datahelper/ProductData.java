package UI.PRO.datahelper;

import lombok.Data;

import java.util.List;

@Data
public class ProductData {

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