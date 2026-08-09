package org.testslotegrator.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testslotegrator.config.ApiConfig;
import org.testslotegrator.model.CredentialsDTO;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.testslotegrator.api.specifications.RequestSpecifications.getBaseSpec;
import static org.testslotegrator.config.ApiConfig.*;

public class AuthenticationApi {

    @Step("Выполнение запроса на получение токена")
    public static Response postLogin() {
        if (isBlank(BASIC_AUTH_USERNAME)
                || isBlank(BASIC_AUTH_PASSWORD)
                || isBlank(AUTH_EMAIL)
                || isBlank(AUTH_PASSWORD)) {
            throw new IllegalStateException("Учётные данные не заданы. Укажите их переменными окружения");
        }
        return postLogin(new CredentialsDTO(AUTH_EMAIL, AUTH_PASSWORD));
    }

    @Step("Выполнение запроса на получение токена с помощью {0}")
    public static Response postLogin(CredentialsDTO credentials) {
        return given()
                    .spec(getBaseSpec())
                    .auth()
                    .basic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD)
                    .body(credentials)
                .when()
                    .post(ApiConfig.LOGIN_ENDPOINT)
                .then()
                    .extract()
                    .response();
    }
}
