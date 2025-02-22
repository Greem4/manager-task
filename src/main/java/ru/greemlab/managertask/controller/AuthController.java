package ru.greemlab.managertask.controller;

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

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtAuthenticationResponse singUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse singIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
