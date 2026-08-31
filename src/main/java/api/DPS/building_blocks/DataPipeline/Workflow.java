package api.DPS.building_blocks.DataPipeline;

import io.qameta.allure.Step;

import api.DPS.helpers.DpsContext;
import api.DPS.helpers.WorkflowHelper.WorkflowHelper;

public final class Workflow {
    private final DpsContext context = new DpsContext();
    private final WorkflowHelper helper = new WorkflowHelper(context);

    public DpsContext setup() { helper.setup(); return context; }
    @Step("Run the pipeline")
    public void runAndVerify() { helper.runAndVerify(); }
}
