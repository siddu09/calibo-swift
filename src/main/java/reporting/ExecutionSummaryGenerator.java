package reporting;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import utils.LoggerUtil;
import configHandler.ConfigManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ExecutionSummaryGenerator {

    // ─────────────────────────────────────────────────────────────
    //  Public entry points
    // ─────────────────────────────────────────────────────────────

    /** BACKWARD-COMPATIBLE: original no-path call → writes to "reports". */
    public static void generateSummary(ISuite suite) {
        generateSummary(suite, "reports");
    }

    /** NEW: category-aware, e.g. generateSummary(suite, "reports/api"). */
    public static void generateSummary(ISuite suite, String outputDir) {

        String environment = System.getProperty("env", "qa");
        String browser = ConfigManager.getUIProperty("browser");

        try (Workbook workbook = new XSSFWorkbook()) {

            // ✅ OPTION B: create the DETAILED sheet FIRST so it opens as tab 1
            Sheet sheet = workbook.createSheet("Execution Summary");

            // Dashboard created SECOND (supporting summary tab)
            Sheet dashboardSheet = workbook.createSheet("Dashboard");

            String executionId = generateExecutionId();

            LoggerUtil.LOGGER.info("Generating execution summary spreadsheet");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Execution ID");
            header.createCell(1).setCellValue("Test Name");
            header.createCell(2).setCellValue("Status");
            header.createCell(3).setCellValue("Environment");
            header.createCell(4).setCellValue("Start Time");
            header.createCell(5).setCellValue("End Time");
            header.createCell(6).setCellValue("Duration (Sec)");
            header.createCell(7).setCellValue("Failure Category");
            header.createCell(8).setCellValue("Retry Count");
            header.createCell(9).setCellValue("Browser");
            header.createCell(10).setCellValue("API Request");
            header.createCell(11).setCellValue("API Response");

            int rowNum = 1;

            Map<String, ISuiteResult> results = suite.getResults();
            int passed = 0;
            int failed = 0;
            int skipped = 0;

            for (ISuiteResult suiteResult : results.values()) {
                ITestContext context = suiteResult.getTestContext();
                passed += context.getPassedTests().size();
                failed += context.getFailedTests().size();
                skipped += context.getSkippedTests().size();
            }

            int totalTests = passed + failed + skipped;
            double passRate = totalTests == 0
                    ? 0
                    : ((double) passed / totalTests) * 100;

            createDashboardSheet(dashboardSheet, executionId,
                    totalTests, passed, failed, skipped, passRate);

            for (ISuiteResult suiteResult : results.values()) {

                ITestContext context = suiteResult.getTestContext();

                // ---- PASSED ----
                for (ITestNGMethod method : context.getPassedTests().getAllMethods()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(executionId);
                    row.createCell(1).setCellValue(method.getMethodName());
                    row.createCell(2).setCellValue("PASS");
                    row.createCell(3).setCellValue(environment);
                    row.createCell(4).setCellValue(formatDate(context.getStartDate()));
                    row.createCell(5).setCellValue(formatDate(context.getEndDate()));
                    row.createCell(6).setCellValue(getDurationInSeconds(
                            context.getStartDate(), context.getEndDate()));
                    row.createCell(7).setCellValue("N/A");
                    row.createCell(8).setCellValue(
                            RetryTracker.getRetryCount(method.getMethodName()));
                    row.createCell(9).setCellValue(browser);
                    row.createCell(10).setCellValue(
                            ApiExecutionTracker.getRequest(method.getMethodName()));
                    row.createCell(11).setCellValue(
                            ApiExecutionTracker.getResponse(method.getMethodName()));
                }

                // ---- FAILED ----
                for (ITestNGMethod method : context.getFailedTests().getAllMethods()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(executionId);
                    row.createCell(1).setCellValue(method.getMethodName());
                    row.createCell(2).setCellValue("FAIL");
                    row.createCell(3).setCellValue(environment);
                    row.createCell(4).setCellValue(formatDate(context.getStartDate()));
                    row.createCell(5).setCellValue(formatDate(context.getEndDate()));
                    row.createCell(6).setCellValue(getDurationInSeconds(
                            context.getStartDate(), context.getEndDate()));
                    row.createCell(7).setCellValue(
                            FailureTracker.getFailureCategory(method.getMethodName()));
                    row.createCell(8).setCellValue(
                            RetryTracker.getRetryCount(method.getMethodName()));
                    row.createCell(9).setCellValue(browser);
                    row.createCell(10).setCellValue(
                            ApiExecutionTracker.getRequest(method.getMethodName()));
                    row.createCell(11).setCellValue(
                            ApiExecutionTracker.getResponse(method.getMethodName()));
                }

                // ---- SKIPPED ----
                for (ITestNGMethod method : context.getSkippedTests().getAllMethods()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(executionId);
                    row.createCell(1).setCellValue(method.getMethodName());
                    row.createCell(2).setCellValue("SKIP");
                    row.createCell(3).setCellValue(environment);
                    row.createCell(4).setCellValue(formatDate(context.getStartDate()));
                    row.createCell(5).setCellValue(formatDate(context.getEndDate()));
                    row.createCell(6).setCellValue(getDurationInSeconds(
                            context.getStartDate(), context.getEndDate()));
                    row.createCell(7).setCellValue("SKIPPED");
                    row.createCell(8).setCellValue(
                            RetryTracker.getRetryCount(method.getMethodName()));
                    row.createCell(9).setCellValue(browser);
                    row.createCell(10).setCellValue(
                            ApiExecutionTracker.getRequest(method.getMethodName()));
                    row.createCell(11).setCellValue(
                            ApiExecutionTracker.getResponse(method.getMethodName()));
                }
            }

            for (int i = 0; i <= 11; i++) {
                sheet.autoSizeColumn(i);
            }

            // ✅ Belt-and-suspenders: force the detailed sheet to be the selected tab
            workbook.setActiveSheet(workbook.getSheetIndex(sheet));

            // ---- Output path parameterized + rolling filename ----
            File dir = new File(outputDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = outputDir + "/ExecutionSummary.xlsx";

            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
            }

            LoggerUtil.LOGGER.info(
                    "Execution summary spreadsheet generated successfully: " + fileName);

        } catch (Exception e) {
            LoggerUtil.LOGGER.error(
                    "Failed to generate execution summary spreadsheet", e);
            throw new RuntimeException(e);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  Helpers (UNCHANGED)
    // ─────────────────────────────────────────────────────────────

    private static void createDashboardSheet(
            Sheet dashboardSheet, String executionId, int totalTests,
            int passed, int failed, int skipped, double passRate) {

        int rowNum = 0;
        Row row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Execution ID");
        row.createCell(1).setCellValue(executionId);

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Environment");
        row.createCell(1).setCellValue(System.getProperty("env", "qa"));

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Browser");
        row.createCell(1).setCellValue(ConfigManager.getUIProperty("browser"));

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Execution Time");
        row.createCell(1).setCellValue(formatDate(new Date()));

        rowNum++;

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Total Tests");
        row.createCell(1).setCellValue(totalTests);

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Passed");
        row.createCell(1).setCellValue(passed);

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Failed");
        row.createCell(1).setCellValue(failed);

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Skipped");
        row.createCell(1).setCellValue(skipped);

        row = dashboardSheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Pass Rate");
        row.createCell(1).setCellValue(String.format("%.2f%%", passRate));

        dashboardSheet.autoSizeColumn(0);
        dashboardSheet.autoSizeColumn(1);
    }

    private static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private static long getDurationInSeconds(Date start, Date end) {
        return (end.getTime() - start.getTime()) / 1000;
    }

    private static String generateExecutionId() {
        return "CALIBO-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
    }
}