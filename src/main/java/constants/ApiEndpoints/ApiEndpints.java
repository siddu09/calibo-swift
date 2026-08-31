package constants.ApiEndpoints;


/** Central location for API endpoint paths used by the framework. */
public final class ApiEndpints {
    private ApiEndpints() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PORTFOLIOS = "/elab/portfolios";
    public static final String PRODUCT = "/elab/v2/projects";
    public static final String PROJECT_BY_ID_V2 = "/elab/v2/projects/{projectId}";
    public static final String FEATURE = "/elab/v2/projects/workstreams";
    public static final String PROJECT_WORKSTREAMS =
            "/elab/projects/workstreams/getAllWorkstreams/{projectId}";
    //delete project
    public static final String DELETE_PROJECT = "/elab/v2/projects/{projectId}";
}

