package com.ardent.commerce.strata.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserPasswordRequest(
        @NotBlank
        String currentPassword,

        @NotBlank
        String newPassword,

        @NotBlank
        String confirmNewPassword) {

    public UpdateUserPasswordRequest {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("Password mismatched");
        }

        if (newPassword.equals(currentPassword)) {
            throw new IllegalArgumentException("Current password cannot be new password");
        }
    }

}
