package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.command.UpdateUserCommand;
import com.ardent.commerce.strata.user.application.dto.UpdateUserProfileRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.Phone;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Application Service: On user profile update.
 * Steps:
 * 1. Load aggregate
 * 2. Update profile
 * 3. Save
 * 4. Publish events
 * 5. Return response
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateUserProfileApplicationService implements ApplicationService<UpdateUserCommand, UserResponse> {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher eventPublisher;

    @Override
    @Transactional
    public UserResponse execute(UpdateUserCommand command) {
        User user = userDomainService.fetchActiveByKeycloakId(command.keycloakId());

        log.info("Updating user profile for ID: {}", user.getId().value());

        user.updateUserProfile(
                command.firstName(),
                command.lastName(),
                Phone.of(command.phone())
                );

        userRepository.save(user);
        log.info("User updated for ID: {}", user.getId().value());

        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

}
