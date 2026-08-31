package common.apiCommon;

import config.Api.apiConfig.Config;
import constants.ApiConstants.apiConstants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecProvider {

    private static RequestSpecification requestSpecification;

    private RequestSpecProvider() {
    }
    public static synchronized void initialize() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(Config.baseUrl)
                .addHeader(apiConstants.AUTHORIZATION, apiConstants.BEARER + Config.accessToken)
                .addHeader(apiConstants.TENANT_ID, Config.tenantId)
                .setContentType(ContentType.JSON);
        requestSpecification = builder.build();
    }

    public static RequestSpecification get() {
        if (requestSpecification == null) {
            throw new IllegalStateException("Request specification has not been initialized");
        }
        return new RequestSpecBuilder().addRequestSpecification(requestSpecification).build();
    }

}