package com.ardent.commerce.strata.user.domain.event;

import com.ardent.commerce.strata.shared.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record UserCreatedEvent(
    UUID userId,
    String email,
    UUID keycloakId,
    LocalDateTime occurredAt) implements DomainEvent{

    public UserCreatedEvent {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(keycloakId, "keycloakId must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }

    public static final String EVENT_NAME = "UserCreatedEvent";

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
