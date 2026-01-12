package com.ardent.commerce.strata.user.domain.model;

import com.ardent.commerce.strata.shared.domain.ValueObject;
import lombok.NonNull;

/**
 * Value Object: Phone number with basic validation.
 * Immutable, validated at construction.
 */
public record Phone(@NonNull String value) implements ValueObject {
    public Phone {
        if (value == null && value.isBlank()) {
            throw new IllegalArgumentException("Phone is mandatory and cannot be blank");
        }
        String digitsOnly = value.replaceAll("\\D", "");
        if (digitsOnly.length() < 10) {
            throw new IllegalArgumentException("Phone must contain at least 10 digits");
        }
    }

    public static Phone of(String value) {
        return new Phone(value);
    }
}
