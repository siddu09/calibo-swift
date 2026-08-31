package ai.providers;

import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;

public class LLMJudgeProvider
        implements EvaluationProvider {

    @Override
    public EvaluationResult evaluate(
            EvaluationRequest request) {

        throw new UnsupportedOperationException(
                "LLM Judge implementation not available yet");
    }
}