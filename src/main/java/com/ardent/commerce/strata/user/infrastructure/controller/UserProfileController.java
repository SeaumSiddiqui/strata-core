package com.ardent.commerce.strata.user.infrastructure.controller;

import com.ardent.commerce.strata.user.application.command.DeleteUserCommand;
import com.ardent.commerce.strata.user.application.command.UpdateUserCommand;
import com.ardent.commerce.strata.user.application.command.UpdateUserEmailCommand;
import com.ardent.commerce.strata.user.application.command.UpdateUserPasswordCommand;
import com.ardent.commerce.strata.user.application.dto.UpdateUserEmailRequest;
import com.ardent.commerce.strata.user.application.dto.UpdateUserPasswordRequest;
import com.ardent.commerce.strata.user.application.dto.UpdateUserProfileRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.application.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/profile")
@RestController
public class UserProfileController {
    private final FindUserApplicationService findUserService;
    private final UpdateUserProfileApplicationService updateUserService;
    private final UpdateUserEmailApplicationService updateEmailService;
    private final UpdateUserPasswordApplicationService updatePasswordService;
    private final DeleteUserApplicationService deleteUserService;

    @GetMapping
    public ResponseEntity<UserResponse> getCurrentUserProfile(@AuthenticationPrincipal Jwt jwt) {
        log.info("GET /api/users/profile - Fetching user profile");

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse response = findUserService.findByKeycloakId(keycloakId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateUserProfile (@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody UpdateUserProfileRequest request) {
        log.info("PUT /api/users/profile - Updating user profile");

        UpdateUserCommand command = new UpdateUserCommand(
                UUID.fromString(jwt.getSubject()),
                request.firstName(),
                request.lastName(),
                request.phone());

        UserResponse response = updateUserService.execute(command);
        return ResponseEntity.ok(response);
    }

    /**
     * Update current users email. Self update (authenticated endpoint).
     */
    @PutMapping("/email")
    public ResponseEntity<UserResponse> updateUserEmail(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserEmailRequest request) {
        log.info("PUT /api/users/email - Updating user email");

        UpdateUserEmailCommand command = new UpdateUserEmailCommand(
                UUID.fromString(jwt.getSubject()),
                request.email(),
                request.password());

        UserResponse response = updateEmailService.execute(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<UserResponse> updateUserPassword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserPasswordRequest request) {
        log.info("PUT api/users/password - Updating user password");

        UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(
                UUID.fromString(jwt.getSubject()),
                request.currentPassword(),
                request.newPassword());

        UserResponse response = updatePasswordService.execute(command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        log.info("DELETE /api/users/delete - Deleting user");

        DeleteUserCommand command = new DeleteUserCommand(UUID.fromString(jwt.getSubject()));

        deleteUserService.execute(command);
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
