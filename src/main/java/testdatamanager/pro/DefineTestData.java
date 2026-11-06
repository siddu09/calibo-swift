package testdatamanager.pro;

import UI.PRO.datahelper.DefineData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.JSONUtils.JsonDataReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Environment-aware persistence for Define user-story test data. */
public final class DefineTestData {
    public static final String CREATE_DEFINE = "CreateNewUserStoryWithMandatoryFields";
    public static final String CREATE_TASK = "CreateNewTaskWithMandatoryFields";
    public static final String CREATE_EPIC = "CreateNewEpicWithMandatoryFields";
    public static final String CREATE_FEATURE = "CreateNewFeatureWithMandatoryFields";
    public static final String ADD_EXISTING_STORY = "AddExistingUserStory";
    private static final String FILE = environmentFile();
    private static final Path SOURCE = Path.of("src/test/resources", FILE);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DefineTestData() {}

    private static String environmentFile() {
        return "testdata/files/Pro/define" + ("uat".equalsIgnoreCase(System.getProperty("env", "qa"))
                ? "_UAT" : "") + ".json";
    }

    public static DefineData getScenario() {
        return getScenario(CREATE_DEFINE);
    }

    public static DefineData getScenario(String scenario) {
        return JsonDataReader.read(FILE, scenario, DefineData.class);
    }

    public static synchronized void saveFeatureName(String scenario, String featureName) {
        try {
            ObjectNode root = (ObjectNode) MAPPER.readTree(SOURCE.toFile());
            ObjectNode data = (ObjectNode) root.required(scenario);
            data.put("featureName", featureName);
            if (data.has("featureData") && data.get("featureData").isObject()) {
                ((ObjectNode) data.get("featureData")).put("title", featureName);
            }
            Path temporary = Files.createTempFile(SOURCE.getParent(), "define-", ".json");
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
                Files.move(temporary, SOURCE, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot persist Define feature name in " + SOURCE, e);
        }
    }
}
