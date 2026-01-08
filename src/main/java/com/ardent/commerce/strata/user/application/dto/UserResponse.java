package com.ardent.commerce.strata.user.application.dto;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class UserResponse {
    UUID id;
    String email;
    String phone;
    String firstName;
    String lastName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
