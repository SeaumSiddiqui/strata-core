package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.UserId;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Application Service: On find user
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FindUserApplicationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findById(UUID userId) {
        log.debug("Fetching user by ID: {}", userId);
        return userRepository.findById(UserId.of(userId))
                .map(userMapper::toResponse)
                .orElseThrow(()-> new RuntimeException("User not found with ID: " + userId));
    }

    public UserResponse findByKeycloakId(UUID keycloakId) {
        log.debug("Fetching user by Keycloak ID: {}", keycloakId);

        return userRepository.findByKeycloakId(keycloakId)
                .map(userMapper::toResponse)
                .orElseThrow(()-> new RuntimeException("User not found with Keycloak ID: " + keycloakId));
    }
}
