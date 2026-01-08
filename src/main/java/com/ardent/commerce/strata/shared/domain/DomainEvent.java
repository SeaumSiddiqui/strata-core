package com.ardent.commerce.strata.shared.domain;

import java.time.LocalDateTime;

/**
 * Base interface for all domain events.
 */
public interface DomainEvent {
    LocalDateTime occurredAt();
    String eventName();
}
