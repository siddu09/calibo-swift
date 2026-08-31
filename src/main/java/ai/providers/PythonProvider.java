package ai.providers;

import ai.config.EvaluationConfig;
import ai.execution.PythonExecutor;
import ai.models.EvaluationMetric;
import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;
import com.google.gson.Gson;
import utils.LoggerUtil;
import ai.constants.AIConstants;

public class PythonProvider
        implements EvaluationProvider {

    private static final String
            PYTHON_SCRIPT =
            "src/main/resources/python/evaluate.py";

    @Override
    public EvaluationResult evaluate(
            EvaluationRequest request) {

        try {

            LoggerUtil.LOGGER.info(
                    "{} Starting evaluation",
                    AIConstants.AI_LOG_PREFIX);

            LoggerUtil.LOGGER.info(
                    "{} Metric={}",
                    AIConstants.AI_LOG_PREFIX,
                    request.getMetric());

            LoggerUtil.LOGGER.info(
                    "{} Provider=PYTHON",
                    AIConstants.AI_LOG_PREFIX);

            LoggerUtil.LOGGER.info(
                    "{} Executing Python evaluator",
                    AIConstants.AI_LOG_PREFIX);

            LoggerUtil.LOGGER.debug(
                    "{} Question={}",
                    AIConstants.AI_LOG_PREFIX,
                    request.getQuestion());

            EvaluationMetric metric =
                    request.getMetric() == null
                            ? EvaluationMetric.ANSWER_RELEVANCY
                            : request.getMetric();

            String json =
                    PythonExecutor.executePythonScript(
                            PYTHON_SCRIPT,
                            request.getQuestion(),
                            request.getAnswer(),
                            request.getContext(),
                            metric.name());

            LoggerUtil.LOGGER.debug(
                    "{} Raw Response={}",
                    AIConstants.AI_LOG_PREFIX,
                    json);

            Gson gson =
                    new Gson();

            LoggerUtil.LOGGER.info(
                    "{} JSON Returned={}",
                    AIConstants.AI_LOG_PREFIX,
                    json);

            if (json == null || json.isBlank()) {

                throw new RuntimeException(
                        "Python returned empty response");
            }

            EvaluationResult result =
                    gson.fromJson(
                            json,
                            EvaluationResult.class);

            if (result == null) {

                throw new RuntimeException(
                        "Unable to deserialize python response : "
                                + json);
            }


            result.setMetric(metric);

            result.setQuestion(
                    request.getQuestion());


//            result.setPassed(
//                    result.getScore() >=
//                            EvaluationConfig.getThreshold());

            result.setProvider("PYTHON");

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

            LoggerUtil.LOGGER.info(
                    "{} Metric={} | Score={} | Verdict={} | Provider=PYTHON",
                    AIConstants.AI_LOG_PREFIX,
                    result.getMetric(),
                    result.getScore(),
                    result.getVerdict());

            return result;

        } catch (Exception e) {

            LoggerUtil.LOGGER.error(
                    "{} Provider=PYTHON failed",
                    AIConstants.AI_LOG_PREFIX,
                    e);

            throw new RuntimeException(
                    "Python evaluation failed",
                    e);
        }
    }
}