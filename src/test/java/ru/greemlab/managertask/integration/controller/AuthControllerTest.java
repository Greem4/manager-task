package ru.greemlab.managertask.integration.controller;

import org.junit.jupiter.api.Test;
import ru.greemlab.managertask.domain.dto.SignUpRequest;
import ru.greemlab.managertask.domain.dto.SignInRequest;
import ru.greemlab.managertask.domain.dto.JwtAuthenticationResponse;
import ru.greemlab.managertask.integration.config.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthControllerTest extends IntegrationTestBase {

    @Test
    void testRegister() {
        var request = new SignUpRequest(
                "test123",
                "test55@test55.com",
                "1234567"
        );

        var response = testRestTemplate
                .postForEntity("/api/v1/auth/register", request, JwtAuthenticationResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotEmpty();
    }

    @Test
    void testLogin() {
        var request = new SignInRequest("admin@mail.ru", "admin");

        var response = testRestTemplate
                .postForEntity("/api/v1/auth/login", request, JwtAuthenticationResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotEmpty();
    }
}
