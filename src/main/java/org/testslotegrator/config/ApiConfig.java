package org.testslotegrator.config;

public class ApiConfig {

    // Базовый URL API (загружается из env или gradle.properties)
    public static final String BASE_URL = ConfigLoader.getProperty("api.base.url", "https://testslotegrator.com/api");

    // Endpoint'ы
    public static final String LOGIN_ENDPOINT = "/tester/login";
    public static final String PLAYERS_CREATE_ENDPOINT = "/automationTask/create";
    public static final String PLAYERS_GET_ONE_ENDPOINT = "/automationTask/getOne";
    public static final String PLAYERS_GET_ALL_ENDPOINT = "/automationTask/getAll";
    public static final String PLAYERS_DELETE_ENDPOINT = "/automationTask/deleteOne/{playerId}";

    // Basic Auth для login (загружаются из env или gradle.properties)
    public static final String BASIC_AUTH_USERNAME = ConfigLoader.getProperty("basic.auth.username");
    public static final String BASIC_AUTH_PASSWORD = ConfigLoader.getProperty("basic.auth.password");

    // Данные для получения Bearer токена (загружаются из env или gradle.properties)
    public static final String AUTH_EMAIL = ConfigLoader.getProperty("auth.email");
    public static final String AUTH_PASSWORD = ConfigLoader.getProperty("auth.password");
}
