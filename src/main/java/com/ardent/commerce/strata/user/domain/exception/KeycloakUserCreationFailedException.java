package com.ardent.commerce.strata.user.domain.exception;

import com.ardent.commerce.strata.shared.domain.exception.DomainException;

public class KeycloakUserCreationFailedException extends DomainException {
    public KeycloakUserCreationFailedException(String email, Throwable cause) {
        super("Failed to create Keycloak user for email: " + email, cause);
    }
}
