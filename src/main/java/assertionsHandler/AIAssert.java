package assertionsHandler;

import ai.models.EvaluationMetric;
import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;
import ai.services.EvaluationService;
import io.qameta.allure.Allure;
import utils.AllureEnvironmentWriter;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI-based assertions (relevancy, faithfulness, ...) on LLM answers.
 *
 * <p><b>Blocking policy:</b> AI evaluation is ADVISORY by default. A low score is
 * computed, logged, and rendered into a rich Allure table, but it will NOT fail the test.
 * Enable strict gating with {@code -Dai.evaluation.blocking=true}.
 *
 * <p><b>Allure reporting:</b> every evaluation attaches an HTML table
 * ("AI Evaluation Report - &lt;metric&gt;") showing Score, Verdict, Coverage, Missing /
 * Hallucinated tables, etc., parsed from the evaluator's reason string.
 */
public class AIAssert {

    private static final EvaluationService
            EVALUATION_SERVICE =
            new EvaluationService();

    private static EvaluationResult lastEvaluationResult;

    /** key=value or key=[list, ...] token parser for the evaluator reason string. */
    private static final Pattern KV_PATTERN =
            Pattern.compile("([A-Za-z][A-Za-z0-9 _]*?)\\s*=\\s*(\\[[^\\]]*\\]|[^,]+)");

    private static boolean isBlockingMode() {
        return Boolean.parseBoolean(
                System.getProperty("ai.evaluation.blocking", "false"));
    }

    private AIAssert() {
    }

    public static void assertRelevant(EvaluationRequest request) {
        assertMetric(request, EvaluationMetric.ANSWER_RELEVANCY);
    }

    public static void assertFaithful(EvaluationRequest request) {
        assertMetric(request, EvaluationMetric.FAITHFULNESS);
    }

    private static void assertMetric(
            EvaluationRequest request,
            EvaluationMetric metric) {

        LoggerUtil.LOGGER.info("[AI-EVAL] Assertion started. Metric={}", metric);

        EvaluationRequest updatedRequest =
                request.toBuilder()
                        .metric(metric)
                        .build();

        EvaluationResult result =
                EVALUATION_SERVICE.evaluate(updatedRequest);

        lastEvaluationResult = result;

        LoggerUtil.LOGGER.info("[AI-EVAL] Assertion completed. Metric={}", metric);

        recordOrAssert(metric, result);
    }

    public static void assertRelevant(EvaluationResult result) {
        LoggerUtil.LOGGER.info(
                "[AI-EVAL] Assertion completed. Metric={}",
                EvaluationMetric.ANSWER_RELEVANCY);

        lastEvaluationResult = result;

        recordOrAssert(EvaluationMetric.ANSWER_RELEVANCY, result);
    }

    /**
     * Central policy: always record the AI result (log + rich Allure table). Only register
     * it as a (potentially test-failing) soft-assert when blocking mode is enabled.
     */
    private static void recordOrAssert(
            EvaluationMetric metric,
            EvaluationResult result) {

        boolean passed = result.isPassed();
        String reason = safe(result.getReason());

        LoggerUtil.LOGGER.info(
                "[AI-EVAL] Metric={} | Passed={} | Blocking={} | Reason={}",
                metric, passed, isBlockingMode(), reason);

        // 1. Rich HTML table (rendered inline in Allure test step)
        attachHtmlReport(metric, passed, reason);

        // 1b. EXECUTIVE SUMMARY on the Allure OVERVIEW page (Environment widget) -
        //     visible immediately when the report is opened, no drill-down needed.
        pushExecutiveSummary(metric, passed, reason);

        // 2. Plain-text copy (handy for quick copy/paste)
        try {
            Allure.addAttachment(
                    "AI Evaluation (text) - " + metric,
                    "text/plain",
                    "Metric=" + metric
                            + System.lineSeparator() + "Passed=" + passed
                            + System.lineSeparator() + "Blocking=" + isBlockingMode()
                            + System.lineSeparator() + "Reason: " + reason,
                    ".txt");
        } catch (Exception ignored) {
        }

        // 3. Policy
        if (isBlockingMode()) {
            AssertionManager.softAssertTrue(passed, reason);
        } else if (!passed) {
            LoggerUtil.LOGGER.warn(
                    "[AI-EVAL] {} below threshold - recorded as ADVISORY only "
                            + "(test will not fail). Enable -Dai.evaluation.blocking=true to enforce.",
                    metric);
        }
    }

    /**
     * Pushes a concise executive summary into the Allure Environment widget so the AI
     * score/verdict is visible on the Overview page the instant the report is opened.
     * Keys are metric-scoped so multiple flows (Structured / Databricks) each appear.
     */
    private static void pushExecutiveSummary(
            EvaluationMetric metric,
            boolean passed,
            String reason) {
        try {
            java.util.Map<String, String> kv = parseReasonMap(reason);

            String score = firstNonNull(kv.get("score"), kv.get("coverage"), "n/a");
            String verdict = passed ? "PASS" : "BELOW-THRESHOLD";
            String expected = firstNonNull(kv.get("expected tables"), kv.get("expected"), null);
            String mentioned = kv.get("mentioned");

            // Unique, human-readable prefix per metric (keeps both flows' rows distinct).
            String p = "AI " + metric + " ";

            java.util.LinkedHashMap<String, String> summary = new java.util.LinkedHashMap<>();
            summary.put(p + "Score", score);
            summary.put(p + "Verdict", verdict);
            if (expected != null) {
                summary.put(p + "Coverage",
                        (mentioned != null ? mentioned : "?") + " / " + expected + " tables");
            }
            summary.put(p + "Mode", isBlockingMode() ? "BLOCKING" : "ADVISORY (non-failing)");

            AllureEnvironmentWriter.putAll(summary);
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[AI-EVAL] Failed to push executive summary: {}", e.getMessage());
        }
    }

    /** Reason string -> lowercase-keyed map for summary extraction. */
    private static java.util.Map<String, String> parseReasonMap(String reason) {
        java.util.LinkedHashMap<String, String> map = new java.util.LinkedHashMap<>();
        for (String[] row : parseReason(reason)) {
            map.put(row[0].toLowerCase(), stripCount(row[1]));
        }
        return map;
    }

    /** Removes the trailing "(n)" count decoration added for list values. */
    private static String stripCount(String v) {
        if (v == null) return null;
        int idx = v.lastIndexOf("  (");
        return idx > 0 ? v.substring(0, idx).trim() : v.trim();
    }

    private static String firstNonNull(String... vals) {
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    /** Builds and attaches an HTML table parsed from the evaluator reason string. */
    private static void attachHtmlReport(
            EvaluationMetric metric,
            boolean passed,
            String reason) {

        try {
            List<String[]> rows = parseReason(reason);

            String statusColor = passed ? "#2e7d32" : "#c62828";
            String statusText = passed ? "PASS" : "BELOW THRESHOLD";

            StringBuilder html = new StringBuilder();
            html.append("<html><head><meta charset='UTF-8'><style>")
                    .append("body{font-family:Segoe UI,Arial,sans-serif;margin:12px;}")
                    .append("h3{margin:0 0 8px 0;}")
                    .append(".badge{display:inline-block;padding:3px 10px;border-radius:12px;")
                    .append("color:#fff;font-weight:600;font-size:12px;background:").append(statusColor).append(";}")
                    .append(".mode{color:#555;font-size:12px;margin-left:8px;}")
                    .append("table{border-collapse:collapse;margin-top:10px;width:100%;max-width:820px;}")
                    .append("th,td{border:1px solid #ddd;padding:6px 10px;text-align:left;font-size:13px;vertical-align:top;}")
                    .append("th{background:#f4f6f8;width:210px;white-space:nowrap;}")
                    .append("tr:nth-child(even){background:#fafafa;}")
                    .append("</style></head><body>");

            html.append("<h3>AI Evaluation Report &mdash; ").append(metric).append("</h3>");
            html.append("<span class='badge'>").append(statusText).append("</span>");
            html.append("<span class='mode'>Mode: ")
                    .append(isBlockingMode() ? "BLOCKING" : "ADVISORY (non-failing)")
                    .append("</span>");

            html.append("<table>");
            html.append("<tr><th>Metric</th><td>").append(metric).append("</td></tr>");
            html.append("<tr><th>Result</th><td style='color:").append(statusColor)
                    .append(";font-weight:600;'>").append(statusText).append("</td></tr>");

            if (rows.isEmpty()) {
                html.append("<tr><th>Reason</th><td>").append(escape(reason)).append("</td></tr>");
            } else {
                for (String[] row : rows) {
                    html.append("<tr><th>").append(escape(row[0])).append("</th><td>")
                            .append(escape(row[1])).append("</td></tr>");
                }
            }
            html.append("</table></body></html>");

            Allure.addAttachment(
                    "AI Evaluation Report - " + metric,
                    "text/html",
                    new ByteArrayInputStream(html.toString().getBytes(StandardCharsets.UTF_8)),
                    ".html");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[AI-EVAL] Failed to attach HTML report: {}", e.getMessage());
        }
    }

    /**
     * Parses "key=value, key=[a, b, c], ..." reason strings into ordered rows.
     * Falls back to an empty list (caller renders raw reason) if nothing matches.
     */
    private static List<String[]> parseReason(String reason) {
        List<String[]> rows = new ArrayList<>();
        if (reason == null || reason.isBlank()) {
            return rows;
        }
        Matcher m = KV_PATTERN.matcher(reason);
        while (m.find()) {
            String key = m.group(1).trim();
            String value = m.group(2).trim();
            // Prettify list values: [a, b] -> a, b  (and show count for long lists)
            if (value.startsWith("[") && value.endsWith("]")) {
                String inner = value.substring(1, value.length() - 1).trim();
                if (inner.isEmpty()) {
                    value = "(none)";
                } else {
                    int count = inner.split("\\s*,\\s*").length;
                    value = inner + "  (" + count + ")";
                }
            }
            rows.add(new String[]{capitalize(key), value});
        }
        return rows;
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public static EvaluationResult getLastEvaluationResult() {
        return lastEvaluationResult;
    }

    public static void clearLastEvaluationResult() {
        lastEvaluationResult = null;
    }
}
