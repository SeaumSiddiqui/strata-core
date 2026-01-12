package com.ardent.commerce.strata.user.domain.model;

import com.ardent.commerce.strata.shared.domain.ValueObject;
import org.springframework.lang.NonNull;

/**
 * Value Object: UseRole with null validation.
 * Immutable, validated at construction.
 */
public record UserRole(@NonNull RoleType value) implements ValueObject {
    public enum RoleType {
        CUSTOMER, STAFF, ADMIN;

        public static RoleType from(String value) {
            try {
                return RoleType.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role", e);
            }
        }
    }

    public UserRole {
        if (value == null) {
            throw new IllegalArgumentException("User RoleType is mandatory and cannot be null");
        }
    }

    public static UserRole of(RoleType value) {
        return new UserRole(value);
    }

    public static UserRole from(String value) {
        return UserRole.of(RoleType.from(value));
    }

    public String getAuthority() {
        return "ROLE_" + value.name();
    }
}
