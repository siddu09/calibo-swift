package constants.ApiEndpoints;

public final class DpsApiEndpoints {

    private DpsApiEndpoints() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DPS_DATA_INTEGRATION_SETTINGS =
            "/configuration/settings/client?configCode=DATA_INTEGRATION";
    public static final String DPS_SECURITY_ASSESSMENT_SETTINGS =
            "/configuration/settings/client?configCode=SECURITY_ASSESSMENT";
    public static final String DPS_SETTINGS_VERSION = "/configuration/settings/version";
    public static final String DPS_DATA_STORES =
            "/configuration/v2/settings/dataStores/all/permission";

    public static final String DPS_PRODUCT = "/elab/v2/projects";
    public static final String DPS_FEATURE = "/elab/v2/projects/workstreams";

    public static final String DPS_STAGE = "/devops/stage/{stageName}";
    public static final String DPS_PIPELINE_DETAILS =
            "/datapipeline/project/dataflow/pipeline/details";
    public static final String DPS_DRAFT = "/datapipeline/project/dataflow/v2/draft";
    public static final String DPS_DRAFT_STATUS =
            "/datapipeline/project/dataflow/v2/draft/status";
    public static final String DPS_DRAFT_PUBLISH =
            "/datapipeline/project/dataflow/v2/draft/publish";
    public static final String DPS_DRAFT_PUBLISH_V3 =
            "/datapipeline/project/dataflow/v3/draft/publish";
    public static final String DPS_ALL_STAGE_DETAILS =
            "/datapipeline/project/dataflow/allStages/detail";
    public static final String DPS_WORKFLOW_INITIATE = "/datapipeline/workflow/v2/initiate";
    public static final String DPS_WORKFLOW_STATUS =
            "/datapipeline/project/dataflow/v3/status";

    public static final String DPS_CRAWLERS = "/datapipeline/crawlers";
    public static final String DPS_CRAWLER = "/datapipeline/crawlers/{crawlerId}";
    public static final String DPS_CRAWLER_RUN = "/datapipeline/crawlers/run";
    public static final String DPS_CRAWLER_DETAILS =
            "/datapipeline/crawlers/{crawlerId}/details";
    public static final String DPS_CATALOGS = "/datapipeline/catalogs";
    public static final String DPS_CATALOG = "/datapipeline/catalogs/{catalogId}";

    public static final String DPS_DATABRICKS_TEMPLATES = "/databricks/v1/template";
    public static final String DPS_CLIENT_SETTINGS = "/configuration/settings/client";
    public static final String DPS_DATABRICKS_TEMPLATE =
            "/databricks/v2/template/{templateId}";
    public static final String DPS_DATABRICKS_CLUSTER_WHL_MAPPING =
            "/databricks/config/clusterWhlMapping";
    public static final String DPS_DATABRICKS_JOB = "/databricks/v1/jobs/{jobId}";
    public static final String DPS_SNOWFLAKE_TABLE_ROWS = "/snowflake/v2/table/rows";

    public static final String DPS_PROJECT_REPOSITORIES = "/elab/projects/repositories";
    public static final String DPS_PROJECT_REPOSITORY_LIST =
            "/elab/projects/{projectId}/repositories";
    public static final String DPS_DELETE_PROJECT_REPOSITORIES =
            "/elab/projects/repositories/{projectId}/deleteRepos";
    public static final String DPS_DELETE_WORKSTREAM =
            "/elab/projects/workstreams/{workstreamId}/{projectId}";

}
