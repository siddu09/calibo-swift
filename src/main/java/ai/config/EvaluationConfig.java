package ai.config;

import ai.models.EvaluationEngine;

public final class EvaluationConfig {

    private EvaluationConfig() {
    }

    public static EvaluationEngine getEngine() {
        String engine = System.getProperty("evaluation.engine", "HYBRID");

        try {
            return EvaluationEngine.valueOf(engine.toUpperCase());
        } catch (Exception e) {
            return EvaluationEngine.HYBRID;
        }
    }

    public static double getThreshold() {
        String threshold = System.getProperty("evaluation.threshold", "0.80");

        try {
            return Double.parseDouble(threshold);
        } catch (Exception e) {
            return 0.80;
        }
    }

    public static double getHallucinationPenaltyThreshold() {
        String threshold = System.getProperty("evaluation.hallucination.penalty.threshold", "0.20");

        try {
            return Double.parseDouble(threshold);
        } catch (Exception e) {
            return 0.20;
        }
    }

    public static boolean isStrictHallucinationCheckEnabled() {
        return Boolean.parseBoolean(
                System.getProperty("evaluation.strict.hallucination.check", "true")
        );
    }
}
