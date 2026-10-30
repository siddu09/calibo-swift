package testdatamanager.pro;

import UI.PRO.datahelper.DesignData;
import UI.PRO.datahelper.FeatureData;
import UI.PRO.datahelper.MaturityAssessmentData;
import UI.PRO.datahelper.ProductData;
import org.testng.Assert;
import utils.FilloUtil;
import utils.LoggerUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Writes PRO test execution data (portfolio/product/feature/design details) to the
 * ProExecutionData.xlsx workbook.
 *
 * Every test case gets exactly ONE row: if a row for the given TestCase already exists
 * it is updated in place (old values replaced), otherwise a new row is inserted
 * dynamically. This lets every ProductTest / PortfolioTest / FeatureTest method record
 * its own results without needing the row to be pre-created in the sheet.
 */
public final class ProExecutionResultWriter {

    private static final String OUTPUT_FILE = "src/test/resources/output/pro/ProExecutionData.xlsx";
    private static final String TEST_CASE_COLUMN = "TestCase";

    private ProExecutionResultWriter() {
        // Utility class
    }

    public static synchronized void write(DesignData data, String testCase, ProExecutionData executionData) {
        Map<String, String> values = buildValues(executionData, data.getProduct(), data.getFeatureData(), data);
        String query = rowExists(data.getExecutionDataFile(), data.getResultSheet(), testCase)
                ? buildUpdateQuery(data.getResultSheet(), testCase, values)
                : buildInsertQuery(data.getResultSheet(), testCase, values);
        FilloUtil.executeUpdate(data.getExecutionDataFile(), query);
        var rows = FilloUtil.getRows(data.getExecutionDataFile(),
                "SELECT ProductName, FeatureName, DesignCategory, DesignSource, DesignTitle FROM "
                        + data.getResultSheet() + " WHERE TestCase='" + escape(testCase) + "'");
        Assert.assertEquals(rows.size(), 1, data.getValidationMessages().get("executionRow"));
        for (var entry : rows.getFirst().entrySet()) {
            String expected = values.entrySet().stream()
                    .filter(value -> value.getKey().equalsIgnoreCase(entry.getKey()))
                    .map(Map.Entry::getValue).findFirst().orElseThrow();
            Assert.assertEquals(entry.getValue(), expected, data.getValidationMessages().get("executionRow"));
        }
        LoggerUtil.LOGGER.info("[DESIGN-RESULT] Verified saved product, feature, category, source and title for {}", testCase);
    }

    public static void write(String sheet, String testCase, ProExecutionData executionData) {
        write(OUTPUT_FILE, sheet, testCase, executionData);
    }

    public static synchronized void write(MaturityAssessmentData data, String testCase, ProExecutionData executionData) {
        Assert.assertNotNull(executionData.getAssessmentName(), data.getMissingAssessmentMessage());
        String outputFilePath = data.getExecutionDataFile();
        String sheet = data.getResultSheet();
        Map<String, String> values = Map.of("AssessmentName", executionData.getAssessmentName());
        boolean exists = rowExists(outputFilePath, sheet, testCase);
        String query = exists
                ? buildUpdateQuery(sheet, testCase, values)
                : buildInsertQuery(sheet, testCase, values);

        LoggerUtil.LOGGER.info("ProExecutionResultWriter: {} row for sheet='{}', testCase='{}'",
                exists ? "Updating" : "Inserting", sheet, testCase);
        FilloUtil.executeUpdate(outputFilePath, query);
        List<Map<String, String>> stored = FilloUtil.getRows(outputFilePath,
                "SELECT AssessmentName FROM " + sheet + " WHERE TestCase='" + escape(testCase) + "'");
        Assert.assertEquals(stored.size(), 1, data.getMissingAssessmentMessage());
        String assessmentName = stored.getFirst().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase("AssessmentName"))
                .map(Map.Entry::getValue).findFirst().orElse("");
        Assert.assertEquals(assessmentName, executionData.getAssessmentName(), data.getMissingAssessmentMessage());
        LoggerUtil.LOGGER.info("[PRO-RESULT] Verified saved assessment for {}", testCase);
    }

    public static synchronized void write(String outputFilePath, String sheet, String testCase, ProExecutionData executionData) {
        if (executionData == null) {
            return;
        }

        ProductData productData = ProTestData.getProduct(ProTestData.CREATE_PRODUCT);
        FeatureData featureData = ProTestData.getFeature("createFeature");
        DesignData designData = ProTestData.getDesign("createDesign");

        Map<String, String> values = buildValues(executionData, productData, featureData, designData);

        boolean exists = rowExists(outputFilePath, sheet, testCase);
        String query = exists
                ? buildUpdateQuery(sheet, testCase, values)
                : buildInsertQuery(sheet, testCase, values);

        LoggerUtil.LOGGER.info("ProExecutionResultWriter: {} row for sheet='{}', testCase='{}'",
                exists ? "Updating" : "Inserting", sheet, testCase);

        FilloUtil.executeUpdate(outputFilePath, query);
        List<Map<String, String>> stored = FilloUtil.getRows(outputFilePath,
                "SELECT PortfolioName, ProductName FROM " + sheet + " WHERE TestCase='" + escape(testCase) + "'");
        if (stored.size() != 1) {
            throw new IllegalStateException("Expected one saved execution row for " + testCase);
        }
        for (String column : List.of("PortfolioName", "ProductName")) {
            String actual = stored.getFirst().entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(column))
                    .map(Map.Entry::getValue).findFirst().orElse("");
            if (!nonBlank(actual).equals(values.get(column))) {
                throw new IllegalStateException("Saved " + column + " does not match " + testCase
                        + ": expected '" + values.get(column) + "', found '" + actual + "'");
            }
        }
        LoggerUtil.LOGGER.info("[PRO-RESULT] Verified saved portfolio and products for {}", testCase);
    }

    // =============================================================
    // Build the column -> value map for a single test case row.
    // Every tracked column is included (even as "") so an UPDATE
    // cleanly overwrites stale values left over from a previous run.
    // =============================================================

    private static Map<String, String> buildValues(
            ProExecutionData executionData,
            ProductData productData,
            FeatureData featureData,
            DesignData designData) {

        Map<String, String> values = new LinkedHashMap<>();

        values.put("PortfolioName", nonBlank(executionData.getPortfolioName()));
        values.put("PublicPortfolio", executionData.getPublicPortfolio() != null
                ? String.valueOf(executionData.getPublicPortfolio()) : "");
        values.put("ProductName", nonBlank(joinProducts(executionData, null)));
        values.put("Phases", nonBlank(joinProducts(executionData, phasesOf(productData))));
        values.put("FeatureName", nonBlank(joinProductFeatures(executionData)));
        values.put("FeaturePhases", nonBlank(joinFeatures(executionData,
                f -> String.join(", ", phasesOf(featureData)), notEmptyPhases(featureData))));
        values.put("DefineBusinessRequirementTitle", nonBlank(joinFeatures(executionData,
                f -> String.join(", ", f.getBusinessRequirementNames()),
                f -> !f.getBusinessRequirementNames().isEmpty())));
        boolean hasDesigns = executionData.hasProducts()
                && executionData.getProducts().stream()
                        .filter(p -> p != null && p.getFeatures() != null)
                        .flatMap(p -> p.getFeatures().stream())
                        .anyMatch(f -> f != null && !f.getDesignNames().isEmpty());

        values.put("DesignCategory", hasDesigns && designData != null ? nonBlank(designData.getCategory()) : "");
        values.put("DesignSource", hasDesigns && designData != null ? nonBlank(designData.getSource()) : "");
        values.put("DesignTitle", nonBlank(joinFeatures(executionData,
                f -> String.join(", ", f.getDesignNames()),
                f -> !f.getDesignNames().isEmpty())));
        values.put("ReleaseTrainName", nonBlank(executionData.getReleaseTrainName()));
        values.put("ReleaseName", nonBlank(executionData.getReleaseName()));

        return values;
    }

    private static List<String> phasesOf(ProductData productData) {
        return productData != null && productData.getPhases() != null ? productData.getPhases() : List.of();
    }

    private static List<String> phasesOf(FeatureData featureData) {
        return featureData != null && featureData.getPhases() != null ? featureData.getPhases() : List.of();
    }

    private static Predicate<FeatureExecutionData> notEmptyPhases(FeatureData featureData) {
        return f -> !phasesOf(featureData).isEmpty();
    }

    /** Joins "<ProductName>" for every created product, or "<ProductName>: <phases>" when phases is non-empty. */
    private static String joinProducts(ProExecutionData executionData, List<String> phases) {
        if (!executionData.hasProducts()) {
            return "";
        }

        List<String> parts = new ArrayList<>();
        for (ProductExecutionData product : executionData.getProducts()) {
            if (product == null || product.getProductName() == null || product.getProductName().isBlank()) {
                continue;
            }
            if (phases == null) {
                parts.add(product.getProductName());
            } else if (!phases.isEmpty()) {
                parts.add(product.getProductName() + ": " + String.join(", ", phases));
            }
        }
        return String.join(", ", parts);
    }

    /** Joins "<ProductName>: <Feature1, Feature2>" for every product that has features. */
    private static String joinProductFeatures(ProExecutionData executionData) {
        if (!executionData.hasProducts()) {
            return "";
        }

        List<String> parts = new ArrayList<>();
        for (ProductExecutionData product : executionData.getProducts()) {
            if (product == null || product.getProductName() == null || product.getProductName().isBlank()
                    || product.getFeatures() == null || product.getFeatures().isEmpty()) {
                continue;
            }
            String features = product.getFeatures().stream()
                    .filter(f -> f != null && f.getFeatureName() != null && !f.getFeatureName().isBlank())
                    .map(FeatureExecutionData::getFeatureName)
                    .collect(Collectors.joining(", "));
            if (!features.isEmpty()) {
                parts.add(product.getProductName() + ": " + features);
            }
        }
        return String.join("; ", parts);
    }

    /**
     * Joins "<FeatureName>: <value>" across every product/feature created during the
     * test. When {@code filter} is supplied, only matching features are included
     * (used to skip features with no business requirements / designs attached).
     */
    private static String joinFeatures(
            ProExecutionData executionData,
            Function<FeatureExecutionData, String> valueFn,
            Predicate<FeatureExecutionData> filter) {

        if (!executionData.hasProducts()) {
            return "";
        }

        List<String> parts = new ArrayList<>();
        for (ProductExecutionData product : executionData.getProducts()) {
            if (product == null || product.getFeatures() == null) {
                continue;
            }
            for (FeatureExecutionData feature : product.getFeatures()) {
                if (feature == null || feature.getFeatureName() == null || feature.getFeatureName().isBlank()) {
                    continue;
                }
                if (filter != null && !filter.test(feature)) {
                    continue;
                }
                parts.add(feature.getFeatureName() + ": " + valueFn.apply(feature));
            }
        }
        return String.join("; ", parts);
    }

    // =============================================================
    // Row existence check (decides UPDATE vs INSERT)
    // =============================================================

    private static boolean rowExists(String outputFilePath, String sheet, String testCase) {
        String query = "SELECT " + TEST_CASE_COLUMN + " FROM " + sheet
                + " WHERE " + TEST_CASE_COLUMN + "='" + escape(testCase) + "'";
        try {
            return !FilloUtil.getRows(outputFilePath, query).isEmpty();
        } catch (RuntimeException e) {
            if (e.getCause() != null
                    && e.getCause().getClass().getName().contains("FilloException")
                    && e.getCause().getMessage() != null
                    && e.getCause().getMessage().contains("No records found")) {
                return false;
            }
            throw e;
        }
    }

    // =============================================================
    // Query builders
    // =============================================================

    private static String buildUpdateQuery(String sheet, String testCase, Map<String, String> values) {
        String setClause = values.entrySet().stream()
                .map(entry -> entry.getKey() + "='" + escape(entry.getValue()) + "'")
                .collect(Collectors.joining(", "));

        return "UPDATE " + sheet + " SET " + setClause
                + " WHERE " + TEST_CASE_COLUMN + "='" + escape(testCase) + "'";
    }

    private static String buildInsertQuery(String sheet, String testCase, Map<String, String> values) {
        Map<String, String> insertValues = new LinkedHashMap<>();
        insertValues.put(TEST_CASE_COLUMN, testCase);
        insertValues.putAll(values);

        String columns = String.join(", ", insertValues.keySet());
        String quotedValues = insertValues.values().stream()
                .map(value -> "'" + escape(value) + "'")
                .collect(Collectors.joining(", "));

        return "INSERT INTO " + sheet + " (" + columns + ") VALUES (" + quotedValues + ")";
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("'", "''");
    }

    private static String nonBlank(String value) {
        return value == null || value.isBlank() ? "" : value;
    }
}
