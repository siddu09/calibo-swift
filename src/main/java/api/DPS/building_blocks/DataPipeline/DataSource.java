package api.DPS.building_blocks.DataPipeline;

import io.qameta.allure.Step;

import api.DPS.helpers.DataSourceHelper.DataSourceHelper;

public final class DataSource {
    private final DataSourceHelper helper;

    public DataSource(DataSourceHelper helper) { this.helper = helper; }
    @Step("Add data source stage")
    public void addDataSourceStageWithMsSqlNode() {
        helper.addDataSourceStageWithMsSqlNode();
    }
}
