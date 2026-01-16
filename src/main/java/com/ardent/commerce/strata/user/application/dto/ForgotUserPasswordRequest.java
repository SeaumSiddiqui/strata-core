package com.ardent.commerce.strata.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotUserPasswordRequest(
        @NotBlank(message = "A new email is required")
        @Email(message = "Invalid email format")
        String email) {
}
