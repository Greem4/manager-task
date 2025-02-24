package ru.greemlab.managertask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.greemlab.managertask.domain.dto.JwtAuthenticationResponse;
import ru.greemlab.managertask.domain.dto.SignInRequest;
import ru.greemlab.managertask.domain.dto.SignUpRequest;
import ru.greemlab.managertask.domain.model.Role;
import ru.greemlab.managertask.domain.model.User;

/**
 * Сервис для аутентификации пользователей.
 * Обрабатывает регистрацию и вход пользователей, генерирует JWT токены.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация нового пользователя.
     *
     * @param request Данные для регистрации.
     * @return Ответ с JWT токеном для аутентификации.
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request Данные для входа.
     * @return Ответ с JWT токеном для аутентификации.
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));

        var email = userService
                .userDetailsService()
                .loadUserByUsername(request.email());

        var jwt = jwtService.generateToken(email);
        return new JwtAuthenticationResponse(jwt);
    }
}
