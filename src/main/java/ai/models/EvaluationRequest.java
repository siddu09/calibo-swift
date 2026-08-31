package ai.models;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class EvaluationRequest {

    private String question;

    private String answer;

    private String context;

    private String groundTruth;

    private EvaluationMetric metric;
}