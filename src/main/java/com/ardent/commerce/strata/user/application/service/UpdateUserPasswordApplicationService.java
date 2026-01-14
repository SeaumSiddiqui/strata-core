package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.user.application.dto.UpdateUserPasswordRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.identity.keycloak.KeycloakUserIdentityService;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateUserPasswordApplicationService {
    private final UserDomainService userDomainService;
    private final KeycloakUserIdentityService keycloakUserIdentityService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher userEventPublisher;

    @Transactional
    public UserResponse execute(UUID keycloakId, @Valid UpdateUserPasswordRequest request) {
        User user = userDomainService.fetchActiveByKeycloakId(keycloakId);

        log.info("Updating user {} password", keycloakId);

        keycloakUserIdentityService.verifyUserCredentials(user.getEmail().value(), request.newPassword());
        keycloakUserIdentityService.updateKeycloakUserPassword(keycloakId, request.newPassword());

        user.recordPasswordChange();

        userRepository.save(user);
        log.info("Password updated for user: {}", keycloakId);

        user.getDomainEvents().forEach(userEventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

}
