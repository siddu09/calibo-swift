package testdatamanager.aiml;

import UI.PRO.datahelper.*;
import utils.JSONUtils.JsonDataReader;

/**
 * Test data loader for AIML/RAG pipeline provisioning scenarios.
 *
 * Structural mirror of testdatamanager.pro.ProTestData — same file-per-entity
 * pattern, same JsonDataReader usage — but points to its own dedicated
 * testdata/files/AIML directory so RAG pipeline test data stays fully
 * isolated from the shared PRO team's testdata/files/Pro files.
 */
public final class RagPipelineTestData {

    private static final String PORTFOLIO_FILE =
            "testdata/files/AIML/portfolio.json";

    private static final String PRODUCT_FILE =
            "testdata/files/AIML/product.json";

    private static final String FEATURE_FILE =
            "testdata/files/AIML/feature.json";

    private static final String DESIGN_FILE =
            "testdata/files/AIML/design.json";

    private static final String DEFINE_FILE =
            "testdata/files/AIML/define.json";

    private static final String FEATURE_INLINE_FILE =
            "testdata/files/AIML/feature-inline.json";

    public static FeatureData getFeature(String scenario) {
        return JsonDataReader.read(FEATURE_FILE, scenario, FeatureData.class);
    }

    public static utils.FeatureData getAimlFeature(String scenario) {
        return JsonDataReader.read(FEATURE_INLINE_FILE, scenario, utils.FeatureData.class);
    }

    private RagPipelineTestData() {
        // Utility class
    }

    public static PortfolioData getPortfolio(String scenario) {
        return JsonDataReader.read(
                PORTFOLIO_FILE,
                scenario,
                PortfolioData.class
        );
    }

    public static ProductData getProduct(String scenario) {
        return JsonDataReader.read(
                PRODUCT_FILE,
                scenario,
                ProductData.class
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