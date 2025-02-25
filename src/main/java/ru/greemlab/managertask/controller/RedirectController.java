package ru.greemlab.managertask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для перенаправления на Swagger UI.
 * Позволяет пользователю попасть на страницу документации API.
 */
@Controller
public class RedirectController {

    /**
     * Метод для перенаправления на Swagger UI.
     * Данный метод доступен по корневому URL "/".
     *
     * @return Строка с адресом для перенаправления на Swagger UI.
     */
    @RequestMapping("/")
    @Operation(
            summary = "Перенаправление на Swagger UI",
            description = "Этот метод перенаправляет пользователя на страницу Swagger UI для визуализации документации API."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Пользователь перенаправлен на Swagger UI")
    })
    public String redirect() {
        return "redirect:/swagger-ui/index.html";
    }
}
