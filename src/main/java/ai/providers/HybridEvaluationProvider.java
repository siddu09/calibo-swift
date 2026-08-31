package ai.providers;

import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;
import utils.LoggerUtil;

public class HybridEvaluationProvider implements EvaluationProvider {

    private final StructuralRagProvider structuralRagProvider;
    private final PythonProvider pythonProvider;

    public HybridEvaluationProvider() {
        this.structuralRagProvider = new StructuralRagProvider();
        this.pythonProvider = new PythonProvider();
    }

    @Override
    public EvaluationResult evaluate(EvaluationRequest request) {
        if (structuralRagProvider.canEvaluate(request)) {
            LoggerUtil.LOGGER.info("[AI-EVAL] HYBRID selected STRUCTURAL_RAG provider");
            return structuralRagProvider.evaluate(request);
        }

        LoggerUtil.LOGGER.info("[AI-EVAL] HYBRID falling back to PYTHON provider");
        return pythonProvider.evaluate(request);
    }
}