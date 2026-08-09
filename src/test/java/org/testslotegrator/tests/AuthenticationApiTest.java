package org.testslotegrator.tests;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testslotegrator.model.TokenDTO;

import static org.testslotegrator.api.AuthenticationApi.postLogin;

public class AuthenticationApiTest {

    @Test
    @DisplayName("Проверка получения токена пользователя")
    public void testGetAuthenticationToken() {
        var response = postLogin();
        var softAsserts = new SoftAssertions();

        softAsserts.assertThat(response.statusCode())
                .as("Код ответа должен быть 200")
                .isEqualTo(HttpStatus.SC_OK);

        softAsserts.assertThatCode(() -> response.body().as(TokenDTO.class))
                .as("Ответ должен соответствовать схеме")
                .doesNotThrowAnyException();

        var accessToken = response.body().jsonPath().getString("accessToken"); // так как не соответствует схеме, но в тесте проверяем что данное значение есть
        softAsserts.assertThat(accessToken)
                .as("Токен доступа не должен быть null или пустым")
                .isNotNull()
                .isNotEmpty();
        softAsserts.assertAll();
    }
}

