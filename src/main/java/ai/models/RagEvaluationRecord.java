package ai.models;

import lombok.Builder;
import lombok.Data;
    @Data
    @Builder
    public class RagEvaluationRecord {

        private String question;

        private String answer;

        private String context;
    }

