package ai.extractors;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RagEntityExtractor {

    private static final Pattern NUMBERED_LIST_ITEM =
            Pattern.compile("\\d+\\.\\s*([a-zA-Z][a-zA-Z0-9_]*)\\s*-");

    private static final Pattern SNAKE_CASE_TOKEN =
            Pattern.compile("\\b[a-z][a-z0-9]*(?:_[a-z0-9]+)+\\b");

    private static final Set<String> IGNORED_TOKENS = Set.of(
            "table", "tables", "data", "store", "available",
            "relationships", "relationship", "question", "answer",
            "expected", "rules", "focus", "ignore", "ordering",
            "descriptions", "description", "additional", "acceptable",
            "pass", "fail", "present", "missing", "mentioned", "result",
            "core", "business", "logical", "semantic", "model",
            "response", "system", "let", "me", "summarize", "from",
            "the", "all", "records", "information"
    );

    private RagEntityExtractor() {
    }

    /**
     * Extract expected tables from context.
     *
     * Supports two formats:
     *  1. Plain "EXPECTED TABLES:\n table1\n table2 ..." block
     *  2. Markdown table rows "| table1 | description |"
     */
    public static List<String> extractExpectedTables(String context) {

        if (context == null || context.isBlank()) {
            return new ArrayList<>();
        }

        List<String> fromPlainList = extractFromExpectedTablesList(context);
        if (!fromPlainList.isEmpty()) {
            return fromPlainList;
        }

        return extractFromMarkdownTable(context);
    }

    /**
     * Tables from the expected list that are actually mentioned in the answer.
     */
    public static List<String> extractMentionedTables(
            String answer,
            List<String> expectedTables) {

        List<String> found = new ArrayList<>();

        if (answer == null || answer.isBlank() || expectedTables == null) {
            return found;
        }

        String normalizedAnswer = answer.toLowerCase(Locale.ROOT);

        for (String table : expectedTables) {

            Pattern pattern = Pattern.compile(
                    "\\b" + Pattern.quote(table) + "\\b"
            );

            if (pattern.matcher(normalizedAnswer).find()) {
                found.add(table);
            }
        }

        return found;
    }

    /**
     * Tables from the expected list not found in the answer.
     */
    public static List<String> findMissingTables(
            List<String> expected,
            List<String> actual) {

        Set<String> actualSet = new LinkedHashSet<>(actual);
        List<String> missing = new ArrayList<>();

        for (String table : expected) {
            if (!actualSet.contains(table)) {
                missing.add(table);
            }
        }

        return missing;
    }

    /**
     * Table-like entities mentioned in the answer that are NOT part of the
     * expected table list. These represent potential hallucinations
     * (e.g. the RAG answer listing tables from a different schema/version).
     */
    public static List<String> extractPossibleHallucinatedTables(
            String answer,
            List<String> expectedTables) {

        if (answer == null || answer.isBlank()) {
            return new ArrayList<>();
        }

        Set<String> expected = new LinkedHashSet<>();
        if (expectedTables != null) {
            for (String table : expectedTables) {
                expected.add(table.toLowerCase(Locale.ROOT));
            }
        }

        Set<String> listedEntities = extractListedEntities(answer);
        List<String> hallucinated = new ArrayList<>();

        for (String entity : listedEntities) {

            if (expected.contains(entity)) {
                continue;
            }

            if (IGNORED_TOKENS.contains(entity)) {
                continue;
            }

            hallucinated.add(entity);
        }

        return hallucinated;
    }

    /**
     * Collects every table-like token the answer actually lists, using two
     * strategies:
     *  1. Numbered list items, e.g. "5. exchange_rates - Daily currency rates"
     *  2. Standalone snake_case tokens anywhere in the text, e.g. "pii_customers"
     */
    private static Set<String> extractListedEntities(String answer) {

        Set<String> entities = new LinkedHashSet<>();
        String normalized = answer.toLowerCase(Locale.ROOT);

        Matcher numbered = NUMBERED_LIST_ITEM.matcher(normalized);
        while (numbered.find()) {
            entities.add(numbered.group(1));
        }

        Matcher snakeCase = SNAKE_CASE_TOKEN.matcher(normalized);
        while (snakeCase.find()) {
            entities.add(snakeCase.group());
        }

        return entities;
    }

    private static List<String> extractFromExpectedTablesList(String context) {

        Set<String> tables = new LinkedHashSet<>();

        String lower = context.toLowerCase(Locale.ROOT);
        int start = lower.indexOf("expected tables:");

        if (start < 0) {
            return new ArrayList<>();
        }

        int blockStart = start + "expected tables:".length();
        int rulesStart = lower.indexOf("rules:", blockStart);

        String block = rulesStart >= 0
                ? context.substring(blockStart, rulesStart)
                : context.substring(blockStart);

        String[] lines = block.split("\\R");

        for (String line : lines) {

            String candidate = line.trim().toLowerCase(Locale.ROOT);

            if (candidate.matches("[a-z][a-z0-9_]*")) {
                tables.add(candidate);
            }
        }

        return new ArrayList<>(tables);
    }

    private static List<String> extractFromMarkdownTable(String context) {

        Set<String> tables = new LinkedHashSet<>();

        String[] lines = context.split("\\R");

        for (String line : lines) {

            String trimmed = line.trim();

            if (!trimmed.startsWith("|")) {
                continue;
            }

            if (trimmed.contains("---")) {
                continue;
            }

            String lowerLine = trimmed.toLowerCase(Locale.ROOT);

            if (lowerLine.contains("table") && lowerLine.contains("description")) {
                continue;
            }

            String[] columns = trimmed.split("\\|");

            if (columns.length < 2) {
                continue;
            }

            String tableName = columns[1].trim().toLowerCase(Locale.ROOT);

            if (tableName.matches("[a-z][a-z0-9_]*")) {
                tables.add(tableName);
            }
        }

        return new ArrayList<>(tables);
    }
}