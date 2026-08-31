package ai.providers;

import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;

public interface EvaluationProvider {

    EvaluationResult evaluate(
            EvaluationRequest request);
}
