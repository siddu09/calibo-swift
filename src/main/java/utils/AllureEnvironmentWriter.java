package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Writes/updates {@code environment.properties} in the Allure results directory so that
 * an EXECUTIVE SUMMARY (e.g. AI evaluation scores) is rendered in the "Environment" widget
 * on the Allure Overview page — i.e. visible the moment the report is opened, WITHOUT
 * drilling into any individual test.
 *
 * <p>The results directory defaults to {@code allure-results} but can be overridden with
 * {@code -Dallure.results.directory=...} to match your project (e.g.
 * {@code reports/ai/allure-results}).
 *
 * <p>Thread-safe: writes are synchronized and merge (never clobber) existing keys, so
 * multiple tests can each contribute a summary line across a run.
 */
public final class AllureEnvironmentWriter {

    private static final Object LOCK = new Object();

    private AllureEnvironmentWriter() {
    }

    /** Resolves the Allure results directory (system property overridable). */
    private static String resultsDir() {
        // Common defaults; -Dallure.results.directory wins if provided.
        return System.getProperty("allure.results.directory", "allure-results");
    }

    /**
     * Adds or updates a single key/value pair in the Environment widget.
     * Existing keys are overwritten; other keys are preserved.
     */
    public static void put(String key, String value) {
        Map<String, String> one = new LinkedHashMap<>();
        one.put(key, value);
        putAll(one);
    }

    /**
     * Adds or updates multiple key/value pairs in the Environment widget in one write.
     * Insertion order of the supplied map is preserved for readability.
     */
    public static void putAll(Map<String, String> entries) {
        if (entries == null || entries.isEmpty()) {
            return;
        }
        synchronized (LOCK) {
            try {
                File dir = new File(resultsDir());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, "environment.properties");

                // Preserve insertion order across reads/writes by using an ordered backing map.
                Properties existing = new OrderedProperties();
                if (file.exists()) {
                    try (InputStream in = new FileInputStream(file)) {
                        existing.load(in);
                    }
                }
                for (Map.Entry<String, String> e : entries.entrySet()) {
                    existing.setProperty(e.getKey(), e.getValue() == null ? "" : e.getValue());
                }
                try (OutputStream out = new FileOutputStream(file)) {
                    existing.store(out, "Allure Environment - Executive Summary (auto-generated)");
                }
                LoggerUtil.LOGGER.info(
                        "[ALLURE-ENV] Executive summary updated ({} keys) at {}",
                        entries.size(), file.getPath());
            } catch (Exception ex) {
                LoggerUtil.LOGGER.warn(
                        "[ALLURE-ENV] Failed to write environment.properties: {}",
                        ex.getMessage());
            }
        }
    }

    /**
     * Properties subclass that preserves insertion order when stored, so the Environment
     * widget lists the executive-summary rows in a stable, readable order.
     */
    private static final class OrderedProperties extends Properties {
        private final java.util.LinkedHashSet<Object> order = new java.util.LinkedHashSet<>();

        @Override
        public synchronized Object put(Object key, Object value) {
            order.add(key);
            return super.put(key, value);
        }

        @Override
        public synchronized java.util.Enumeration<Object> keys() {
            return java.util.Collections.enumeration(order);
        }

        @Override
        public java.util.Set<Map.Entry<Object, Object>> entrySet() {
            java.util.LinkedHashMap<Object, Object> ordered = new java.util.LinkedHashMap<>();
            for (Object k : order) {
                ordered.put(k, get(k));
            }
            return ordered.entrySet();
        }
    }
}
