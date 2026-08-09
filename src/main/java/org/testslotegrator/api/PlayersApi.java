package org.testslotegrator.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testslotegrator.model.PlayerRequestDTO;
import org.testslotegrator.model.PlayerRequestOneDTO;

import static io.restassured.RestAssured.given;
import static org.testslotegrator.api.specifications.RequestSpecifications.getAuthenticatedSpec;
import static org.testslotegrator.config.ApiConfig.*;

public class PlayersApi {

    @Step("Выполнение запроса на создание игрока {1}")
    public static Response postPlayer(String token, PlayerRequestDTO playerRequestDTO) {
        return given()
                    .spec(getAuthenticatedSpec(token))
                    .body(playerRequestDTO)
                .when()
                    .post(PLAYERS_CREATE_ENDPOINT)
                .then()
                    .extract()
                    .response();

    }

    @Step("Выполнение запроса на удаление игрока {1}")
    public static Response deleteOnePlayer(String token, Integer playerId) {
        return given()
                    .spec(getAuthenticatedSpec(token))
                    .pathParam("playerId", playerId)
                .when()
                    .delete(PLAYERS_DELETE_ENDPOINT)
                .then()
                    .extract()
                    .response();

    }

    @Step("Выполнение запроса по получение всех игроков")
    public static Response getAllPlayers(String token) {
        return given()
                    .spec(getAuthenticatedSpec(token))
                .when()
                    .get(PLAYERS_GET_ALL_ENDPOINT)
                .then()
                    .extract()
                    .response();
    }

    @Step("Выполнение запроса на получение информации об одном игроке {1}")
    public static Response getOnePlayer(String token, PlayerRequestOneDTO playerRequestOneDTO) {
        return given()
                    .spec(getAuthenticatedSpec(token))
                    .body(playerRequestOneDTO)
                .when()
                    .get(PLAYERS_GET_ONE_ENDPOINT)
                .then()
                    .extract()
                    .response();

    }
}
