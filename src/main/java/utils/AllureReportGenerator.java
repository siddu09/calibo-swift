package utils;

import ai.models.DatasetSummary;

import ai.models.EvaluationResult;

import io.qameta.allure.Allure;


import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.List;

import java.util.concurrent.TimeUnit;


public class AllureReportGenerator {


    // ─────────────────────────────────────────────────────────────

    //  Report generation (portable + category-aware + graceful skip)

    // ─────────────────────────────────────────────────────────────


    /**
     * Global opt-out (e.g. on Jenkins where the Allure plugin renders the report).
     */

    private static final boolean SKIP_HTML =

            Boolean.parseBoolean(System.getProperty("allure.skipHtml", "false"))

                    || "true".equalsIgnoreCase(System.getenv("ALLURE_SKIP_HTML"));


    /**
     * Resolves the Allure executable in an OS-aware way. Precedence:
     * <p>
     * 1) -Dallure.command=... or ALLURE_HOME env var
     * <p>
     * 2) 'allure' (mac/linux) / 'allure.bat' (windows) on PATH
     * <p>
     * Returns null if nothing usable is found (caller then skips gracefully).
     */

    private static String allureCommand() {

        // 1) explicit override (best for CI / pinned installs)

        String cmd = System.getProperty("allure.command");

        if (cmd == null || cmd.isBlank()) {

            cmd = System.getenv("ALLURE_COMMAND");

        }

        if (cmd != null && !cmd.isBlank()) {

            return cmd.trim();

        }


        boolean isWindows = System.getProperty("os.name", "").toLowerCase().contains("win");


        // 1b) ALLURE_HOME/bin/allure[.bat]

        String home = System.getenv("ALLURE_HOME");

        if (home == null || home.isBlank()) {

            home = System.getProperty("allure.home");

        }

        if (home != null && !home.isBlank()) {

            var candidate = Paths.get(home.trim(), "bin", isWindows ? "allure.bat" : "allure");

            if (Files.isRegularFile(candidate)) {

                return candidate.toAbsolutePath().toString();

            }

        }

        System.out.println("ALLURE_HOME=" + System.getenv("ALLURE_HOME"));
        System.out.println("ALLURE_COMMAND=" + System.getenv("ALLURE_COMMAND"));

        // 2) rely on PATH — verify it actually runs

        String binName = isWindows ? "allure.bat" : "allure";

        if (isOnPath(binName)) {

            return binName;

        }


        return null; // not found → caller skips

    }


    /**
     * Probes '<exe> --version' to confirm the CLI is really callable.
     */

    private static boolean isOnPath(String exe) {

        try {

            Process p = new ProcessBuilder(exe, "--version")

                    .redirectErrorStream(true)

                    .start();

            if (!p.waitFor(10, TimeUnit.SECONDS)) {

                p.destroyForcibly();

                return false;

            }

            return p.exitValue() == 0;

        } catch (IOException | InterruptedException e) {

            if (e instanceof InterruptedException) {

                Thread.currentThread().interrupt();

            }

            return false;

        }

    }


    /**
     * Category-aware generation. Called by SuiteListener with per-category paths, e.g.
     * <p>
     * generateReport("reports/api/allure-results", "reports/api/allure-report")
     * <p>
     * <p>
     * <p>
     * Produces the HTML report IF the Allure CLI is available; otherwise it
     * <p>
     * SKIPS gracefully (raw allure-results are always retained).
     */

    public static void generateReport(String resultsDir, String reportDir) {


        // Nothing to render if there are no results.

        var results = Paths.get(resultsDir);

        if (!Files.isDirectory(results) || isEmptyDir(resultsDir)) {

            LoggerUtil.LOGGER.warn("[ALLURE] No results in '{}' — skipping report.", resultsDir);

            return;

        }

        LoggerUtil.LOGGER.info(
                "[ALLURE] Command resolved: {}",
                allureCommand());



        // Global opt-out (let Jenkins Allure plugin handle rendering).

        if (SKIP_HTML) {

            LoggerUtil.LOGGER.info("[ALLURE] HTML generation disabled (allure.skipHtml). "

                    + "Raw results retained at: {}", resultsDir);

            return;

        }


        // Resolve CLI; skip cleanly if missing (fixes the Mac 'C:\Tools\...' crash).

        String allure = allureCommand();

        if (allure == null) {

            LoggerUtil.LOGGER.error(
                    """
                    [ALLURE] Allure CLI NOT FOUND
        
                    Checked:
                    1. -Dallure.command
                    2. ALLURE_COMMAND environment variable
                    3. ALLURE_HOME environment variable
                    4. System PATH
        
                    HTML report generation skipped.
                    """
            );

            return;
        }

        try {

            Files.createDirectories(Paths.get(reportDir));

            ProcessBuilder builder = new ProcessBuilder(

                    allure, "generate", resultsDir, "-o", reportDir, "--clean");

            builder.inheritIO();


            Process process = builder.start();

            int exitCode = process.waitFor();


            if (exitCode == 0) {

                LoggerUtil.LOGGER.info("[ALLURE] Report generated successfully: {}", reportDir);

            } else {

                LoggerUtil.LOGGER.warn("[ALLURE] Allure generate exited with code {} for {}",

                        exitCode, reportDir);

            }


        } catch (IOException e) {

            // The exact failure you hit on Mac — now handled instead of crashing the suite.

            LoggerUtil.LOGGER.warn("[ALLURE] Could not run Allure CLI ('{}'): {}. "

                            + "SKIPPING HTML; raw results retained at: {}",

                    allure, e.getMessage(), resultsDir);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            LoggerUtil.LOGGER.warn("[ALLURE] Report generation interrupted for {}", reportDir);

        }

    }


    /**
     * BACKWARD-COMPATIBLE: your original no-arg method still works.
     * <p>
     * Delegates to the parameterized version with the old default paths.
     */

    public static void generateReport() {

        generateReport("reports/allure-results", "reports/allure-report");

    }


    private static boolean isEmptyDir(String dir) {

        try (var stream = Files.list(Paths.get(dir))) {

            return stream.findAny().isEmpty();

        } catch (IOException e) {

            return true;

        }

    }


    // ─────────────────────────────────────────────────────────────

    //  AI attachments (UNCHANGED — your existing logic preserved)

    // ─────────────────────────────────────────────────────────────


    public static void attachDatasetSummary(DatasetSummary summary) {


        LoggerUtil.LOGGER.info("[ALLURE-AI] Attaching Dataset Summary");


        String content = String.format(

                """
                        
                        ========================================
                        
                        AI DATASET SUMMARY
                        
                        ========================================
                        
                        
                        Total Records : %d
                        
                        
                        Strong Matches : %d
                        
                        Likely Matches : %d
                        
                        Mismatches     : %d
                        
                        
                        Passed Records : %d
                        
                        Failed Records : %d
                        
                        
                        Pass Rate : %.2f%%
                        
                        
                        Average Score : %.2f
                        
                        Highest Score : %.2f
                        
                        Lowest Score  : %.2f
                        
                        
                        ========================================
                        
                        """,

                summary.getTotalRecords(),

                summary.getStrongMatches(),

                summary.getLikelyMatches(),

                summary.getMismatches(),

                summary.getPassedRecords(),

                summary.getFailedRecords(),

                summary.getPassRate(),

                summary.getAverageScore(),

                summary.getHighestScore(),

                summary.getLowestScore());


        Allure.addAttachment(

                "AI Dataset Summary",

                "text/plain",

                content);

    }


    public static void attachDetailedResults(List<EvaluationResult> results) {


        StringBuilder report = new StringBuilder();


        LoggerUtil.LOGGER.info("[ALLURE-AI] Attaching Detailed Results");


        report.append("Question,Score,Verdict,Passed,Reason\n");


        for (EvaluationResult result : results) {

            report.append(result.getQuestion()).append(",")

                    .append(result.getScore()).append(",")

                    .append(result.getVerdict()).append(",")

                    .append(result.isPassed()).append(",")

                    .append(result.getReason()).append("\n");

        }


        Allure.addAttachment(

                "AI Evaluation Results",

                "text/csv",

                report.toString());

    }

}