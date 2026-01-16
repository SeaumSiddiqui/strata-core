package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.command.DeleteUserCommand;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteUserApplicationService implements ApplicationService<DeleteUserCommand, Void> {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @Override
    @Transactional
    public Void execute(DeleteUserCommand command) {
        log.info("Deleting user with Keycloak ID: {}", command.keycloakId());

        User user = userDomainService.fetchActiveByKeycloakId(command.keycloakId());

        user.markAsDeleted();
        userRepository.save(user);
        log.info("User marked as inactive in application");

        user.getDomainEvents().forEach(userEventPublisher::publish);
        user.clearDomainEvents();

        return null;
    }

}
