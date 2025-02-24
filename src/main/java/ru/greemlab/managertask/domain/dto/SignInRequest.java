package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для запроса аутентификации пользователя.
 */
@Schema(description = "Запрос на вход пользователя в систему")
public record SignInRequest(

        @Email(message = "Введен не верно")
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Schema(description = "Электронная почта пользователя", example = "user@mail.ru")
        String email,

        @Size(min = 4, max = 255, message = "Длина пароля должна быть от 4 до 255 символов")
        @NotBlank(message = "Пароль не может быть пустым")
        @Schema(description = "Пароль пользователя", example = "user")
        String password
) {
}
