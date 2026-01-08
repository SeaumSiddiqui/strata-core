package com.ardent.commerce.strata.user.infrastructure.controller;

import com.ardent.commerce.strata.user.application.dto.CreateUserRequest;
import com.ardent.commerce.strata.user.application.dto.UpdateUserProfileRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.application.service.CreateUserApplicationService;
import com.ardent.commerce.strata.user.application.service.FindUserApplicationService;
import com.ardent.commerce.strata.user.application.service.UpdateUserProfileApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller: User endpoints.
 * Converts HTTP requests to application service calls.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final CreateUserApplicationService createUserService;
    private final FindUserApplicationService findUserService;
    private final UpdateUserProfileApplicationService updateUserService;

    /**
     * POST /api/users
     * Create new user.
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/users - Creating user with email: {}", request.email());

        UserResponse response = createUserService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/users/profile
     * Get current users profile (authenticated endpoint).
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getCurrentUserProfile(@AuthenticationPrincipal Jwt jwt) {
        log.info("GET /api/users/profile - Fetching user profile");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = findUserService.findByKeycloakId(keycloakId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/{userId}
     * Get user by ID (admin endpoint).
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        log.info("GET /api/users/{} - Fetching user by ID", userId);

        UserResponse response = findUserService.findById(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/users/profile
     * Update current user profile.
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateUserProfile (@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody UpdateUserProfileRequest request) {
        log.info("PUT /api/users/profile - Updating user profile");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = updateUserService.execute(keycloakId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/health
     * Endpoint for health checking.
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "User service is running"));
    }

    record HealthResponse(String status, String message) {}

}
