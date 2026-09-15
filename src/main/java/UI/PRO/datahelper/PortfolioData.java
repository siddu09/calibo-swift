package UI.PRO.datahelper;

import lombok.Data;

@Data
public class PortfolioData {

    private java.util.List<AuditEventData> auditEvents;

    @Data
    public static class AuditEventData {
        private String search;
        private String eventName;
        private String filterLabel;
    }

    private String auditPageTitle;
    private String reportNamePrefix;
    private String reportGeneratedMessage;
    private String reportCompletedStatus;
    private java.util.List<String> reportFormats;
    private String stakeholder;
    private java.util.Map<String, String> customFields;
    private String localFieldNamePrefix;
    private String localFieldValuePrefix;
    private String detailsSavedMessage;
    private String customFieldDeleteMessage;
    private String financialYearDeleteMessage;
    private String name;
    private String description;
    private boolean publicPortfolio;
    private String businessOutcome;
    private String owner;
    private String priority;
    private String customFieldName;
    private String customFieldValue;
    private String financialYear;
    private String approvedBudget;
    private String revenueTarget;
    private String portfolioValue;
    private String strategy;
    private String validLogoPath;
    private String invalidLogoPath;
}
