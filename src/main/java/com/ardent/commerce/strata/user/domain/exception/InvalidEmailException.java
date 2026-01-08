package com.ardent.commerce.strata.user.domain.exception;

import com.ardent.commerce.strata.shared.domain.exception.DomainException;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String email) {
        super("Invalid email format: " + email);
    }
}
