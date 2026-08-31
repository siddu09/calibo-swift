package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableValidationUtil {

    // UI table structure
    private static final String ROW_SELECTOR = "tbody tr";
    private static final String CELL_SELECTOR = "td";

    private TableValidationUtil() {
        // Utility class
    }

    /**
     * Validates a UI table against an Excel sheet.
     *
     * @param page       Playwright page
     * @param excelPath  Path to Excel file
     * @param sheetName  Excel sheet name
     * @param columns    Expected column names
     */
    public static void validateTableAgainstExcel(
            Page page,
            String excelPath,
            String sheetName,
            List<String> columns) {

        // --------------------------------------------------
        // 1. Read expected data from Excel using Fillo
        // --------------------------------------------------

        List<Map<String, String>> expectedRows =
                FilloUtil.getRows(
                        excelPath,
                        "SELECT * FROM " + sheetName
                );

        System.out.println(
                "Expected Excel records: " + expectedRows.size()
        );

        // --------------------------------------------------
        // 2. Read actual data from UI
        // --------------------------------------------------

        List<Map<String, String>> actualRows =
                readUiTable(page, columns);

        System.out.println(
                "Actual UI records: " + actualRows.size()
        );

        // --------------------------------------------------
        // 3. Validate row count
        // --------------------------------------------------

        if (expectedRows.size() != actualRows.size()) {

            System.out.println(
                    "WARN: Table row count mismatch.\n"
                            + "Expected Excel rows: " + expectedRows.size() + "\n"
                            + "Actual UI rows parsed: " + actualRows.size() + "\n"
                            + "Difference: " + Math.abs(expectedRows.size() - actualRows.size())
            );

            if (actualRows.size() < expectedRows.size()) {
                throw new AssertionError(
                        "Table row count mismatch: UI has fewer rows than expected.\n"
                                + "Expected: " + expectedRows.size() + "\n"
                                + "Actual  : " + actualRows.size()
                );
            } else {
                // If UI has more rows, use only the expected number of rows for validation
                System.out.println(
                        "INFO: UI has more rows than expected. "
                                + "Using first " + expectedRows.size() + " rows for validation."
                );
                actualRows = actualRows.subList(0, expectedRows.size());
            }
        }

        // --------------------------------------------------
        // 4. Validate table data
        // --------------------------------------------------

        compareTables(
                expectedRows,
                actualRows,
                columns
        );

        System.out.println(
                "PASS: Table validation successful.\n"
                        + "Records validated: "
                        + expectedRows.size()
                        + "\nColumns validated: "
                        + columns.size()
        );
    }


    /**
     * Reads all rows and cells from the UI table.
     * Handles both direct text content and nested divs with title attributes.
     * Skips rows that don't have the expected number of columns.
     */
    private static List<Map<String, String>> readUiTable(
            Page page,
            List<String> columns) {

        List<Map<String, String>> actualRows =
                new ArrayList<>();

        Locator rows = page.locator(ROW_SELECTOR);

        int rowCount = rows.count();

        System.out.println(
                "UI rows found: " + rowCount
        );

        for (int rowIndex = 0;
             rowIndex < rowCount;
             rowIndex++) {

            Locator row = rows.nth(rowIndex);

            Locator cells = row.locator(CELL_SELECTOR);

            int cellCount = cells.count();

            // Skip rows that don't have the expected number of columns
            // (e.g., pagination controls, empty rows, or malformed rows)
            if (cellCount != columns.size()) {

                System.out.println(
                        "WARN: Skipping UI row "
                                + (rowIndex + 1)
                                + " - Column count mismatch. "
                                + "Expected: "
                                + columns.size()
                                + ", Actual: "
                                + cellCount
                );
                continue;
            }

            Map<String, String> rowData =
                    new LinkedHashMap<>();

            // ----------------------------------------------
            // Read each cell with proper text extraction
            // Handles: direct text, nested divs, and title attributes
            // ----------------------------------------------

            for (int columnIndex = 0;
                 columnIndex < columns.size();
                 columnIndex++) {

                Locator cell = cells.nth(columnIndex);
                
                String value = extractCellText(cell);

                rowData.put(
                        columns.get(columnIndex),
                        value
                );
            }

            actualRows.add(rowData);
        }

        System.out.println(
                "Valid rows parsed: " + actualRows.size()
        );

        return actualRows;
    }

    /**
     * Extracts text from a table cell, handling various HTML structures:
     * - Direct text: <td>value</td>
     * - Nested div: <td><div title="value">value</div></td>
     * - Text in nested elements: <td><div>value</div></td>
     */
    private static String extractCellText(Locator cell) {
        
        // First, try to get text from any nested div with title attribute
        Locator titleDiv = cell.locator("div[title]");
        
        if (titleDiv.count() > 0) {
            String titleValue = titleDiv.first().getAttribute("title");
            if (titleValue != null && !titleValue.trim().isEmpty()) {
                return titleValue.trim();
            }
        }

        // Fallback to innerText (for direct text or any other content)
        String innerText = cell.innerText().trim();
        
        if (!innerText.isEmpty()) {
            return innerText;
        }

        // Last resort: try textContent
        return cell.evaluate("el => el.textContent").toString().trim();
    }


    /**
     * Compares Excel data against UI data.
     */
    private static void compareTables(
            List<Map<String, String>> expectedRows,
            List<Map<String, String>> actualRows,
            List<String> columns) {

        for (int rowIndex = 0;
             rowIndex < expectedRows.size();
             rowIndex++) {

            Map<String, String> expected =
                    expectedRows.get(rowIndex);

            Map<String, String> actual =
                    actualRows.get(rowIndex);

            for (String column : columns) {

                String expectedValue =
                        normalize(expected.get(column));

                String actualValue =
                        normalize(actual.get(column));

                if (!expectedValue.equals(actualValue)) {

                    throw new AssertionError(
                            "Table validation failed.\n"
                                    + "Row      : "
                                    + (rowIndex + 1)
                                    + "\n"
                                    + "Column   : "
                                    + column
                                    + "\n"
                                    + "Expected : ["
                                    + expectedValue
                                    + "]\n"
                                    + "Actual   : ["
                                    + actualValue
                                    + "]"
                    );
                }
            }
        }
    }


    /**
     * Normalizes values before comparison.
     *
     * Examples:
     * 55,000.00 -> 55000 (trailing zeros/decimals removed)
     * 55,000    -> 55000
     * TRUE      -> true
     * " PAID "  -> paid
     * 10.5      -> 10.5
     */
    private static String normalize(String value) {

        if (value == null) {
            return "";
        }

        String normalized = value
                .trim()
                .replace(",", "")
                .replaceAll("\\s+", " ")
                .toLowerCase();

        // Handle decimal numbers: remove trailing zeros after decimal point
        // Examples: 55000.00 -> 55000, 10.50 -> 10.5, 10.5 -> 10.5
        if (normalized.contains(".")) {
            // Remove trailing zeros after decimal point
            normalized = normalized.replaceAll("0+$", "");
            // If only decimal point remains, remove it too
            normalized = normalized.replaceAll("\\.$", "");
        }

        return normalized;
    }
}