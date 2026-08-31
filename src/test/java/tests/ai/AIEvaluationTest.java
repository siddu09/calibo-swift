package tests.ai;

import ai.models.EvaluationRequest;
import assertionsHandler.AIAssert;
import assertionsHandler.AssertionManager;
import org.testng.annotations.Test;

public class AIEvaluationTest {

    @Test
    public void validateResponse() {

        EvaluationRequest request =
                EvaluationRequest.builder()
                        .question(
                                "What is harvesting?")
                        .answer(
                                "Harvesting is crop collection.")
                        .context(
                                "Harvesting is the process of collecting mature crops.")
                        .build();

        AIAssert.assertRelevant(
                request);

        AIAssert.assertFaithful(
                request);

        AssertionManager.assertAll();
    }
}