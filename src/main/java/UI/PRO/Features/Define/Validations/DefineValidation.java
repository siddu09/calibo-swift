package UI.PRO.Features.Define.Validations;

import UI.PRO.datahelper.DefineData;
import com.microsoft.playwright.Page;
import pages.PRO.Features.Define.BusinessRequirementDetailsPage;
import pages.PRO.Features.Define.DefinePage;
import testdatamanager.pro.ProExecutionData;
import org.testng.Assert;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class DefineValidation {

    private DefineValidation() {
        // Utility class
    }

    public static void validateUserFeedbackExecutionData(
            ProExecutionData executionData, DefineData defineData) {
        Assert.assertNotNull(executionData.getCreatedStoryId(),
                defineData.getValidationMessages().get("feedbackTitle"));
        Assert.assertFalse(executionData.getCreatedStoryId().isBlank(),
                defineData.getValidationMessages().get("feedbackTitle"));
        Assert.assertNotNull(executionData.getProductId(),
                defineData.getValidationMessages().get("productId"));
        Assert.assertFalse(executionData.getProductId().isBlank(),
                defineData.getValidationMessages().get("productId"));
        Assert.assertNotNull(executionData.getFeatureId(),
                defineData.getValidationMessages().get("featureId"));
        Assert.assertFalse(executionData.getFeatureId().isBlank(),
                defineData.getValidationMessages().get("featureId"));
    }

    public static void validateUserFeedbackDetails(
            Page page, ProExecutionData executionData, DefineData defineData) {
        BusinessRequirementDetailsPage details = new BusinessRequirementDetailsPage(page);

        assertThat(details.userFeedbackTitle(executionData.getCreatedStoryId()))
                .hasText(executionData.getCreatedStoryId());
        assertThat(details.userFeedbackDescription()).hasText(defineData.getDescription());
        assertThat(details.fetchPriority()).hasText(defineData.getPriority());
        assertThat(details.fetchSource()).hasText(defineData.getSource());
        assertThat(details.fetchImpact()).hasText(defineData.getImpact());

        String storedFeedbackTitle = executionData.getPrimaryProduct().getFeatures().getLast()
                .getBusinessRequirementNames().getLast();
        Assert.assertEquals(storedFeedbackTitle, executionData.getCreatedStoryId(),
                defineData.getValidationMessages().get("correctFeature"));
    }

    public static void validateMandatoryFieldErrors(Page page, DefineData data) {
        DefinePage form = new DefinePage(page);
        for (String message : data.getMandatoryFieldErrors()) {
            assertThat(form.feedbackValidationMessage(message)).isVisible();
        }
        assertThat(form.createUserFeedbackButton()).isVisible();
    }

    public static void validateEditedDefineItem(Page page, String updatedTitle, DefineData data) {
        BusinessRequirementDetailsPage details = new BusinessRequirementDetailsPage(page);
        assertThat(details.userFeedbackTitle(updatedTitle)).hasText(updatedTitle);
        assertThat(details.userFeedbackDescription()).hasText(data.getDescription());
    }

    public static void validateFeedbackAndConvertedRequirementSearchResults(
            Page page, ProExecutionData executionData) {
        var results = new DefinePage(page).feedbackAndRequirementSearchResults(executionData.getCreatedStoryId());
        assertThat(results).hasCount(2);
        assertThat(results.nth(0)).isVisible();
        assertThat(results.nth(1)).isVisible();
    }

    public static void validateUserFeedbackUnderCurrentFeature(
            Page page, ProExecutionData executionData, DefineData defineData) {
        String featureId = queryParameter(page.url(), "streamId");
        Assert.assertEquals(featureId, executionData.getFeatureId(),
                defineData.getValidationMessages().get("correctFeature"));
        assertThat(new DefinePage(page).selectBusinessRequirement(executionData.getCreatedStoryId()))
                .isVisible();
    }

    public static void validateUserFeedbackForm(
            Page page, DefineData defineData, String generatedTitle) {
        DefinePage form = new DefinePage(page);

        assertThat(form.title()).hasValue(generatedTitle);
        assertThat(form.description()).hasText(defineData.getDescription());
        assertThat(form.addUserName()).hasValue(defineData.getUserName());
        assertThat(form.feedbackDropdown("Customer Name")).containsText(defineData.getCustomerName());
        assertThat(form.feedbackDropdown("Priority")).containsText(defineData.getPriority());
        assertThat(form.feedbackDropdown("Source")).containsText(defineData.getSource());
        assertThat(form.feedbackDropdown("Impact")).containsText(defineData.getImpact());
        assertThat(form.feedbackDropdown("Status")).containsText(defineData.getStatus());
    }

    public static void validateBusinessRequirementForm(
            Page page, DefineData defineData, String generatedTitle) {
        DefinePage form = new DefinePage(page);

        assertThat(form.title()).hasValue(generatedTitle);
        assertThat(form.description()).hasText(defineData.getDescription());
        assertThat(form.feedbackDropdown("Priority")).containsText(defineData.getPriority());
        assertThat(form.feedbackDropdown("Source")).containsText(defineData.getSource());
        assertThat(form.feedbackDropdown("Impact")).containsText(defineData.getImpact());
        assertThat(form.feedbackDropdown("Status")).containsText(defineData.getStatus());
    }

    public static void validateConfluencePageTitle(Page page, DefineData defineData) {
        assertThat(new DefinePage(page).confluencePageTitle())
                .hasValue(defineData.getConfluencePageTitle());
    }

    public static void validateBusinessRequirementConfluenceLink(Page page, DefineData defineData) {
        assertThat(new BusinessRequirementDetailsPage(page)
                .confluencePageLink(defineData.getConfluencePageTitle()))
                .containsText(defineData.getConfluencePageTitle());
    }

    public static void validateUserFeedbackComment(Page page, DefineData defineData) {
        String savedComment = new BusinessRequirementDetailsPage(page)
                .savedComment(defineData.getComment()).innerText();
        Assert.assertTrue(savedComment.contains(defineData.getComment()),
                defineData.getValidationMessages().get("commentSaved"));
    }

    public static void validateUserFeedbackReview(Page page, DefineData defineData) {
        String savedReview = new BusinessRequirementDetailsPage(page)
                .savedReview(defineData.getReviewComment()).innerText();
        Assert.assertTrue(savedReview.contains(defineData.getReviewComment()),
                defineData.getValidationMessages().get("reviewSaved"));
    }

    private static String queryParameter(String url, String name) {
        String query = URI.create(url).getRawQuery();
        if (query == null) return null;
        for (String pair : query.split("&")) {
            String[] entry = pair.split("=", 2);
            if (entry.length == 2 && name.equals(entry[0])) {
                return URLDecoder.decode(entry[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    public static void validateBusinessRequirementDetails(
            Page page,
            ProExecutionData executionData,
            DefineData defineData) {

        BusinessRequirementDetailsPage
                businessRequirementDetailsPage =
                new BusinessRequirementDetailsPage(page);

        // Get the currently created Business Requirement
        var currentProduct =
                executionData.getProducts()
                        .get(
                                executionData.getProducts().size() - 1
                        );

        var currentFeature =
                currentProduct.getFeatures()
                        .get(
                                currentProduct.getFeatures().size() - 1
                        );

        String currentBusinessRequirementTitle =
                currentFeature
                        .getBusinessRequirementNames()
                        .get(
                                currentFeature
                                        .getBusinessRequirementNames()
                                        .size() - 1
                        );

        // Validate Business Requirement Title
        assertThat(
                businessRequirementDetailsPage
                        .fetchBusinessRequirementTitle()
        ).hasText(
                currentBusinessRequirementTitle
        );

        // Validate Priority
        assertThat(
                businessRequirementDetailsPage
                        .fetchPriority()
        ).hasText(
                defineData.getPriority()
        );

        // Validate Source
        assertThat(
                businessRequirementDetailsPage
                        .fetchSource()
        ).hasText(
                defineData.getSource()
        );

        // Validate Impact
        assertThat(
                businessRequirementDetailsPage
                        .fetchImpact()
        ).hasText(
                defineData.getImpact()
        );

        // Validate Description
        assertThat(
                businessRequirementDetailsPage
                        .fetchDescription()
        ).hasText(
                defineData.getDescription()
        );
    }
}
