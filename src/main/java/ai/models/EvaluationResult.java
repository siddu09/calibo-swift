package ai.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EvaluationResult {

    private EvaluationMetric metric;

    private double score;

    private String question;

    private boolean passed;

    private String reason;

    private String provider;

    private double coverageScore;
    private double penaltyScore;
    private double threshold;

    private String verdict;

}