package com.ardent.commerce.strata.user.domain.model;

import com.ardent.commerce.strata.shared.domain.ValueObject;
import lombok.NonNull;

import java.util.UUID;

/**
 * Value Object: Unique identifier for User aggregate.
 * Immutable, no external dependencies, validated at construction.
 */

public record UserId(@NonNull UUID value) implements ValueObject {
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}
