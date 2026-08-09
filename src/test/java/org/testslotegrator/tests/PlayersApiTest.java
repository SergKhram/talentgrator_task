package org.testslotegrator.tests;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testslotegrator.model.PlayerRequestOneDTO;
import org.testslotegrator.model.PlayerResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testslotegrator.api.AuthenticationApi.postLogin;
import static org.testslotegrator.api.PlayersApi.*;
import static org.testslotegrator.utils.Generators.buildPlayerRequestDTO;

public class PlayersApiTest {

    private static String getAccessToken() {
        return postLogin().jsonPath().getString("accessToken");  //потому что не соответствует схеме, чтобы отработал дальше тест
    }

    @Test
    @DisplayName("Проверка регистрации 12 игроков")
    public void testRegister12Players() {
        var softAsserts = new SoftAssertions();

        IntStream.range(0, 12).forEach(i -> {
            var playerRequest = buildPlayerRequestDTO();
            var playerResponse = postPlayer(getAccessToken(), playerRequest);
            var playerResponseBody = playerResponse.getBody();

            softAsserts.assertThat(playerResponse.statusCode())
                    .as("Код ответа для игрока %d должен быть 201".formatted(i))
                    .isEqualTo(HttpStatus.SC_CREATED);

            softAsserts.assertThatCode(() ->
                            playerResponseBody.as(PlayerResponseDTO.class)
                    )
                    .as("Ответ на создание игрока %d должен соответствовать схеме".formatted(i))
                    .doesNotThrowAnyException();

            softAsserts.assertThat(playerResponseBody.jsonPath())
                    .satisfies(b -> {
                        assertThat(b.getString("name"))
                                .as("Имя игрока %d должно соответствовать ожидаемому".formatted(i))
                                .isEqualTo(playerRequest.getName());
                        assertThat(b.getString("email"))
                                .as("Email игрока %d должен соответствовать ожидаемому".formatted(i))
                                .isEqualTo(playerRequest.getEmail());
                        assertThat(b.getString("username"))
                                .as("Username игрока %d должен соответствовать ожидаемому".formatted(i))
                                .isEqualTo(playerRequest.getUsername());
                        assertThat(b.getString("surname"))
                                .as("Фамилия игрока %d должна соответствовать ожидаемой".formatted(i))
                                .isEqualTo(playerRequest.getSurname());
                    });
        });
        softAsserts.assertAll();
    }

    @Test
    @DisplayName("Проверка получения данных одного игрока")
    // упадет, потому что баг и он не возвращает по email игрока, хотя в getAll он будет отображаться
    public void testGetSinglePlayer() {
        var softAsserts = new SoftAssertions();
        var playerRequest = buildPlayerRequestDTO();
        var playerResponse = postPlayer(getAccessToken(), playerRequest);

        var playerByEmailResponse = getOnePlayer(getAccessToken(), new PlayerRequestOneDTO(playerRequest.getEmail()));
        var playerByEmailResponseBody = playerByEmailResponse.getBody();

        softAsserts.assertThat(playerByEmailResponse.statusCode())
                .as("Код ответа должен быть 201")
                .isEqualTo(HttpStatus.SC_CREATED);

        softAsserts.assertThatCode(() ->
                        playerByEmailResponseBody.as(PlayerResponseDTO.class)
                )
                .as("Ответ на получение игрока должен соответствовать схеме")
                .doesNotThrowAnyException();

        softAsserts.assertThat(playerByEmailResponseBody.jsonPath())
                .satisfies(b -> {
                    assertThat(b.getInt("id"))
                            .as("ID игрока должно соответствовать ожидаемому")
                            .isEqualTo(playerResponse.jsonPath().getInt("id")); // упадет, потому что по факту тут String-и, а еще в ответе _id, что не соответствует схеме
                    assertThat(b.getString("name"))
                            .as("Имя игрока должно соответствовать ожидаемому")
                            .isEqualTo(playerRequest.getName());
                    assertThat(b.getString("email"))
                            .as("Email игрока должен соответствовать ожидаемому")
                            .isEqualTo(playerRequest.getEmail());
                    assertThat(b.getString("username"))
                            .as("Username игрока должен соответствовать ожидаемому")
                            .isEqualTo(playerRequest.getUsername());
                    assertThat(b.getString("surname"))
                            .as("Фамилия игрока должна соответствовать ожидаемой")
                            .isEqualTo(playerRequest.getSurname());
                });
        softAsserts.assertAll();
    }

    @Test
    @DisplayName("Проверка получения всех игроков и сортировки по имени")
    // не понимаю смысла сортировки после получения данных по апи, возможно был бы смысл, что мы на вход можем передать параметр для сортировки ответа, но такого нет, поэтому просто в тесте в конце сделаю сортировку сам после ответа, раз в задании написано
    public void testGetAllPlayersSorted() {
        var softAsserts = new SoftAssertions();

        var allPlayersResponse = getAllPlayers(getAccessToken());
        var allPlayersResponseBody = allPlayersResponse.getBody();
        softAsserts.assertThat(allPlayersResponse.statusCode())
                .as("Код ответа должен быть 200")
                .isEqualTo(HttpStatus.SC_OK);

        softAsserts.assertThatCode(() -> {
                    allPlayersResponseBody.as(PlayerResponseDTO.class); // по схеме почему то ответ соответствует одному игроку, хотя даже по логике должен быть список
                })
                .as("Ответ должен соответствовать схеме")
                .doesNotThrowAnyException();

        // если бы мы были уверены, что ответ соответствует схеме как списку игроков, я бы через stream просто этот список отсортировал, но раз нет, то по jsonPath
        List<String> names = allPlayersResponseBody.jsonPath()
                .getList("sort { it.name }.name");
        softAsserts.assertThat(names)
                .as("Список имен игроков должен быть отсортирован по алфавиту")
                .isSortedAccordingTo(CASE_INSENSITIVE_ORDER); // не обращаем внимание на case
        softAsserts.assertAll();
    }

    @Test
    @DisplayName("Проверка удаления всех созданных игроков")
    public void testDeleteAllCreatedPlayers() {
        var softAsserts = new SoftAssertions();
        var createdPlayersIds = new ArrayList<Integer>();

        IntStream.range(0, 3).forEach(i -> {
            var requestBody = buildPlayerRequestDTO();
            var playerResponse = postPlayer(getAccessToken(), requestBody);
            createdPlayersIds.add(playerResponse.jsonPath().getInt("id")); // по факту тут возвращается _id и все равно упадет из-за типа
        });

        // отдельно, потому что тест должен проверять удаление созданных, и поэтому сначала насоздавали, а потом только удаляем все
        for (Integer playerId : createdPlayersIds) {
            var deleteResponse = deleteOnePlayer(getAccessToken(), playerId);
            softAsserts.assertThat(deleteResponse.statusCode())
                    .as("Код ответа при удалении игрока с id %d должен быть 200".formatted(playerId))
                    .isEqualTo(HttpStatus.SC_OK);
        }

        // тут возможно будет проблема когда будет слишком много записей и будет отдавать парционно, но тогда 100% у ментода должен быть параметр фильтра и через фильтр бы доставал нужные id для проверки
        var allPlayersResponse = getAllPlayers(getAccessToken());
        var ids = allPlayersResponse.getBody().jsonPath().getList("id");
        softAsserts.assertThatCollection(ids)
                        .as("Список не содержит удаленных id")
                        .doesNotContain(createdPlayersIds.toArray());

        softAsserts.assertAll();
    }

//  тест "Запросить список всех пользователей и убедиться что он пустой (/api/automationTask/getAll)" по сути делает тоже самое что выше
//  но если вдруг нужен тест, который правда удаляет всех игроков с целью потом проверить, что весь список пустой, то я не вижу смысла в нем, ведь это ломает вообще все тесты, где как то создаются/удаляются/редактируются
//  игроки, либо принудительно выполнять всегда послений, но тоже по мне плохая практика ради такой проверки
//  или если бы отображались и удялись только те игроки, которые созданы под определенным пользователем(специально только для этого теста создаваемый) и это могло никак не влиять на другие данные
//  если нужен прям тест со всеми этими шагами как в задании(хотя написано Автоматизировать REST API-тесты - в множественном), то он выглядел бы как ниже(но я бы не стал на каждом шаге проверять результаты, так как по-любому должны быть отдельные тесты как выше и в AuthentificationApiTest которые проверяют каждый кусочек отдельно
//    @Test
//    @DisplayName("Проверка создания и удаления всех созданных игроков")
//    public void testCreateAndDeleteAllCreatedPlayers() {
//        //Получить токен пользователя
//        var response = postLogin();
//        var newAccessToken = response.getBody().jsonPath().getString("access_token");
//        assertThat(newAccessToken)
//                .as("Access token присутствует в ответе")
//                .isNotEmpty();
//
//        //Зарегистрировать игроков (12 штук)
//        var createdPlayersInfo = IntStream.range(0, 12).mapToObj(i -> {
//            var playerRequest = buildPlayerRequestDTO();
//            var playerResponse = postPlayer(newAccessToken, playerRequest);
//            return Pair.of(playerResponse.getBody().jsonPath().getInt("id"), playerResponse.getBody().jsonPath().getString("email"));
//        }).toList();
//
//        //Запросить данные профиля созданного игрока
//        for (Pair<Integer, String> createdPlayerInfo: createdPlayersInfo) {
//            var playerByEmailResponse = getOnePlayer(newAccessToken, new PlayerRequestOneDTO(createdPlayerInfo.getRight()));
//            assertThat(playerByEmailResponse.getBody().jsonPath().getInt("id"))
//                    .as("Id созданного игрока соответствует полученному через get")
//                    .isEqualTo(createdPlayerInfo.getLeft());
//        }
//
//        //Запросить данные всех пользователей и отсортировать их по имени
//        var allPlayersResponse = getAllPlayers(newAccessToken).getBody();
//        List<String> names = allPlayersResponse.jsonPath()
//                .getList("sort { it.name }.name");
//        assertThat(names)
//                .as("Список имен игроков должен быть отсортирован по алфавиту")
//                .isSortedAccordingTo(CASE_INSENSITIVE_ORDER);
//
//        //Удалить всех ранее созданных пользователей
//        for (Pair<Integer, String> createdPlayerInfo: createdPlayersInfo) {
//            deleteOnePlayer(newAccessToken, createdPlayerInfo.getLeft());
//        }
//
//        //Запросить список всех пользователей и убедиться что он пустой
//        allPlayersResponse = getAllPlayers(newAccessToken).getBody();
//        var ids = allPlayersResponse.jsonPath().getList("id");
//        assertThatCollection(ids)
//                .as("Список не содержит удаленных id")
//                .doesNotContain(createdPlayersInfo.stream().map(Pair::getLeft).toArray());
//    }
}

