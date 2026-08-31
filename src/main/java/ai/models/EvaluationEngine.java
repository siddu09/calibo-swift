package ai.models;

public enum EvaluationEngine {
    MOCK,
    PYTHON,
    DEEPEVAL,
    LLM_JUDGE,

    /**
     * Cost-effective evaluator.
     *
     * First tries deterministic structural validation.
     * Falls back to Python evaluator only when deterministic evaluation
     * cannot confidently handle the question/context.
     */
    HYBRID
}