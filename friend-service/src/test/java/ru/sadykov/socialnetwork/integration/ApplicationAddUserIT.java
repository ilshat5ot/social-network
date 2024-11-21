package ru.sadykov.socialnetwork.integration;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import ru.sadykov.socialnetwork.integration.stubs.AuthClientStub;

import java.time.Duration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@Transactional
@Sql(scripts = "/sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ApplicationAddUserIT {

    @ServiceConnection
    static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:14");

    @LocalServerPort
    private Integer port;

    private static final Network KAFKA_NETWORK = Network.newNetwork();
    private static final String CONFLUENT_PLATFORM_VERSION = "7.5.0";
    private static final DockerImageName KAFKA_IMAGE =
            DockerImageName.parse("confluentinc/cp-kafka").withTag(CONFLUENT_PLATFORM_VERSION);
    private static final KafkaContainer KAFKA =
            new KafkaContainer(KAFKA_IMAGE)
                    .withNetwork(KAFKA_NETWORK)
                    .withKraft()
                    .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1")
                    .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1");

    private static final SchemaRegistryContainer SCHEMA_REGISTRY =
            new SchemaRegistryContainer(CONFLUENT_PLATFORM_VERSION)
                    .withStartupTimeout(Duration.ofMinutes(2));
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.consumer.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add(
                "spring.kafka.producer.properties.schema.registry.url",
                SCHEMA_REGISTRY::getSchemaUrl);
        registry.add("spring.kafka.properties.schema.registry.url", SCHEMA_REGISTRY::getSchemaUrl);
    }

    static {
        postgresql.start();
        KAFKA.start();
        SCHEMA_REGISTRY.withKafka(KAFKA).start();
        SCHEMA_REGISTRY.withEnv("SCHEMA_REGISTRY_LISTENERS", SCHEMA_REGISTRY.getSchemaUrl());
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

        long userId = 5L;
        long subscriberId = 2L;

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

        long userId = 9L;
        long subscriberId = 6L;

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

    private static class SchemaRegistryContainer extends GenericContainer<SchemaRegistryContainer> {
        public static final String SCHEMA_REGISTRY_IMAGE = "confluentinc/cp-schema-registry";
        public static final int SCHEMA_REGISTRY_PORT = 8081;

        public SchemaRegistryContainer() {
            this(CONFLUENT_PLATFORM_VERSION);
        }

        public SchemaRegistryContainer(String version) {
            super(DockerImageName.parse(SCHEMA_REGISTRY_IMAGE).withTag(CONFLUENT_PLATFORM_VERSION));

            waitingFor(Wait.forHttp("/subjects").forStatusCode(200));
            withExposedPorts(SCHEMA_REGISTRY_PORT);
        }

        public SchemaRegistryContainer withKafka(KafkaContainer kafka) {
            return withKafka(kafka.getNetwork(), kafka.getNetworkAliases().get(0) + ":9092");
        }

        public SchemaRegistryContainer withKafka(Network network, String bootstrapServers) {
            withNetwork(network);
            withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry");
            withEnv(
                    "SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                    "PLAINTEXT://" + bootstrapServers);
            return self();
        }

        public String getSchemaUrl() {
            return "http://%s:%d".formatted(getHost(), getMappedPort(SCHEMA_REGISTRY_PORT));
        }
    }
}
