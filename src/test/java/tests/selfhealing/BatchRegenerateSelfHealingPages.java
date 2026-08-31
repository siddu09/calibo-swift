package tests.selfhealing;

import org.testng.annotations.Test;
import selfhealingHandler.pipeline.LocatorPageObjectPipeline;
import selfhealingHandler.pipeline.PipelineMode;
import selfhealingHandler.pipeline.PipelineOptions;
import selfhealingHandler.pipeline.PipelineResult;
import utils.LoggerUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Batch regenerator for ALL self-healing Page Objects.
 *
 * Walks the locator-repository tree, finds every *.json snapshot produced by
 * the DOM crawler, and regenerates a ResilientLocator-based POM for each one
 * (SELF_HEALING mode) in a single run.
 *
 * Run it:
 *   mvn test -Dtest=BatchRegenerateSelfHealingPages
 */
public class BatchRegenerateSelfHealingPages {

    /** Root of the locator-repository JSON tree (recursively scanned). */
    private static final String REPO_ROOT = "src/main/java/selfhealing/repository";

    /** Destination package dir for generated self-healing POMs. */
    private static final String GENERATED_SH_DIR = "src/main/java/pages/generated/selfhealing";

    @Test
    public void regenerateAllSelfHealingPages() throws IOException {
        Path repoRoot = Paths.get(REPO_ROOT);
        if (!Files.exists(repoRoot)) {
            throw new IllegalStateException("Repository root not found: " + repoRoot.toAbsolutePath());
        }

        // Ensure output directory exists.
        Files.createDirectories(Paths.get(GENERATED_SH_DIR));

        List<Path> jsonFiles = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(repoRoot)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
                    .sorted()
                    .forEach(jsonFiles::add);
        }

        LoggerUtil.LOGGER.info("[BATCH-GEN] Found {} JSON snapshot(s) under {}",
                jsonFiles.size(), repoRoot.toAbsolutePath());

        List<String> succeeded = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        PipelineOptions options = PipelineOptions.builder()
                .pomOutputDir(GENERATED_SH_DIR)
                .mode(PipelineMode.SELF_HEALING)
                .build();

        for (Path json : jsonFiles) {
            String name = repoRoot.relativize(json).toString().replace('\\', '/');
            try {
                PipelineResult result = LocatorPageObjectPipeline.runFromJson(json.toString(), options);
                LoggerUtil.LOGGER.info("[BATCH-GEN] OK   {} -> {}",
                        name, result.selfHealingPomPath().getFileName());
                succeeded.add(name);
            } catch (Exception e) {
                LoggerUtil.LOGGER.error("[BATCH-GEN] FAIL {} : {}", name, rootMessage(e));
                failed.add(name + "  (" + rootMessage(e) + ")");
            }
        }

        // ---- Summary ----
        LoggerUtil.LOGGER.info("========================================");
        LoggerUtil.LOGGER.info(" BATCH SELF-HEALING POM REGENERATION");
        LoggerUtil.LOGGER.info("========================================");
        LoggerUtil.LOGGER.info(" JSON snapshots found : {}", jsonFiles.size());
        LoggerUtil.LOGGER.info(" Generated OK         : {}", succeeded.size());
        LoggerUtil.LOGGER.info(" Failed               : {}", failed.size());
        LoggerUtil.LOGGER.info(" Output dir           : {}", GENERATED_SH_DIR);
        LoggerUtil.LOGGER.info("========================================");

        if (!failed.isEmpty()) {
            LoggerUtil.LOGGER.warn("[BATCH-GEN] The following JSONs failed to generate:");
            failed.forEach(f -> LoggerUtil.LOGGER.warn("   - {}", f));
        }

        // Do not hard-fail the run on a single bad JSON; surface counts instead.
        // Flip to an assertion here if you want the build to break on any failure:
        // Assert.assertTrue(failed.isEmpty(), "Some JSONs failed to generate: " + failed);
    }

    /** First line of the deepest cause - keeps the FAIL log readable. */
    private static String rootMessage(Throwable t) {
        Throwable root = t;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String msg = root.getMessage();
        if (msg == null) {
            return root.getClass().getSimpleName();
        }
        int nl = msg.indexOf('\n');
        return nl >= 0 ? msg.substring(0, nl).trim() : msg.trim();
    }
}