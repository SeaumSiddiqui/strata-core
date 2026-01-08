package com.ardent.commerce.strata.shared.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all domain Entities.
 * Entities are defined by their identity and can track domain events.
 */
public abstract class Entity {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Add domain event to be published.
     * Called during business operations.
     */
    protected void addDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    /**
     * Returns an immutable list of events recorded by this entity.
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Clear events after successful persistence.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
