package com.ardent.commerce.strata.user.domain.exception;

import com.ardent.commerce.strata.shared.domain.exception.DomainException;

import java.util.UUID;

public class UserInactiveException extends DomainException {
    public UserInactiveException(UUID keycloakId) {
        super("User with Keycloak ID: "+ keycloakId + " is inactive in the application and cannot be used");
    }
}
