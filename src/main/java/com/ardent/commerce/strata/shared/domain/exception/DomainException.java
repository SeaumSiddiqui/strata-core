package com.ardent.commerce.strata.shared.domain.exception;

/**
 * Base class for ALL domain exceptions.
 * Domain exceptions represent business rule violations.

 * Extending RuntimeException,
 * - Domain operations should fail fast on business logic errors.
 * - Clients know exceptions might occur (no checked exception forcing).
 * - Spring exception handlers will catch them.
 */
public class DomainException extends RuntimeException{
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
