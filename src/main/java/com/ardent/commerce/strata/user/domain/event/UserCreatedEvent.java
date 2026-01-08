package com.ardent.commerce.strata.user.domain.event;

import com.ardent.commerce.strata.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Event: Fired when user is created.
 * Other bounded contexts listen to this event.
 */
@Value
public class UserCreatedEvent implements DomainEvent {
    UUID userId;
    String email;
    UUID keycloakId;
    @Getter(onMethod_ = @Override)
    LocalDateTime occurredAt;

    // Custom constructor to handle the timestamp automatically
    public UserCreatedEvent(@NonNull UUID userId, @NonNull String email, @NonNull UUID keycloakId) {
        this.userId = userId;
        this.email = email;
        this.keycloakId = keycloakId;
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public String eventName() {
        return this.getClass().getSimpleName();
    }
}
