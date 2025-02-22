package ru.greemlab.managertask.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @Email(message = "Введен не верно")
        @NotBlank(message = "Имя пользователя не может быть пустыми")
        String email,
        @Size(min = 4, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
        @NotBlank(message = "Пароль не может быть пустыми")
        String password
) {
}
