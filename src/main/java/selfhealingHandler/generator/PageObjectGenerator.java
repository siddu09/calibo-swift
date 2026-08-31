package selfhealingHandler.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import selfhealingHandler.model.ElementSnapshot;
import selfhealingHandler.model.PageSnapshot;
import utils.LoggerUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Generates a first-draft Playwright Page Object (POM) class from a
 * {@link PageSnapshot} (in-memory, or read from a JSON file produced by
 * {@code PlaywrightDomCrawler} / {@code ElementRepositoryWriter}).
 *
 * <p><b>Internal collaborator:</b> most callers should use
 * {@link selfhealingHandler.pipeline.LocatorPageObjectPipeline} instead of
 * calling this class directly - it wires crawling, JSON persistence, and
 * generation together in one call (including batch regeneration and a
 * unified CLI). This class remains the generation engine underneath.</p>
 *
 * <p>Two modes:</p>
 * <ul>
 *   <li><b>PLAIN</b> (default): each element -&gt; a method returning a raw
 *       {@link com.microsoft.playwright.Locator} using the single most stable
 *       locator (testId &gt; id &gt; name &gt; placeholder &gt; role &gt; text
 *       &gt; href &gt; css &gt; xpath).</li>
 *   <li><b>SELF_HEALING</b>: each element -&gt; a method that builds a fluent
 *       {@code selfhealing.ResilientLocator} chain with ALL discovered
 *       candidates (data-testid, id, name, role+name, placeholder, aria-label,
 *       text, href, css, xpath), ordered most-stable-first, and resolves it at
 *       runtime. This removes both the old THREE-locator cap and the
 *       single-point-of-failure of a weak {@code css} locator.</li>
 * </ul>
 *
 * <p>Design notes for the Calibo SWIFT framework:</p>
 * <ul>
 *   <li>SELF_HEALING mode now targets the unified {@code selfhealing.ResilientLocator}
 *       engine (Playwright, unlimited fallbacks, wait-for-attached, Log4j+Allure
 *       telemetry) instead of the legacy {@code LocatorInfo}/{@code SelfHealingLocator}.</li>
 *   <li>Uses the TYPED builder methods ({@code byTestId}, {@code byRole}, ...)
 *       where the crawler identified a specific strategy, and falls back to
 *       {@code byCss}/{@code byXPath} for structural selectors.</li>
 *   <li>De-duplicates member names; guarantees legal Java identifiers.</li>
 *   <li>Skips {@code visible=false} elements (e.g. hidden MUI tab panels).</li>
 *   <li>FIX: SELF_HEALING classes now declare package
 *       {@code pages.generated.selfhealing} to match the output directory
 *       (previously emitted {@code pages.generated}, which mismatched the
 *       {@code .../generated/selfhealing} folder and broke compilation).</li>
 * </ul>
 *
 * Usage (programmatic):
 * <pre>
 *   // plain
 *   PageObjectGenerator.generate(json, "src/main/java/pages/generated");
 *   // self-healing
 *   PageObjectGenerator.generate(json, "src/main/java/pages/generated/selfhealing", Mode.SELF_HEALING);
 * </pre>
 *
 * Usage (CLI):
 * <pre>
 *   java PageObjectGenerator &lt;input.json&gt; &lt;output-dir&gt; [--mode=selfhealing]
 * </pre>
 */
public final class PageObjectGenerator {

    public enum Mode { PLAIN, SELF_HEALING }

    private static final String PLAIN_PACKAGE = "pages.generated";
    private static final String SELF_HEALING_PACKAGE = "pages.generated.selfhealing";

    private static final Pattern NON_ALNUM = Pattern.compile("[^A-Za-z0-9]+");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PageObjectGenerator() {
    }

    // ------------------------------------------------------------------ public API

    /** Default PLAIN mode. */
    public static Path generate(String jsonPath, String outputDir) {
        return generate(jsonPath, outputDir, Mode.PLAIN);
    }

    /** Generate in the requested mode; returns the path of the .java file written. */
    public static Path generate(String jsonPath, String outputDir, Mode mode) {
        try {
            PageSnapshot snapshot = MAPPER.readValue(new File(jsonPath), PageSnapshot.class);
            return generate(snapshot, outputDir, mode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Page Object from: " + jsonPath, e);
        }
    }

    /**
     * Generate directly from an in-memory {@link PageSnapshot} (no JSON
     * re-read). Lets callers that already hold a freshly-crawled snapshot
     * (e.g. a unified crawl-then-generate pipeline) skip the file round-trip.
     */
    public static Path generate(PageSnapshot snapshot, String outputDir, Mode mode) {
        try {
            String className = toPascalCase(snapshot.getPageName()) + "Page";

            String code = (mode == Mode.SELF_HEALING)
                    ? generateSelfHealingClass(className, snapshot)
                    : generatePlainClass(className, snapshot);

            Path dir = Paths.get(outputDir);
            Files.createDirectories(dir);
            Path outFile = dir.resolve(className + ".java");
            Files.write(outFile, code.getBytes(StandardCharsets.UTF_8));

            LoggerUtil.LOGGER.info("[POM-GEN] ({}) Generated {} ({} elements)",
                    mode, outFile.toAbsolutePath(), snapshot.getElements().size());
            return outFile;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to generate Page Object for: " + snapshot.getPageName(), e);
        }
    }

    /** CLI: java PageObjectGenerator &lt;input.json&gt; &lt;output-dir&gt; [--mode=selfhealing] */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java PageObjectGenerator <input.json> <output-dir> [--mode=selfhealing]");
            return;
        }
        Mode mode = Mode.PLAIN;
        for (int i = 2; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("--mode=selfhealing")) {
                mode = Mode.SELF_HEALING;
            }
        }
        generate(args[0], args[1], mode);
    }

    // ============================================================ PLAIN mode

    private static String generatePlainClass(String className, PageSnapshot snapshot) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(PLAIN_PACKAGE).append(";\n\n");
        sb.append("import com.microsoft.playwright.Locator;\n");
        sb.append("import com.microsoft.playwright.Page;\n");
        sb.append("import com.microsoft.playwright.options.AriaRole;\n\n");
        header(sb, snapshot, "raw Locator");
        sb.append("public class ").append(className).append(" {\n\n");
        sb.append("    private final Page page;\n\n");
        sb.append("    public ").append(className).append("(Page page) {\n");
        sb.append("        this.page = page;\n");
        sb.append("    }\n\n");

        Map<String, Integer> nameCounts = new HashMap<>();
        for (ElementSnapshot el : snapshot.getElements()) {
            if (!el.isVisible()) continue;
            String[] loc = bestLocator(el);
            if (loc == null) continue;
            String field = uniqueName(baseName(el), nameCounts);

            comment(sb, el);
            sb.append("    public Locator ").append(field).append("() {\n");
            sb.append("        return ").append(loc[1]).append(";  // via ").append(loc[0]).append("\n");
            sb.append("    }\n\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    // ====================================================== SELF_HEALING mode

    private static String generateSelfHealingClass(String className, PageSnapshot snapshot) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(SELF_HEALING_PACKAGE).append(";\n\n");
        sb.append("import com.microsoft.playwright.Locator;\n");
        sb.append("import com.microsoft.playwright.Page;\n");
        sb.append("import com.microsoft.playwright.options.AriaRole;\n");
        sb.append("import selfhealingHandler.ResilientLocator;\n\n");
        header(sb, snapshot, "fluent ResilientLocator chain (ALL candidates)");
        sb.append("public class ").append(className).append(" {\n\n");
        sb.append("    private final Page page;\n\n");
        sb.append("    public ").append(className).append("(Page page) {\n");
        sb.append("        this.page = page;\n");
        sb.append("    }\n\n");

        Map<String, Integer> nameCounts = new HashMap<>();
        for (ElementSnapshot el : snapshot.getElements()) {
            if (!el.isVisible()) continue;

            List<String> chain = fluentChain(el);
            if (chain.isEmpty()) continue;

            String field = uniqueName(baseName(el), nameCounts);
            String elementLabel = humanLabel(el, field);

            comment(sb, el);
            sb.append("    public Locator ").append(field).append("() {\n");
            sb.append("        return new ResilientLocator(page, \"")
                    .append(esc(elementLabel)).append("\")\n");
            for (String call : chain) {
                sb.append("                ").append(call).append("\n");
            }
            sb.append("                .resolve();\n");
            sb.append("    }\n\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Builds the ordered list of fluent builder calls for one element, using
     * EVERY distinct candidate the crawler captured (no 3-cap), most-stable
     * strategy first. Each entry is a ready-to-emit Java call, e.g.
     * {@code .byTestId("save-btn")}.
     */
    private static List<String> fluentChain(ElementSnapshot el) {
        Map<String, String> lc = el.getLocatorCandidates() == null
                ? new HashMap<>() : el.getLocatorCandidates();

        List<String> out = new ArrayList<>();

        // 1) data-testid (most intentional / stable)
        if (has(el.getDataTestId())) {
            out.add(".byTestId(\"" + esc(el.getDataTestId()) + "\")");
        }

        // 2) id
        if (has(el.getId())) {
            out.add(".byId(\"" + esc(el.getId()) + "\")");
        }

        // 3) name
        if (has(el.getName())) {
            out.add(".byName(\"" + esc(el.getName()) + "\")");
        }

        // 4) role + accessible name (only when we have a name to target)
        if (has(lc.get("getByRole"))) {
            String raw = lc.get("getByRole");           // e.g. "tab=Custom Fields"
            int eq = raw.indexOf('=');
            String role = eq >= 0 ? raw.substring(0, eq) : raw;
            String name = eq >= 0 ? raw.substring(eq + 1) : "";
            String ariaRole = toAriaRoleEnum(role);
            if (has(ariaRole) && has(name)) {
                out.add(".byRole(AriaRole." + ariaRole + ", \"" + esc(name) + "\")");
            }
        }

        // 5) placeholder
        String placeholder = firstNonEmpty(lc.get("getByPlaceholder"), el.getPlaceholder());
        if (has(placeholder)) {
            out.add(".byPlaceholder(\"" + esc(placeholder) + "\")");
        }

        // 6) aria-label (attribute selector via css)
        if (has(el.getAriaLabel())) {
            out.add(".byCss(\"[aria-label=\\\"" + esc(el.getAriaLabel()) + "\\\"]\")");
        }

        // 7) text (short only, to avoid brittle long-text matches)
        String text = firstNonEmpty(lc.get("getByText"), el.getText());
        if (has(text) && text.length() <= 60) {
            out.add(".byText(\"" + esc(text) + "\")");
        }

        // 8) href (structural but meaningful for nav links)
        if (has(el.getHref())) {
            out.add(".byCss(\"" + esc(el.getTag()) + "[href=\\\"" + esc(el.getHref()) + "\\\"]\")");
        }

        // 9) css (weak) — prefer id/name-scoped css if present
        String css = firstNonEmpty(lc.get("cssById"), lc.get("cssByName"), lc.get("css"));
        if (has(css)) {
            out.add(".byCss(\"" + esc(css) + "\")");
        }

        // 10) xpath (fragile — last resort)
        if (has(lc.get("xpath"))) {
            out.add(".byXPath(\"" + esc(lc.get("xpath")) + "\")");
        }

        // distinct, preserve order (NO cap — all candidates become fallbacks)
        List<String> distinct = new ArrayList<>();
        for (String c : out) {
            if (!distinct.contains(c)) distinct.add(c);
        }
        return distinct;
    }

    /** Maps a raw role string ("tab", "text-box") to an AriaRole enum name ("TAB", "TEXTBOX"). */
    private static String toAriaRoleEnum(String role) {
        if (!has(role)) return "";
        // Playwright's AriaRole enum constants are uppercase with no separators
        // (e.g. TEXTBOX, TABLIST). Normalise: strip non-alnum, upper-case.
        return NON_ALNUM.matcher(role).replaceAll("").toUpperCase();
    }

    // ------------------------------------------- PLAIN-mode single-best locator

    /** Returns {strategyName, javaExpression} for the most stable candidate. */
    private static String[] bestLocator(ElementSnapshot el) {
        Map<String, String> lc = el.getLocatorCandidates() == null
                ? new HashMap<>() : el.getLocatorCandidates();

        if (has(el.getDataTestId())) {
            return new String[]{"data-testid", "page.getByTestId(\"" + esc(el.getDataTestId()) + "\")"};
        }
        if (has(lc.get("cssById")) || has(el.getId())) {
            String css = has(lc.get("cssById")) ? lc.get("cssById") : el.getTag() + "#" + el.getId();
            return new String[]{"id", "page.locator(\"" + esc(css) + "\")"};
        }
        if (has(lc.get("cssByName")) || has(el.getName())) {
            String css = has(lc.get("cssByName")) ? lc.get("cssByName")
                    : el.getTag() + "[name=\"" + el.getName() + "\"]";
            return new String[]{"name", "page.locator(\"" + esc(css) + "\")"};
        }
        if (has(lc.get("getByPlaceholder")) || has(el.getPlaceholder())) {
            String ph = has(lc.get("getByPlaceholder")) ? lc.get("getByPlaceholder") : el.getPlaceholder();
            return new String[]{"placeholder", "page.getByPlaceholder(\"" + esc(ph) + "\")"};
        }
        if (has(lc.get("getByRole"))) {
            String raw = lc.get("getByRole");
            int eq = raw.indexOf('=');
            String role = (eq >= 0 ? raw.substring(0, eq) : raw).toUpperCase().replace('-', '_');
            String accName = eq >= 0 ? raw.substring(eq + 1) : "";
            String expr = has(accName)
                    ? "page.getByRole(AriaRole." + role
                    + ", new Page.GetByRoleOptions().setName(\"" + esc(accName) + "\"))"
                    : "page.getByRole(AriaRole." + role + ")";
            return new String[]{"role", expr};
        }
        if (has(lc.get("getByText")) && lc.get("getByText").length() <= 60) {
            return new String[]{"text", "page.getByText(\"" + esc(lc.get("getByText")) + "\")"};
        }
        if (has(el.getHref())) {
            String css = el.getTag() + "[href=\"" + el.getHref() + "\"]";
            return new String[]{"href", "page.locator(\"" + esc(css) + "\")"};
        }
        if (has(lc.get("css")))   return new String[]{"css (weak)", "page.locator(\"" + esc(lc.get("css")) + "\")"};
        if (has(lc.get("xpath"))) return new String[]{"xpath (fragile)", "page.locator(\"" + esc(lc.get("xpath")) + "\")"};
        return null;
    }

    // ---------------------------------------------------------- shared helpers

    private static void header(StringBuilder sb, PageSnapshot snapshot, String flavour) {
        sb.append("/**\n");
        sb.append(" * Auto-generated Page Object for: ").append(snapshot.getPageName()).append("\n");
        sb.append(" * Source URL : ").append(nz(snapshot.getUrl())).append("\n");
        sb.append(" * Strategy   : ").append(flavour).append("\n");
        sb.append(" * Generated  : STARTING SKELETON - review before use.\n");
        sb.append(" */\n");
    }

    private static void comment(StringBuilder sb, ElementSnapshot el) {
        sb.append("    // <").append(el.getTag()).append("> index=").append(el.getIndex());
        if (!nz(el.getText()).isEmpty()) {
            sb.append("  text=\"").append(trim(el.getText(), 40)).append("\"");
        }
        sb.append("\n");
    }

    private static String baseName(ElementSnapshot el) {
        String src = firstNonEmpty(el.getText(), el.getAriaLabel(),
                el.getPlaceholder(), el.getName(), el.getId());
        String camel = toCamelCase(cleanLabel(src));
        if (camel.isEmpty() && has(el.getHref())) {
            String[] seg = el.getHref().split("/");
            String last = seg.length > 0 ? seg[seg.length - 1] : "";
            camel = toCamelCase(last) + toPascalCase(el.getTag());
        }
        if (camel.isEmpty()) {
            camel = toCamelCase(nz(el.getRole()) + " " + nz(el.getTag()));
        }
        if (camel.isEmpty()) {
            camel = "element";
        }
        if (Character.isDigit(camel.charAt(0))) {
            camel = toCamelCase(nz(el.getTag())) + toPascalCase(camel);
        }
        return camel;
    }

    /** A readable name for logs/reports, e.g. "Save" or the field name as fallback. */
    private static String humanLabel(ElementSnapshot el, String field) {
        String src = firstNonEmpty(el.getText(), el.getAriaLabel(),
                el.getPlaceholder(), el.getName(), el.getId());
        String label = trim(cleanLabel(src), 60);
        return label.isEmpty() ? field : label;
    }

    private static String uniqueName(String base, Map<String, Integer> counts) {
        int n = counts.merge(base, 1, Integer::sum);
        return n == 1 ? base : base + n;
    }

    private static String cleanLabel(String label) {
        if (label == null) return "";
        String cleaned = label.replaceAll("\\(.*?\\)", "").trim();
        return cleaned.isEmpty() ? label.trim() : cleaned;
    }

    private static String toPascalCase(String text) {
        StringBuilder sb = new StringBuilder();
        for (String p : NON_ALNUM.split(nz(text))) {
            if (p.isEmpty()) continue;
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
        }
        return sb.toString();
    }

    private static String toCamelCase(String text) {
        String p = toPascalCase(text);
        return p.isEmpty() ? "" : Character.toLowerCase(p.charAt(0)) + p.substring(1);
    }

    // ------------------------------------------------------------- tiny utils

    private static boolean has(String s) { return s != null && !s.isEmpty(); }
    private static String nz(String s) { return s == null ? "" : s; }
    private static String esc(String s) { return nz(s).replace("\\", "\\\\").replace("\"", "\\\""); }

    private static String trim(String s, int max) {
        s = nz(s).replaceAll("\\s+", " ").trim();
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }

    private static String firstNonEmpty(String... vals) {
        for (String v : vals) if (has(v)) return v;
        return "";
    }
}
