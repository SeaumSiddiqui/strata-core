package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.dto.CreateUserRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.Phone;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service: On create user.
 * Steps:
 * 1. Check if email already exists
 * 2. Create user aggregate
 * 3. Save
 * 4. Publish events
 * 5. Return response
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CreateUserApplicationService implements ApplicationService<CreateUserRequest, UserResponse> {
    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;
    private final UserMapper userMapper;


    @Override
    @Transactional // Ensuring DB save and Event publish happen in one task
    public UserResponse execute(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());

        // Check if email already exists
        Email email = Email.of(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            log.warn("User registration failed: Email {} already registered", request.getEmail());
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        // Create user aggregate
        User user = User.create(
                request.getKeycloakId(),
                email,
                Phone.of(request.getPhone()),
                request.getFirstName(),
                request.getLastName()
        );

        // Save
        userRepository.save(user);
        log.info("New user created with ID: {}", user.getId());

        // Publish events
        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        // Return Response
        return userMapper.toResponse(user);
    }
}
