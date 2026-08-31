package selfhealingHandler.pipeline;

import com.microsoft.playwright.Page;
import selfhealingHandler.crawler.PlaywrightDomCrawler;
import selfhealingHandler.generator.PageObjectGenerator;
import selfhealingHandler.model.PageSnapshot;
import selfhealingHandler.repository.ElementRepositoryWriter;
import utils.LoggerUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Single unified entry point for the Calibo SWIFT "locator discovery ->
 * Page Object generation" workflow, replacing the previously separate,
 * manually-wired steps:
 *
 * <pre>
 *   PlaywrightDomCrawler       -&gt; crawl page             -&gt; PageSnapshot
 *   ElementRepositoryWriter    -&gt; persist snapshot        -&gt; JSON
 *   PageObjectGenerator        -&gt; generate Page Object(s) -&gt; .java
 * </pre>
 *
 * <p>Those three classes are retained as internal collaborators (composition)
 * so their individually-tested logic is reused as-is; this class only
 * orchestrates them behind one call.</p>
 *
 * <h2>Usage</h2>
 * <pre>
 *   // Live crawl (needs an open Playwright Page)
 *   PipelineResult result = LocatorPageObjectPipeline.run(page, "LoginPage",
 *           PipelineOptions.builder().mode(PipelineMode.BOTH).build());
 *
 *   // Re-generate from an existing JSON snapshot (no live page needed)
 *   PipelineResult result = LocatorPageObjectPipeline.runFromJson(
 *           "src/main/java/selfhealing/repository/LoginPage.json",
 *           PipelineOptions.builder().mode(PipelineMode.SELF_HEALING).build());
 *
 *   // Batch-regenerate every JSON snapshot under a repository root
 *   List&lt;PipelineResult&gt; results = LocatorPageObjectPipeline.runBatch(
 *           "src/main/java/selfhealing/repository",
 *           PipelineOptions.builder().mode(PipelineMode.SELF_HEALING).build());
 * </pre>
 *
 * <h2>CLI</h2>
 * <pre>
 *   java LocatorPageObjectPipeline --json &lt;input.json&gt; &lt;pomOutDir&gt; [--mode=selfhealing|plain|both]
 *   java LocatorPageObjectPipeline --batch &lt;repoRootDir&gt; &lt;pomOutDir&gt; [--mode=...]
 * </pre>
 */
public final class LocatorPageObjectPipeline {

    private LocatorPageObjectPipeline() {
    }

    // ------------------------------------------------------------ live crawl

    /** Crawl a live page, optionally persist the JSON snapshot, then generate POM(s). */
    public static PipelineResult run(Page page, String pageName, PipelineOptions options) {
        PageSnapshot snapshot = new PlaywrightDomCrawler().crawl(page, pageName);

        Path jsonPath = null;
        if (options.getJsonOutputDir() != null) {
            new ElementRepositoryWriter().write(snapshot, options.getJsonOutputDir());
            jsonPath = Paths.get(options.getJsonOutputDir(), snapshot.getPageName() + ".json");
        }

        return generateFromSnapshot(snapshot, jsonPath, options);
    }

    // ------------------------------------------------------------ from JSON

    /** Generate POM(s) from an existing locator snapshot JSON (no live page required). */
    public static PipelineResult runFromJson(String jsonPath, PipelineOptions options) {
        PageSnapshot snapshot = readSnapshot(jsonPath);
        return generateFromSnapshot(snapshot, Paths.get(jsonPath), options);
    }

    // ------------------------------------------------------------ batch

    /** Regenerate POM(s) for every {@code *.json} snapshot found (recursively) under {@code repoRootDir}. */
    public static List<PipelineResult> runBatch(String repoRootDir, PipelineOptions options) {
        Path repoRoot = Paths.get(repoRootDir);
        if (!Files.exists(repoRoot)) {
            throw new IllegalStateException("Repository root not found: " + repoRoot.toAbsolutePath());
        }

        List<Path> jsonFiles = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(repoRoot)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
                    .sorted()
                    .forEach(jsonFiles::add);
        } catch (IOException e) {
            throw new RuntimeException("Failed to walk repository root: " + repoRoot, e);
        }

        LoggerUtil.LOGGER.info("[PIPELINE-BATCH] Found {} JSON snapshot(s) under {}",
                jsonFiles.size(), repoRoot.toAbsolutePath());

        List<PipelineResult> results = new ArrayList<>();
        for (Path json : jsonFiles) {
            try {
                results.add(runFromJson(json.toString(), options));
            } catch (Exception e) {
                LoggerUtil.LOGGER.error("[PIPELINE-BATCH] FAILED {} : {}", json, e.getMessage());
            }
        }
        return results;
    }

    // ------------------------------------------------------------ shared core

    private static PipelineResult generateFromSnapshot(PageSnapshot snapshot, Path jsonPath, PipelineOptions options) {
        Path plainPath = null;
        Path selfHealingPath = null;

        if (options.getMode() == PipelineMode.PLAIN || options.getMode() == PipelineMode.BOTH) {
            plainPath = PageObjectGenerator.generate(snapshot, options.getPomOutputDir(), PageObjectGenerator.Mode.PLAIN);
        }
        if (options.getMode() == PipelineMode.SELF_HEALING || options.getMode() == PipelineMode.BOTH) {
            selfHealingPath = PageObjectGenerator.generate(snapshot, options.getSelfHealingPomOutputDir(), PageObjectGenerator.Mode.SELF_HEALING);
        }

        return new PipelineResult(snapshot.getPageName(), jsonPath, plainPath, selfHealingPath);
    }

    private static PageSnapshot readSnapshot(String jsonPath) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(new java.io.File(jsonPath), PageSnapshot.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read locator snapshot JSON: " + jsonPath, e);
        }
    }

    // ------------------------------------------------------------ CLI

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage:");
            System.out.println("  java LocatorPageObjectPipeline --json <input.json> <pomOutDir> [--mode=plain|selfhealing|both]");
            System.out.println("  java LocatorPageObjectPipeline --batch <repoRootDir> <pomOutDir> [--mode=plain|selfhealing|both]");
            return;
        }

        PipelineMode mode = PipelineMode.BOTH;
        for (String arg : args) {
            if (arg.startsWith("--mode=")) {
                mode = parseMode(arg.substring("--mode=".length()));
            }
        }

        PipelineOptions options = PipelineOptions.builder()
                .pomOutputDir(args[2])
                .mode(mode)
                .build();

        if (args[0].equalsIgnoreCase("--json")) {
            PipelineResult result = runFromJson(args[1], options);
            System.out.println("Generated: " + result);
        } else if (args[0].equalsIgnoreCase("--batch")) {
            List<PipelineResult> results = runBatch(args[1], options);
            System.out.println("Generated " + results.size() + " page object set(s).");
        } else {
            System.out.println("Unknown command: " + args[0] + " (expected --json or --batch)");
        }
    }

    private static PipelineMode parseMode(String value) {
        return switch (value.toLowerCase()) {
            case "plain" -> PipelineMode.PLAIN;
            case "selfhealing", "self_healing", "self-healing" -> PipelineMode.SELF_HEALING;
            default -> PipelineMode.BOTH;
        };
    }
}
