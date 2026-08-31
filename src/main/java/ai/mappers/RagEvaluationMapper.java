package ai.mappers;

import ai.models.EvaluationMetric;
import ai.models.EvaluationRequest;
import ai.models.RagEvaluationRecord;

/**
 * Maps a RagEvaluationRecord (from Excel) to an EvaluationRequest
 */
public class RagEvaluationMapper {

    public EvaluationRequest map(RagEvaluationRecord record) {

        if (record == null) return null;

        return EvaluationRequest.builder()
                .question(record.getQuestion())
                .answer(record.getAnswer())
                // Use the Excel 'context' column as both retrieval context and ground truth
                .context(record.getContext())
                .groundTruth(record.getContext())
                .metric(EvaluationMetric.ANSWER_RELEVANCY)
                .build();
    }
}
