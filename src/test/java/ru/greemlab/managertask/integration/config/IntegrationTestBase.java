package ru.greemlab.managertask.integration.config;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.greemlab.managertask.domain.dto.JwtAuthenticationResponse;
import ru.greemlab.managertask.domain.dto.SignInRequest;

import java.util.Objects;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Sql(scripts = "classpath:sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class IntegrationTestBase {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Container
    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    protected @NotNull HttpHeaders getHeaders(String username, String password) {
        var jwtResponse = authenticate(username, password);
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwtResponse.token());
        return headers;
    }

    protected @NotNull HttpEntity<Object> getAuth(String username, String password) {
        var headers = getHeaders(username, password);
        return new HttpEntity<>(null, headers);
    }

    protected @NotNull HttpHeaders getHeadersAdmin() {
        return getHeaders("admin@mail.ru", "admin");
    }

    protected @NotNull HttpHeaders getHeadersUser() {
        return getHeaders("user@mail.ru", "user");
    }

    protected JwtAuthenticationResponse authenticate(String username, String password) {
        var loginRequest = new SignInRequest(username, password);

        var response = testRestTemplate.postForEntity(
                "/api/v1/auth/login",
                new HttpEntity<>(loginRequest, getHttpHeaders()),
                JwtAuthenticationResponse.class
        );
        return Objects.requireNonNull(response.getBody());
    }

    protected static @NotNull HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
