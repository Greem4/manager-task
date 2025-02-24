package ru.greemlab.managertask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greemlab.managertask.domain.dto.JwtAuthenticationResponse;
import ru.greemlab.managertask.domain.dto.SignInRequest;
import ru.greemlab.managertask.domain.dto.SignUpRequest;
import ru.greemlab.managertask.service.AuthenticationService;

/**
 * Контроллер аутентификации пользователей.
 * Предоставляет два эндпоинта для регистрации и входа пользователя.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос на регистрацию пользователя, содержащий данные для создания аккаунта.
     * @return объект {@link JwtAuthenticationResponse}, содержащий JWT токен.
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя",
            description = "Регистрирует нового пользователя и возвращает JWT токен для аутентификации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация и возвращён токен"),
            @ApiResponse(responseCode = "400", description = "Неверные или неполные данные для регистрации")
    })
    public JwtAuthenticationResponse singUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    /**
     * Вход пользователя в систему.
     *
     * @param request запрос на вход пользователя, содержащий имя пользователя и пароль.
     * @return объект {@link JwtAuthenticationResponse}, содержащий JWT токен для аутентификации.
     */
    @PostMapping("/login")
    @Operation(summary = "Вход пользователя",
            description = "Аутентифицирует пользователя по его учетным данным и возвращает JWT токен.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход и возвращён токен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для входа"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    public JwtAuthenticationResponse singIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
