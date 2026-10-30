package tests.ui.PRO;

import UI.E2E.LoginBuildingBlock;
import UI.PRO.Features.Design.Flows.DesignFlows;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdatamanager.pro.DesignTestData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProExecutionResultWriter;
import tests.base.BaseUITest;
import tests.constants.Constants;
import utils.LoggerUtil;

public class DesignTest extends BaseUITest {
    private DesignFlows designFlows;
    private ProExecutionData executionData;

    @BeforeMethod
    @Step("Login and initialize Design flows")
    public void setUp() {
        new LoginBuildingBlock(page).proLogin();
        executionData = new ProExecutionData();
        designFlows = new DesignFlows(page, executionData);
    }

    @Step("Create Design with Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void createDesignWithMandatoryFields() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting createDesignWithMandatoryFields");
        designFlows.createDesignWithMandatoryFields();
        ProExecutionResultWriter.write(DesignTestData.getScenario(), DesignTestData.CREATE_DESIGN, executionData);
        designFlows.deleteDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: {}", DesignTestData.getScenario().getDesignName());
    }
    @Step("Create Design with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void createDesignWithAllFields() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting createDesignWithAllFields");
        designFlows.createDesignWithAllFields();
        ProExecutionResultWriter.write(DesignTestData.getScenario(DesignTestData.CREATE_ALL_FIELDS), DesignTestData.CREATE_ALL_FIELDS, executionData);
        designFlows.deleteAllFieldsDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: {}", DesignTestData.getScenario(DesignTestData.CREATE_ALL_FIELDS).getDesignName());
    }
    @Step("Add a Business Requirement to a Design")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void addBusinessRequirementToDesign() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting addBusinessRequirementToDesign");
        designFlows.addBusinessRequirementToDesign();
        ProExecutionResultWriter.write(DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT), DesignTestData.ADD_BUSINESS_REQUIREMENT, executionData);
        designFlows.deleteBusinessRequirementDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: {}", DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT).getDesignName());
    }
    @Step("Create Multiple Designs within a Feature")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void createMultipleDesignsWithinFeature() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting createMultipleDesignsWithinFeature");
        designFlows.createMultipleDesignsWithinFeature();
        designFlows.writeMultipleDesignExecutionResults();
        designFlows.deleteMultipleDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: createMultipleDesignsWithinFeature");
    }
    @Step("Edit Design with All Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void editDesignWithAllFields() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting editDesignWithAllFields");
        designFlows.editDesignWithAllFields();
        ProExecutionResultWriter.write(DesignTestData.getScenario(DesignTestData.EDIT_UPDATED_FIELDS), DesignTestData.EDIT_ALL_FIELDS, executionData);
        designFlows.deleteEditedDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: {}", DesignTestData.getScenario(DesignTestData.EDIT_UPDATED_FIELDS).getDesignName());
    }
    @Step("Validate Design Mandatory Fields")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void validateDesignMandatoryFields() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting validateDesignMandatoryFields");
        designFlows.validateDesignMandatoryFields();
        ProExecutionResultWriter.write(DesignTestData.getScenario(DesignTestData.VALIDATE_MANDATORY_FIELDS), DesignTestData.VALIDATE_MANDATORY_FIELDS, executionData);
        designFlows.deleteMandatoryDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: {}", DesignTestData.getScenario(DesignTestData.VALIDATE_MANDATORY_FIELDS).getDesignName());
    }
    @Step("Delete the created Design")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void deleteDesign() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting deleteDesign");
        designFlows.deleteDesign();
        ProExecutionResultWriter.write(DesignTestData.getScenario(DesignTestData.DELETE_DESIGN), DesignTestData.DELETE_DESIGN, executionData);
        designFlows.deleteDesignScenarioProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: {}", DesignTestData.getScenario(DesignTestData.DELETE_DESIGN).getDesignName());
    }

    @Step("Validate Design Links Navigation")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void validateDesignLinksNavigation() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting validateDesignLinksNavigation");
        designFlows.validateDesignLinksNavigation();
        ProExecutionResultWriter.write(DesignTestData.getScenario(DesignTestData.VALIDATE_LINKS_NAVIGATION),
                DesignTestData.VALIDATE_LINKS_NAVIGATION, executionData);
        designFlows.deleteDesignLinksProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: validateDesignLinksNavigation");
    }
    @Step("Validate document download from Design Details including filename and content")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void validateDocumentDownloadedAndValidation() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting validateDocumentDownloadedAndValidation");
        designFlows.validateDocumentDownloadedAndValidation();
        ProExecutionResultWriter.write(DesignTestData.getScenario(DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD),DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD, executionData);
        designFlows.deleteDocumentDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: validateDocumentDownloadedAndValidation");
    }
    @Step("Search two Designs independently by their persisted names")
    @Test(groups = {Constants.PRO_REGRESSION, Constants.PRO_DESIGN})
    public void searchDesign() {
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Starting searchDesign");
        designFlows.searchDesign();
        designFlows.writeSearchDesignExecutionResults();
        designFlows.deleteSearchDesignProductAndFeature();
        LoggerUtil.LOGGER.info("[DESIGN-TEST] Completed successfully: searchDesign");
    }
}
