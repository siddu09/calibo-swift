package UI.PRO.datahelper;

import lombok.Data;

@Data
public class PortfolioData {

    private String name;
    private String description;
    private boolean publicPortfolio;
    private String businessOutcome;
    private String owner;
    private String priority;
    private String customFieldName;
    private String customFieldValue;
    private String approvedBudget;
    private String revenueTarget;
    private String portfolioValue;
    private String strategy;
    private String validLogoPath;
    private String invalidLogoPath;
}
