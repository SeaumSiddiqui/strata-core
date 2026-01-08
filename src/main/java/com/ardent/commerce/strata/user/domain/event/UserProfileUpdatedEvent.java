package com.ardent.commerce.strata.user.domain.event;

import com.ardent.commerce.strata.shared.domain.DomainEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Event: Fired when user profile is updated.
 */
@Value
public class UserProfileUpdatedEvent implements DomainEvent {
    UUID userId;
    String email;
    @Getter(onMethod_ = @Override)
    LocalDateTime occurredAt;
    public UserProfileUpdatedEvent(@NonNull UUID userId, @NonNull String email) {
        this.userId = userId;
        this.email = email;
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public String eventName() {
        return this.getClass().getSimpleName();
    }
}
