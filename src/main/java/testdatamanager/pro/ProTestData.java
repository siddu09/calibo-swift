package testdatamanager.pro;

import UI.PRO.datahelper.*;
import utils.JSONUtils.JsonDataReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class ProTestData {

    private static final String PORTFOLIO_FILE =
            "testdata/files/Pro/portfolio.json";

    private static final String PRODUCT_FILE =
            "testdata/files/Pro/product.json";

    private static final String FEATURE_FILE =
            "testdata/files/Pro/feature.json";

    private static final String DESIGN_FILE =
            "testdata/files/Pro/design.json";

    private static final String DEFINE_FILE =
            "testdata/files/Pro/define.json";

    private ProTestData() {
        // Utility class
    }

    public static PortfolioData getPortfolio(String scenario) {
        return JsonDataReader.read(
                PORTFOLIO_FILE,
                scenario,
                PortfolioData.class
        );
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Path PRODUCT_SOURCE = Path.of("src/test/resources", PRODUCT_FILE);
    public static final String CREATE_PRODUCT = "createProduct";
    public static final String PRODUCT_DEPENDENCY = "createPrivateProductAndAddDependencyBetweenProducts";

    public static ProductData getProduct(String scenario) {
        // Read persisted source data first, including updates from earlier tests in this run.
        try (java.io.InputStream input = Files.exists(PRODUCT_SOURCE)
                ? Files.newInputStream(PRODUCT_SOURCE)
                : ProTestData.class.getClassLoader().getResourceAsStream(PRODUCT_FILE)) {
            if (input == null) {
                throw new IOException("Product test data resource is missing: " + PRODUCT_FILE);
            }
            ObjectNode root = (ObjectNode) MAPPER.readTree(input);
            ObjectNode scenarioData = ((ObjectNode) root.required(CREATE_PRODUCT)).deepCopy();
            scenarioData.setAll((ObjectNode) root.required(scenario));
            return MAPPER.treeToValue(scenarioData, ProductData.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read product data: " + PRODUCT_SOURCE, e);
        }
    }

    public static synchronized void saveProductPortfolioName(String portfolioName) {
        saveProductValues(CREATE_PRODUCT, java.util.Map.of("portfolioName", portfolioName));
    }

    public static synchronized void saveDependencyProductNames(String product1Name, String product2Name) {
        saveProductValues(PRODUCT_DEPENDENCY, java.util.Map.of(
                "product1Name", product1Name, "product2Name", product2Name));
    }

    private static void saveProductValues(String scenario, java.util.Map<String, String> values) {
        try {
            ObjectNode root = (ObjectNode) MAPPER.readTree(PRODUCT_SOURCE.toFile());
            ObjectNode scenarioData = (ObjectNode) root.required(scenario);
            values.forEach(scenarioData::put);
            Path temporary = Files.createTempFile(PRODUCT_SOURCE.getParent(), "product-", ".json");
            try {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), root);
                Files.move(temporary, PRODUCT_SOURCE,
                        StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot persist product data: " + PRODUCT_SOURCE, e);
        }
    }

    public static FeatureData getFeature(String scenario) {

        return JsonDataReader.read(
                FEATURE_FILE,
                scenario,
                FeatureData.class
        );
    }

    public static DesignData getDesign(String scenario) {

        return JsonDataReader.read(
                DESIGN_FILE,
                scenario,
                DesignData.class
        );
    }

    public static DefineData getDefine(String scenario) {

        return JsonDataReader.read(
                DEFINE_FILE,
                scenario,
                DefineData.class
        );
    }
}