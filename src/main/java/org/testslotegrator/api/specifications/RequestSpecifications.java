package org.testslotegrator.api.specifications;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.testslotegrator.config.ApiConfig.BASE_URL;

public class RequestSpecifications {

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .addFilters(
                    List.of(
                        new AllureRestAssured(),
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter()
                    )
                )
                .setBaseUri(BASE_URL)
                .setContentType(JSON)
                .setAccept(JSON)
                .build();
    }

    public static RequestSpecification getAuthenticatedSpec(String token) {
        return getBaseSpec()
                .auth()
                .oauth2(token);
    }
}
