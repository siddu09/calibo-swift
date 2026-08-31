package reporting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataReconciler {

    public static ReconciliationResult reconcile(
            List<Map<String, String>> expected,
            List<Map<String, String>> actual,
            String expectedKey,
            String actualKey,
            Map<String, String> fieldMapping) {

        ReconciliationResult result = new ReconciliationResult();
        result.setExpectedTotal(expected.size());
        result.setActualTotal(actual.size());

        Map<String, Map<String, String>> actualByKey = new HashMap<>();
        for (Map<String, String> row : actual) {
            actualByKey.put(norm(row.get(actualKey)), row);
        }

        Map<String, Boolean> seenActual = new HashMap<>();

        for (Map<String, String> expRow : expected) {
            String key = norm(expRow.get(expectedKey));
            Map<String, String> actRow = actualByKey.get(key);

            if (actRow == null) {
                result.addDiscrepancy(new ReconciliationResult.Discrepancy(
                        key, "MISSING_IN_DB", null,
                        "present in Excel", null));
                continue;
            }

            seenActual.put(key, true);

            boolean rowMatches = true;
            for (Map.Entry<String, String> map : fieldMapping.entrySet()) {
                String expCol = map.getKey();
                String actCol = map.getValue();
                String expVal = expRow.get(expCol);
                String actVal = actRow.get(actCol);

                if (!valuesEqual(expVal, actVal)) {
                    rowMatches = false;
                    result.addDiscrepancy(new ReconciliationResult.Discrepancy(
                            key, "FIELD_MISMATCH", expCol, expVal, actVal));
                }
            }
            if (rowMatches) {
                result.incrementMatched();
            }
        }

        for (Map<String, String> row : actual) {
            String key = norm(row.get(actualKey));
            if (!seenActual.containsKey(key)) {
                result.addDiscrepancy(new ReconciliationResult.Discrepancy(
                        key, "MISSING_IN_EXCEL", null,
                        null, "present in DB"));
            }
        }

        return result;
    }

    private static boolean valuesEqual(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;

        String x = a.trim();
        String y = b.trim();

        try {
            double da = Double.parseDouble(x);
            double db = Double.parseDouble(y);
            return Math.abs(da - db) < 0.001;
        } catch (NumberFormatException ignored) {
            return x.equalsIgnoreCase(y);
        }
    }

    private static String norm(String s) {
        return s == null ? "" : s.trim();
    }
}