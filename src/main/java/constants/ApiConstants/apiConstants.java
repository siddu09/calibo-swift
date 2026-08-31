package constants.ApiConstants;


public final class apiConstants {

    private apiConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String TENANT_ID = "x-tenantid";
    public static final String APPLICATION_JSON = "application/json";
    public static final String BEARER = "Bearer ";
    public static final String CONFIG_FILE = "config/envConfig/api/qa.properties";


}