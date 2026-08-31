package ai.services;

import ai.models.DatasetSummary;
import ai.models.EvaluationResult;

import java.util.List;

public class EvaluationSummaryService {

    public DatasetSummary generateSummary(
            List<EvaluationResult> results) {

        DatasetSummary summary = new DatasetSummary();


        int totalRecords = results.size();

        int passedRecords = (int) results.stream()
                .filter(EvaluationResult::isPassed)
                .count();

        int failedRecords = totalRecords - passedRecords;

        double averageScore = results.stream()
                .mapToDouble(EvaluationResult::getScore)
                .average()
                .orElse(0.0);

        double highestScore = results.stream()
                .mapToDouble(EvaluationResult::getScore)
                .max()
                .orElse(0.0);

        double lowestScore = results.stream()
                .mapToDouble(EvaluationResult::getScore)
                .min()
                .orElse(0.0);

        double passRate = totalRecords == 0
                ? 0
                : ((double) passedRecords / totalRecords) * 100;

        long strongMatches =
                results.stream()
                        .filter(r ->
                                "STRONG_MATCH".equals(
                                        r.getVerdict()))
                        .count();

        long likelyMatches =
                results.stream()
                        .filter(r ->
                                "LIKELY_MATCH".equals(
                                        r.getVerdict()))
                        .count();

        long mismatches =
                results.stream()
                        .filter(r ->
                                "MISMATCH".equals(
                                        r.getVerdict()))
                        .count();

        summary.setTotalRecords(totalRecords);
        summary.setPassedRecords(passedRecords);
        summary.setFailedRecords(failedRecords);

        summary.setAverageScore(averageScore);
        summary.setHighestScore(highestScore);
        summary.setLowestScore(lowestScore);

        summary.setPassRate(passRate);


        summary.setStrongMatches(
                strongMatches);

        summary.setLikelyMatches(
                likelyMatches);

        summary.setMismatches(
                mismatches);

        return summary;
    }
}