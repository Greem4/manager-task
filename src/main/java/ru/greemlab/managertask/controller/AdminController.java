package ru.greemlab.managertask.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.greemlab.managertask.domain.dto.UpdateUserRoleRequest;
import ru.greemlab.managertask.domain.model.User;
import ru.greemlab.managertask.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/make-admin{username}")
    @ResponseStatus(HttpStatus.OK)
    public User makeAdmin(@RequestBody UpdateUserRoleRequest request) {
        return userService.updateUserRole(request.username(), request.role());
    }
}
