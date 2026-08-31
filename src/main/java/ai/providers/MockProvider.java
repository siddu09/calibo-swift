package ai.providers;

import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;

public class MockProvider
        implements EvaluationProvider {

    @Override
    public EvaluationResult evaluate(
            EvaluationRequest request) {

        return EvaluationResult.builder()
                .metric(request.getMetric())
                .score(0.95)
                .passed(true)
                .reason("Mock evaluation passed")
                .build();
    }
}