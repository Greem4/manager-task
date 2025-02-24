package ru.greemlab.managertask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.greemlab.managertask.domain.dto.UpdateUserRoleRequest;
import ru.greemlab.managertask.domain.model.User;
import ru.greemlab.managertask.service.UserService;

/**
 * Контроллер для управления пользователями администраторами.
 * Позволяет обновлять роль пользователя.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    /**
     * Метод для повышения пользователя до роли администратора.
     * Доступно только для пользователей с ролью "ADMIN".
     *
     * @param request Запрос на изменение роли пользователя.
     * @return Обновленный пользователь с новой ролью.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/make-admin/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Повысить пользователя до администратора",
            description = "Этот метод позволяет повысить пользователя до роли администратора. Доступно только для пользователей с ролью 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно повышен до администратора"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для выполнения операции"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    public User makeAdmin(@RequestBody UpdateUserRoleRequest request) {
        return userService.updateUserRole(request.username(), request.role());
    }
}
