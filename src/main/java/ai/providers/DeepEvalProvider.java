package ai.providers;

import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;
import ai.models.EvaluationMetric;
import ai.execution.PythonExecutor;
import com.google.gson.Gson;
import utils.LoggerUtil;
import ai.constants.AIConstants;

import java.util.Map;

public class DeepEvalProvider implements EvaluationProvider {

    private static final String PYTHON_SCRIPT =
            "src/main/resources/python/deepeval_runner.py";

    @Override
    public EvaluationResult evaluate(EvaluationRequest request) {

        try {

            LoggerUtil.LOGGER.info(
                    "{} Starting DeepEval evaluation",
                    AIConstants.AI_LOG_PREFIX);

            LoggerUtil.LOGGER.info(
                    "{} Metric={}",
                    AIConstants.AI_LOG_PREFIX,
                    request.getMetric());

            String json = PythonExecutor.executePythonScript(
                    PYTHON_SCRIPT,
                    request.getQuestion(),
                    request.getAnswer(),
                    request.getContext(),
                    request.getMetric() == null ? "ANSWER_RELEVANCY" : request.getMetric().name());

            LoggerUtil.LOGGER.debug(
                    "{} Raw Response={}",
                    AIConstants.AI_LOG_PREFIX,
                    json);

            if (json == null || json.isBlank()) {
                throw new RuntimeException(
                        "DeepEval python returned empty response");
            }

            Gson gson = new Gson();

            Map map = gson.fromJson(json, Map.class);

            EvaluationResult result = new EvaluationResult();
            result.setMetric(request.getMetric() == null
                    ? EvaluationMetric.ANSWER_RELEVANCY
                    : request.getMetric());

            if (map.get("score") != null) result.setScore(((Number) map.get("score")).doubleValue());
            if (map.get("passed") != null) result.setPassed((Boolean) map.get("passed"));
            if (map.get("reason") != null) result.setReason((String) map.get("reason"));
            if (map.get("verdict") != null) result.setVerdict((String) map.get("verdict"));
            if (map.get("coverageScore") != null) result.setCoverageScore(((Number) map.get("coverageScore")).doubleValue());
            if (map.get("penaltyScore") != null) result.setPenaltyScore(((Number) map.get("penaltyScore")).doubleValue());
            if (map.get("threshold") != null) result.setThreshold(((Number) map.get("threshold")).doubleValue());

            result.setProvider("DEEPEVAL");

            LoggerUtil.LOGGER.info(
                    "{} Score={}",
                    AIConstants.AI_LOG_PREFIX,
                    result.getScore());

            LoggerUtil.LOGGER.info(
                    "{} Passed={}",
                    AIConstants.AI_LOG_PREFIX,
                    result.isPassed());

            LoggerUtil.LOGGER.info(
                    "{} Verdict={}",
                    AIConstants.AI_LOG_PREFIX,
                    result.getVerdict());

            LoggerUtil.LOGGER.info(
                    "{} Reason={}",
                    AIConstants.AI_LOG_PREFIX,
                    result.getReason());

            LoggerUtil.LOGGER.info(
                    "{} Evaluation completed",
                    AIConstants.AI_LOG_PREFIX);

            return result;

        } catch (Exception e) {

            LoggerUtil.LOGGER.error(
                    "{} DeepEval provider failed",
                    AIConstants.AI_LOG_PREFIX,
                    e);

            throw new RuntimeException(
                    "DeepEval evaluation failed",
                    e);
        }
    }
}