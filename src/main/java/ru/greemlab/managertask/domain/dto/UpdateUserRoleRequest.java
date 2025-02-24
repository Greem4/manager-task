package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.greemlab.managertask.domain.model.Role;

/**
 * DTO для запроса на обновление роли пользователя.
 */
@Schema(description = "Запрос на обновление роли пользователя")
public record UpdateUserRoleRequest(

        @Schema(description = "Имя пользователя", example = "user")
        String username,

        @Schema(description = "Новая роль пользователя", allowableValues = {"ROLE_USER", "ROLE_ADMIN"}, example = "ROLE_ADMIN")
        Role role
) {
}
