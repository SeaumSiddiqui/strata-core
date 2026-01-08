package com.ardent.commerce.strata.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class UpdateUserProfileRequest {
    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;

    @NotBlank(message = "Phone is required")
    String phone;
}
