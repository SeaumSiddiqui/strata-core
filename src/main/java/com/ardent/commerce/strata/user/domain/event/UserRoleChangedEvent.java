package com.ardent.commerce.strata.user.domain.event;

import com.ardent.commerce.strata.shared.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record UserRoleChangedEvent(UUID keycloakId,
                                   String role,
                                   RoleChangeType changeType,
                                   LocalDateTime occurredAt) implements DomainEvent {

    public enum RoleChangeType {
        ASSIGNED, REMOVED;
    }

    public UserRoleChangedEvent {
        Objects.requireNonNull(keycloakId, "keycloakId must not be null");
        Objects.requireNonNull(role, "role must not be null");
        Objects.requireNonNull(changeType, "changeType must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
    public static final String EVENT_NAME = "UserRoleChangedEvent";

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
