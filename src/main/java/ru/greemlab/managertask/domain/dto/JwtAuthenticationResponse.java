package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для ответа с JWT токеном при аутентификации.
 */
@Schema(description = "Ответ с JWT токеном при аутентификации")
public record JwtAuthenticationResponse(

        @Schema(description = "JWT токен для авторизации", example = "token")
        String token
) {
}
