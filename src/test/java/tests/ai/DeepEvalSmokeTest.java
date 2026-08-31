package tests.ai;

import ai.execution.PythonExecutor;
import org.testng.annotations.Test;

public class DeepEvalSmokeTest {

    @Test
    public void validateDeepEvalRunner()
            throws Exception {

        String response =
                PythonExecutor.executePythonScript(
                        "src/main/resources/python/deepeval_runner.py",
                        "What is harvesting?",
                        "Harvesting is collecting crops",
                        "Harvesting is the process of collecting mature crops",
                        "ANSWER_RELEVANCY");

        System.out.println(response);
    }
}