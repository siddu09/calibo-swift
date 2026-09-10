package testdatamanager.pro;

import UI.PRO.datahelper.DefineData;
import UI.PRO.datahelper.DesignData;
import UI.PRO.datahelper.FeatureData;
import UI.PRO.datahelper.PortfolioData;
import UI.PRO.datahelper.ProductData;
import utils.FilloUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ProExecutionResultWriter {

    private static final String OUTPUT_FILE =
            "src/test/resources/output/Pro/ProExecutionData.xlsx";

    private ProExecutionResultWriter() {
        // Utility class
    }

    // =============================================================
    // Public write methods
    // =============================================================

    public static void write(
            String sheet,
            String testCase,
            ProExecutionData executionData) {

        write(
                OUTPUT_FILE,
                sheet,
                testCase,
                executionData
        );
    }

    public static void write(
            String outputFilePath,
            String sheet,
            String testCase,
            ProExecutionData executionData) {

        if (executionData == null) {
            return;
        }

        // =========================================================
        // Load static test data from JSON
        // =========================================================

        PortfolioData portfolioData =
                ProTestData.getPortfolio("createPortfolio");

        ProductData productData =
                ProTestData.getProduct("createProduct");

        FeatureData featureData =
                ProTestData.getFeature("createFeature");

        DefineData defineData =
                ProTestData.getDefine("createUserFeedback");

        DesignData designData =
                ProTestData.getDesign("createDesign");

        // =========================================================
        // Build Excel values
        //
        // IMPORTANT:
        // Every tracked column is added to this map.
        //
        // If a value does not exist, we explicitly put "".
        // Therefore the UPDATE query will overwrite any old
        // Excel value with an empty value.
        // =========================================================

        Map<String, String> values =
                new LinkedHashMap<>();

        // ---------------------------------------------------------
        // Portfolio
        // ---------------------------------------------------------

        values.put(
                "PortfolioName",
                nonBlankOrEmpty(
                        executionData.getPortfolioName()
                )
        );

        values.put(
                "PublicPortfolio",
                executionData.getPublicPortfolio() != null
                        ? String.valueOf(executionData.getPublicPortfolio())
                        : portfolioData != null
                        ? String.valueOf(
                        portfolioData.isPublicPortfolio()
                )
                        : ""
        );

        // ---------------------------------------------------------
        // Release Train
        // ---------------------------------------------------------

        values.put(
                "ReleaseTrainName",
                nonBlankOrEmpty(
                        executionData.getReleaseTrainName()
                )
        );

        values.put(
                "ReleaseName",
                nonBlankOrEmpty(
                        executionData.getReleaseName()
                )
        );

        // ---------------------------------------------------------
        // Product
        // ---------------------------------------------------------

        values.put(
                "ProductName",
                nonBlankOrEmpty(
                        formatProductNames(executionData)
                )
        );

        values.put(
                "Phases",
                nonBlankOrEmpty(
                        formatProductPhases(
                                executionData,
                                productData
                        )
                )
        );

        // ---------------------------------------------------------
        // Feature
        // ---------------------------------------------------------

        values.put(
                "FeatureName",
                nonBlankOrEmpty(
                        formatFeatureNames(executionData)
                )
        );

        values.put(
                "FeaturePhases",
                nonBlankOrEmpty(
                        formatFeaturePhases(
                                executionData,
                                featureData
                        )
                )
        );

        // ---------------------------------------------------------
        // Define
        // ---------------------------------------------------------

        values.put(
                "DefineBusinessRequirementTitle",
                defineData != null
                        ? nonBlankOrEmpty(
                        formatBusinessRequirements(
                                executionData
                        )
                )
                        : ""
        );

        // ---------------------------------------------------------
        // Design
        // ---------------------------------------------------------

        values.put(
                "DesignCategory",
                designData != null
                        ? nonBlankOrEmpty(
                        designData.getCategory()
                )
                        : ""
        );

        values.put(
                "DesignSource",
                designData != null
                        ? nonBlankOrEmpty(
                        designData.getSource()
                )
                        : ""
        );

        values.put(
                "DesignTitle",
                designData != null
                        ? nonBlankOrEmpty(
                        formatDesigns(executionData)
                )
                        : ""
        );

        // =========================================================
        // Build UPDATE query
        //
        // We intentionally DO NOT execute a separate clear query.
        //
        // Every column is included in this UPDATE, including columns
        // whose value is "".
        //
        // Example:
        //
        // SET ProductName='Product A',
        //     Phases='',
        //     FeatureName='Feature A',
        //     DesignTitle=''
        //
        // This ensures old values are overwritten.
        // =========================================================

        String query =
                buildUpdateQuery(
                        sheet,
                        testCase,
                        values
                );

        // Useful for debugging Fillo queries.
        System.out.println(
                "================================================="
        );
        System.out.println(
                "ProExecutionResultWriter - Excel UPDATE"
        );
        System.out.println(
                "================================================="
        );
        System.out.println(query);
        System.out.println(
                "================================================="
        );

        // =========================================================
        // Execute UPDATE
        // =========================================================

        FilloUtil.executeUpdate(
                outputFilePath,
                query
        );
    }

    // =============================================================
    // Product Names
    // =============================================================

    private static String formatProductNames(
            ProExecutionData executionData) {

        if (executionData.getProducts() == null
                || executionData.getProducts().isEmpty()) {

            return "";
        }

        return executionData.getProducts()
                .stream()
                .map(
                        ProductExecutionData
                                ::getProductName
                )
                .filter(
                        name ->
                                name != null
                                        && !name.isBlank()
                )
                .collect(
                        Collectors.joining(", ")
                );
    }

    // =============================================================
    // Feature Names
    // =============================================================

    private static String formatFeatureNames(
            ProExecutionData executionData) {

        if (executionData.getProducts() == null
                || executionData.getProducts().isEmpty()) {

            return "";
        }

        List<String> productFeatures =
                new ArrayList<>();

        for (ProductExecutionData product :
                executionData.getProducts()) {

            if (product == null) {
                continue;
            }

            if (product.getProductName() == null
                    || product.getProductName().isBlank()) {

                continue;
            }

            if (product.getFeatures() == null
                    || product.getFeatures().isEmpty()) {

                continue;
            }

            String features =
                    product.getFeatures()
                            .stream()
                            .filter(feature -> feature != null)
                            .map(
                                    FeatureExecutionData
                                            ::getFeatureName
                            )
                            .filter(
                                    name ->
                                            name != null
                                                    && !name.isBlank()
                            )
                            .collect(
                                    Collectors.joining(", ")
                            );

            if (!features.isEmpty()) {

                productFeatures.add(
                        product.getProductName()
                                + ": "
                                + features
                );
            }
        }

        return String.join(
                "; ",
                productFeatures
        );
    }

    // =============================================================
    // Product Phases
    // =============================================================

    private static String formatProductPhases(
            ProExecutionData executionData,
            ProductData productData) {

        if (productData == null) {
            return "";
        }

        if (productData.getPhases() == null
                || productData.getPhases().isEmpty()) {

            return "";
        }

        if (executionData.getProducts() == null
                || executionData.getProducts().isEmpty()) {

            return "";
        }

        List<String> productPhases =
                new ArrayList<>();

        for (ProductExecutionData product :
                executionData.getProducts()) {

            if (product == null) {
                continue;
            }

            if (product.getProductName() == null
                    || product.getProductName().isBlank()) {

                continue;
            }

            productPhases.add(
                    product.getProductName()
                            + ": "
                            + String.join(
                            ", ",
                            productData.getPhases()
                    )
            );
        }

        return String.join(
                "; ",
                productPhases
        );
    }

    // =============================================================
    // Feature Phases
    // =============================================================

    private static String formatFeaturePhases(
            ProExecutionData executionData,
            FeatureData featureData) {

        if (featureData == null) {
            return "";
        }

        if (featureData.getPhases() == null
                || featureData.getPhases().isEmpty()) {

            return "";
        }

        if (executionData.getProducts() == null
                || executionData.getProducts().isEmpty()) {

            return "";
        }

        List<String> featurePhases =
                new ArrayList<>();

        for (ProductExecutionData product :
                executionData.getProducts()) {

            if (product == null) {
                continue;
            }

            if (product.getFeatures() == null
                    || product.getFeatures().isEmpty()) {

                continue;
            }

            for (FeatureExecutionData feature :
                    product.getFeatures()) {

                if (feature == null) {
                    continue;
                }

                if (feature.getFeatureName() == null
                        || feature.getFeatureName().isBlank()) {

                    continue;
                }

                featurePhases.add(
                        feature.getFeatureName()
                                + ": "
                                + String.join(
                                ", ",
                                featureData.getPhases()
                        )
                );
            }
        }

        return String.join(
                "; ",
                featurePhases
        );
    }

    // =============================================================
    // Business Requirements
    // =============================================================

    private static String formatBusinessRequirements(
            ProExecutionData executionData) {

        if (executionData.getProducts() == null
                || executionData.getProducts().isEmpty()) {

            return "";
        }

        List<String> requirements =
                new ArrayList<>();

        for (ProductExecutionData product :
                executionData.getProducts()) {

            if (product == null) {
                continue;
            }

            if (product.getFeatures() == null
                    || product.getFeatures().isEmpty()) {

                continue;
            }

            for (FeatureExecutionData feature :
                    product.getFeatures()) {

                if (feature == null) {
                    continue;
                }

                if (feature.getBusinessRequirementNames() == null
                        || feature
                        .getBusinessRequirementNames()
                        .isEmpty()) {

                    continue;
                }

                if (feature.getFeatureName() == null
                        || feature.getFeatureName().isBlank()) {

                    continue;
                }

                requirements.add(
                        feature.getFeatureName()
                                + ": "
                                + String.join(
                                ", ",
                                feature
                                        .getBusinessRequirementNames()
                        )
                );
            }
        }

        return String.join(
                "; ",
                requirements
        );
    }

    // =============================================================
    // Designs
    // =============================================================

    private static String formatDesigns(
            ProExecutionData executionData) {

        if (executionData.getProducts() == null
                || executionData.getProducts().isEmpty()) {

            return "";
        }

        List<String> designs =
                new ArrayList<>();

        for (ProductExecutionData product :
                executionData.getProducts()) {

            if (product == null) {
                continue;
            }

            if (product.getFeatures() == null
                    || product.getFeatures().isEmpty()) {

                continue;
            }

            for (FeatureExecutionData feature :
                    product.getFeatures()) {

                if (feature == null) {
                    continue;
                }

                if (feature.getDesignNames() == null
                        || feature
                        .getDesignNames()
                        .isEmpty()) {

                    continue;
                }

                if (feature.getFeatureName() == null
                        || feature.getFeatureName().isBlank()) {

                    continue;
                }

                designs.add(
                        feature.getFeatureName()
                                + ": "
                                + String.join(
                                ", ",
                                feature
                                        .getDesignNames()
                        )
                );
            }
        }

        return String.join(
                "; ",
                designs
        );
    }

    // =============================================================
    // Build Fillo UPDATE query
    // =============================================================

    private static String buildUpdateQuery(
            String sheet,
            String testCase,
            Map<String, String> values) {

        String setClause =
                values.entrySet()
                        .stream()
                        .map(entry ->
                                entry.getKey()
                                        + "='"
                                        + escape(
                                        entry.getValue()
                                )
                                        + "'"
                        )
                        .collect(
                                Collectors.joining(", ")
                        );

        return "UPDATE "
                + sheet
                + " SET "
                + setClause
                + " WHERE TestCase='"
                + escape(testCase)
                + "'";
    }

    // =============================================================
    // Escape single quotes for Fillo
    // =============================================================

    private static String escape(
            String value) {

        if (value == null) {
            return "";
        }

        return value.replace(
                "'",
                "''"
        );
    }

    // =============================================================
    // Convert null / blank values to empty string
    // =============================================================

    private static String nonBlankOrEmpty(
            String value) {

        return value == null || value.isBlank()
                ? ""
                : value;
    }
}
