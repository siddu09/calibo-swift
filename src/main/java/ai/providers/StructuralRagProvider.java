package ai.providers;

import ai.config.EvaluationConfig;
import ai.extractors.RagEntityExtractor;
import ai.models.EvaluationMetric;
import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;
import utils.LoggerUtil;

import java.util.List;

public class StructuralRagProvider implements EvaluationProvider {

    @Override
    public EvaluationResult evaluate(EvaluationRequest request) {

        if (!canEvaluate(request)) {
            return unsupported(request);
        }

        List<String> expectedTables =
                RagEntityExtractor.extractExpectedTables(request.getContext());

        List<String> mentionedTables =
                RagEntityExtractor.extractMentionedTables(
                        request.getAnswer(),
                        expectedTables);

        List<String> missingTables =
                RagEntityExtractor.findMissingTables(
                        expectedTables,
                        mentionedTables);

        List<String> hallucinatedTables =
                RagEntityExtractor.extractPossibleHallucinatedTables(
                        request.getAnswer(),
                        expectedTables);

        double coverageScore = expectedTables.isEmpty()
                ? 0.0
                : (double) mentionedTables.size() / expectedTables.size();

        double penaltyScore = calculatePenaltyScore(hallucinatedTables, expectedTables);

        double finalScore = Math.max(0.0, coverageScore - penaltyScore);
        double threshold = EvaluationConfig.getThreshold();

        boolean passed = finalScore >= threshold
                && missingTables.isEmpty()
                && isHallucinationAcceptable(hallucinatedTables);

        String verdict = determineVerdict(passed, coverageScore, penaltyScore);

        String reason =
                "Expected tables=" + expectedTables.size()
                        + ", Mentioned=" + mentionedTables.size()
                        + ", Missing=" + missingTables
                        + ", Hallucinated=" + hallucinatedTables
                        + ", Coverage=" + round(coverageScore)
                        + ", Penalty=" + round(penaltyScore)
                        + ", Score=" + round(finalScore);

        LoggerUtil.LOGGER.info(
                "[AI-EVAL] Provider=STRUCTURAL_RAG | Expected={} | Mentioned={} | Missing={} | Hallucinated={} | Score={} | Verdict={}",
                expectedTables.size(),
                mentionedTables.size(),
                missingTables,
                hallucinatedTables,
                round(finalScore),
                verdict
        );

        return EvaluationResult.builder()
                .metric(request.getMetric() == null
                        ? EvaluationMetric.ANSWER_RELEVANCY
                        : request.getMetric())
                .question(request.getQuestion())
                .score(round(finalScore))
                .coverageScore(round(coverageScore))
                .penaltyScore(round(penaltyScore))
                .threshold(threshold)
                .passed(passed)
                .verdict(verdict)
                .reason(reason)
                .provider("STRUCTURAL_RAG")
                .build();
    }

    public boolean canEvaluate(EvaluationRequest request) {

        if (request == null) {
            return false;
        }

        String question = safe(request.getQuestion()).toLowerCase();
        String context = safe(request.getContext()).toLowerCase();

        boolean tableQuestion =
                question.contains("what tables")
                        || question.contains("available in the data store")
                        || question.contains("tables are available")
                        || question.contains("list tables");

        boolean hasExpectedTables =
                context.contains("expected tables:")
                        || context.contains("| table |")
                        || context.contains("table | description");

        return tableQuestion && hasExpectedTables;
    }

    private EvaluationResult unsupported(EvaluationRequest request) {

        return EvaluationResult.builder()
                .metric(request == null ? EvaluationMetric.ANSWER_RELEVANCY : request.getMetric())
                .question(request == null ? null : request.getQuestion())
                .score(0.0)
                .coverageScore(0.0)
                .penaltyScore(0.0)
                .threshold(EvaluationConfig.getThreshold())
                .passed(false)
                .verdict("UNSUPPORTED")
                .reason("Structural evaluator could not identify a deterministic table-list validation pattern.")
                .provider("STRUCTURAL_RAG")
                .build();
    }

    private double calculatePenaltyScore(List<String> hallucinatedTables, List<String> expectedTables) {

        if (hallucinatedTables == null || hallucinatedTables.isEmpty()) {
            return 0.0;
        }

        int expectedSize = expectedTables == null || expectedTables.isEmpty()
                ? 1
                : expectedTables.size();

        return Math.min(
                0.50,
                (double) hallucinatedTables.size() / expectedSize
        );
    }

    private boolean isHallucinationAcceptable(List<String> hallucinatedTables) {

        if (!EvaluationConfig.isStrictHallucinationCheckEnabled()) {
            return true;
        }

        return hallucinatedTables == null || hallucinatedTables.isEmpty();
    }

    private String determineVerdict(boolean passed, double coverageScore, double penaltyScore) {

        if (passed) {
            return "STRONG_MATCH";
        }

        if (coverageScore >= 0.80 && penaltyScore <= EvaluationConfig.getHallucinationPenaltyThreshold()) {
            return "LIKELY_MATCH";
        }

        return "MISMATCH";
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}