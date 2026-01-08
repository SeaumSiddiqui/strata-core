package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.user.application.dto.UpdateUserProfileRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.Phone;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Application Service: On create update.
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
public class UpdateUserProfileApplicationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher eventPublisher;

    @Transactional
    public UserResponse execute(UUID keycloakId, UpdateUserProfileRequest request) {
        // Load aggregate
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(()-> new RuntimeException("User not found with keycloak ID: " + keycloakId));

        log.info("Updating user profile for ID: {}", user.getId().value());

        // Update profile
        user.updateUserProfile(
                request.firstName(),
                request.lastName(),
                Phone.of(request.phone())
                );

        // Save
        userRepository.save(user);
        log.info("User updated for ID: {}", user.getId().value());

        // Publish events
        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        // Return response
        return userMapper.toResponse(user);
    }
}
