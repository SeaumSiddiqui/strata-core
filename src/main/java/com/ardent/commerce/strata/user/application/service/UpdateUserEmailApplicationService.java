package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.command.UpdateUserEmailCommand;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.identity.keycloak.KeycloakUserIdentityService;
import com.ardent.commerce.strata.user.infrastructure.persistence.UserPersistenceHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Application Service: On user email update.
 * Steps:
 * 1. Check if email already exists
 * 2. Fetch user with Keycloak ID
 * 3. Preserve old email value for rollback
 * 4. Try to save updated email in both Keycloak and DB

 * Rollback on failed database save, Update Keycloak with old email value.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateUserEmailApplicationService implements ApplicationService<UpdateUserEmailCommand, UserResponse> {
    private final UserDomainService userDomainService;
    private final KeycloakUserIdentityService keycloakUserIdentityService;
    private final UserPersistenceHandler userPersistenceHandler;

    @Override
    public UserResponse execute(UpdateUserEmailCommand command) {
        log.info("Updating user {} with new email: {}", command.keycloakId(), command.newEmail());

        User user = userDomainService.fetchActiveByKeycloakId(command.keycloakId());

        Email newEmail = Email.of(command.newEmail());
        userDomainService.assertEmailIsUnique(newEmail); // Business rule duplicate check

        String currentEmail = user.getEmail().value();
        keycloakUserIdentityService.verifyUserCredentials(currentEmail, command.password()); // Passing current email & password to verify user credentials

        user.changeEmail(newEmail);
        return userPersistenceHandler.persist(user);
    }

}
