package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import reporting.ExecutionSummaryGenerator;
import utils.AllureReportGenerator;
import utils.LoggerUtil;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public class SuiteListener implements ISuiteListener {

    /**
     * Allure writes results here during execution.
     */
    private static final String STAGING = "allure-results";

    @Override
    public void onStart(ISuite suite) {

        // Fresh staging so this run starts clean
        cleanDirectory(Paths.get(STAGING));

        LoggerUtil.LOGGER.info(
                "[REPORT] Staging cleaned: " + STAGING);
    }

    @Override
    public void onFinish(ISuite suite) {

        String category = resolveCategory(suite);

        String resultsDir =
                "reports/" + category + "/allure-results";

        String reportDir =
                "reports/" + category + "/allure-report";

        try {

            // Route this run's results into category folder
            cleanDirectory(Paths.get(resultsDir));

            copyDirectory(
                    Paths.get(STAGING),
                    Paths.get(resultsDir));

            // Environment details
            writeEnvironment(resultsDir);

            // Restore previous history for trend graphs
            Path previousHistory =
                    Paths.get(reportDir, "history");

            if (Files.exists(previousHistory)) {

                copyDirectory(
                        previousHistory,
                        Paths.get(resultsDir, "history"));
            }

            // Generate execution summary
            ExecutionSummaryGenerator.generateSummary(
                    suite,
                    "reports/" + category);

            // Generate Allure report
            AllureReportGenerator.generateReport(
                    resultsDir,
                    reportDir);

            LoggerUtil.LOGGER.info(
                    "[" + category.toUpperCase()
                            + "] Allure report generated at: "
                            + reportDir);

        } catch (IOException e) {

            LoggerUtil.LOGGER.error(
                    "[REPORT] Failed to generate report for "
                            + category,
                    e);
        }
    }

    private String resolveCategory(ISuite suite) {

        String category =
                suite.getParameter("category");

        return (category == null || category.isBlank())
                ? "combined"
                : category.toLowerCase();
    }

    private void writeEnvironment(String resultsDir)
            throws IOException {

        Files.createDirectories(Paths.get(resultsDir));

        String content = """
                Browser=chromium
                Environment=QA
                Execution=Local
                Framework=Playwright + TestNG
                """;

        Files.writeString(
                Paths.get(resultsDir, "environment.properties"),
                content);
    }

    private void cleanDirectory(Path dir) {

        try {

            if (Files.exists(dir)) {

                try (Stream<Path> walk = Files.walk(dir)) {

                    walk.sorted(Comparator.reverseOrder())
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException ignored) {
                                }
                            });
                }
            }

            Files.createDirectories(dir);

        } catch (IOException e) {

            LoggerUtil.LOGGER.error(
                    "[REPORT] Failed cleaning directory: "
                            + dir,
                    e);
        }
    }

    private void copyDirectory(
            Path source,
            Path target) throws IOException {

        if (!Files.exists(source)) {
            return;
        }

        Files.createDirectories(target);

        try (Stream<Path> walk = Files.walk(source)) {

            walk.forEach(src -> {

                try {

                    Path destination =
                            target.resolve(
                                    source.relativize(src));

                    if (Files.isDirectory(src)) {

                        Files.createDirectories(destination);

                    } else {

                        Files.copy(
                                src,
                                destination,
                                StandardCopyOption.REPLACE_EXISTING);
                    }

                } catch (IOException e) {

                    throw new RuntimeException(e);
                }
            });
        }
    }
}