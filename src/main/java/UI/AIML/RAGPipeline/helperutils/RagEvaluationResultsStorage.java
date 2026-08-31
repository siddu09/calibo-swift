package UI.AIML.RAGPipeline.helperutils;

import ai.config.RagPipelineConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import utils.LoggerUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RAG Evaluation Results Storage
 * Captures and stores AI evaluation results for reporting
 * Generates JSON, CSV, and HTML reports
 */
public class RagEvaluationResultsStorage {

    private static final String RESULTS_DIR = "reports/ai/evaluation-results";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static List<Map<String, Object>> results = new ArrayList<>();

    static {
        // Create results directory if it doesn't exist
        new File(RESULTS_DIR).mkdirs();
        LoggerUtil.LOGGER.info("[RESULTS] Results directory: {}", RESULTS_DIR);
    }

    /**
     * Record an evaluation result (also stores in Excel)
     */
    public static void recordResult(
            String caseId,
            String testCaseName,
            String question,
            String ragAnswer,
            String groundTruthContext,
            double evaluationScore,
            boolean passed,
            String verdict,
            String reason) {

        recordResult(caseId, testCaseName, question, ragAnswer, groundTruthContext,
                evaluationScore, passed, verdict, reason, "default");
    }

    /**
     * Record an evaluation result with category
     */
    public static void recordResult(
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
        result.put("caseId", caseId);
        result.put("testCaseName", testCaseName);
        result.put("question", question);
        result.put("ragAnswer", ragAnswer != null ? ragAnswer : "N/A");
        result.put("groundTruthContext", groundTruthContext);
        result.put("evaluationScore", String.format("%.2f", evaluationScore));
        result.put("passed", passed);
        result.put("verdict", verdict);
        result.put("reason", reason);
        result.put("category", category);
        result.put("environment", RagPipelineConfig.getCurrentEnvironment());

        results.add(result);

        // Also record in Excel storage
        RagValidationResultsExcelWriter.recordValidation(
                generateExecutionId(),
                caseId,
                testCaseName,
                question,
                ragAnswer,
                groundTruthContext,
                evaluationScore,
                passed,
                verdict,
                reason,
                category);

        LoggerUtil.LOGGER.info("[RESULTS] Recorded: {} | Score: {} | Verdict: {}",
                testCaseName, evaluationScore, verdict);
    }

    /**
     * Generate or get execution ID (unique identifier for this test run)
     */
    private static String generateExecutionId() {
        return "EXE-" + System.currentTimeMillis();
    }

    /**
     * Generate JSON report
     */
    public static void generateJsonReport() {
        try {
            String filename = RESULTS_DIR + "/evaluation-results-" + 
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + 
                             ".json";
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            
            Map<String, Object> report = new HashMap<>();
            report.put("generatedAt", LocalDateTime.now().format(TIME_FORMATTER));
            report.put("environment", RagPipelineConfig.getCurrentEnvironment());
            report.put("totalTestCases", results.size());
            report.put("passedCount", results.stream().filter(r -> (boolean) r.get("passed")).count());
            report.put("failedCount", results.stream().filter(r -> !(boolean) r.get("passed")).count());
            report.put("results", results);
            
            mapper.writeValue(new File(filename), report);
            LoggerUtil.LOGGER.info("[RESULTS] JSON report generated: {}", filename);
            
        } catch (IOException e) {
            LoggerUtil.LOGGER.error("[RESULTS] Failed to generate JSON report: {}", e.getMessage());
        }
    }

    /**
     * Generate CSV report
     */
    public static void generateCsvReport() {
        try {
            String filename = RESULTS_DIR + "/evaluation-results-" + 
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + 
                             ".csv";
            
            try (FileWriter writer = new FileWriter(filename)) {
                // Header
                writer.append("Timestamp,Case ID,Test Case Name,Question,RAG Answer,Ground Truth Context,Score,Passed,Verdict,Reason,Environment\n");
                
                // Data rows
                for (Map<String, Object> result : results) {
                    writer.append(String.valueOf(result.get("timestamp"))).append(",");
                    writer.append(escapeCsv(String.valueOf(result.get("caseId")))).append(",");
                    writer.append(escapeCsv(String.valueOf(result.get("testCaseName")))).append(",");
                    writer.append(escapeCsv(String.valueOf(result.get("question")))).append(",");
                    writer.append(escapeCsv(String.valueOf(result.get("ragAnswer")))).append(",");
                    writer.append(escapeCsv(String.valueOf(result.get("groundTruthContext")))).append(",");
                    writer.append(String.valueOf(result.get("evaluationScore"))).append(",");
                    writer.append(String.valueOf(result.get("passed"))).append(",");
                    writer.append(escapeCsv(String.valueOf(result.get("verdict")))).append(",");
                    writer.append(escapeCsv(String.valueOf(result.get("reason")))).append(",");
                    writer.append(String.valueOf(result.get("environment"))).append("\n");
                }
            }
            
            LoggerUtil.LOGGER.info("[RESULTS] CSV report generated: {}", filename);
            
        } catch (IOException e) {
            LoggerUtil.LOGGER.error("[RESULTS] Failed to generate CSV report: {}", e.getMessage());
        }
    }

    /**
     * Generate HTML report (Executive Summary)
     */
    public static void generateHtmlReport() {
        try {
            String filename = RESULTS_DIR + "/evaluation-results-" + 
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + 
                             ".html";
            
            try (FileWriter writer = new FileWriter(filename)) {
                long passedCount = results.stream().filter(r -> (boolean) r.get("passed")).count();
                long failedCount = results.stream().filter(r -> !(boolean) r.get("passed")).count();
                double avgScore = results.stream()
                    .mapToDouble(r -> Double.parseDouble(String.valueOf(r.get("evaluationScore"))))
                    .average()
                    .orElse(0.0);
                
                writer.append("<!DOCTYPE html>\n");
                writer.append("<html>\n");
                writer.append("<head>\n");
                writer.append("<title>RAG Evaluation Results</title>\n");
                writer.append("<style>\n");
                writer.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
                writer.append(".container { background-color: white; padding: 30px; border-radius: 5px; }\n");
                writer.append("h1 { color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }\n");
                writer.append(".summary { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin: 20px 0; }\n");
                writer.append(".summary-card { background: #f8f9fa; padding: 20px; border-radius: 5px; text-align: center; }\n");
                writer.append(".summary-card h3 { margin: 0; color: #666; font-size: 14px; }\n");
                writer.append(".summary-card .value { font-size: 32px; font-weight: bold; color: #007bff; margin: 10px 0; }\n");
                writer.append(".summary-card.passed .value { color: #28a745; }\n");
                writer.append(".summary-card.failed .value { color: #dc3545; }\n");
                writer.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
                writer.append("th { background-color: #007bff; color: white; padding: 12px; text-align: left; font-weight: bold; }\n");
                writer.append("td { padding: 12px; border-bottom: 1px solid #ddd; }\n");
                writer.append("tr:nth-child(even) { background-color: #f9f9f9; }\n");
                writer.append("tr:hover { background-color: #f5f5f5; }\n");
                writer.append(".pass { color: #28a745; font-weight: bold; }\n");
                writer.append(".fail { color: #dc3545; font-weight: bold; }\n");
                writer.append(".score-high { color: #28a745; }\n");
                writer.append(".score-medium { color: #ffc107; }\n");
                writer.append(".score-low { color: #dc3545; }\n");
                writer.append(".footer { margin-top: 30px; text-align: center; color: #999; font-size: 12px; }\n");
                writer.append("</style>\n");
                writer.append("</head>\n");
                writer.append("<body>\n");
                writer.append("<div class=\"container\">\n");
                
                // Header
                writer.append("<h1>RAG Evaluation Results Report</h1>\n");
                writer.append("<p><strong>Generated:</strong> ").append(LocalDateTime.now().format(TIME_FORMATTER)).append("</p>\n");
                writer.append("<p><strong>Environment:</strong> ").append(RagPipelineConfig.getCurrentEnvironment().toUpperCase()).append("</p>\n");
                
                // Summary
                writer.append("<div class=\"summary\">\n");
                writer.append("<div class=\"summary-card\">\n");
                writer.append("<h3>Total Test Cases</h3>\n");
                writer.append("<div class=\"value\">").append(String.valueOf(results.size())).append("</div>\n");
                writer.append("</div>\n");
                
                writer.append("<div class=\"summary-card passed\">\n");
                writer.append("<h3>Passed</h3>\n");
                writer.append("<div class=\"value\">").append(String.valueOf(passedCount)).append("</div>\n");
                writer.append("</div>\n");
                
                writer.append("<div class=\"summary-card failed\">\n");
                writer.append("<h3>Failed</h3>\n");
                writer.append("<div class=\"value\">").append(String.valueOf(failedCount)).append("</div>\n");
                writer.append("</div>\n");
                
                writer.append("<div class=\"summary-card\">\n");
                writer.append("<h3>Average Score</h3>\n");
                writer.append("<div class=\"value\">").append(String.format("%.2f", avgScore)).append("</div>\n");
                writer.append("</div>\n");
                writer.append("</div>\n");
                
                // Results Table
                writer.append("<h2>Detailed Results</h2>\n");
                writer.append("<table>\n");
                writer.append("<tr>\n");
                writer.append("<th>Case</th>\n");
                writer.append("<th>Test Name</th>\n");
                writer.append("<th>Question</th>\n");
                writer.append("<th>Score</th>\n");
                writer.append("<th>Status</th>\n");
                writer.append("<th>Verdict</th>\n");
                writer.append("</tr>\n");
                
                for (Map<String, Object> result : results) {
                    double score = Double.parseDouble(String.valueOf(result.get("evaluationScore")));
                    boolean passed = (boolean) result.get("passed");
                    String scoreClass = score >= 0.6 ? "score-high" : (score >= 0.3 ? "score-medium" : "score-low");
                    
                    writer.append("<tr>\n");
                    writer.append("<td>").append(String.valueOf(result.get("caseId"))).append("</td>\n");
                    writer.append("<td>").append(String.valueOf(result.get("testCaseName"))).append("</td>\n");
                    writer.append("<td>").append(String.valueOf(result.get("question"))).append("</td>\n");
                    writer.append("<td class=\"").append(scoreClass).append("\">").append(String.valueOf(result.get("evaluationScore"))).append("</td>\n");
                    writer.append("<td class=\"").append(passed ? "pass" : "fail").append("\">").append(passed ? "✓ PASS" : "✗ FAIL").append("</td>\n");
                    writer.append("<td>").append(String.valueOf(result.get("verdict"))).append("</td>\n");
                    writer.append("</tr>\n");
                }
                
                writer.append("</table>\n");
                
                // Footer
                writer.append("<div class=\"footer\">\n");
                writer.append("<p>This is an automated report generated by RAG Evaluation Framework</p>\n");
                writer.append("</div>\n");
                
                writer.append("</div>\n");
                writer.append("</body>\n");
                writer.append("</html>\n");
            }
            
            LoggerUtil.LOGGER.info("[RESULTS] HTML report generated: {}", filename);
            
        } catch (IOException e) {
            LoggerUtil.LOGGER.error("[RESULTS] Failed to generate HTML report: {}", e.getMessage());
        }
    }

    /**
     * Escape special characters for CSV
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Generate all reports (JSON, CSV, HTML, and Excel)
     */
    public static void generateAllReports() {
        LoggerUtil.LOGGER.info("[RESULTS] Generating evaluation reports...");
        generateJsonReport();
        generateCsvReport();
        generateHtmlReport();
        RagValidationResultsExcelWriter.generateExcelReport();
        LoggerUtil.LOGGER.info("[RESULTS] All reports generated in: {}", RESULTS_DIR);
    }

    /**
     * Clear results (for new test run)
     */
    public static void clearResults() {
        results.clear();
        LoggerUtil.LOGGER.info("[RESULTS] Results cleared");
    }

    /**
     * Get all results
     */
    public static List<Map<String, Object>> getAllResults() {
        return new ArrayList<>(results);
    }
}
