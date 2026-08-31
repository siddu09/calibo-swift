package ai.providers;

import ai.models.EvaluationEngine;

public final class ProviderFactory {

    private ProviderFactory() {
    }

    public static EvaluationProvider getProvider(EvaluationEngine engine) {
        return switch (engine) {
            case MOCK -> new MockProvider();
            case PYTHON -> new PythonProvider();
            case DEEPEVAL -> new DeepEvalProvider();
            case LLM_JUDGE -> new LLMJudgeProvider();
            case HYBRID -> new HybridEvaluationProvider();
        };
    }
}