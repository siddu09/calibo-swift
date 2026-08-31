package ai.services;

import ai.config.EvaluationConfig;
import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;
import ai.providers.EvaluationProvider;
import ai.providers.ProviderFactory;
import utils.LoggerUtil;

public class EvaluationService {

    private final EvaluationProvider provider;

    public EvaluationService() {

        LoggerUtil.LOGGER.info(
                "[AI-EVAL] Selected Engine={}",
                EvaluationConfig.getEngine());
        provider =
                ProviderFactory.getProvider(
                        EvaluationConfig.getEngine());
    }

    public EvaluationResult evaluate(
            EvaluationRequest request) {

        return provider.evaluate(request);
    }
}