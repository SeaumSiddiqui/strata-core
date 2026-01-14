package com.ardent.commerce.strata.user.infrastructure.identity.keycloak;

import com.ardent.commerce.strata.user.domain.event.UserDeletedEvent;
import com.ardent.commerce.strata.user.domain.event.UserEmailChangedEvent;
import com.ardent.commerce.strata.user.domain.event.UserPasswordChangeEvent;
import com.ardent.commerce.strata.user.domain.event.UserRoleChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Infrastructure Listener: Syncs Domain state changes to Keycloak Identity Provider.
 * * Logic:
 * 1. Listen for successful DB commits (AFTER_COMMIT).
 * 2. Sync corresponding change to Keycloak.
 * 3. Handle synchronization failures gracefully without affecting the DB transaction.

 * * Note: Uses Administrative "Force Update" for emails to avoid password propagation in events.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserKeycloakSyncListener {
    private final KeycloakUserIdentityService keycloakUserIdentityService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleKeycloakUserDelete(UserDeletedEvent event) {
        try {
            keycloakUserIdentityService.deleteKeycloakUser(event.keycloakId());
        } catch (Exception e) {
            log.error("Failed to delete user from Keycloak with ID: {}", event.keycloakId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserEmailChange(UserEmailChangedEvent event) {
        try {
            keycloakUserIdentityService.updateKeycloakUserEmail(event.keycloakId(), event.email());
        } catch (Exception e) {
            log.error("Failed to update user email on Keycloak with ID: {}", event.keycloakId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRoleChange(UserRoleChangedEvent event) {
        try {
            keycloakUserIdentityService.updateKeycloakUserRole(event.keycloakId(), event.role());
        } catch (Exception e) {
            log.error("Failed updating role in keycloak for user: {}", event.keycloakId());
        }
    }

}
