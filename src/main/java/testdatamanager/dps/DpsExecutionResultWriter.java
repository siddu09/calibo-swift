package testdatamanager.dps;

import UI.DPS.Flows.CrawlerFlows;
import UI.DPS.Flows.DataIngestionFlows;
import UI.DPS.dataHelper.CrawlerData;
import UI.DPS.dataHelper.DataIngestionData;
import testdatamanager.pro.FeatureExecutionData;
import testdatamanager.pro.ProductExecutionData;
import testdatamanager.pro.ProExecutionData;
import utils.FilloUtil;
import utils.LoggerUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class DpsExecutionResultWriter {

    private static final String OUTPUT_FILE =
            "src/test/resources/output/dps/DpsExecutionData.xlsx";

    private static final String TEST_CASE_COLUMN = "TestCase";

    private static final String COL_PORTFOLIO_NAME = "PortfolioName";
    private static final String COL_PRODUCT_NAME = "ProductName";
    private static final String COL_FEATURE_NAME = "FeatureName";
    private static final String COL_CRAWLER_NAME = "CrawlerName";
    private static final String COL_CRAWLER_SOURCE = "CrawlerSource";
    private static final String COL_CRAWLER_DATASTORE_NAME = "CrawlerDataStoreName";
    private static final String COL_CRAWLER_TABLE_NAME = "CrawlerTableName";
    private static final String COL_CATALOG_NAME = "CatalogName";
    private static final String COL_DATA_SOURCES = "DataSources";
    private static final String COL_DATA_SOURCES_CONFIG_TYPE = "DataSourcesConfigurationType";
    private static final String COL_DATA_SOURCES_DETAILS = "DataSourcesDetails";
    private static final String COL_DATA_INTEGRATION = "DataIntegration";
    private static final String COL_DATA_INTEGRATION_INSTANCE = "DataIntegrationInstance";
    private static final String COL_DATA_INTEGRATION_JOB_NAME = "DataIntegrationJobName";
    private static final String COL_DATA_INTEGRATION_TARGET_TABLE = "DataIntegrationTargetTableName";
    private static final String COL_SOURCE_TABLE_NAME = "SourceTableName";
    private static final String COL_TARGET_TABLE_NAME = "TargetTableName";
    private static final String COL_DATA_LAKE = "DataLake";
    private static final String COL_DATA_LAKE_CONFIG_TYPE = "DataLakeCofigurationType";
    private static final String COL_DATA_LAKE_DATASTORE = "DataLakeDataStore";

    private DpsExecutionResultWriter() {
        // Utility class
    }

    public static void write(
            String sheet,
            String testCase,
            ProExecutionData proExecutionData,
            CrawlerFlows crawlerFlows,
            DataIngestionFlows dataIngestionFlows,
            String catalogName) {
        write(
                OUTPUT_FILE,
                sheet,
                testCase,
                proExecutionData,
                crawlerFlows,
                dataIngestionFlows,
                catalogName
        );
    }

    public static void write(
            String outputFilePath,
            String sheet,
            String testCase,
            ProExecutionData proExecutionData,
            CrawlerFlows crawlerFlows,
            DataIngestionFlows dataIngestionFlows,
            String catalogName) {

        Map<String, String> values = buildColumnValues(
                proExecutionData,
                crawlerFlows,
                dataIngestionFlows,
                catalogName
        );

        boolean exists = rowExists(outputFilePath, sheet, testCase, TEST_CASE_COLUMN);
        if (exists) {
            if (values.isEmpty()) {
                LoggerUtil.LOGGER.warn(
                        "Skipping DPS execution data update because no non-empty values were found. sheet='{}', testCase='{}'",
                        sheet,
                        testCase
                );
                return;
            }

            LoggerUtil.LOGGER.info(
                    "Updating DPS execution data row. sheet='{}', testCase='{}', columns={} ",
                    sheet,
                    testCase,
                    values.keySet()
            );
            FilloUtil.executeUpdate(outputFilePath, buildUpdateQuery(sheet, testCase, values, TEST_CASE_COLUMN));
            return;
        }

        LoggerUtil.LOGGER.info(
                "No DPS execution row found for testCase='{}' in sheet='{}'. Creating new row with columns={}",
                testCase,
                sheet,
                values.keySet()
        );

        FilloUtil.executeUpdate(outputFilePath, buildInsertQuery(sheet, testCase, values, TEST_CASE_COLUMN));
    }

    private static Map<String, String> buildColumnValues(
            ProExecutionData proExecutionData,
            CrawlerFlows crawlerFlows,
            DataIngestionFlows dataIngestionFlows,
            String catalogName) {

        Map<String, String> values = new LinkedHashMap<>();
        CrawlerData crawlerData = crawlerFlows.getCrawlerData();
        DataIngestionData ingestionData = dataIngestionFlows.getIngestionData();

        putIfPresent(values, COL_PORTFOLIO_NAME, getPortfolioName(proExecutionData));
        putIfPresent(values, COL_PRODUCT_NAME, getProductName(proExecutionData));
        putIfPresent(values, COL_FEATURE_NAME, getFeatureName(proExecutionData));

        putIfPresent(values, COL_CRAWLER_NAME, crawlerFlows.getLastCrawlerName());
        putIfPresent(values, COL_CRAWLER_SOURCE, getCrawlerSource(crawlerData));
        putIfPresent(values, COL_CRAWLER_DATASTORE_NAME, getCrawlerDatastoreName(crawlerData));
        putIfPresent(values, COL_CRAWLER_TABLE_NAME, getCrawlerTableName(crawlerData));

        putIfPresent(values, COL_CATALOG_NAME, catalogName);

        putIfPresent(values, COL_DATA_SOURCES, ingestionData.getDataSourceStageName());
        putIfPresent(values, COL_DATA_SOURCES_CONFIG_TYPE, ingestionData.getDataSourceNodeName());
        putIfPresent(values, COL_DATA_SOURCES_DETAILS, buildDataSourceDetails(catalogName, ingestionData.getCatalogSchema()));

        putIfPresent(values, COL_DATA_INTEGRATION, ingestionData.getDataIntegrationStageName());
        putIfPresent(values, COL_DATA_INTEGRATION_INSTANCE, ingestionData.getDatabricksInstanceName());
        putIfPresent(values, COL_DATA_INTEGRATION_JOB_NAME, dataIngestionFlows.getDataIntegrationJobName());
        putIfPresent(values, COL_DATA_INTEGRATION_TARGET_TABLE, dataIngestionFlows.getTargetTableName());

        putIfPresent(values, COL_SOURCE_TABLE_NAME, ingestionData.getSourceTableName());
        putIfPresent(values, COL_TARGET_TABLE_NAME, dataIngestionFlows.getTargetTableName());

        putIfPresent(values, COL_DATA_LAKE, ingestionData.getDataLakeStageName());
        putIfPresent(values, COL_DATA_LAKE_CONFIG_TYPE, ingestionData.getDataLakeNodeName());
        putIfPresent(values, COL_DATA_LAKE_DATASTORE, ingestionData.getSnowflakeDatastoreName());

        return values;
    }

    private static String buildDataSourceDetails(String catalogName, String catalogSchema) {
        if (catalogSchema == null || catalogSchema.isBlank()) {
            return null;
        }
        if (catalogName == null || catalogName.isBlank()) {
            return catalogSchema;
        }
        return catalogName + "." + catalogSchema;
    }

    private static String getPortfolioName(ProExecutionData proExecutionData) {
        return proExecutionData != null ? proExecutionData.getPortfolioName() : null;
    }

    private static String getProductName(ProExecutionData proExecutionData) {
        if (proExecutionData == null || proExecutionData.getProducts().isEmpty()) {
            return null;
        }

        for (ProductExecutionData product : proExecutionData.getProducts()) {
            if (product != null && product.getProductName() != null && !product.getProductName().isBlank()) {
                return product.getProductName();
            }
        }
        return null;
    }

    private static String getFeatureName(ProExecutionData proExecutionData) {
        if (proExecutionData == null || proExecutionData.getProducts().isEmpty()) {
            return null;
        }

        for (ProductExecutionData product : proExecutionData.getProducts()) {
            if (product == null || product.getFeatures().isEmpty()) {
                continue;
            }
            for (FeatureExecutionData feature : product.getFeatures()) {
                if (feature != null && feature.getFeatureName() != null && !feature.getFeatureName().isBlank()) {
                    return feature.getFeatureName();
                }
            }
        }
        return null;
    }

    private static String getCrawlerSource(CrawlerData crawlerData) {
        return crawlerData != null ? crawlerData.getSourceName() : null;
    }

    private static String getCrawlerDatastoreName(CrawlerData crawlerData) {
        return crawlerData != null ? crawlerData.getDatastoreName() : null;
    }

    private static String getCrawlerTableName(CrawlerData crawlerData) {
        return crawlerData != null ? crawlerData.getTableName() : null;
    }

    private static void putIfPresent(Map<String, String> values, String column, String value) {
        if (value != null && !value.isBlank()) {
            values.put(column, value);
        }
    }

    private static String buildUpdateQuery(
            String sheet,
            String testCase,
            Map<String, String> values,
            String testCaseColumn) {

        String setClause = values.entrySet().stream()
                .map(entry -> entry.getKey() + "='" + escape(entry.getValue()) + "'")
                .collect(Collectors.joining(", "));

        return "UPDATE \""
                + sheet
                + "\" SET "
                + setClause
                + " WHERE "
                + testCaseColumn
                + "='"
                + escape(testCase)
                + "'";
    }

    private static boolean rowExists(String outputFilePath, String sheet, String testCase, String testCaseColumn) {
        String query = "SELECT "
                + testCaseColumn
                + " FROM \""
                + sheet
                + "\" WHERE "
                + testCaseColumn
                + "='"
                + escape(testCase)
                + "'";

        return !FilloUtil.getRows(outputFilePath, query).isEmpty();
    }

    private static String buildInsertQuery(
            String sheet,
            String testCase,
            Map<String, String> values,
            String testCaseColumn) {

        Map<String, String> insertValues = new LinkedHashMap<>();
        insertValues.put(testCaseColumn, testCase);
        insertValues.putAll(values);

        String columns = insertValues.keySet()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        String quotedValues = insertValues.values().stream()
                .map(value -> "'" + escape(value) + "'")
                .collect(Collectors.joining(", "));

        return "INSERT INTO \""
                + sheet
                + "\" ("
                + columns
                + ") VALUES ("
                + quotedValues
                + ")";
    }


    private static String escape(String value) {
        return value == null ? "" : value.replace("'", "''");
    }
}
