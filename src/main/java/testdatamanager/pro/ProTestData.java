package testdatamanager.pro;

import UI.PRO.datahelper.*;
import utils.JSONUtils.JsonDataReader;

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

    public static ProductData getProduct(String scenario) {
        return JsonDataReader.read(
                PRODUCT_FILE,
                scenario,
                ProductData.class
        );
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