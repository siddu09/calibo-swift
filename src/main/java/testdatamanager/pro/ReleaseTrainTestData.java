package testdatamanager.pro;

import UI.PRO.datahelper.ReleaseTrainData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public final class ReleaseTrainTestData {
    public static final String CREATE_RELEASE_TRAIN = "CreatenewReleaseTrainAndaddReleaseWithAllfields";
    public static final String CREATE_MULTIPLE_RELEASES = "CreatenewReleaseTrainAndAddMultipleReleases";
    public static final String EDIT_RELEASE = "EditRelease";
    public static final String EDIT_RELEASE_TRAIN = "EditReleaseTrain";
    public static final String DELETE_RELEASE = "DeleteRelease";
    public static final String DELETE_RELEASE_TRAIN = "DeleteReleaseTrain";
    public static final String VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE_TRAIN = "validateMandatoryFieldErrorsForReleaseTrain";
    public static final String VALIDATE_MANDATORY_FIELD_ERRORS_FOR_RELEASE = "validateMandatoryFieldErrorsForRelease";
    public static final String VALIDATE_PRODUCT_ASSIGNED_TO_RELEASE_IN_RELEASE_PAGE = "validationOfProductAssignedToreleaseInReleasePage";
    public static final String VALIDATE_RELEASE_NOTES_ADDED_IN_PRODUCT = "ValidateReleaseNotesAddedINProduct";
    public static final String VERIFY_NAVIGATION_FROM_RELEASE_TO_PRODUCT = "verifyNavigationFromReleaseToProduct";
    public static final String ADD_FEATURE_TO_RELEASE = "addFeatureToRelease";
    public static final String VERIFY_RELEASE_DATE_FILTER_APPLIED_SUCCESSFULLY_ON_DEPENDENCIES = "verifyReleaseDateFilterAppliedSuccessfullyOnDependencies";
    public static final String VERIFY_RELEASE_RELATIONSHIPS = "verifyDependsOnAndDependentReleasesOnSelectingRelease";
    public static final String VERIFY_RELEASES_IN_DEPENDENCY_GRAPH = "verifyReleasesInDependencyGraph";
    public static final String VERIFY_ADD_RELEASE_DEPENDENCIES = "verifyAddReleaseDependencies";
    public static final String ADD_PRODUCT_TO_RELEASE = "AddProductToRelease";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String RESOURCE = "testdata/files/Pro/releaseTrain"
            + ("uat".equalsIgnoreCase(System.getProperty("env", "qa")) ? "_UAT" : "") + ".json";
    private static final Path SOURCE = Path.of("src/test/resources", RESOURCE);

    private ReleaseTrainTestData() {}

    public static ReleaseTrainData getScenario() {
        return getScenario(CREATE_RELEASE_TRAIN);
    }

    public static ReleaseTrainData getScenario(String scenarioName) {
        try (var input = Files.exists(SOURCE) ? Files.newInputStream(SOURCE)
                : ReleaseTrainTestData.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (input == null) {
                throw new IOException("Missing release train data: " + RESOURCE);
            }
            var root = MAPPER.readTree(input);
            ObjectNode scenario = ((ObjectNode) root.required(CREATE_RELEASE_TRAIN)).deepCopy();
            scenario.setAll((ObjectNode) root.required(scenarioName));
            ReleaseTrainData data = MAPPER.treeToValue(scenario, ReleaseTrainData.class);
            data.setScenarioName(scenarioName);
            return data;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read release train data: " + SOURCE, e);
        }
    }

    public static synchronized void saveValues(Map<String, String> values) {
        saveValues(CREATE_RELEASE_TRAIN, values);
    }

    public static synchronized void saveValues(String scenarioName, Map<String, ?> values) {
        try {
            ObjectNode root = (ObjectNode) MAPPER.readTree(SOURCE.toFile());
            ObjectNode scenario = (ObjectNode) root.required(scenarioName);
            values.forEach((key, value) -> scenario.set(key, MAPPER.valueToTree(value)));
            Path temporary = Files.createTempFile(SOURCE.getParent(), "releaseTrain-", ".json");
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
                Files.move(temporary, SOURCE, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot persist release train data: " + SOURCE, e);
        }
    }
}

