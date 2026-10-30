package testdatamanager.pro;

import UI.PRO.datahelper.DesignData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public final class DesignTestData {
    public static final String SEARCH_DESIGN = "searchDesign";

    public static synchronized void saveSearchDesignValues(String scenarioName, Map<String, String> values) {
        for (String filename : java.util.List.of("design.json", "design_UAT.json")) {
            Path source = Path.of("src/test/resources/testdata/files/Pro", filename);
            try {
                ObjectNode root = (ObjectNode) MAPPER.readTree(source.toFile());
                ObjectNode scenario = (ObjectNode) root.required(scenarioName);
                values.forEach(scenario::put);
                Path temporary = Files.createTempFile(source.getParent(), "design-", ".json");
                try {
                    MAPPER.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
                    Files.move(temporary, source, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } finally {
                    Files.deleteIfExists(temporary);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cannot persist search Design data: " + source, e);
            }
        }
    }
    public static final String VALIDATE_DOCUMENT_DOWNLOAD = "validateDocumentDownloadedAndValidation";
    public static final String CREATE_DESIGN = "createDesignProductWithMandatoryFields";
    public static final String CREATE_ALL_FIELDS = "createDesignWithAllFields";
    public static final String ADD_BUSINESS_REQUIREMENT = "addBusinessRequirementToDesign";
    public static final String CREATE_MULTIPLE_DESIGNS = "createMultipleDesignsWithinFeature";
    public static final String EDIT_ALL_FIELDS = "editDesignWithAllFields";
    public static final String EDIT_UPDATED_FIELDS = "editDesignWithAllFields_updated";
    public static final String VALIDATE_MANDATORY_FIELDS = "validateDesignMandatoryFields";
    public static final String DELETE_DESIGN = "deleteDesign";
    public static final String VALIDATE_LINKS_NAVIGATION = "validateDesignLinksNavigation";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String RESOURCE = "testdata/files/Pro/design"
            + ("uat".equalsIgnoreCase(System.getProperty("env", "qa")) ? "_UAT" : "") + ".json";
    private static final Path SOURCE = Path.of("src/test/resources", RESOURCE);

    private DesignTestData() {}

    public static synchronized void saveDocumentFeatureNameForBothEnvironments(String featureName) {
        for (String filename : java.util.List.of("design.json", "design_UAT.json")) {
            Path source = Path.of("src/test/resources/testdata/files/Pro", filename);
            try {
                ObjectNode root = (ObjectNode) MAPPER.readTree(source.toFile());
                ((ObjectNode) root.required(VALIDATE_DOCUMENT_DOWNLOAD)).put("featureName", featureName);
                Path temporary = Files.createTempFile(source.getParent(), "design-", ".json");
                try {
                    MAPPER.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
                    Files.move(temporary, source, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } finally {
                    Files.deleteIfExists(temporary);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cannot persist document Design feature name: " + source, e);
            }
        }
    }

    public static DesignData getScenario(String scenarioName) {
        try (var input = Files.exists(SOURCE) ? Files.newInputStream(SOURCE)
                : DesignTestData.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (input == null) throw new IOException("Missing design data: " + RESOURCE);
            var root = MAPPER.readTree(input);
            ObjectNode data = ((ObjectNode) root.required(CREATE_DESIGN)).deepCopy();
            data.setAll((ObjectNode) root.required(scenarioName));
            return MAPPER.treeToValue(data, DesignData.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read design data: " + SOURCE, e);
        }
    }

    public static synchronized void saveValues(String scenarioName, Map<String, String> values) {
        try {
            ObjectNode root = (ObjectNode) MAPPER.readTree(SOURCE.toFile());
            ObjectNode scenario = (ObjectNode) root.required(scenarioName);
            values.forEach(scenario::put);
            Path temporary = Files.createTempFile(SOURCE.getParent(), "design-", ".json");
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
                Files.move(temporary, SOURCE, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot persist design data: " + SOURCE, e);
        }
    }

    public static DesignData getScenario() {
        try (var input = Files.exists(SOURCE) ? Files.newInputStream(SOURCE)
                : DesignTestData.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (input == null) throw new IOException("Missing design data: " + RESOURCE);
            return MAPPER.treeToValue(MAPPER.readTree(input).required(CREATE_DESIGN), DesignData.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read design data: " + SOURCE, e);
        }
    }

    public static synchronized void saveValues(Map<String, String> values) {
        try {
            ObjectNode root = (ObjectNode) MAPPER.readTree(SOURCE.toFile());
            ObjectNode scenario = (ObjectNode) root.required(CREATE_DESIGN);
            values.forEach(scenario::put);
            Path temporary = Files.createTempFile(SOURCE.getParent(), "design-", ".json");
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
                Files.move(temporary, SOURCE, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot persist design data: " + SOURCE, e);
        }
    }
}
