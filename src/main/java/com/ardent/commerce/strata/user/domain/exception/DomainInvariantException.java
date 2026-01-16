package com.ardent.commerce.strata.user.domain.exception;

import com.ardent.commerce.strata.shared.domain.exception.DomainException;

public class DomainInvariantException extends DomainException {
    public DomainInvariantException(String message) {
        super("Domain invariant violation: " + message);
    }
}
