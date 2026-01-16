package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.command.UpdateUserPasswordCommand;
import com.ardent.commerce.strata.user.application.dto.UpdateUserPasswordRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.identity.keycloak.KeycloakUserIdentityService;
import com.ardent.commerce.strata.user.infrastructure.persistence.UserPersistenceHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateUserPasswordApplicationService implements ApplicationService<UpdateUserPasswordCommand, UserResponse> {
    private final UserDomainService userDomainService;
    private final KeycloakUserIdentityService keycloakUserIdentityService;
    private final UserPersistenceHandler userPersistenceHandler;

    @Override
    public UserResponse execute(UpdateUserPasswordCommand command) {
        User user = userDomainService.fetchActiveByKeycloakId(command.keycloakId());

        log.info("Updating user {} password", command.keycloakId());

        keycloakUserIdentityService.verifyUserCredentials(user.getEmail().value(), command.currentPassword());
        keycloakUserIdentityService.updateKeycloakUserPassword(command.keycloakId(), command.newPassword());

        user.recordPasswordChange();
        return userPersistenceHandler.persist(user);
    }

}
