package datavalidation;

import tech.tablesaw.api.Table;
import utils.LoggerUtil;

public class DataFrameValidator {

    public Table loadCSV(String filePath) {

        try {

            LoggerUtil.LOGGER.info(
                    "Loading CSV file: " + filePath);

            Table table =
                    Table.read().csv(filePath);

            LoggerUtil.LOGGER.info(
                    "Rows: " + table.rowCount());

            LoggerUtil.LOGGER.info(
                    "Columns: " + table.columnCount());

            return table;

        } catch (Exception e) {

            LoggerUtil.LOGGER.error(
                    "Failed to load CSV: " + filePath,
                    e);

            throw new RuntimeException(e);
        }
    }
}