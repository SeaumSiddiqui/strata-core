package com.ardent.commerce.strata.user.application.dto;

import com.ardent.commerce.strata.user.domain.model.UserRole;
import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

public record CreateUserRequest(
        @NotNull(message = "Keycloak ID is required")
        UUID keycloakId,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone is required")
        String phone,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Size(min = 1, message = "User must have at least one role on creation")
        Set<UserRole.RoleType> roles,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String password) {
}
