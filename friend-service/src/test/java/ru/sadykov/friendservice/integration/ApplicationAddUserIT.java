package ru.sadykov.friendservice.integration;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.sadykov.friendservice.integration.stubs.AuthClientStub;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@Transactional
@Sql(scripts = "/sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ApplicationAddUserIT {


    @ServiceConnection
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14");

    @LocalServerPort
    private Integer port;

    static {
        container.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void alreadyFriends() {

        long userId = 3L;
        long subscriberId = 4L;

        AuthClientStub.stubAuthCallResponseTrue(subscriberId);

        RestAssured.given()
                .when()
                .post(String.format("/api/v1/%d/%d", userId, subscriberId))
                .then()
                .statusCode(200)
                .body("message", Matchers.notNullValue())
                .body("message", Matchers.equalTo("Вы уже являетесь друзьями!"));
    }

    @Test
    void youAreFriend() {

        long userId = 2L;
        long subscriberId = 5L;

        AuthClientStub.stubAuthCallResponseTrue(subscriberId);

        RestAssured.given()
                .when()
                .post(String.format("/api/v1/%d/%d", userId, subscriberId))
                .then()
                .statusCode(200)
                .body("message", Matchers.notNullValue())
                .body("message", Matchers.equalTo("Вы приняли запрос дружбы!"));
    }

    @Test
    void youAreSub() {

        long userId = 1L;
        long subscriberId = 2L;

        AuthClientStub.stubAuthCallResponseTrue(subscriberId);

        RestAssured.given()
                .when()
                .post(String.format("/api/v1/%d/%d", userId, subscriberId))
                .then()
                .statusCode(200)
                .body("message", Matchers.notNullValue())
                .body("message", Matchers.equalTo("Вы подписались на пользователя!"));
    }

    @Test
    void requestAlreadySent() {

        long userId = 6L;
        long subscriberId = 9L;

        AuthClientStub.stubAuthCallResponseTrue(subscriberId);

        RestAssured.given()
                .when()
                .post(String.format("/api/v1/%d/%d", userId, subscriberId))
                .then()
                .statusCode(200)
                .body("message", Matchers.notNullValue())
                .body("message", Matchers.equalTo("Вы уже отправили заявку в друзья!"));
    }

    @Test
    void userNotFoundException() {

        long userId = 6L;
        long subscriberId = 9L;

        AuthClientStub.stubAuthCallResponseFalse(subscriberId);

        RestAssured.given()
                .when()
                .post(String.format("/api/v1/%d/%d", userId, subscriberId))
                .then()
                .statusCode(400)
                .body("message", Matchers.notNullValue())
                .body("message", Matchers.equalTo(String.format("Пользователь с ид %d не найден!", subscriberId)));

    }

    @Test
    void invalidRequestParameterException() {

        long userId = 9L;
        long subscriberId = 9L;

        AuthClientStub.stubAuthCallResponseTrue(subscriberId);

        RestAssured.given()
                .when()
                .post(String.format("/api/v1/%d/%d", userId, subscriberId))
                .then()
                .statusCode(400)
                .body("message", Matchers.notNullValue())
                .body("message", Matchers.equalTo("Нельзя добавить самого себя в друзья!"));
    }
}
