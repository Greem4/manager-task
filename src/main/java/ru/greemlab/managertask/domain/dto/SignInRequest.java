package ru.greemlab.managertask.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @Size(min = 4, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
        @NotBlank(message = "Имя пользователя не может быть пустыми")
        String username,
        @Size(min = 4, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
        @NotBlank(message = "Пароль не может быть пустыми")
        String password
) {
}
