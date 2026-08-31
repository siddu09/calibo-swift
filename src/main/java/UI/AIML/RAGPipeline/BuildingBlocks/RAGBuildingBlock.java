package UI.AIML.RAGPipeline.BuildingBlocks;

import ai.models.EvaluationRequest;
import assertionsHandler.AIAssert;
import assertionsHandler.AssertionManager;
import io.qameta.allure.Step;
import org.testng.Assert;
import utils.LoggerUtil;

/**
 * Building block responsible for RAG Automation VALIDATION:
 * - LLM answer presence/quality
 * - AI-evaluator (relevancy) assessment of the LLM answer
 * - Presence of core pipeline components captured upstream
 *
 * This class owns only the validation capability. It does not navigate,
 * touch the pipeline canvas, or talk to Conversa - those concerns live in
 * their own Building Blocks. This class receives already-captured state
 * (booleans, answer text) and validates it.
 *
 * Lives in main source - must NEVER depend on test-source classes
 * (e.g. RagTestData). Ground-truth/question data must be passed in by the
 * caller (workflow/test).
 */
public class RAGBuildingBlock {

    private boolean aiEvaluationRan;

    @Step("Validate LLM answer is present")
    public void validateAnswerExists(String answer) {

        boolean answerPresent =
                answer != null && !answer.isBlank();

        AssertionManager.softAssertTrue(
                answerPresent,
                "LLM answer should not be null or empty");

        LoggerUtil.LOGGER.info(
                "[RAG-VALIDATION-BLOCK] Answer present: {}",
                answerPresent);
    }

    @Step("Validate LLM answer with AI evaluator")
    public void validateAnswerWithAI(
            String question,
            String answer,
            String groundTruthContext) {

        if (answer == null || answer.isBlank()) {

            AssertionManager.softAssertTrue(
                    false,
                    "LLM answer was empty - cannot evaluate");

            return;
        }

        EvaluationRequest evalRequest =
                EvaluationRequest.builder()
                        .question(question)
                        .answer(answer)
                        .context(groundTruthContext)
                        .groundTruth(groundTruthContext)
                        .build();
        //AI evaluation
        AIAssert.assertRelevant(evalRequest);

        aiEvaluationRan = true;

        LoggerUtil.LOGGER.info(
                "[RAG-VALIDATION-BLOCK] ✓ AI relevancy evaluation submitted");
    }

    @Step("Assert pipeline components present")
    public void assertPipelineComponentsPresent(
            boolean ragBuilderPresent,
            boolean dataLakePresent,
            boolean testEndpointPresent) {

        Assert.assertTrue(
                ragBuilderPresent,
                "RAG Builder node should be present in the pipeline");

        Assert.assertTrue(
                dataLakePresent,
                "Data Lake node should be present in the pipeline");

        Assert.assertTrue(
                testEndpointPresent,
                "Test RAG Endpoint section should be present in the panel");

        Assert.assertTrue(
                aiEvaluationRan,
                "AI evaluation should have run on the LLM answer");
    }

    public boolean didAiEvaluationRun() {
        return aiEvaluationRan;
    }
}