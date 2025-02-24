package ru.greemlab.managertask.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Перечисление ролей пользователей.
 */
@Schema(description = "Роли пользователей")
public enum Role {

    @Schema(description = "Пользователь с базовыми правами")
    ROLE_USER,

    @Schema(description = "Пользователь с правами администратора")
    ROLE_ADMIN
}
