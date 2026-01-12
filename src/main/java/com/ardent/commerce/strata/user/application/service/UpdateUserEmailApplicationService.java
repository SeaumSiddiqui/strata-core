package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.user.application.dto.UpdateUserEmailRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.identity.keycloak.KeycloakUserIdentityService;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Application Service: On user email update.
 * Steps:
 * 1. Check if email already exists
 * 2. Fetch user with Keycloak ID
 * 3. Preserve old email value for rollback
 * 4. Try to save updated email in both Keycloak and DB
 * 5. Save
 * 6. Publish events
 * 7. Return response

 * Rollback on failed database save, Update Keycloak with old email value.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateUserEmailApplicationService {
    private final UserDomainService userDomainService;
    private final KeycloakUserIdentityService keycloakUserIdentityService;
    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;
    private final UserMapper userMapper;

    public UserResponse execute(UUID keycloakId, UpdateUserEmailRequest request) {
        log.info("Updating user {} with new email: {}", keycloakId, request.email());

        User user = userDomainService.fetchActiveByKeycloakId(keycloakId);

        Email newEmail = Email.of(request.email());
        userDomainService.assertEmailIsUnique(newEmail); // Business rule duplicate check

        String currentEmail = user.getEmail().value();
        keycloakUserIdentityService.verifyUserCredentials(currentEmail, request.password()); // Passing current email & password to verify user credentials

        return persistEmailChange(user, newEmail);
    }

    @Transactional
    private UserResponse persistEmailChange(User user, Email newEmail) {

        user.changeEmail(newEmail);
        userRepository.save(user);
        log.info("email updated for user {} with new email: {}", user.getKeycloakId(), newEmail.value());

        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

}
