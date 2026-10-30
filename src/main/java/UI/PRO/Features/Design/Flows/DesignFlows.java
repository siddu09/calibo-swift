package UI.PRO.Features.Design.Flows;

import UI.PRO.Features.Design.BuildingBlocks.DesignBuildingBlock;
import UI.PRO.Features.Design.Validations.DesignValidation;
import UI.PRO.datahelper.DesignData;
import UI.PRO.CommonProValidations.ProValidation;
import com.microsoft.playwright.Page;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProTestData;
import testdatamanager.pro.DesignTestData;
import io.qameta.allure.Step;

public class DesignFlows {

    @Step("Create two mandatory-only Designs in one feature and search each independently")
    public void searchDesign() {
        designBuildingBlock.prepareSearchDesignFeature(DesignTestData.getScenario(DesignTestData.SEARCH_DESIGN));
        var scenario = DesignTestData.getScenario(DesignTestData.SEARCH_DESIGN);
        scenario.getDesignScenarios().forEach(designBuildingBlock::createSearchableMandatoryDesign);
        designBuildingBlock.validateSearchDesignResults(DesignTestData.getScenario(DesignTestData.SEARCH_DESIGN));
    }

    @Step("Record each searched Design in its own associated execution workbook row")
    public void writeSearchDesignExecutionResults() {
        for (String key : DesignTestData.getScenario(DesignTestData.SEARCH_DESIGN).getDesignScenarios()) {
            var data = DesignTestData.getScenario(key);
            testdatamanager.pro.ProExecutionResultWriter.write(data, key,
                    designBuildingBlock.multipleDesignExecutionRow(data));
        }
    }

    @Step("Delete the shared search Design feature and product")
    public void deleteSearchDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario(DesignTestData.SEARCH_DESIGN));
    }

    @Step("Create an all-fields Design with a reusable document and validate its download")
    public void validateDocumentDownloadedAndValidation() {
        designBuildingBlock.prepareDocumentDesignFeature(
                DesignTestData.getScenario(DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD));
        designBuildingBlock.createAndPersistMultipleDesign(DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD,
                DesignTestData.getScenario(DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD));
        var data = DesignTestData.getScenario(DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD);
        designBuildingBlock.reopenDesignSlide(data);
        designBuildingBlock.validateDocumentDownload(data);
    }

    @Step("Delete the document Design feature and product")
    public void deleteDocumentDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(
                DesignTestData.getScenario(DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD));
    }

    @Step("Create an all-fields Design and validate navigation from every saved link symbol")
    public void validateDesignLinksNavigation() {
        designBuildingBlock.prepareDesignLinksFeature(DesignTestData.getScenario(DesignTestData.VALIDATE_LINKS_NAVIGATION));
        var data = DesignTestData.getScenario(DesignTestData.VALIDATE_LINKS_NAVIGATION);
        designBuildingBlock.generateMultipleDesignFile(data);
        designBuildingBlock.createDesignForLinksNavigation(data);
        data = DesignTestData.getScenario(DesignTestData.VALIDATE_LINKS_NAVIGATION);
        designBuildingBlock.reopenDesignSlide(data);
        designBuildingBlock.validateSavedDesignLinkDestinations(data);
    }

    @Step("Delete the feature and product used by Design link navigation")
    public void deleteDesignLinksProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario(DesignTestData.VALIDATE_LINKS_NAVIGATION));
    }

    @Step("Create, delete and verify a Design artifact using the existing all-fields creation flow")
    public void deleteDesign() {
        designBuildingBlock.prepareDeleteDesignFeature(DesignTestData.getScenario(DesignTestData.DELETE_DESIGN));
        var data = DesignTestData.getScenario(DesignTestData.DELETE_DESIGN);
        designBuildingBlock.generateMultipleDesignFile(data);
        designBuildingBlock.createAndPersistMultipleDesign(DesignTestData.DELETE_DESIGN, data);
        data = DesignTestData.getScenario(DesignTestData.DELETE_DESIGN);
        designBuildingBlock.validatePersistedDesign(data);
        designBuildingBlock.verifyDesignAttachments(data);
        designBuildingBlock.deleteSavedDesign(data);
        designBuildingBlock.validateDeletedDesignAbsent(data);
    }

    @Step("Delete the feature and product used by the Design deletion scenario")
    public void deleteDesignScenarioProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario(DesignTestData.DELETE_DESIGN));
    }

    @Step("Validate missing Design Name and Description independently and together")
    public void validateDesignMandatoryFields() {
        designBuildingBlock.prepareMandatoryDesignFeature(DesignTestData.getScenario(DesignTestData.VALIDATE_MANDATORY_FIELDS));
        var data = DesignTestData.getScenario(DesignTestData.VALIDATE_MANDATORY_FIELDS);
        designBuildingBlock.openMandatoryDesignForm(data);
        designBuildingBlock.validateIncompleteDesignSubmissions(data);
        designBuildingBlock.validateNoMandatoryDesignCreated(data);
    }

    @Step("Delete the feature and product used by mandatory Design validation")
    public void deleteMandatoryDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario(DesignTestData.VALIDATE_MANDATORY_FIELDS));
    }

    @Step("Edit and validate all fields of an existing Design")
    public void editDesignWithAllFields() {
        designBuildingBlock.prepareDesignEditFeature(DesignTestData.getScenario(DesignTestData.EDIT_ALL_FIELDS));
        var original = DesignTestData.getScenario(DesignTestData.EDIT_ALL_FIELDS);
        designBuildingBlock.generateMultipleDesignFile(original);
        designBuildingBlock.createDesignEditRequirement(DesignTestData.EDIT_ALL_FIELDS, original);
        designBuildingBlock.createEditableDesignWithAllFields(DesignTestData.getScenario(DesignTestData.EDIT_ALL_FIELDS));
        original = DesignTestData.getScenario(DesignTestData.EDIT_ALL_FIELDS);
        designBuildingBlock.validatePersistedDesign(original);
        designBuildingBlock.validateMultipleDesignMetadata(original);
        designBuildingBlock.validateDesignBusinessCase(original);
        designBuildingBlock.verifyDesignAttachments(original);
        designBuildingBlock.openMultipleDesignProduct(original);
        designBuildingBlock.createDesignEditRequirement(DesignTestData.EDIT_UPDATED_FIELDS,
                DesignTestData.getScenario(DesignTestData.EDIT_UPDATED_FIELDS));
        designBuildingBlock.reopenDesignSlide(original);
        designBuildingBlock.openDesignEditForm(original);
        var updated = DesignTestData.getScenario(DesignTestData.EDIT_UPDATED_FIELDS);
        designBuildingBlock.generateMultipleDesignFile(updated);
        designBuildingBlock.updateDesignAllFields(original, updated);
        designBuildingBlock.validateEditedDesign(DesignTestData.getScenario(DesignTestData.EDIT_ALL_FIELDS),
                DesignTestData.getScenario(DesignTestData.EDIT_UPDATED_FIELDS));
    }

    @Step("Delete the feature and product used by the Design edit scenario")
    public void deleteEditedDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario(DesignTestData.EDIT_ALL_FIELDS));
    }

    @Step("Create and revalidate multiple designs in one private product and feature")
    public void createMultipleDesignsWithinFeature() {
        var scenario = DesignTestData.getScenario(DesignTestData.CREATE_MULTIPLE_DESIGNS);
        designBuildingBlock.prepareMultipleDesignFeature(scenario);
        designBuildingBlock.captureMultipleDesignSourceImages(scenario);
        for (String key : scenario.getDesignScenarios()) {
            var data = DesignTestData.getScenario(key);
            designBuildingBlock.generateMultipleDesignFile(data);
            designBuildingBlock.openMultipleDesignProduct(data);
            designBuildingBlock.createAndPersistMultipleDesign(key, data);
            data = DesignTestData.getScenario(key);
            designBuildingBlock.validatePersistedDesign(data);
            designBuildingBlock.validateMultipleDesignMetadata(data);
            designBuildingBlock.verifyDesignAttachments(data);
        }
        designBuildingBlock.validateMultipleDesignsWithinFeature(
                DesignTestData.getScenario(DesignTestData.CREATE_MULTIPLE_DESIGNS));
    }

    @Step("Save each design in a separate existing workbook row")
    public void writeMultipleDesignExecutionResults() {
        var scenario = DesignTestData.getScenario(DesignTestData.CREATE_MULTIPLE_DESIGNS);
        for (String key : scenario.getDesignScenarios()) {
            var data = DesignTestData.getScenario(key);
            testdatamanager.pro.ProExecutionResultWriter.write(data, key,
                    designBuildingBlock.multipleDesignExecutionRow(data));
        }
    }

    @Step("Delete the shared feature and product after all designs are verified")
    public void deleteMultipleDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(
                DesignTestData.getScenario(DesignTestData.CREATE_MULTIPLE_DESIGNS));
    }

    private final DesignBuildingBlock designBuildingBlock;
    private final Page page;
    private final ProExecutionData executionData;

    public DesignFlows(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;
        designBuildingBlock =
                new DesignBuildingBlock(
                        page,
                        executionData);
    }

    public void createDesignWithMandatoryFields() {

        DesignData designData =
                ProTestData.getDesign(
                        "createDesign");

        designBuildingBlock
                .createDesign(
                        designData);

        ProValidation.validateSuccessMessage(
                page,
                "Design artifact added successfully."
        );

        DesignValidation.validateDesignDetails(
                page,
                executionData,
                designData);

    }

    @Step("Remove the Design scenario feature and product")
    public void deleteDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario());
    }

    @Step("Create and validate a Design artifact with link and file attachments")
    public void createDesignWithAllFields() {
        designBuildingBlock.createPrivateDesignProduct(DesignTestData.getScenario());
        designBuildingBlock.ensureDesignFeature(DesignTestData.getScenario());
        DesignTestData.saveValues(DesignTestData.CREATE_ALL_FIELDS, java.util.Map.of(
                "productName", executionData.getPrimaryProduct().getProductName(),
                "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName()));
        designBuildingBlock.createDesignWithAllFields(DesignTestData.getScenario(DesignTestData.CREATE_ALL_FIELDS));
        designBuildingBlock.validatePersistedDesign(DesignTestData.getScenario(DesignTestData.CREATE_ALL_FIELDS));
        designBuildingBlock.verifyDesignAttachments(DesignTestData.getScenario(DesignTestData.CREATE_ALL_FIELDS));
    }

    @Step("Remove the all-fields Design feature and product")
    public void deleteAllFieldsDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario(DesignTestData.CREATE_ALL_FIELDS));
    }

    @Step("Create a Business Requirement and associate it with a Sketch design")
    public void addBusinessRequirementToDesign() {
        var data = DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT);
        designBuildingBlock.createPrivateDesignProduct(data);
        designBuildingBlock.ensureDesignFeature(data);
        DesignTestData.saveValues(DesignTestData.ADD_BUSINESS_REQUIREMENT, java.util.Map.of(
                "productName", executionData.getPrimaryProduct().getProductName(),
                "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName()));
        designBuildingBlock.createBusinessRequirementForDesign(DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT));
        designBuildingBlock.createDesignLinkedToBusinessRequirement(DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT));
        designBuildingBlock.validatePersistedDesign(DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT));
        designBuildingBlock.validateDesignBusinessCase(DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT));
    }

    @Step("Remove the feature and product used for the linked Business Requirement design")
    public void deleteBusinessRequirementDesignProductAndFeature() {
        designBuildingBlock.deleteDesignProductAndFeature(DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT));
    }
}
