package com.ardent.commerce.strata.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRoleRequest(
        @NotBlank(message = "Role name is required")
        String roleName) {
}
