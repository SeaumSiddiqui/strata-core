package com.ardent.commerce.strata.user.infrastructure.controller;

import com.ardent.commerce.strata.user.application.dto.*;
import com.ardent.commerce.strata.user.application.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static java.util.Objects.hash;

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
    private final UpdateUserEmailApplicationService updateEmailService;
    private final UpdateUserPasswordApplicationService updatePasswordService;
    private final ManageUserRoleApplicationService userRoleService;
    private final DeleteUserApplicationService deleteUserService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.debug("Creating user with email hash: {}", hash(request.email()));

        UserResponse response = createUserService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get current users profile. Self view (authenticated endpoint).
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getCurrentUserProfile(@AuthenticationPrincipal Jwt jwt) {
        log.info("GET /api/users/profile - Fetching user profile");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = findUserService.findByKeycloakId(keycloakId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get any user by ID (admin endpoint).
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        log.info("GET /api/users/{} - Fetching user by ID", userId);

        UserResponse response = findUserService.findById(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update current user profile. Self update (authenticated endpoint).
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateUserProfile (@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody UpdateUserProfileRequest request) {
        log.info("PUT /api/users/profile - Updating user profile");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = updateUserService.execute(keycloakId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update current users email. Self update (authenticated endpoint).
     */
    @PutMapping("/email")
    public ResponseEntity<UserResponse> updateUserEmail(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserEmailRequest request) {
        log.info("PUT /api/users/email - Updating user email");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = updateEmailService.execute(keycloakId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<UserResponse> updateUserPassword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserPasswordRequest request) {
        log.info("PUT api/users/password - Updating user password");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = updatePasswordService.execute(keycloakId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/role/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignUserRole(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserRoleRequest request) {
        log.info("PUT /api/users/role/assign - Assign new role to user");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = userRoleService.assignUserRole(keycloakId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/role/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserRole(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserRoleRequest request) {
        log.info("PUT /api/users/role/remove - Remove and existing role from user");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = userRoleService.removeUserRole(keycloakId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        log.info("DELETE /api/users/delete - Deleting user");
        UUID keycloakId = UUID.fromString(jwt.getSubject());

        deleteUserService.execute(keycloakId);
        return ResponseEntity.noContent().build();
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
