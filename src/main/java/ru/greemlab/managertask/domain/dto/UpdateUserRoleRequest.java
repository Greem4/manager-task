package ru.greemlab.managertask.domain.dto;

import ru.greemlab.managertask.domain.model.Role;

public record UpdateUserRoleRequest(
        String username,
        Role role
) {
}
