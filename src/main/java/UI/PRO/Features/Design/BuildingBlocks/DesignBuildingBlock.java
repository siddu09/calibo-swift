package UI.PRO.Features.Design.BuildingBlocks;

import UI.PRO.datahelper.DesignData;
import UI.PRO.CommonProValidations.ProValidation;
import UI.PRO.Product.BuildingBlocks.ProductBuildingBlock;
import UI.PRO.Product.validations.ProductValidation;
import UI.PRO.Features.Feature.BuildingBlocks.FeatureBuildingBlock;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.PRO.Features.Design.DesignPage;
import pages.PRO.Features.Design.ResilientDesignPage;
import pages.PRO.Features.Feature.FeaturePage;
import pages.PRO.Features.Design.DesignFeaturePage;
import pages.PRO.Features.Design.DesignAttachmentsPage;
import pages.PRO.Features.Design.MultipleDesignPage;
import pages.PRO.Features.Design.EditDesignPage;
import pages.PRO.ProductPortfolio.ProductPortfolioAdditionalDetailsOthersTabPage;
import pages.PRO.Features.Define.ResilientBusinessRequirementPage;
import pages.PRO.ReleaseTrain.NewReleasePage;
import pages.PRO.Product.ProductAddProjectDetailsPage;
import pages.PRO.Product.ProductFeatureTabPage;
import testdatamanager.pro.DesignTestData;
import testdatamanager.pro.FeatureExecutionData;
import testdatamanager.pro.ProExecutionData;
import testdatamanager.pro.ProductExecutionData;
import utils.CommonMethods;
import utils.LoggerUtil;
import java.net.URI;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.UncheckedIOException;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DesignBuildingBlock {

    @Step("Prepare one private product and feature for both searchable Designs")
    public void prepareSearchDesignFeature(DesignData scenario) {
        var previous = DesignTestData.getScenario();
        try {
            createPrivateDesignProduct(scenario);
            ensureDesignFeature(scenario);
            var names = Map.of("productName", executionData.getPrimaryProduct().getProductName(),
                    "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName());
            DesignTestData.saveSearchDesignValues(DesignTestData.SEARCH_DESIGN, names);
            scenario.getDesignScenarios().forEach(key -> DesignTestData.saveSearchDesignValues(key, names));
        } finally {
            DesignTestData.saveValues(Map.of("productName", previous.getProductName(),
                    "featureName", previous.getFeatureName()));
        }
    }

    @Step("Reuse mandatory-only Design creation and persist its searchable name")
    public void createSearchableMandatoryDesign(String scenarioKey) {
        String previous = DesignTestData.getScenario().getDesignName();
        try {
            var data = DesignTestData.getScenario(scenarioKey);
            openMultipleDesignProduct(data);
            createPersistedFeatureDesign(data);
            DesignTestData.saveSearchDesignValues(scenarioKey, Map.of("designName",
                    executionData.getPrimaryProduct().getFeatures().getLast().getDesignNames().getLast()));
        } finally {
            DesignTestData.saveValues(Map.of("designName", previous));
        }
    }

    @Step("Search each stored Design name and exclude the other Design from its results")
    public void validateSearchDesignResults(DesignData scenario) {
        var designs = scenario.getDesignScenarios().stream().map(DesignTestData::getScenario).toList();
        Assert.assertNotEquals(designs.getFirst().getDesignName(), designs.getLast().getDesignName(),
                scenario.getValidationMessages().get("uniqueDesignNames"));
        openMultipleDesignProduct(scenario);
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(scenario.getFeatureName()).click();
        feature.selectPhase(scenario.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new pages.PRO.Features.Design.SearchDesignPage(page);
        String placeholder = scenario.getValidationMessages().get("searchPlaceholder");
        for (var data : designs) {
            design.searchDesigns(placeholder).clear();
            assertThat(design.searchDesigns(placeholder)).isEmpty();
            design.searchDesigns(placeholder).fill(data.getDesignName());
            assertThat(design.searchDesigns(placeholder)).hasValue(data.getDesignName());
            design.expandCategory(data.getCategory()).click();
            assertThat(design.savedFeatureCards()).hasCount(1);
            assertThat(design.savedFeatureCards()).hasAttribute("title", data.getDesignName());
            assertThat(design.designCard(data.getDesignName())).isVisible();
            for (var other : designs) {
                if (!other.getDesignName().equals(data.getDesignName())) {
                    Assert.assertFalse(design.savedFeatureCards().all().stream()
                                    .anyMatch(card -> other.getDesignName().equals(card.getAttribute("title"))),
                            scenario.getValidationMessages().get("otherDesignExcluded"));
                }
            }
            design.designCard(data.getDesignName()).click();
            var detailsTitle = design.designDetailsTitle();
            assertThat(detailsTitle).hasText(data.getDesignName());
            assertThat(design.designDetailsDescription()).hasText(data.getDescription());
            design.closeDetails().click();
            assertThat(detailsTitle).isHidden();
            design.expandCategory(data.getCategory()).click();
            LoggerUtil.LOGGER.info("[SEARCH-DESIGN] Verified exact result and excluded other Design: {}",
                    data.getDesignName());
        }
        design.searchDesigns(placeholder).clear();
        assertThat(design.searchDesigns(placeholder)).isEmpty();
    }

    @Step("Prepare the document Design using existing private product and feature creation")
    public void prepareDocumentDesignFeature(DesignData data) {
        var previous = DesignTestData.getScenario();
        try {
            createPrivateDesignProduct(data);
            ensureDesignFeature(data);
            DesignTestData.saveDocumentFeatureNameForBothEnvironments(
                    executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName());
            DesignTestData.saveValues(DesignTestData.VALIDATE_DOCUMENT_DOWNLOAD,
                    Map.of("productName", executionData.getPrimaryProduct().getProductName(),
                            "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName(),
                            "designName", ""));
        } finally {
            DesignTestData.saveValues(Map.of("productName", previous.getProductName(),
                    "featureName", previous.getFeatureName()));
        }
    }

    @Step("Reuse attachment verification and validate the original document download name")
    public void validateDocumentDownload(DesignData data) {
        var downloads = new java.util.ArrayList<com.microsoft.playwright.Download>();
        java.util.function.Consumer<com.microsoft.playwright.Download> recordDownload = downloads::add;
        page.onDownload(recordDownload);
        try {
            assertThat(new DesignAttachmentsPage(page).savedLink(data.getDesignLink().getUrl()))
                    .hasText(data.getDesignLink().getName());
            assertThat(new DesignAttachmentsPage(page).downloadFile(data.getFileTitle())).isVisible();
            verifyDesignAttachments(data);
            Assert.assertEquals(downloads.size(), 1, data.getValidationMessages().get("documentDownloadCount"));
            for (var download : downloads) {
                Assert.assertEquals(download.suggestedFilename(), data.getDesignFile().getName(),
                        data.getValidationMessages().get("downloadFileName"));
                LoggerUtil.LOGGER.info("[DESIGN-DOCUMENT] Verified original filename and matching content: {}",
                        download.suggestedFilename());
            }
        } finally {
            page.offDownload(recordDownload);
        }
    }

    @Step("Reuse private product and feature creation for Design link navigation")
    public void prepareDesignLinksFeature(DesignData data) {
        var previous = DesignTestData.getScenario();
        try {
            createPrivateDesignProduct(data);
            ensureDesignFeature(data);
            DesignTestData.saveValues(DesignTestData.VALIDATE_LINKS_NAVIGATION,
                    Map.of("productName", executionData.getPrimaryProduct().getProductName(),
                            "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName(),
                            "designName", ""));
        } finally {
            DesignTestData.saveValues(Map.of("productName", previous.getProductName(),
                    "featureName", previous.getFeatureName()));
        }
    }

    @Step("Reuse all-fields Design creation with each configured documentation link")
    public void createDesignForLinksNavigation(DesignData data) {
        // Extend only the link-add action; the existing all-fields creation flow remains unchanged.
        var creation = new DesignBuildingBlock(page, executionData) {
            @Override
            public void addDesignDocumentationLink(DesignData draft) {
                var original = draft.getDesignLink();
                try {
                    for (var link : draft.getDesignLinks()) {
                        draft.setDesignLink(link);
                        super.addDesignDocumentationLink(draft);
                    }
                } finally {
                    draft.setDesignLink(original);
                }
            }
        };
        creation.createAndPersistMultipleDesign(DesignTestData.VALIDATE_LINKS_NAVIGATION, data);
    }

    @Step("Open every saved link symbol and verify its complete destination URL")
    public void validateSavedDesignLinkDestinations(DesignData data) {
        var attachments = new DesignAttachmentsPage(page);
        assertThat(attachments.savedLinks()).hasCount(data.getDesignLinks().size());
        String detailsUrl = page.url();
        int originalPageCount = page.context().pages().size();
        for (var link : data.getDesignLinks()) {
            assertThat(attachments.savedLink(link.getUrl())).hasText(link.getName());
            Assert.assertEquals(attachments.savedLink(link.getUrl()).getAttribute("href"), link.getUrl(),
                    data.getValidationMessages().get("savedLinkDestination"));
            var destination = page.waitForPopup(new Page.WaitForPopupOptions().setTimeout(data.getLoadTimeoutMs()),
                    () -> attachments.linkIcon(link.getUrl()).click());
            try {
                destination.waitForURL(link.getUrl(), new Page.WaitForURLOptions()
                        .setTimeout(data.getLoadTimeoutMs())
                        .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
                Assert.assertFalse(destination.isClosed(), link.getLinkOpenMessage());
                Assert.assertEquals(destination.url(), link.getUrl(), data.getValidationMessages().get("destinationUrl"));
                LoggerUtil.LOGGER.info("[DESIGN-LINKS] Verified {} opens {}", link.getName(), destination.url());
            } finally {
                destination.close();
            }
            Assert.assertEquals(page.url(), detailsUrl, data.getValidationMessages().get("detailsPageRetained"));
            Assert.assertEquals(page.context().pages().size(), originalPageCount,
                    data.getValidationMessages().get("popupClosed"));
            assertThat(new ResilientDesignPage(page).designDetailsTitle()).hasText(data.getDesignName());
        }
    }

    @Step("Reuse private product and feature creation for deleting a Design")
    public void prepareDeleteDesignFeature(DesignData data) {
        var previous = DesignTestData.getScenario();
        try {
            createPrivateDesignProduct(data);
            ensureDesignFeature(data);
            DesignTestData.saveValues(DesignTestData.DELETE_DESIGN,
                    Map.of("productName", executionData.getPrimaryProduct().getProductName(),
                            "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName(),
                            "designName", ""));
        } finally {
            DesignTestData.saveValues(Map.of("productName", previous.getProductName(),
                    "featureName", previous.getFeatureName()));
        }
    }

    @Step("Delete the saved Design through its slide action menu and confirmation dialog")
    public void deleteSavedDesign(DesignData data) {
        new pages.PRO.Features.Feature.FeatureDetailsPage(page).featureActions(data.getDesignName()).click();
        new EditDesignPage(page).edit(data.getProduct().getDeleteAction()).click();
        var deletion = new pages.PRO.Features.Design.DeleteDesignPage(page);
        assertThat(deletion.confirmation(data.getDeleteConfirmationMessage())).isVisible();
        deletion.comments().fill(data.getDesignDeleteComment());
        assertThat(deletion.comments()).hasValue(data.getDesignDeleteComment());
        var confirm = deletion.confirmDelete(data.getProduct().getDeleteAction());
        assertThat(confirm).isEnabled();
        var response = page.waitForResponse(result -> "DELETE".equals(result.request().method()),
                new Page.WaitForResponseOptions().setTimeout(data.getLoadTimeoutMs()), confirm::click);
        Assert.assertTrue(response.ok(), data.getValidationMessages().get("deletionRequest"));
        ProValidation.validateSuccessMessage(page, data.getDeletedMessage());
        assertThat(new MultipleDesignPage(page).savedFeatureCards()
                .filter(new Locator.FilterOptions().setHasText(data.getDesignName()))).hasCount(0);
    }

    @Step("Reload the feature and search the Design cards for the deleted artifact")
    public void validateDeletedDesignAbsent(DesignData data) {
        openMultipleDesignProduct(data);
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(data.getFeatureName()).click();
        feature.selectPhase(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        page.reload(new Page.ReloadOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(new ResilientDesignPage(page).categoryControl()).isVisible();
        var cards = new MultipleDesignPage(page).savedFeatureCards();
        assertThat(cards.filter(new Locator.FilterOptions().setHasText(data.getDesignName()))).hasCount(0);
        assertThat(cards).hasCount(data.getExpectedDesignCount());
        LoggerUtil.LOGGER.info("[DELETE-DESIGN] Verified deleted Design is absent after reopening and reloading: {}",
                data.getDesignName());
    }

    @Step("Reuse private product and feature creation for mandatory Design validation")
    public void prepareMandatoryDesignFeature(DesignData data) {
        var previous = DesignTestData.getScenario();
        try {
            createPrivateDesignProduct(data);
            ensureDesignFeature(data);
            DesignTestData.saveValues(DesignTestData.VALIDATE_MANDATORY_FIELDS,
                    Map.of("productName", executionData.getPrimaryProduct().getProductName(),
                            "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName(),
                            "designName", ""));
        } finally {
            DesignTestData.saveValues(Map.of("productName", previous.getProductName(),
                    "featureName", previous.getFeatureName()));
        }
    }

    @Step("Open the configured Design form without creating an artifact")
    public void openMandatoryDesignForm(DesignData data) {
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(data.getFeatureName()).click();
        feature.selectPhase(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new ResilientDesignPage(page);
        design.chooseCategory(data.getCategory());
        design.proceed(data.getProceedButton()).click();
        design.selectDesignSource(data.getSource()).click();
        assertThat(design.name()).isEmpty();
        assertThat(design.description()).isEmpty();
    }

    @Step("Submit each incomplete Design and compare all mandatory errors with JSON expectations")
    public void validateIncompleteDesignSubmissions(DesignData data) {
        var design = new pages.PRO.Features.Design.DesignMandatoryFieldsPage(page);
        String creationUrl = page.url();
        for (var testCase : data.getMandatoryCases()) {
            String name = testCase.getName().isBlank() ? testCase.getName()
                    : CommonMethods.generateUniqueTitle(testCase.getName());
            design.name().fill(name);
            design.description().fill(testCase.getDescription());
            assertThat(design.create()).isEnabled();
            design.create().click();
            assertThat(design.mandatoryErrors()).hasText(testCase.getExpectedErrors().toArray(String[]::new));
            for (String error : testCase.getExpectedErrors()) {
                assertThat(design.mandatoryError(error)).isVisible();
            }
            assertThat(design.name()).hasValue(name);
            assertThat(design.description()).hasValue(testCase.getDescription());
            assertThat(design.form()).isVisible();
            Assert.assertEquals(page.url(), creationUrl, data.getValidationMessages().get("creationBlocked"));
        }
    }

    @Step("Reload the feature and prove no incomplete Design was saved")
    public void validateNoMandatoryDesignCreated(DesignData data) {
        Assert.assertTrue(executionData.getPrimaryProduct().getFeatures().getLast().getDesignNames().isEmpty(),
                data.getValidationMessages().get("noDesignCreated"));
        openMultipleDesignProduct(data);
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(data.getFeatureName()).click();
        feature.selectPhase(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(new ResilientDesignPage(page).categoryControl()).isVisible();
        assertThat(new MultipleDesignPage(page).savedFeatureCards()).hasCount(data.getExpectedDesignCount());
        LoggerUtil.LOGGER.info("[DESIGN-MANDATORY] All {} invalid submissions were rejected; no Design was saved",
                data.getMandatoryCases().size());
    }

    @Step("Prepare a private product and mandatory feature for editing a Design")
    public void prepareDesignEditFeature(DesignData data) {
        var previous = DesignTestData.getScenario();
        try {
            createPrivateDesignProduct(data);
            ensureDesignFeature(data);
            var names = Map.of("productName", executionData.getPrimaryProduct().getProductName(),
                    "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName());
            DesignTestData.saveValues(DesignTestData.EDIT_ALL_FIELDS, names);
            DesignTestData.saveValues(DesignTestData.EDIT_UPDATED_FIELDS, names);
        } finally {
            DesignTestData.saveValues(Map.of("productName", previous.getProductName(),
                    "featureName", previous.getFeatureName()));
        }
    }

    @Step("Reuse Business Requirement creation and persist the edit scenario's requirement")
    public void createDesignEditRequirement(String scenarioKey, DesignData data) {
        String previous = DesignTestData.getScenario(DesignTestData.ADD_BUSINESS_REQUIREMENT).getBusinessRequirementName();
        try {
            createBusinessRequirementForDesign(data);
            String name = executionData.getPrimaryProduct().getFeatures().getLast().getBusinessRequirementNames().getLast();
            DesignTestData.saveValues(scenarioKey, Map.of("businessRequirementName", name));
        } finally {
            DesignTestData.saveValues(DesignTestData.ADD_BUSINESS_REQUIREMENT, Map.of("businessRequirementName", previous));
        }
    }

    @Step("Populate the Design name, description and associated Business Requirement")
    public String fillDesignWithRequirement(DesignData data, ResilientDesignPage design) {
        String name = CommonMethods.generateUniqueTitle(data.getName());
        design.name().fill(name);
        design.description().fill(data.getDescription());
        assertThat(design.name()).hasValue(name);
        assertThat(design.description()).hasValue(data.getDescription());
        design.businessRequirementControl().click();
        design.businessRequirementSearch().fill(data.getBusinessRequirementName());
        design.selectDropdownValue(data.getBusinessRequirementName()).click();
        assertThat(design.businessRequirementControl()).containsText(data.getBusinessRequirementName());
        return name;
    }

    @Step("Create the original all-fields Design with attachments and a Business Requirement")
    public void createEditableDesignWithAllFields(DesignData data) {
        new ResilientBusinessRequirementPage(page).designWorkflow(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new MultipleDesignPage(page);
        design.chooseCategory(data.getCategory());
        design.proceed(data.getProceedButton()).click();
        String source = sourceImageFingerprint(design.sourcePickerImage(data.getSource()).getAttribute("src"));
        DesignTestData.saveValues(DesignTestData.EDIT_ALL_FIELDS, Map.of("sourceImageFingerprint", source));
        design.selectDesignSource(data.getSource()).click();
        String name = fillDesignWithRequirement(data, design);
        addDesignDocumentationLink(data);
        addDesignFile(data);
        design.create().click();
        ProValidation.validateSuccessMessage(page, data.getSavedMessage());
        storeDesignExecutionData(name);
        DesignTestData.saveValues(DesignTestData.EDIT_ALL_FIELDS, Map.of("designName", name));
    }

    @Step("Open the existing Design's edit form")
    public void openDesignEditForm(DesignData data) {
        var design = new EditDesignPage(page);
        new pages.PRO.Features.Feature.FeatureDetailsPage(page).featureActions(data.getDesignName()).click();
        design.edit(data.getEditButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(design.name()).hasValue(data.getDesignName());
        assertThat(design.description()).hasValue(data.getDescription());
    }

    @Step("Reopen the saved Design slide through its product and feature")
    public void reopenDesignSlide(DesignData data) {
        openMultipleDesignProduct(data);
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(data.getFeatureName()).click();
        feature.selectPhase(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        validatePersistedDesign(data);
    }

    @Step("Update the Design, confirm the inline link edit and replace its uploaded file")
    public void updateDesignAllFields(DesignData original, DesignData updated) {
        var design = new EditDesignPage(page);
        String designId = currentEditedDesignId();
        DesignTestData.saveValues(DesignTestData.EDIT_ALL_FIELDS, Map.of("designId", designId));
        design.clearRequirement().click();
        assertThat(design.businessRequirementControl()).not().containsText(original.getBusinessRequirementName());
        String name = fillDesignWithRequirement(updated, design);

        design.editLink(original.getDesignLink().getUrl()).click();
        design.inlineLinkField(true).fill(updated.getDesignLink().getName());
        design.inlineLinkField(false).fill(updated.getDesignLink().getUrl());
        assertThat(design.inlineLinkField(true)).hasValue(updated.getDesignLink().getName());
        assertThat(design.inlineLinkField(false)).hasValue(updated.getDesignLink().getUrl());
        design.confirmLinkEdit().click();
        assertThat(design.attachmentCells(original.getDesignLink().getName())).hasCount(0);
        assertThat(design.attachmentCells(updated.getDesignLink().getName())).hasCount(1);

        new DesignAttachmentsPage(page).filesTab().click();
        design.removeFile(original.getFileTitle()).click();
        assertThat(design.attachmentCells(original.getFileTitle())).hasCount(0);
        design.fileTitle().fill(updated.getFileTitle());
        var browse = new ProductPortfolioAdditionalDetailsOthersTabPage(page).browseThisComputerButton();
        var chooser = page.waitForFileChooser(browse::click);
        chooser.setFiles(Path.of(updated.getDesignFile().getFilePath()));
        assertThat(design.addFile()).isEnabled();
        design.addFile().click();
        assertThat(design.attachmentCells(updated.getFileTitle())).hasCount(1);
        design.save(updated.getSaveButton()).click();
        ProValidation.validateSuccessMessage(page, updated.getSavedMessage());

        var names = executionData.getPrimaryProduct().getFeatures().getLast().getDesignNames();
        Assert.assertEquals(names.size(), original.getExpectedDesignCount(), original.getValidationMessages().get("designCount"));
        names.set(names.size() - 1, name);
        DesignTestData.saveValues(DesignTestData.EDIT_UPDATED_FIELDS, Map.of("designName", name,
                "designId", designId, "sourceImageFingerprint", original.getSourceImageFingerprint()));
    }

    @Step("Verify replacement values persist and the original attachments and requirement are absent")
    public void validateEditedDesign(DesignData original, DesignData updated) {
        reopenDesignSlide(updated);
        validateMultipleDesignMetadata(updated);
        validateDesignBusinessCase(updated);
        verifyDesignAttachments(updated);
        var design = new EditDesignPage(page);
        for (String removed : java.util.List.of(original.getDesignName(), original.getDescription(),
                original.getDesignLink().getName(), original.getFileTitle(), original.getBusinessRequirementName())) {
            assertThat(design.savedValue(removed)).hasCount(0);
        }
        assertThat(new MultipleDesignPage(page).savedFeatureCards()).hasCount(updated.getExpectedDesignCount());
        openDesignEditForm(updated);
        Assert.assertEquals(currentEditedDesignId(), original.getDesignId(), updated.getValidationMessages().get("sameDesign"));
        assertThat(design.businessRequirementControl()).containsText(updated.getBusinessRequirementName());
        assertThat(design.businessRequirementControl()).not().containsText(original.getBusinessRequirementName());
        assertThat(design.attachmentCells(updated.getDesignLink().getName())).hasCount(1);
        assertThat(design.attachmentCells(original.getDesignLink().getName())).hasCount(0);
        new DesignAttachmentsPage(page).filesTab().click();
        assertThat(design.attachmentCells(updated.getFileTitle())).hasCount(1);
        assertThat(design.attachmentCells(original.getFileTitle())).hasCount(0);
        LoggerUtil.LOGGER.info("[EDIT-DESIGN] Verified persisted name, description, replacement link/file and Business Requirement; category={} and source={} are read-only",
                updated.getCategory(), updated.getSource());
    }

    private String currentEditedDesignId() {
        return java.util.Arrays.stream(URI.create(page.url()).getQuery().split("&"))
                .filter(parameter -> parameter.startsWith("designId="))
                .map(parameter -> parameter.substring("designId=".length())).findFirst().orElseThrow();
    }

    @Step("Create one shared private product and reuse or create its saved feature")
    public void prepareMultipleDesignFeature(DesignData scenario) {
        var previous = DesignTestData.getScenario();
        try {
            createPrivateDesignProduct(scenario);
            ensureDesignFeature(scenario);
            var names = Map.of("productName", executionData.getPrimaryProduct().getProductName(),
                    "featureName", executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName());
            DesignTestData.saveValues(DesignTestData.CREATE_MULTIPLE_DESIGNS, names);
            scenario.getDesignScenarios().forEach(key -> DesignTestData.saveValues(key, names));
        } finally {
            // Shared creation helpers persist to the original scenario; preserve its recorded values.
            DesignTestData.saveValues(Map.of("productName", previous.getProductName(),
                    "featureName", previous.getFeatureName()));
        }
    }

    @Step("Open the shared product's feature list without creating another product or feature")
    public void openMultipleDesignProduct(DesignData data) {
        var products = new ProductBuildingBlock(page, executionData);
        products.openProduct(data.getProductName(), data.getProduct());
        products.navigateToFeatureTab();
        CommonMethods.waitForLoaderToDisappear(page);
    }

    @Step("Generate and store the configured PNG design diagram")
    public void generateMultipleDesignFile(DesignData data) {
        var image = new java.awt.image.BufferedImage(1200, 420, java.awt.image.BufferedImage.TYPE_INT_RGB);
        var graphics = image.createGraphics();
        try {
            graphics.setColor(java.awt.Color.WHITE);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            graphics.setColor(new java.awt.Color(25, 45, 75));
            graphics.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 26));
            graphics.drawString(data.getName(), 40, 60);
            graphics.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 18));
            graphics.drawString(data.getDescription(), 40, 110);
            String[] labels = {data.getFeatureData().getTitle(), data.getCategory(), data.getSource()};
            for (int i = 0; i < labels.length; i++) {
                int x = 40 + i * 390;
                graphics.drawRoundRect(x, 180, 340, 120, 18, 18);
                graphics.drawString(labels[i], x + 15, 245);
                if (i < labels.length - 1) {
                    graphics.drawLine(x + 340, 240, x + 390, 240);
                    graphics.drawLine(x + 380, 230, x + 390, 240);
                    graphics.drawLine(x + 380, 250, x + 390, 240);
                }
            }
            Path output = Path.of(data.getDesignFile().getFilePath());
            Files.createDirectories(output.toAbsolutePath().getParent());
            Assert.assertTrue(javax.imageio.ImageIO.write(image, "png", output.toFile()),
                    data.getValidationMessages().get("generatedFile"));
            Assert.assertNotNull(javax.imageio.ImageIO.read(output.toFile()),
                    data.getValidationMessages().get("generatedFile"));
        } catch (IOException e) {
            throw new UncheckedIOException(data.getValidationMessages().get("generatedFile"), e);
        } finally {
            graphics.dispose();
        }
    }

    @Step("Reuse all-fields design creation and persist this design's independent scenario values")
    public void createAndPersistMultipleDesign(String scenarioKey, DesignData data) {
        String previousName = DesignTestData.getScenario(DesignTestData.CREATE_ALL_FIELDS).getDesignName();
        try {
            createDesignWithAllFields(data);
            String name = executionData.getPrimaryProduct().getFeatures().getLast().getDesignNames().getLast();
            DesignTestData.saveValues(scenarioKey, Map.of("designName", name));
        } finally {
            DesignTestData.saveValues(DesignTestData.CREATE_ALL_FIELDS, Map.of("designName", previousName));
        }
    }

    @Step("Validate saved design category, source and documentation title")
    public void validateMultipleDesignMetadata(DesignData data) {
        var design = new MultipleDesignPage(page);
        assertThat(design.categorySection(data.getCategory())).isVisible();
        Assert.assertEquals(sourceImageFingerprint(design.savedSourceImage().getAttribute("src")),
                data.getSourceImageFingerprint(), data.getValidationMessages().get("savedSource"));
        var attachments = new DesignAttachmentsPage(page);
        assertThat(attachments.savedLink(data.getDesignLink().getUrl())).hasText(data.getDesignLink().getName());
    }

    @Step("Record the labeled source picker images for later saved-source validation")
    public void captureMultipleDesignSourceImages(DesignData scenario) {
        var first = DesignTestData.getScenario(scenario.getDesignScenarios().getFirst());
        openMultipleDesignProduct(first);
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(first.getFeatureName()).click();
        feature.selectPhase(first.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new MultipleDesignPage(page);
        design.chooseCategory(first.getCategory());
        design.proceed(first.getProceedButton()).click();
        for (String key : scenario.getDesignScenarios()) {
            var data = DesignTestData.getScenario(key);
            String image = design.sourcePickerImage(data.getSource()).getAttribute("src");
            Assert.assertNotNull(image, data.getValidationMessages().get("savedSource"));
            DesignTestData.saveValues(key, Map.of("sourceImageFingerprint", sourceImageFingerprint(image)));
        }
    }

    private String sourceImageFingerprint(String image) {
        try {
            return java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(image.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Step("Reload every saved design and prove later creations did not overwrite earlier designs")
    public void validateMultipleDesignsWithinFeature(DesignData scenario) {
        var designs = scenario.getDesignScenarios().stream().map(DesignTestData::getScenario).toList();
        Assert.assertEquals(designs.size(), scenario.getExpectedDesignCount(), scenario.getValidationMessages().get("designCount"));
        Assert.assertEquals(designs.stream().map(DesignData::getCategory).distinct().count(),
                (long) scenario.getExpectedDesignCount(), scenario.getValidationMessages().get("distinctCategories"));
        Assert.assertEquals(designs.stream().map(DesignData::getSource).distinct().count(),
                (long) scenario.getExpectedDesignCount(), scenario.getValidationMessages().get("distinctSources"));
        Assert.assertEquals(designs.stream().map(DesignData::getDesignName).distinct().count(),
                (long) scenario.getExpectedDesignCount(), scenario.getValidationMessages().get("designCount"));
        Assert.assertEquals(executionData.getPrimaryProduct().getFeatures().getLast().getDesignNames().size(),
                scenario.getExpectedDesignCount(), scenario.getValidationMessages().get("designCount"));
        for (var data : designs) {
            Assert.assertEquals(data.getProductName(), scenario.getProductName(), scenario.getValidationMessages().get("sharedFeature"));
            Assert.assertEquals(data.getFeatureName(), scenario.getFeatureName(), scenario.getValidationMessages().get("sharedFeature"));
            openMultipleDesignProduct(data);
            var feature = new DesignFeaturePage(page);
            feature.selectFeature(data.getFeatureName()).click();
            feature.selectPhase(data.getDesignPhase()).click();
            CommonMethods.waitForLoaderToDisappear(page);
            validatePersistedDesign(data);
            validateMultipleDesignMetadata(data);
            verifyDesignAttachments(data);
        }
        openMultipleDesignProduct(designs.getFirst());
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(scenario.getFeatureName()).click();
        feature.selectPhase(scenario.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new MultipleDesignPage(page);
        for (var data : designs) {
            design.expandCategory(data.getCategory()).click();
            assertThat(design.savedCategoryCard(data.getCategory(), data.getDesignName())).isVisible();
        }
        assertThat(design.savedFeatureCards()).hasCount(scenario.getExpectedDesignCount());
        LoggerUtil.LOGGER.info("[MULTIPLE-DESIGN] Verified all {} designs coexist in feature {}",
                designs.size(), scenario.getFeatureName());
    }

    public ProExecutionData multipleDesignExecutionRow(DesignData data) {
        var row = new ProExecutionData();
        row.setPortfolioName(executionData.getPortfolioName());
        var product = new ProductExecutionData();
        product.setProductName(data.getProductName());
        var feature = new FeatureExecutionData();
        feature.setFeatureName(data.getFeatureName());
        feature.getDesignNames().add(data.getDesignName());
        product.getFeatures().add(feature);
        row.getProducts().add(product);
        return row;
    }


    private final Page page;
    private final ProExecutionData executionData;

    private final FeaturePage featurePage;
    private final DesignPage designPage;

    public DesignBuildingBlock(
            Page page,
            ProExecutionData executionData) {

        this.page = page;
        this.executionData = executionData;

        this.featurePage = new FeaturePage(page);
        this.designPage = new DesignPage(page);
    }

    public void createDesign(DesignData designData) {
        page.waitForTimeout(2000);

        LoggerUtil.LOGGER.info(
                "========== Design Started ==========");


        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        FeatureExecutionData featureExecutionData =
                productExecutionData.getFeatures()
                        .get(productExecutionData.getFeatures().size() - 1);

        String featureName =
                featureExecutionData.getFeatureName();

        CommonMethods.clickButton(page, featureName).click();

        page.waitForTimeout(2000);

        featurePage.selectPhase("Design").click();

        page.waitForTimeout(2000);

        CommonMethods.waitForLoaderToDisappear(page);

        designPage.chooseCategory(
                designData.getCategory());

        CommonMethods.clickButton(page, "Proceed").click();

        designPage.selectDesignSource(
                designData.getSource()).click();

        String designName =
                CommonMethods.generateUniqueTitle(
                        designData.getName());

        designPage.name().fill(designName);

        designPage.description().fill(
                designData.getDescription());

        designPage.create().click();

        page.waitForTimeout(2000);

        CommonMethods.waitForLoaderToDisappear(page);

        LoggerUtil.LOGGER.info(
                "========== Design Completed ==========");

        storeDesignExecutionData(designName);
    }

    private void storeDesignExecutionData(String designName) {

        ProductExecutionData productExecutionData =
                executionData.getProducts()
                        .get(executionData.getProducts().size() - 1);

        FeatureExecutionData featureExecutionData =
                productExecutionData.getFeatures()
                        .get(productExecutionData.getFeatures().size() - 1);

        featureExecutionData.getDesignNames()
                .add(designName);
    }

    @Step("Create a private Design product in the configured existing portfolio")
    public void createPrivateDesignProduct(DesignData data) {
        var product = data.getProduct();
        var products = new ProductBuildingBlock(page, executionData);
        products.openNewProductFromProducts();
        products.selectProductType(product.getProductType());
        var creation = new ProductAddProjectDetailsPage(page);
        creation.portfolioDropdown().click();
        creation.portfolioSearch().fill(product.getPortfolioName());
        creation.portfolioOption(product.getPortfolioName()).click();
        executionData.setPortfolioName(product.getPortfolioName());
        products.createNewProduct(product);
        ProValidation.validateSuccessMessage(page, product.getProductCreatedMessage());
        products.saveOrSkipProductAdditionalDetails(product.getSkipButton());
        products.chooseFeatureCreationOption();
        ProductValidation.validateProductDetails(page, executionData, product);
        DesignTestData.saveValues(Map.of("productName", executionData.getPrimaryProduct().getProductName()));
    }

    @Step("Reuse the saved feature or create and persist a mandatory feature")
    public void ensureDesignFeature(DesignData data) {
        var products = new ProductBuildingBlock(page, executionData);
        products.navigateToFeatureTab();
        CommonMethods.waitForLoaderToDisappear(page);
        var features = new FeatureBuildingBlock(page, executionData, new DesignFeaturePage(page));
        String savedName = data.getFeatureName();
        if (savedName != null && !savedName.isBlank()) {
            try {
                new ProductFeatureTabPage(page).matchingFeature(savedName).first()
                        .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
                                .setTimeout(data.getFeatureLookupTimeoutMs()));
                if (executionData.getPrimaryProduct().getFeatures().stream()
                        .noneMatch(feature -> savedName.equals(feature.getFeatureName()))) {
                    features.storeFeatureExecutionDataForProduct(savedName, executionData.getPrimaryProduct());
                }
                LoggerUtil.LOGGER.info("[DESIGN-BLOCK] Reusing saved feature {}", savedName);
                return;
            } catch (TimeoutError missingFeature) {
                LoggerUtil.LOGGER.info("[DESIGN-BLOCK] Saved feature is absent; creating a new feature: {}", savedName);
            }
        }
        createAndPersistDesignFeature(data);
    }

    @Step("Create a mandatory feature and persist its environment-specific name")
    public void createAndPersistDesignFeature(DesignData data) {
        var features = new FeatureBuildingBlock(page, executionData, new DesignFeaturePage(page));
        String productId = URI.create(page.url()).getPath();
        productId = productId.substring(productId.lastIndexOf('/') + 1);
        features.createFeatureUnderProduct(data.getFeatureData(), executionData.getPrimaryProduct());
        features.skipOrAddFeatureAdditionalDetails(data.getFeatureDetailsAction());
        String name = executionData.getPrimaryProduct().getFeatures().getLast().getFeatureName();
        DesignTestData.saveValues(Map.of("featureName", name));
        data = DesignTestData.getScenario();
        Assert.assertEquals(data.getFeatureName(), name, data.getValidationMessages().get("savedFeature"));
        if (!URI.create(page.url()).getPath().startsWith("/projects/project-view/")) {
            new ResilientDesignPage(page).returnToProduct(productId, data.getGoToProductButton()).click();
        }
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(new ProductFeatureTabPage(page).matchingFeature(data.getFeatureName()).first()).isVisible();
    }

    @Step("Create the configured design artifact for the persisted feature")
    public void createPersistedFeatureDesign(DesignData data) {
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(data.getFeatureName()).click();
        feature.selectPhase(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new ResilientDesignPage(page);
        design.chooseCategory(data.getCategory());
        design.proceed(data.getProceedButton()).click();
        design.selectDesignSource(data.getSource()).click();
        String name = CommonMethods.generateUniqueTitle(data.getName());
        design.name().fill(name);
        design.description().fill(data.getDescription());
        assertThat(design.name()).hasValue(name);
        assertThat(design.description()).hasValue(data.getDescription());
        design.create().click();
        ProValidation.validateSuccessMessage(page, data.getSavedMessage());
        storeDesignExecutionData(name);
        DesignTestData.saveValues(Map.of("designName", name));
    }

    @Step("Open the created design slide and validate its saved fields")
    public void validatePersistedDesign(DesignData data) {
        var design = new ResilientDesignPage(page);
        design.expandCategory(data.getCategory()).click();
        design.designCard(data.getDesignName()).click();
        assertThat(design.designDetailsTitle()).hasText(data.getDesignName(),
                new com.microsoft.playwright.assertions.LocatorAssertions.HasTextOptions()
                        .setTimeout(data.getLoadTimeoutMs()));
        assertThat(design.designDetailsDescription()).hasText(data.getDescription(),
                new com.microsoft.playwright.assertions.LocatorAssertions.HasTextOptions()
                        .setTimeout(data.getLoadTimeoutMs()));
    }

    @Step("Search the Design product, delete its feature, then delete the product")
    public void deleteDesignProductAndFeature(DesignData data) {
        var products = new ProductBuildingBlock(page, executionData);
        products.openProduct(data.getProductName(), data.getProduct());
        products.navigateToFeatureTab();
        var features = new FeatureBuildingBlock(page, executionData, new DesignFeaturePage(page));
        features.featureName = data.getFeatureName();
        features.viewFeatureDetails();
        features.deleteFeature(data.getProduct());
        ProValidation.validateSuccessMessage(page, data.getProduct().getFeatureDeletedMessage());
        products.validateLinkedFeatureDeleted(data.getFeatureName(), data.getProduct());
        products.deleteProduct(data.getProductName(), data.getProduct());
        ProValidation.validateSuccessMessage(page, data.getProduct().getProductDeletedMessage());
        products.validateRecordedProductDeleted(data.getProductName(), data.getProduct());
    }

    @Step("Create a Design artifact including a documentation link and a design file")
    public void createDesignWithAllFields(DesignData data) {
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(data.getFeatureName()).click();
        feature.selectPhase(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new ResilientDesignPage(page);
        design.chooseCategory(data.getCategory());
        design.proceed(data.getProceedButton()).click();
        design.selectDesignSource(data.getSource()).click();
        String name = CommonMethods.generateUniqueTitle(data.getName());
        design.name().fill(name);
        design.description().fill(data.getDescription());
        assertThat(design.name()).hasValue(name);
        assertThat(design.description()).hasValue(data.getDescription());
        addDesignDocumentationLink(data);
        addDesignFile(data);
        design.create().click();
        ProValidation.validateSuccessMessage(page, data.getSavedMessage());
        storeDesignExecutionData(name);
        DesignTestData.saveValues(DesignTestData.CREATE_ALL_FIELDS, Map.of("designName", name));
    }

    @Step("Add and verify the Design documentation link")
    public void addDesignDocumentationLink(DesignData data) {
        var attachments = new DesignAttachmentsPage(page);
        attachments.title().fill(data.getDesignLink().getName());
        attachments.linkAddress().fill(data.getDesignLink().getUrl());
        assertThat(attachments.addLink()).isEnabled();
        attachments.addLink().click();
        assertThat(attachments.stagedAttachment(data.getDesignLink().getName())).isVisible();
    }

    @Step("Upload and verify the stored Design file")
    public void addDesignFile(DesignData data) {
        Path image = Path.of(data.getDesignFile().getFilePath());
        var attachments = new DesignAttachmentsPage(page);
        attachments.filesTab().click();
        attachments.title().fill(data.getFileTitle());
        // Reuse the portfolio Browse locator and the framework's file-chooser upload pattern.
        var browse = new ProductPortfolioAdditionalDetailsOthersTabPage(page).browseThisComputerButton();
        var chooser = page.waitForFileChooser(browse::click);
        chooser.setFiles(image);
        assertThat(attachments.addFile()).isEnabled();
        attachments.addFile().click();
        assertThat(attachments.stagedAttachment(data.getFileTitle())).isVisible();
    }

    @Step("Verify the saved design link opens and the file downloads correctly")
    public void verifyDesignAttachments(DesignData data) {
        var attachments = new DesignAttachmentsPage(page);
        var opened = page.waitForPopup(new Page.WaitForPopupOptions().setTimeout(data.getLoadTimeoutMs()),
                () -> attachments.linkIcon(data.getDesignLink().getUrl()).click());
        try {
            opened.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);
            Assert.assertFalse(opened.isClosed(), data.getDesignLink().getLinkOpenMessage());
            Assert.assertEquals(URI.create(opened.url()).getHost(),
                    URI.create(data.getDesignLink().getUrl()).getHost(), data.getDesignLink().getLinkOpenMessage());
        } finally {
            opened.close();
        }
        var download = page.waitForDownload(new Page.WaitForDownloadOptions().setTimeout(data.getLoadTimeoutMs()),
                () -> attachments.downloadFile(data.getFileTitle()).click());
        Assert.assertNull(download.failure(), data.getDesignFile().getDownloadFailureMessage());
        Path downloaded = Path.of(data.getDownloadDirectory(), data.getDesignFile().getName());
        try {
            Files.createDirectories(downloaded.toAbsolutePath().getParent());
            download.saveAs(downloaded);
            Assert.assertTrue(Files.size(downloaded) > 0, data.getValidationMessages().get("downloadContents"));
            Assert.assertEquals(Files.mismatch(Path.of(data.getDesignFile().getFilePath()), downloaded), -1L,
                    data.getValidationMessages().get("downloadContents"));
        } catch (IOException e) {
            throw new UncheckedIOException(data.getDesignFile().getDownloadFailureMessage(), e);
        }
    }

    @Step("Create a Business Requirement with all configured fields for the Design feature")
    public void createBusinessRequirementForDesign(DesignData data) {
        var feature = new DesignFeaturePage(page);
        feature.selectFeature(data.getFeatureName()).click();
        feature.selectPhase(data.getDefinePhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var requirements = new ResilientBusinessRequirementPage(page);
        requirements.tab(data.getBusinessRequirementsTab()).click();
        requirements.action(data.getNewBusinessRequirementButton()).click();
        requirements.action(data.getCreateNewRequirementButton()).click();
        String name = CommonMethods.generateUniqueTitle(data.getBusinessRequirement().getTitle());
        requirements.title().fill(name);
        requirements.description().fill(data.getBusinessRequirement().getDescription());
        assertThat(requirements.title()).hasValue(name);
        assertThat(requirements.description()).hasText(data.getBusinessRequirement().getDescription());
        var dropdowns = new NewReleasePage(page);
        data.getRequirementDropdowns().forEach(dropdowns::selectDropDown);
        data.getRequirementTextFields().forEach((field, value) -> {
            requirements.field(field).fill(value);
            assertThat(requirements.field(field)).hasValue(value);
        });
        data.getAdditionalAttributes().forEach((attribute, value) -> {
            requirements.field("attributeName").fill(attribute);
            requirements.field("attributeValue").fill(value);
            requirements.addAttribute().click();
            assertThat(requirements.attributeValue(attribute)).isVisible();
            assertThat(requirements.attributeValue(value)).isVisible();
        });
        requirements.action(data.getCreateRequirementButton()).click();
        ProValidation.validateSuccessMessage(page, data.getBusinessRequirementSavedMessage());
        executionData.getPrimaryProduct().getFeatures().getLast().getBusinessRequirementNames().add(name);
        DesignTestData.saveValues(DesignTestData.ADD_BUSINESS_REQUIREMENT, Map.of("businessRequirementName", name));
        returnToBusinessRequirementFeatureWorkflows(data);
    }

    @Step("Return to the saved feature and verify its workflows after requirement creation")
    public void returnToBusinessRequirementFeatureWorkflows(DesignData data) {
        page.goBack(new Page.GoBackOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        CommonMethods.waitForLoaderToDisappear(page);
        new DesignFeaturePage(page).selectFeature(data.getFeatureName()).click();
        new DesignFeaturePage(page).workflowDetails(data.getViewFeatureDetailsButton()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        assertThat(new ResilientBusinessRequirementPage(page).workflows(data.getWorkflowsTab())).isVisible();
    }

    @Step("Create a Sketch design linked to the persisted Business Requirement")
    public void createDesignLinkedToBusinessRequirement(DesignData data) {
        var workflows = new ResilientBusinessRequirementPage(page);
        assertThat(workflows.workflows(data.getWorkflowsTab())).isVisible();
        workflows.designWorkflow(data.getDesignPhase()).click();
        CommonMethods.waitForLoaderToDisappear(page);
        var design = new ResilientDesignPage(page);
        design.chooseCategory(data.getCategory());
        design.proceed(data.getProceedButton()).click();
        design.selectDesignSource(data.getSource()).click();
        String name = CommonMethods.generateUniqueTitle(data.getName());
        design.name().fill(name);
        design.description().fill(data.getDescription());
        design.businessRequirementControl().click();
        design.businessRequirementSearch().fill(data.getBusinessRequirementName());
        design.selectDropdownValue(data.getBusinessRequirementName()).click();
        assertThat(design.businessRequirementControl()).containsText(data.getBusinessRequirementName());
        design.create().click();
        ProValidation.validateSuccessMessage(page, data.getSavedMessage());
        storeDesignExecutionData(name);
        DesignTestData.saveValues(DesignTestData.ADD_BUSINESS_REQUIREMENT, Map.of("designName", name));
    }

    @Step("Verify the saved requirement appears as the Design Business Case")
    public void validateDesignBusinessCase(DesignData data) {
        var design = new ResilientDesignPage(page);
        assertThat(design.businessCaseLabel(data.getBusinessCaseLabel())).isVisible();
        assertThat(design.businessCaseValue(data.getBusinessRequirementName())).isVisible();
    }


}
