package com.ardent.commerce.strata.user.domain.model;

import com.ardent.commerce.strata.shared.domain.ValueObject;
import lombok.NonNull;

/**
 * Value Object: Email address with validation.
 * Immutable, validated at construction.
 */
public record Email(@NonNull String value) implements ValueObject {
    public Email{
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
