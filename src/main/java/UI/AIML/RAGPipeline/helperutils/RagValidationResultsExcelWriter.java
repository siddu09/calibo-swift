package UI.AIML.RAGPipeline.helperutils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.LoggerUtil;
import ai.config.RagPipelineConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Structured Excel storage for RAG Validation Results
 * Creates professional Excel workbook with multiple sheets and formatting
 */
public class RagValidationResultsExcelWriter {

    private static final String RESULTS_DIR = "reports/ai/rag-validation";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static List<Map<String, Object>> validationResults = new ArrayList<>();

    static {
        new File(RESULTS_DIR).mkdirs();
        LoggerUtil.LOGGER.info("[RAG-EXCEL] Results directory: {}", RESULTS_DIR);
    }

    private RagValidationResultsExcelWriter() {
        // Utility class
    }

    /**
     * Record a validation result
     */
    public static void recordValidation(
            String executionId,
            String caseId,
            String testCaseName,
            String question,
            String ragAnswer,
            String groundTruthContext,
            double evaluationScore,
            boolean passed,
            String verdict,
            String reason,
            String category) {

        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", LocalDateTime.now().format(TIME_FORMATTER));
        result.put("executionId", executionId);
        result.put("caseId", caseId);
        result.put("testCaseName", testCaseName);
        result.put("question", question);
        result.put("ragAnswer", ragAnswer != null ? ragAnswer : "N/A");
        result.put("groundTruthContext", groundTruthContext);
        result.put("evaluationScore", evaluationScore);
        result.put("passed", passed);
        result.put("verdict", verdict);
        result.put("reason", reason);
        result.put("category", category);
        result.put("environment", RagPipelineConfig.getCurrentEnvironment());

        validationResults.add(result);

        LoggerUtil.LOGGER.info(
                "[RAG-EXCEL] Recorded validation: {} | Score: {} | Status: {} | Verdict: {}",
                testCaseName, String.format("%.2f", evaluationScore), (passed ? "PASS" : "FAIL"), verdict);
    }

    /**
     * Generate comprehensive Excel workbook with multiple sheets
     */
    public static void generateExcelReport() {
        try {
            String filename = RESULTS_DIR + "/RAG-Validation-Results-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) +
                    ".xlsx";

            try (Workbook workbook = new XSSFWorkbook()) {
                // Create sheets
                Sheet summarySheet = workbook.createSheet("Summary");
                Sheet detailedSheet = workbook.createSheet("Detailed Results");
                Sheet metricsSheet = workbook.createSheet("Metrics");

                // Generate content
                generateSummarySheet(workbook, summarySheet);
                generateDetailedSheet(workbook, detailedSheet);
                generateMetricsSheet(workbook, metricsSheet);

                // Write to file
                try (FileOutputStream fos = new FileOutputStream(filename)) {
                    workbook.write(fos);
                }

                LoggerUtil.LOGGER.info("[RAG-EXCEL] Excel report generated: {}", filename);
            }
        } catch (IOException e) {
            LoggerUtil.LOGGER.error("[RAG-EXCEL] Failed to generate Excel report: {}", e.getMessage(), e);
        }
    }

    /**
     * Generate Summary Sheet with key metrics and overview
     */
    private static void generateSummarySheet(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle metricStyle = createMetricStyle(workbook);

        // Title
        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("RAG Validation Results Summary");
        titleRow.getCell(0).setCellStyle(createTitleStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        // Execution Info
        int rowNum = 2;
        Row execInfoRow = sheet.createRow(rowNum++);
        execInfoRow.createCell(0).setCellValue("Execution Date:");
        execInfoRow.createCell(1).setCellValue(LocalDateTime.now().format(TIME_FORMATTER));
        execInfoRow.getCell(0).setCellStyle(headerStyle);
        execInfoRow.getCell(1).setCellStyle(dataStyle);

        Row envRow = sheet.createRow(rowNum++);
        envRow.createCell(0).setCellValue("Environment:");
        envRow.createCell(1).setCellValue(RagPipelineConfig.getCurrentEnvironment().toUpperCase());
        envRow.getCell(0).setCellStyle(headerStyle);
        envRow.getCell(1).setCellStyle(dataStyle);

        // Metrics
        rowNum++;
        Row metricsHeaderRow = sheet.createRow(rowNum++);
        metricsHeaderRow.createCell(0).setCellValue("Total Tests");
        metricsHeaderRow.createCell(1).setCellValue("Passed");
        metricsHeaderRow.createCell(2).setCellValue("Failed");
        metricsHeaderRow.createCell(3).setCellValue("Pass Rate %");
        metricsHeaderRow.createCell(4).setCellValue("Avg Score");

        for (int i = 0; i <= 4; i++) {
            metricsHeaderRow.getCell(i).setCellStyle(metricStyle);
        }

        long passedCount = validationResults.stream().filter(r -> (boolean) r.get("passed")).count();
        long failedCount = validationResults.stream().filter(r -> !(boolean) r.get("passed")).count();
        double avgScore = validationResults.stream()
                .mapToDouble(r -> (double) r.get("evaluationScore"))
                .average()
                .orElse(0.0);
        double passRate = validationResults.isEmpty() ? 0 :
                (passedCount * 100.0) / validationResults.size();

        Row metricsDataRow = sheet.createRow(rowNum);
        metricsDataRow.createCell(0).setCellValue(validationResults.size());
        metricsDataRow.createCell(1).setCellValue(passedCount);
        metricsDataRow.createCell(2).setCellValue(failedCount);
        metricsDataRow.createCell(3).setCellValue(String.format("%.2f%%", passRate));
        metricsDataRow.createCell(4).setCellValue(String.format("%.2f", avgScore));

        for (int i = 0; i <= 4; i++) {
            metricsDataRow.getCell(i).setCellStyle(dataStyle);
        }

        // Set column widths
        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
    }

    /**
     * Generate Detailed Results Sheet
     */
    private static void generateDetailedSheet(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = createDetailedHeaderStyle(workbook);
        CellStyle dataStyle = createDetailedDataStyle(workbook);

        // Headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Execution ID", "Case ID", "Test Name", "Category", "Question",
                "RAG Answer", "Ground Truth", "Score", "Status", "Verdict", "Reason", "Timestamp"
        };

        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        // Data rows
        int rowNum = 1;
        for (Map<String, Object> result : validationResults) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(String.valueOf(result.get("executionId")));
            row.createCell(1).setCellValue(String.valueOf(result.get("caseId")));
            row.createCell(2).setCellValue(String.valueOf(result.get("testCaseName")));
            row.createCell(3).setCellValue(String.valueOf(result.get("category")));
            row.createCell(4).setCellValue(String.valueOf(result.get("question")));
            row.createCell(5).setCellValue(String.valueOf(result.get("ragAnswer")));
            row.createCell(6).setCellValue(String.valueOf(result.get("groundTruthContext")));

            double score = (double) result.get("evaluationScore");
            row.createCell(7).setCellValue(String.format("%.2f", score));

            String status = (boolean) result.get("passed") ? "PASS" : "FAIL";
            row.createCell(8).setCellValue(status);
            row.createCell(9).setCellValue(String.valueOf(result.get("verdict")));
            row.createCell(10).setCellValue(String.valueOf(result.get("reason")));
            row.createCell(11).setCellValue(String.valueOf(result.get("timestamp")));

            for (int i = 0; i < 12; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Set column widths
        int[] columnWidths = {18, 15, 20, 15, 25, 30, 30, 12, 10, 15, 30, 20};
        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i] * 256);
        }
    }

    /**
     * Generate Metrics Sheet with performance analysis
     */
    private static void generateMetricsSheet(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        // Score Distribution
        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        titleRow.createCell(0).setCellValue("RAG Validation Metrics & Analysis");
        titleRow.getCell(0).setCellStyle(createTitleStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

        // Score ranges
        rowNum++;
        Row scoreHeaderRow = sheet.createRow(rowNum++);
        scoreHeaderRow.createCell(0).setCellValue("Score Range");
        scoreHeaderRow.createCell(1).setCellValue("Count");
        scoreHeaderRow.createCell(2).setCellValue("Percentage");
        scoreHeaderRow.getCell(0).setCellStyle(headerStyle);
        scoreHeaderRow.getCell(1).setCellStyle(headerStyle);
        scoreHeaderRow.getCell(2).setCellStyle(headerStyle);

        long scoreHigh = validationResults.stream()
                .filter(r -> (double) r.get("evaluationScore") >= 0.7).count();
        long scoreMedium = validationResults.stream()
                .filter(r -> (double) r.get("evaluationScore") >= 0.4 &&
                        (double) r.get("evaluationScore") < 0.7).count();
        long scoreLow = validationResults.stream()
                .filter(r -> (double) r.get("evaluationScore") < 0.4).count();

        addMetricRow(sheet, rowNum++, "High (>=0.7)", scoreHigh, validationResults.size(), dataStyle);
        addMetricRow(sheet, rowNum++, "Medium (0.4-0.7)", scoreMedium, validationResults.size(), dataStyle);
        addMetricRow(sheet, rowNum++, "Low (<0.4)", scoreLow, validationResults.size(), dataStyle);

        // Test status by category
        rowNum += 2;
        Row categoryHeaderRow = sheet.createRow(rowNum++);
        categoryHeaderRow.createCell(0).setCellValue("Test Category");
        categoryHeaderRow.createCell(1).setCellValue("Total");
        categoryHeaderRow.createCell(2).setCellValue("Passed");
        categoryHeaderRow.createCell(3).setCellValue("Failed");
        for (int i = 0; i <= 3; i++) {
            categoryHeaderRow.getCell(i).setCellStyle(headerStyle);
        }

        // Group by category
        Map<String, Map<String, Integer>> categoryStats = new HashMap<>();
        for (Map<String, Object> result : validationResults) {
            String category = String.valueOf(result.get("category"));
            categoryStats.putIfAbsent(category, new HashMap<>());

            Map<String, Integer> stats = categoryStats.get(category);
            stats.put("total", stats.getOrDefault("total", 0) + 1);

            if ((boolean) result.get("passed")) {
                stats.put("passed", stats.getOrDefault("passed", 0) + 1);
            } else {
                stats.put("failed", stats.getOrDefault("failed", 0) + 1);
            }
        }

        for (Map.Entry<String, Map<String, Integer>> entry : categoryStats.entrySet()) {
            Row catRow = sheet.createRow(rowNum++);
            Map<String, Integer> stats = entry.getValue();

            catRow.createCell(0).setCellValue(entry.getKey());
            catRow.createCell(1).setCellValue(stats.get("total"));
            catRow.createCell(2).setCellValue(stats.getOrDefault("passed", 0));
            catRow.createCell(3).setCellValue(stats.getOrDefault("failed", 0));

            for (int i = 0; i <= 3; i++) {
                catRow.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Set column widths
        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
    }

    /**
     * Add a metric row
     */
    private static void addMetricRow(Sheet sheet, int rowNum, String label, long count,
                                     int total, CellStyle style) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(count);

        double percentage = total == 0 ? 0 : (count * 100.0) / total;
        row.createCell(2).setCellValue(String.format("%.2f%%", percentage));

        for (int i = 0; i <= 2; i++) {
            row.getCell(i).setCellStyle(style);
        }
    }

    /**
     * Cell style for title
     */
    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Cell style for headers
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    /**
     * Cell style for detailed sheet headers
     */
    private static CellStyle createDetailedHeaderStyle(Workbook workbook) {
        CellStyle style = createHeaderStyle(workbook);
        style.setWrapText(true);
        return style;
    }

    /**
     * Cell style for data cells
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    /**
     * Cell style for detailed data cells
     */
    private static CellStyle createDetailedDataStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setWrapText(true);
        return style;
    }

    /**
     * Cell style for metrics
     */
    private static CellStyle createMetricStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Clear results for new execution
     */
    public static void clearResults() {
        validationResults.clear();
        LoggerUtil.LOGGER.info("[RAG-EXCEL] Results cleared");
    }

    /**
     * Get all recorded results
     */
    public static List<Map<String, Object>> getAllResults() {
        return new ArrayList<>(validationResults);
    }

    /**
     * Get result count
     */
    public static int getResultCount() {
        return validationResults.size();
    }
}
