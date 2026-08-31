package api.DPS.building_blocks.DataPipeline;

import io.qameta.allure.Step;


import api.DPS.helpers.DataIntegrationHelper.DataIntegrationHelper;

public final class DataIntegration {
    private final DataIntegrationHelper helper;

    public DataIntegration(DataIntegrationHelper helper) { this.helper = helper; }

    @Step("Add data integration stage")
    public void addDataIntegrationStageWithDatabricksNode() {
        helper.addDataIntegrationStageWithDatabricksNode();
    }

    public void configureDatabricksJob() { helper.configureDatabricksJob(); }
}
