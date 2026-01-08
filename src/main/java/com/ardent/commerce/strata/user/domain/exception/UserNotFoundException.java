package com.ardent.commerce.strata.user.domain.exception;

import com.ardent.commerce.strata.shared.domain.exception.DomainException;

import java.util.UUID;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(UUID userId) {
        super("User not found with ID: " + userId);
    }

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
