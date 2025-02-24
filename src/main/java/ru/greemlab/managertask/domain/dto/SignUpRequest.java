package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для запроса регистрации пользователя.
 */
@Schema(description = "Запрос на регистрацию нового пользователя")
public record SignUpRequest(

        @Size(min = 4, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Schema(description = "Имя пользователя", example = "new_user")
        String username,

        @Size(min = 4, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
        @NotBlank(message = "Адрес электронной почты не может быть пустым")
        @Email(message = "Email адрес должен быть в формате user@example.com")
        @Schema(description = "Адрес электронной почты", example = "user@example.com")
        String email,

        @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
        @Schema(description = "Пароль пользователя", example = "password1234")
        String password
) {
}
