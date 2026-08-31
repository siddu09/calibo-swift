package utils.JSONUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public final class JsonDataReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonDataReader() {
        // Utility class
    }

    public static <T> T read(
            String filePath,
            String scenario,
            Class<T> clazz) {

        try (InputStream inputStream =
                     JsonDataReader.class
                             .getClassLoader()
                             .getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "Test data file not found: " + filePath
                );
            }

            JsonNode rootNode =
                    OBJECT_MAPPER.readTree(inputStream);

            JsonNode scenarioNode =
                    rootNode.get(scenario);

            if (scenarioNode == null) {
                throw new RuntimeException(
                        "Scenario '" + scenario
                                + "' not found in file: "
                                + filePath
                );
            }

            return OBJECT_MAPPER.treeToValue(
                    scenarioNode,
                    clazz
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read test data. "
                            + "File: " + filePath
                            + ", Scenario: " + scenario,
                    e
            );
        }
    }
}