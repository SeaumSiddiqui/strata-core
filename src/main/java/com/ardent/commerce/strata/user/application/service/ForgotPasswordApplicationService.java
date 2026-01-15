package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.user.application.dto.ForgotPasswordRequest;
import com.ardent.commerce.strata.user.application.dto.ForgotPasswordResponse;
import com.ardent.commerce.strata.user.infrastructure.identity.keycloak.KeycloakUserIdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ForgotPasswordApplicationService {
    private final KeycloakUserIdentityService keycloakUserIdentityService;

    public ForgotPasswordResponse execute(ForgotPasswordRequest request) {
        String email = request.email().toLowerCase().trim();
        log.info("Forgot password request for email: {}", email);

        try {
            keycloakUserIdentityService.sendPasswordResetEmail(email); // Internally checks if user exists

            return new ForgotPasswordResponse(
                    "Password reset instructions sent to email",
                    "If an account exists with this email, you will receive password reset instructions"
            );
        } catch (Exception e) {
            log.error("Error processing forgot password request: {}", email, e);

            return new ForgotPasswordResponse(
                    "Password reset instructions sent to email",
                    "If an account exists with this email, you will receive password reset instructions"
            );
        }
    }

}
