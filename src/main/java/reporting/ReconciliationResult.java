package reporting;

import java.util.ArrayList;
import java.util.List;

public class ReconciliationResult {

    public static class Discrepancy {
        public final String key;
        public final String type;
        public final String field;
        public final String expected;
        public final String actual;

        public Discrepancy(String key, String type, String field,
                           String expected, String actual) {
            this.key = key;
            this.type = type;
            this.field = field;
            this.expected = expected;
            this.actual = actual;
        }

        @Override
        public String toString() {
            return String.format(
                    "[%s] key=%s field=%s expected=%s actual=%s",
                    type, key, field == null ? "-" : field,
                    expected == null ? "-" : expected,
                    actual == null ? "-" : actual);
        }
    }

    private final List<Discrepancy> discrepancies = new ArrayList<>();
    private int matchedRecords = 0;
    private int expectedTotal = 0;
    private int actualTotal = 0;

    public void addDiscrepancy(Discrepancy d) {
        discrepancies.add(d);
    }

    public void incrementMatched() {
        matchedRecords++;
    }

    public void setExpectedTotal(int expectedTotal) {
        this.expectedTotal = expectedTotal;
    }

    public void setActualTotal(int actualTotal) {
        this.actualTotal = actualTotal;
    }

    public List<Discrepancy> getDiscrepancies() {
        return discrepancies;
    }

    public boolean isReconciled() {
        return discrepancies.isEmpty();
    }

    public int getMatchedRecords() {
        return matchedRecords;
    }

    public int getExpectedTotal() {
        return expectedTotal;
    }

    public int getActualTotal() {
        return actualTotal;
    }

    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append(" FILLO (Excel) vs H2 (DB) RECONCILIATION\n");
        sb.append("========================================\n");
        sb.append("Expected records (Excel) : ").append(expectedTotal).append("\n");
        sb.append("Actual records   (DB)    : ").append(actualTotal).append("\n");
        sb.append("Matched records          : ").append(matchedRecords).append("\n");
        sb.append("Discrepancies            : ").append(discrepancies.size()).append("\n");
        sb.append("Status                   : ")
                .append(isReconciled() ? "RECONCILED" : "MISMATCH").append("\n");
        if (!discrepancies.isEmpty()) {
            sb.append("----------------------------------------\n");
            for (Discrepancy d : discrepancies) {
                sb.append(d).append("\n");
            }
        }
        sb.append("========================================\n");
        return sb.toString();
    }
}
