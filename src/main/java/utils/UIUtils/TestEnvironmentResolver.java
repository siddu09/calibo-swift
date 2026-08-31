package utils.UIUtils;

import com.microsoft.playwright.Page;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Resolves the target application URL for UI tests.
 *
 * Resolution order:
 * 1. -DDPSURL system property
 * 2. src/test/resources/config/envConfig/ui/qa.properties -> DPSURL
 * 3. Fallback: current page URL with /login stripped, appended with /projects
 */
public final class TestEnvironmentResolver {

    private static final String QA_PROPERTIES_PATH =
            "src/test/resources/config/envConfig/ui/qa.properties";

    private TestEnvironmentResolver() {
    }

    public static String resolveTargetUrl(Page page) {

        String targetUrl = System.getProperty("DPSURL");

        if (isBlank(targetUrl)) {
            targetUrl = readFromQaProperties();
        }

        if (isBlank(targetUrl)) {
            targetUrl = page.url().replaceAll("/login$", "") + "/projects";
        }

        return targetUrl;
    }

    private static String readFromQaProperties() {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(QA_PROPERTIES_PATH));
            return properties.getProperty("DPSURL");

        } catch (Exception ignored) {
            return null;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}