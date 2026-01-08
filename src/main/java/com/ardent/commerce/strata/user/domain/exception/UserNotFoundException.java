package com.ardent.commerce.strata.user.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID userId) {
        super("User not found with ID: " + userId);
    }

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
