package utils;

import java.util.List;

public class RagJobData {
    private String jobName;
    private int rerunAttempts = 0;
    private String faultTolerance = "DEFAULT";
    private List<String> tablesToAdd;
    private String semanticViewName;
    private String cortexAgentName;
    private String inferenceModel;
    private String orchestrationInstruction;
    private String responseFormat;

    public RagJobData() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final RagJobData data = new RagJobData();
        public Builder jobName(String n) { data.jobName = n; return this; }
        public Builder rerunAttempts(int r) { data.rerunAttempts = r; return this; }
        public Builder faultTolerance(String f) { data.faultTolerance = f; return this; }
        public Builder tablesToAdd(List<String> t) { data.tablesToAdd = t; return this; }
        public Builder semanticViewName(String s) { data.semanticViewName = s; return this; }
        public Builder cortexAgentName(String c) { data.cortexAgentName = c; return this; }
        public Builder inferenceModel(String m) { data.inferenceModel = m; return this; }
        public Builder orchestrationInstruction(String o) { data.orchestrationInstruction = o; return this; }
        public Builder responseFormat(String r) { data.responseFormat = r; return this; }
        public RagJobData build() { return data; }
    }

    public String getJobName() { return jobName; }
    public int getRerunAttempts() { return rerunAttempts; }
    public String getFaultTolerance() { return faultTolerance; }
    public java.util.List<String> getTablesToAdd() { return tablesToAdd; }
    public String getSemanticViewName() { return semanticViewName; }
    public String getCortexAgentName() { return cortexAgentName; }
    public String getInferenceModel() { return inferenceModel; }
    public String getOrchestrationInstruction() { return orchestrationInstruction; }
    public String getResponseFormat() { return responseFormat; }
}
