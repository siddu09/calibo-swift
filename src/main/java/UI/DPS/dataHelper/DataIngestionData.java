package UI.DPS.dataHelper;

import lombok.Data;

@Data
public class DataIngestionData {
    private String dataSourceStageName;
    private String dataSourceNodeName;
    private String dataIntegrationStageName;
    private String dataIntegrationNodeName;
    private String databricksInstanceName;
    private String dataLakeStageName;
    private String dataLakeNodeName;
    private String catalogSchema;
    private String snowflakeDatastoreName;
    private String sourceTableName;
    private String targetTableName;
}

