package com.ardent.commerce.strata.user.domain.model;

import com.ardent.commerce.strata.shared.domain.ValueObject;
import lombok.NonNull;

/**
 * Value Object: Phone number with basic validation.
 * CountryCode The ISO or international prefix (e.g., "+880")
 * Immutable, validated at construction.
 */
public record Phone(@NonNull String countryCode, @NonNull String number) implements ValueObject {
    public Phone{
        // Remove all non-digit characters to get the raw digit count
        String rawDigits = number.replaceAll("\\D", "");
        if (rawDigits.length() < 10) {
            throw new IllegalArgumentException("Phone number must contain at-least 10 digits");
        }
    }

    /**
     * Factory Method: Creates a phone object from single string.
     * Useful when source (like Keycloak or an API) provides a full string.
     */
    public static Phone of(String value) {
        if (value == null || !value.contains(" ")) {
            throw new IllegalArgumentException("Phone format must be: +ISD NUMBER(e.g., +44 567890)");
        }
        // Split by any whitespace character (one or more)
        String[] chunks = value.trim().split("\\s+", 2);
        return new Phone(chunks[0], chunks[1]);
    }

    /**
     * Returns in readable international format
     */
    public String getFullNumber() {
        return countryCode + " " + number;
    }
}
