package com.ardent.commerce.strata.user.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String phone,
        String firstName,
        String lastName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
