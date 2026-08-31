package UI.DSO.buildingblocks;

import UI.DSO.helpers.DSOConstants;
import utils.webUtils.GenerateRandomString;

import java.util.Map;

public class runManagerDataHandler {

    public DSOConstants build(Map<String, String> dataRow) {
        String randomSuffix = GenerateRandomString.generateRandomString();

        DSOConstants data = new DSOConstants();
        data.setTestCaseName(readValue(dataRow, "TestCase Name", "DSO_AWS_Default"));
        data.setTechnology(readValue(dataRow, "Technology", "Core Java-Gradle"));
        data.setCloudName(readValue(dataRow, "Cloud Name", "AWS"));
        data.setSourceCode(readValue(dataRow, "Source Code", "GitLab"));
        data.setDeploymentMode(readValue(dataRow, "Deployment Mode", "KUBERNETES").toUpperCase());
        data.setAccountCluster(readValue(dataRow, "AccountCluster", ""));
        data.setCiTool(readValue(dataRow, "CI Tool", "Jenkins"));
        data.setArtifactory(readValue(dataRow, "Artifactory", "Jfrog"));
        data.setCodeAnalysis(readValue(dataRow, "Code Analysis", "Sonarqube"));
        data.setImageScan(readValue(dataRow, "Image Scan", ""));
        data.setOrchestrator(readValue(dataRow, "Orchestrator", ""));
        data.setStatus(readValue(dataRow, "Status", ""));

        data.setRandomSuffix(randomSuffix);

        String testCaseToken = normalizeToken(data.getTestCaseName());
        data.setProductName("AutomationE2EProduct" + testCaseToken + randomSuffix);
        data.setFeatureName("AutomationE2EFeature" + testCaseToken + randomSuffix);

        data.setTechnologyTitle(randomSuffix);
        data.setRepositoryName(randomSuffix + randomSuffix);
        data.setContextPath("/" + randomSuffix + randomSuffix);

        data.setPortfolioName("Default Automation PL");
        data.setPortfolioSearchValue("DefaultAutomationPL");
        data.setFeatureOwner("API Automation");
        data.setBusinessGroup("BG_Automation");
        data.setRepositoryGroup("automation");
        data.setRepositoryVisibility("Private");
        data.setSourceCodeBranch("main");
        data.setNamespace("automation");
        data.setKubernetesCluster(readValue(dataRow, "Kubernetes Cluster", "cluster-eks-qa"));
        return data;
    }

    private String readValue(
            Map<String, String> dataRow,
            String key,
            String defaultValue) {

        if (dataRow == null) {
            return defaultValue;
        }

        String value = dataRow.get(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    private String normalizeToken(String value) {
        String normalized = value.replaceAll("[^a-zA-Z0-9]", "");
        if (normalized.isEmpty()) {
            return "DSO";
        }
        return normalized;
    }
}
