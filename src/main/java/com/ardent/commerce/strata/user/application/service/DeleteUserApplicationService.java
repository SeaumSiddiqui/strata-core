package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteUserApplicationService {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @Transactional
    public void execute(UUID keycloakId) {
        log.info("Deleting user with Keycloak ID: {}", keycloakId);

        User user = userDomainService.fetchActiveByKeycloakId(keycloakId);

        user.markAsDeleted();
        userRepository.save(user);
        log.info("User marked as inactive in application");

        user.getDomainEvents().forEach(userEventPublisher::publish);
        user.clearDomainEvents();
    }

}
