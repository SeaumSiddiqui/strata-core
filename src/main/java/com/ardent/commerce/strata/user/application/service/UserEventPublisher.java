package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Publish domain events to other bounded contexts.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publish(DomainEvent event) {
        log.debug("Publishing event: {} at {}", event.eventName(), event.occurredAt());
        eventPublisher.publishEvent(event);
    }

}
