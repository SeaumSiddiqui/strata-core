package com.ardent.commerce.strata.user.application.command;

import com.ardent.commerce.strata.user.domain.model.UserRole;

import java.util.Set;

public record CreateUserCommand(
        String email,
        String phone,
        String firstName,
        String lastName,
        Set<UserRole.RoleType> roles,
        String password) {
}
