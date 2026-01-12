package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.dto.CreateUserRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.exception.KeycloakUserCreationFailedException;
import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.Phone;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.model.UserRole;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.identity.keycloak.KeycloakUserIdentityService;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * Application Service: On create user.
 * Steps:
 * 1. Check if email already exists
 * 2. Create new user in Keycloak
 * 3. Create new user aggregate
 * 4. Save
 * 5. Publish events
 * 6. Return response

 * Delete user from keycloak on failed DB save
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CreateUserApplicationService implements ApplicationService<CreateUserRequest, UserResponse> {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final KeycloakUserIdentityService keycloakUserIdentityService;
    private final UserEventPublisher eventPublisher;
    private final UserMapper userMapper;

    @Override
    public UserResponse execute(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.email());

        Email email = Email.of(request.email());
        userDomainService.assertEmailIsUnique(email);

        UUID keycloakId = null;

        try {
            keycloakId = keycloakUserIdentityService.createKeycloakUser(request.email(), request.password());

            return saveUserToDatabase(request, keycloakId, email);

        } catch (Exception e) {
            if (keycloakId != null) {
                rollingKeycloakUserCreation(keycloakId);
            }
            throw e;
        }
    }

    @Transactional
    protected UserResponse saveUserToDatabase(CreateUserRequest request, UUID keycloakId, Email email) {
        User user = User.create(
                keycloakId,
                email,
                Phone.of(request.phone()),
                request.firstName(),
                request.lastName(),
                Set.of(UserRole.of(UserRole.RoleType.CUSTOMER)) // Default role for new users
        );

        userRepository.save(user);
        log.info("New user created with ID: {}", user.getId());

        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

    private void rollingKeycloakUserCreation(UUID keycloakId) {
        try {
            log.warn("Failed to save new user into the database, rolling back Keycloak user creation for ID: {}", keycloakId);
            keycloakUserIdentityService.deleteKeycloakUser(keycloakId);
        } catch (KeycloakUserCreationFailedException ex) { //TODO-> createKeycloakUser() need to throw this error
            log.error("Failed rolling back Keycloak user creation after saving new user failed into the database", ex);
        }
    }

}
