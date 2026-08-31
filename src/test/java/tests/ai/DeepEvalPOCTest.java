package tests.ai;

import ai.execution.PythonExecutor;
import ai.models.EvaluationResult;
import com.google.gson.Gson;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeepEvalPOCTest {

    private static final String PYTHON_SCRIPT =
            "src/main/resources/python/evaluate.py";

    @Test
    public void validatePythonIntegration()
            throws Exception {

        String json =
                PythonExecutor.executePythonScript(
                        PYTHON_SCRIPT);

        Gson gson = new Gson();

        EvaluationResult result =
                gson.fromJson(
                        json,
                        EvaluationResult.class);

        System.out.println(
                "Metric : "
                        + result.getMetric());

        System.out.println(
                "Score : "
                        + result.getScore());

        Assert.assertTrue(
                result.isPassed());
    }
}