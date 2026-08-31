package api.DPS.building_blocks.DataPipeline;

import io.qameta.allure.Step;

import api.DPS.helpers.DataLakeHelper.DataLakeHelper;

public final class DataLake {
    private final DataLakeHelper helper;

    public DataLake(DataLakeHelper helper) { this.helper = helper; }

    @Step("Add data lake stage")
    public void addDataLakeStageWithSnowflakeNode() {
        helper.addDataLakeStageWithSnowflakeNode();
    }
}
